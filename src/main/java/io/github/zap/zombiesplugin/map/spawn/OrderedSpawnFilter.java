package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.utils.OrderedIterator;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.HashSet;

public class OrderedSpawnFilter extends SpawnFilter {
	@Override
	public void spawn(GameManager manager, ArrayList<MythicMob> mobs, ArrayList<Room> rooms) {
		super.spawn(manager, mobs, rooms);
	}
}
