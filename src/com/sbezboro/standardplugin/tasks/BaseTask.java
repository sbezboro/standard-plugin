package com.sbezboro.standardplugin.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import com.sbezboro.standardplugin.StandardPlugin;

public abstract class BaseTask extends BukkitRunnable {
	protected StandardPlugin plugin;
	
	public BaseTask(StandardPlugin plugin) {
		this.plugin = plugin;
	}
}
