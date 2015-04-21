package com.sbezboro.standardplugin.tasks;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class SpawnKillProtectionTask extends BaseTask {
	private StandardPlayer player;

	public SpawnKillProtectionTask(StandardPlugin plugin, StandardPlayer player) {
		super(plugin);
		
		this.player = player;
	}

	@Override
	public void run() {
		if (player.isOnline()) {
			player.disableSpawnKillProtection();
		}
	}

}
