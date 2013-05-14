package com.sbezboro.standardplugin.persistence;

import java.util.HashMap;
import java.util.Map;


public class PersistedProperty<T> {
	private PersistedObject object;
	private Class<T> cls;
	private String name;
	
	private T value;
	
	@SuppressWarnings("serial")
	private static final Map<Class<?>, Object> defaultValueMap = new HashMap<Class<?>, Object>() { { 
			put(Boolean.class, Boolean.FALSE);
			put(Integer.class, Integer.valueOf(0)); 
		}
	};
	
	@SuppressWarnings("unchecked")
	public PersistedProperty(PersistedObject object, Class<T> cls, String name) {
		this.cls = cls;
		this.name = name;
		this.object = object;
		
		try {
			value = (T) object.loadProperty(name, null);
		} catch (Exception e) {
			value = (T) defaultValueMap.get(cls);
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
