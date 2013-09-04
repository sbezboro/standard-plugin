package com.sbezboro.standardplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

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
		
		if (entity instanceof Player) {
			DeathEvent deathEvent = new DeathEvent((Player) event.getEntity());
			deathEvent.log();
		} else {
			EntityDamageEvent damageEvent = entity.getLastDamageCause();
			if (damageEvent instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent lastDamageByEntityEvent = (EntityDamageByEntityEvent) damageEvent;
				Entity damager = lastDamageByEntityEvent.getDamager();

				KillEvent killEvent = new KillEvent(damager, entity);
				killEvent.log();
			}
			
			if (entity.getType() == EntityType.ENDER_DRAGON) {
				if (!plugin.getEndResetManager().isEndResetScheduled()) {
					plugin.getEndResetManager().scheduleNextEndReset(true);
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		String deathMessage = event.getDeathMessage();
		Player victim = event.getEntity();

		deathMessage = ChatColor.DARK_RED + deathMessage.replaceAll(victim.getName(), ChatColor.stripColor(victim.getDisplayName()));

		EntityDamageEvent damageEvent = victim.getLastDamageCause();
		if (damageEvent instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent lastDamageByEntityEvent = (EntityDamageByEntityEvent) damageEvent;
			Entity damager = lastDamageByEntityEvent.getDamager();
			Player killer = null;

			if (damager instanceof Player && damager != victim) {
				killer = (Player) damager;
			} else if (damager instanceof Arrow) {
				Arrow arrow = (Arrow) damager;

				if (arrow.getShooter() instanceof Player && arrow.getShooter() != victim) {
					killer = (Player) arrow.getShooter();
				}
			}

			if (killer != null && deathMessage.contains(killer.getName())) {
				deathMessage = deathMessage.replaceAll(killer.getName(), ChatColor.stripColor(killer.getDisplayName()));
			}
		}
		
		StandardPlugin.webchatMessage(deathMessage);

		event.setDeathMessage(deathMessage);

		Location location = victim.getLocation();
		Bukkit.getConsoleSender().sendMessage(
				deathMessage + ChatColor.RESET + " ([" + location.getWorld().getName() + "] " + ((int) location.getX()) + ", " + ((int) location.getY()) + ", "
						+ ((int) location.getZ()) + ")");
	}
}
