package com.sbezboro.standardplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class HungerListener extends EventListener implements Listener {

	public HungerListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (!plugin.isHungerProtectionEnabled()) {
			return;
		}

		StandardPlayer player = plugin.getStandardPlayer(event.getEntity());

		if (player != null && player.isHungerProtected()) {
			event.setCancelled(true);
		}
	}

}
