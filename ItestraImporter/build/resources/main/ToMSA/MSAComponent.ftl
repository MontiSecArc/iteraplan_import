component ${componentName} <#if componentInstanceName??>${componentInstanceName}</#if> {

    <#if trustlevel??>
    trustlevel ${trustlevel};
    </#if>

    <#if inNeeded??>port in String inDummy;</#if>
    <#if outNeeded??>port out String outDummy;</#if>

    <#if subcomponents??>
    <#list subcomponents as subcomponent>
    ${subcomponent}
    </#list>
    </#if>
    <#if instances??>
        <#list instances as instance>
        ${instance.type} ${instance.name};
        </#list>
        <#list connections as connection>
        connect ${connection.from}outDummy -> ${connection.to}inDummy;
        </#list>
    </#if>
}