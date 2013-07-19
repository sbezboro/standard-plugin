package com.sbezboro.standardplugin.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.Gate;

public class GateStorage extends ConfigStorage {
	private Map<String, Gate> gates;
	private Map<String, Gate> locationMap;

	public GateStorage(StandardPlugin plugin) {
		super(plugin, "gates");
	}
	
	@Override
	public void loadData(Set<String> keys) {
		gates = new HashMap<String, Gate>();
		locationMap = new HashMap<String,Gate>();
		
		for (String key : keys) {
			ConfigurationSection section = config.getConfigurationSection(key);
			String displayName = section.getString("displayName");
			String worldName = section.getString("world");
			double x = section.getDouble("x");
			double y = section.getDouble("y");
			double z = section.getDouble("z");
			float yaw = (float) section.getDouble("yaw");
			float pitch = (float) section.getDouble("pitch");
			
			World world = Bukkit.getWorld(worldName);
			Location location = new Location(world, x, y, z, yaw, pitch);
			
			Gate gate = new Gate(key, displayName, location);
			gates.put(key, gate);
			
			locationMap.put(getLocationKey(location), gate);
		}
		
		for (String key : keys) {
			ConfigurationSection section = config.getConfigurationSection(key);
			String target = section.getString("target");
			
			Gate targetGate = gates.get(target);
			if (targetGate != null) {
				Gate sourceGate = gates.get(key);
				sourceGate.setTarget(targetGate);
			}
		}
	}
	
	public void addGate(Gate gate) {
		Location location = gate.getLocation();
		
		gates.put(gate.getName(), gate);
		locationMap.put(getLocationKey(location), gate);
		
		ConfigurationSection section = config.createSection(gate.getName());
		section.set("displayName", gate.getDisplayName());
		section.set("world", location.getWorld().getName());
		section.set("x", location.getX());
		section.set("y", location.getY());
		section.set("z", location.getZ());
		section.set("yaw", location.getYaw());
		section.set("pitch", location.getPitch());
		
		save();
	}
	
	public void removeGate(Gate gate) {
		gates.remove(gate.getName());
		locationMap.remove(getLocationKey(gate.getLocation()));
		
		for (Gate other : gates.values()) {
			if (other.getTarget() == gate) {
				other.setTarget(null);
				
				ConfigurationSection section = config.getConfigurationSection(other.getName());
				section.set("target", null);
			}
		}
		
		config.set(gate.getName(), null);
		
		save();
	}

	public void linkGates(Gate source, Gate target) {
		source.setTarget(target);

		ConfigurationSection section = config.getConfigurationSection(source.getName());
		section.set("target", target.getName());
		
		save();
	}
	
	public Gate getGate(String name) {
		return gates.get(name);
	}
	
	public Gate getGate(Location location) {
		return locationMap.get(getLocationKey(location));
	}

	public Collection<Gate> getGates() {
		return gates.values();
	}

	private String getLocationKey(Location location) {
		return location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
	}

}
