package com.sbezboro.standardplugin.events;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sbezboro.standardplugin.StandardPlugin;
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
		if (StandardPlugin.getPlugin().isDebug()) {
			return;
		}
		
		if (killer instanceof Arrow) {
            Arrow arrow = (Arrow) killer;
            
            if (arrow.getShooter() instanceof Player) {
            	Player player = (Player) arrow.getShooter();
            	
            	KillHttpRequest request = new KillHttpRequest(player.getName(), MiscUtil.getNameFromLivingEntity(victim).toLowerCase());
    			request.start();
            }
        } else if (killer instanceof Player) {
        	Player player = (Player) killer;
        	
        	KillHttpRequest request = new KillHttpRequest(player.getName(), MiscUtil.getNameFromLivingEntity(victim).toLowerCase());
			request.start();
        }
	}
}
