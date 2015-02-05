package com.sbezboro.http;

import org.json.simple.JSONObject;

import java.util.List;

public class HttpResponse {
	private String stringResponse;
	private JSONObject jsonResponse;

	public HttpResponse(String response) {
		stringResponse = response;
	}

	public HttpResponse(JSONObject object) {
		jsonResponse = object;
	}

	public int getInt(String key) {
		if (jsonResponse.containsKey(key) && jsonResponse.get(key) instanceof Long) {
			return ((Long) jsonResponse.get(key)).intValue();
		}

		return -1;
	}

	public String getString(String key) {
		if (jsonResponse.containsKey(key) && jsonResponse.get(key) instanceof String) {
			return (String) jsonResponse.get(key);
		}

		return null;
	}

	public boolean getBoolean(String key) {
		if (jsonResponse.containsKey(key) && jsonResponse.get(key) instanceof Boolean) {
			return ((Boolean) jsonResponse.get(key)).booleanValue();
		}

		return false;
	}

	public List<Object> getList(String key) {
		if (jsonResponse.containsKey(key) && jsonResponse.get(key) instanceof List) {
			return (List<Object>) jsonResponse.get(key);
		}

		return null;
	}

	public String getStringResponse() {
		return stringResponse;
	}
	
	public JSONObject getJsonResponse() {
		return jsonResponse;
	}

	public boolean isApiSuccess() {
		try {
			return getInt("err") == 0;
		} catch (Exception e) {
			return false;
		}
	}
}
