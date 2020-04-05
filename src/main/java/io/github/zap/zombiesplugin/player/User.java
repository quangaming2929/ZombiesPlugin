package io.github.zap.zombiesplugin.player;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.hotbar.HotbarManager;
import io.github.zap.zombiesplugin.manager.UserManager;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.Window;
import io.github.zap.zombiesplugin.utils.MathUtils;
import io.github.zap.zombiesplugin.equipments.guns.GunObjectGroup;
import io.github.zap.zombiesplugin.equipments.meele.MeeleObjectGroup;
import io.github.zap.zombiesplugin.equipments.perks.PerkObjectGroup;
import io.github.zap.zombiesplugin.equipments.skills.SkillObjectGroup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class User {
    private UserManager manager;
    private Player player;

    private int tick = 0;

    /**
     * Manager gun-related
     */
    private GunUser gunUser;
    private HotbarManager hotbar;
    private PlayerState state = PlayerState.ALIVE;

    // TODO: make a player statistic class
    private int gold;
    private int kills;

    public User(UserManager manager, Player player) {
        this.manager = manager;
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

    public void userTick() {
        tick++;
        if(tick % 2 == 0) {
            boolean repairTick = tick == 10;
            for(Room room : manager.getGameManager().getSettings().getGameMap().getRooms()) {
                if(room.isOpen()) {
                    for(Window window : room.getWindows()) {
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
    }
}
