package com.sbezboro.standardplugin.listeners;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.util.MiscUtil;

public class CreatureSpawnListener extends EventListener implements Listener {

	public CreatureSpawnListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		World world = entity.getLocation().getWorld();
		
		// Only allow withers to be spawned in the end
		if (entity.getType() == EntityType.WITHER) {
			if (world.getEnvironment() == Environment.THE_END) {
				plugin.getLogger().info("Wither spawned at " + MiscUtil.locationFormat(entity.getLocation()));
			} else {
				event.setCancelled(true);
			}
		}
	}

}
