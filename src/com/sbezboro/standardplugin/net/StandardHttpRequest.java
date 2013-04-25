package com.sbezboro.standardplugin.net;

import com.sbezboro.http.HttpRequest;
import com.sbezboro.standardplugin.StandardPlugin;

public abstract class StandardHttpRequest extends HttpRequest {

	public StandardHttpRequest(StandardPlugin plugin, HTTPMethod method) {
		super(plugin, method);
		addProperty("server-id", plugin.getServerId());
		addProperty("secret-key", plugin.getSecretKey());
	}

	@Override
	public String getUrl() {
		return ((StandardPlugin) plugin).getEndpoint() + apiType();
	}

	public abstract String apiType();
}
