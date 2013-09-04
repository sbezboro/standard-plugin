package com.sbezboro.standardplugin.model;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import com.sbezboro.standardplugin.persistence.persistables.Persistable;
import com.sbezboro.standardplugin.persistence.persistables.PersistableImpl;
import com.sbezboro.standardplugin.persistence.persistables.PersistableLocation;

public class Honeypot extends PersistableImpl implements Persistable {
	private PersistableLocation location;
	private String discoverer;
	private long discoverDate;

	public Honeypot() {
	}
	
	public Honeypot(Location location) {
		this.location = new PersistableLocation(location);
	}


	@Override
	@SuppressWarnings("unchecked")
	public void loadFromPersistance(Map<String, Object> map) {
		location = new PersistableLocation();
		location.loadFromPersistance((Map<String, Object>) map.get("location"));
		
		discoverer = (String) map.get("discoverer");
		discoverDate = (Long) map.get("discover-date");
	}

	@Override
	public Map<String, Object> mapRepresentation() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("location", location.persistableRepresentation());
		
		if (isDiscovered()) {
			map.put("discoverer", discoverer);
			map.put("discover-date", discoverDate);
		}
		
		return map;
	}
	
	public Location getLocation() {
		return location.getLocation();
	}
	
	public boolean isDiscovered() {
		return discoverer != null && discoverer.length() > 0;
	}
	
	public void setDiscovered(String username) {
		discoverer = username;
		discoverDate = System.currentTimeMillis();
	}

}
