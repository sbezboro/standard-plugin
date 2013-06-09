package com.sbezboro.standardplugin.persistence;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;


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
	public PersistedProperty(PersistedObject object, Class<T> cls, String name, Object def) {
		this.cls = cls;
		this.name = name;
		this.object = object;
		
		try {
			value = (T) loaded(object.loadProperty(name, null));
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
	
	// If required, loads the key value pairs representing this object from disk
	// and transforms them into object properties inside a newly instantiated object
	private Object loaded(Object value) {
		if (value != null) {
			if (this.cls == Location.class) {
				ConfigurationSection section = (ConfigurationSection) value;
				
				String worldName = section.getString("world");
				double x = section.getDouble("x");
				double y = section.getDouble("y");
				double z = section.getDouble("z");
				float yaw = (float) section.getDouble("yaw");
				float pitch = (float) section.getDouble("pitch");
				
				World world = Bukkit.getWorld(worldName);
				return new Location(world, x, y, z, yaw, pitch);
			}
		}
		
		return value;
	}
	
	// If required, transforms the object to be saved into a persistable version
	private Object savable(T value) {
		if (value != null) {
			if (this.cls == Location.class) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				
				Location location = (Location) value;
				
				map.put("world", location.getWorld().getName());
				map.put("x", location.getX());
				map.put("y", location.getY());
				map.put("z", location.getZ());
				map.put("yaw", location.getYaw());
				map.put("pitch", location.getPitch());
				return map;
			}
		}
		
		return value;
	}

	public void setValue(T value) {
		this.value = value;
		
		object.saveProperty(name, savable(value));
	}
}
