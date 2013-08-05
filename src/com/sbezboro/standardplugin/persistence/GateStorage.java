package com.sbezboro.standardplugin.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.Gate;

public class GateStorage extends ConfigStorage<Gate> {
	private Map<String, Gate> locationMap;

	public GateStorage(StandardPlugin plugin) {
		super(plugin, Gate.class, "gates");

		locationMap = new HashMap<String, Gate>();
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

	public void addGate(Gate gate) {
		Location location = gate.getLocation();

		locationMap.put(getLocationKey(location), gate);

		addObject(gate);
	}

	public void removeGate(Gate gate) {
		locationMap.remove(getLocationKey(gate.getLocation()));

		for (Gate other : idToObject.values()) {
			if (other.getTarget() == gate) {
				other.setTarget(null);

				ConfigurationSection section = idToConfig.get(other.getName());
				section.set("target", null);
			}
		}

		removeObject(gate);
	}

	public void linkGates(Gate source, Gate target) {
		source.setTarget(target);

		ConfigurationSection section = idToConfig.get(source.getName());
		section.set("target", target.getName());

		save();
	}

	public Gate getGate(String name) {
		return idToObject.get(name);
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
