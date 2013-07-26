package com.sbezboro.http.listeners;

import com.sbezboro.http.HttpResponse;

public interface HttpRequestListener {
	public void requestSuccess(HttpResponse response);
	public void requestFailure(HttpResponse response);
}
