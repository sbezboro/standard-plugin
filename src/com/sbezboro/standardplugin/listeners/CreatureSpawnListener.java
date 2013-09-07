package com.sbezboro.standardplugin.listeners;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.sbezboro.standardplugin.StandardPlugin;

public class CreatureSpawnListener extends EventListener implements Listener {

	public CreatureSpawnListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		World world = entity.getLocation().getWorld();
		
		// Only allow withers to be spawned in the end
		if (entity.getType() == EntityType.WITHER && world.getEnvironment() != Environment.THE_END) {
			event.setCancelled(true);
		}
	}

}
