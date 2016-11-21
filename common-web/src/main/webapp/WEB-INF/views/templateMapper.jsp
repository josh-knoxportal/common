<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<%@ page contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<mapper namespace="${namespace}">
<select id="list" resultType="${table}">
	<include refid="com.nemustech.common.mapper.CommonMapper.page_top" />

	<![CDATA[
SELECT
	]]>

	<if test="hint != null">
	<![CDATA[
 <c:out value="\${"/>hint<c:out value="}"/>
	]]>
	</if>

	<if test="fields == null">
	<![CDATA[
 ${fields}
	]]>
	</if>
	<if test="fields != null">
	<![CDATA[
 <c:out value="\${"/>fields<c:out value="}"/>
	]]>
	</if>

	<include refid="com.nemustech.common.mapper.CommonMapper.page_middle" />

	<![CDATA[
  FROM ${table}
	]]>

	<where>
	<if test="condition != null">
	<![CDATA[
   AND <c:out value="\${"/>condition<c:out value="}"/>
	]]>
	</if><c:forEach var="column" items="${columnList}">
	<if test="${column.value} != null">
	<![CDATA[
   AND ${column.key} = <c:out value="#{"/>${column.value}<c:out value="}"/>
	]]>
	</if></c:forEach>
	</where>

	<if test="having != null">
	<![CDATA[
HAVING <c:out value="\${"/>having<c:out value="}"/>
	]]>
	</if>

	<if test="group_by != null">
	<![CDATA[
GROUP BY <c:out value="\${"/>group_by<c:out value="}"/>
	]]>
	</if>

	<if test="order_by != null">
	<![CDATA[
ORDER BY <c:out value="\${"/>order_by<c:out value="}"/>
	]]>
	</if>

	<include refid="com.nemustech.common.mapper.CommonMapper.page_bottom" />
</select>

	<select id="count" parameterType="${table}" resultType="java.lang.Integer">
		<include refid="com.nemustech.common.mapper.CommonMapper.count_top" />

		<![CDATA[
  FROM ${table}
		]]>

	<where>
	<if test="condition != null">
	<![CDATA[
   AND <c:out value="\${"/>condition<c:out value="}"/>
	]]>
	</if><c:forEach var="column" items="${columnList}">
	<if test="${column.value} != null">
	<![CDATA[
   AND ${column.key} = <c:out value="#{"/>${column.value}<c:out value="}"/>
	]]>
	</if></c:forEach>
	</where>
	</select>

	<insert id="insert" parameterType="${table}">
		<selectKey keyProperty="id" resultType="long" order="BEFORE">
			<![CDATA[
SELECT ${sequence}.NEXTVAL AS ${sequenceFieldName}
  FROM dual
			]]>
		</selectKey>

		<![CDATA[
INSERT INTO ${table} (
		]]><c:forEach var="column" items="${columnList}">, ${column.key}</c:forEach>

	</insert>
</mapper>