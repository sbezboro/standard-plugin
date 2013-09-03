package com.sbezboro.standardplugin.persistence.persistables;

import java.util.Map;

public interface Persistable {
	public void loadFromPersistance(Map<String, Object> map);

	public Object persistableRepresentation();
}
