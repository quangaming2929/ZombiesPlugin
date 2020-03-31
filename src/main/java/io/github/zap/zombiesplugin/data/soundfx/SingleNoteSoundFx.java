package io.github.zap.zombiesplugin.data.soundfx;

import io.github.zap.zombiesplugin.data.SoundFx;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class SingleNoteSoundFx extends SoundFx {
    public Sound sound;
    public float pitch = 1;
    public float volume = 1;

    @Override
    public void play(List<Player> players, float masterVolume, SoundCategory category, Vector offset) {
        for (Player player : players) {
            Location loc = getPlaylocation(player, offset);
            player.playSound(loc, sound, category, masterVolume * volume, pitch);
        }
    }

    @Override
    public void playGlobal(World world, float masterVolume, SoundCategory category, Location location) {
        world.playSound(location, sound, category, masterVolume * volume, pitch);
    }
}
