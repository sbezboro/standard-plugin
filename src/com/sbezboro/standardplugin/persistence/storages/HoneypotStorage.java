package com.sbezboro.standardplugin.persistence.storages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.Honeypot;

public class HoneypotStorage extends ConfigStorage {
	
	private List<Honeypot> honeypots;

	public HoneypotStorage(StandardPlugin plugin) {
		super(plugin, "honeypots");
	}

	@Override
	@SuppressWarnings("unchecked")
	public void load() {
		this.honeypots = new ArrayList<Honeypot>();

		List<?> honeypots = config.getList("honeypots");
		if (honeypots != null) {
			for (Object section : honeypots) {
				Honeypot honeypot = new Honeypot();
				honeypot.loadFromPersistance((Map<String, Object>) section);
				this.honeypots.add(honeypot);
			}
		}
	}
	
	public void addHoneypot(Honeypot honeypot) {
		this.honeypots.add(honeypot);
		
		saveHoneypots();
	}
	
	public void removeHoneypot(Honeypot honeypot) {
		this.honeypots.remove(honeypot);
		
		saveHoneypots();
	}
	
	public void saveHoneypots() {
		ArrayList<Map<String, Object>> newList = new ArrayList<Map<String,Object>>();
		
		for (Honeypot honeypot : honeypots) {
			newList.add(honeypot.mapRepresentation());
		}
		
		config.set("honeypots", newList);
		save();
	}
	
	public List<Honeypot> getHoneypots() {
		return honeypots;
	}

}
