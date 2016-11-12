package com.nemustech.common.json;

import org.codehaus.jackson.node.ObjectNode;

/**
 * 전문의 헤더를 쉽게 사용할 수 있는 POJO를 만들기 위한 interface
 * 
 * 
 * @version 1.0.0
 * 
 */
public interface SmartHeaderNode {
	/**
	 * rootNode 하위에 <code>header</code> 노드를 생성한다.
	 * 
	 * @param rootNode <code>header</code> 노드가 생성될 부모 노드
	 */
	abstract void buildJsonHeader(ObjectNode rootNode);
}
