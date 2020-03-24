package io.github.zap.zombiesplugin.utils.event;

/**
 * Represent a event lambda invokes by event handler
 * @param <E> Type of event data
 */
@FunctionalInterface
public interface IEventDelegate<E extends EventArgs> {
    /**
     * This method use to create simple event lambda expression
     * @param sender The event sender
     * @param args Event data
     */
    void execute (Object sender, E args);
}