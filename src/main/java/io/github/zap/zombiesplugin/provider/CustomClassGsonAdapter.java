package io.github.zap.zombiesplugin.provider;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;

import java.lang.reflect.Type;

public class CustomClassGsonAdapter implements JsonDeserializer<ICustomSerializerIdentity>, JsonSerializer<ICustomSerializerIdentity> {
    private Gson jsonParser = new Gson();
    private final ConfigFileManager manager;

    public CustomClassGsonAdapter(ConfigFileManager manager) {
        this.manager = manager;
    }

    @Override
    public ICustomSerializerIdentity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            String classSignature = json.getAsJsonObject().getAsJsonPrimitive("signature").getAsString();
            Class<? extends ICustomSerializerIdentity> cc =  manager.getCustomClass(classSignature);
            return jsonParser.fromJson(json, cc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public JsonElement serialize(ICustomSerializerIdentity src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            Class<? extends ICustomSerializerIdentity> cc = (Class<? extends ICustomSerializerIdentity>) typeOfSrc;
            String classSignature = manager.getCustomClassFriendlyName(cc);
            JsonElement element =  jsonParser.toJsonTree(src);
            JsonObject object =  element.getAsJsonObject();
            object.add("signature", jsonParser.toJsonTree(classSignature));


            // Testing
            if (classSignature == null) {
                throw new Exception();
            }

            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
