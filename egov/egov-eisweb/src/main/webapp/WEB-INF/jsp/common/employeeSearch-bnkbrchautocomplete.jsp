<%@ page contentType="text/json" %><%@ taglib prefix="s" uri="/struts-tags" %><s:iterator var="s" value="listOfBranches" status="status"><s:property value="%{branchname}"/>`~`<s:property value="%{id}"/><s:if test="!#status.last"></s:if>+</s:iterator>^       

