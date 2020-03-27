package io.github.zap.zombiesplugin.player;

import io.github.zap.zombiesplugin.guns.GunObjectGroup;
import io.github.zap.zombiesplugin.hotbar.HotbarManager;
import io.github.zap.zombiesplugin.hotbar.HotbarObject;
import io.github.zap.zombiesplugin.perks.PerkObjectGroup;
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
        this.hotbar.addGroup("GunGroup", new GunObjectGroup(false), 1,2,3);
        this.hotbar.addGroup("PerkGroup", new PerkObjectGroup(true), 6,7,8 );
    }

    public Player getPlayer() {
        return player;
    }

    public GunObjectGroup getGunGroup() {
        return (GunObjectGroup) getHotbar().getGroup("GunGroup");
    }

    public PerkObjectGroup getPerkGroup() {
        return (PerkObjectGroup) getHotbar().getGroup("PerkGroup");
    }

    public HotbarManager getHotbar() {
        return hotbar;
    }

    public int getGold() {
        return gold;
    }
}
