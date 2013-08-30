package com.sbezboro.standardplugin.persistence.persistables;

import java.util.Map;

public interface Persistable {
	public String getIdentifier();

	public void loadFromPersistance(Map<String, Object> map);

	public Object persistableRepresentation();
}
