package org.oh.common.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

import eu.vahlas.json.schema.JSONSchema;
import eu.vahlas.json.schema.JSONSchemaProvider;
import eu.vahlas.json.schema.impl.JacksonSchemaProvider;

/**
 * Jackson Json Processor기반으로 Json Schema를 검증하는 서비스
 * 
 * 
 * @version 1.0.0
 * @see <a href="http://gitorious.org/json-schema-validation-in-java">JSON Schema Validation in Java</a>
 * 
 */
public class SimpleJsonSchemaValidator implements JsonValidator {
	protected static Log log = LogFactory.getLog(SimpleJsonSchemaValidator.class);
	List<String> errors;

	/**
	 * 생성자
	 */
	public SimpleJsonSchemaValidator() {
		errors = new ArrayList<String>();
	}

	public boolean validate(String text, String schema) {
		log.debug("Start::validate()");
		log.debug("  > text: " + text);
		log.debug("  > schema: " + schema);

		List<String> errs = null;
		boolean value = false;
		errors.clear();

		if (text == null || text.length() == 0) {
			log.debug("Validation error : Data is empty.");
			this.errors.add("Data is empty.");
		}

		else if (schema == null || schema.length() == 0) {
			log.debug("Validation error : Schema is empty.");
			this.errors.add("Schema is empty.");
		}

		else {
			ObjectMapper mapper = new ObjectMapper();
			JSONSchemaProvider schemaProvider = new JacksonSchemaProvider(mapper);
			JSONSchema schemaObj = schemaProvider.getSchema(schema);
			errs = schemaObj.validate(text);

			if (!errs.isEmpty()) {
				for (String s : errs) {
					log.debug("Validation error: " + s);
					this.errors.add(s);
				}
			}

			else {
				value = true;
			}
		}

		log.debug("  > RV(value): " + value);
		log.debug("End::validate");

		return value;
	}

	public List<String> errors() {
		return this.errors;
	}
}
