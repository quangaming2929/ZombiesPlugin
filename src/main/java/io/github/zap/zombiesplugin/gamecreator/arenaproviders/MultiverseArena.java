package io.github.zap.zombiesplugin.gamecreator.arenaproviders;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVDestination;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.api.SafeTTeleporter;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.gamecreator.GameRoom;
import io.github.zap.zombiesplugin.gamecreator.IArenaProvider;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.manager.GameSession;
import io.github.zap.zombiesplugin.manager.GameSettings;
import io.github.zap.zombiesplugin.manager.MultiverseHook;
import io.github.zap.zombiesplugin.scoreboard.IInGameScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class MultiverseArena implements IArenaProvider {
    private final MultiverseCore core;

    public MultiverseArena() {
        MultiverseCore tempVar = null;

        try {
            tempVar = (MultiverseCore) Bukkit.getPluginManager().getPlugin("Multiverse-Core");
        } catch (Exception e) {
            ZombiesPlugin.instance.getLogger().log(Level.WARNING, "Can't find Multiverse-Core plugin, lobby warp is disabled");
        }
        core = tempVar;
    }

    @Override
    @Nullable
    public GameManager createGame(GameRoom room) {
        try {
            // Create game manager
            GameSettings settings = new GameSettings(room.getSelectedDiff(), room.getSelectedMap(), room.getRoomCapacity());
            String name = "Room: " + room.getRoomID() + " games";

            GameSession session = new GameSession(room);
            GameManager manager = new GameManager(name, settings);
            session.scoreboard = ZombiesPlugin.instance.getApiSupplier().inGameScoreboardSupplier.apply(manager);
            session.hook = new MultiverseHook(manager);
            manager.setGameSession(session);

            String mvWorldName = "room_" + room.getRoomID();
            // Create delete old multiverse world if exist
            MultiverseWorld oldWorld = core.getMVWorldManager().getMVWorld(mvWorldName);
            if (oldWorld != null) {
                if(!core.deleteWorld(mvWorldName)) {
                    return null;
                }
            }

            // Create a new multiverse world
            String mapTemplateWorld = "t_map_" + room.getSelectedMap().getId();
            MultiverseWorld templateWorld = core.getMVWorldManager().getMVWorld(mapTemplateWorld);
            if (templateWorld != null) {
                if (!core.getMVWorldManager().cloneWorld(mapTemplateWorld, mvWorldName)) {
                    return null;
                }
            }

            return manager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Delete the multiverse world created by this GameRoom
     */
    public boolean finalizeGame (GameRoom room) {
        try {
            String mvWorldName = "room_" + room.getRoomID();
            MultiverseWorld world = core.getMVWorldManager().getMVWorld(mvWorldName);
            if (world == null || !core.deleteWorld(mvWorldName)) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean wrapPlayer(GameRoom room, Player player) {
        try {
            String mvWorldName = "room_" + room.getRoomID();
            MVDestination arenaDest = core.getDestFactory().getDestination(mvWorldName);

            SafeTTeleporter tp = core.getSafeTTeleporter();
            tp.safelyTeleport(player, player, arenaDest);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Nullable
    public MultiverseWorld getRoomArena (GameRoom room) {
        String mvWorldName = "room_" + room.getRoomID();
        return core.getMVWorldManager().getMVWorld(mvWorldName);
    }
}
