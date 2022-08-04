package com.sbezboro.standardplugin.integrations;

import com.sbezboro.standardplugin.StandardPlugin;
import oculus.tablist.bukkit.BukkitMain;
import org.bukkit.entity.Player;

public class PlainTablistIntegration extends PluginIntegration {
	private static final String CLASS_NAME = "oculus.tablist.bukkit.BukkitMain";
	private static final String PLUGIN_NAME = "PlainTablist";

	public static void init(StandardPlugin plugin) {
		init(plugin, CLASS_NAME, PLUGIN_NAME);
	}


	public static void set_extra_message(String message) {
		BukkitMain.set_extra_message(message);
	}
}
