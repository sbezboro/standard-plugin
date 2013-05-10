package com.sbezboro.standardplugin.listeners;

import me.asofold.bpl.simplyvanish.SimplyVanish;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.sbezboro.http.HttpResponse;
import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.net.RankHttpRequest;
import com.sbezboro.standardplugin.util.MiscUtil;

public class PlayerJoinListener extends EventListener implements Listener {
	
	public PlayerJoinListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
    	StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());
    	
    	if (player.hasPlayedBefore()) {
        	if (!SimplyVanish.isVanished(player)) {
        		broadcastRank(player);
        	}
    	} else {
    		StandardPlugin.playerBroadcast(player, ChatColor.LIGHT_PURPLE + "Welcome " + player.getName() + " to the server!");
    		
    		World world = player.getLocation().getWorld();
    		player.teleport(world.getSpawnLocation());
    	}
    	
    	if (player.getBedSpawnLocation() != null) {
    		plugin.getBedData().setLocation(player, player.getBedSpawnLocation());
    	}
    	
    	if (!player.getName().equals(ChatColor.stripColor(player.getDisplayName()))) {
        	String joinMessage = event.getJoinMessage().replace(player.getName(), ChatColor.stripColor(player.getDisplayName()) + " (" + player.getName() + ")");
        	event.setJoinMessage(joinMessage);
    	}
	}
	
	private void broadcastRank(final StandardPlayer player) {
    	RankHttpRequest request = new RankHttpRequest(player.getName(), true);
    	request.start(new HttpRequestListener() {
			
			@Override
			public void requestSuccess(HttpResponse response) {
				int result = response.getInt("result");
				if (result == 1) {
					int rank = response.getInt("rank");
					
					String name = player.getDisplayName();
					for (StandardPlayer other : plugin.getOnlinePlayers()) {
						if (player == other) {
							player.sendMessage("You are ranked " + ChatColor.AQUA + MiscUtil.getRankString(rank) + ChatColor.WHITE + " on the server!");
						} else {
							other.sendMessage(ChatColor.AQUA + name + ChatColor.WHITE + " is ranked " + ChatColor.AQUA + MiscUtil.getRankString(rank) + ChatColor.WHITE + " on the server!");
						}
					}
				}
			}
			
			@Override
			public void requestFailure(HttpResponse response) {
				StandardPlugin.getPlugin().getLogger().severe(response.getStringResponse());
			}
		});
	}
}
