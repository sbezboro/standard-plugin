package com.sbezboro.standardplugin.net;

import com.sbezboro.http.HttpRequest;
import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;

public abstract class StandardHttpRequest extends HttpRequest {

	public StandardHttpRequest(StandardPlugin plugin, HTTPMethod method, HttpRequestListener listener) {
		super(plugin, method, listener);
		addProperty("server-id", plugin.getServerId());
		addProperty("secret-key", plugin.getSecretKey());
	}

	@Override
	public String getUrl() {
		return ((StandardPlugin) plugin).getEndpoint() + apiType();
	}

	public abstract String apiType();
}
