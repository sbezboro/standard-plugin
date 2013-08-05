package com.sbezboro.standardplugin.net;

import com.sbezboro.http.HttpRequest;
import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;

public class WebchatHttpRequest extends HttpRequest {

	public WebchatHttpRequest(HttpRequestListener listener) {
		super(StandardPlugin.getPlugin(), HTTPMethod.GET, listener);
	}

	@Override
	public String getUrl() {
		return ((StandardPlugin) plugin).getRTSAddress() + "/users";
	}
}
