package ${package};
import lombok.Data;

<#list useJSR310Types as ipt>
import ${ipt};
</#list>


@Data
public class ${className} {

<#list javaProperties as prop>

<#--    判断是否为空prop.remarks??，-->
<#--    判断是不是空字符串prop.remarks?trim?length gt 1-->
    <#if prop.remarks?? && prop.remarks?trim?length gt 1>
    /** ${prop.remarks} */
    </#if>
    private ${prop.javaPropertyName} ${prop.javaProperty};
</#list>

}
