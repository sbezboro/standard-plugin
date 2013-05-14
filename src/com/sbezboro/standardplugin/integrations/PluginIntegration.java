package com.sbezboro.standardplugin.integrations;

import org.bukkit.plugin.Plugin;

import com.sbezboro.standardplugin.StandardPlugin;

public abstract class PluginIntegration {
	public static boolean enabled;
	
	@SuppressWarnings("unchecked")
	protected static <T> T init(StandardPlugin standardPlugin, Class<T> pluginClass, String pluginName) {
		Plugin plugin = standardPlugin.getServer().getPluginManager().getPlugin(pluginName);
		if (plugin == null || !pluginClass.isInstance(plugin)) {
			pluginHookFailure(standardPlugin, pluginName);
			plugin = null;
		} else {
			standardPlugin.getLogger().info("Successfully hooked into " + pluginName);
			enabled = true;
		}
		
		return (T) plugin;
	}
	
	protected static void pluginHookFailure(StandardPlugin standardPlugin, String name) {
		standardPlugin.getLogger().warning("Could not hook into " + name);
		enabled = false;
	}
}
