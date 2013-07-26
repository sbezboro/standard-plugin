package com.sbezboro.standardplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.http.HttpRequestManager;
import com.sbezboro.http.HttpResponse;
import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.net.LinkHttpRequest;

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
		final String password = args[0];

		player.sendMessage("Registering...");

		HttpRequestManager.getInstance().startRequest(
				new LinkHttpRequest(username, password, new HttpRequestListener() {

			@Override
			public void requestSuccess(HttpResponse response) {
				String result = response.getStringResponse();

				player.sendMessage(result);
				player.sendMessage(ChatColor.GREEN + "Website username: " + ChatColor.AQUA + username + ChatColor.GREEN + ", password: " + ChatColor.AQUA
						+ password);
				player.sendMessage(ChatColor.GREEN + "Visit standardsurvival.com/login");
				plugin.getLogger().info(username + ": " + result);
			}

			@Override
			public void requestFailure(HttpResponse response) {
				player.sendMessage("There was an error registering your account!");
			}
		}));

		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + name + " <password>");
		sender.sendMessage(ChatColor.GREEN + "This command will create an account on the website using");
		sender.sendMessage(ChatColor.GREEN + "your Minecraft username and a password you specify. ");
		sender.sendMessage(ChatColor.RED + "WARNING! DO NOT use your Minecraft password!");
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return true;
	}
}
