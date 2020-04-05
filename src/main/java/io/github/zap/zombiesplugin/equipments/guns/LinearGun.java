package io.github.zap.zombiesplugin.equipments.guns;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.equipments.guns.logics.LinearBeam;
import io.github.zap.zombiesplugin.manager.UserManager;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class LinearGun extends Gun {
    public final String GUN_RANGE = "gunRange";
    public final String GUN_MAX_HIT_ENTITIES = "maxHitEntities";
    public final String GUN_PARTICLE = "particle";

    public LinearGun(EquipmentData equipmentData, UserManager userManager) {
        super(equipmentData, userManager);
    }

    @Override
    public boolean shoot(){
        Player player = getPlayer();
        World world = player.getWorld();
        Vector eyeLocation = player.getEyeLocation().toVector().clone();
        Vector eyeDirection = player.getEyeLocation().getDirection().clone();
        Vector targetBlockVector = getTargetBlockVector(player, eyeLocation, eyeDirection);

        sendShot(world, eyeLocation, eyeDirection, targetBlockVector);

        return true;
    }


    private void sendShot(World world, Vector particleLocation, Vector particleDirection, Vector targetBlockVector) {
        LinearBeam beam = new LinearBeam(world, getParticle(), particleLocation, particleDirection, targetBlockVector, getMaxHitEntities());
        beam.send();
    }

    private Vector getTargetBlockVector(Player player, Vector eyeLocation, Vector eyeDirection) {
        Set<Material> materials = new HashSet<>();

        materials.add(Material.AIR);
        materials.add(Material.CAVE_AIR);

        // TODO: Add slab blocks
        int range = (int) Math.ceil(tryGetValue(GUN_RANGE));
        BoundingBox targetedBlockBoundingBox = player.getTargetBlock(materials, range).getBoundingBox();

        return targetedBlockBoundingBox.rayTrace(eyeLocation, eyeDirection, range + 1.74).getHitPosition();
    }


    public int getMaxHitEntities() {
        return (int)tryGetValue(GUN_MAX_HIT_ENTITIES);
    }

    public Particle getParticle() {
        return Particle.valueOf(tryGetCustomValue(GUN_PARTICLE));
    }
}
