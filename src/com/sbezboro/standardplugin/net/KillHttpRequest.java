package com.sbezboro.standardplugin.net;

import com.sbezboro.standardplugin.StandardPlugin;

public class KillHttpRequest extends ApiHttpRequest {

	public KillHttpRequest(String killer, String type) {
		super(StandardPlugin.getPlugin(), "log_kill", HTTPMethod.POST, null);
		addProperty("killer", killer);
		addProperty("type", type);
	}
}
