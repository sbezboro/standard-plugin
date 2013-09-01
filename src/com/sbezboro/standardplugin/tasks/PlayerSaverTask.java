package com.sbezboro.standardplugin.tasks;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class PlayerSaverTask extends BaseTask {

	public PlayerSaverTask(StandardPlugin plugin) {
		super(plugin);
	}

	@Override
	public void run() {
		for (StandardPlayer player : StandardPlugin.getPlugin().getOnlinePlayers()) {
			player.saveData();
		}
	}

}
