<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<s:if test="targetAreaList.size == 0"></s:if>
<s:else>
	<s:iterator var="s" value="targetAreaList" status="status">
		<s:property value="%{name}" />
	</s:iterator>
</s:else>
