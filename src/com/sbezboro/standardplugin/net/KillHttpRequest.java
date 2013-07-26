package com.sbezboro.standardplugin.net;

import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;

public class KillHttpRequest extends StandardHttpRequest {

	public KillHttpRequest(String killer, String type, HttpRequestListener listener) {
		super(StandardPlugin.getPlugin(), HTTPMethod.POST, listener);
		addProperty("killer", killer);
		addProperty("type", type);
	}

	@Override
	public String apiType() {
		return "log_kill";
	}
}
