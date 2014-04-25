package com.sbezboro.standardplugin.net;

import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;

public class JoinHttpRequest extends ApiHttpRequest {
	
	public JoinHttpRequest(String uuid, HttpRequestListener listener) {
		super(StandardPlugin.getPlugin(), "join_server", HTTPMethod.POST, listener);
		
		addProperty("uuid", uuid);
	}

}
