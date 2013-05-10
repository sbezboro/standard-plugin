package com.sbezboro.standardplugin.persistence;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class PersistedObject {
	protected ObjectStorage<?> storage;
	private FileConfiguration config;
	private String identifier;
	
	private boolean toCommit;
	
	public PersistedObject(ObjectStorage<?> storage, String identifier) {
		this.storage = storage;
		this.identifier = identifier;
		toCommit = false;
		
		this.config = storage.load(identifier);
		
		loadProperties();
	}
	
	protected abstract void loadProperties();
	
	public final Object loadProperty(String key, Object def) {
		if (config.contains(key)) {
			return config.get(key);
		}
		
		if (def != null) {
			saveProperty(key, def);
		}
		
		return def;
	}

	public final Object loadProperty(String key) {
		return loadProperty(key, null);
	}
	
	public final void saveProperty(String key, Object value, boolean commit) {
		config.set(key, value);
		
		toCommit = !commit;
		if (commit) {
			save();
		}
	}
	
	public final void saveProperty(String key, Object value) {
		saveProperty(key, value, true);
	}
	
	public final void save() {
		storage.save(identifier);
	}
	
	public boolean toCommit() {
		return toCommit;
	}
}
