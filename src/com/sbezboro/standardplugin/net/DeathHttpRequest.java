package com.sbezboro.standardplugin.net;

import com.sbezboro.standardplugin.StandardPlugin;

public class DeathHttpRequest extends ApiHttpRequest {

	public DeathHttpRequest(String victimUuid, String type) {
		super(StandardPlugin.getPlugin(), "log_death", HTTPMethod.POST, null);
		addProperty("victim_uuid", victimUuid);
		addProperty("type", type);
	}

	public DeathHttpRequest(String victimUuid, String type, String killerUuid) {
		super(StandardPlugin.getPlugin(), "log_death", HTTPMethod.POST, null);
		addProperty("victim_uuid", victimUuid);
		addProperty("killer_uuid", killerUuid);
		addProperty("type", type);
	}
}
