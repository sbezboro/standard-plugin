package com.sbezboro.standardplugin.model;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.sbezboro.standardplugin.persistence.persistables.Persistable;
import com.sbezboro.standardplugin.persistence.persistables.PersistableImpl;
import com.sbezboro.standardplugin.persistence.persistables.PersistableLocation;

public class Gate extends PersistableImpl implements Persistable {
	private String name;
	private String displayName;
	private PersistableLocation location;
	private Gate target;

	public Gate() {
	}

	public Gate(String name, String displayName, Location location) {
		this.name = name.replace(".", "");
		this.displayName = displayName;
		this.location = new PersistableLocation(location);
	}

	@Override
	public String getIdentifier() {
		return name;
	}

	@Override
	public void loadFromPersistance(ConfigurationSection section) {
		name = section.getName();

		displayName = section.getString("displayName");
		location = new PersistableLocation();
		location.loadFromPersistance(section);
	}

	@Override
	public HashMap<String, Object> mapRepresentation() {
		HashMap<String, Object> repr = new HashMap<String, Object>();

		repr.putAll(location.mapRepresentation());
		repr.put("displayName", displayName);

		return repr;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Location getLocation() {
		return location.getLocation();
	}

	public Gate getTarget() {
		return target;
	}

	public void setTarget(Gate target) {
		this.target = target;
	}
}
