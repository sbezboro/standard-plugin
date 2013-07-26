package com.sbezboro.standardplugin.persistence;

import com.sbezboro.standardplugin.persistence.persistables.Persistable;

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

	protected final <T> PersistedProperty<T> loadProperty(Class<T> cls, String name) {
		return new PersistedProperty<T>(this, cls, name, null);
	}

	protected final <T> PersistedProperty<T> loadProperty(Class<T> cls, String name, Object def) {
		return new PersistedProperty<T>(this, cls, name, def);
	}

	protected final <T> PersistedList<T> loadList(Class<T> cls, String name) {
		return new PersistedList<T>(this, cls, name);
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
}
