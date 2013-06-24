package com.sbezboro.standardplugin.model;

import java.util.HashMap;
import java.util.Map;

public class Title {
	public static final String NEWBIE_STALKER = "newbie-stalker";
	public static final String TOP10_VETERAN = "top10-veteran";
	public static final String TOP40_VETERAN = "top40-veteran";
	public static final String VETERAN = "veteran";
	
	@SuppressWarnings("serial")
	private static final Map<String, Title> allTitles = new HashMap<String, Title>() { { 
			put(NEWBIE_STALKER, new Title(NEWBIE_STALKER, "Newbie Stalker"));
			put(TOP10_VETERAN, new Title(TOP10_VETERAN, "Top 10 Veteran"));
			put(TOP40_VETERAN, new Title(TOP40_VETERAN, "Top 40 Veteran"));
			put(VETERAN, new Title(VETERAN, "Veteran"));
		}
	};
	
	private String name;
	private String displayName;
	
	public Title(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
	}
	
	public static Title getTitle(String name) {
		return allTitles.get(name);
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}
}
