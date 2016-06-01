package com.sbezboro.standardplugin.managers;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.scheduler.BukkitRunnable;

import com.sbezboro.standardplugin.StandardPlugin;
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
				endResetCheckTask.runTaskTimerAsynchronously(plugin, 20, 20);
				
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
	
	public void scheduleNextEndReset(boolean broadcast) {
		if (!plugin.isEndResetEnabled()) {
			return;
		}
		
		final long nextReset = decideNextEndReset();
		
		storage.setNextReset(nextReset);
		
		if (endResetCheckTask == null) {
			endResetCheckTask = new EndResetCheckTask(plugin);
			endResetCheckTask.runTaskTimerAsynchronously(plugin, 20, 20);
		}
		
		if (broadcast) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					StandardPlugin.broadcast(String.format("%s%sThe Ender Dragon has been slain! Next end reset is scheduled to be on %s%s", 
							ChatColor.BLUE, ChatColor.BOLD, ChatColor.AQUA, MiscUtil.friendlyTimestamp(nextReset)));
				}
			}.runTaskLater(plugin, 100);
		}
	}
	
	private long decideNextEndReset() {
		// Get x days from now
		long time = System.currentTimeMillis() + plugin.getEndResetPeriod() * 86400000;
		// Round to get the start of the next day in GMT, 5PM Pacific, 8PM Eastern,
		// when there are the most players on more-or-less
		return (time / 86400000) * 86400000;
	}
	
	public World getNewEndWorld() {
		return newEndWorld;
	}
	
	public boolean isEndResetScheduled() {
		return plugin.isEndResetEnabled() && storage.getNextReset() > System.currentTimeMillis();
	}

}
