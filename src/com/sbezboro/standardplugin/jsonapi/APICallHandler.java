package com.sbezboro.standardplugin.jsonapi;

import java.util.HashMap;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.alecgorge.minecraft.jsonapi.api.APIMethodName;
import com.alecgorge.minecraft.jsonapi.api.JSONAPICallHandler;
import com.sbezboro.standardplugin.StandardPlugin;

public abstract class APICallHandler implements JSONAPICallHandler {
	protected StandardPlugin plugin;
	protected String name;
	
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

			Object result = handle(payload);
			if (result == Boolean.FALSE) {
				logger.warning("API Call \"" + name + "\" not handled properly with args \"" + StringUtils.join(args) + "\"");
			}
			
			return result;
		}
		
		return null;
	}
		
	@Override
	public boolean willHandle(APIMethodName methodName) {
		return methodName.matches(name);
	}
	
	public abstract Object handle(HashMap<String, Object> payload);
}
