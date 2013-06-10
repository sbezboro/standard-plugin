package com.sbezboro.standardplugin.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class PlayerInteractListener extends EventListener implements Listener {
	
	public PlayerInteractListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(ignoreCancelled=true)
	public void onPlayerInteract(final PlayerInteractEvent event) {
		switch (event.getAction()) {
		case RIGHT_CLICK_BLOCK:
			Block block = event.getClickedBlock();

			StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());
			if (block.getTypeId() == Material.BED_BLOCK.getId()) {
				Location location = block.getLocation();
				player.saveBedLocation(location);
			}
			break;
		}
	}
}
