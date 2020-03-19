package io.github.zap.zombiesplugin.guns.data.gunattributes;

import org.bukkit.Particle;

import java.util.Hashtable;

public class LinearGunAttribute extends GunAttribute {
    // Name of feature data
    private final String maxHitEntitiesName = "maxHitEntities";
    private final String particleName = "particle";

    protected int maxHitEntities;
    protected Particle particle;

    public LinearGunAttribute(Hashtable<String, String> customFeatureValues) {
        super(customFeatureValues);

        maxHitEntities = Integer.parseInt(getAttribute(maxHitEntitiesName));
        particle = Particle.valueOf(getAttribute(particleName));
    }

    @Override
    public String getBehaviourName() {
        return "LinearGun";
    }

    public int getMaxHitEntities() {
        return maxHitEntities;
    }

    public void setMaxHitEntities(int maxHitEntities) {
        this.maxHitEntities = maxHitEntities;
        updateAttribute(maxHitEntitiesName, String.valueOf(maxHitEntities));
    }

    public Particle getParticle() {
        return particle;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
        updateAttribute(particleName, particle.name());
    }
}
