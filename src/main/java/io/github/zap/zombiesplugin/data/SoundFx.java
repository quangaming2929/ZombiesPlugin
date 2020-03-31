package io.github.zap.zombiesplugin.data;

import io.github.zap.zombiesplugin.provider.ICustomSerializerIdentity;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public abstract class SoundFx implements ICustomSerializerIdentity {
    /**
     * Play the soundfx to player
     * @param players the players you want to play
     * @param masterVolume the sound master volume
     * @param offset offset from the player.
     */
    public abstract void play(List<Player> players, float masterVolume, SoundCategory category, Vector offset);

    /**
     * Play the sound fx to the world
     * @param masterVolume the sound master volume
     * @param location location to play the sound
     */
    public abstract void playGlobal(World world, float masterVolume, SoundCategory category, Location location);

    protected Location getPlaylocation(Player player, Vector offset) {
        Location loc = player.getLocation().clone();
        if (offset != null) {
            loc.setX(loc.getX() + offset.getX());
            loc.setY(loc.getY() + offset.getY());

        }

        return loc;
    }
}
