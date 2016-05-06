package com.sbezboro.standardplugin.tasks;

import com.sbezboro.standardplugin.StandardPlugin;

public class EndResetCheckTask extends BaseTask {
	public EndResetCheckTask(StandardPlugin plugin) {
		super(plugin);
	}

	@Override
	public void run() {
		long curTime = System.currentTimeMillis();
		long nextEndReset = plugin.getEndResetStorage().getNextReset();
		
		int totalSeconds = (int) ((nextEndReset - curTime) / 1000);
		
		// No end reset scheduled
		if (totalSeconds < -60) {
			return;
		}
		
		if (totalSeconds <= 0) {
			plugin.getEndResetManager().resetEnd();
		}
	}

}
