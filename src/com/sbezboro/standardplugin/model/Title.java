package com.sbezboro.standardplugin.model;

import java.util.HashMap;
import java.util.Map;

import com.sbezboro.standardplugin.persistence.Persistable;
import com.sbezboro.standardplugin.persistence.PersistableImpl;

public class Title extends PersistableImpl implements Persistable {
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

	public String getDisplayName() {
		return displayName;
	}

	@Override
	public void loadFromPersistance(Object object) {
		name = (String) object;
		
		Title validTitle = allTitles.get(name);
		displayName = validTitle.displayName;
	}
	
	@Override
	public String stringRepresentation() {
		return name;
	}
}
