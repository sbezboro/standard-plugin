package com.sbezboro.standardplugin.listeners;

import com.sbezboro.http.HttpRequestManager;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.net.DeathHttpRequest;
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
					Location location = entity.getLocation();
					if (location.getWorld().getEnvironment() == Environment.THE_END) {
						if (Math.max(Math.abs(location.getBlockX()), Math.abs(location.getBlockZ())) > 200) {
							event.getDrops().clear();
							
							// 5% chance to drop a pearl
							if (Math.random() < 0.05) {
								event.getDrops().add(new ItemStack(Material.ENDER_PEARL, 1));
							}
						}
						
						if (event.getDroppedExp() > 0) {
							event.setDroppedExp(1);
						}
					}
				}
			} else if (entity.getType() == EntityType.ZOMBIFIED_PIGLIN) {
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

		if (System.currentTimeMillis() - victim.getLastDeathTime() < plugin.getDeathMessageTimeout() * 1000) {
			victim.sendMessage(deathMessage);
			deathMessage = null;
			plugin.getLogger().info("Hiding death message for " + victim.getDisplayName(false));
		}

		victim.setLastDeathTime();

		// some more info about death while in pvp + logging the kill (below)
		StandardPlayer indirect_killer = null;

		// Essentials' /suicide and PVP log deaths seem to be only detectable this way
		if (deathMessage != null && deathMessage.equals(ChatColor.DARK_RED + victim.getDisplayName(false) + " died")) {
			victim.setLastDeathInPvp(victim.wasInPvp() || victim.hasPvpLogged());
			victim.setLastDeathBySpawnkill(false);
		} else if (killerPlayer != null) {
			if (victim.hasSpawnKillTimeout()) {
				victim.enableSpawnKillProtection();

				victim.setLastDeathInPvp(true);
				victim.setLastDeathBySpawnkill(true);

				event.setDeathMessage(null);
				plugin.getLogger().info("Not logging player " + killerPlayer.getDisplayName(false) +
						" killing " + victim.getDisplayName(false));
				return;
			}

			victim.enableSpawnKillProtection();

			if (deathMessage != null && deathMessage.contains(killerPlayer.getName())) {
				deathMessage = deathMessage.replaceAll(killerPlayer.getName(), killerPlayer.getDisplayName(false));
			}
			
			victim.setLastDeathInPvp(true);
			victim.setLastDeathBySpawnkill(false);
		} else {
			if (victim.wasInPvp()) {
				indirect_killer = plugin.getStandardPlayerByUUID(victim.getLastAttackerUuid());
				deathMessage += ", while in PVP with " + indirect_killer.getDisplayName();
			}
			victim.setLastDeathInPvp(victim.wasInPvp());
			victim.setLastDeathBySpawnkill(false);
		}
		DeathEvent deathEvent = new DeathEvent(victim);
		deathEvent.log();

		if (indirect_killer != null) {
			// code copied from private DeathEvent::log
			HttpRequestManager.getInstance().startRequest(
					new DeathHttpRequest(victim.getUuidString(), "player", indirect_killer.getUuidString()));
		}

		victim.setNotInPvp();

		event.setDeathMessage(deathMessage);

		if (deathMessage != null) {
			StandardPlugin.webchatMessage(deathMessage);
		}

		Location location = victim.getLocation();
		Bukkit.getConsoleSender().sendMessage(
				ChatColor.RESET + " ([" + location.getWorld().getName() + "] " + ((int) location.getX()) + ", " + ((int) location.getY()) + ", "
						+ ((int) location.getZ()) + ")");
	}
}
