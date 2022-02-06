package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.util.MiscUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class PlayerAdvancementListener extends EventListener implements Listener {
	public PlayerAdvancementListener(StandardPlugin plugin) {
		super(plugin);
	}

    @EventHandler
	public void onAdvancementDone(PlayerAdvancementDoneEvent event){
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		// example keys:
		// recipes/brewing/cauldron (are ignored)
		// husbandry/tactical_fishing

		String advancement_key = event.getAdvancement().getKey().getKey();
		if (!advancement_key.startsWith("recipes")) {
			String advancement_text = advancement_key.substring(advancement_key.indexOf('/') + 1).replaceAll("_", " ");
			StandardPlugin.webchatMessage(String.format("%s%s%s got the advancement %s[%s]", ChatColor.AQUA, player.getDisplayName(), ChatColor.WHITE, ChatColor.GREEN, advancement_text));
		}
	}
}
