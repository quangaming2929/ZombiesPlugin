package io.github.zap.zombiesplugin.guns.data;

import org.bukkit.Sound;

public class SoundFx {

    private final Sound sound;

    private final float volume;

    private final float pitch;

    public SoundFx(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public Sound getSound() {
        return sound;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}
