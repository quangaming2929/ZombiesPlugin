package io.github.zap.zombiesplugin.player;

import io.github.zap.zombiesplugin.hotbar.HotbarManager;
import io.github.zap.zombiesplugin.manager.PlayerManager;
import io.github.zap.zombiesplugin.map.Window;
import io.github.zap.zombiesplugin.utils.MathUtils;
import org.bukkit.entity.Player;

public class User {
    private Player player;
    private int repairTicks = 0;

    /**
     * Manager gun-related
     */
    private GunUser gunUser;
    private HotbarManager hotbar;
    private int gold;

    public User(Player player) {
        this.player = player;
        this.hotbar = new HotbarManager(player);
        this.gunUser = new GunUser(this, 2, 1,2,3);
    }

    public Player getPlayer() {
        return player;
    }

    public GunUser getGunUser() {
        return gunUser;
    }

    public HotbarManager getHotbar() {
        return hotbar;
    }

    public int getGold() {
        return gold;
    }

    public void doTick(PlayerManager manager) {
        if(player.isSneaking()) {
            for(Window window : manager.getGameManager().getMap().getWindows()) {
                if(!window.getBreaking()) {
                    if(MathUtils.manhattanDistance(window.getOrigin(), player.getLocation()) <= 5) {
                        repairTicks++;
                        if(repairTicks == 4) {
                            window.repairWindow(manager.getGameManager());
                            repairTicks = 0;
                        }
                        break;
                    }
                    else repairTicks = 0;
                }
                else repairTicks = 0;
            }
        }
        else {
            repairTicks = 0;
        }
    }
}
