package com.sbezboro.standardplugin.persistence;

import java.util.ArrayList;

import com.sbezboro.standardplugin.persistence.persistables.Persistable;
import com.sbezboro.standardplugin.persistence.storages.FileStorage;

public abstract class PersistedObject {
	protected FileStorage storage;
	private String identifier;
	
	private ArrayList<PersistedBase> persistedProperties;

	private boolean toCommit;

	public PersistedObject(FileStorage storage, String identifier) {
		this.storage = storage;
		this.identifier = identifier;
		
		persistedProperties = new ArrayList<PersistedBase>();
		
		toCommit = false;

		storage.load(identifier);
		
		createProperties();
	}

	public abstract void createProperties();
	
	public void loadProperties() {
		for (PersistedBase property : persistedProperties) {
			property.load();
		}
	}

	protected final <T> PersistedProperty<T> createProperty(Class cls, String name, Object def) {
		PersistedProperty<T> property = new PersistedProperty<T>(this, cls, name, def);
		persistedProperties.add(property);
		return property;
	}

	protected final <T> PersistedProperty<T> createProperty(Class cls, String name) {
		return createProperty(cls, name, null);
	}

	protected final <T> PersistedProperty<T> createProperty(PersistedPropertyDefinition definition) {
		return createProperty(definition.getCls(), definition.getIdentifier());
	}

	protected final <T> PersistedListProperty<T> createList(Class<T> cls, String name) {
		PersistedListProperty<T> property = new PersistedListProperty<T>(this, cls, name);
		persistedProperties.add(property);
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
	
	public final void setIdentifier(String identifier) {
		storage.rename(this.identifier, identifier);
		this.identifier = identifier;
	}

	public final void save() {
		for (PersistedBase property : persistedProperties) {
			saveProperty(property.getName(), property.getValue(), false);
		}

		storage.save(identifier);

		toCommit = false;
	}

	public final boolean toCommit() {
		return toCommit;
	}
	
	public String getIdentifier() {
		return identifier;
	}
}
