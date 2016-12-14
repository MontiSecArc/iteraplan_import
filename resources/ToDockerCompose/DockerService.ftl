<#-- @ftlvariable name="links" type="java.lang.String[]" -->
<#-- @ftlvariable name="cpe" type="java.lang.String" -->
<#-- @ftlvariable name="serviceName" type="java.lang.String" -->
    ${serviceName}:
        <#if cpe != "">
        image: ${cpe}
        </#if>
        <#if (links?size > 0)>
        links:
            <#list links as link>
            - ${link}
            </#list>
        </#if>