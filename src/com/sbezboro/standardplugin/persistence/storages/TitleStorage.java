package com.sbezboro.standardplugin.persistence.storages;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.Title;

public class TitleStorage extends SingleFileStorage<Title> {

	public TitleStorage(StandardPlugin plugin) {
		super(plugin, "titles");
		
	}

	@Override
	public Title createObject(String identifier) {
		return new Title(this, identifier);
	}

	@Override
	public void onPostLoad(Set<String> keys) {
		Map<String, Title> defaults = new HashMap<String, Title>();
		defaults.put(Title.NEWBIE_STALKER, new Title(this, Title.NEWBIE_STALKER, "Newbie Stalker"));
		defaults.put(Title.TOP10_VETERAN, new Title(this, Title.TOP10_VETERAN, "Top 10 Veteran"));
		defaults.put(Title.TOP40_VETERAN, new Title(this, Title.TOP40_VETERAN, "Top 40 Veteran"));
		defaults.put(Title.VETERAN, new Title(this, Title.VETERAN, "Veteran"));
		defaults.put(Title.PVP_LOGGER, new Title(this, Title.PVP_LOGGER, "PVP Logger"));
		
		for (Title title : defaults.values()) {
			if (!keys.contains(title.getIdentifier())) {
				addObject(title);
			}
		}
	}
	
	public void createTitle(String identifier, String displayName) {
		Title title = new Title(this, identifier, displayName);
		addObject(title);
	}
	
	public void removeTitle(Title title) {
		removeObject(title);
	}

	public Collection<Title> getTitles() {
		return idToObject.values();
	}

	public Title getTitle(String name) {
		return getObject(name);
	}

}
