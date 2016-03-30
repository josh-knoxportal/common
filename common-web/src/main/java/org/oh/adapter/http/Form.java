package org.oh.adapter.http;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.DirectFieldAccessor;

public class Form {
	private final List<NameValuePair> params;

	public static Form form() {
		return new Form();
	}

	public Form() {
		this.params = new ArrayList<NameValuePair>();
	}

	public Form add(final String name, final String value) {
		this.params.add(new BasicNameValuePair(name, value));
		return this;
	}

	public List<NameValuePair> build() {
		return new ArrayList<NameValuePair>(this.params);
	}

	public <T> List<NameValuePair> build(T paramObj) {

		Class<?> clazz = paramObj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(paramObj);
		for (Field field : fields) {
			Object value = fieldAccessor.getPropertyValue(field.getName());
			add(field.getName(), String.class.cast(value));
		}

		return build();
	}
}
