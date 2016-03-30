package org.oh.common.db.type;

/**
 * SQL 처리를 위한 파라미터 객체
 * 
 * 
 * @version 1.0.0
 *
 */
public class DBSqlParameter {
	private Object parameters;
	private String sqlId;
	private String dsName;

	/**
	 * 생성자
	 */
	public DBSqlParameter() {
		this(null);
	}

	/**
	 * 생성자
	 * 
	 * @param sql SQL ID
	 */
	public DBSqlParameter(String sql) {
		this(sql, null);
	}

	/**
	 * 생성자
	 * 
	 * @param sql SQL ID
	 * @param parameters SQL문에 사용할 파라미터 객체
	 */
	public DBSqlParameter(String sql, Object parameters) {
		this(sql, "default", parameters);
	}

	/**
	 * 생성자
	 * 
	 * @param sql SQL ID
	 * @param dsName <code>DataSource</code> 이름
	 */
	public DBSqlParameter(String sql, String dsName) {
		this(sql, dsName, null);
	}

	/**
	 * 생성자
	 * 
	 * @param sql SQL ID
	 * @param dsName <code>DataSource</code> 이름
	 * @param parameters SQL문에 사용할 파라미터 객체
	 */
	public DBSqlParameter(String sql, String dsName, Object parameters) {
		this.sqlId = sql;
		this.dsName = dsName;
		this.parameters = parameters;
	}

	/**
	 * Getter
	 * 
	 * @return the parameters field value to get
	 */
	public Object getParameters() {
		return parameters;
	}

	/**
	 * Setter
	 * 
	 * @param parameters the parameters field value to set
	 */
	public void setParameters(Object parameters) {
		this.parameters = parameters;
	}

	/**
	 * Getter
	 * 
	 * @return the sqlId field value to get
	 */
	public String getSqlID() {
		return sqlId;
	}

	/**
	 * Setter
	 * 
	 * @param sqlId the sqlId field value to set
	 */
	public void setSqlID(String sqlId) {
		this.sqlId = sqlId;
	}

	/**
	 * Getter
	 * 
	 * @return the dsName field value to get
	 */
	public String getDsName() {
		return dsName;
	}

	/**
	 * Setter
	 * 
	 * @param dsName the dsName field value to set
	 */
	public void setDsName(String dsName) {
		this.dsName = dsName;
	}
}
