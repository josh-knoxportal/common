<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.nemustech.sample.mapper.SampleMapper">
<select id="list" resultType="${type_alias}">
	<include refid="com.nemustech.web.mapper.CommonMapper.page_top" />

	<![CDATA[
SELECT id
      ,name
      ,test_id
      ,reg_id
      ,reg_dt
      ,mod_id
      ,mod_dt
		]]>

	<include refid="com.nemustech.web.mapper.CommonMapper.page_middle" />

	<![CDATA[
  FROM ${table_name}
		]]>

	<where>
	<if test="id != null and id != 0">
	<![CDATA[
   AND ${column_name} = ${column_value}
			]]>
	</if>
	</where>

	<if test="order_by != null and order_by != ''">
	<![CDATA[
ORDER BY ${order_by}
		]]>
	</if>

	<include refid="com.nemustech.web.mapper.CommonMapper.page_bottom" />
</select>

<select id="count" parameterType="${type_alias}" resultType="java.lang.Integer">
	<include refid="com.nemustech.web.mapper.CommonMapper.count_top" />

	<![CDATA[
  FROM ${table_name}
		]]>

	<where>
	<if test="id != null and id != 0">
	<![CDATA[
   AND id = #{id}
			]]>
	</if>
	</where>
</select>

<insert id="insert" parameterType="${type_alias}">
<selectKey keyProperty="id" resultType="long" order="BEFORE">
<![CDATA[
SELECT ${sequence_name}.NEXTVAL AS id
  FROM dual
			]]>
</selectKey>

<![CDATA[
INSERT INTO ${table_name} (id
		]]>

<if test="name != null and name != ''">
<![CDATA[
                  ,name
		]]>
</if>

<![CDATA[
                   ,reg_id
                   ,reg_dt
                   ,mod_id
                   ,mod_dt
		]]>

<![CDATA[
                   )
VALUES (#{id}
		]]>

<if test="name != null and name != ''">
<![CDATA[
       ,#{name}
		]]>
</if>

<![CDATA[
       ,#{reg_id}
       ,TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')
       ,#{mod_id}
       ,TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))
		]]>
</insert>

<update id="update" parameterType="${type_alias}">
<![CDATA[
UPDATE ${table_name}
		]]>

<set>
<if test="name != null and name != ''">
<![CDATA[
       name = #{name},
			]]>
</if>
</set>

<![CDATA[
      ,mod_id = #{mod_id},
       mod_dt = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')
 WHERE id = #{id}
		]]>
</update>

<delete id="delete" parameterType="${type_alias}">
<![CDATA[
DELETE FROM ${table_name}
 WHERE id = #{id}
		]]>
</delete>
</mapper>