package com.sbezboro.standardplugin.tasks;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class PlayerSaverTask implements Runnable {

	@Override
	public void run() {
		for (StandardPlayer player : StandardPlugin.getPlugin().getOnlinePlayers()) {
			player.saveData();
		}
	}

}
