package com.sbezboro.standardplugin.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.sbezboro.standardplugin.StandardPlugin;

public class CreatureSpawnListener extends EventListener implements Listener {

	public CreatureSpawnListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler
	public void WitherSpawnEvent(CreatureSpawnEvent event) {
		EntityType type = event.getEntity().getType();
		if (type == EntityType.WITHER) {
			event.setCancelled(true);
		}
	}
	
}
