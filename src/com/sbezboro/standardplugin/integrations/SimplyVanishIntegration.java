package com.sbezboro.standardplugin.integrations;

import org.bukkit.entity.Player;

import me.asofold.bpl.simplyvanish.SimplyVanish;

import com.sbezboro.standardplugin.StandardPlugin;

public class SimplyVanishIntegration extends PluginIntegration {
	private static final String CLASS_NAME = "me.asofold.bpl.simplyvanish.SimplyVanish";
	private static final String PLUGIN_NAME = "SimplyVanish";
	
	public static void init(StandardPlugin plugin) {
		init(plugin, CLASS_NAME, PLUGIN_NAME);
	}
	
	public static boolean isVanished(Player player) {
		return isVanished(player.getName());
	}
	
	public static boolean isVanished(String username) {
		if (!enabled) {
			return false;
		}
		return SimplyVanish.isVanished(username);
	}
}
