package io.github.zap.zombiesplugin.gamecreator.lobbyproviders;

import com.comphenix.protocol.PacketType;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVDestination;
import com.onarandombox.MultiverseCore.api.SafeTTeleporter;
import com.onarandombox.MultiverseCore.enums.TeleportResult;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.gamecreator.ILobbyProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class MultiverseLobby implements ILobbyProvider {
    private final MultiverseCore core;

    public MultiverseLobby() {
        MultiverseCore tempVar = null;

        try {
            tempVar = (MultiverseCore) Bukkit.getPluginManager().getPlugin("Multiverse-Core");
        } catch (Exception e) {
            ZombiesPlugin.instance.getLogger().log(Level.WARNING, "Can't find Multiverse-Core plugin, lobby warp is disabled");
        }
        core = tempVar;
    }

    @Override
    public boolean warpToLobby(Player p, String name) {
        if (core != null) {
            MVDestination dest = core.getDestFactory().getDestination(name);
            SafeTTeleporter teleporter = core.getSafeTTeleporter();
            if(teleporter.safelyTeleport(p, p, dest) == TeleportResult.SUCCESS) {
                return true;
            }
        } else {
            p.sendMessage(ChatColor.RED + "Villagers invaded our lobbies so we can't wrap you to a lobby, please report to the admins");
        }

        return false;
    }
}
