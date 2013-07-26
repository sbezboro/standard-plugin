package com.sbezboro.standardplugin.persistence;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.exceptions.NotPersistableException;
import com.sbezboro.standardplugin.persistence.persistables.Persistable;
import com.sbezboro.standardplugin.util.MiscUtil;

public class PersistedProperty<T> {
	private PersistedObject object;
	private Class<T> cls;
	private String name;

	private T value;

	@SuppressWarnings("serial")
	private static final Map<Class<?>, Object> defaultValueMap = new HashMap<Class<?>, Object>() {
		{
			put(Boolean.class, Boolean.FALSE);
			put(Integer.class, Integer.valueOf(0));
		}
	};

	@SuppressWarnings("unchecked")
	public PersistedProperty(PersistedObject object, Class<T> cls, String name, Object def) {
		this.cls = cls;
		this.name = name;
		this.object = object;

		try {
			Object obj = object.loadProperty(name, null);

			if (Persistable.class.isAssignableFrom(cls)) {
				Persistable persistable = (Persistable) cls.newInstance();
				if (obj != null) {
					ConfigurationSection section = (ConfigurationSection) obj;
					persistable.loadFromPersistance(section);
				}

				value = (T) persistable;
			} else if (MiscUtil.isWrapperType(cls)) {
				value = (T) obj;
			} else {
				throw new NotPersistableException("Class " + cls.getName() + " does not implement Persistable nor is a primative wrapper.");
			}
		} catch (Exception e) {
			StandardPlugin.getPlugin().getLogger().severe(e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	public T getValue() {
		if (value == null) {
			return (T) defaultValueMap.get(cls);
		}
		return value;
	}

	public void setValue(T value) {
		this.value = value;

		object.saveProperty(name, value);
	}
}
