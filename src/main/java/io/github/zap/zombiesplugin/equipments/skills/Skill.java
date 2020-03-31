package io.github.zap.zombiesplugin.equipments.skills;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.equipments.UpgradeableEquipment;
import io.github.zap.zombiesplugin.equipments.perks.Perk;
import io.github.zap.zombiesplugin.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// TODO: Test this buy making a test skill. ADD importer :)
public abstract class Skill extends UpgradeableEquipment {
    public static final String SKILL_CD = "CD";
    protected static final String READY_LORE = ChatColor.YELLOW + "RIGHT-CLICK " + ChatColor.GRAY + "to use!";

    private final int loreSize;
    public Skill(EquipmentData equipmentData, PlayerManager playerManager) {
        super(equipmentData, playerManager);
        loreSize = equipmentData.getDefaultVisual(0).getItemMeta().getLore().size();
    }

    @Override
    public void setVisibility(boolean isVisible) {
        super.setVisibility(isVisible);

        if (System.currentTimeMillis() > usableUntil) {
            setLoreState(READY_LORE);
        }
    }

    @Override
    public boolean onRightClick(Block clickedBlock, BlockFace clickedFace) {
        if (System.currentTimeMillis() > usableUntil) {
            if (use()) {
                updateVisualAfterUse();
            }
        }

        return super.onRightClick(clickedBlock, clickedFace);
    }

    protected void updateVisualAfterUse() {
        usableUntil = System.currentTimeMillis() + (int)(tryGetValue(SKILL_CD) * 1000);

        new BukkitRunnable() {
            int timer = (int) tryGetValue(SKILL_CD);

            @Override
            public void run() {
                changeDisplayNameColor(ChatColor.RED);
                if (timer > 0) {
                    timer -= 1;
                    if (isVisible()) {
                        setLoreState(ChatColor.RED + "Use must wait " + timer + "s to use!");
                    }
                } else {
                    if (isVisible()) {
                        setLoreState(READY_LORE);
                    }
                    changeDisplayNameColor(getEquipmentData().getDisplayColor());
                    cancel();
                }
            }
        }.runTaskTimer(ZombiesPlugin.instance, 0, 20);
    }

    protected void setLoreState(String state) {
        ItemMeta meta = getSlot().getItemMeta();
        List<String> lore = meta.getLore();
        // Some how lore can be null :)?
        if (lore == null) {
            lore = new ArrayList<>();
        }
        // remove status lore
        while (lore.size() > loreSize) {
            lore.remove(lore.size() - 1);
        }

        lore.add("");
        lore.add(state);
        meta.setLore(lore);

        getSlot().setItemMeta(meta);
    }

    private long usableUntil = 0;
    protected abstract boolean use();
}
