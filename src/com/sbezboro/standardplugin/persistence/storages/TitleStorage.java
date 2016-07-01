package com.sbezboro.standardplugin.persistence.storages;

import java.util.*;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
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
		defaults.put(Title.PVP_LOGGER, new Title(this, Title.PVP_LOGGER, "PVP Logger"));
		defaults.put(Title.DRAGON_SLAYER, new Title(this, Title.DRAGON_SLAYER, "Dragon Slayer", false, true));
		
		for (Title title : defaults.values()) {
			if (!keys.contains(title.getIdentifier())) {
				addObject(title);
			}
		}
	}

	public Title createTitle(String identifier) {
		Title title = new Title(this, identifier, "");
		addObject(title);

		return title;
	}
	
	public Title createTitle(String identifier, String displayName) {
		Title title = new Title(this, identifier, displayName);
		addObject(title);

		return title;
	}

	public void addTitles(ArrayList<HashMap<String, Object>> titles, StandardPlayer player) {
		for (HashMap<String, Object> titleInfo : titles) {
			String name = (String) titleInfo.get("name");
			boolean broadcast = (Boolean) titleInfo.get("broadcast");

			Title title = getTitle(name);
			if (title == null) {
				title = createTitle(name);
			}

			title.setBroadcast(broadcast);

			player.addTitle(title);
		}
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
