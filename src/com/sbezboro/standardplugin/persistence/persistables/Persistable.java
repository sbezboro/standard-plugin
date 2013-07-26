package com.sbezboro.standardplugin.persistence.persistables;

import org.bukkit.configuration.ConfigurationSection;


public interface Persistable {
	public String getIdentifier();
	public void loadFromPersistance(ConfigurationSection section);
	public Object persistableRepresentation();
}
