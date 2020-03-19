package io.github.zap.zombiesplugin.guns.data.gunattributes;

import java.util.Hashtable;

// provides easy way to access feature data for yaml imported guns
// TODO: Propagate exceptions and show error to the console in all subclasses of this class
public abstract class GunAttribute {
    private Hashtable<String, String> InternalValues;

    public Hashtable<String, String> getInternalValues() {
        return InternalValues;
    }

    protected <T> T getAttribute(String name) {
        return (T)InternalValues.get(name);
    }

    // Zombies Editor Plugin might use this methods z
    public void setAttribute(String name, String value) {
        InternalValues.put(name, value);
    }

    protected <T> void updateAttribute(String name, T value) {
        InternalValues.replace(name, (String) value);
    }

    public GunAttribute(Hashtable<String, String> attributes) {
        this.InternalValues = attributes;
    }

    public abstract String getBehaviourName();
}
