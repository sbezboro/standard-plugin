package com.sbezboro.standardplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class PlayerLeaveListener extends EventListener implements Listener {

	public PlayerLeaveListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		event.setQuitMessage(String.format("%s%s has left the server", ChatColor.DARK_GRAY, player.getDisplayName(false)));

		player.onLeaveServer();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerKick(PlayerKickEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		event.setLeaveMessage(String.format("%s%s was kicked!", ChatColor.DARK_GRAY, player.getDisplayName(false)));

		player.onLeaveServer();
	}
}
