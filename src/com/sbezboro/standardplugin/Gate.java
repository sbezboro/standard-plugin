package com.sbezboro.standardplugin;

import org.bukkit.Location;

public class Gate {
	private String name;
	private String displayName;
	private Location location;
	private Gate target;

	public Gate(String name, String displayName, Location location) {
		this.name = name.replace(".", "");
		this.displayName = displayName;
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Location getLocation() {
		return location;
	}

	public Gate getTarget() {
		return target;
	}

	public void setTarget(Gate target) {
		this.target = target;
	}
}
