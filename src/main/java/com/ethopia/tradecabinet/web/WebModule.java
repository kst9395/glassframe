package com.ethopia.tradecabinet.web;

import com.ethopia.tradecabinet.WebHandlerMetadata;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.List;

public class WebModule extends AbstractModule {

    private List<WebHandlerMetadata> metadataList;
    private static final Logger logger = LoggerFactory.getLogger(WebModule.class.getName());
    public WebModule(List<WebHandlerMetadata> metadataList) {
        this.metadataList = metadataList;
    }

    @Override
    protected void configure() {

        metadataList.stream().forEach(metadata -> {
            WebHandler webHandler = metadata.getRoute();
            String componentName = webHandler.name();
            logger.info("Registering component:"+componentName+" to guice");
            bind(Handler.class).annotatedWith(Names.named(componentName)).to(metadata.getImpl());
        });

    }
}
