package com.sbezboro.standardplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.sbezboro.http.HttpRequestManager;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.integrations.SimplyVanishIntegration;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.model.Title;
import com.sbezboro.standardplugin.net.LeaveHttpRequest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerLeaveListener extends EventListener implements Listener {

	public static final String HAVE_BEEN = "have been";

	public PlayerLeaveListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		String message = String.format("%s%s has left the server", ChatColor.DARK_GRAY, player.getDisplayName(false));
		event.setQuitMessage(message);

		if (!SimplyVanishIntegration.isVanished(player)) {
			StandardPlugin.webchatMessage(message);
		}
		
		if (player.isInPvp()) {
			player.setPvpLogged(true);
			
			player.incrementPvpLogs();

			StandardPlugin.broadcast(String.format("%s%s %sPVP logged to %s%s%s!",
					ChatColor.AQUA, player.getDisplayName(), ChatColor.RED, ChatColor.AQUA, 
					plugin.getStandardPlayerByUUID(player.getLastAttackerUuid()).getDisplayName(), ChatColor.RED));

			if (player.hasTitle(Title.PVP_LOGGER)) {

				// check if holding a totem, and remove if so
				PlayerInventory inv = player.getInventory();

				boolean hadTotem = false;

				// check twice because of main hand / off hand
				if (inv.getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING) {
					inv.setItemInMainHand(null);
					hadTotem = true;
				}
				if (inv.getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
					inv.setItemInOffHand(null);
					hadTotem = true;
				}

				if (hadTotem) {
					StandardPlugin.broadcast(String.format("%s%s%s lost a totem as well", ChatColor.AQUA, player.getDisplayName(), ChatColor.RED));
				}

				player.damage(1000.0);
			}
		}

		player.onLeaveServer();
		
		HttpRequestManager.getInstance().startRequest(new LeaveHttpRequest(player.getUuidString(), null));
	}

	//onPlayerQuit will be called right after this
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		String reason = event.getReason();
		String action;

		if (reason.contains(HAVE_BEEN) && reason.contains(":")) {
			int actionIndex = reason.indexOf(HAVE_BEEN) + HAVE_BEEN.length() + 1;
			action = ChatColor.stripColor(reason.substring(actionIndex));
		} else {
			action = "kicked: " + reason;
		}

		String message = String.format("%s%s was " + action, ChatColor.DARK_GRAY, player.getDisplayName(false));
		event.setLeaveMessage(message);

		if (!SimplyVanishIntegration.isVanished(player)) {
			StandardPlugin.webchatMessage(message);
		}
	}
}
