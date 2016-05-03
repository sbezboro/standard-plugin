package com.sbezboro.standardplugin.net;

import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;

public class LeaveHttpRequest extends ApiHttpRequest {
	
	public LeaveHttpRequest(String uuid, HttpRequestListener listener) {
		super(StandardPlugin.getPlugin(), "leave_server", HTTPMethod.POST, listener);
		
		addProperty("uuid", uuid);
	}

}
