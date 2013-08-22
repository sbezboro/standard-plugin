package com.sbezboro.standardplugin.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class RespawnListener extends EventListener implements Listener {

	public RespawnListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		Location actualBed = player.getBedLocation();
		Location eventBed = player.getBedSpawnLocation();

		boolean bedSpawn = false;

		if (eventBed == null && actualBed != null) {
			Block bedBlock = actualBed.getBlock();

			// Bukkit tells us the bed is obstructed when in fact it's still there
			if (bedBlock.getType() == Material.BED_BLOCK) {
				bedSpawn = true;

				player.setBedSpawnLocation(actualBed);

				Block firstBlock = bedBlock.getRelative(BlockFace.UP);
				Block secondBlock = firstBlock.getRelative(BlockFace.UP);

				if (firstBlock.getType() == Material.AIR && secondBlock.getType() == Material.AIR) {
					event.setRespawnLocation(firstBlock.getLocation());
				} else {
					event.setRespawnLocation(bedBlock.getLocation());
				}

				plugin.getLogger().info("Bukkit said bed was missing, but it was actually there for " + player.getName());
			} else {
				player.saveBedLocation(null);

				plugin.getLogger().info("Bed was actually missing for " + player.getName());
			}
		}

		Location location = event.getRespawnLocation();

		if (!bedSpawn) {
			event.setRespawnLocation(new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
		}
	}
}
