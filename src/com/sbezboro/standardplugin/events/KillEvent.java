package com.sbezboro.standardplugin.events;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sbezboro.http.HttpRequestManager;
import com.sbezboro.standardplugin.net.KillHttpRequest;
import com.sbezboro.standardplugin.util.MiscUtil;

public class KillEvent {
	private Entity killer;
	private LivingEntity victim;

	public KillEvent(Entity killer, LivingEntity victim) {
		this.killer = killer;
		this.victim = victim;
	}

	public void log() {
		if (killer instanceof Arrow) {
			Arrow arrow = (Arrow) killer;

			if (arrow.getShooter() instanceof Player) {
				Player player = (Player) arrow.getShooter();

				HttpRequestManager.getInstance().startRequest(
						new KillHttpRequest(player.getName(), MiscUtil.getNameFromLivingEntity(victim).toLowerCase()));
			}
		} else if (killer instanceof Player) {
			Player player = (Player) killer;

			HttpRequestManager.getInstance().startRequest(
					new KillHttpRequest(player.getName(), MiscUtil.getNameFromLivingEntity(victim).toLowerCase()));
		}
	}
}
