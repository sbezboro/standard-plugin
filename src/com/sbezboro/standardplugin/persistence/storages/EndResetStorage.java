package com.sbezboro.standardplugin.persistence.storages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.persistence.persistables.PersistableLocation;

public class EndResetStorage extends ConfigStorage {
	private int currentEndId;
	private long nextReset;
	private ArrayList<PersistableLocation> activePortals;
	private ArrayList<PersistableLocation> inactivePortals;

	public EndResetStorage(StandardPlugin plugin) {
		super(plugin, "end-reset");
	}

	@Override
	@SuppressWarnings("unchecked")
	public void load() {
		currentEndId = config.getInt("current-end-id");
		nextReset = config.getLong("next-reset");
		activePortals = new ArrayList<PersistableLocation>();
		inactivePortals = new ArrayList<PersistableLocation>();
		
		List<?> portals = config.getList("active-portals");
		if (portals != null) {
			for (Object section : portals) {
				PersistableLocation location = new PersistableLocation();
				location.loadFromPersistance((Map<String, Object>) section);
				activePortals.add(location);
			}
		}
		
		portals = config.getList("inactive-portals");
		if (portals != null) {
			for (Object section : portals) {
				PersistableLocation location = new PersistableLocation();
				location.loadFromPersistance((Map<String, Object>) section);
				inactivePortals.add(location);
			}
		}
	}
	
	public long getNextReset() {
		return nextReset;
	}
	
	public void setNextReset(long nextReset) {
		this.nextReset = nextReset;
		config.set("next-reset", nextReset);
		save();
	}
	
	public int getCurrentEndId() {
		return currentEndId;
	}
	
	public void incrementEndId() {
		currentEndId++;
		config.set("current-end-id", currentEndId);
		save();
	}
	
	public List<Location> getActivePortals() {
		ArrayList<Location> list = new ArrayList<Location>();
		for (PersistableLocation persistableLocation : activePortals) {
			list.add(persistableLocation.getLocation());
		}
		
		return list;
	}
	
	public void addActivePortalLocation(Location location) {
		activePortals.add(new PersistableLocation(location));
		saveList(activePortals, "active-portals");
	}
	
	public void removeActivePortalLocation(Location location) {
		for (PersistableLocation persistableLocation : activePortals) {
			if (persistableLocation.getLocation().equals(location)) {
				activePortals.remove(persistableLocation);
				break;
			}
		}
		
		saveList(activePortals, "active-portals");
	}
	
	public void addInactivePortalLocation(Location location) {
		inactivePortals.add(new PersistableLocation(location));
		saveList(inactivePortals, "inactive-portals");
	}
	
	private void saveList(List<PersistableLocation> list, String configName) {
		ArrayList<Map<String, Object>> newList = new ArrayList<Map<String,Object>>();
		for (PersistableLocation persistableLocation : list) {
			newList.add(persistableLocation.mapRepresentation());
		}
		
		config.set(configName, newList);
		save();
	}
	
	public Location getClosestPortal(Location source) {
		Location portalLocation = null;
		double maxDistance = Double.MAX_VALUE;
		
		for (PersistableLocation location : activePortals) {
			if (source.getWorld() == location.getLocation().getWorld()) {
				double distance = source.distanceSquared(location.getLocation());
				if (distance < maxDistance) {
					maxDistance = distance;
					portalLocation = location.getLocation();
				}
			}
		}
		
		return portalLocation;
	}
	
	public boolean isNearPortal(Location location, int blocks) {
		Location portal = getClosestPortal(location);
		
		if (portal != null) {
			return Math.abs(portal.getBlockX() - location.getBlockX()) < blocks
					&& Math.abs(portal.getBlockZ() - location.getBlockZ()) < blocks;
		}
		
		return false;
	}

}
