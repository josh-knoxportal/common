package org.oh.common.db.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

/**
 * DBMap을 List로 가지는 Data Type
 * 
 * 
 * @version 1.0.0
 * @see java.util.ArrayList
 *
 */
public class DBMapList extends ArrayList<DBMap> {
	protected static Log log = LogFactory.getLog(DBMapList.class);

	private static final long serialVersionUID = 1L;

	/**
	 * 생성자.
	 * 
	 * @param list 초기 data를 가지고 있는 List
	 */
	public DBMapList(List<DBMap> list) {
		super(list.size());

		for (DBMap map : list) {
			this.add(map);
		}
	}

	/**
	 * 생성자
	 */
	public DBMapList() {
		this(1);
	}

	/**
	 * 생성자
	 * 
	 * @param size List의 기본 크기
	 * @see java.util.ArrayList#ArrayList(int)
	 */
	public DBMapList(int size) {
		super(size);
	}

	/**
	 * DBMap 객체 리스트를 array 형태로 parentNode에 추가한다.
	 * 
	 * @param parentNode 객체를 저장할 <code>ArrayNode</code>
	 * @throws NullPointerException parentNode가 null
	 */
	public void toJsonNode(ArrayNode parentNode) throws NullPointerException {
		log.trace("Start::toJsonNode()");
		log.trace("  > parentNode: " + parentNode);

		ObjectNode objNode = null;

		for (DBMap map : this) {
			objNode = parentNode.objectNode();
			map.toJsonNode(objNode);
			parentNode.add(objNode);
		}

		log.trace("End::toJsonNode()");
	}

	/**
	 * DBMap 객체 리스트를 array 형태로 parentNode에 추가한다. key 이름은 대/소문자를 무시하고 하나의 형태로 한다.
	 * 
	 * @param parentNode 객체를 저장할 <code>ArrayNode</code>
	 * @param upperCase true 면 key 이름을 일괄적으로 대문자로 변경하며, false 면 일괄적으로 소문자로 변경
	 * @throws NullPointerException parentNode가 null
	 */
	public void toJsonNode(ArrayNode parentNode, boolean upperCase) throws NullPointerException {
		log.trace("Start::toJsonNode()");
		log.trace("  > parentNode: " + parentNode);
		log.trace("  > upperCase: " + upperCase);

		ObjectNode objNode = null;

		for (DBMap map : this) {
			objNode = parentNode.objectNode();
			map.toJsonNode(objNode, upperCase);
			parentNode.add(objNode);
		}

		log.trace("End::toJsonNode()");
	}

	/**
	 * DBMap 객체 리스트를 array 형태로 가지는 {@link org.codehaus.jackson.node.ObjectNode}를 생성한다.
	 * 
	 * @param nodeName <code>ArrayNode</code>의 이름(key)
	 * @return <code>ObjectNode</code> 객체를 반환
	 */
	public ObjectNode toJsonNode(String nodeName) {
		ObjectNode objectJsonNode = JsonNodeFactory.instance.objectNode();
		this.toJsonNode(objectJsonNode.putArray(nodeName));

		return objectJsonNode;
	}

	/**
	 * DBMap 객체 리스트를 array 형태로 가지는 {@link org.codehaus.jackson.node.ObjectNode}를 생성한다. key 이름은 대/소문자를 무시하고 하나의 형태로 한다.
	 * 
	 * @param nodeName <code>ArrayNode</code>의 이름(key). upperCase의 설정에 따라 대/소문자로 일괄 변경
	 * @param upperCase true 면 key 이름을 일괄적으로 대문자로 변경하며, false 면 일괄적으로 소문자로 변경
	 * @return <code>ObjectNode</code> 객체를 반환
	 */
	public ObjectNode toJsonNode(String nodeName, boolean upperCase) {
		ObjectNode objectJsonNode = JsonNodeFactory.instance.objectNode();
		nodeName = (upperCase) ? nodeName.toUpperCase(Locale.ENGLISH) : nodeName.toLowerCase(Locale.ENGLISH);
		this.toJsonNode(objectJsonNode.putArray(nodeName), upperCase);

		return objectJsonNode;
	}

	/**
	 * List의 첫번째 DBMap을 돌려 준다.
	 * 
	 * @return 첫번째 <coce>DBMap</code>
	 */
	public DBMap get() {
		if (this.size() == 0) {
			return null;
		}

		else {
			return this.get(0);
		}
	}
}
