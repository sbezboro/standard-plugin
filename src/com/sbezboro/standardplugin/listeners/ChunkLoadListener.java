package com.sbezboro.standardplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import com.sbezboro.standardplugin.StandardPlugin;

public class ChunkLoadListener extends EventListener implements Listener {

	public ChunkLoadListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onChunkLoadEvent(ChunkLoadEvent event) {
		if (event.isNewChunk()) {
			int x = event.getChunk().getX();
			int z = event.getChunk().getZ();
			String world = event.getChunk().getWorld().getName();
			plugin.getLogger().info("New chunk generated at ([" + world + "] " + x + " (" + (x << 4) + "), " + z + " (" + (z << 4) + "))");
		}
	}
}
