package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import cz.jirutka.unidecode.Unidecode;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PlayerChatListener extends EventListener implements Listener {

	private Unidecode unidecoder = Unidecode.toAscii();

	private final String PLAYER_NAME_REPLACE = "PLAYER_NAME";

	private class AutoHelpTopic {
		private String topicName;
		private List<Pattern> helpPatterns;
		private String[] helpMessages;

		public AutoHelpTopic(String topicName, String[] helpRegexes, String[] helpMessages) {
			this.topicName = topicName;

			this.helpPatterns = new ArrayList<Pattern>();
			for (String regex : helpRegexes) {
				this.helpPatterns.add(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
			}

			this.helpMessages = helpMessages;
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

			String[] messages = this.helpMessages.clone();
			for (int i = 0; i < messages.length; ++i) {
				messages[i] = this.helpMessages[i].replaceAll(PLAYER_NAME_REPLACE, player.getDisplayName(false));
			}
			player.sendDelayedMessages(messages);
		}
	}

	private final AutoHelpTopic[] helpTopics = new AutoHelpTopic[]{
			new AutoHelpTopic("Groups",
					new String[]{".*(how|can you|can we|can i).* claim.*",
							".*(how|can you|can we|can i).* own land.*",
							".*(how|can you|can we|can i).* protect.*",
							".*(how|can you|can we|can i).* lock.*",
							".* faction.*"},
					new String[]{
							ChatColor.DARK_AQUA + "Hey " + ChatColor.BOLD + PLAYER_NAME_REPLACE +
									ChatColor.DARK_AQUA + ", need help with our custom plugin " +
									ChatColor.ITALIC + "Groups" + ChatColor.DARK_AQUA + "?",
							ChatColor.DARK_AQUA + "Try clicking on this link: " + ChatColor.UNDERLINE +
									"standardsurvival.com/help"
					}),

			new AutoHelpTopic("PVP protection",
					new String[]{".*on peaceful.*",
							".*los(e|ing) hunger.*"},
					new String[]{
							String.valueOf(ChatColor.DARK_AQUA) + ChatColor.BOLD + PLAYER_NAME_REPLACE +
									ChatColor.DARK_AQUA + ", you start off with one hour of" +
									" PVP and hunger protection.",
							ChatColor.DARK_AQUA + "Don't worry, you will receive several warnings" +
									" before it expires."
					}),

			new AutoHelpTopic("Generic",
					new String[]{".*help.*",
							".*how (do|can) (you|we|i).*",
							".*i(['`\"]?m| am) new.*"},
					new String[]{
							ChatColor.DARK_AQUA + "Hey " + ChatColor.BOLD + PLAYER_NAME_REPLACE +
									ChatColor.DARK_AQUA + ", need help? Try clicking on this link:",
							String.valueOf(ChatColor.DARK_AQUA) + ChatColor.UNDERLINE + "standardsurvival.com/help"
					})};

	public PlayerChatListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		String message = unidecoder.decode(event.getMessage()).toLowerCase();

		if (player.getTimeSpent() < 600) {
			for (int i = 0; i < helpTopics.length; i++) {
				if (helpTopics[i].matches(message)) {
					helpTopics[i].sendHelpMessage(player);
					return;
				}
			}
		}

		for (String str : plugin.getMutedWords()) {
			// TODO: use regex patterns
			if (message.contains(str.toLowerCase())) {
				player.sendMessage(ChatColor.RED + "You used a blocked word");
				event.setCancelled(true);
			}
		}
	}
}
