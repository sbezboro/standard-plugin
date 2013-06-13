package com.sbezboro.standardplugin.persistence;


public interface Persistable {
	public void loadFromPersistance(Object object);
	public Object persistableRepresentation();
}
