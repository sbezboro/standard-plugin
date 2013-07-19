package com.sbezboro.standardplugin.model;


public class Title {
	public static final String NEWBIE_STALKER = "newbie-stalker";
	public static final String TOP10_VETERAN = "top10-veteran";
	public static final String TOP40_VETERAN = "top40-veteran";
	public static final String VETERAN = "veteran";
	
	private String name;
	private String displayName;
	private boolean hidden;
	
	public Title(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
		this.hidden = false;
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
