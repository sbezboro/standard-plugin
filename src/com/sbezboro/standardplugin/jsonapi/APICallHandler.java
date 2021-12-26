package com.sbezboro.standardplugin.jsonapi;

import com.alecgorge.minecraft.jsonapi.api.APIMethodName;
import com.alecgorge.minecraft.jsonapi.api.JSONAPICallHandler;
import com.sbezboro.standardplugin.StandardPlugin;
import org.apache.commons.lang.StringUtils;
import org.json.simpleForBukkit.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public abstract class APICallHandler implements JSONAPICallHandler {
	protected StandardPlugin plugin;
	protected String name;

	@SuppressWarnings("serial")
	protected static final Map<String, Integer> API_CALL_RESULTS = new HashMap<String, Integer>() {{
		put("ok", 0);
		put("exception", 1);
		put("not_handled", 2);
		put("banned", 3);
		put("muted", 4);
		put("never_joined", 5);
	}};

	protected Logger logger;

	public APICallHandler(StandardPlugin plugin, String name) {
		this.plugin = plugin;
		this.name = name;

		logger = plugin.getLogger();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object handle(APIMethodName methodName, final Object[] args) {
		if (methodName.matches(name)) {
			Callable<JSONObject> callable = new Callable<JSONObject>() {

				@Override
				public JSONObject call() throws Exception {
					HashMap<String, Object> payload = null;

					if (args.length == 1) {
						payload = (HashMap<String, Object>) args[0];
					}

					try {
						JSONObject result = handle(payload);

						if (result.get("result") == API_CALL_RESULTS.get("not_handled")) {
							logger.warning("API call not handled properly! Args: " + StringUtils.join(args));
						}

						return result;
					} catch (Exception e) {
						e.printStackTrace();
						return buildResult("exception", "Exception while handling API call: " + e.toString());
					}
				}
			};

			if (isAsync()) {
				try {
					return callable.call();
				} catch (Exception e) {
					return null;
				}
			} else {
				Future<JSONObject> future = plugin.getServer().getScheduler().callSyncMethod(plugin, callable);

				try {
					return future.get();
				} catch (Exception e) {
					return null;
				}
			}
		}

		return null;
	}

	@Override
	public boolean willHandle(APIMethodName methodName) {
		return methodName.matches(name);
	}

	public boolean isAsync() {
		return false;
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

	protected JSONObject buildResult(String result) {
		return buildResult(result, null, null);
	}
	
	protected JSONObject okResult() {
		return buildResult("ok", null);
	}
	
	protected JSONObject okResult(HashMap<String, Object> data) {
		return buildResult("ok", null, data);
	}
	
	protected JSONObject notHandledResult() {
		return buildResult("not_handled", "args not handled properly");
	}

	protected JSONObject notHandledResult(String message) {
		return buildResult("not_handled", message);
	}

	public abstract JSONObject handle(HashMap<String, Object> payload);
}
