<%@ page contentType="text/json" %>
<%@ page import="java.util.*"%>
<%@ taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld" %>
<s:iterator var="s" value="entityList" status="status">
<s:property value="%{code}" />`-`<s:property value="%{name}" />`-`<s:property value="%{id}" />+
</s:iterator>^
