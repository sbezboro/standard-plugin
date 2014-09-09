package com.sbezboro.standardplugin.model;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import com.sbezboro.standardplugin.persistence.persistables.Persistable;
import com.sbezboro.standardplugin.persistence.persistables.PersistableImpl;
import com.sbezboro.standardplugin.persistence.persistables.PersistableLocation;

public class Honeypot extends PersistableImpl implements Persistable {
	private PersistableLocation location;
	private long creationDate;
	private String discoverer;
	private long discoverDate;
	private boolean removed;

	public Honeypot() {
	}
	
	public Honeypot(Location location) {
		this.location = new PersistableLocation(location);
		
		this.creationDate = System.currentTimeMillis();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void loadFromPersistance(Map<String, Object> map) {
		location = new PersistableLocation();
		location.loadFromPersistance((Map<String, Object>) map.get("location"));
		
		creationDate = (Long) map.get("creation-date");
		
		try {
			discoverer = (String) map.get("discoverer");
			discoverDate = (Long) map.get("discover-date");
		} catch (NullPointerException e) {
			// Nothing
		}
		
		try {
			removed = (Boolean) map.get("removed");
		} catch (NullPointerException e) {
			// Nothing
		}
	}

	@Override
	public Map<String, Object> mapRepresentation() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("location", location.persistableRepresentation());
		
		map.put("creation-date", creationDate);
		
		if (isDiscovered()) {
			map.put("discoverer", discoverer);
			map.put("discover-date", discoverDate);
		}
		
		if (removed) {
			map.put("removed", removed);
		}
		
		return map;
	}
	
	public Location getLocation() {
		return location.getLocation();
	}
	
	public boolean isDiscovered() {
		return discoverer != null && discoverer.length() > 0;
	}
	
	public void setDiscovered(String uuid) {
		discoverer = uuid;
		discoverDate = System.currentTimeMillis();
	}
	
	public boolean isRemoved() {
		return removed;
	}
	
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

}
