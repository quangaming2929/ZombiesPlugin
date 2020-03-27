package io.github.zap.zombiesplugin.events;

import java.util.ArrayList;
import java.util.List;

/**
 * encapsulate IEventDelegate
 * @param <E> Event data type
 */
public class EventHandler <E extends EventArgs> {
    private List<IEventDelegate<E>> subscribers = new ArrayList<>();

    /**
     * Register an event delegate.
     * @param delegate the event delegate to register
     */
    public void registerEvent(IEventDelegate<E> delegate) {
        subscribers.add(delegate);
    }

    /**
     * Unregister an event delegate.
     * @param delegate the event delegate to register
     */
    public void unregisterEvent(IEventDelegate<E> delegate) {
        subscribers.remove(delegate);
    }

    /**
     * Invoke to all event delegate that register this event hander
     * @param sender The event sender
     * @param args Event argument
     */
    public void invoke(Object sender, E args) {
        for (IEventDelegate<E> d : subscribers) {
            d.execute(sender, args);
        }
    }

    /**
     * Return a list of event delegates that registers this event handler
     * @return A list of registered event delegate
     */
    public List<IEventDelegate<E>> getRegisteredEvent() {
        return subscribers;
    }
}