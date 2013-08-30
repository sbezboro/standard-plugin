package com.sbezboro.standardplugin.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.bukkit.ChatColor;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.util.MiscUtil;

public class EndResetCheckTask implements Runnable {
	private StandardPlugin plugin;
	
	public EndResetCheckTask(StandardPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		long curTime = System.currentTimeMillis();
		long nextEndReset = plugin.getEndResetStorage().getNextReset();
		
		int totalSeconds = (int) ((nextEndReset - curTime) / 1000);
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
			if (minutes % 10 == 0) {
				plugin.getLogger().info("End reset in " + totalHours + " hours " + minutes + " minutes");
			}
			
			if (totalSeconds <= 0) {
				plugin.resetEnd();
			// Less than an hour remains, announce every 10 minutes, and every minute under 10 remaining, how much time is left
			} else if (totalHours == 0 && (minutes % 10 == 0 || minutes < 10)) {
				StandardPlugin.broadcast(String.format("%s%sThe end will reset in %d %s!", ChatColor.BLUE, ChatColor.BOLD, minutes, MiscUtil.pluralize("minute", minutes)));
			// If less than 12 hours remain, announce every hour on the hour how much time is left
			} else if (totalHours <= 12 && minutes == 0) {
				StandardPlugin.broadcast(String.format("%s%sThe end will reset in %d %s!", ChatColor.BLUE, ChatColor.BOLD, totalHours, MiscUtil.pluralize("hour", totalHours)));
			// More than 12 hours remain, announce the date every hour
			} else if (totalHours > 12 && minutes == 0) {
				DateFormat format = new SimpleDateFormat("MMMM d h:mm a zz");
				format.setTimeZone(TimeZone.getTimeZone("America/New_York"));
				StandardPlugin.broadcast(String.format("%s%sThe end will reset on " + format.format(nextEndReset), ChatColor.BLUE, ChatColor.BOLD));
			}
		} else if (totalMinutes == 0 && seconds == 10) {
			StandardPlugin.broadcast(String.format("%s%sThe end will reset in %d %s!", ChatColor.BLUE, ChatColor.BOLD, seconds, MiscUtil.pluralize("second", seconds)));
		}
	}

}
