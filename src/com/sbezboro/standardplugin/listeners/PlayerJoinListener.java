package com.sbezboro.standardplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
				player.setInPvp(player.getLastAttacker());
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						StandardPlugin.broadcast(String.format("%s%s %sis back after PVP logging", 
								ChatColor.AQUA, player.getDisplayName(), ChatColor.RED));
					}
				}.runTaskLater(plugin, 5);
			} else {
				player.setLastAttacker(null);
			}
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
		
		player.setEndId(currentEndId);
		
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
					
					StandardPlugin.playerBroadcast(player, player.getRankDescription(false, rank));
					player.sendMessage(player.getRankDescription(true, rank));
				}
			}

			@Override
			public void requestFailure(HttpResponse response) {
			}
		}));
	}
}
