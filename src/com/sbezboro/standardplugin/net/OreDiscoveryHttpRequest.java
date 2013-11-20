package com.sbezboro.standardplugin.net;

import com.sbezboro.standardplugin.StandardPlugin;

public class OreDiscoveryHttpRequest extends ApiHttpRequest {
	
	public OreDiscoveryHttpRequest(String username, String type, int x, int y, int z) {
		super(StandardPlugin.getPlugin(), "log_ore_discovery", HTTPMethod.POST, null);
		addProperty("username", username);
		addProperty("type", type);
		addProperty("x", x);
		addProperty("y", y);
		addProperty("z", z);
	}

}
