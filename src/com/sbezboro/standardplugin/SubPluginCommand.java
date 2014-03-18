package com.sbezboro.standardplugin;

import org.bukkit.plugin.java.JavaPlugin;

import com.sbezboro.standardplugin.commands.BaseCommand;

public abstract class SubPluginCommand<T extends JavaPlugin> extends BaseCommand {
	protected T subPlugin;

	public SubPluginCommand(StandardPlugin plugin, T subPlugin, String name) {
		super(plugin, name);
		this.subPlugin = subPlugin;
	}

	@Override
	public void register() {
		subPlugin.getCommand(name).setExecutor(this);
	}
	
}
