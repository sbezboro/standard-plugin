package com.sbezboro.standardplugin.util;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class PlayerSaver implements Runnable {

	@Override
	public void run() {
		for (StandardPlayer player : StandardPlugin.getPlugin().getOnlinePlayers()) {
			player.saveData();
		}
	}

}
