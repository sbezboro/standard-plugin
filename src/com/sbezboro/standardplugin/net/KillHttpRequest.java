package com.sbezboro.standardplugin.net;

import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;

public class KillHttpRequest extends ApiHttpRequest {

	public KillHttpRequest(String killer, String type, HttpRequestListener listener) {
		super(StandardPlugin.getPlugin(), "log_kill", HTTPMethod.POST, listener);
		addProperty("killer", killer);
		addProperty("type", type);
	}
}
