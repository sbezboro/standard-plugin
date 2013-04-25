package com.sbezboro.standardplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.sbezboro.standardplugin.StandardPlugin;

public class PlayerQuitListener extends EventListener implements Listener {
	
	public PlayerQuitListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
    	Player player = event.getPlayer();
    	String quitMessage = ChatColor.DARK_GRAY + ChatColor.stripColor(event.getQuitMessage());
    	
    	if (player != null) {
    		quitMessage = quitMessage.replaceAll(player.getName(), ChatColor.stripColor(player.getDisplayName()));
    	}
    	
    	event.setQuitMessage(quitMessage);
	}
}
