package com.sbezboro.standardplugin.persistence.storages;

import org.bukkit.configuration.ConfigurationSection;

public interface FileStorage extends IStorage {
	public ConfigurationSection load(String identifier);
	public void save(String identifier);
	
	public Object loadProperty(String identifier, String key);
	public void saveProperty(String identifier, String key, Object value);
	
	public void remove(String identifier);
	public void rename(String fromIdentifier, String toIdentifier);
}
