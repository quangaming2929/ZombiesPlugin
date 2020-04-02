package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerManager implements Listener, ITickable {
    private GameManager gameManager;
    private HashMap<Player,User> players = new HashMap<>();
    private ArrayList<User> spectators = new ArrayList<>();

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;

        //self-register
        ZombiesPlugin.instance.getServer().getPluginManager().registerEvents(this, ZombiesPlugin.instance);
        ZombiesPlugin.instance.getTickManager().register(this);
    }

    @Override
    public void doTick() {
        for(User user : players.values()) {
            user.userTick();
        }
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

        players.put(player, new User(gameManager, player));
        return true;
    }

    /**
     * Returns true if the player is successfully removed; returns false otherwise.
     * @param player The player to remove
     * @return Whether or not the player was removed
     */
    public boolean removePlayer(Player player) {
        return players.remove(player) != null;
    }

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