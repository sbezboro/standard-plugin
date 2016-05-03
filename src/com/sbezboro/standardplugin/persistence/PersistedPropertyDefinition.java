package com.sbezboro.standardplugin.persistence;

public class PersistedPropertyDefinition {
	private Class cls;
	private String identifier;

	public PersistedPropertyDefinition(Class cls, String identifier) {
		this.cls = cls;
		this.identifier = identifier;
	}

	public Class getCls() {
		return cls;
	}

	public String getIdentifier() {
		return identifier;
	}
}
