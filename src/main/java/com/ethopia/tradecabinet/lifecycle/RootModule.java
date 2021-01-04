package com.ethopia.tradecabinet.lifecycle;

import com.google.inject.AbstractModule;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.WorkerExecutor;
import io.vertx.reactivex.ext.web.common.template.TemplateEngine;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;

public class RootModule extends AbstractModule {
    private Vertx vertx;
    private WorkerExecutor workerExecutor;
    private ServerRuntime serverRuntime;
    private TemplateEngine templateEngine;

    public RootModule(Vertx vertx, WorkerExecutor workerExecutor, ServerRuntime serverRuntime, TemplateEngine templateEngine) {
        this.vertx = vertx;
        this.workerExecutor = workerExecutor;
        this.serverRuntime = serverRuntime;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void configure() {
        bind(Vertx.class).toInstance(vertx);
        bind(WorkerExecutor.class).toInstance(workerExecutor);
        bind(ServerRuntime.class).toInstance(serverRuntime);
        bind(ObjectContext.class).toProvider(() -> serverRuntime.newContext());
        bind(TemplateEngine.class).toInstance(templateEngine);


    }
}
