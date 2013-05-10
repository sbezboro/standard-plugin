package com.sbezboro.standardplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.Gate;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class PlayerMoveListener extends EventListener implements Listener {

	public PlayerMoveListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());
		Location from = event.getFrom();
		Location to = event.getTo();
		
		if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
			Gate source = plugin.getGateStorage().getGate(to);
			if (source != null) {
				Gate target = source.getTarget();
				if (target != null) {
					event.setTo(target.getLocation());
					
					Location blockLocation = new Location(to.getWorld(), to.getBlockX(), to.getBlockY() + 1, to.getBlockZ());
					
					for (Entity entity : player.getNearbyEntities(20, 10, 20)) {
						if (entity instanceof Player) {
							Player other = (Player) entity;
							if (other.canSee(player)) {
								other.playEffect(blockLocation, Effect.ENDER_SIGNAL, 0);
								other.playEffect(blockLocation, Effect.EXTINGUISH, 0);
							}
						}
					}

					if (target.getDisplayName() != null) {
						player.sendMessage("You are now at " + ChatColor.AQUA + target.getDisplayName());
					}
					
					plugin.getLogger().info(player.getName() + " went from " + source.getName() + " to " + target.getName());
				}
			}
		}
	}
}
