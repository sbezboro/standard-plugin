package com.sbezboro.standardplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.sbezboro.http.HttpRequestManager;
import com.sbezboro.http.HttpResponse;
import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.integrations.SimplyVanishIntegration;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.model.Title;
import com.sbezboro.standardplugin.net.RankHttpRequest;

public class PlayerJoinListener extends EventListener implements Listener {

	public PlayerJoinListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		if (player.hasPlayedBefore()) {
			if (!SimplyVanishIntegration.isVanished(player)) {
				broadcastRank(player);
			}
		} else {
			String welcomeMessage = String.format("%sWelcome %s to the server!", ChatColor.LIGHT_PURPLE, player.getName());
			StandardPlugin.playerBroadcast(player, welcomeMessage);
			plugin.webChatLog(welcomeMessage);
			
			World world = player.getLocation().getWorld();
			player.teleport(world.getSpawnLocation());

			if (plugin.isPvpProtectionEnabled()) {
				player.setPvpProtection(true);
			}
		}

		String message;
		if (player.hasNickname()) {
			message = String.format("%s%s (%s) has joined the server", ChatColor.GREEN, player.getDisplayName(false), player.getName());
		} else {
			message = String.format("%s%s has joined the server", ChatColor.GREEN, player.getDisplayName(false));
		}
		
		if (!SimplyVanishIntegration.isVanished(player)) {
			plugin.webChatLog(message);
		}
		
		event.setJoinMessage(message);
	}

	private void broadcastRank(final StandardPlayer player) {
		HttpRequestManager.getInstance().startRequest(new RankHttpRequest(player.getName(), true, new HttpRequestListener() {

			@Override
			public void requestSuccess(HttpResponse response) {
				int result = response.getInt("result");
				if (result == 1) {
					int rank = response.getInt("rank");
					int veteranRank = response.getInt("veteran_rank");
					int timeSpent = response.getInt("minutes");

					player.setRank(rank);
					player.setTimeSpent(timeSpent);

					if (veteranRank > 0) {
						if (veteranRank <= 10) {
							if (!player.isTop10Veteran()) {
								player.addTitle(Title.TOP10_VETERAN);
							}
						} else if (veteranRank <= 40) {
							if (!player.isTop40Veteran()) {
								player.addTitle(Title.TOP40_VETERAN);
							}
						} else if (!player.isVeteran()) {
							player.addTitle(Title.VETERAN);
						}
					}

					for (StandardPlayer other : plugin.getOnlinePlayers()) {
						other.sendMessage(player.getRankDescription(player == other, rank));
					}
				}
			}

			@Override
			public void requestFailure(HttpResponse response) {
			}
		}));
	}
}
