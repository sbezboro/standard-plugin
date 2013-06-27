package com.sbezboro.standardplugin.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

import com.sbezboro.standardplugin.StandardPlugin;

public class DispenseListener extends EventListener implements Listener {

	public DispenseListener(StandardPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler
	public void onBlockDispense(BlockDispenseEvent event) {
		if (event.getItem().getTypeId() == Material.LAVA_BUCKET.getId()) {
			event.setCancelled(true);
		}
	}

}
