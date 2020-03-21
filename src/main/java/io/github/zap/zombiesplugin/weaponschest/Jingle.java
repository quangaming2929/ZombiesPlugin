package io.github.zap.zombiesplugin.weaponschest;

import io.github.zap.zombiesplugin.guns.Gun;
import java.util.List;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Jingle extends BukkitRunnable {

	private final Player player;

	private final WeaponsChestGunSwapper gunSwapper;

	private int counter = 1;

	private final float[] pitches = {(float) Math.pow(2, -8.0/12.0), 1.0F, (float) Math.pow(2, 7.0/12.0), 1.0F};

	private List<Gun> guns;

	public Jingle(Player player, WeaponsChestGunSwapper gunSwapper) {
		this.player = player;
		this.gunSwapper = gunSwapper;
	}

	@Override
	public void run() {
		if (counter == 4) {
			cancel();
		}

		player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0F, pitches[counter - 1]);
		gunSwapper.nextGun();
		counter++;
	}
}

