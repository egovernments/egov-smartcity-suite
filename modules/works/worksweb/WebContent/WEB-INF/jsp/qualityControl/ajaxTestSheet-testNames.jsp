<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:iterator var="s" value="testMasterList" status="status"><s:property value="%{testName}" />~<s:property value="%{id}" />~<s:property value="%{materialType.documentNumber}" />$</s:iterator>#<s:property value="%{recId}" />