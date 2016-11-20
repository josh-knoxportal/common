<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<%@ page contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<mapper namespace="com.nemustech.common.mapper.CommonMapper">
<select id="list" resultType="${table}">
	<include refid="com.nemustech.common.mapper.CommonMapper.page_top" />

	<![CDATA[
SELECT <c:forEach var="column" items="${column_list}"><c:out value="${column}"/>
       </c:forEach>]]>

	<include refid="com.nemustech.common.mapper.CommonMapper.page_middle" />

	<![CDATA[
  FROM ${table}
	]]>

	<where><c:forEach var="column" items="${column_list}">
	<if test="${column} != null">
	<![CDATA[
   AND ${column} = <c:out value="$"/><c:out value="{"/>${column}<c:out value="}"/>
	]]>
	</if></c:forEach>
	</where>

	<if test="order_by != null">
	<![CDATA[
ORDER BY ${order_by}
	]]>
	</if>

	<include refid="com.nemustech.common.mapper.CommonMapper.page_bottom" />
</select>
</mapper>