package io.github.zap.zombiesplugin.equipments.guns;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.manager.PlayerManager;
import io.github.zap.zombiesplugin.utils.Tuple;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RandomizedConicProjection extends Gun{
    private Random rand = new Random();

    public RandomizedConicProjection(EquipmentData equipmentData, PlayerManager playerManager) {
        super(equipmentData, playerManager);
    }

    @Override
    public boolean shoot() {
        int bullets = 8;
        int radius = 10;
        float shotAngle = 30;

        // Get the search limit
        Location eyeLocation = getPlayer().getEyeLocation();
        BoundingBox searchArea = new BoundingBox(eyeLocation.getX() - radius, eyeLocation.getY() - radius, eyeLocation.getZ() - radius,
                                                 eyeLocation.getX() + radius, eyeLocation.getY() + radius, eyeLocation.getZ() + radius);

        Collection<Entity> entities = getPlayer().getWorld().getNearbyEntities(searchArea, et -> et instanceof Mob);

        // dist zombie from player

        Vector playerVec  = eyeLocation.toVector();
        Vector zero = new Vector(0,0,0);
        double pitch = eyeLocation.getPitch();
        double yaw = eyeLocation.getYaw();

        // Generate disturbance
        List<Tuple<Double, Double>> bulletsPitchYaw = new ArrayList<>();
        for (int i = 0; i < bullets; i++) {
            double bulletPitch = nextAngleRand() * (shotAngle / 2);
            double bulletYaw = Math.sqrt(1 - Math.pow(bulletPitch / (shotAngle / 2), 2));

            bulletsPitchYaw.add(new Tuple<>(pitch + bulletPitch, yaw + bulletYaw));
        }

            for (Entity entity : entities) {
            Mob mob = (Mob) entity;
            double dist = getDist(eyeLocation, mob.getEyeLocation());
            if(dist < 100) { // range
                // the distance of the bullet is equal to the the length between the player and zombie

                Vector maxVec = minus(mob.getBoundingBox().getMax(), playerVec);
                Vector minVec = minus(mob.getBoundingBox().getMin(), playerVec);
                Vector eyeVec = minus(mob.getEyeLocation().toVector(), playerVec);

                double maxMag = maxVec.distance(zero);
                double minMag = minVec.distance(zero);
                double eyeMag = eyeVec.distance(zero);

                // Bullets will hit the target by these angles
                double maxPitch = Math.toDegrees(Math.acos(maxVec.getY() / maxMag));
                double maxYaw = Math.toDegrees(Math.atan(maxVec.getZ() / maxVec.getX()));
                double minPitch = Math.toDegrees(Math.acos(minVec.getY() / minMag));
                double minYaw = Math.toDegrees(Math.atan(minVec.getZ() / minVec.getX()));
                double eyePitch = Math.toDegrees(Math.acos(eyeVec.getY() / eyeMag));
                double eyeYaw = Math.toDegrees(Math.atan(eyeVec.getZ() / eyeVec.getX()));

                for (Tuple<Double,Double> py : bulletsPitchYaw) {
                    if (isInRange(py.x, minPitch, maxPitch) &&
                        isInRange(py.y, minYaw, maxYaw)) {
                        mob.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1, 5, true));
                    }
                }

            }
        }


        return false;
    }

    private Vector minus (Vector a, Vector b) {
        return new Vector (a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
    }

    private double nextAngleRand() {
        return rand.nextDouble() * 2 - 1;
    }

    private boolean isInRange (double val, double min, double max) {
        return val > min && val < max;
    }


    private double getDist (Location a, Location b) {
        return Math.sqrt( Math.pow(b.getX() - a.getX(), 2) +
                          Math.pow(b.getY() - a.getY(), 2) +
                          Math.pow(b.getZ() - a.getZ(), 2) );
    }

    // We treat them like vector
    private Location minus(Location a, Location b) {
        return new Location(a.getWorld(), b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
    }



}
