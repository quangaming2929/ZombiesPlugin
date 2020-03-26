package io.github.zap.zombiesplugin.equipments;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.data.IEquipmentValue;
import io.github.zap.zombiesplugin.data.SoundFx;
import io.github.zap.zombiesplugin.data.soundfx.SingleNoteSoundFx;
import io.github.zap.zombiesplugin.utils.RomanNumber;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Hashtable;

public abstract class UpgradeableEquipment extends Equipment {
    protected String exceedUltMessage = "This equipment is already maxed out";
    protected String ultMessage = "You have upgraded your %s to level %s";
    protected SoundFx upgradeSound;
    private int level;

    public UpgradeableEquipment(EquipmentData equipmentData) {
        super(equipmentData);

        SingleNoteSoundFx sound = new SingleNoteSoundFx();
        sound.sound = Sound.ENTITY_PLAYER_LEVELUP;
        upgradeSound = sound;
    }

    public void upgrade() {
        if (getLevel() < getEquipmentData().levels.size() - 1) {
            setLevel(getLevel() + 1);
            if(isVisible()) {
                setSlot(equipmentData.getDefaultVisual(getLevel()));
            }

            // Play upgrade sound
            ArrayList<Player> players = new ArrayList<>();
            players.add(getPlayer());
            upgradeSound.play(players, 100, SoundCategory.BLOCKS, null);

            // Send message
            String msg = ChatColor.GREEN + String.format(ultMessage, getEquipmentData().name, RomanNumber.toRoman(getLevel()));
            getPlayer().sendMessage(msg);
        } else {
            // Send error message
            getPlayer().sendMessage(ChatColor.RED + exceedUltMessage);
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
