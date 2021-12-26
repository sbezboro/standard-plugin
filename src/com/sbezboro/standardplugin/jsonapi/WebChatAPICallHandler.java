package com.sbezboro.standardplugin.jsonapi;

import java.util.HashMap;

import com.sbezboro.standardplugin.SubPlugin;
import org.bukkit.ChatColor;
import org.json.simpleForBukkit.JSONObject;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.integrations.EssentialsIntegration;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class WebChatAPICallHandler extends APICallHandler {

	public WebChatAPICallHandler(StandardPlugin plugin) {
		super(plugin, "web_chat");
	}

	@Override
	public JSONObject handle(HashMap<String, Object> payload) {
		String type = (String) payload.get("type");
		String username = (String) payload.get("username");
		String uuid = (String) payload.get("uuid");

		StandardPlayer player = null;

		if (uuid != null && !uuid.isEmpty()) {
			player = plugin.getStandardPlayerByUUID(uuid);
		}

		if (type.equals("message")) {
			String message = (String) payload.get("message");
			return handleMessage(player, username, message);
		} else if (type.equals("enter")) {
			return handleStatus(type, player, username);
		} else if (type.equals("exit")) {
			return handleStatus(type, player, username);
		}

		return notHandledResult();
	}

	private JSONObject handleMessage(StandardPlayer sender, String username, String message) {
		String name;

		if (sender == null) {
			name = username;
		} else {
			name = sender.getDisplayName();

			if (sender.isBanned()) {
				plugin.getLogger().warning(sender.getName() + " has been blocked from web chat because they are banned.");
				return buildResult("banned");
			}

			if (sender.isMuted()) {
				plugin.getLogger().warning(sender.getName() + " has been blocked from web chat because they are muted.");
				return buildResult("muted");
			}

			if (!sender.hasPlayedBefore()) {
				plugin.getLogger().warning(username + " (" + sender.getUuidString() + ") " +
						"has been blocked from web chat because they never joined.");
				return buildResult("never_joined");
			}

			if (plugin.shouldBlockMessage(message)) {
				plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[Blocked] " + ChatColor.BLUE + "[Web Chat] " + ChatColor.AQUA +
						sender.getDisplayName() + ChatColor.RESET + ": " + message);

				return okResult(new HashMap<String, Object>() {{
					put("message", "You used a blocked word");
				}});
			}
		}

		String fullMessage = ChatColor.BLUE + "[Web Chat] " + ChatColor.AQUA + name +
				ChatColor.RESET + ": " + ChatColor.GRAY + message;

		String newMessage = fullMessage;
		String newName = name;

		for (SubPlugin subPlugin : plugin.getSubPlugins()) {
			newName = subPlugin.formatWebChatName(sender, null, newName);
		}
		if (!name.equals(newName)) {
			newMessage = newMessage.replaceAll(name, newName);
		}
		StandardPlugin.consoleWebchatMessage(newMessage);

		for (StandardPlayer player : plugin.getOnlinePlayers()) {
			if (!EssentialsIntegration.doesPlayerIgnorePlayer(player, sender)) {
				newMessage = fullMessage;
				newName = name;

				for (SubPlugin subPlugin : plugin.getSubPlugins()) {
					newName = subPlugin.formatWebChatName(sender, player, newName);
				}

				if (!name.equals(newName)) {
					newMessage = newMessage.replaceAll(name, newName);
				}

				player.sendMessage(newMessage);
			}
		}

		return okResult();
	}

	private JSONObject handleStatus(String type, StandardPlayer player, String username) {
		String name;

		if (player == null) {
			name = username;
		} else {
			name = player.getDisplayName(false);
		}

		String message;

		if (type.equals("enter")) {
			message = ChatColor.BLUE + "[Web Chat] " + ChatColor.YELLOW + name + " has entered web chat";
		} else if (type.equals("exit")) {
			message = ChatColor.BLUE + "[Web Chat] " + ChatColor.YELLOW + name + " has left web chat";
		} else {
			return notHandledResult();
		}

		if (player != null && (player.isBanned() || player.isMuted() || !player.hasPlayedBefore())) {
			plugin.getLogger().warning(ChatColor.stripColor(message));
		} else {
			StandardPlugin.broadcast(message);
		}

		return okResult();
	}
}
