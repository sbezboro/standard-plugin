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
						+ MiscUtil.friendlyTimestamp(storage.getNextReset()));
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
					StandardPlugin.broadcast(
							String.format("%s%sThe Ender Dragon has been slain!", ChatColor.BLUE, ChatColor.BOLD)
					);
				}
			}.runTaskLater(plugin, 100);
		}
	}
	
	private long decideNextEndReset() {
		// modified the code originally written by apv1313 (June 2016 (!))
		// (distribution was modelled here: https://standardsurvival.com/forums/topic/12870)
		// to limit range of possible end reset time to Saturday, between 2pm and 5pm ET
		// (decent range for Europe and US)
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
	
	// Used to return a number for Fri/Sat/Sun/Mon (0-3), now just returns 1 (Sat)
	private int decideDayOfWeekend() {
		return 1;  // always Saturday
	}

	// returns "fractional" hour, so maybe should be called decideTimeOfDay
	private double decideHourOfDay(int dayOfWeekend) {
		double value = Math.random();
		// shooting for a random time between 19:00 and 22:00 GMT (2pm and 5pm ET)
		return 19 + 3.0 * value;
	}
	
	public World getNewEndWorld() {
		return newEndWorld;
	}
	
	public boolean isEndResetScheduled() {
		return plugin.isEndResetEnabled() && (
				storage.getNextReset() + 60000 > System.currentTimeMillis() ||
				!storage.isDragonAlive()
		);
	}

}
