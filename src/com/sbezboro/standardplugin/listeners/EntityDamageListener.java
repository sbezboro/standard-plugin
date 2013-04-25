package com.sbezboro.standardplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.sbezboro.standardplugin.StandardPlugin;

public class EntityDamageListener extends EventListener implements Listener {

	public EntityDamageListener(StandardPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		Entity entity = event.getEntity();
		
		if (entity instanceof Player) {
			final Player victim = (Player) entity;
			
			final int initialHealth = victim.getHealth();
			final int initialDamage = event.getDamage();
			
			//Bukkit.broadcastMessage("Damage: " + event.getDamage());
			//Bukkit.broadcastMessage("Last Damage: " + victim.getLastDamage());
			//Bukkit.broadcastMessage("No Damage Ticks: " + victim.getNoDamageTicks());
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					Bukkit.broadcastMessage("Damage dealt: " + (initialHealth - victim.getHealth()));
					Bukkit.broadcastMessage("Armor blocked: " + (initialDamage - (initialHealth - victim.getHealth())));
				}
			}, 1);
		}
	}

}
