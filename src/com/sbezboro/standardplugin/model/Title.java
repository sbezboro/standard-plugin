package com.sbezboro.standardplugin.model;

import java.util.HashMap;
import java.util.Map;

public class Title {
	public static final String newbieStalker = "newbie-stalker";
	@SuppressWarnings("serial")
	private static final Map<String, Title> allTitles = new HashMap<String, Title>() { { 
			put(newbieStalker, new Title(newbieStalker, "Newbie Stalker"));
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
