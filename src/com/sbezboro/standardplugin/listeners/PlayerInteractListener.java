package com.sbezboro.standardplugin.listeners;

import net.minecraft.server.v1_7_R1.EntityEnderSignal;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftEnderSignal;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

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
		// Eye of Ender handling
		} else if (itemStack != null && itemStack.getType() == Material.EYE_OF_ENDER) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK && clickedBlock.getType() != Material.ENDER_PORTAL_FRAME) {
				handleThrownEnderEye(event, event.getPlayer(), itemStack);
			} else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
				handleThrownEnderEye(event, event.getPlayer(), itemStack);
			}
		}
	}
	
	private void handleThrownEnderEye(PlayerInteractEvent event, Player player, ItemStack itemStack) {
		Location playerLocation = player.getLocation();
		Location eyeLocation = new Location(player.getWorld(), playerLocation.getX(), playerLocation.getY() + player.getEyeHeight() - 0.1D, playerLocation.getZ());
		Location portalLocation = plugin.getEndResetStorage().getClosestPortal(eyeLocation);
		
		if (portalLocation == null) {
			return;
		}

		event.setUseItemInHand(Result.DENY);
		event.setCancelled(true);
		
		// Gross hacks to internal server code to spawn a new eye of ender, send it to a new location, and play a sound
		CraftWorld craftWorld = (CraftWorld) eyeLocation.getWorld();
		craftWorld.getHandle().makeSound(((CraftPlayer) player).getHandle(), "random.bow", 0.5F, 0.4F / 1.0F);
		EntityEnderSignal eye = ((CraftEnderSignal) craftWorld.spawn(eyeLocation, EnderSignal.class)).getHandle();
		eye.a(portalLocation.getX(), portalLocation.getBlockY(), portalLocation.getZ());
		
		if (player.getGameMode() == GameMode.SURVIVAL) {
			if (itemStack.getAmount() == 1) {
				player.setItemInHand(null);
			} else {
				itemStack.setAmount(itemStack.getAmount() - 1);
				player.setItemInHand(itemStack);
			}
		}
	}
}
