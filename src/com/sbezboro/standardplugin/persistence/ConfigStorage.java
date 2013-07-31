package com.sbezboro.standardplugin.persistence;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.persistence.persistables.PersistableImpl;

public abstract class ConfigStorage<T extends PersistableImpl> implements IStorage {
	protected HashMap<String, ConfigurationSection> idToConfig;
	protected HashMap<String, T> idToObject;

	protected StandardPlugin plugin;
	private Class<T> cls;
	private String filename;

	protected FileConfiguration config = null;
	private File file = null;

	public ConfigStorage(StandardPlugin plugin, Class<T> cls, String type) {
		this.plugin = plugin;
		this.cls = cls;
		this.filename = type + ".yml";

		idToObject = new HashMap<String, T>();
		idToConfig = new HashMap<String, ConfigurationSection>();
	}

	@Override
	public final void reload() {
		if (file == null) {
			file = new File(plugin.getDataFolder(), filename);
		}
		config = YamlConfiguration.loadConfiguration(file);

		InputStream defConfigStream = plugin.getResource(filename);
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}

		idToConfig.clear();
		idToObject.clear();

		Set<String> keys = config.getKeys(false);
		for (String key : keys) {
			try {
				ConfigurationSection section = config.getConfigurationSection(key);

				T object = cls.newInstance();
				object.loadFromPersistance(section);

				idToObject.put(key, object);
				idToConfig.put(key, section);
			} catch (Exception e) {
				plugin.getLogger().severe("Couldn't load object " + key + " from " + filename + "! " + e.toString());
			}
		}

		onPostLoad(keys);
	}

	@Override
	public void unload() {
	}

	protected void addObject(T object) {
		idToObject.put(object.getIdentifier(), object);

		ConfigurationSection section = config.createSection(object.getIdentifier(), object.mapRepresentation());
		idToConfig.put(object.getIdentifier(), section);
	}

	protected void removeObject(T object) {
		idToObject.remove(object.getIdentifier());
		idToConfig.remove(object.getIdentifier());

		config.set(object.getIdentifier(), null);
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

	public abstract void onPostLoad(Set<String> keys);
}
