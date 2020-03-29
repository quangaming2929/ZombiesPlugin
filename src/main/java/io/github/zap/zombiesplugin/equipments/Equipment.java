package io.github.zap.zombiesplugin.equipments;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.events.ValueRequestedEventArgs;
import io.github.zap.zombiesplugin.hotbar.HotbarObject;
import io.github.zap.zombiesplugin.events.EventHandler;
import io.github.zap.zombiesplugin.manager.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.logging.Level;

public abstract class Equipment extends HotbarObject {
    protected final EquipmentData equipmentData;
    protected final PlayerManager playerManager;

    public Equipment(EquipmentData equipmentData, PlayerManager playerManager) {
        this.equipmentData = equipmentData;
        this.playerManager = playerManager;
    }

    public EquipmentData getEquipmentData() {
        return equipmentData;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }


    protected void changeDisplayNameColor(ChatColor color) {
        ItemMeta meta = getSlot().getItemMeta();
        meta.setDisplayName(getEquipmentData().getDisplayNameWith(color, 0));

        getSlot().setItemMeta(meta);
    }

    public final EventHandler<ValueRequestedEventArgs> valueRequested = new EventHandler<>();
    protected void onValueRequested(ValueRequestedEventArgs e) {
        valueRequested.invoke(this, e);
    }

    protected String tryGetCustomValue(String name) {
        if (getEquipmentData().customData.containsKey(name)) {
            String originalValue = getEquipmentData().customData.get(name);
            ValueRequestedEventArgs e = new ValueRequestedEventArgs(originalValue, name);
            onValueRequested(e);
            return (String) e.modifiedValue;
        }else {
            // Log this error to the console and the player
            String msg = ChatColor.RED + "The requested value: " + name + " is not existed! If you are a server operator," +
                    "please make sure that the config file is configured correctly. Value defaulted to 0";
            getPlayer().sendMessage(msg);
            ZombiesPlugin.instance.getLogger().log(Level.SEVERE, msg + " Sender: " + getPlayer().getDisplayName());

            return "";
        }
    }
}
