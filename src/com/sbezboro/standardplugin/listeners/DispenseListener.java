package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

public class DispenseListener extends EventListener implements Listener {

	public DispenseListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockDispense(BlockDispenseEvent event) {
		if (event.getItem().getType() == Material.LAVA_BUCKET) {
			event.setCancelled(true);
		}
	}

}
