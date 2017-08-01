package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener extends EventListener implements Listener {

	public PlayerInteractListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		Block clickedBlock = event.getClickedBlock();
		ItemStack itemStack = event.getItem();
		
		// Setting bed locations
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && clickedBlock.getType() == Material.BED_BLOCK) {
			StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());
			Location location = clickedBlock.getLocation();
			player.saveBedLocation(location);
		// Honeypot handling
		} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && clickedBlock.getType() == Material.CHEST) {
			StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());
			plugin.getHoneypotManager().checkChest(clickedBlock.getLocation(), player);
		// End crystal handling (disables manually respawning the dragon)
		} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && itemStack != null && itemStack.getType() == Material.END_CRYSTAL) {
			event.setCancelled(true);
		}
	}
}
