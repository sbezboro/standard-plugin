package com.sbezboro.standardplugin.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sbezboro.standardplugin.StandardPlugin;

public abstract class ConfigStorage {
	protected StandardPlugin plugin;
	
	protected FileConfiguration config = null;
	private File file = null;


	public ConfigStorage(StandardPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void reload() {
		if (file == null) {
			file = new File(plugin.getDataFolder(), getFilename());
	    }
	    config = YamlConfiguration.loadConfiguration(file);
	 
	    InputStream defConfigStream = plugin.getResource(getFilename());
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	    }
	}
	
	public void save() {
		if (config == null || file == null) {
		    return;
	    }
	    try {
	        config.save(file);
	    } catch (IOException e) {
	    	plugin.getLogger().severe("Error saving config storage to file!");
	    }
	}
	
	public abstract String getFilename();
	
	public static void loadNewSettings(Configuration config) {
		Configuration defaults = config.getDefaults();
		
		if (defaults != null) {
			for (String key : defaults.getKeys(false)) {
				StandardPlugin.getPlugin().getLogger().info(key + " " + defaults.get(key).toString());
				StandardPlugin.getPlugin().getLogger().info(key + " " + config.get(key).toString());
				if (!config.contains(key)) {
					config.set(key, defaults.get(key));
				}
			}
		}
		
		StandardPlugin.getPlugin().getLogger().info(config.getKeys(false).toString());
    }
}
