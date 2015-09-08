<%@ page contentType="text/json"%><%@ taglib prefix="s"
	uri="/WEB-INF/tags/struts-tags.tld"%><s:if test="targetAreaCode==null">Please Enter TargetArea Code </s:if>
<s:else>
	<s:if test="targetAreaCodeList.size == 0 "> Nothing found to display
</s:if>
	<s:else>
		<s:iterator var="s" value="targetAreaCodeList" status="status">
      <s:property value="%{code}"/>~<s:property value="%{name}"/>~^
    </s:iterator>
	</s:else>
</s:else>
