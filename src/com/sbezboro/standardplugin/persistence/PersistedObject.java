package com.sbezboro.standardplugin.persistence;

import java.util.ArrayList;

import com.sbezboro.standardplugin.persistence.persistables.Persistable;
import com.sbezboro.standardplugin.persistence.storages.FileStorage;

public abstract class PersistedObject {
	protected FileStorage storage;
	private String identifier;
	
	private ArrayList<Persisted> persistedPorperties;

	private boolean toCommit;

	public PersistedObject(FileStorage storage, String identifier) {
		this.storage = storage;
		this.identifier = identifier;
		
		persistedPorperties = new ArrayList<Persisted>();
		
		toCommit = false;

		storage.load(identifier);
		
		createProperties();
	}

	public abstract void createProperties();
	
	public void loadProperties() {
		for (Persisted propery : persistedPorperties) {
			propery.load();
		}
	}

	protected final <T> PersistedProperty<T> createProperty(Class<T> cls, String name, Object def) {
		PersistedProperty<T> property = new PersistedProperty<T>(this, cls, name, def);
		persistedPorperties.add(property);
		return property;
	}

	protected final <T> PersistedProperty<T> createProperty(Class<T> cls, String name) {
		return createProperty(cls, name, null);
	}

	protected final <T> PersistedListProperty<T> createList(Class<T> cls, String name) {
		PersistedListProperty<T> property = new PersistedListProperty<T>(this, cls, name);
		persistedPorperties.add(property);
		return property;
	}

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

	public final void saveProperty(String key, Object value, boolean commit) {
		if (value instanceof Persistable) {
			Persistable persistable = (Persistable) value;
			value = persistable.persistableRepresentation();
		}

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

	public final boolean toCommit() {
		return toCommit;
	}
	
	public String getIdentifier() {
		return identifier;
	}
}
