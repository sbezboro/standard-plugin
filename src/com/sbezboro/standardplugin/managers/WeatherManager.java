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
		}, 36000, 36000); // Every half hour
	}

	private void weatherCheck() {
		// 50% chance of a storm period
		if (Math.random() < 0.50) {
			// The storm lasts randomly between 5 and 20 minutes
			int minutes = (int) (15 * Math.random()) + 5;
			int duration = minutes * 60 * 20;

			overworld.setWeatherDuration(duration);
			overworld.setStorm(true);

			plugin.getLogger().info("Starting storm for " + minutes + " minutes.");

			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					plugin.getLogger().info("Turning off storm.");

					overworld.setThunderDuration(0);
					overworld.setWeatherDuration(0);
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
						overworld.setThunderDuration(2400);
					}
				}, duration);
			}
		} else {
			plugin.getLogger().info("Not starting storm.");
		}
	}
}
