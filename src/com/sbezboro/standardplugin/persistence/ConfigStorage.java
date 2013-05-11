package com.sbezboro.standardplugin.persistence;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sbezboro.standardplugin.StandardPlugin;

public abstract class ConfigStorage implements IStorage {
	protected StandardPlugin plugin;
	
	protected FileConfiguration config = null;
	private File file = null;


	public ConfigStorage(StandardPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public final void reload() {
		if (file == null) {
			file = new File(plugin.getDataFolder(), getFilename());
	    }
	    config = YamlConfiguration.loadConfiguration(file);
	 
	    InputStream defConfigStream = plugin.getResource(getFilename());
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	    }
	    
	    loadData(config.getKeys(false));
	}

	@Override
	public void unload() {
	}
	
	public final void save() {
		if (config == null || file == null) {
		    return;
	    }
	    try {
	        config.save(file);
	    } catch (IOException e) {
	    	plugin.getLogger().severe("Error saving config storage to file!");
	    }
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	public abstract void loadData(Set<String> keys);
	
	public abstract String getFilename();
}
