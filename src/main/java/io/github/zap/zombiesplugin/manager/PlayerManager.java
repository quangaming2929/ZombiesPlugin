package io.github.zap.zombiesplugin.manager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerManager implements Listener {
    @EventHandler(priority= EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();

    }
}
