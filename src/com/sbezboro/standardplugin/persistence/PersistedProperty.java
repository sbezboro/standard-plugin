package com.sbezboro.standardplugin.persistence;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.exceptions.NotPersistableException;
import com.sbezboro.standardplugin.persistence.persistables.Persistable;
import com.sbezboro.standardplugin.util.MiscUtil;

public class PersistedProperty<T> implements PersistedBase {
	private PersistedObject object;
	private Class cls;
	private String name;
	private Object def;

	private T value;

	@SuppressWarnings("serial")
	private static final Map<Class<?>, Object> defaultValueMap = new HashMap<Class<?>, Object>() {
		{
			put(Boolean.class, Boolean.FALSE);
			put(Integer.class, Integer.valueOf(0));
			put(Long.class, Long.valueOf(0));
		}
	};

	public PersistedProperty(PersistedObject object, Class cls, String name, Object def) {
		this.cls = cls;
		this.name = name;
		this.object = object;
		this.def = def;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void load() {
		try {
			Object obj = object.loadProperty(name, def);

			if (Persistable.class.isAssignableFrom(cls)) {
				Persistable persistable = (Persistable) cls.newInstance();
				if (obj != null) {
					ConfigurationSection section = (ConfigurationSection) obj;
					persistable.loadFromPersistance(section.getValues(false));
				}

				value = (T) persistable;
			} else if (MiscUtil.isWrapperType(cls)) {
				value = (T) obj;
			} else {
				throw new NotPersistableException("Class " + cls.getName() + " does not implement Persistable nor is a primative wrapper.");
			}
		} catch (Exception e) {
			StandardPlugin.getPlugin().getLogger().severe("Could not load persisted property!");
			e.printStackTrace();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getValue() {
		if (value == null) {
			return (T) defaultValueMap.get(cls);
		}
		return value;
	}

	public void setValue(T value, boolean commit) {
		this.value = value;

		object.saveProperty(name, value, commit);
	}

	public void setValue(T value) {
		setValue(value, true);
	}
}
