package com.sbezboro.standardplugin.persistence.persistables;

import java.util.List;
import java.util.Map;

public abstract class PersistableImpl implements Persistable {
	public String stringRepresentation() {
		return null;
	}

	public List<Object> listRepresentation() {
		return null;
	}

	public Map<String, Object> mapRepresentation() {
		return null;
	}

	@Override
	public Object persistableRepresentation() {
		String stringRepr = stringRepresentation();
		List<Object> listRepr = listRepresentation();
		Map<String, Object> mapRepr = mapRepresentation();

		if (stringRepr != null) {
			return stringRepresentation();
		} else if (listRepr != null) {
			return listRepr;
		}
		return mapRepr;
	}

}
