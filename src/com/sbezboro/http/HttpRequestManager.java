package com.sbezboro.http;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpRequestManager {
	private static HttpRequestManager instance;
	
	private ThreadPoolExecutor executor;
	private ScheduledExecutorService service;
	
	private HttpRequestManager() {
		executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		service = Executors.newSingleThreadScheduledExecutor();
	}
	
	public static HttpRequestManager getInstance() {
		if (instance == null) {
			instance = new HttpRequestManager();
		}
		
		return instance;
	}
	
	public void startRequest(HttpRequest request) {
		executor.execute(request);
	}
	
	public void scheduleRetry(final HttpRequest request) {
		int delay = request.getAttempts();
		
		service.schedule(new Runnable() {
			
			@Override
			public void run() {
				executor.execute(request);
			}
		}, delay, TimeUnit.SECONDS);
	}
}
