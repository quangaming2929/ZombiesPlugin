package io.github.zap.zombiesplugin.events;

public class ValueRequestedEventArgs extends EventArgs {
    public final Object originalValue;
    public Object modifiedValue;
    public final String valueName;

    public ValueRequestedEventArgs(Object originalValue, String valueName) {
        this.originalValue = originalValue;
        this.modifiedValue = originalValue;
        this.valueName = valueName;
    }
}
