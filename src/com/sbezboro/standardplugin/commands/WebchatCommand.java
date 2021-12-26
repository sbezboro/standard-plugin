package com.sbezboro.standardplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.json.simpleForBukkit.JSONArray;
import org.json.simpleForBukkit.JSONObject;

import com.sbezboro.http.HttpRequestManager;
import com.sbezboro.http.HttpResponse;
import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.net.WebchatHttpRequest;

public class WebchatCommand extends BaseCommand {

	public WebchatCommand(StandardPlugin plugin) {
		super(plugin, "webchat");
	}

	@Override
	public boolean handle(final CommandSender sender, Command command, String label, String[] args) {
		HttpRequestManager.getInstance().startRequest(
				new WebchatHttpRequest(new HttpRequestListener() {
			
			@Override
			public void requestSuccess(HttpResponse response) {
				JSONObject data = response.getJsonResponse();
				JSONArray users = (JSONArray) data.get("users");
				
				sender.sendMessage(String.format("%sThere %s %s%d%s player%s on web chat.", ChatColor.GOLD,
						users.size() != 1 ? "are" : "is", ChatColor.RED, users.size(), 
						ChatColor.GOLD, users.size() != 1 ? "s" : ""));
				
				String message = "";
				for (int i = 0; i < users.size(); i++) {
					JSONObject object = (JSONObject) users.get(i);
					
					String uuid = (String) object.get("uuid");
					StandardPlayer player = plugin.getStandardPlayerByUUID(uuid);
					
					message += ChatColor.WHITE + player.getDisplayName();
					if (i < users.size() - 1) {
						message += ChatColor.RESET + ", ";
					}
				}
				
				if (!message.isEmpty()) {
					sender.sendMessage(message);
				}
			}
			
			@Override
			public void requestFailure(HttpResponse response) {
			}
		}));
		
		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {

	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return false;
	}

}
