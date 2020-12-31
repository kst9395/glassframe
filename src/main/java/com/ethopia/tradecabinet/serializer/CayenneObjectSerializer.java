package com.ethopia.tradecabinet.serializer;

import io.vertx.core.json.JsonObject;
import org.apache.cayenne.Cayenne;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.map.ObjEntity;

import java.util.Collection;
import java.util.Set;

public class CayenneObjectSerializer {

    public static JsonObject toJson(DataObject cayenneDataObject) {
        ObjEntity objEntity = Cayenne.getObjEntity(cayenneDataObject);
        Set<String> keys = objEntity.getAttributeMap().keySet();
        JsonObject jsonObject = new JsonObject();
        keys.forEach(key -> {
            Object value = cayenneDataObject.readProperty(key);
            if (!(value instanceof DataObject || value instanceof Collection)) {
                jsonObject.put(key, cayenneDataObject.readProperty(key));
            }
        });
        return jsonObject;
    }

}
