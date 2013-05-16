package com.sbezboro.standardplugin.integrations;

import org.bukkit.plugin.Plugin;

import com.sbezboro.standardplugin.StandardPlugin;

public abstract class PluginIntegration {
	public static boolean enabled;
	
	@SuppressWarnings("unchecked")
	protected static <T> T init(StandardPlugin standardPlugin, String className, String pluginName) {
		Plugin plugin = standardPlugin.getServer().getPluginManager().getPlugin(pluginName);
		Class<T> cls;
		
		try {
			cls = (Class<T>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			pluginHookFailure(standardPlugin, pluginName);
			return null;
		}
		
		if (plugin == null || !cls.isInstance(plugin)) {
			pluginHookFailure(standardPlugin, pluginName);
		} else {
			pluginHookSuccess(standardPlugin, pluginName);
		}
		
		return (T) plugin;
	}
	
	private static void pluginHookSuccess(StandardPlugin standardPlugin, String name) {
		standardPlugin.getLogger().info("Successfully hooked into " + name);
		enabled = true;
	}
	
	private static void pluginHookFailure(StandardPlugin standardPlugin, String name) {
		standardPlugin.getLogger().warning("Could not hook into " + name + "!");
		enabled = false;
	}
}
