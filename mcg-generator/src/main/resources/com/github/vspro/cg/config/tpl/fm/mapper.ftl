package ${package};

import ${domainFullType};
<#if rootInterface?? && rootInterface?trim?length gt 1>
import ${rootInterface};
</#if>
import org.apache.ibatis.annotations.Param;
<#if interfaceMethods?seq_contains("batchInsert")>
import java.util.Collection;
</#if>

public interface ${interfaceName}<#if rootInterface?? && rootInterface?trim?length gt 1> extends ${rootInterfaceShortName}<${domainType}></#if> {

<#if interfaceMethods?seq_contains("insert")>
    int insert(${domainType} ${domainShortName});

</#if>
<#if interfaceMethods?seq_contains("insertOrUpdate")>
    int insertOrUpdate(${domainType} ${domainShortName});

</#if>
<#if interfaceMethods?seq_contains("selectByPrimaryKey")>
    <#--##    int insertSelective(${domainType} ${domainShortName});-->
    ${domainType} selectByPrimaryKey(<#list primaryColumns as prop><#if !prop_has_next>@Param("${prop.javaProperty}")${prop.javaPropertyName} ${prop.javaProperty}<#else>@Param("${prop.javaProperty}") ${prop.javaPropertyName} ${prop.javaProperty},</#if></#list>);

</#if>
<#if interfaceMethods?seq_contains("selectOneSelective")>
    ${domainType} selectOneSelective(${domainType} ${domainShortName});

</#if>
<#if interfaceMethods?seq_contains("updateByPrimaryKeySelective")>
    int updateByPrimaryKeySelective(${domainType} ${domainShortName});

</#if>
<#if interfaceMethods?seq_contains("updateByPrimaryKey")>
    int updateByPrimaryKey(${domainType} ${domainShortName});

</#if>
<#if interfaceMethods?seq_contains("deleteByPrimaryKey")>
    int deleteByPrimaryKey(<#list primaryColumns as prop><#if !prop_has_next>@Param("${prop.javaProperty}") ${prop.javaPropertyName} ${prop.javaProperty}<#else>@Param("${prop.javaProperty}") ${prop.javaPropertyName} ${prop.javaProperty},</#if></#list>);

</#if>
<#if interfaceMethods?seq_contains("deleteLogicalByPrimaryKey")>
    <#if enableLogicalDel>
    int deleteLogicalByPrimaryKey(<#list primaryColumns as prop><#if !prop_has_next>@Param("${prop.javaProperty}") ${prop.javaPropertyName} ${prop.javaProperty}<#else>@Param("${prop.javaProperty}") ${prop.javaPropertyName} ${prop.javaProperty},</#if></#list>);

    </#if>
</#if>
<#if interfaceMethods?seq_contains("batchInsert")>
    int batchInsert(@Param("list") Collection<${domainType}> list);

</#if>
}

