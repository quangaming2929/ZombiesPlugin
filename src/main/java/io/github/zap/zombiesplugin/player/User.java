package io.github.zap.zombiesplugin.player;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.equipments.guns.GunObjectGroup;
import io.github.zap.zombiesplugin.hotbar.HotbarManager;
import io.github.zap.zombiesplugin.equipments.meele.MeeleObjectGroup;
import io.github.zap.zombiesplugin.equipments.perks.PerkObjectGroup;
import io.github.zap.zombiesplugin.equipments.skills.SkillObjectGroup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class User {
    private Player player;
    private HotbarManager hotbar;
    private PlayerState state;

    // TODO: make a player statistic class
    private int gold;
    private int kills;

    public User(Player player) {
        this.gold = 1500; // TODO: REMOVE THIS AFTER TESTING
        this.player = player;
        this.hotbar = new HotbarManager(player);

        // TODO: We might set up the hotbar layout by GameSettings
        this.hotbar.addGroup("MeleeGroup", new MeeleObjectGroup(false), 0);
        this.hotbar.addGroup("GunGroup", new GunObjectGroup(false), 1,2,3);
        this.hotbar.addGroup("SkillGroup", new SkillObjectGroup(false), 4);
        this.hotbar.addGroup("PerkGroup", new PerkObjectGroup(true), 6,7,8 );
    }

    public Player getPlayer() {
        return player;
    }

    public MeeleObjectGroup getMeleeGroup() {
        return (MeeleObjectGroup) getHotbar().getGroup("MeleeGroup");
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

    public void setGold (int amount) {
        gold = amount;
    }

    public void addGold (int amount) {
        gold += amount;
    }

    public PlayerState getState(){
        return state;
    }

    public void setState(PlayerState newState) {
        // TODO: Do actual implementation here @ Tahmid
        // add more cases here
        if(getState() != newState) {
            if (getState() == PlayerState.KNOCKED_DOWN) {
                if (newState == PlayerState.ALIVE) {
                    Bukkit.broadcastMessage(player.getDisplayName() + " revived from knock down");
                }
            } else if (getState() == PlayerState.DEAD) {
                Bukkit.broadcastMessage(player.getDisplayName() + " revived from death");
            } else if (getState() == PlayerState.ALIVE) {
                getPlayer().closeInventory();
            }
        }

    }
}
