package com.sbezboro.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.sbezboro.http.listeners.HttpRequestListener;

public abstract class HttpRequest implements Runnable {
	private static final int DEFAULT_MAX_ATTEMPTS = 5;
	
	protected enum HTTPMethod {
		GET, POST
	};

	protected final Plugin plugin;
	private HashMap<String, String> properties;
	private HTTPMethod method;
	private HttpRequestListener listener;
	
	protected int maxAttempts;
	private int attemptNum;

	public HttpRequest(Plugin plugin, HTTPMethod method, HttpRequestListener listener) {
		this.plugin = plugin;
		this.method = method;
		this.listener = listener;
		
		this.maxAttempts = DEFAULT_MAX_ATTEMPTS;
		this.attemptNum = 1;
		
		properties = new HashMap<String, String>();
	}
	
	public int getAttempts() {
		return attemptNum;
	}

	public void addProperty(String key, String value) {
		properties.put(key, value);
	}

	public void addProperty(String key, int value) {
		properties.put(key, String.valueOf(value));
	}

	public void addProperty(String key, boolean value) {
		properties.put(key, value ? "1" : "0");
	}

	private String getPropertyData() {
		String data = "";

		int i = 0;
		for (String key : properties.keySet()) {
			String value = properties.get(key);

			try {
				data += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
				if (i < properties.keySet().size() - 1) {
					data += "&";
				}
			} catch (UnsupportedEncodingException e) {
			}

			i++;
		}

		return data;
	}
	
	@Override
	public void run() {
		try {
			String urlString = getUrl();
			if (method == HTTPMethod.GET && !properties.isEmpty()) {
				urlString += "?" + getPropertyData();
			}

			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();

			if (method == HTTPMethod.POST) {
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(getPropertyData());
				wr.flush();
				wr.close();
			}

			String response = "";
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				response += line;
			}
			rd.close();

			HttpResponse httpResponse;
			JSONObject jsonResponse = null;
			try {
				jsonResponse = (JSONObject) JSONValue.parse(response);
			} catch (Exception e) {
			}

			if (jsonResponse == null) {
				httpResponse = new HttpResponse(response);
			} else {
				httpResponse = new HttpResponse(jsonResponse);
			}

			if (listener != null) {
				final HttpResponse finalResponse = httpResponse;
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

					@Override
					public void run() {
						listener.requestSuccess(finalResponse);
					}
				});
			}
		} catch (final IOException e) {
			plugin.getLogger().severe("HTTPRequest '" + getUrl() + "' failure: " + e.toString() + ", attempt " + attemptNum);
			
			if (attemptNum < maxAttempts) {
				attemptNum++;
				HttpRequestManager.getInstance().scheduleRetry(HttpRequest.this);
			} else if (listener != null) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

					@Override
					public void run() {
						listener.requestFailure(new HttpResponse(e.toString()));
					}
				});
			}
		}
	}

	public abstract String getUrl();
}
