package io.github.zap.zombiesplugin.utils;

import com.comphenix.protocol.injector.netty.ProtocolInjector;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_15_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class TabDecorator {
    public static void sendTabTitle(Player player, String header, String footer) {
        JsonObject headerJson = new JsonObject();
        headerJson.add("text", new JsonPrimitive(header));
        JsonObject footerJson = new JsonObject();
        footerJson.add("text", new JsonPrimitive(footer));

        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter();
        headerPacket.header = IChatBaseComponent.ChatSerializer.a(headerJson);
        headerPacket.footer = IChatBaseComponent.ChatSerializer.a(footerJson);

        connection.sendPacket(headerPacket);
    }
}
