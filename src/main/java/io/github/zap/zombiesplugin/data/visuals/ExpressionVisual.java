package io.github.zap.zombiesplugin.data.visuals;

import io.github.zap.zombiesplugin.data.IDefaultVisual;
import io.github.zap.zombiesplugin.data.IEquipmentValue;
import io.github.zap.zombiesplugin.data.IIncludeLore;
import io.github.zap.zombiesplugin.data.ILeveling;
import io.github.zap.zombiesplugin.utils.InterpolationString;
import io.github.zap.zombiesplugin.utils.RomanNumber;
import io.github.zap.zombiesplugin.utils.WeaponStatsLoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Hashtable;

/* TODO: To prevent class explosion, we will define color and instruction in Importers class
 *  - Perks:  ChatColor.YELLOW;
 *            ChatColor.ITALIC.toString() + ChatColor.DARK_GRAY + "Active until death.";
 *  - Skills: ChatColor.CYAN;
 *            ChatColor.YELLOW.toString() + "RIGHT-CLICK " + ChatColor.GRAY + "to use!"
 */

/**
 * A class define equipments default visual state include display item and name,
 * instruction, and resolve interpolated description string. The default expression
 * syntax is "Some string {$expr} blah blah"
 */
public class ExpressionVisual implements IDefaultVisual {
    public Material displayItem;
    public ChatColor displayColor;
    public String[] instruction;

    public String[] description;

    public String exprStart = "${";
    public String exprEnd = "}";

    @Override
    public ItemStack getDefaultVisual(String name, int level, ILeveling levels) {
        Hashtable<String, IEquipmentValue> currentValue = levels.getLevel(level);
        ItemStack item = new ItemStack(getDisplayItem(), 1);
        ItemMeta meta = item.getItemMeta();

        String displayName =  name;
        if (level > 0) {
            displayName = ChatColor.BOLD + displayName + RomanNumber.toRoman(level);
        }
        meta.setDisplayName(getDisplayColor() + displayName);
        InterpolationString exprResolver = new InterpolationString() {
            @Override
            public String evalExpr(String expr) {
                String stripedExpr = this.removeExprNotation(expr);
                if (currentValue.containsKey(stripedExpr)) {
                    IEquipmentValue ev = currentValue.get(stripedExpr);
                    if (ev instanceof IIncludeLore) {
                        IIncludeLore il = (IIncludeLore)ev;
                        return il.getDisplayValue();
                    }
                }

                return expr;
            }
        };

        exprResolver.startSequence = exprStart;
        exprResolver.endSequence = exprEnd;

        String[] evaluatedDesc = new String[description.length];
        for (int i = 0; i < description.length; i++) {
            evaluatedDesc[i] = exprResolver.evalString(description[i]);
        }

        // We can use the weapon lore builder but don't add any stats
        WeaponStatsLoreBuilder builder = new WeaponStatsLoreBuilder();
        builder.withDescriptions(evaluatedDesc);
        builder.withInstruction(getInstruction());

        meta.setLore(builder.build());
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Material getDisplayItem() {
        return displayItem;
    }

    @Override
    public ChatColor getDisplayColor() {
        return displayColor;
    }

    @Override
    public String[] getInstruction() {
        return instruction;
    }

    @Override
    public void setDisplayItem(Material item) {
        displayItem = item;
    }

    @Override
    public void setDisplayColor(ChatColor color) {
        displayColor = color;
    }

    @Override
    public void setInstruction(String[] instruction) {
        this.instruction = instruction;
    }
}
