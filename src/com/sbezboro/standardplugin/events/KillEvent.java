package com.sbezboro.standardplugin.events;

import com.sbezboro.http.HttpRequestManager;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.net.KillHttpRequest;
import com.sbezboro.standardplugin.util.MiscUtil;
import org.bukkit.entity.LivingEntity;

public class KillEvent {
	private StandardPlayer killer;
	private LivingEntity victim;

	public KillEvent(StandardPlayer killer, LivingEntity victim) {
		this.killer = killer;
		this.victim = victim;
	}

	public void log() {
		HttpRequestManager.getInstance().startRequest(
				new KillHttpRequest(killer.getUuidString(), MiscUtil.getNameFromLivingEntity(victim).toLowerCase()));
	}
}
