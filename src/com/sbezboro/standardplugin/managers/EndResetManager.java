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
								ChatColor.AQUA, player.getName(), ChatColor.BLUE, ChatColor.GOLD, ChatColor.BLUE));
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
		// This yields a time between Friday evening and Sunday afternoon of USA time
		
		int daysFromNow = decideDaysFromNow();
		double hourOfDay = decideHourOfDay();
		
		long time = System.currentTimeMillis() + daysFromNow * 86400000;
		time = (time / 86400000) * 86400000; // Round down
		time += Math.round(hourOfDay * 3600000);
		
		return time;
	}
	
	private int decideDaysFromNow() {
		// Find a random day of the weekend after the next (Saturday 00:00 GMT or Sunday 00:00 GMT)
		
		DayOfWeek dayOfWeek = ZonedDateTime.now(ZoneId.of("America/New_York")).getDayOfWeek();
		
		if (dayOfWeek.getValue() >= 5) { // Fri~Sun
			return 20 - dayOfWeek.getValue() + (Math.random() < 0.5 ? 1 : 0);
		} else {
			return 13 - dayOfWeek.getValue() + (Math.random() < 0.5 ? 1 : 0);
		}
	}
	
	private double decideHourOfDay() {
		// Using inverse transformation, find a random GMT hour with peak times being more likely
		// Non-normalized density used: Affine (linear) on [0,9] and [9,24], f(0) = 7 = f(24), f(9) = 1
		
		double y = Math.random();
		
		if (y <= 0.375) {
			return 1.5*(7.0 - Math.sqrt(49.0-128.0*y));
		} else {
			return 0.5*(13.0 + Math.sqrt(695.0+1920.0*y));
		}
	}
	
	public World getNewEndWorld() {
		return newEndWorld;
	}
	
	public boolean isEndResetScheduled() {
		return plugin.isEndResetEnabled() && storage.getNextReset() > System.currentTimeMillis();
	}

}
