package io.github.zap.zombiesplugin.powerup;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PowerupBar {

	private BossBar bossBar;

	private ChatColor chatColor;

	private String title;

	private int duration;

	public PowerupBar(String title, ChatColor chatColor, BarColor color, int duration) {
		bossBar = Bukkit.createBossBar(getTitleString((float) duration / 20), color, BarStyle.SOLID);

		this.chatColor = chatColor;
		this.title = title;
		this.duration = duration;
	}

	public void display(Player player) {
		bossBar.addPlayer(player);

		new BukkitRunnable() {

			float decrement = 1F / duration;

			int counter = 1;

			@Override
			public void run() {
				bossBar.setProgress(1F - counter * decrement);

				if (counter % 2 == 0) {
					bossBar.setTitle(getTitleString((float) duration / 20 - 0.1F *  (float) counter / 2));
				}

				if (counter == duration) {
					bossBar.removeAll();
					cancel();
				}

				counter++;
			}
		}.runTaskTimer(ZombiesPlugin.instance, 0L, 1L);
	}

	public String getTitleString(float seconds) {
		StringBuilder stringBuilder = (new StringBuilder())
				.append(chatColor)
				.append(ChatColor.BOLD)
				.append(title)
				.append(ChatColor.GRAY)
				.append(" - ")
				.append(ChatColor.GREEN)
				.append(ChatColor.BOLD)
				.append(String.format("%.1fs", seconds));

		return stringBuilder.toString();
	}

}
