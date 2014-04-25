package com.sbezboro.standardplugin.net;

import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class RankHttpRequest extends ApiHttpRequest {

	public RankHttpRequest(StandardPlayer player, HttpRequestListener listener) {
		super(StandardPlugin.getPlugin(), "rank_query", HTTPMethod.GET, listener);

		addProperty("uuid", player.getUuidString());
	}

	public RankHttpRequest(String username, HttpRequestListener listener) {
		super(StandardPlugin.getPlugin(), "rank_query", HTTPMethod.GET, listener);

		addProperty("username", username);
		maxAttempts = 1;
	}
}
