package com.sbezboro.standardplugin.net;

import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class UsernamesHttpRequest extends ApiHttpRequest {

	public UsernamesHttpRequest(StandardPlayer player, HttpRequestListener listener) {
		super(StandardPlugin.getPlugin(), "past_usernames", HTTPMethod.GET, listener);

		addProperty("uuid", player.getUuidString());
	}

}
