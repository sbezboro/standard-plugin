package com.sbezboro.standardplugin.net;

import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;

public class RankHttpRequest extends ApiHttpRequest {

	public RankHttpRequest(String username, boolean exact, HttpRequestListener listener) {
		super(StandardPlugin.getPlugin(), "rank_query", HTTPMethod.GET, listener);

		addProperty("username", username);
		if (exact) {
			addProperty("exact", true);
		} else {
			maxAttempts = 1;
		}
	}
}
