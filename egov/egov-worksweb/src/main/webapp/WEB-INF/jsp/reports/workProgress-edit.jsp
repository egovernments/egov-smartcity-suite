<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<s:if test="%{reportType=='deptwise'}"> 
<%@ include file='workProgress-deptwise.jsp'%>
</s:if>
<s:else>
<%@ include file='workProgress-workwise.jsp'%>
</s:else>