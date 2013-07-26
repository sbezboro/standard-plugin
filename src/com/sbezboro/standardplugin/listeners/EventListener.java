package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;

public abstract class EventListener {
	protected StandardPlugin plugin;

	public EventListener(StandardPlugin plugin) {
		this.plugin = plugin;
	}
}
