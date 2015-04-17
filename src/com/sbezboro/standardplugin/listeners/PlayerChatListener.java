package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener extends EventListener implements Listener {

	public PlayerChatListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		String message = event.getMessage();

		if (player.getTimeSpent() < 120 && (
				message.equalsIgnoreCase("help") ||
				message.toLowerCase().contains("how do i")
		)) {
			plugin.getLogger().info("Sending help message to " + player.getDisplayName(false));

			player.sendDelayedMessages(new String[] {
					ChatColor.GRAY + "Hey " + ChatColor.BOLD + player.getDisplayName(false) +
							ChatColor.GRAY + ", need help? Try clicking on this link:",
					ChatColor.GRAY + "standardsurvival.com/help"
			});
		}
	}
	
}
