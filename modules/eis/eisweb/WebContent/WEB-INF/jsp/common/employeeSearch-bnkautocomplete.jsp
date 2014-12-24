<%@ page contentType="text/json" %><%@ taglib prefix="s" uri="/struts-tags" %><s:iterator var="s" value="listOfBanks" status="status"><s:property value="%{name}"/>`~`<s:property value="%{id}"/><s:if test="!#status.last"></s:if>+</s:iterator>^       

