package io.github.zap.zombiesplugin.provider;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.gsonfire.PostProcessor;

public class CustomClassPostProcessor implements PostProcessor {
    protected final ConfigFileManager manager;

    public CustomClassPostProcessor(ConfigFileManager manager) {
        this.manager = manager;
    }

    @Override
    public void postDeserialize(Object o, JsonElement jsonElement, Gson gson) {
        // Don't need to do anything here
    }

    @Override
    public void postSerialize(JsonElement jsonElement, Object o, Gson gson) {
        if (o instanceof ICustomSerializerIdentity) {
            String familyName = manager.getCustomClassFriendlyName(((ICustomSerializerIdentity)o).getClass());
            if (familyName != null) {
                jsonElement.getAsJsonObject().add("signature", new JsonPrimitive(familyName));
            }
        }
    }
}
