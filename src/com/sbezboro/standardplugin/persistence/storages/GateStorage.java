package com.sbezboro.standardplugin.persistence.storages;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.Gate;

public class GateStorage extends SingleFileStorage<Gate> {
	private Map<String, Gate> locationMap;

	public GateStorage(StandardPlugin plugin) {
		super(plugin, "gates");

		locationMap = new HashMap<String, Gate>();
	}

	@Override
	public Gate createObject(String identifier) {
		return new Gate(this, identifier);
	}

	@Override
	public void onPostLoad(Set<String> keys) {
		locationMap.clear();

		for (String key : keys) {
			Gate gate = idToObject.get(key);
			locationMap.put(getLocationKey(gate.getLocation()), gate);

			ConfigurationSection section = config.getConfigurationSection(key);
			String target = section.getString("target");

			Gate targetGate = idToObject.get(target);
			if (targetGate != null) {
				Gate sourceGate = idToObject.get(key);
				sourceGate.setTarget(targetGate);
			}
		}
	}
	
	public void createGate(String name, String displayName, Location location) {
		Gate gate = new Gate(this, name, displayName, location);
		locationMap.put(getLocationKey(gate.getLocation()), gate);
		
		addObject(gate);
	}

	public void removeGate(Gate gate) {
		locationMap.remove(getLocationKey(gate.getLocation()));

		for (Gate other : idToObject.values()) {
			if (other.getTarget() == gate) {
				other.setTarget(null);
			}
		}

		removeObject(gate);
	}

	public Gate getGate(String name) {
		return getObject(name);
	}

	public Gate getGate(Location location) {
		return locationMap.get(getLocationKey(location));
	}

	public Collection<Gate> getGates() {
		return idToObject.values();
	}

	private static String getLocationKey(Location location) {
		return location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
	}

}
