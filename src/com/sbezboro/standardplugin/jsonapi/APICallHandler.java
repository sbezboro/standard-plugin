package com.sbezboro.standardplugin.jsonapi;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

import com.alecgorge.minecraft.jsonapi.api.APIMethodName;
import com.alecgorge.minecraft.jsonapi.api.JSONAPICallHandler;
import com.sbezboro.standardplugin.StandardPlugin;

public abstract class APICallHandler implements JSONAPICallHandler {
	protected StandardPlugin plugin;
	protected String name;

	@SuppressWarnings("serial")
	protected static final Map<String, Integer> API_CALL_RESULTS = new HashMap<String, Integer>() {
		{
			put("OK", 0);
			put("EXCEPTION", 1);
			put("NOT_HANDLED", 2);
			put("BANNED", 3);
		}
	};

	protected Logger logger;

	public APICallHandler(StandardPlugin plugin, String name) {
		this.plugin = plugin;
		this.name = name;

		logger = plugin.getLogger();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object handle(APIMethodName methodName, Object[] args) {
		if (methodName.matches(name)) {
			HashMap<String, Object> payload = null;

			if (args.length == 1) {
				payload = (HashMap<String, Object>) args[0];
			}

			try {
				JSONObject result = handle(payload);
				
				if (result.get("result") == API_CALL_RESULTS.get("NOT_HANDLED")) {
					logger.warning("API call not handled properly! Args: " + StringUtils.join(args));
				}
	
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return buildResult("EXCEPTION", "Exception while handling API call: " + e.toString());
			}
		}

		return null;
	}

	@Override
	public boolean willHandle(APIMethodName methodName) {
		return methodName.matches(name);
	}
	
	@SuppressWarnings("unchecked")
	protected JSONObject buildResult(String result, String message, HashMap<String, Object> data) {
		if (!API_CALL_RESULTS.containsKey(result)) {
			logger.warning("API Result '" + result + "' not found");
		}
		
		int resultCode = API_CALL_RESULTS.get(result);
		
		JSONObject object = new JSONObject();
		object.put("result", resultCode);
		
		if (message != null) {
			object.put("message", message);
		}
		if (data != null) {
			object.put("data", data);
		}
		
		return object;
	}

	protected JSONObject buildResult(String result, String message) {
		return buildResult(result, message, null);
	}
	
	protected JSONObject okResult() {
		return buildResult("OK", null);
	}
	
	protected JSONObject okResult(HashMap<String, Object> data) {
		return buildResult("OK", null, data);
	}
	
	protected JSONObject notHandledResult() {
		return buildResult("NOT_HANDLED", "args not handled properly");
	}

	public abstract JSONObject handle(HashMap<String, Object> payload);
}
