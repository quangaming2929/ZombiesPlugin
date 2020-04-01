package io.github.zap.zombiesplugin.provider;

import com.google.gson.JsonElement;
import io.gsonfire.TypeSelector;

public class CustomClassSelector implements TypeSelector {
    protected final ConfigFileManager manager;

    public CustomClassSelector(ConfigFileManager manager) {
        this.manager = manager;
    }

    @Override
    public Class getClassForElement(JsonElement jsonElement) {
        JsonElement sigElement = jsonElement.getAsJsonObject().get("signature");
        if(sigElement != null) {
            String sig = sigElement.getAsString();
            return manager.getCustomClass(sig);
        }

        return null;
    }
}
