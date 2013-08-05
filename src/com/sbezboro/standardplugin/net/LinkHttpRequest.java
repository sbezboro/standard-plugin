package com.sbezboro.standardplugin.net;

import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;

public class LinkHttpRequest extends ApiHttpRequest {

	public LinkHttpRequest(String username, String password, HttpRequestListener listener) {
		super(StandardPlugin.getPlugin(), "link", HTTPMethod.POST, listener);

		addProperty("username", username);
		addProperty("password", password);
	}
}
