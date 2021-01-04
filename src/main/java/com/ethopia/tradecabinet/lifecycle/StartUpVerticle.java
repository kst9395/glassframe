package com.ethopia.tradecabinet.lifecycle;

import com.ethopia.tradecabinet.WebHandlerMetadata;
import com.ethopia.tradecabinet.app.auth.AuthModule;
import com.ethopia.tradecabinet.app.crm.CRMModule;
import com.ethopia.tradecabinet.app.management.user.UserManagementModule;
import com.ethopia.tradecabinet.security.SecurityModule;
import com.ethopia.tradecabinet.web.WebHandler;
import com.ethopia.tradecabinet.web.WebModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import io.reactivex.Maybe;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.templ.pebble.PebbleTemplateEngine;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.WorkerExecutor;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Route;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.common.template.TemplateEngine;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.ResponseContentTypeHandler;
import io.vertx.reactivex.ext.web.handler.SessionHandler;
import io.vertx.reactivex.ext.web.sstore.LocalSessionStore;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.cayenne.configuration.server.ServerRuntime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


public class StartUpVerticle extends AbstractVerticle {


    private Injector injector;

    private HttpServer httpServer;

    private WorkerExecutor workerExecutor;

    private ServerRuntime serverRuntime;
    private TemplateEngine templateEngine;

    private static final Logger logger = LoggerFactory.getLogger(StartUpVerticle.class.getName());


    private Maybe<Boolean> initializeCayenne() {
        serverRuntime = ServerRuntime.builder().addConfig("cayenne-project.xml").build();

        return Maybe.just(true);
    }

    private Maybe<Boolean> initializeWorkerExecutor() {
        this.workerExecutor = vertx.createSharedWorkerExecutor(config().getString(AppConfiguration.APP_WORKER_EXECUTOR_POOL_NAME.key()), config().getInteger(AppConfiguration.APP_WORKER_EXECUTOR_POOL_SIZE.key()));
        return Maybe.just(true);
    }


    private Maybe<Boolean> executeMigration() {
        return vertx.rxExecuteBlocking(promise -> {
            logger.info("Initializing migration");
            try {
                Class.forName(config().getString(AppConfiguration.APP_JDBC_DRIVER_CLASS.key()));

                try (Connection connection = DriverManager.getConnection(config().getString(AppConfiguration.APP_JDBC_URL.key()), config().getString(AppConfiguration.APP_JDBC_USERNAME.key()), config().getString(AppConfiguration.APP_JDBC_PASSWORD.key()))) {
                    Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                    Liquibase liquibase = new Liquibase(config().getString(AppConfiguration.APP_DATABASE_MIGRATION_MASTER_FILE.key()), new ClassLoaderResourceAccessor(getClass().getClassLoader()), database);
                    liquibase.update(new Contexts(), new LabelExpression());
                    promise.complete(true);
                    logger.info("Migration complete");
                } catch (Exception e) {
                    logger.error("Migration Failed", e);
                    throw e;
                }
            } catch (Exception e) {
                promise.fail(e);
            }
        });

    }


    //Setup Guice Modules
    private Maybe<Injector> initializeModules(List<WebHandlerMetadata> metadataList) {
        return vertx.rxExecuteBlocking(promise -> {
            try {
                injector = Guice.createInjector(new RootModule(vertx, workerExecutor, serverRuntime, templateEngine), new WebModule(metadataList), new CRMModule(), new SecurityModule(), new AuthModule(), new UserManagementModule());
                promise.complete(injector);
            } catch (Exception e) {
                logger.error("Failed to initialize module", e);
                promise.fail(e);
            }
        });

    }

