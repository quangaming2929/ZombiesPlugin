package io.github.zap.zombiesplugin.guns;

import io.github.zap.zombiesplugin.guns.data.GunData;
import io.github.zap.zombiesplugin.guns.data.IModifiedValueResolver;
import io.github.zap.zombiesplugin.guns.data.ModifiableBulletStats;
import io.github.zap.zombiesplugin.guns.logics.LinearBeam;
import io.github.zap.zombiesplugin.player.GunUser;
import io.github.zap.zombiesplugin.utils.Factory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class LinearGun extends Gun implements IModifiedValueResolver {
    public LinearGun(GunData data, GunUser user) {
        super(data, user);
    }

    @Override
    public void shoot(){
        if(!canShoot())
            return;

        Player player = this.gunOwner.user.getPlayer();
        World world = player.getWorld();
        Vector eyeLocation = player.getEyeLocation().toVector().clone();
        Vector eyeDirection = player.getEyeLocation().getDirection().clone();
        Vector targetBlockVector = getTargetBlockVector(player);

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

    public Vector getTargetBlockVector(Player player) {
        Set<Material> materials = new HashSet<>();

        materials.add(Material.AIR);
        materials.add(Material.CAVE_AIR);
        materials.add(Material.VOID_AIR);

        // TODO: Add slab blocks
        int range = 100; // (int) Math.ceil(this.getCurrentStats().baseRange);;
        Block targetBlock = player.getTargetBlock(materials, range);
        BoundingBox boundingBox;

        if (targetBlock.getType().equals(Material.AIR) || targetBlock.getType().equals(Material.CAVE_AIR) || targetBlock.getType().equals(Material.VOID_AIR)) {
            Location location = targetBlock.getLocation();
            boundingBox = new BoundingBox(location.getX(), targetBlock.getY(), targetBlock.getZ(), location.getX() + 1, location.getY() + 1, targetBlock.getZ() + 1);
        } else {
            boundingBox = targetBlock.getBoundingBox();
        }

        return boundingBox.rayTrace(player.getEyeLocation().toVector(), player.getEyeLocation().getDirection(), range + 1.74).getHitPosition();
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
