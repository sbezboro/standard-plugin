package com.sbezboro.standardplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.sbezboro.http.HttpRequestManager;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.integrations.SimplyVanishIntegration;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.model.Title;
import com.sbezboro.standardplugin.net.LeaveHttpRequest;

public class PlayerLeaveListener extends EventListener implements Listener {

	public PlayerLeaveListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		String message = String.format("%s%s has left the server", ChatColor.DARK_GRAY, player.getDisplayName(false));
		event.setQuitMessage(message);

		if (!SimplyVanishIntegration.isVanished(player)) {
			StandardPlugin.webchatMessage(message);
		}
		
		if (player.isInPvp()) {
			player.setPvpLogged(true);
			
			player.incrementPvpLogs();

			StandardPlugin.broadcast(String.format("%s%s %sPVP logged to %s%s%s!",
					ChatColor.AQUA, player.getDisplayName(), ChatColor.RED, ChatColor.AQUA, 
					player.getLastAttacker().getDisplayName(), ChatColor.RED));

			if (player.hasTitle(Title.PVP_LOGGER)) {
				player.damage(1000.0);
			}
		}

		player.onLeaveServer();
		
		HttpRequestManager.getInstance().startRequest(new LeaveHttpRequest(player.getName(), null));
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		String message = String.format("%s%s was kicked!", ChatColor.DARK_GRAY, player.getDisplayName(false));
		event.setLeaveMessage(message);

		if (!SimplyVanishIntegration.isVanished(player)) {
			StandardPlugin.webchatMessage(message);
		}

		player.onLeaveServer();
		
		HttpRequestManager.getInstance().startRequest(new LeaveHttpRequest(player.getName(), null));
	}
}
