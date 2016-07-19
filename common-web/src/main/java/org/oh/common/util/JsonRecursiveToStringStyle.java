package org.oh.common.util;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;

/**
 * JSON 포맷 스타일
 */
public class JsonRecursiveToStringStyle extends RecursiveToStringStyle {
	public static final String FIELD_NAME_PREFIX = "\"";

	public JsonRecursiveToStringStyle() {
		super();

		this.setUseClassName(false);
		this.setUseIdentityHashCode(false);

		this.setContentStart("{");
		this.setContentEnd("}");

		this.setArrayStart("[");
		this.setArrayEnd("]");

		this.setFieldSeparator(",");
		this.setFieldNameValueSeparator(":");

		this.setNullText("null");

		this.setSummaryObjectStartText("\"<");
		this.setSummaryObjectEndText(">\"");

		this.setSizeStartText("\"<size=");
		this.setSizeEndText(">\"");
	}

	@Override
	public void append(StringBuffer buffer, String fieldName, Object[] array, Boolean fullDetail) {
		if (fieldName == null) {
			throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
		}
		if (!isFullDetail(fullDetail)) {
			throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
		}

		super.append(buffer, fieldName, array, fullDetail);
	}

	@Override
	public void append(StringBuffer buffer, String fieldName, long[] array, Boolean fullDetail) {
		if (fieldName == null) {
			throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
		}
		if (!isFullDetail(fullDetail)) {
			throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
		}

		super.append(buffer, fieldName, array, fullDetail);
	}

	@Override
	public void append(StringBuffer buffer, String fieldName, int[] array, Boolean fullDetail) {
		if (fieldName == null) {
			throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
		}
		if (!isFullDetail(fullDetail)) {
			throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
		}

		super.append(buffer, fieldName, array, fullDetail);
	}

	@Override
	public void append(StringBuffer buffer, String fieldName, short[] array, Boolean fullDetail) {
		if (fieldName == null) {
			throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
		}
		if (!isFullDetail(fullDetail)) {
			throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
		}

		super.append(buffer, fieldName, array, fullDetail);
	}

	@Override
	public void append(StringBuffer buffer, String fieldName, byte[] array, Boolean fullDetail) {
		if (fieldName == null) {
			throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
		}
		if (!isFullDetail(fullDetail)) {
			throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
		}

		super.append(buffer, fieldName, array, fullDetail);
	}

	@Override
	public void append(StringBuffer buffer, String fieldName, char[] array, Boolean fullDetail) {
		if (fieldName == null) {
			throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
		}
		if (!isFullDetail(fullDetail)) {
			throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
		}

		super.append(buffer, fieldName, array, fullDetail);
	}

	@Override
	public void append(StringBuffer buffer, String fieldName, double[] array, Boolean fullDetail) {
		if (fieldName == null) {
			throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
		}
		if (!isFullDetail(fullDetail)) {
			throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
		}

		super.append(buffer, fieldName, array, fullDetail);
	}

	@Override
	public void append(StringBuffer buffer, String fieldName, float[] array, Boolean fullDetail) {
		if (fieldName == null) {
			throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
		}
		if (!isFullDetail(fullDetail)) {
			throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
		}

		super.append(buffer, fieldName, array, fullDetail);
	}

	@Override
	public void append(StringBuffer buffer, String fieldName, boolean[] array, Boolean fullDetail) {
		if (fieldName == null) {
			throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
		}
		if (!isFullDetail(fullDetail)) {
			throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
		}

		super.append(buffer, fieldName, array, fullDetail);
	}

	@Override
	public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
		if (fieldName == null) {
			throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
		}
		if (!isFullDetail(fullDetail)) {
			throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
		}

		super.append(buffer, fieldName, value, fullDetail);
	}

	@Override
	public void appendDetail(StringBuffer buffer, String fieldName, Object value) {
		if (value == null) {
			appendNullText(buffer, fieldName);
			return;
		}
		if (value.getClass() == String.class) {
			appendValueAsString(buffer, (String) value);
			return;
		}

		// RecursiveToStringStyle 호출
//			buffer.append(value);
		super.appendDetail(buffer, fieldName, value);
	}

	@Override
	protected void appendFieldStart(StringBuffer buffer, String fieldName) {
		if (fieldName == null) {
			throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
		}

		super.appendFieldStart(buffer, FIELD_NAME_PREFIX + fieldName + FIELD_NAME_PREFIX);
	}

	protected void appendValueAsString(StringBuffer buffer, String value) {
		buffer.append("\"" + value + "\"");
	}
}