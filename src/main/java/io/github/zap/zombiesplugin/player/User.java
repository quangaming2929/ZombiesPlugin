package io.github.zap.zombiesplugin.player;

import io.github.zap.zombiesplugin.guns.GunObjectGroup;
import io.github.zap.zombiesplugin.hotbar.HotbarManager;
import io.github.zap.zombiesplugin.hotbar.HotbarObject;
import io.github.zap.zombiesplugin.meele.MeeleObjectGroup;
import io.github.zap.zombiesplugin.perks.PerkObjectGroup;
import io.github.zap.zombiesplugin.skills.SkillObjectGroup;
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
        this.hotbar.addGroup("MeeleGroup", new MeeleObjectGroup(false), 0);
        this.hotbar.addGroup("GunGroup", new GunObjectGroup(false), 1,2,3);
        this.hotbar.addGroup("SkillGroup", new SkillObjectGroup(false), 4);
        this.hotbar.addGroup("PerkGroup", new PerkObjectGroup(true), 6,7,8 );
    }

    public Player getPlayer() {
        return player;
    }

    public MeeleObjectGroup getMeeleGroup() {
        return (MeeleObjectGroup) getHotbar().getGroup("MeeleGroup");
    }

    public GunObjectGroup getGunGroup() {
        return (GunObjectGroup) getHotbar().getGroup("GunGroup");
    }

    public SkillObjectGroup getSkillGroup() {
        return (SkillObjectGroup) getHotbar().getGroup("SkillGroup");
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
