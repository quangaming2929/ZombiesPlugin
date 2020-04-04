package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.events.EventHandler;
import io.github.zap.zombiesplugin.events.UserJoinLeaveEventArgs;
import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class UserManager implements Listener, ITickable {
    private GameManager gameManager;
    private HashMap<Player,User> players = new HashMap<>();
    private ArrayList<User> spectators = new ArrayList<>();

    //event handlers
    private EventHandler<UserJoinLeaveEventArgs> userJoinLeaveEventHandler;

    public UserManager(GameManager gameManager) {
        this.gameManager = gameManager;

        //self-register
        ZombiesPlugin.instance.getServer().getPluginManager().registerEvents(this, ZombiesPlugin.instance);
        ZombiesPlugin.instance.getTickManager().register(this);

        userJoinLeaveEventHandler = new EventHandler<>();
    }

    public ArrayList<User> getPlayers() {
        return new ArrayList<>(players.values());
    }

    public GameManager getGameManager() { return gameManager; }

    public User getAssociatedUser(Player player) { return players.get(player); }

    public boolean hasUser(User user) { return players.containsValue(user); }

    public EventHandler<UserJoinLeaveEventArgs> getPlayerJoinLeaveHandler() { return userJoinLeaveEventHandler; }

    /**
     * Essentially a gameloop for users.
     */
    @Override
    public void doTick() {
        for(User user : players.values()) {
            user.userTick();
        }
    }

    /**
     * Returns true if the player is successfully added; returns false otherwise.
     * @param player The player to add
     * @return Whether or not the player was added
     */
    public boolean addUser(Player player) {
        if(players.size() - 1 < gameManager.getSettings().getGameSize())
        {
            if(players.containsKey(player)) return false;
        }

        User user = new User(this, player);
        players.put(player, user);

        userJoinLeaveEventHandler.invoke(this, new UserJoinLeaveEventArgs(user, UserJoinLeaveEventArgs.ChangeType.JOIN));
        return true;
    }

    /**
     * Returns true if the User is successfully removed; returns false otherwise.
     * @param user The User to remove
     * @return Whether or not the player was removed
     */
    public boolean removeUser(User user) {
        Player player = user.getPlayer();
        if(players.containsKey(player)) {
            players.remove(player);
            userJoinLeaveEventHandler.invoke(this, new UserJoinLeaveEventArgs(user, UserJoinLeaveEventArgs.ChangeType.LEAVE));
            return true;
        }
        return false;
    }

    @org.bukkit.event.EventHandler
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

    @org.bukkit.event.EventHandler
    public void onPlayerSwitchHeldItem(PlayerItemHeldEvent event) {
        User user = getAssociatedUser(event.getPlayer());
        if(user != null) {
            // Pass the event to hotbar manager
            user.getHotbar().processEvent(event);
        }
    }
}