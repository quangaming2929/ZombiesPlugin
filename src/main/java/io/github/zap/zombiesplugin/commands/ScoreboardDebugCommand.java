package io.github.zap.zombiesplugin.commands;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.scoreboard.IngameStatePanel;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardDebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings[0].equals("show") && commandSender instanceof Player) {
            Player player = (Player) commandSender;
            List<User> user = new ArrayList<>();

            for(Player p : player.getWorld().getPlayers()) {
                user.add(new User(p));
            }

            IngameStatePanel pnl = new IngameStatePanel(user, "Test Map Bro", "04/01/20");
            pnl.show();
            new BukkitRunnable() {
                @Override
                public void run() {
                    pnl.setTime("10:00:00");
                    pnl.setGameOver();
                }
            }.runTaskLater(ZombiesPlugin.instance, 10);

        }
        return false;
    }
}
