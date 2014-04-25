package com.sbezboro.standardplugin.net;

import com.sbezboro.standardplugin.StandardPlugin;

public class KillHttpRequest extends ApiHttpRequest {

	public KillHttpRequest(String killerUuid, String type) {
		super(StandardPlugin.getPlugin(), "log_kill", HTTPMethod.POST, null);
		addProperty("killer_uuid", killerUuid);
		addProperty("type", type);
	}
}
