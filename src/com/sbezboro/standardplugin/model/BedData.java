package com.sbezboro.standardplugin.model;


import java.util.HashMap;

import org.bukkit.Location;


public class BedData {
	private HashMap<String, Location> beds;
	
	public BedData() {
		beds = new HashMap<String, Location>();
	}
	
	public void setLocation(StandardPlayer player, Location location) {
		beds.put(player.getName(), location);
	}
	
	public void removeBed(StandardPlayer player) {
		beds.remove(player.getName());
	}
	
	public Location getLocation(StandardPlayer player) {
		return beds.get(player.getName());
	}

}
