package com.sbezboro.standardplugin.persistence.persistables;


public interface Persistable {
	public void loadFromPersistance(Object object);
	public Object persistableRepresentation();
}
