package io.github.zap.zombiesplugin.player;

import io.github.zap.zombiesplugin.hotbar.HotbarManager;
import org.bukkit.entity.Player;

public class User {
    private Player player;

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
}
