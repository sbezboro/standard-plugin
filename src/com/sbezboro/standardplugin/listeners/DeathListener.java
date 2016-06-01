package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.util.MiscUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.events.DeathEvent;
import com.sbezboro.standardplugin.events.KillEvent;

public class DeathListener extends EventListener implements Listener {

	public DeathListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();

		StandardPlayer player = plugin.getStandardPlayer(entity);

		if (player == null) {
			EntityDamageEvent damageEvent = entity.getLastDamageCause();
			if (damageEvent instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent lastDamageByEntityEvent = (EntityDamageByEntityEvent) damageEvent;
				Entity damager = lastDamageByEntityEvent.getDamager();

				StandardPlayer killer = plugin.getStandardPlayer(damager);

				if (killer == null) {
					if (damager instanceof Projectile) {
						Projectile projectile = (Projectile) damager;

						killer = plugin.getStandardPlayer(projectile.getShooter());
					}
				}

				if (killer != null) {
					KillEvent killEvent = new KillEvent(killer, entity);
					killEvent.log();
				}
				
				if (entity.getType() == EntityType.ENDER_DRAGON) {
					// Possible null value intended
					plugin.getEndResetManager().setDragonSlayer(killer, true);
				}
			} else {
				if (entity.getType() == EntityType.ENDER_DRAGON) {
					plugin.getEndResetManager().setDragonSlayer(null, true);
				}
			}

			if (entity.getType() == EntityType.ENDER_DRAGON) {
				if (!plugin.getEndResetManager().isEndResetScheduled()) {
					plugin.getEndResetManager().scheduleNextEndReset(true);
				}
			} else if (entity.getType() == EntityType.ENDERMAN) {
				if (plugin.getNerfEndermenDrops()) {
					// Nerfed xp and drops for endermen in the end
					if (entity.getLocation().getWorld().getEnvironment() == Environment.THE_END) {
						event.getDrops().clear();

						if (event.getDroppedExp() > 0) {
							event.setDroppedExp(1);

							// 5% chance to drop a pearl
							if (Math.random() < 0.05) {
								event.getDrops().add(new ItemStack(Material.ENDER_PEARL, 1));
							}
						}
					}
				}
			} else if (entity.getType() == EntityType.PIG_ZOMBIE) {
				if (plugin.getNerfPigzombieDrops()) {
					// Nerfed xp and drops for zombie pigmen in the overworld
					if (entity.getLocation().getWorld().getEnvironment() == Environment.NORMAL) {
						if (event.getDroppedExp() > 0) {
							event.setDroppedExp(1);
						}

						event.getDrops().clear();
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		String deathMessage = event.getDeathMessage();

		StandardPlayer victim = plugin.getStandardPlayer(event.getEntity());

		deathMessage = ChatColor.DARK_RED + deathMessage.replaceAll(victim.getName(), victim.getDisplayName(false));

		EntityDamageEvent damageEvent = victim.getLastDamageCause();
		LivingEntity entity = MiscUtil.getLivingEntityFromDamageEvent(damageEvent);
		StandardPlayer killerPlayer = plugin.getStandardPlayer(entity);

		if (killerPlayer != null) {
			if (victim.hasSpawnKillTimeout()) {
				victim.enableSpawnKillProtection();

				event.setDeathMessage(null);
				plugin.getLogger().info("Not logging player " + killerPlayer.getDisplayName(false) +
						" killing " + victim.getDisplayName(false));
				return;
			}

			victim.enableSpawnKillProtection();

			if (deathMessage.contains(killerPlayer.getName())) {
				deathMessage = deathMessage.replaceAll(killerPlayer.getName(), killerPlayer.getDisplayName(false));
			}
		}

		DeathEvent deathEvent = new DeathEvent(victim);
		deathEvent.log();

		victim.setNotInPvp();

		event.setDeathMessage(deathMessage);

		StandardPlugin.webchatMessage(deathMessage);

		Location location = victim.getLocation();
		Bukkit.getConsoleSender().sendMessage(
				ChatColor.RESET + " ([" + location.getWorld().getName() + "] " + ((int) location.getX()) + ", " + ((int) location.getY()) + ", "
						+ ((int) location.getZ()) + ")");
	}
}
