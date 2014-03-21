package com.sbezboro.standardplugin;

import org.bukkit.plugin.java.JavaPlugin;

import com.sbezboro.standardplugin.listeners.EventListener;

public class SubPluginEventListener<T extends JavaPlugin> extends EventListener {
	protected T subPlugin;
	
	public SubPluginEventListener(StandardPlugin plugin, T subPlugin) {
		super(plugin);
		
		this.subPlugin = subPlugin;
	}

}
