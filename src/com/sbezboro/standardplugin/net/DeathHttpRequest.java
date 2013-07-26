package com.sbezboro.standardplugin.net;

import com.sbezboro.standardplugin.StandardPlugin;

public class DeathHttpRequest extends StandardHttpRequest {

	public DeathHttpRequest(String victim, String type) {
		super(StandardPlugin.getPlugin(), HTTPMethod.POST);
		addProperty("victim", victim);
		addProperty("type", type);
	}

	public DeathHttpRequest(String victim, String type, String killer) {
		super(StandardPlugin.getPlugin(), HTTPMethod.POST);
		addProperty("victim", victim);
		addProperty("killer", killer);
		addProperty("type", type);
	}

	@Override
	public String apiType() {
		return "log_death";
	}
}
