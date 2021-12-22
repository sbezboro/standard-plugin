package com.sbezboro.standardplugin.listeners;

import com.sbezboro.http.HttpRequestManager;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.net.DeathHttpRequest;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

import com.sbezboro.standardplugin.StandardPlugin;

public class ResurrectListener extends EventListener implements Listener {

	public ResurrectListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityResurrect(EntityResurrectEvent event) {
		LivingEntity entity = event.getEntity();
		StandardPlayer player = plugin.getStandardPlayer(entity);

		if (player.wasInPvp()) {
			StandardPlayer attacker = plugin.getStandardPlayerByUUID(player.getLastAttackerUuid());

			StandardPlugin.broadcast(String.format("%sA totem of " + player.getDisplayName() + " was popped by " + attacker.getDisplayName(), ChatColor.RED), true, true);

			// also log this as a kill, code copied from private DeathEvent::log
			HttpRequestManager.getInstance().startRequest(
					new DeathHttpRequest(player.getUuidString(), "player", attacker.getUuidString()));

		}  // if totem was popped while in PVP
	}

}
