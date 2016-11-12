package com.nemustech.adapter.http;

import java.io.IOException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.ssl.SSLInitializationException;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import com.nemustech.common.util.LogUtil;

public class HttpConnector {
	final static PoolingClientConnectionManager CONNMGR;
	final static DefaultHttpClient CLIENT;

	static {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		SchemeSocketFactory plain = PlainSocketFactory.getSocketFactory();
		schemeRegistry.register(new Scheme("http", 80, plain));
		SchemeSocketFactory ssl = null;

		try {
			ssl = SSLSocketFactory.getSystemSocketFactory();
		} catch (SSLInitializationException ex) {
			SSLContext sslcontext;
			try {
				sslcontext = SSLContext.getInstance(SSLSocketFactory.TLS);
				sslcontext.init(null, null, null);
				ssl = new SSLSocketFactory(sslcontext);
			} catch (Exception e) {
				LogUtil.writeLog(e, HttpConnector.class);
			}
		}

		if (ssl != null) {
			schemeRegistry.register(new Scheme("https", 443, ssl));
		}
		CONNMGR = new PoolingClientConnectionManager(schemeRegistry);
		CONNMGR.setDefaultMaxPerRoute(100);
		CONNMGR.setMaxTotal(200);
		CLIENT = new DefaultHttpClient(CONNMGR);
	}

	public static HttpConnector newInstance() {
		return new HttpConnector(CLIENT);
	}

	private final HttpClient httpclient;
	private final BasicHttpContext localContext;
	private final AuthCache authCache;

	private CredentialsProvider credentialsProvider;
	private CookieStore cookieStore;

	HttpConnector(final HttpClient httpclient) {
		this.httpclient = httpclient;
		this.localContext = new BasicHttpContext();
		this.authCache = new BasicAuthCache();
	}

	public void cookieStore(final CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	public void clearCookies() {
		if (this.cookieStore != null) {
			this.cookieStore.clear();
		}
	}

	public HttpResponse execute(final HttpRequestBase httprequest) throws ClientProtocolException, IOException {
		this.localContext.setAttribute(ClientContext.CREDS_PROVIDER, this.credentialsProvider);
		this.localContext.setAttribute(ClientContext.AUTH_CACHE, this.authCache);
		this.localContext.setAttribute(ClientContext.COOKIE_STORE, this.cookieStore);

		httprequest.reset();
		return this.httpclient.execute(httprequest, this.localContext);
	}

	public static void registerScheme(final Scheme scheme) {
		CONNMGR.getSchemeRegistry().register(scheme);
	}

	public static void unregisterScheme(final String name) {
		CONNMGR.getSchemeRegistry().unregister(name);
	}

}
