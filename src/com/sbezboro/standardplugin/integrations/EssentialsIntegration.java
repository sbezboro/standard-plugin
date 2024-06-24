package com.sbezboro.standardplugin.integrations;

import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.User;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EssentialsIntegration extends PluginIntegration {
	private static final String CLASS_NAME = "com.earth2me.essentials.IEssentials";
	private static final String PLUGIN_NAME = "Essentials";
	private static IEssentials essentials;

	public static void init(StandardPlugin plugin) {
		essentials = init(plugin, CLASS_NAME, PLUGIN_NAME);
	}

	public static User getUser(StandardPlayer player) {
		if (!enabled) {
			return null;
		}
		return essentials.getUser(player.getUniqueId());
	}

	public static boolean hasNickname(StandardPlayer player) {
		if (!enabled) {
			// StandardPlugin.getPlugin().getLogger().info("EssentialsIntegration not enabled");
			return false;
		}
		return getNickname(player) != null;
	}

	public static String getNickname(StandardPlayer player) {
		User user = getUser(player);
		if (user != null) {
			String nickname = user.getNickname();
			// StandardPlugin.getPlugin().getLogger().info("EssentialsIntegration::getNickname, returning [" + nickname + "]");
			return nickname;
		}
		else {
			// StandardPlugin.getPlugin().getLogger().info("EssentialsIntegration::getNickname, user is null");
		}

		return null;
	}

	public static double getTPS() {
		if (!enabled) {
			return 0f;
		}

		Double tps = essentials.getTimer().getAverageTPS();
		if (tps == null) {
			return 0f;
		}

		return tps;
	}
	
	public static boolean isPlayerMuted(StandardPlayer player) {
		if (!enabled) {
			return false;
		}
		
		return getUser(player).isMuted();
	}

	public static void setPlayerMuted(StandardPlayer player, boolean muted) {
		if (!enabled) {
			return;
		}
		getUser(player).setMuted(muted);
	}

	public static boolean doesPlayerIgnorePlayer(StandardPlayer first, StandardPlayer second) {
		if (!enabled) {
			return false;
		}

		if (first == null || second == null) {
			return false;
		}

		User firstUser = getUser(first);
		User secondUser = getUser(second);

		return firstUser.isIgnoredPlayer(secondUser);
	}
}
