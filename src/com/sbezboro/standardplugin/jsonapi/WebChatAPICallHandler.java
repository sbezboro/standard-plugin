package com.sbezboro.standardplugin.jsonapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class WebChatAPICallHandler extends APICallHandler {

	public WebChatAPICallHandler(StandardPlugin plugin) {
		super(plugin);
	}

	@Override
	public Object handle(Object[] args) {
		if (args.length == 0) {
			return false;
		}

		String type = (String) args[0];
		
		if (type.equals("message")) {
			return handleMessage(args);
		} else if (type.equals("enter")) {
			return handleStatus(args);
		} else if (type.equals("exit")) {
			return handleStatus(args);
		}
		
		return false;
	}
	
	private boolean handleMessage(Object[] args) {
		if (args.length != 3) {
			return false;
		}
		
		String username = (String) args[1];
		String message = (String) args[2];
		
		StandardPlayer player = plugin.getStandardPlayer(username);
		
		if (player.isBanned()) {
			plugin.getLogger().warning(username + " has been blocked from web chat because they are banned.");
			return true;
		}
		
		Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "[Web Chat] " + ChatColor.AQUA + player.getDisplayName() + ChatColor.RESET + ": " + message);
		
		return true;
	}
	
	private boolean handleStatus(Object[] args) {
		if (args.length != 2) {
			return false;
		}
		
		String type = (String) args[0];
		String username = (String) args[1];
		
		StandardPlayer player = plugin.getStandardPlayer(username);
		
		if (player.isBanned()) {
			plugin.getLogger().warning(username + " has been blocked from web chat because they are banned.");
			return true;
		}
		
		String message;
		
		if (type.equals("enter")) {
			message = ChatColor.BLUE + "[Web Chat] " + ChatColor.YELLOW + player.getDisplayName(false) + " has entered web chat";
		} else if (type.equals("exit")) {
			message = ChatColor.BLUE + "[Web Chat] " + ChatColor.YELLOW + player.getDisplayName(false) + " has left web chat";
		} else {
			return false;
		}
		
		Bukkit.getServer().broadcastMessage(message);
		
		return true;
	}

	@Override
	public String getName() {
		return "web_chat";
	}

}
