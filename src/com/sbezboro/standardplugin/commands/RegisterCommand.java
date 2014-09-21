package com.sbezboro.standardplugin.commands;

import com.sbezboro.standardplugin.net.RegisterHttpRequest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.http.HttpRequestManager;
import com.sbezboro.http.HttpResponse;
import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class RegisterCommand extends BaseCommand {

	public RegisterCommand(StandardPlugin plugin) {
		super(plugin, "register");
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		final StandardPlayer player = plugin.getStandardPlayer(sender);

		if (args.length != 1 || args[0].equals("help")) {
			showUsageInfo(player);
			return false;
		}

		final String username = player.getName();
		final String uuid = player.getUuidString();
		final String email = args[0].trim();

		player.sendMessage("Registering...");

		HttpRequestManager.getInstance().startRequest(
				new RegisterHttpRequest(uuid, username, email, new HttpRequestListener() {

			@Override
			public void requestSuccess(HttpResponse response) {
				String message = (String) response.getJsonResponse().get("message");
				
				if (response.isApiSuccess()) {
					player.sendMessage(ChatColor.DARK_GREEN + message);
					player.sendMessage(ChatColor.GREEN + "Email: " + ChatColor.AQUA + email);
					plugin.getLogger().info(username + ": " + message);
				} else {
					player.sendMessage(ChatColor.RED + message);
				}
			}

			@Override
			public void requestFailure(HttpResponse response) {
				player.sendMessage(ChatColor.RED + "There was an error registering your account!");
			}
		}));

		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage(ChatColor.AQUA + "Usage: /" + name + " <email>");
		sender.sendMessage(ChatColor.GREEN + "This command allows you to create an account on the server's website using your email address.");
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return true;
	}
}
