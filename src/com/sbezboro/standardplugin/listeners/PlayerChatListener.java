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
	
	private class AutoHelpTopic {
		private String topicName;
		private Pattern[] helpPatterns;
		private String[] helpMessage;
		
		public AutoHelpTopic(String name, String[] regExpressions, String[] messageLines) {
			topicName = name;
			
			helpPattern = new Pattern[regExpressions.length()];
			for (int i = 0; i < regExpressions.length(); i++) {
				helpPattern[i] = Pattern.compile(regExpressions[i], Pattern.CASE_INSENSITIVE);
			}
			
			helpMessage = new String[messageLines.length()];
			System.arraycopy(messageLines, 0, helpMessage, 0, messageLines.length());
		}
		
		public boolean matches(String message) {
			for (Pattern pattern : helpPatterns) {
				if (pattern.matcher(message).matches()) {
					return true;
				}
			}
			return false;
		}
		
		public void sendHelpMessage(StandardPlayer player) {
			plugin.getLogger().info("Sending " + topicName + " help message to " + player.getDisplayName(false));
			player.sendDelayedMessages(helpMessage);
		}
	}
	
	private final AutoHelpTopic helpTopics = new AutoHelpTopic[] {
		new AutoHelpTopic("Groups",
			new String[] { ".*(how|can you|can we|can i).* claim.*",
				".*(how|can you|can we|can i).* own land.*",
				".*(how|can you|can we|can i).* protect .*",
				".*(how|can you|can we|can i).* lock chest.*",
				".* faction.*" },
			new String[] {
				ChatColor.GRAY + "Hey " + ChatColor.BOLD + player.getDisplayName(false) +
					ChatColor.GRAY + ", need help with our custom plugin " +
					ChatColor.ITALIC + "Groups" + ChatColor.GRAY + "?",
				ChatColor.GRAY + "Try clicking on this link: standardsurvival.com/help"
		}),
		
		new AutoHelpTopic("PVP protection",
			new String[] { ".*on peaceful.*",
				".*los(e|ing) hunger.*" },
			new String[] {
				ChatColor.GRAY + ChatColor.BOLD + player.getDisplayName(false) +
					ChatColor.GRAY + ", you start off with one hour of" +
					" PVP and hunger protection.",
				ChatColor.GRAY + "Don't worry, you will receive several warnings" +
					" before it expires."
		}),
		
		new AutoHelpTopic("generic",
			new String[] { ".*help.*",
				".*how (do|can) (you|we|i).*",
				".*how (do|can) (you|we|i).*" },
			new String[] {
				ChatColor.GRAY + "Hey " + ChatColor.BOLD + player.getDisplayName(false) +
					ChatColor.GRAY + ", need help with our custom plugin " +
					ChatColor.ITALIC + "Groups" + ChatColor.GRAY + "?",
				ChatColor.GRAY + "Try clicking on this link: standardsurvival.com/help"
	}) };


	public PlayerChatListener(StandardPlugin plugin) {
		super(plugin);
	}


	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		String message = event.getMessage();

		if (player.getTimeSpent() < 600) {
			for (int i = 0; i < helpTopics.length(); i++) {
				if (helpTopics[i].matches(message)) {
					helpTopics[i].sendHelpMessage(player);
					return;
				}
			}
		}
	}
}
