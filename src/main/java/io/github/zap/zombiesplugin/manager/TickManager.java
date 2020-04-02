package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class TickManager {
    private final long interval;
    private final double tps;

    private final BukkitRunnable task;
    private HashSet<ITickable> tickables;

    private boolean started = false;

    public TickManager(long interval) {
        this.interval = interval;
        tickables = new HashSet<>();
        tps = 20D / interval;

        task = new BukkitRunnable() {
            @Override
            public void run(){
                started = true;
                for(ITickable tickable : tickables) {
                    tickable.doTick();

                    if(isCancelled()) {
                        started = false;
                        break;
                    }
                }
            }
        };
    }

    public void start() {
        if(!started) {
            task.runTaskTimer(ZombiesPlugin.instance, 0L, interval);
        }
    }

    public void stop() {
        if(started) {
            task.cancel();
        }
    }

    public boolean isRunning() { return started; }

    public void register(ITickable tickable) {
        tickables.add(tickable);
    }

    public boolean tryRegister(ITickable tickable) {
        if(!tickables.contains(tickable)) {
            tickables.add(tickable);
            return true;
        }
        return false;
    }

    public boolean canRegister(ITickable tickable) {
        return tickables.contains(tickable);
    }

    public double getTps() { return tps; }
}
