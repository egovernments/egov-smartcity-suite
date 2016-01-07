<%@ page contentType="text/json"%><%@ taglib prefix="s"
	uri="/WEB-INF/tags/struts-tags.tld"%><s:if test="consumerNumber==null">Please select consumer number </s:if>
<s:else>
	<s:if test="consumerList.size == 0 ">
	</s:if>
	<s:else>
		<s:iterator var="s" value="consumerList" status="status">
			<s:property value="%{code}" /> `~` <s:property value="%{name}" />~^</s:iterator>
	</s:else>
</s:else>
