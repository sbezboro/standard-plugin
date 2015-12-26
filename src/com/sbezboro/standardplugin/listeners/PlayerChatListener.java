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

	private final Pattern[] genericHelpPatterns = new Pattern[] {
		Pattern.compile(".*help.*", Pattern.CASE_INSENSITIVE),
		Pattern.compile(".*how (do|can) (you|we|i).*", Pattern.CASE_INSENSITIVE),
		Pattern.compile(".*i(['`\"]?m| am) new.*", Pattern.CASE_INSENSITIVE),
	};
	
	private final Pattern[] groupHelpPatterns = new Pattern[] {
		Pattern.compile(".*(how|can you|can we|can i).* claim.*", Pattern.CASE_INSENSITIVE),
		Pattern.compile(".*(how|can you|can we|can i).*own land.*", Pattern.CASE_INSENSITIVE),
		Pattern.compile(".*(how|can you|can we|can i).* protect .*", Pattern.CASE_INSENSITIVE),
		Pattern.compile(".*(how|can you|can we|can i).* lock chest.*", Pattern.CASE_INSENSITIVE),
		Pattern.compile(".* faction.*", Pattern.CASE_INSENSITIVE),
	};
	
	private final Pattern[] protectionHelpPatterns = new Pattern[] {
		Pattern.compile(".*on peaceful.*", Pattern.CASE_INSENSITIVE),
		Pattern.compile(".*los(e|ing) hunger.*", Pattern.CASE_INSENSITIVE),
	};

	public PlayerChatListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		String message = event.getMessage();

		if (player.getTimeSpent() < 600) {
			if (matchesGroupHelpPattern(message)) {
				plugin.getLogger().info("Sending group help message to " + player.getDisplayName(false));

				player.sendDelayedMessages(new String[] {
						ChatColor.GRAY + "Hey " + ChatColor.BOLD + player.getDisplayName(false) +
								ChatColor.GRAY + ", need help with our custom plugin " +
								ChatColor.ITALIC + "Groups" + ChatColor.GRAY + "?",
						ChatColor.GRAY + "Try clicking on this link: standardsurvival.com/guide"
				});
			}
			
			else if (matchesProtectionHelpPattern(message)) {
				if (player.isPvpProtected()) {
					plugin.getLogger().info("Sending PVP protection help message to " + player.getDisplayName(false));
					
					player.sendDelayedMessages(new String[] {
							ChatColor.GRAY + ChatColor.BOLD + player.getDisplayName(false) +
									ChatColor.GRAY + ", you start off with one hour of" +
									" PVP and hunger protection.",
							ChatColor.GRAY + "Don't worry, you will receive several warnings" +
									" before it expires."
					});
				}
			}
			
			else if (matchesGenericHelpPattern(message)) {
				plugin.getLogger().info("Sending generic help message to " + player.getDisplayName(false));

				player.sendDelayedMessages(new String[] {
						ChatColor.GRAY + "Hey " + ChatColor.BOLD + player.getDisplayName(false) +
								ChatColor.GRAY + ", need help? Try clicking on this link:",
						ChatColor.GRAY + "standardsurvival.com/guide"
				});
			}
		}
	}

	private boolean matchesGroupHelpPattern(String message) {
		for (Pattern pattern : groupHelpPatterns) {
			if (pattern.matcher(message).matches()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean matchesProtectionHelpPattern(String message) {
		for (Pattern pattern : protectionHelpPatterns) {
			if (pattern.matcher(message).matches()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean matchesGenericHelpPattern(String message) {
		for (Pattern pattern : genericHelpPatterns) {
			if (pattern.matcher(message).matches()) {
				return true;
			}
		}
		return false;
	}
	
}
