package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.map.Window;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.utils.MathUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class PlayerManager implements Listener {
    private GameManager gameManager;
    private HashMap<Player,User> players = new HashMap<>();
    private ArrayList<User> spectators;
    private BukkitTask gameTick;

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;

        //self-register
        ZombiesPlugin.instance.getServer().getPluginManager().registerEvents(this, ZombiesPlugin.instance);

        //runs every five ticks (4 times per second)
        PlayerManager ref = this;
        gameTick = new BukkitRunnable() {
            @Override
            public void run(){
                for(User user : players.values()) {
                    user.doTick(ref);
                }
            }
        }.runTaskTimer(ZombiesPlugin.instance, 0L, 5L);
    }

    public ArrayList<User> getPlayers() {
        return new ArrayList<>(players.values());
    }

    /**
     * Returns true if the player is successfully added; returns false otherwise.
     * @param player The player to add
     * @return Whether or not the player was added
     */
    public boolean addPlayer(Player player) {
        if(players.size() - 1 < gameManager.getGameSize())
        {
            if(players.containsKey(player)) return false;
        }

        players.put(player, new User(player));
        return  true;
    }

    /**
     * Returns true if the player is successfully removed; returns false otherwise.
     * @param player The player to remove
     * @return Whether or not the player was removed
     */
    public boolean removePlayer(Player player) {
        return players.remove(player) != null;
    }

    /*
     * Currently this event is invoked every time ANY player right-clicks, which is obviously not
     * ideal. Might be a way to have the event only apply to the players in the "players" array without
     * having to filter manually, but I wasn't able to find one.
     */
    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        User user = getAssociatedUser(event.getPlayer());
        if(user != null) {
            Gun gun = user.getGunUser().getGunByItemStack(user.getPlayer().getInventory().getItemInMainHand());
            if(gun != null ) {
                if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() ==  Action.RIGHT_CLICK_BLOCK)
                    gun.shoot();
                else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() ==  Action.LEFT_CLICK_BLOCK)
                    gun.reload();
            }

            // Pass the event to hotbar manager
            user.getHotbar().processEvent(event);
        }
    }

    @EventHandler
    public void onPlayerSwitchHeldItem(PlayerItemHeldEvent event) {
        User user = getAssociatedUser(event.getPlayer());
        if(user != null) {


            // Pass the event to hotbar manager
            user.getHotbar().processEvent(event);
        }
    }

    public User getAssociatedUser(Player player) {
        return players.get(player);
    }

    public boolean hasUser(User user) {
        return players.containsValue(user);
    }

    public GameManager getGameManager() {return gameManager;}
}