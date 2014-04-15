package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class PlayerTeleportListener extends EventListener implements Listener {

	public PlayerTeleportListener(StandardPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Location to = event.getTo();

		if (event.getCause() == TeleportCause.ENDER_PEARL &&
				to.getWorld().getEnvironment() == Environment.NETHER &&
				event.getTo().getBlockY() >= 124) {
			event.setCancelled(true);
		}
	}

}
