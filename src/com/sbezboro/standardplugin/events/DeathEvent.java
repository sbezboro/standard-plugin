package com.sbezboro.standardplugin.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.sbezboro.http.HttpRequestManager;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.net.DeathHttpRequest;
import com.sbezboro.standardplugin.util.MiscUtil;

public class DeathEvent {
	public static enum DeathType {
		PLAYER, SUICIDE, FALL, FIRE, LAVA, EXPLOSION, CACTUS, VOID, SUFFOCATION, DROWNING, STARVATION, MAGIC, PVP_LOG, OTHER
	};

	private Player player;
	private EntityDamageEvent damageEvent;
	private DamageCause cause;

	public DeathEvent(Player player) {
		this.player = player;

		if (player.getLastDamageCause() != null) {
			damageEvent = player.getLastDamageCause();
			cause = damageEvent.getCause();
		}
	}

	private void log(DeathType type) {
		String typeString = "other";

		switch (type) {
		case SUICIDE:
			typeString = "suicide";
			break;
		case FALL:
			typeString = "fall";
			break;
		case FIRE:
			typeString = "fire";
			break;
		case LAVA:
			typeString = "lava";
			break;
		case EXPLOSION:
			typeString = "explosion";
			break;
		case CACTUS:
			typeString = "cactus";
			break;
		case SUFFOCATION:
			typeString = "suffocation";
			break;
		case DROWNING:
			typeString = "drowning";
			break;
		case STARVATION:
			typeString = "starvation";
			break;
		case MAGIC:
			typeString = "magic";
			break;
		case PVP_LOG:
			typeString = "pvp_log";
			break;
		case OTHER:
			typeString = "other";
			break;
		default:
			typeString = "other";
			break;
		}

		HttpRequestManager.getInstance().startRequest(
				new DeathHttpRequest(player.getName(), typeString, null));
	}

	private void log(LivingEntity killer) {
		if (killer instanceof Player) {
			Player playerKiller = (Player) killer;
			HttpRequestManager.getInstance().startRequest(
					new DeathHttpRequest(player.getName(), "player", playerKiller.getName()));
		} else {
			HttpRequestManager.getInstance().startRequest(
					new DeathHttpRequest(player.getName(), MiscUtil.getNameFromLivingEntity(killer).toLowerCase()));
		}
	}

	public void log() {
		if (cause == null) {
			StandardPlayer standardPlayer = StandardPlugin.getPlugin().getStandardPlayer(player);
			if (standardPlayer.hasPvpLogged()) {
				log(DeathType.PVP_LOG);
			} else {
				log(DeathType.SUICIDE);
			}
		} else if (damageEvent instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent lastDamageByEntityEvent = (EntityDamageByEntityEvent) damageEvent;
			Entity damager = lastDamageByEntityEvent.getDamager();

			if (damager instanceof Arrow) {
				Arrow arrow = (Arrow) damager;

				log(arrow.getShooter());
			} else if (cause.equals(DamageCause.ENTITY_EXPLOSION)) {
				log(DeathType.EXPLOSION);
			} else if (damager instanceof LivingEntity) {
				LivingEntity livingEntity = (LivingEntity) damager;

				log(livingEntity);
			} else {
				log(DeathType.SUICIDE);
			}
		} else if (damageEvent instanceof EntityDamageByBlockEvent) {
			EntityDamageByBlockEvent lastDamageByBlockEvent = (EntityDamageByBlockEvent) damageEvent;
			Block damager = lastDamageByBlockEvent.getDamager();

			if (cause.equals(DamageCause.CONTACT)) {
				if (damager.getType() == Material.CACTUS) {
					log(DeathType.CACTUS);
				} else {
					log(DeathType.OTHER);
				}
			} else if (cause.equals(DamageCause.LAVA)) {
				log(DeathType.LAVA);
			} else if (cause.equals(DamageCause.VOID)) {
				log(DeathType.VOID);
			} else {
				log(DeathType.OTHER);
			}
		} else {
			if (cause.equals(DamageCause.FIRE)) {
				log(DeathType.FIRE);
			} else if (cause.equals(DamageCause.FIRE_TICK)) {
				log(DeathType.FIRE);
			} else if (cause.equals(DamageCause.SUFFOCATION)) {
				log(DeathType.SUFFOCATION);
			} else if (cause.equals(DamageCause.DROWNING)) {
				log(DeathType.DROWNING);
			} else if (cause.equals(DamageCause.STARVATION)) {
				log(DeathType.STARVATION);
			} else if (cause.equals(DamageCause.FALL)) {
				log(DeathType.FALL);
			} else if (cause.equals(DamageCause.MAGIC)) {
				log(DeathType.MAGIC);
			} else {
				log(DeathType.SUICIDE);
			}
		}
	}
}
