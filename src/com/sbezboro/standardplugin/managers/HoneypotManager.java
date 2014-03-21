package com.sbezboro.standardplugin.managers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.Honeypot;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.persistence.storages.HoneypotStorage;
import com.sbezboro.standardplugin.util.MiscUtil;

public class HoneypotManager extends BaseManager {
	
	private HoneypotStorage storage;
	private Map<String, Honeypot> locationMap;

	public HoneypotManager(StandardPlugin plugin, HoneypotStorage storage) {
		super(plugin);
		
		this.storage = storage;
		this.locationMap = new HashMap<String, Honeypot>();
		
		for (Honeypot honeypot : storage.getHoneypots()) {
			locationMap.put(MiscUtil.getLocationKey(honeypot.getLocation()), honeypot);
		}
		
		plugin.getLogger().info(String.format("Loaded %d %s", locationMap.size(), MiscUtil.pluralize("honeypot", locationMap.size())));
	}

	private boolean testHoneypotLocation(Location location) {
		World world = location.getWorld();
		int newX = location.getBlockX();
		int newY = location.getBlockY();
		int newZ = location.getBlockZ();
		
		for (int x = newX - 2; x <= newX + 2; ++x) {
			for (int z = newZ - 2; z <= newZ + 2; ++z) {
				for (int y = newY - 2; y <= newY + 2; ++y) {
					Block block = world.getBlockAt(x, y, z);
					
					if (block.getType() != Material.DIRT && block.getType() != Material.STONE
							&& block.getType() != Material.GRAVEL && block.getType() != Material.COAL_ORE
							&& block.getType() != Material.IRON_ORE) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	public void createHoneypot(Location location) {
		// No location means choose a random one near spawn
		if (location == null) {
			World overworld = plugin.getServer().getWorld(StandardPlugin.OVERWORLD_NAME);
			Location spawn = overworld.getSpawnLocation();
			
			int x, y, z;

			do {
				x = (int) (spawn.getBlockX() + (Math.random() * 1000.0) - 500);
				y = (int) (40 + Math.random() * 20.0);
				z = (int) (spawn.getBlockZ() + (Math.random() * 1000.0) - 500);
				
				location = new Location(overworld, x, y, z);
			} while(!testHoneypotLocation(location));
		}
		
		Honeypot honeypot = new Honeypot(location);
		locationMap.put(MiscUtil.getLocationKey(location), honeypot);
		
		Block block = location.getWorld().getBlockAt(location);
		block.setType(Material.CHEST);
		
		storage.addHoneypot(honeypot);
		
		plugin.getLogger().info("Honeypot created at " + MiscUtil.locationFormat(location));
	}
	
	public void checkChest(Location location, StandardPlayer player) {
		Honeypot honeypot = locationMap.get(MiscUtil.getLocationKey(location));
		
		if (honeypot != null && !honeypot.isDiscovered() && !honeypot.isRemoved()) {
			honeypot.setDiscovered(player.getName());
			storage.saveHoneypots();
			
			player.incrementHoneypotsDiscovered();
			
			plugin.getServer().getConsoleSender().sendMessage(String.format("%sATTENTION! %s has found a honeypot at %s", 
					ChatColor.YELLOW, player.getName(), MiscUtil.locationFormat(location)));
		}
	}

	public Location removeOldestHoneypot() {
		Honeypot honeypot = null;
		
		for (Honeypot h : storage.getHoneypots()) {
			if (!h.isRemoved()) {
				honeypot = h;
				break;
			}
		}
		
		if (honeypot == null) {
			return null;
		}
		
		honeypot.setRemoved(true);
		storage.saveHoneypots();
		
		Location location = honeypot.getLocation();
		Block block = location.getWorld().getBlockAt(location);
		block.setType(Material.STONE);
		
		return location;
	}

}
