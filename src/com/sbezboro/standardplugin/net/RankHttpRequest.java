package com.sbezboro.standardplugin.net;

import com.sbezboro.standardplugin.StandardPlugin;

public class RankHttpRequest extends StandardHttpRequest {

	public RankHttpRequest(String username, boolean exact) {
		super(StandardPlugin.getPlugin(), HTTPMethod.GET);

		addProperty("username", username);
		if (exact) {
			addProperty("exact", true);
		}
	}

	@Override
	public String apiType() {
		return "rank_query";
	}
}
