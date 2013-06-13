package com.sbezboro.standardplugin.persistence;

import java.util.HashMap;

public abstract class PersistableImpl implements Persistable {

	public String stringRepresentation() {
		return null;
	}

	public HashMap<String, Object> mapRepresentation() {
		return null;
	}

	@Override
	public Object persistableRepresentation() {
		String repr = stringRepresentation();
		if (repr == null) {
			return mapRepresentation();
		}
		return stringRepresentation();
	}

}
