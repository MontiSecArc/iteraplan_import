<#if packageName??>
package ${packageName};
</#if>

<#if imports??>
<#list imports as import>
import ${import};
</#list>
</#if>
import java.lang.String;

component ${componentName} {

    accesscontrol off;
    <#if trustlevel??>trustlevel ${trustlevel};</#if>

    <#if inNeeded??>port in String inDummy;</#if>
    <#if outNeeded??>port out String outDummy;</#if>

    <#list subcomponents as subcomponent>
    ${subcomponent}
    </#list>
<#if instances??>
    <#list instances as instance>
    ${instance.type} ${instance.name};
    </#list>
    <#list connections as connection>
    connect ${connection.from}outDummy -> ${connection.to}inDummy;
    </#list>
</#if>
}