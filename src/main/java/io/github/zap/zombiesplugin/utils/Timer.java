package io.github.zap.zombiesplugin.utils;

/**
 * Used to call a function every X number of seconds. The start(), stop() and restart() methods are intended to be
 * entirely thread-safe so they can be accessed in a multithreaded context.
 *
 * The Runnable instance that is passed to the constructor will be executed once every specified number of milliseconds,
 * or indefinitely if repeatForever is true.
 */
public class Timer<T> implements Runnable {
    private Runnable action;
    private int interval;
    private boolean repeatForever;
    private int repeatTimes = -1;

    private Thread thread;
    private volatile boolean isRunning = false;
    private volatile boolean stopFlag = false;

    //empty object used for synchronizing thread access
    private final Object lock = new Object();

    public Timer(Runnable action, int intervalMilliseconds, boolean repeatForever) {
        this.action = action;
        interval = intervalMilliseconds;
        this.repeatForever = repeatForever;
    }

    public Timer(Runnable action, int intervalMilliseconds, int repeatTimes) {
        this.action = action;
        interval = intervalMilliseconds;
        repeatForever = false;
        this.repeatTimes = repeatTimes;
    }

    public void start() {
        synchronized (lock) { //we don't want more than one thread to access this simultaneously
            if(isRunning) { //restart the if we're already running
                try {
                    stopFlag = true;
                    thread.join();
                } catch (InterruptedException e) {
                    onInterrupt();
                    return; //don't start a new thread
                }
            }

            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        synchronized (lock) {
            if(!isRunning) return;
            try {
                stopFlag = true;
                thread.join(); //wait for the task thread to stop
            } catch (InterruptedException e) {
                onInterrupt();
            }
        }
    }

    @Override
    public void run() {
        isRunning = true;
        int repeatCount = 0;
        while(!stopFlag) {
            action.run(); //runs the function specified on this object's creation

            try {
                Thread.sleep(interval);

                repeatCount++;
                if(!repeatForever && repeatTimes == repeatCount) stopFlag = true;
            } catch (InterruptedException e) {
                onInterrupt();
            }
        }

        stopFlag = false; //reset the flag
        isRunning = false; //we technically haven't stopped running but we are very close to doing so
    }

    /*
    If this ever gets called, very bad things have happened.

    This function should log an error message and perform any necessary cleanup tasks.
     */
    private void onInterrupt() {

    }
}