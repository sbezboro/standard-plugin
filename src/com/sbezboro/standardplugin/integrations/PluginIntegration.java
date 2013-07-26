package com.sbezboro.standardplugin.integrations;

import org.bukkit.plugin.Plugin;

import com.sbezboro.standardplugin.StandardPlugin;

public abstract class PluginIntegration {
	public static boolean enabled;

	@SuppressWarnings("unchecked")
	protected static <T> T init(StandardPlugin standardPlugin, String className, String pluginName) {
		Plugin plugin = standardPlugin.getServer().getPluginManager().getPlugin(pluginName);
		Class<T> cls = null;

		try {
			cls = (Class<T>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			plugin = null;
		}

		if (plugin == null || !cls.isInstance(plugin)) {
			standardPlugin.getLogger().warning("Could not hook into " + pluginName + "!");
			enabled = false;
		} else {
			standardPlugin.getLogger().info("Successfully hooked into " + pluginName);
			enabled = true;
		}

		return (T) plugin;
	}
}
