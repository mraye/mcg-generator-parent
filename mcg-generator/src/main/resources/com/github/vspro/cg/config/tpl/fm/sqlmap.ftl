<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${namespace}">


    <resultMap id="baseResultMap" type="${domainFullType}">
    <#list primaryColumns as prop>
        <id column="${prop.actualColumnName}" property="${prop.javaProperty}" jdbcType="${prop.jdbcTypeName}"/>
    </#list>
    <#list baseColumns as prop>
        <result column="${prop.actualColumnName}" property="${prop.javaProperty}" jdbcType="${prop.jdbcTypeName}"/>
    </#list>
    <#list blobColumns as prop>
        <result column="${prop.actualColumnName}" property="${prop.javaProperty}" jdbcType="${prop.jdbcTypeName}"/>
    </#list>
    </resultMap>

    <sql id="baseColumns">
        <#list columns as prop>
        <#if !prop_has_next>
        ${prop.actualColumnName}
        <#else>
        ${prop.actualColumnName},
        </#if>
        </#list>
    </sql>

    <sql id="insertColumnsVal">
        <#list columns as prop>
        <#if !prop_has_next>
        <#noparse>#{</#noparse>{${prop.javaProperty}<#noparse>}</#noparse>,
        <#else>
        <#noparse>#{</#noparse>{prop.javaProperty}<#noparse>}</#noparse>
        </#if>
        </#list>
    </sql>

    <sql id="selectiveInsertColumnsVal">
    <#list columns as prop>
        <if test="$prop.javaProperty != null" >
            ${prop.actualColumnName} = <#noparse>#{</#noparse>${prop.javaProperty}<#noparse>}</#noparse>,
        </if>
    </#list>
    </sql>

    <sql id="updateColumns">
        <set>
            <#list columns as prop>
            <if test="${prop.javaProperty} != null <#if prop.jdbcTypeName=='VARCHAR'>${prop.javaProperty} != ''</#if>">
                ${prop.actualColumnName} = <#noparse>#{</#noparse>${prop.javaProperty},jdbcType=${prop.jdbcTypeName}<#noparse>}</#noparse>,
            </if>
            </#list>
        </set>
    </sql>

    <insert id="insert" useGeneratedKeys="true" keyProperty="id" parameterType="${domainFullType}">
        insert into ${tableName}
        (
            <include refid="baseColumns"/>
        )
        values
        (
            <include refid="insertColumnsVal"/>
        )
    </insert>


   <insert id="insertOrUpdate" parameterType="${domainFullType}">
       insert into ${tableName}
       (
           <include refid="baseColumns"/>
       )
       values
       (
           <include refid="insertColumnsVal"/>
       )
       ON DUPLICATE KEY UPDATE
       <trim suffixOverrides=",">
           <include refid="selectiveInsertColumnsVal"/>
       </trim>
   </insert>


    <select id="selectByPrimaryKey" resultMap="baseResultMap" parameterType="map" >
        select
        <include refid="baseColumns"/>
        from ${tableName}
        <where>
            <trim suffixOverrides="and">
                 <#list primaryColumns as prop>
                and ${prop.actualColumnName} = <#noparse>#{</#noparse>${prop.javaProperty},jdbcType=${prop.jdbcTypeName}<#noparse>}</#noparse>
                </#list>
            </trim>
        </where>
    </select>


    <update id="updateByPrimaryKeySelective" parameterType="${domainFullType}" >
        update ${tableName}
        <set>
            <#list columnsNoPrimaryKey as prop>
            <if test="${prop.javaProperty} != null <#if prop.jdbcTypeName=='VARCHAR'>and ${prop.javaProperty} != ''</#if> ">
                ${prop.actualColumnName} = <#noparse>#{</#noparse>${prop.javaProperty},jdbcType=${prop.jdbcTypeName}<#noparse>}</#noparse>,
            </if>
            </#list>
        </set>
        <where>
            <trim suffixOverrides="and">
                <#list primaryColumns as prop>
                and ${prop.actualColumnName} = <#noparse>#{</#noparse>${prop.javaProperty},jdbcType=${prop.jdbcTypeName}<#noparse>}</#noparse>
                </#list>
            </trim>
        </where>
    </update>


    <update id="updateByPrimaryKey" parameterType="${domainFullType}" >
        update ${tableName}
        <set>
            <#list columnsNoPrimaryKey as prop>
            ${prop.actualColumnName} = <#noparse>#{</#noparse>${prop.javaProperty},jdbcType=${prop.jdbcTypeName}<#noparse>}</#noparse>,
            </#list>
        </set>
        <where>
            <trim suffixOverrides="and">
                <#list primaryColumns as prop>
                and ${prop.actualColumnName} = <#noparse>#{</#noparse>${prop.javaProperty},jdbcType=${prop.jdbcTypeName}<#noparse>}</#noparse>
                </#list>
            </trim>
        </where>
    </update>

    <delete id="deleteByPrimaryKey" parameterType="map" >
        delete from ${tableName}
        <where>
            <trim suffixOverrides="and">
                <#list primaryColumns as prop>
                and ${prop.actualColumnName} = <#noparse>#{</#noparse>${prop.javaProperty},jdbcType=${prop.jdbcTypeName}<#noparse>}</#noparse>
                </#list>
            </trim>
        </where>
    </delete>

<#if enableLogicalDel>
    <update id="deleteLogicalByPrimaryKey" parameterType="map" >
        update ${tableName}
        <#if logicalDelColValIsNum>
            set ${logicalDelColName}=${logicalDelColVal}
        <#else>
            set ${logicalDelColName}='${logicalDelColVal}'
        </#if>
        <where>
            <trim suffixOverrides="and">
                <#list primaryColumns as prop>
                and ${prop.actualColumnName} = <#noparse>#{</#noparse>${prop.javaProperty},jdbcType=${prop.jdbcTypeName}<#noparse>}</#noparse>
                </#list>
            </trim>
        </where>
    </update>
</#if>

    <insert id="batchInsert" useGeneratedKeys="true" keyProperty="id">
        insert into ${tableName}
        (
            <include refid="baseColumns"/>
        ) values
        <foreach collection="list" item="obj" separator=",">
            (
            <#list columns as prop>
                <#if !prop_has_next>
                <#noparse>#{</#noparse>obj.${prop.javaProperty}<#noparse>}</#noparse>
                    <#else>
                <#noparse>#{</#noparse>obj.${prop.javaProperty}<#noparse>}</#noparse>,
                </#if>
            </#list>
            )
        </foreach>
    </insert>

</mapper>
