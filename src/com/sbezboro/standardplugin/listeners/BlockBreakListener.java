package com.sbezboro.standardplugin.listeners;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.sbezboro.standardplugin.StandardPlugin;

public class BlockBreakListener extends EventListener implements Listener {

	public BlockBreakListener(StandardPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Location location = event.getBlock().getLocation();
		
		// Don't let players destroy the obsidian spawn platform in the end
		if (location.getWorld().getEnvironment() == Environment.THE_END
				&& location.getBlockX() >= 98 && location.getBlockX() <= 102
				&& location.getBlockZ() >= -2 && location.getBlockZ() <= 2
				&& location.getBlockY() == 48) {
			event.setCancelled(true);
		}
	}

}
