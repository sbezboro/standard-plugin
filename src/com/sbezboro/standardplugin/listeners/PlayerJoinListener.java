package com.sbezboro.standardplugin.listeners;

import com.sbezboro.http.HttpRequestManager;
import com.sbezboro.http.HttpResponse;
import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.integrations.SimplyVanishIntegration;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.model.Title;
import com.sbezboro.standardplugin.net.JoinHttpRequest;
import com.sbezboro.standardplugin.net.RankHttpRequest;
import com.sbezboro.standardplugin.util.MiscUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerJoinListener extends EventListener implements Listener {

	public PlayerJoinListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		final StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		int currentEndId = plugin.getEndResetStorage().getCurrentEndId();
		
		if (player.hasPlayedBefore()) {
			if (!SimplyVanishIntegration.isVanished(player)) {
				broadcastRank(player);
			}

			World playerWorld = player.getWorld();
			// Check to see if the player is joining into an end world that was reset
			if (playerWorld.getEnvironment() == Environment.THE_END && player.getEndId() < currentEndId) {
				World overworld = plugin.getServer().getWorld(StandardPlugin.OVERWORLD_NAME);
				player.sendHome(overworld);
			}
			
			if (player.hasPvpLogged()) {
				player.setPvpLogged(false);
				player.setNotInPvp();
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (!player.isOnline()) {
							return;
						}

						if (player.isDead()) {
							player.sendMessage(ChatColor.RED + "You were killed for PVP logging");
							StandardPlugin.playerBroadcast(player, String.format("%s%s %sis back after PVP logging", 
									ChatColor.AQUA, player.getDisplayName(), ChatColor.RED));
						} else {
							StandardPlugin.broadcast(String.format("%s%s %sis back after PVP logging", 
									ChatColor.AQUA, player.getDisplayName(), ChatColor.RED));
							
							if (!player.hasTitle(Title.PVP_LOGGER)) {
								if (player.getPvpLogs() < plugin.getPvpLogThreshold()) {
									int pvpLogsLeft = plugin.getPvpLogThreshold() - player.getPvpLogs();
									player.sendMessage(String.format("%sWARNING! You will be given the %sPVP Logger %stitle if you PVP log %d more %s!", 
											ChatColor.RED, ChatColor.AQUA, ChatColor.RED, pvpLogsLeft, MiscUtil.pluralize("time", pvpLogsLeft)));
								} else {
									Title title = player.addTitle(Title.PVP_LOGGER);
									
									StandardPlugin.broadcast(String.format("%s%s %shas automatically been bestowed the %s%s %stitle after PVP logging at least %d times!", 
											ChatColor.AQUA, player.getDisplayName(), ChatColor.RED, ChatColor.AQUA, title.getDisplayName(), ChatColor.RED, plugin.getPvpLogThreshold()));
								}
							}
						}
					}
				}.runTaskLater(plugin, 5);
			} else {
				player.setLastAttacker(null);
			}

			player.sendTitleMessage("Welcome back, " + player.getDisplayName() + ChatColor.RESET + "!");
		} else {
			String welcomeMessage = String.format("%sWelcome %s to the server!", ChatColor.LIGHT_PURPLE, player.getName());
			StandardPlugin.playerBroadcast(player, welcomeMessage);
			
			World world = player.getLocation().getWorld();
			Location spawnLocation = world.getSpawnLocation();
			Location newSpawnLocation = new Location(world, spawnLocation.getX() + 0.5, spawnLocation.getY(), spawnLocation.getZ() + 0.5);
			player.teleport(newSpawnLocation);

			if (plugin.isPvpProtectionEnabled()) {
				player.setPvpProtection(true);
			}

			player.sendTitleMessage("Welcome, " + player.getDisplayName() + ChatColor.RESET + "!");
		}

		String message;
		if (player.hasNickname()) {
			message = String.format("%s%s (%s) has joined the server", ChatColor.GREEN, player.getDisplayName(false), player.getName());
		} else {
			message = String.format("%s%s has joined the server", ChatColor.GREEN, player.getDisplayName(false));
		}
		
		if (!SimplyVanishIntegration.isVanished(player)) {
			StandardPlugin.webchatMessage(message);
		}

		broadcastDuplicateIP(player);
		
		player.setEndId(currentEndId);
		
		event.setJoinMessage(message);

		joinServerEvent(player);
	}

	private void broadcastRank(final StandardPlayer player) {
		HttpRequestManager.getInstance().startRequest(new RankHttpRequest(player, new HttpRequestListener() {

			@Override
			public void requestSuccess(HttpResponse response) {
				if (response.isApiSuccess()) {
					int rank = response.getInt("rank");
					int timeSpent = response.getInt("minutes");
					ArrayList<HashMap<String, Object>> titles = (ArrayList<HashMap<String, Object>>) response.getJsonResponse().get("titles");

					player.setRank(rank);
					player.setTimeSpent(timeSpent);

					plugin.getTitleStorage().addTitles(titles, player);

					StandardPlugin.playerBroadcast(player, player.getRankDescription(false, rank));
					player.sendMessage(player.getRankDescription(true, rank));
				}
			}

			@Override
			public void requestFailure(HttpResponse response) {
				// Do nothing
			}
		}));
	}

	private void broadcastDuplicateIP(final StandardPlayer player) {
		final List<String> duplicateIPUsernames = new ArrayList<String>();

		for (StandardPlayer otherPlayer : plugin.getOnlinePlayers()) {
			if (player != otherPlayer && otherPlayer.getAddress().getAddress().getHostAddress().equals(
					player.getAddress().getAddress().getHostAddress()
			)) {
				duplicateIPUsernames.add(otherPlayer.getDisplayName());
			}
		}

		if (!duplicateIPUsernames.isEmpty()) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					String message = ChatColor.DARK_RED + "[IP Address] " + ChatColor.AQUA +
							player.getDisplayName() + ChatColor.RED + " shares IP address with: " + StringUtils.join(
								duplicateIPUsernames, ChatColor.RED + "," + ChatColor.AQUA);

					for (StandardPlayer otherPlayer : plugin.getOnlinePlayers()) {
						if (player != otherPlayer && otherPlayer.hasPermission("standardplugin.moderator")) {
							otherPlayer.sendMessage(message);
						}
					}

					plugin.getServer().getConsoleSender().sendMessage(message);
				}
			}, 20);
		}
	}

	private void joinServerEvent(final StandardPlayer player) {
		HttpRequestManager.getInstance().startRequest(new JoinHttpRequest(player.getUuidString(), new HttpRequestListener() {

			@Override
			public void requestSuccess(HttpResponse response) {
				if (!player.isOnline()) {
					return;
				}

				JSONObject data = response.getJsonResponse();
				Map<String, Object> playerMessages = (Map<String, Object>) data.get("player_messages");
				Map<String, Object> playerNotifications = (Map<String, Object>) data.get("player_notifications");
				Boolean noUser = (Boolean) data.get("no_user");

				notifyNewEvents(player, playerMessages, playerNotifications, noUser);
			}

			@Override
			public void requestFailure(HttpResponse response) {
				// Do nothing
			}
		}));
	}

	private boolean notifyMessages(StandardPlayer player, Map<String, Object> playerMessages) {
		Long numNewMessages = (Long) playerMessages.get("num_new_messages");
		List<String> fromUuids = (List<String>) playerMessages.get("from_uuids");
		String url = (String) playerMessages.get("url");

		if (numNewMessages == 0) {
			return false;
		}

		String message = "";
		message += "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "You have " +
				ChatColor.YELLOW + ChatColor.BOLD +	numNewMessages +
				ChatColor.DARK_GREEN + ChatColor.BOLD + " unread " +
				MiscUtil.pluralize("message", numNewMessages) + "! ";

		if (!fromUuids.isEmpty()) {
			message += ChatColor.RESET + "(From: ";

			String names = "";
			for (String uuid : fromUuids) {
				StandardPlayer fromPlayer = plugin.getStandardPlayerByUUID(uuid);
				if (names.length() > 0) {
					names += ", ";
				}
				names += fromPlayer.getDisplayName(true) + ChatColor.RESET;
			}

			message += names;
			message += ")";
		}

		player.sendMessage(message);
		player.sendMessage(ChatColor.DARK_GREEN + "Click here: " + ChatColor.AQUA + url);

		return true;
	}

	private boolean notifyNotifications(StandardPlayer player, Map<String, Object> playerNotifications) {
		Long numNewNotifications = (Long) playerNotifications.get("num_new_notifications");
		String url = (String) playerNotifications.get("url");

		if (numNewNotifications == 0) {
			return false;
		}

		player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "You have " +
				ChatColor.YELLOW + ChatColor.BOLD +	numNewNotifications +
				ChatColor.DARK_GREEN + ChatColor.BOLD + " unread " +
				MiscUtil.pluralize("notification", numNewNotifications) + "! ");
		player.sendMessage(ChatColor.DARK_GREEN + "Click here: " + ChatColor.AQUA + url);

		return true;
	}

	private void notifyNewEvents(final StandardPlayer player,
								 final Map<String, Object> playerMessages,
								 final Map<String, Object> playerNotifications,
								 final boolean noUser) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

			@Override
			public void run() {
				if (!player.isOnline()) {
					return;
				}

				boolean hasMessages = notifyMessages(player, playerMessages);
				boolean hasNotifications = notifyNotifications(player, playerNotifications);

				if (noUser && (hasMessages || hasNotifications)) {
					player.sendMessage(ChatColor.RED + "You will need to create a website account first by typing /register");
				}
			}
		}, 80);
	}
}
