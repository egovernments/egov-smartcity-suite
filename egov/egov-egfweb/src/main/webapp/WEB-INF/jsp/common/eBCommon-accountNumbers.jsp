<%@ page contentType="text/json"%><%@ taglib prefix="s"
	uri="/WEB-INF/tags/struts-tags.tld"%><s:if test="accountNumber==null">Please select account number </s:if>
<s:else>
	<s:if test="accountNumberList.size == 0 ">
	</s:if>
	<s:else>
		<s:iterator var="s" value="accountNumberList" status="status">
			<s:property value="%{name}" /> `~` <s:property value="%{code}" />~^</s:iterator>
	</s:else>
</s:else>
