package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.HashSet;

public class PlayerTeleportListener extends EventListener implements Listener {

	private static final HashSet<Material> blockedPearlMaterials = new HashSet<Material>() {{
		add(Material.IRON_BARS);
		add(Material.ACACIA_FENCE);
		add(Material.BIRCH_FENCE);
		add(Material.DARK_OAK_FENCE);
		add(Material.JUNGLE_FENCE);
		add(Material.OAK_FENCE);
		add(Material.SPRUCE_FENCE);
		add(Material.NETHER_BRICK_FENCE);
		add(Material.ACACIA_STAIRS);
		add(Material.BIRCH_STAIRS);
		add(Material.BRICK_STAIRS);
		add(Material.COBBLESTONE_STAIRS);
		add(Material.DARK_OAK_STAIRS);
		add(Material.JUNGLE_STAIRS);
		add(Material.NETHER_BRICK_STAIRS);
		add(Material.QUARTZ_STAIRS);
		add(Material.SANDSTONE_STAIRS);
		add(Material.STONE_BRICK_STAIRS);
		add(Material.SPRUCE_STAIRS);
	}};

	public PlayerTeleportListener(StandardPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Location to = event.getTo();

		if (event.getCause() == TeleportCause.ENDER_PEARL) {
			if (to.getWorld().getEnvironment() == Environment.NETHER &&
					event.getTo().getBlockY() >= 124) {
				event.setCancelled(true);
				return;
			}

			if (blockedPearlMaterials.contains(to.getBlock().getType())) {
				event.setCancelled(true);
			}
		}
	}

}
