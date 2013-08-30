package com.sbezboro.standardplugin.persistence.persistables;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class PersistableLocation extends PersistableImpl implements Persistable {
	private Location location;

	public PersistableLocation() {
	}

	public PersistableLocation(Location location) {
		this.location = location;
	}

	@Override
	public String getIdentifier() {
		return null;
	}

	@Override
	public void loadFromPersistance(Map<String, Object> map) {
		String worldName = (String) map.get("world");
		double x = (Double) map.get("x");
		double y = (Double) map.get("y");
		double z = (Double) map.get("z");
		double yaw = (Double) map.get("yaw");
		double pitch = (Double) map.get("pitch");

		World world = Bukkit.getWorld(worldName);
		location = new Location(world, x, y, z, (float) yaw, (float) pitch);
	}

	@Override
	public Map<String, Object> mapRepresentation() {
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
