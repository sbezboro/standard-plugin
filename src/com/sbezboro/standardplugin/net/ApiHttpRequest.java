package com.sbezboro.standardplugin.net;

import com.sbezboro.http.HttpRequest;
import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;

public abstract class ApiHttpRequest extends HttpRequest {
	private String apiType;

	public ApiHttpRequest(StandardPlugin plugin, String apiType, HTTPMethod method, HttpRequestListener listener) {
		super(plugin, method, listener);
		
		this.apiType = apiType;
		
		setAuth(String.valueOf(plugin.getServerId()), plugin.getSecretKey());
	}

	@Override
	public String getUrl() {
		return ((StandardPlugin) plugin).getEndpoint() + apiType;
	}
}
