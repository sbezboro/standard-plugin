package com.sbezboro.standardplugin.persistence;

public interface PersistedBase {
	public String getName();
	public void load();
	public Object getValue();
}
