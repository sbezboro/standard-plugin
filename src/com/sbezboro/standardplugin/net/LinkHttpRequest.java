package com.sbezboro.standardplugin.net;

import com.sbezboro.standardplugin.StandardPlugin;

public class LinkHttpRequest extends StandardHttpRequest {

	public LinkHttpRequest(String username, String password) {
		super(StandardPlugin.getPlugin(), HTTPMethod.POST);

		addProperty("username", username);
		addProperty("password", password);
	}

	@Override
	public String apiType() {
		return "link";
	}
}
