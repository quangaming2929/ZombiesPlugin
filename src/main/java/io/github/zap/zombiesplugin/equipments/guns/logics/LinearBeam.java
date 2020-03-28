package io.github.zap.zombiesplugin.equipments.guns.logics;

import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class LinearBeam {
    protected final double distance;
    protected final List<Entity> hitEntities = new ArrayList();

    protected final World world;
    protected final Vector particleVector;
    protected final Vector directionVector;
    protected final Vector targetBlockVector;
    protected final int maxHitEntities;

    public LinearBeam(World world, Particle particle, Vector eyeLocation, Vector directionVector, Vector targetBlockVector, int maxHitEntities) {
        this.distance = eyeLocation.distance(targetBlockVector);

        this.world = world;
        this.particleVector = eyeLocation.clone();
        this.directionVector = directionVector.clone();
        this.targetBlockVector = targetBlockVector;
        this.maxHitEntities = maxHitEntities;
    }

    public void send() {
        final int particleCount = (int) Math.floor(distance);

        for (int i = 0; i < particleCount; i++) {
            if (hitEntities.size() == maxHitEntities) {
                break;
            } else {
                world.spawnParticle(Particle.CRIT, particleVector.getX(), particleVector.getY(), particleVector.getZ(), 0, 0, 0, 0);
                findEntitiesInLineOfSight();
                particleVector.add(directionVector);
            }
        }

        for (Entity entity : hitEntities) {
            damageEntity(entity);
        }
    }

    protected void findEntitiesInLineOfSight() {
        for (Entity entity : world.getNearbyEntities(particleVector.toLocation(world), 1, 1 , 1)) {
            if (entity instanceof Mob) { //TODO: Change requirement
                final BoundingBox boundingBox = entity.getBoundingBox();

                if (boundingBox.rayTrace(particleVector, directionVector.clone().normalize(), 1) != null && hitEntities.size() < maxHitEntities) {
                    hitEntities.add(entity);
                    // TODO: Damaging the entities
                }
            }
        }

    }

    protected void damageEntity(Entity entity) {
        // TODO: Damaging the entities
        if(entity instanceof LivingEntity){
            ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 1));
        }
    }
}