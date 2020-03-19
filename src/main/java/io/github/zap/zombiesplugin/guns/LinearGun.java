package io.github.zap.zombiesplugin.guns;

import io.github.zap.zombiesplugin.guns.data.GunData;
import io.github.zap.zombiesplugin.guns.data.gunattributes.LinearGunAttribute;
import io.github.zap.zombiesplugin.guns.logics.LinearBeam;
import io.github.zap.zombiesplugin.player.GunUser;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class LinearGun extends Gun {

    protected final int maxHitEntities;
    protected final Particle particle;

    public LinearGun(GunData data, GunUser user) {
        super(data, user);

        LinearGunAttribute feature = (LinearGunAttribute) gunStats.feature;
        this.maxHitEntities = feature.getMaxHitEntities();
        this.particle = feature.getParticle();
    }

    public void shoot(){
        if(!canShoot())
            return;

        Player player = this.gunOwner.user.getPlayer();
        World world = player.getWorld();
        Vector eyeLocation = player.getEyeLocation().toVector().clone();
        Vector eyeDirection = player.getEyeLocation().getDirection().clone();
        Vector targetBlockVector = getTargetBlockVector(player, eyeLocation, eyeDirection);

        sendShot(world, eyeLocation, eyeDirection, targetBlockVector);

        updateVisualAfterShoot();
    }


    private void sendShot(World world, Vector particleLocation, Vector particleDirection, Vector targetBlockVector) {
        LinearBeam beam = new LinearBeam(world, particle, particleLocation, particleDirection, targetBlockVector, maxHitEntities);
        beam.send();
    }

    private Vector getTargetBlockVector(Player player, Vector eyeLocation, Vector eyeDirection) {
        Set<Material> materials = new HashSet<>();

        materials.add(Material.AIR);
        materials.add(Material.CAVE_AIR);

        // TODO: Add slab blocks
        int range = (int) Math.ceil(this.getCurrentStats().baseRange);
        BoundingBox targetedBlockBoundingBox = player.getTargetBlock(materials, range).getBoundingBox();

        return targetedBlockBoundingBox.rayTrace(eyeLocation, eyeDirection, range).getHitPosition();
    }
}
