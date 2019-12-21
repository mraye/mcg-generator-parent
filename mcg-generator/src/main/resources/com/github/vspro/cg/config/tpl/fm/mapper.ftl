package ${package};

import ${domainFullType};
import org.apache.ibatis.annotations.Param;
import java.util.Collection;

public interface ${interfaceName} {


    int insert(${domainType} ${domainShortName});

    int insertOrUpdate(${domainType} ${domainShortName});

    <#--##    int insertSelective(${domainType} ${domainShortName});-->
    ${domainType} selectByPrimaryKey(<#list primaryColumns as prop><#if !prop_has_next>@Param("${prop.javaProperty}")${prop.javaPropertyName} ${prop.javaProperty}<#else>@Param("${prop.javaProperty}") ${prop.javaPropertyName} ${prop.javaProperty},</#if></#list>);

    int updateByPrimaryKeySelective(${domainType} ${domainShortName});

    int updateByPrimaryKey(${domainType} ${domainShortName});

    int deleteByPrimaryKey(<#list primaryColumns as prop><#if !prop_has_next>@Param("${prop.javaProperty}") ${prop.javaPropertyName} ${prop.javaProperty}<#else>@Param("${prop.javaProperty}") ${prop.javaPropertyName} ${prop.javaProperty},</#if></#list>);

    <#if enableLogicalDel??>
    int deleteLogicalByPrimaryKey(<#list primaryColumns as prop><#if !prop_has_next>@Param("${prop.javaProperty}") ${prop.javaPropertyName} ${prop.javaProperty}<#else>@Param("${prop.javaProperty}") ${prop.javaPropertyName} ${prop.javaProperty},</#if></#list>);
    </#if>

    int batchInsert(@Param("list") Collection<${domainType}> list);

}

