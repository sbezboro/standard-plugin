package com.sbezboro.standardplugin.net;

import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;

public class DeathHttpRequest extends ApiHttpRequest {

	public DeathHttpRequest(String victim, String type) {
		super(StandardPlugin.getPlugin(), "log_death", HTTPMethod.POST, null);
		addProperty("victim", victim);
		addProperty("type", type);
	}

	public DeathHttpRequest(String victim, String type, String killer) {
		super(StandardPlugin.getPlugin(), "log_death", HTTPMethod.POST, null);
		addProperty("victim", victim);
		addProperty("killer", killer);
		addProperty("type", type);
	}
}
