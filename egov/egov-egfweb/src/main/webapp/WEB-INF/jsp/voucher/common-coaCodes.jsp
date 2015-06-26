<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld" %>
<s:if test="glCode==0">
Please select COA 
</s:if>
<s:else>
<s:if test="glCodesList.size == 0 ">
 Nothing found to display
</s:if>
<s:else>
<s:iterator var="s" value="glCodesList" status="status">
<s:property value="%{glcode}" />`-`<s:property value="%{name}" />`~`<s:property value="%{id}" />
</s:iterator>
</s:else>
</s:else>
