package io.github.zap.zombiesplugin.player;

import io.github.zap.zombiesplugin.hotbar.HotbarManager;
import io.github.zap.zombiesplugin.manager.PlayerManager;
import io.github.zap.zombiesplugin.map.Window;
import io.github.zap.zombiesplugin.utils.MathUtils;
import org.bukkit.entity.Player;

public class User {
    private PlayerManager manager;
    private Player player;

    private int tick = 0;

    /**
     * Manager gun-related
     */
    private GunUser gunUser;
    private HotbarManager hotbar;
    private int gold;

    public User(PlayerManager manager, Player player) {
        this.manager = manager;
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

    public void userTick() {
        tick++;
        if(tick % 2 == 0) {
            boolean repairTick = tick == 10;
            for(Window window : manager.getGameManager().getSettings().getGameMap().getWindows()) {
                if(repairTick) { //one second interval
                    if(MathUtils.manhattanDistance(window.getWindowBounds().getCenter(), player.getLocation()) <= 6) {
                        if(player.isSneaking()) {
                            window.repairWindow();
                            tick = 0;
                            break;
                        }
                        //todo: display text that says "press sneak to repair"
                    }
                    tick = 0;
                }

                //fifth of a second second interval for interior bounds check
                if(window.getInteriorBounds().isInBound(player.getLocation())) {
                    player.teleport(window.getSpawnPoint().getTarget());
                    break;
                }
            }
        }
    }
}
