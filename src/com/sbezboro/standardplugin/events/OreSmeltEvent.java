package com.sbezboro.standardplugin.events;

import com.sbezboro.http.HttpRequestManager;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.net.OreSmeltHttpRequest;

public class OreSmeltEvent {
	private StandardPlayer player;
	private String type;
	private int amount;
	
	public OreSmeltEvent(StandardPlayer player, String type, int amount) {
		this.player = player;
		this.type = type;
		this.amount = (amount > 0) ? amount : 1;
	}
	
	public void log() {
		HttpRequestManager.getInstance().startRequest(
				new OreSmeltHttpRequest(player.getName(), type, amount));
	}
}
