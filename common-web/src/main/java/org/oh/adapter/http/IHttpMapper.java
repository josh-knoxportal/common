package org.oh.adapter.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.oh.adapter.exception.AdapterException;

public interface IHttpMapper<T> {

	public HttpRequestBase setRequestParam(HttpRequestBase request, Object paramObj) throws AdapterException;

	public T handleResponse(HttpResponse response) throws AdapterException;
}
