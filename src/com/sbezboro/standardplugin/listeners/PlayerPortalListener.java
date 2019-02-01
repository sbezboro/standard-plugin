package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.util.MiscUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
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
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerPortal(PlayerPortalEvent event) {
		World fromWorld = event.getFrom().getWorld();
		
		if (event.getCause() == TeleportCause.END_PORTAL) {
			// Going from the end
			if (fromWorld.getEnvironment() == Environment.THE_END) {
				StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

				plugin.getLogger().info(player.getDisplayName(false) + " leaving the end.");
				
				// Set event location to the player's bed if one exists
				Location to = player.getBedLocationIfValid();
				
				if (to == null) {
					plugin.getLogger().info("Can't find bed for " + player.getDisplayName(false) + ", sending to spawn.");
					to = plugin.getServer().getWorld(StandardPlugin.OVERWORLD_NAME).getSpawnLocation();

					if (player.getBedLocation() != null) {
						plugin.getLogger().info("Bed location points to (" +
								MiscUtil.locationFormat(player.getBedLocation()) + ") which is of type " +
								player.getBedLocation().getBlock().getType());
					}
				}

				event.setTo(to);
			// Going to the end
			} else {
				World newEnd = plugin.getEndResetManager().getNewEndWorld();
				StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

				if (newEnd != null) {
					event.setTo(new Location(newEnd, 100, 50, 0));
				}
				
				plugin.getLogger().info(player.getDisplayName(false) + " going to the end.");
			}
		}
	}

}
