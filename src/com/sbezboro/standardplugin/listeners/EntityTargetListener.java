package com.sbezboro.standardplugin.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import com.sbezboro.standardplugin.StandardPlugin;

public class EntityTargetListener extends EventListener implements Listener {

	public EntityTargetListener(StandardPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onEntityTarget(EntityTargetEvent event) {
		Entity source = event.getEntity();
		Entity target = event.getTarget();

		// Try to mitigate server lag caused by zombie pathfinding to unreachable villagers
		if (source != null && source.getType() == EntityType.ZOMBIE && target != null && target.getType() == EntityType.VILLAGER) {
			event.setCancelled(true);
		}
	}

}
