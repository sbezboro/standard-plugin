package com.sbezboro.standardplugin.net;

import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;

public class DeathHttpRequest extends StandardHttpRequest {

	public DeathHttpRequest(String victim, String type, HttpRequestListener listener) {
		super(StandardPlugin.getPlugin(), HTTPMethod.POST, listener);
		addProperty("victim", victim);
		addProperty("type", type);
	}

	public DeathHttpRequest(String victim, String type, String killer, HttpRequestListener listener) {
		super(StandardPlugin.getPlugin(), HTTPMethod.POST, listener);
		addProperty("victim", victim);
		addProperty("killer", killer);
		addProperty("type", type);
	}

	@Override
	public String apiType() {
		return "log_death";
	}
}
