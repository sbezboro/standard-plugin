package com.sbezboro.standardplugin.persistence;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sbezboro.standardplugin.StandardPlugin;

public abstract class ObjectStorage<T extends PersistedObject> {
	protected StandardPlugin plugin;
	private String dataFolder;
	
	private HashMap<String, File> idToFile;
	private HashMap<String, FileConfiguration> idToConfig;

	private HashMap<String, T> idToObject;
	

	public ObjectStorage(StandardPlugin plugin, String dataFolder) {
		this.plugin = plugin;
		this.dataFolder = dataFolder;
		
		idToFile = new HashMap<String, File>();
		idToConfig = new HashMap<String, FileConfiguration>();
		
		idToObject = new HashMap<String, T>();
	}
	
	public FileConfiguration load(String identifier) {
		File file = idToFile.get(identifier);
		FileConfiguration config = null;
		
		if (file == null) {
			file = new File(plugin.getDataFolder(), dataFolder + "/" + identifier + ".yml");
	    } else {
	    	config = idToConfig.get(identifier);
	    }
		
		if (config == null) {
			config = YamlConfiguration.loadConfiguration(file);
		}
		
		idToFile.put(identifier, file);
		idToConfig.put(identifier, config);
		
		return config;
	}
	
	public void save(String identifer) {
		File file = idToFile.get(identifer);
		FileConfiguration config = idToConfig.get(identifer);
		if (config == null || file == null) {
		    return;
	    }
		
	    try {
	        config.save(file);
	    } catch (IOException e) {
	    	plugin.getLogger().severe("Error saving object storage to file!");
	    }
	}
	
	protected T getObject(String identifier) {
		return idToObject.get(identifier);
	}
	
	protected void cacheObject(String identifier, T object) {
		idToObject.put(identifier, object);
	}
	
	public void unload() {
		for (T object : idToObject.values()) {
			if (object.toCommit()) {
				object.save();
			}
		}
	}
}
