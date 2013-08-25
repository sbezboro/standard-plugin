package com.sbezboro.standardplugin.persistence.storages;

import org.bukkit.configuration.ConfigurationSection;

import com.sbezboro.standardplugin.persistence.IStorage;

public interface FileStorage extends IStorage {
	public ConfigurationSection load(String identifier);
	public void save(String identifier);
	
	public Object loadProperty(String identifier, String key);
	public void saveProperty(String identifier, String key, Object value);
}
