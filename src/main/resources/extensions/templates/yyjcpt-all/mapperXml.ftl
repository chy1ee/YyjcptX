<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${mapperInterface.packageName}.${baseInfo.fileName}">

    <resultMap id="${tableClass.shortClassName}ResultMap" type="${tableClass.fullClassName}">
        <#list tableClass.pkFields as field>
            <id property="${field.fieldName}" column="${field.columnName}" jdbcType="${field.jdbcType}"/>
        </#list>
        <#list tableClass.baseFields as field>
            <result property="${field.fieldName}" column="${field.columnName}" jdbcType="${field.jdbcType}"/>
        </#list>
    </resultMap>

    <sql id="${tableClass.shortClassName}ColumnList">
        <#list tableClass.allFields as field>${field.columnName}<#sep>,<#if field_index%11==10>${"\n        "}</#if></#list>
    </sql>

    <select id="listPage" parameterType="${baseInfo.basePackage}.qo.${tableClass.shortClassName}QO" resultMap="${tableClass.shortClassName}ResultMap">
        select <include refid="${tableClass.shortClassName}ColumnList" />
        from ${tableClass.tableName}<#if (tableClass.parameterFields?size>0)>
        <where><#list tableClass.parameterFields as field>
            <if test='${field.fieldName} != null<#if field.shortTypeName==""> and ${field.fieldName} != "String"</#if>'>AND ${field.columnName} = ${'#'}{${field.fieldName},jdbcType=${field.jdbcType}}</if></#list>
        </where></#if>
    </select>
</mapper>
