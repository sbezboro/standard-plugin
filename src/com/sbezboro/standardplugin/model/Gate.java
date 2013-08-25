package com.sbezboro.standardplugin.model;

import org.bukkit.Location;

import com.sbezboro.standardplugin.persistence.PersistedObject;
import com.sbezboro.standardplugin.persistence.PersistedProperty;
import com.sbezboro.standardplugin.persistence.persistables.PersistableLocation;
import com.sbezboro.standardplugin.persistence.storages.GateStorage;

public class Gate extends PersistedObject {
	private PersistedProperty<String> displayName;
	private PersistedProperty<PersistableLocation> location;
	private PersistedProperty<String> targetName;
	
	private Gate target;

	public Gate(GateStorage storage, String identifier) {
		super(storage, identifier);
	}

	public Gate(GateStorage storage, String identifier, String displayName, Location location) {
		super(storage, identifier);
		
		this.displayName.setValue(displayName);
		this.location.setValue(new PersistableLocation(location));
	}

	@Override
	public void createProperties() {
		displayName = createProperty(String.class, "displayName");
		location = createProperty(PersistableLocation.class, "location");
		targetName = createProperty(String.class, "target");
	}

	public String getDisplayName() {
		return displayName.getValue();
	}

	public Location getLocation() {
		return location.getValue() != null ? location.getValue().getLocation() : null;
	}

	public Gate getTarget() {
		return target;
	}

	public void setTarget(Gate target) {
		this.target = target;
		if (target == null) {
			this.targetName.setValue(null);
		} else {
			this.targetName.setValue(target.getIdentifier());
		}
		
	}
}
