<%@ page contentType="text/json"%><%@ taglib prefix="s"
	uri="/WEB-INF/tags/struts-tags.tld"%><s:iterator var="s"
	value="entitiesList" status="status">
	<s:property value="%{code}" />`-`<s:property value="%{name}" />`~`<s:property
		value="%{id}" />+</s:iterator>
^
