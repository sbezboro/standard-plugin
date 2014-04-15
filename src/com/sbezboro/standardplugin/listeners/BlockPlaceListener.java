package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BlockPlaceListener extends EventListener implements Listener {

	public BlockPlaceListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockPlace(final org.bukkit.event.block.BlockPlaceEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());
		
		Location location = event.getBlock().getLocation();

		if (location.getWorld().getEnvironment() == World.Environment.NETHER &&
				location.getBlockY() >= 128) {
			event.setCancelled(true);
		}
	}

}
