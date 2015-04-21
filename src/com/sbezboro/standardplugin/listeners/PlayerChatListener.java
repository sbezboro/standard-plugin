package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Pattern;

public class PlayerChatListener extends EventListener implements Listener {

	private final Pattern[] helpPatterns = new Pattern[] {
			Pattern.compile(".*help.*", Pattern.CASE_INSENSITIVE),
			Pattern.compile(".*how do i.*", Pattern.CASE_INSENSITIVE),
			Pattern.compile(".*i['`\"]?m new.*", Pattern.CASE_INSENSITIVE),
	};

	public PlayerChatListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		String message = event.getMessage();

		if (player.getTimeSpent() < 600 && matchesHelpPattern(message)) {
			plugin.getLogger().info("Sending help message to " + player.getDisplayName(false));

			player.sendDelayedMessages(new String[] {
					ChatColor.GRAY + "Hey " + ChatColor.BOLD + player.getDisplayName(false) +
							ChatColor.GRAY + ", need help? Try clicking on this link:",
					ChatColor.GRAY + "standardsurvival.com/help"
			});
		}
	}

	private boolean matchesHelpPattern(String message) {
		for (Pattern pattern : helpPatterns) {
			if (pattern.matcher(message).matches()) {
				return true;
			}
		}

		return false;
	}
	
}
