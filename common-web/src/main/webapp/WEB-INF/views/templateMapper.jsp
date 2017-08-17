<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<%@ page contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<mapper namespace="${namespace}">
	<sql id="_where">
		<where>
			<if test="condition != null">
				<![CDATA[
	AND \${condition}
				]]>
			</if>
<c:forEach var="column" items="${columnList}">
			<if test="${column.value} != null">
				<![CDATA[
	AND ${column.key} = \#{${column.value}}
				]]>
			</if></c:forEach>
		</where>
	</sql>

	<select id="list" resultType="${className}">
		<include refid="com.nemustech.common.mapper.CommonMapper.page_top" />

		<![CDATA[
SELECT
		]]>

		<if test="hint != null">
			<![CDATA[
	\${hint}
			]]>
		</if>

		<if test="fields == null">
			<![CDATA[
	${fields}
			]]>
		</if>
		<if test="fields != null">
			<![CDATA[
	\${fields}
			]]>
		</if>

		<![CDATA[
  FROM ${table}
		]]>

		<include refid="_where" />

		<if test="having != null">
			<![CDATA[
HAVING \${having}
			]]>
		</if>

		<if test="group_by != null">
			<![CDATA[
GROUP BY \${group_by}
			]]>
		</if>

		<if test="order_by != null">
			<![CDATA[
ORDER BY \${order_by}
			]]>
		</if>

		<include refid="com.nemustech.common.mapper.CommonMapper.page_bottom" />
	</select>

	<select id="count" parameterType="${className}" resultType="java.lang.Integer">
		<include refid="com.nemustech.common.mapper.CommonMapper.count_top" />

		<![CDATA[
  FROM ${table}
		]]>

		<include refid="_where" />
	</select>

	<insert id="insert" parameterType="${className}"<c:if test="${autoIncrement != null}"> useGeneratedKeys="true" keyProperty="${autoIncrement}"</c:if>>
<c:if test="${sequence != null}">		<selectKey keyProperty="id" resultType="long" order="BEFORE">
			<![CDATA[
SELECT ${sequence.key}.NEXTVAL AS ${sequence.value}
  FROM dual
			]]>
		</selectKey>

</c:if>		<![CDATA[
INSERT INTO ${table} (
		]]>

		<trim prefixOverrides=","><c:forEach var="column" items="${createColumnList}">
			<if test="${column.key} != null">
				<![CDATA[
	,${column.key}
				]]>
			</if></c:forEach>
		</trim>

		<![CDATA[
) VALUES (
		]]>

		<trim prefixOverrides=","><c:forEach var="column" items="${createColumnList}">
			<if test="${column.key} != null">
				<![CDATA[
	,${column.value}
				]]>
			</if></c:forEach>
		</trim>

		<![CDATA[
)
		]]>
	</insert>

	<update id="update" parameterType="${className}">
		<![CDATA[
UPDATE ${table}
		]]>

		<set><c:forEach var="column" items="${updatgeColumnList}">
			<if test="${column.key} != null">
				<![CDATA[
	${column.key} = ${column.value},
				]]>
			</if></c:forEach>
		</set>

		<where>
			<if test="${primaryKey.value} != null">
				<![CDATA[
	AND ${primaryKey.key} = \#{${primaryKey.value}}
				]]>
			</if>

			<if test="condition != null">
				<![CDATA[
	AND \${condition}
				]]>
			</if>
		</where>
	</update>

	<delete id="delete" parameterType="${className}">
		<![CDATA[
DELETE FROM ${table}
		]]>

		<include refid="_where" />
	</delete>
</mapper>