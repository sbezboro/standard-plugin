package com.sbezboro.standardplugin.managers;

import java.time.*;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.scheduler.BukkitRunnable;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.model.Title;
import com.sbezboro.standardplugin.persistence.storages.EndResetStorage;
import com.sbezboro.standardplugin.tasks.EndResetCheckTask;
import com.sbezboro.standardplugin.tasks.EndResetTask;
import com.sbezboro.standardplugin.util.MiscUtil;

public class EndResetManager extends BaseManager {
	private EndResetStorage storage;

	private EndResetCheckTask endResetCheckTask;
	
	private World newEndWorld;

	public EndResetManager(StandardPlugin plugin, EndResetStorage storage) {
		super(plugin);
		
		this.storage = storage;
		
		if (plugin.isEndResetEnabled()) {
			plugin.getLogger().info("End resets enabled");
			
			linkNewEndWorld();
			
			if (storage.getNextReset() == 0) {
				scheduleNextEndReset(false);
			}
			
			if (isEndResetScheduled()) {
				endResetCheckTask = new EndResetCheckTask(plugin);
				endResetCheckTask.runTaskTimerAsynchronously(plugin, 1200, 1200);
				
				plugin.getLogger().info("End reset scheduled to be on "
						+ MiscUtil.friendlyTimestamp(storage.getNextReset(), "America/Los_Angeles"));
			} else {
				plugin.getLogger().info("No end resets scheduled since the Ender Dragon is still alive");
			}
		} else {
			plugin.getLogger().info("End resets disabled");
		}
	}
	
	public void linkNewEndWorld() {
		WorldCreator creator = new WorldCreator(StandardPlugin.NEW_END_WORLD_NAME);
		creator.environment(Environment.THE_END);
		newEndWorld = plugin.getServer().createWorld(creator);
		newEndWorld.setKeepSpawnInMemory(false);
	}

	public void resetEnd() {
		if (plugin.isEndResetEnabled()) {
			World overworld = plugin.getServer().getWorld(StandardPlugin.OVERWORLD_NAME);
			
			new EndResetTask(plugin, overworld).runTask(plugin);
		} else {
			plugin.getLogger().severe("resetEnd() called when end resets aren't enabled!");
		}
	}
	
	public void setDragonSlayer(final StandardPlayer player, boolean broadcast) {
		StandardPlayer oldDragonSlayer = storage.getDragonSlayer();
		if (oldDragonSlayer != null) {
			oldDragonSlayer.removeTitle(Title.DRAGON_SLAYER);
		}
		if (player != null) {
			player.addTitle(Title.DRAGON_SLAYER);
			if (broadcast) {
				new BukkitRunnable() {
					@Override
					public void run() {
						StandardPlugin.broadcast(String.format("%s%s%s has received the title %sDragon Slayer%s!", 
								ChatColor.AQUA, player.getDisplayName(true), ChatColor.BLUE, ChatColor.GOLD, ChatColor.BLUE));
					}
				}.runTaskLater(plugin, 200);
			}
		} else if (broadcast) {
			new BukkitRunnable() {
				@Override
				public void run() {
					StandardPlugin.broadcast(String.format("%sThe title %sDragon Slayer%s was not awarded to anybody this time.", 
							ChatColor.BLUE, ChatColor.GOLD, ChatColor.BLUE));
				}
			}.runTaskLater(plugin, 200);
		}
		
		storage.setDragonSlayer(player);
	}
	
	public void scheduleNextEndReset(boolean broadcast) {
		if (!plugin.isEndResetEnabled()) {
			return;
		}
		
		final long nextReset = decideNextEndReset();
		
		storage.setNextReset(nextReset);
		storage.setDragonAlive(false);
		
		if (endResetCheckTask == null) {
			endResetCheckTask = new EndResetCheckTask(plugin);
			endResetCheckTask.runTaskTimerAsynchronously(plugin, 1200, 1200);
		}
		
		if (broadcast) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					StandardPlugin.broadcast(String.format("%s%sThe Ender Dragon has been slain! The next end reset will be on " +
							"the weekend after the next.", ChatColor.BLUE, ChatColor.BOLD));
				}
			}.runTaskLater(plugin, 100);
		}
	}
	
	private long decideNextEndReset() {
		// This yields a time between Friday morning and Sunday night of USA time
		// Fri day: 5 %, Fri night: 25 %, Sat day: 20 %, Sat night: 25 %, Sun day: 15 %, Sun night: 10 %
		
		int dayOfWeekend = decideDayOfWeekend();
		double hourOfDay = decideHourOfDay(dayOfWeekend);
		
		DayOfWeek dayOfWeek = ZonedDateTime.now(ZoneId.of("America/New_York")).getDayOfWeek();
		int daysFromNow;
		
		if (dayOfWeek.getValue() >= 5) { // Fri~Sun
			daysFromNow = 19 - dayOfWeek.getValue() + dayOfWeekend;
		} else {
			daysFromNow = 12 - dayOfWeek.getValue() + dayOfWeekend;
		}
		
		long time = System.currentTimeMillis() + daysFromNow * 86400000;
		time = (time / 86400000) * 86400000; // Round down to 00:00 GMT
		time += Math.round(hourOfDay * 3600000);
		
		return time;
	}
	
	private int decideDayOfWeekend() {
		// Find a random day of the weekend after the next (Fri~Mon GMT)
		
		double r = Math.random();
		int d = 0;
		if (r > 0.9) {
			d = 3;
		} else if (r > 0.5) {
			d = 2;
		} else if (r > 0.05) {
			d = 1;
		}
		
		return d;
	}
	
	private double decideHourOfDay(int dayOfWeekend) {
		// Using inverse transformation, find a random GMT hour with peak times being more likely
		
		double y = Math.random();
		
		switch (dayOfWeekend) {
		case 0: // Fri
			return 15.0 + Math.sqrt(81.0*y);
			
		case 1: // Sat
			if (y < 5.0/9.0) {
				return 9.0 - 1.8 * Math.sqrt(-45.0*y+25.0);
			} else {
				return 9.0 + 7.5 * Math.sqrt(9.0*y-5.0);
			}
			
		case 2: // Sun
			if (y < 5.0/8.0) {
				return 9.0 - 1.8 * Math.sqrt(-40.0*y+25.0);
			} else {
				return 9.0 + 5.0 * Math.sqrt(12.0*y-7.5);
			}
			
		case 3: // Mon
			return 9.0 - 9.0 * Math.sqrt(-y+1.0);

		default: // Should never happen
			return 0.0;
		}
	}
	
	public World getNewEndWorld() {
		return newEndWorld;
	}
	
	public boolean isEndResetScheduled() {
		return plugin.isEndResetEnabled() && (storage.getNextReset() + 60000 > System.currentTimeMillis() || !storage.isDragonAlive());
	}

}
