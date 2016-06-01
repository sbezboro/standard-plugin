package com.sbezboro.standardplugin.model;

import com.sbezboro.standardplugin.persistence.PersistedObject;
import com.sbezboro.standardplugin.persistence.PersistedProperty;
import com.sbezboro.standardplugin.persistence.storages.TitleStorage;

public class Title extends PersistedObject {
	public static final String NEWBIE_STALKER = "newbie-stalker";
	public static final String PVP_LOGGER = "pvp-logger";
	public static final String DRAGON_SLAYER = "dragon-slayer";

	private PersistedProperty<String> displayName;
	private PersistedProperty<Boolean> hidden;
	private PersistedProperty<Boolean> broadcast;

	public Title(TitleStorage storage, String identifier) {
		super(storage, identifier);
	}

	public Title(TitleStorage storage, String identifier, String displayName) {
		super(storage, identifier);
		
		this.displayName.setValue(displayName, false);
		this.hidden.setValue(false, false);
		this.broadcast.setValue(false, false);
	}

	public Title(TitleStorage storage, String identifier, String displayName, boolean hidden, boolean broadcast) {
		super(storage, identifier);
		
		this.displayName.setValue(displayName, false);
		this.hidden.setValue(hidden, false);
		this.broadcast.setValue(broadcast, false);
	}

	@Override
	public void createProperties() {
		displayName = createProperty(String.class, "displayName");
		hidden = createProperty(Boolean.class, "hidden");
		broadcast = createProperty(Boolean.class, "broadcast");
	}

	public String getDisplayName() {
		if (displayName.getValue().length() == 0) {
			return getIdentifier();
		}

		return displayName.getValue();
	}

	public void setDisplayName(String displayName) {
		this.displayName.setValue(displayName);
	}

	public String getDescription() {
		return getIdentifier() + " - " + displayName.getValue();
	}

	public boolean isHidden() {
		return hidden.getValue();
	}

	public void setHidden(boolean hidden) {
		this.hidden.setValue(hidden);
	}

	public boolean isBroadcast() {
		return broadcast.getValue();
	}

	public void setBroadcast(boolean broadcast) {
		this.broadcast.setValue(broadcast);
	}
}
