<%@ page contentType="text/json" %><%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld" %><s:if test="targetAreaName==null">Please Enter TargetArea Name </s:if><s:else><s:if test="targetAreaNameList.size == 0 "> Nothing found to display
</s:if><s:else><s:iterator var="s" value="targetAreaNameList" status="status"><s:property value="%{s}" />~^</s:iterator></s:else></s:else>
