package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

public class EntityPortalListener extends EventListener implements Listener {

	public EntityPortalListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityPortalEnter(EntityPortalEvent event) {
		EntityType entityType = event.getEntityType();
		Location location = event.getFrom();

		if (location.getWorld().getEnvironment() == World.Environment.NORMAL &&
				entityType == EntityType.ZOMBIFIED_PIGLIN || entityType == EntityType.MINECART_TNT || entityType == EntityType.PRIMED_TNT) {
			event.setCancelled(true);
		}
	}
}