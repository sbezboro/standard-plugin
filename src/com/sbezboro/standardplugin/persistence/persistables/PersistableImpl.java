package com.sbezboro.standardplugin.persistence.persistables;

import java.util.HashMap;
import java.util.List;

public abstract class PersistableImpl implements Persistable {
	public String stringRepresentation() {
		return null;
	}

	public List<Object> listRepresentation() {
		return null;
	}

	public HashMap<String, Object> mapRepresentation() {
		return null;
	}

	@Override
	public Object persistableRepresentation() {
		String stringRepr = stringRepresentation();
		List<Object> listRepr = listRepresentation();
		HashMap<String, Object> mapRepr = mapRepresentation();
		
		if (stringRepr != null) {
			return stringRepresentation();
		} else if (listRepr != null) {
			return listRepr;
		}
		return mapRepr;
	}

}
