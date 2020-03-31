package io.github.zap.zombiesplugin.pathfind.nms;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import net.minecraft.server.v1_15_R1.*;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class UnboundedMeleeAttack extends PathfinderGoal {
    protected final EntityCreature entity;
    protected int attackTimer;
    private final double speedModifier;
    private final boolean unknown;
    private PathEntity pathToTarget;
    private int counter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private long timeSinceEntityValidation;

    public UnboundedMeleeAttack(EntityCreature entity, double var1, boolean var3) {
        this.entity = entity;
        this.speedModifier = var1;
        this.unknown = var3;
        this.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
    }

    //entity validator
    public boolean a() {
        long time = this.entity.world.getTime();
        if (time - this.timeSinceEntityValidation < 20L) {
            return false;
        } else {
            this.timeSinceEntityValidation = time;
            EntityLiving targetEntity = this.entity.getGoalTarget();
            if (targetEntity == null) {
                this.entity.targetSelector.c();
                return this.entity.getGoalTarget() != null;
            } else if (!targetEntity.isAlive()) {
                return false;
            } else {
                this.pathToTarget = this.entity.getNavigation().a(targetEntity, 0);
                if (this.pathToTarget != null) {
                    return true;
                } else {
                    return this.getBoundsWidth(targetEntity) >= this.entity.g(targetEntity.locX(), targetEntity.locY(), targetEntity.locZ());
                }
            }
        }
    }

    //target entity validator...?
    public boolean b() {
        EntityLiving targetEntity = this.entity.getGoalTarget();
        if (targetEntity == null) {
            return false;
        } else if (!targetEntity.isAlive()) {
            return false;
        } else if (!this.unknown) { //calls some stuff on PathEntity
            return !this.entity.getNavigation().m();
        } else { //obvious
            return !(targetEntity instanceof EntityHuman) || !targetEntity.isSpectator() && !((EntityHuman)targetEntity).isCreative();
        }
    }

    public void c() {
        this.entity.getNavigation().a(this.pathToTarget, this.speedModifier);
        this.entity.q(true);
        this.counter = 0;
    }

    public void e() {
        EntityLiving targetEntity = this.entity.getGoalTarget();

        this.entity.getControllerLook().a(targetEntity, 30.0F, 30.0F);
        double distanceToTarget = this.entity.g(targetEntity.locX(), targetEntity.locY(), targetEntity.locZ());
        --this.counter;
        if ((this.unknown || this.entity.getEntitySenses().a(targetEntity)) &&
                this.counter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D &&
                this.targetZ == 0.0D || targetEntity.g(this.targetX, this.targetY, this.targetZ) >= 1.0D ||
                this.entity.getRandom().nextFloat() < 0.05F)) {
            this.targetX = targetEntity.locX();
            this.targetY = targetEntity.locY();
            this.targetZ = targetEntity.locZ();
            this.counter = 4 + this.entity.getRandom().nextInt(7); //no idea why there is rng here

            if (distanceToTarget > 1024.0D) {
                this.counter += 10;
            } else if (distanceToTarget > 256.0D) {
                this.counter += 5;
            }

            if (!this.entity.getNavigation().a(targetEntity, this.speedModifier)) {
                this.counter += 15;
            }
        }

        this.attackTimer = Math.max(this.attackTimer - 1, 0);
        this.tryAttack(targetEntity, distanceToTarget);

    }

    protected void tryAttack(EntityLiving targetEntity, double distanceToTarget) {
        double boundsWidth = this.getBoundsWidth(targetEntity);
        if (distanceToTarget <= boundsWidth && this.attackTimer <= 0) {
            this.attackTimer = 15;
            this.entity.a(EnumHand.MAIN_HAND);
            this.entity.B(targetEntity);
        }

    }

    //collision check
    protected double getBoundsWidth(EntityLiving target) {
        return (this.entity.getWidth() * 2.0F * this.entity.getWidth() * 2.0F + target.getWidth());
    }
}
