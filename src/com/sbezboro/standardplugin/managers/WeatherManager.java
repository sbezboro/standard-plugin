package com.sbezboro.standardplugin.managers;

import com.sbezboro.standardplugin.StandardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WeatherManager extends BaseManager {
	private World overworld;

	public WeatherManager(final StandardPlugin plugin, final World overworld) {
		super(plugin);

		this.overworld = overworld;

		overworld.setThunderDuration(0);
		overworld.setWeatherDuration(0);
		overworld.setStorm(false);
		overworld.setThundering(false);

		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				weatherCheck();
			}
		}, 100, 72000);
	}

	private void weatherCheck() {
		// 25% chance of a storm every hour
		if (Math.random() < 0.25) {
			overworld.setStorm(true);

			// The storm lasts randomly between 5 and 20 minutes
			int minutes = (int) (15 * Math.random()) + 5;
			int duration = minutes * 60 * 20;

			plugin.getLogger().info("Starting storm for " + minutes + " minutes.");

			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					plugin.getLogger().info("Turning off storm.");

					overworld.setStorm(false);
					overworld.setThundering(false);
				}
			}, duration);

			// 25% chance of thunder in this storm
			if (Math.random() < 0.25) {
				// The thunder starts at some random point in the storm
				minutes = (int) (minutes * Math.random());
				duration = minutes * 60 * 20;

				plugin.getLogger().info("Thunder will start in " + minutes + " minutes.");

				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						plugin.getLogger().info("Starting thunder.");

						overworld.setThundering(true);
					}
				}, duration);
			}
		}
	}
}
