package com.sbezboro.standardplugin.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.sbezboro.standardplugin.StandardPlugin;

public class EntityDeathListener extends EventListener implements Listener {

	public EntityDeathListener(StandardPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		
		if (entity.getType() == EntityType.ENDER_DRAGON) {
			if (!plugin.isEndResetScheduled()) {
				plugin.scheduleNextEndReset();
			}
		}
	}
}
