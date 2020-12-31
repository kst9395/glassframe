package com.ethopia.tradecabinet.model;

import com.ethopia.tradecabinet.model.auto._AppFeature;
import com.ethopia.tradecabinet.serializer.CayenneObjectSerializer;
import io.vertx.core.json.JsonObject;

public class AppFeature extends _AppFeature {

    private static final long serialVersionUID = 1L;


    public JsonObject toJson() {
        return CayenneObjectSerializer.toJson(this);
    }

}
