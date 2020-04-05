package io.github.zap.zombiesplugin.equipments;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.data.IEquipmentValue;
import io.github.zap.zombiesplugin.data.SoundFx;
import io.github.zap.zombiesplugin.data.soundfx.SingleNoteSoundFx;
import io.github.zap.zombiesplugin.events.ValueRequestedEventArgs;
import io.github.zap.zombiesplugin.manager.PlayerManager;
import io.github.zap.zombiesplugin.utils.RomanNumber;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;

public abstract class UpgradeableEquipment extends Equipment {
    protected String exceedUltMessage = "This equipment is already maxed out";
    protected String ultMessage = "You have upgraded your %s to level %s";
    protected SoundFx upgradeSound;
    private int level;

    public UpgradeableEquipment(EquipmentData equipmentData, PlayerManager playerManager) {
        super(equipmentData, playerManager);

        SingleNoteSoundFx sound = new SingleNoteSoundFx();
        sound.sound = Sound.ENTITY_PLAYER_LEVELUP;
        upgradeSound = sound;
    }

    @Override
    protected void changeDisplayNameColor(ChatColor color) {
        ItemMeta meta = getSlot().getItemMeta();
        meta.setDisplayName(getEquipmentData().getDisplayNameWith(color, getLevel()));

        getSlot().setItemMeta(meta);
    }

    public boolean upgrade() {
        if (getLevel() < getEquipmentData().levels.size() - 1) {
            setLevel(getLevel() + 1);
            if(isVisible()) {
                setSlot(equipmentData.getDefaultVisual(getLevel()));
            }

            // Play upgrade sound
            ArrayList<Player> players = new ArrayList<>();
            players.add(getPlayer());
            upgradeSound.play(players, 1, SoundCategory.BLOCKS, null);

            // Send message
            String msg = ChatColor.GREEN + String.format(ultMessage, getEquipmentData().name, RomanNumber.toRoman(getLevel()));
            getPlayer().sendMessage(msg);
            return true;
        } else {
            // Send error message
            getPlayer().sendMessage(ChatColor.RED + exceedUltMessage);
            return false;
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if (level < getEquipmentData().levels.size() && level >= 0) {
            this.level = level;
        } else {
            throw new IllegalArgumentException("Invalid level");
        }
    }

    public Hashtable<String, IEquipmentValue> getCurrentStat() {
        return equipmentData.levels.getLevel(getLevel());
    }

    public float tryGetValue (String name) {
        if (getCurrentStat().containsKey(name)) {
            float originalValue = getCurrentStat().get(name).provideValue();
            ValueRequestedEventArgs e = new ValueRequestedEventArgs(originalValue, name);
            onValueRequested(e);

            return (float) e.modifiedValue;
        } else {
            // Log this error to the console and the player
            String msg = ChatColor.RED + "The requested value: " + name + " is not existed! If you are a server operator," +
                    "please make sure that the config file is configured correctly. Value defaulted to 0";
            getPlayer().sendMessage(msg);
            ZombiesPlugin.instance.getLogger().log(Level.SEVERE, msg + " Sender: " + getPlayer().getDisplayName());

            return 0f;
        }
    }

    @Override
    public void setVisibility(boolean isVisible) {
        super.setVisibility(isVisible);
        if(isVisible) {
            setSlot(getEquipmentData().getDefaultVisual(getLevel()));
        } else {
            setSlot(null);
        }
    }
}
