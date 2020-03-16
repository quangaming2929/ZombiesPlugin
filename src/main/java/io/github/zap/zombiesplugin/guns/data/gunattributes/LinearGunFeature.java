package io.github.zap.zombiesplugin.guns.data.gunattributes;

import org.bukkit.Particle;

import java.util.Hashtable;

public class LinearGunFeature extends BaseFeature {
    // Name of feature data
    private final String maxHitEntitiesName = "maxHitEntities";
    private final String particleName = "particle";

    protected int maxHitEntities;
    protected Particle particle;

    public LinearGunFeature(Hashtable<String, String> customFeatureValues) {
        super(customFeatureValues);

        maxHitEntities = Integer.parseInt(getFeatureValue(maxHitEntitiesName));
        particle = Particle.valueOf(getFeatureValue(particleName));
    }

    @Override
    public String getFeatureName() {
        return "LinearGun";
    }

    public int getMaxHitEntities() {
        return maxHitEntities;
    }

    public void setMaxHitEntities(int maxHitEntities) {
        this.maxHitEntities = maxHitEntities;
        updateFeatureValue(maxHitEntitiesName, String.valueOf(maxHitEntities));
    }

    public Particle getParticle() {
        return particle;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
        updateFeatureValue(particleName, particle.name());
    }
}
