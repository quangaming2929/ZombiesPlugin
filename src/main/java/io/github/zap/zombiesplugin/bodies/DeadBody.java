package io.github.zap.zombiesplugin.bodies;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import com.mojang.authlib.GameProfile;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.hologram.Hologram;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeadBody implements Listener {
    private EntityPlayer body;

    private Player copied;

    private Player reviver = null;

    private Hologram hologram;

    private BukkitRunnable knockdownTask = new BukkitRunnable() {

        int tenthSecondsElapsed = 0;

        @Override
        public void run() {
            if (tenthSecondsElapsed == 250 && !isCancelled()) {
                destroyBody();
                copied.setFlying(true);
                copied.sendTitle("You died!", "", 1, 20, 1);;
            } else {
                hologram.editLine(ChatColor.RED + String.format("%.1fs", 5.0 - (float) tenthSecondsElapsed / 10), 3);
                tenthSecondsElapsed++;
            }
        }
    };

    private BukkitRunnable reviveTask = new BukkitRunnable() {

        int tenthSecondsElapsed = 0;

        @Override
        public void run() {
            if (tenthSecondsElapsed == 15 && !isCancelled()) {
                destroyBody();
                copied.setWalkSpeed(0.2F);
                copied.removePotionEffect(PotionEffectType.JUMP);
                copied.teleport(copied.getLocation().clone().add(0, 2, 0));
                HandlerList.unregisterAll(DeadBody.this);
            } else {
                hologram.editLine(ChatColor.RED + String.format("%.1fs", 1.5 - (float) tenthSecondsElapsed / 10), 3);
                tenthSecondsElapsed++;
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
        body.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
        copied.teleport(copied.getLocation().clone().add(-0.5, -1, 0));
        copied.setWalkSpeed(0);

        createReviveHologram();
        ZombiesPlugin.instance.getServer().getPluginManager().registerEvents(this, ZombiesPlugin.instance);
    }

    private void createReviveHologram() {
        hologram = new Hologram(new Location(body.getBukkitEntity().getWorld(), body.locX() -1 + 0.03125, body.locY() + 2.25, body.locZ() + 0), 0.25);
        hologram.addLine(ChatColor.BOLD +  "" + ChatColor.YELLOW + "■■■■■■■■■■■■■■■");
        hologram.addLine(ChatColor.YELLOW + "" + ChatColor.BOLD  + "HOLD SNEAK TO REVIVE!");
        hologram.addLine(ChatColor.BOLD +  "" + ChatColor.YELLOW + "■■■■■■■■■■■■■■■");
        hologram.addLine(ChatColor.RED + String.format("%.1fs", 25.0));
    }

    public void displayTo(Player player) {
        try {
            ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, getPlayerInfoPacket(body, EnumWrappers.PlayerInfoAction.ADD_PLAYER));
            ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, getBodySpawnPacket());
            /*ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, getEntityLookPacket());
            ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, getHeadRotationPacket());*/
            //ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, getEntityLookPacket());
            ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, getBodySleepPacket());
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

    /*private PacketContainer getEntityLookPacket() {
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
    }*/

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

    public void startKnockdownTask() {
        knockdownTask.runTaskTimer(ZombiesPlugin.instance, 0L, 2L);
        copied.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 250, 128));
        copied.setWalkSpeed(0F);
    }

    public void startReviveTask() {
        reviveTask.runTaskTimer(ZombiesPlugin.instance, 0L, 2L);
    }

    public void destroyBody() {
        knockdownTask.cancel();
        hologram.remove();
        body.setHealth(0);
        copied.setWalkSpeed(0.2F);
    }

    @EventHandler
    public void onToggleShift(PlayerToggleSneakEvent event) {
        if (reviver == null && event.getPlayer().getLocation().distance(copied.getLocation()) < 0.5 && event.isSneaking()) {
            reviver = event.getPlayer();
            startReviveTask();
            hologram.editLine(ChatColor.YELLOW + "" + ChatColor.BOLD + "REVIVING...", 1);
        } else if (reviver == event.getPlayer() && !event.isSneaking()) {
            reviveTask.cancel();
            hologram.editLine(ChatColor.YELLOW + "" + ChatColor.BOLD  + "HOLD SNEAK TO REVIVE!", 1);
            knockdownTask.runTaskTimer(ZombiesPlugin.instance, 0L, 2L);
        }
    }
}
