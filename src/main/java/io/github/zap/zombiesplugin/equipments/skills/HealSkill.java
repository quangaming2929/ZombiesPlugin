package io.github.zap.zombiesplugin.equipments.skills;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.manager.UserManager;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.utils.MathUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.Collection;

public class HealSkill extends Skill{
    public static final String SKILL_HEAL_SELF = "healSelf";
    public static final String SKILL_HEAL_TEAM = "healTeam";
    public static final String SKILL_HEAL_RADIUS = "radius";

    public HealSkill(EquipmentData equipmentData, UserManager userManager) {
        super(equipmentData, userManager);
    }

    @Override
    protected boolean use() {
        final float radius = tryGetValue(SKILL_HEAL_RADIUS);
        final float self = tryGetValue(SKILL_HEAL_SELF);
        final float team = tryGetValue(SKILL_HEAL_TEAM);

        World currentWorld  = getPlayer().getWorld();
        User useUser = getPlayerManager().getAssociatedUser(getPlayer());

        Collection<Player> nearbyPlayers = currentWorld.getNearbyPlayers(getPlayer().getLocation(), radius);

        boolean isHealAny = false;
        for (Player player : nearbyPlayers) {
            User user = getPlayerManager().getAssociatedUser(player);
            double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            if (user != null && player.getHealth() < maxHealth) {
                if (user == useUser) {
                    player.setHealth(MathUtils.clamp(0, maxHealth, player.getHealth() + self));
                } else {
                    player.setHealth(MathUtils.clamp(0, maxHealth, player.getHealth() + team));
                }

                isHealAny = true;
            }
        }

        if (!isHealAny) {
            getPlayer().sendMessage(ChatColor.RED + "No one need to heal right now!");
        }

        return isHealAny;
    }
}
