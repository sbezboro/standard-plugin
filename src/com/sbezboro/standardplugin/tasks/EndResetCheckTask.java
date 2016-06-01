package com.sbezboro.standardplugin.tasks;

import org.bukkit.ChatColor;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.util.MiscUtil;

public class EndResetCheckTask extends BaseTask {
	public EndResetCheckTask(StandardPlugin plugin) {
		super(plugin);
	}
	
	private void broadcastTime(String time) {
		StandardPlugin.broadcast(String.format("%s%sThe end will reset in %s%s%s%s%s!", ChatColor.BLUE, 
				ChatColor.BOLD, ChatColor.AQUA, ChatColor.BOLD, time, ChatColor.BLUE, ChatColor.BOLD));
	}

	@Override
	public void run() {
		long curTime = System.currentTimeMillis();
		long nextEndReset = plugin.getEndResetStorage().getNextReset();
		
		int totalSeconds = (int) ((nextEndReset - curTime) / 1000);
		
		// No end reset scheduled
		if (totalSeconds < -1) {
			return;
		}
		
		int totalMinutes = totalSeconds / 60;
		int totalHours = totalMinutes / 60;

		int seconds;
		if (totalMinutes > 0) {
			seconds = totalSeconds % (totalMinutes * 60);
		} else {
			seconds = totalSeconds;
		}
		
		int minutes;
		if (totalHours > 0) {
			minutes = totalMinutes % (totalHours * 60);
		} else {
			minutes = totalMinutes;
		}
		
		// A minute has passed
		if (seconds == 0) {
			if (totalSeconds <= 0) {
				plugin.getEndResetManager().resetEnd();
			// Less than an hour remains, announce every 10 minutes, and every minute under 10 remaining, how much time is left
			} else if (totalHours == 0 && (minutes % 10 == 0 || minutes < 10)) {
				broadcastTime(minutes + " " + MiscUtil.pluralize("minute", minutes));
			// If less than 24 hours remain, announce every hour on the hour how much time is left
			} else if (totalHours <= 24 && minutes == 0) {
				broadcastTime(totalHours + " " + MiscUtil.pluralize("hour", totalHours));
			// More than 24 hours remain, announce the date every hour
			} else if (totalHours > 24 && minutes == 0) {
				StandardPlugin.broadcast(String.format("%s%sThe end will reset on %s%s%s%s", ChatColor.BLUE, 
						ChatColor.BOLD, ChatColor.AQUA, MiscUtil.friendlyTimestamp(nextEndReset), ChatColor.BLUE, ChatColor.BOLD));
			}
		} else if (totalMinutes == 0 && seconds == 10) {
			broadcastTime(seconds + " " + MiscUtil.pluralize("second", seconds));
		}
	}

}
