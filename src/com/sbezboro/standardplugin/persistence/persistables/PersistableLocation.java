package com.sbezboro.standardplugin.persistence.persistables;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;


public class PersistableLocation extends PersistableImpl implements Persistable {
	private Location location;

	public PersistableLocation() {}
	
	public PersistableLocation(Location location) {
		this.location = location;
	}

	@Override
	public String getIdentifier() {
		return null;
	}

	@Override
	public void loadFromPersistance(ConfigurationSection section) {
		String worldName = section.getString("world");
		double x = section.getDouble("x");
		double y = section.getDouble("y");
		double z = section.getDouble("z");
		float yaw = (float) section.getDouble("yaw");
		float pitch = (float) section.getDouble("pitch");
		
		World world = Bukkit.getWorld(worldName);
		location = new Location(world, x, y, z, yaw, pitch);
	}

	@Override
	public HashMap<String, Object> mapRepresentation() {
		if (location == null) {
			return null;
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("world", location.getWorld().getName());
		map.put("x", location.getX());
		map.put("y", location.getY());
		map.put("z", location.getZ());
		map.put("yaw", location.getYaw());
		map.put("pitch", location.getPitch());
		
		return map;
	}
	
	public Location getLocation() {
		return location;
	}
	
}
