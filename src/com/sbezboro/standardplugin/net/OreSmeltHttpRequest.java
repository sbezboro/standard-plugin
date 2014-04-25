package com.sbezboro.standardplugin.net;

import com.sbezboro.standardplugin.StandardPlugin;

public class OreSmeltHttpRequest extends ApiHttpRequest {
	
	public OreSmeltHttpRequest(String uuid, String type, int amount) {
		super(StandardPlugin.getPlugin(), "log_ore_smelt", HTTPMethod.POST, null);
		addProperty("uuid", uuid);
		addProperty("type", type);
		addProperty("amount", amount);
	}

}
