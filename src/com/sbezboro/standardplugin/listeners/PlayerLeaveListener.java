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

		String quitMessage = ChatColor.DARK_GRAY + ChatColor.stripColor(event.getQuitMessage()).replaceAll(player.getName(), player.getDisplayName(false));

		event.setQuitMessage(quitMessage);

		player.onLeaveServer();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerKick(PlayerKickEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		String quitMessage = ChatColor.DARK_GRAY + player.getDisplayName(false) + " was kicked!";

		event.setLeaveMessage(quitMessage);

		player.onLeaveServer();
	}
}
