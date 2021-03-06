package com.sbezboro.standardplugin.net;

import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;

import java.util.Map;

public class AuditLogHttpRequest extends ApiHttpRequest {

	public AuditLogHttpRequest(String type, String uuid, Map<String, Object> data, HttpRequestListener listener) {
		super(StandardPlugin.getPlugin(), "audit_log", HTTPMethod.POST, listener);
		setJson(true);

		addProperty("type", type);
		addProperty("uuid", uuid);

		try {
			addProperty("data", data);
		} catch (NotJsonException e) {
			e.printStackTrace();
		}
	}
}
