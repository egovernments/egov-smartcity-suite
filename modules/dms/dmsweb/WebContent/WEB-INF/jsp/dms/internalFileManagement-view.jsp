<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>
<jsp:include page="fileManagementHeader.jsp"/>
<s:push value="model">
	<jsp:include page="fileManagementDetail-view.jsp"/>
	<jsp:include page="internal-senderReceiver.jsp"/>
	<jsp:include page="fileManagementHistory.jsp"/>
	<br/>	
</s:push>
<c:import url="/commons/accessPermissionLink.jsp" context="/egi" />
<table border="0" width="100%">
	<tr align="center" class="graybox">
		<td>
			<input type="button" value="<s:text name="lbl.close"/>" tabindex="1" onclick="window.close()">
		</td>
	</tr>
</table>
<jsp:include page="fileManagementFooter.jsp"/>
