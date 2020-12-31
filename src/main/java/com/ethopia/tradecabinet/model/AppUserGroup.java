package com.ethopia.tradecabinet.model;

import com.ethopia.tradecabinet.model.auto._AppUserGroup;
import com.ethopia.tradecabinet.serializer.CayenneObjectSerializer;
import io.vertx.core.json.JsonObject;

public class AppUserGroup extends _AppUserGroup {

    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_GROUP_NAME="Unassigned";

    public JsonObject toJson() {
        return CayenneObjectSerializer.toJson(this);
    }

}
