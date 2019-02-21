package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

public class BlockFormListener extends EventListener implements Listener {

	public BlockFormListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler
	public void onBlockForm(BlockFormEvent event) {
		Block block = event.getNewState().getBlock();
		Material material = block.getType();

		// Prevent cobble monsters
		if (material.equals(Material.LAVA)) {
			Levelled l = (Levelled) block.getBlockData();
			if (l.getLevel() != 0) {
				event.setCancelled(true);
			}
		} else if (material.equals(Material.WATER)) {
			Levelled l = (Levelled) block.getBlockData();
			if (l.getLevel() == 0) {
				event.setCancelled(true);
			}
		}
	}
}