package io.github.zap.zombiesplugin.bodies;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import com.mojang.authlib.GameProfile;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.hologram.Hologram;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class DeadBody {
    private EntityPlayer body;

    private Player copied;

    private Hologram hologram;

    private BukkitRunnable reviveTask = new BukkitRunnable() {

        AtomicInteger tenthSecondsElapsed = new AtomicInteger(0);

        @Override
        public void run() {
            if (tenthSecondsElapsed.get() == 50 && !isCancelled()) {
                destroyBody();
            } else {
                hologram.editLine(ChatColor.RED + String.format("%.1fs", 5.0 - (float) tenthSecondsElapsed.getAndIncrement() / 10), 3);
            }
        }
    };

    public DeadBody(Player player) {
        this.copied = player;
        CraftPlayer craftPlayer = (CraftPlayer) player;

        MinecraftServer minecraftServer = craftPlayer.getHandle().getMinecraftServer();
        WorldServer worldServer = craftPlayer.getHandle().getWorldServer();

        body = new EntityPlayer(minecraftServer, worldServer, new GameProfile(player.getUniqueId(), UUID.randomUUID().toString().substring(0, 16)), new PlayerInteractManager(worldServer));

        Location location = player.getLocation();
        body.setLocation(location.getX(), location.getY(), location.getZ(), player.getEyeLocation().getYaw(), location.getPitch());
        ZombiesPlugin.instance.getLogger().info(String.format("%f, %f", location.getYaw(), location.getPitch()));
        ZombiesPlugin.instance.getLogger().info(String.format("%f, %f", body.yaw, body.pitch));
        hologram = new Hologram(location.clone().add(-1 + 0.03125, 2.25, 0), 0.25);
        hologram.addLine(ChatColor.BOLD +  "" + ChatColor.YELLOW + "■■■■■■■■■■■■■■■");
        hologram.addLine(ChatColor.YELLOW + "" + ChatColor.BOLD  + "HOLD SNEAK TO REVIVE!");
        hologram.addLine(ChatColor.BOLD +  "" + ChatColor.YELLOW + "■■■■■■■■■■■■■■■");
        hologram.addLine(ChatColor.RED + String.format("%.1fs", 25.0));
        // copied.teleport(copied.getLocation().clone().add(-0.5, -1, 0));
        startReviveTimer();
    }

    public void displayTo(Player player) {
        try {
            ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, getPlayerInfoPacket(body, EnumWrappers.PlayerInfoAction.ADD_PLAYER));
            ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, getBodySpawnPacket());
            ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, getEntityLookPacket());
            ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, getHeadRotationPacket());
            //ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, getEntityLookPacket());
            //ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, getBodySleepPacket());
            hideNametagFrom(player);
            ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, getPlayerInfoPacket(body, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER));
            ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, getPlayerInfoPacket(((CraftPlayer) copied).getHandle(), EnumWrappers.PlayerInfoAction.ADD_PLAYER));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private PacketContainer getPlayerInfoPacket(EntityPlayer player, EnumWrappers.PlayerInfoAction action) {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        container.getPlayerInfoAction().write(0, action);

        WrappedGameProfile profile = new WrappedGameProfile(player.getUniqueID(), player.getName());
        PlayerInfoData data = new PlayerInfoData(profile, 1, EnumWrappers.NativeGameMode.NOT_SET, WrappedChatComponent.fromText(player.getName()));

        List<PlayerInfoData> infoDataList = new ArrayList<>();
        infoDataList.add(data);

        container.getPlayerInfoDataLists().write(0, infoDataList);
        return container;
    }

    private PacketContainer getBodySpawnPacket() {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.NAMED_ENTITY_SPAWN);

        container.getIntegers().write(0, body.getId());
        container.getUUIDs().write(0, body.getUniqueID());

        container.getDoubles().write(0, body.locX());
        container.getDoubles().write(1, body.locY());
        container.getDoubles().write(2, body.locZ());
        container.getBytes().write(0, (byte) (body.yaw * 256.0F / 360.0F));
        container.getBytes().write(1, (byte) (body.pitch * 256.0F / 360.0F));

        return container;
    }

    private PacketContainer getEntityLookPacket() {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_LOOK);

        container.getIntegers().write(0, body.getId());
        container.getBytes().write(0, (byte) (body.yaw * 256.0F / 360.0F));
        container.getBytes().write(1, (byte) (body.pitch * 256.0F / 360.0F));

        return container;
    }

    private PacketContainer getHeadRotationPacket() {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_HEAD_ROTATION);

        container.getIntegers().write(0, body.getId());
        container.getBytes().write(0, (byte) (body.yaw * 256.0F / 360.0F));

        return container;
    }

    private PacketContainer getBodySleepPacket() {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        container.getIntegers().write(0, body.getId());

        WrappedDataWatcher watcher = new WrappedDataWatcher();
        watcher.setEntity(body.getBukkitEntity());
        watcher.setObject(6, WrappedDataWatcher.Registry.get(EntityPose.class), EntityPose.SLEEPING);

        container.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        return container;
    }

    private void hideNametagFrom(Player player) {
        Scoreboard scoreboard = player.getScoreboard();

        Team team;
        if (scoreboard.getTeam("NPC-ZombiesGame") == null) {
            scoreboard.registerNewTeam("NPC-ZombiesGame");
        }

        team = scoreboard.getTeam("NPC-ZombiesGame");
        team.addEntry(body.getName());
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
    }

    public void startReviveTimer() {
        reviveTask.runTaskTimer(ZombiesPlugin.instance, 0L, 2L);
    }

    public void destroyBody() {
        reviveTask.cancel();
        hologram.remove();
        body.setHealth(0);
    }

    public EntityPlayer getBody() {
        return body;
    }
}
