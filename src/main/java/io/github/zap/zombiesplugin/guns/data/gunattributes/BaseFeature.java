package io.github.zap.zombiesplugin.guns.data.gunattributes;

import java.util.Hashtable;

// provides easy way to access feature data for yaml imported guns
// TODO: Propagate exceptions and show error to the console in all subclasses of this class
public abstract class BaseFeature {
    private Hashtable<String, String> customFeatureValues;

    public Hashtable<String, String> getCustomFeatureValues() {
        return customFeatureValues;
    }

    protected String getFeatureValue(String name) {
        return customFeatureValues.get(name);
    }

    // Zombies Editor Plugin might use this methods z
    public void setFeatureValue(String name, String value) {
        customFeatureValues.put(name, value);
    }

    protected void updateFeatureValue(String name, String value) {
        customFeatureValues.replace(name, value);
    }

    public BaseFeature(Hashtable<String, String> customFeatureValues) {
        this.customFeatureValues = customFeatureValues;
    }

    public abstract String getFeatureName();
}
