package com.sbezboro.standardplugin.managers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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
	
	public void createHoneypot(Location location) {
		Honeypot honeypot = new Honeypot(location);
		locationMap.put(MiscUtil.getLocationKey(location), honeypot);
		
		Block block = location.getWorld().getBlockAt(location);
		block.setType(Material.CHEST);
		
		storage.addHoneypot(honeypot);
		
		plugin.getLogger().info("Honeypot created at " + MiscUtil.locationFormat(location));
	}
	
	public void checkChest(Location location, StandardPlayer player) {
		Honeypot honeypot = locationMap.get(MiscUtil.getLocationKey(location));
		
		if (honeypot != null && !honeypot.isDiscovered()) {
			honeypot.setDiscovered(player.getName());
			storage.saveHoneypots();
			
			plugin.getServer().getConsoleSender().sendMessage(String.format("%sATTENTION! %s has found a honeypot at %s", 
					ChatColor.YELLOW, player.getName(), MiscUtil.locationFormat(location)));
		}
	}

}
