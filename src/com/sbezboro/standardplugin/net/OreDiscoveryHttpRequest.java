package com.sbezboro.standardplugin.net;

import com.sbezboro.standardplugin.StandardPlugin;

public class OreDiscoveryHttpRequest extends ApiHttpRequest {
	
	public OreDiscoveryHttpRequest(String uuid, String type, int x, int y, int z) {
		super(StandardPlugin.getPlugin(), "log_ore_discovery", HTTPMethod.POST, null);
		addProperty("uuid", uuid);
		addProperty("type", type);
		addProperty("x", x);
		addProperty("y", y);
		addProperty("z", z);
	}

}
