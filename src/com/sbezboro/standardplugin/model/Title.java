package com.sbezboro.standardplugin.model;

import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;

import com.sbezboro.standardplugin.persistence.persistables.Persistable;
import com.sbezboro.standardplugin.persistence.persistables.PersistableImpl;


public class Title extends PersistableImpl implements Persistable {
	public static final String NEWBIE_STALKER = "newbie-stalker";
	public static final String TOP10_VETERAN = "top10-veteran";
	public static final String TOP40_VETERAN = "top40-veteran";
	public static final String VETERAN = "veteran";
	
	private String name;
	private String displayName;
	private boolean hidden;
	private boolean broadcast;
	
	public Title() {}
	
	public Title(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
		this.hidden = false;
		this.broadcast = false;
	}
	
	@Override
	public String getIdentifier() {
		return name;
	}

	@Override
	public void loadFromPersistance(ConfigurationSection section) {
		name = section.getName();
		
		displayName = section.getString("displayName");
		hidden = section.getBoolean("hidden");
		broadcast = section.getBoolean("broadcast");
	}

	@Override
	public HashMap<String, Object> mapRepresentation() {
		HashMap<String, Object> repr = new HashMap<String, Object>();
		
		repr.put("displayName", displayName);
		repr.put("hidden", hidden);
		repr.put("broadcast", broadcast);
		
		return repr;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public String getDescription() {
		return name + " - " + displayName;
	}
	
	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
