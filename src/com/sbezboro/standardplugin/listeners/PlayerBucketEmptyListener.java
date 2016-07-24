package com.sbezboro.standardplugin.listeners;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import com.sbezboro.standardplugin.StandardPlugin;

public class PlayerBucketEmptyListener extends EventListener implements Listener {
	public PlayerBucketEmptyListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		Block block = event.getBlockClicked().getRelative(event.getBlockFace());

		Location location = block.getLocation();

		if (location.getWorld().getEnvironment() == Environment.THE_END) {
			if (Math.abs(block.getX()) <= 3 && Math.abs(block.getZ()) <= 3) {
				// Prevent glitch-destroying the exit portal from the End
				event.setCancelled(true);
			}
		}
	}
}
