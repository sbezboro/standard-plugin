package com.sbezboro.standardplugin.listeners;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class PlayerPortalListener extends EventListener implements Listener {

	public PlayerPortalListener(StandardPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler
	public void onPortalEvent(PlayerPortalEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Location from = event.getFrom();
		Biome biome = from.getWorld().getBiome(from.getBlockX(), from.getBlockZ());
		
		// Make sure this is someone teleporting from the end back to the main world
		if (event.getCause() == TeleportCause.END_PORTAL && biome == Biome.SKY) {
			StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());
			
			if (event.getTo().equals(event.getTo().getWorld().getSpawnLocation())) {
				if (player.getBedLocation() != null) {
					// Set event location to the player's bed
					event.setTo(player.getBedLocation());
				}
			}
		}
	}

}
