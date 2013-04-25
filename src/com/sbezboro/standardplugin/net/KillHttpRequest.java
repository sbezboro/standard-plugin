package com.sbezboro.standardplugin.net;

import com.sbezboro.standardplugin.StandardPlugin;

public class KillHttpRequest extends StandardHttpRequest {
	
	public KillHttpRequest(String killer, String type) {
		super(StandardPlugin.getPlugin(), HTTPMethod.POST);
		addProperty("killer", killer);
		addProperty("type", type);
	}

	@Override
	public String apiType() {
		return "log_kill";
	}
}
