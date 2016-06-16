/**
 * 
 */
package org.oh.adapter.http;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.oh.adapter.HTTPAdapter;
import org.oh.adapter.aspect.AuditRequired;
import org.oh.adapter.exception.AdapterException;
import org.springframework.beans.DirectFieldAccessor;

public class HTTPAdapterImpl implements HTTPAdapter {

	public static final String DATE_FORMAT = "EEE, dd MM yyyy HH:mm:ss zzz";

	public static final Locale DATE_LOCALE = Locale.KOREA;
	public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT");

	private String domain;

	private HttpRequestBase request;

	// private HttpParams localParams;

	public HTTPAdapterImpl() {
	}

	public HttpClient getHttpClient() {
		return HttpConnector.CLIENT;
	}

	public HttpResponse doGet(String uri) throws AdapterException {
		this.request = new HttpGet(uri);
		try {
			HttpResponse response = HttpConnector.CLIENT.execute(request);
			return response;
		} catch (Exception e) {
			throw new AdapterException(e);
		}

	}

	public HttpResponse doGet(URI uri) throws AdapterException {
		this.request = new HttpGet(uri);
		try {
			HttpResponse response = HttpConnector.CLIENT.execute(request);
			return response;
		} catch (Exception e) {
			throw new AdapterException(e);
		}
	}

	@AuditRequired
	public <T> T doGet(String uri, Object paramObj, IHttpMapper<T> mapper) throws AdapterException {
		this.request = new HttpGet(uri);
		// this.localParams = request.getParams();
		HttpResponse response = null;
		try {
			if (paramObj != null) {
				request = mapper.setRequestParam(request, paramObj);
			}
			response = HttpConnector.CLIENT.execute(request);
			T model = mapper.handleResponse(response);
			return model;
		} catch (AdapterException e) {
			throw e;
		} catch (Exception e) {
			throw new AdapterException(e);
		} finally {
			dispose(response);
		}

	}

	public <T> T doGet(URI uri, Object paramObj, IHttpMapper<T> mapper) throws AdapterException {
		this.request = new HttpGet(uri);
		// this.localParams = request.getParams();
		HttpResponse response = null;
		try {
			if (paramObj != null) {
				request = mapper.setRequestParam(request, paramObj);
			}
			response = HttpConnector.CLIENT.execute(request);
			T model = mapper.handleResponse(response);
			return model;
		} catch (AdapterException e) {
			throw e;
		} catch (Exception e) {
			throw new AdapterException(e);
		} finally {
			dispose(response);
		}
	}

	@AuditRequired
	public <T> T doPost(String uri, Object paramObj, IHttpMapper<T> mapper) throws AdapterException {

		this.request = new HttpPost(uri);
		// this.localParams = request.getParams();
		HttpResponse response = null;
		try {
			mapper.setRequestParam(request, paramObj);
			response = HttpConnector.newInstance().execute(request);
			return mapper.handleResponse(response);
		} catch (AdapterException e) {
			throw e;
		} catch (Exception e) {
			throw new AdapterException(e);
		} finally {
			dispose(response);
		}
	}

	private void dispose(HttpResponse response) {
		try {
			EntityUtils.consume(response.getEntity());
		} catch (Exception e) {
		}
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public URI buildURI(String url, Map<String, String> paramMap) throws AdapterException {

		try {
			URIBuilder builder = new URIBuilder(url);
			Iterator<String> iter = paramMap.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				String value = paramMap.get(key);
				builder.addParameter(key, value);
			}
			return builder.build();
		} catch (URISyntaxException e) {
			throw new AdapterException(e);
		}
	}

	public <T> URI buildURI(String url, T paramObj) throws AdapterException {

		try {
			URIBuilder builder = new URIBuilder(url);
			Class<?> clazz = paramObj.getClass();
			Field[] fields = clazz.getFields();
			DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(paramObj);
			for (Field field : fields) {
				Object value = fieldAccessor.getPropertyValue(field.getName());
				builder.addParameter(field.getName(), String.valueOf(value));
			}
			return builder.build();
		} catch (URISyntaxException e) {
			throw new AdapterException(e);
		}
	}

}