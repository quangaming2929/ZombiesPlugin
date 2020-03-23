package io.github.zap.zombiesplugin.guns;

import io.github.zap.zombiesplugin.guns.data.GunData;
import io.github.zap.zombiesplugin.guns.data.IModifiedValueResolver;
import io.github.zap.zombiesplugin.guns.data.ModifiableBulletStats;
import io.github.zap.zombiesplugin.guns.logics.LinearBeam;
import io.github.zap.zombiesplugin.utils.Factory;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class LinearGun extends Gun implements IModifiedValueResolver {
    public LinearGun(GunData data) {
        super(data);
    }

    @Override
    public void shoot(){
        if(!canShoot())
            return;

        Player player = getPlayer();
        World world = player.getWorld();
        Vector eyeLocation = player.getEyeLocation().toVector().clone();
        Vector eyeDirection = player.getEyeLocation().getDirection().clone();
        Vector targetBlockVector = getTargetBlockVector(player, eyeLocation, eyeDirection);

        sendShot(world, eyeLocation, eyeDirection, targetBlockVector);

        updateVisualAfterShoot();
    }

    @Override
    protected ModifiableBulletStats attachStats(GunData gunStats) {
        return Factory.GetModifableBulletStats(this, gunStats);
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
        int range = (int) Math.ceil(getStats().getRange());
        BoundingBox targetedBlockBoundingBox = player.getTargetBlock(materials, range).getBoundingBox();

        return targetedBlockBoundingBox.rayTrace(eyeLocation, eyeDirection, range + 1.74).getHitPosition();
    }


    @Override
    public String addModifier(String name, String baseValue, float modifier) {
        if(name.equals("maxHitEntities")) {
            return String.valueOf(Float.parseFloat(baseValue) * modifier);
        } else {
            // These value can't be modified return their base value
            return baseValue;
        }
    }


    public int getMaxHitEntities() {
        return (int)Float.parseFloat(getStats().getCustomValue("maxHitEntities"));
    }

    public Particle getParticle() {
        return Particle.valueOf(getStats().getCustomValue("particle"));
    }
}
