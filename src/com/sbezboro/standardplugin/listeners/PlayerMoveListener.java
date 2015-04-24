package com.sbezboro.standardplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
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

	@EventHandler(ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		Location from = event.getFrom();
		Location to = event.getTo();

		if (from == null || to == null || from.getWorld() == null || to.getWorld() == null) {
			return;
		}

		if (event.getPlayer().isDead()) {
			return;
		}

		if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
			final StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());
			if (player.isSpawnKillProtected()) {
				player.disableSpawnKillProtection();
			}

			Gate source = plugin.getGateStorage().getGate(to);

			if (source != null) {
				Gate target = source.getTarget();

				if (target != null) {
					// Play effect at the source portal to other players
					Location effectLocation = new Location(to.getWorld(), to.getBlockX(), to.getBlockY() + 1, to.getBlockZ());
					for (Entity entity : player.getNearbyEntities(20, 10, 20)) {
						if (entity instanceof Player) {
							StandardPlayer other = plugin.getStandardPlayer(entity);
							if (other.canSee(player)) {
								other.playEffect(effectLocation, Effect.ENDER_SIGNAL, null);
								other.playEffect(effectLocation, Effect.EXTINGUISH, null);
							}
						}
					}

					Location destination = target.getLocation();

					boolean withHorse = false;
					if (player.isInsideVehicle()) {
						Entity vehicle = player.getVehicle();

						if (vehicle instanceof Horse) {
							withHorse = true;

							final Horse horse = (Horse) vehicle;
							horse.eject();

							horse.teleport(destination);

							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

								@Override
								public void run() {
									horse.setPassenger(player);
								}
							}, 20);
						} else {
							return;
						}
					}

					event.setTo(destination);

					if (target.getDisplayName() != null) {
						player.sendMessage("You are now at " + ChatColor.AQUA + target.getDisplayName());
					}

					String message = player.getName() + " went from " + source.getIdentifier() + " to " + target.getIdentifier();
					if (withHorse) {
						message += " on a horse";
					}

					plugin.getLogger().info(message);

					Chunk chunk = destination.getChunk();
					if (!chunk.isLoaded()) {
						chunk.load();
					}
				}
			}
		}
	}
}
