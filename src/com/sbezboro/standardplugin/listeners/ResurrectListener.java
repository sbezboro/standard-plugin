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

			if (attacker != null) {
				StandardPlugin.broadcast(String.format("%sA totem of " + player.getDisplayName(false) + " was popped by " + attacker.getDisplayName(false), ChatColor.RED), true, true);
			}
		}
	}
}
