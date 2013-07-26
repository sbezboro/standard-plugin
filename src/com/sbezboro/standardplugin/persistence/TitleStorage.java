package com.sbezboro.standardplugin.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.Title;

public class TitleStorage extends ConfigStorage<Title> {
	@SuppressWarnings("serial")
	private static final Map<String, Title> defaults = new HashMap<String, Title>() {
		{
			put(Title.NEWBIE_STALKER, new Title(Title.NEWBIE_STALKER, "Newbie Stalker"));
			put(Title.TOP10_VETERAN, new Title(Title.TOP10_VETERAN, "Top 10 Veteran"));
			put(Title.TOP40_VETERAN, new Title(Title.TOP40_VETERAN, "Top 40 Veteran"));
			put(Title.VETERAN, new Title(Title.VETERAN, "Veteran"));
		}
	};

	public TitleStorage(StandardPlugin plugin) {
		super(plugin, Title.class, "titles");
	}

	@Override
	public void onPostLoad(Set<String> keys) {
		for (Title title : defaults.values()) {
			if (!keys.contains(title.getName())) {
				saveTitle(title);
			}
		}
	}

	public void saveTitle(Title title) {
		addObject(title);

		save();
	}

	public Collection<Title> getTitles() {
		return idToObject.values();
	}

	public Title getTitle(String name) {
		return idToObject.get(name);
	}

}
