package com.sbezboro.standardplugin.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.Title;

public class TitleStorage extends ConfigStorage {
	private Map<String, Title> titles;
	
	@SuppressWarnings("serial")
	private static final Map<String, Title> defaults = new HashMap<String, Title>() { { 
			put(Title.NEWBIE_STALKER, new Title(Title.NEWBIE_STALKER, "Newbie Stalker"));
			put(Title.TOP10_VETERAN, new Title(Title.TOP10_VETERAN, "Top 10 Veteran"));
			put(Title.TOP40_VETERAN, new Title(Title.TOP40_VETERAN, "Top 40 Veteran"));
			put(Title.VETERAN, new Title(Title.VETERAN, "Veteran"));
		}
	};

	public TitleStorage(StandardPlugin plugin) {
		super(plugin, "titles");
	}

	@Override
	public void loadData(Set<String> keys) {
		titles = new HashMap<String, Title>();
		
		for (String key : keys) {
			ConfigurationSection section = config.getConfigurationSection(key);
			String displayName = section.getString("displayName");
			
			Title title = new Title(key, displayName);
			titles.put(key, title);
		}
		
		for (Title title : defaults.values()) {
			if (!keys.contains(title.getName())) {
				saveTitle(title);
			}
		}
	}
	
	public void saveTitle(Title title) {
		titles.put(title.getName(), title);
		
		ConfigurationSection section = config.createSection(title.getName());
		section.set("displayName", title.getDisplayName());
		
		save();
	}
	
	public Collection<Title> getTitles() {
		return titles.values();
	}
	
	public Title getTitle(String name) {
		return titles.get(name);
	}

}
