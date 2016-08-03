package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Dispenser;
import org.bukkit.material.MaterialData;
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
		
		if (event.getItem().getType() == Material.WATER_BUCKET) {
			MaterialData facingData = event.getBlock().getState().getData();
			BlockFace face = ((Dispenser)facingData).getFacing();
			Block targetBlock = event.getBlock().getRelative(face);
			if (targetBlock.getType() == Material.ENDER_PORTAL) {
				event.setCancelled(true);
			}
		}
	}

}
