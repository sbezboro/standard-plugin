package com.sbezboro.standardplugin.jsonapi;

import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.alecgorge.minecraft.jsonapi.api.APIMethodName;
import com.alecgorge.minecraft.jsonapi.api.JSONAPICallHandler;
import com.sbezboro.standardplugin.StandardPlugin;

public abstract class APICallHandler implements JSONAPICallHandler {
	protected StandardPlugin plugin;
	protected Logger logger;
	
	public APICallHandler(StandardPlugin plugin) {
		this.plugin = plugin;
		logger = plugin.getLogger();
	}
	
	@Override
	public Object handle(APIMethodName methodName, Object[] args) {
		if (methodName.matches(getName())) {
			Object result = handle(args);
			if (result == Boolean.FALSE) {
				logger.warning("API Call \"" + getName() + "\" not handled properly with args \"" + StringUtils.join(args) + "\"");
			}
			
			return result;
		}
		
		return null;
	}
		
	@Override
	public boolean willHandle(APIMethodName methodName) {
		return methodName.matches(getName());
	}
	
	public abstract Object handle(Object[] args);
	public abstract String getName();
}
