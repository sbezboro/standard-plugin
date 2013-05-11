package com.sbezboro.standardplugin.persistence;


public abstract class PersistedObject {
	protected ObjectStorage<?> storage;
	private String identifier;
	
	private boolean toCommit;
	
	public PersistedObject(ObjectStorage<?> storage, String identifier) {
		this.storage = storage;
		this.identifier = identifier;
		toCommit = false;
		
		storage.load(identifier);
		
		loadProperties();
	}
	
	protected abstract void loadProperties();
	
	public final Object loadProperty(String key, Object def) {
		Object value = storage.loadProperty(identifier, key);
		if (value != null) {
			return value;
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
		storage.saveProperty(identifier, key, value);
		
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
