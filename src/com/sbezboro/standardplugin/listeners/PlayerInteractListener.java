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
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener extends EventListener implements Listener {
	public PlayerInteractListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());
		Block clickedBlock = event.getClickedBlock();
		ItemStack itemStack = event.getItem();

		// Block ender pearl glitching
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

			if (itemStack != null && itemStack.getType() == Material.ENDER_PEARL &&
					clickedBlock.getType().isSolid() && !(clickedBlock.getState() instanceof InventoryHolder)) {
				event.setCancelled(true);
				// Setting bed locations
			}
			else if (StandardPlugin.BED_BLOCKS.contains(clickedBlock.getType())) {
				Location location = clickedBlock.getLocation();
				player.saveBedLocation(location);
				// Honeypot handling
			}
			else if (clickedBlock.getType() == Material.CHEST) {
				plugin.getHoneypotManager().checkChest(clickedBlock.getLocation(), player);
				// End crystal handling (disables manually respawning the dragon)
			}
			else if (itemStack != null && itemStack.getType() == Material.END_CRYSTAL) {
				event.setCancelled(true);
			}

		}  // if right click block
	}
}
