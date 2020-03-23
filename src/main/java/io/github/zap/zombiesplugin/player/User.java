package io.github.zap.zombiesplugin.player;

import io.github.zap.zombiesplugin.guns.GunObjectGroup;
import io.github.zap.zombiesplugin.hotbar.HotbarManager;
import io.github.zap.zombiesplugin.hotbar.HotbarObject;
import org.bukkit.entity.Player;

import java.util.List;

public class User {
    private Player player;
    private HotbarManager hotbar;

    private int gold;

    public User(Player player) {
        this.player = player;
        this.hotbar = new HotbarManager(player);

        // TODO: We might set up the hotbar layout by GameSettings
        this.hotbar.addGroup("GunGroup", new GunObjectGroup(), 1,2,3);
    }

    public Player getPlayer() {
        return player;
    }

    public GunObjectGroup getGunGroup() {
        return (GunObjectGroup) getHotbar().getGroup("GunGroup");
    }

    public HotbarManager getHotbar() {
        return hotbar;
    }

    public int getGold() {
        return gold;
    }
}
