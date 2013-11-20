package com.sbezboro.standardplugin.net;

import com.sbezboro.standardplugin.StandardPlugin;

public class OreSmeltHttpRequest extends ApiHttpRequest {
	
	public OreSmeltHttpRequest(String username, String type, int amount) {
		super(StandardPlugin.getPlugin(), "log_ore_smelt", HTTPMethod.POST, null);
		addProperty("username", username);
		addProperty("type", type);
		addProperty("amount", amount);
	}

}
