package com.sbezboro.standardplugin.persistence.storages;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.persistence.IStorage;

public abstract class ConfigStorage implements IStorage {
	protected StandardPlugin plugin;
	protected String filename;
	
	protected FileConfiguration config = null;
	private File file = null;
	
	public ConfigStorage(StandardPlugin plugin, String name) {
		this.plugin = plugin;
		this.filename = name + ".yml";
	}

	@Override
	public void reload() {
		if (file == null) {
			file = new File(plugin.getDataFolder(), filename);
		}
		config = YamlConfiguration.loadConfiguration(file);
		
		InputStream defConfigStream = plugin.getResource(filename);
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
		
		load();
	}

	@Override
	public void unload() {
	}
	
	public abstract void load();
	
	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			plugin.getLogger().severe("Error saving config " + filename + "!");
		}
	}
	
}