    private Maybe<List<WebHandlerMetadata>> discoverWebHandlers() {

        return vertx.rxExecuteBlocking(promise -> {
            logger.info("Scanning classpath for web handlers");
            List<WebHandlerMetadata> metadataList = new ArrayList<>();
            try (ScanResult scanResult = new ClassGraph().enableAllInfo().scan()) {
                ClassInfoList classInfos = scanResult.getClassesWithAnnotation(WebHandler.class.getName());
                classInfos.forEach(classInfo -> {
                    AnnotationInfo annotationInfo = classInfo.getAnnotationInfo(WebHandler.class.getName());
                    WebHandler webHandler = ((WebHandler) annotationInfo.loadClassAndInstantiate());
                    Class<Handler> handlerClass = classInfo.loadClass(Handler.class);
                    WebHandlerMetadata metadata = new WebHandlerMetadata();
                    metadata.setImpl(handlerClass);
                    metadata.setRoute(webHandler);
                    metadataList.add(metadata);
                });
                logger.info("Discovery complete, total handler found: " + metadataList.size());
                promise.complete(metadataList);

            } catch (Exception e) {
                logger.error("Failed to discover web handler", e);
                promise.fail(e);
            }

        });
    }


    private void handleRoute(Function<String, Route> routeFunc, String route, Handler<RoutingContext> handler) {
        logger.info(route + " handled by " + handler.getClass().getName());
        routeFunc.apply(route).handler(handler);
    }

    //Deploy services like web controller
    private Maybe<HttpServer> initializeWebApplication(List<WebHandlerMetadata> metadataList) {
        HttpServerOptions serverOptions = new HttpServerOptions().setPort(8080);
        httpServer = vertx.createHttpServer(serverOptions);
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create(false))
                .handler(ResponseContentTypeHandler.create())
                .handler(SessionHandler.create(LocalSessionStore.create(vertx)));
        return vertx.rxExecuteBlocking(promise -> {
            try {
                metadataList.forEach(metadata -> {
                    WebHandler webHandler = metadata.getRoute();
                    String[] path = webHandler.path();
                    String componentName = webHandler.name();
                    Handler<RoutingContext> handler = injector.getInstance(Key.get(Handler.class, Names.named(componentName)));
                    switch (webHandler.method()) {
                        case GET:
                            Arrays.stream(path).forEach(p ->
                                    handleRoute(router::get, p, handler)
                            );
                            break;
                        case POST:
                            Arrays.stream(path).forEach(p ->
                                    handleRoute(router::post, p, handler)
                            );
                            break;
                        case PUT:
                            Arrays.stream(path).forEach(p ->
                                    handleRoute(router::put, p, handler)
                            );
                            break;
                        case DELETE:
                            Arrays.stream(path).forEach(p ->
                                    handleRoute(router::delete, p, handler)
                            );
                            break;
                        default:
                            break;
                    }
                });
                promise.complete(httpServer.requestHandler(router));

            } catch (Exception e) {
                logger.error("Failed to register web route", e);
                promise.fail(e);
            }
        });


    }


    @Override
    public void start() throws Exception {
        this.templateEngine = TemplateEngine.newInstance(PebbleTemplateEngine.create(vertx.getDelegate()));
        initializeWorkerExecutor()
                .flatMap(success ->
                        //run migration script (liquibase) if any
                        executeMigration()

                )
                .flatMap(success -> initializeCayenne())
                .flatMap(nothing ->
                        //wire up guice module
                        discoverWebHandlers()
                                .flatMap(metadataList ->
                                        initializeModules(metadataList)
                                                .flatMap(v ->
                                                        initializeWebApplication(metadataList)
                                                )
                                )
                )
                .doOnSuccess(server ->
                        server.rxListen()
                                .doOnSuccess(s -> logger.info("http server listening to port: " + server.actualPort()))
                                .doOnError(e -> {
                                    logger.error("Failed to start http server", e);
                                    vertx.close();
                                }).subscribe()

                )
                .doOnError(e -> {
                    logger.error("Error starting up verticle", e);
                    vertx.close();
                })
                .subscribe();


    }

    @Override
    public void stop() throws Exception {

        if (workerExecutor != null) {
            workerExecutor.close();
        }
        if (httpServer != null) {
            httpServer.close();
        }


    }
}
