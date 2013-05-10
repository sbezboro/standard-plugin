package com.sbezboro.standardplugin.model;


import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public class BedData {
	private HashMap<String, Location> beds;
	
	public BedData() {
		beds = new HashMap<String, Location>();
	}
	
	public void setLocation(Player player, Location location) {
		beds.put(player.getName(), location);
	}
	
	public void removeBed(Player player) {
		beds.remove(player.getName());
	}
	
	public Location getLocation(Player player) {
		return beds.get(player.getName());
	}

}
