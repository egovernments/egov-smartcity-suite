<%@ include file="/includes/taglibs.jsp" %>

<html>
<head>
	<title>Complaint Receiving Center</title>
	
</head>
<body>
	<table align="center" >
		<tr>
			<td>
			<div id="main">
			<div id="m2">
			<div id="m3">
			<table class="tableStylenew">
			 	<tr>
			 		<td  class="tableheadernew" >
			 		Create Complaint Receiving Center 
			 		</td>
			 	</tr>
			 	<tr>	
					<td>&nbsp;</td>
				</tr>	
				<tr>
					<td >
					<s:actionerror/>
					<s:form action="complaintReceivingCenterMaster.action" >
					<%@ include file='complaintReceivingCenterMaster-form.jsp'%>
					<s:hidden name="complantGroup_Id" id="complantGroup_Id"/>
					</td>
				</tr>
				<tr>
						<td class="checkboxareanew" colspan="2">
						<span class="checkboxlabelstyle">Active</span>
						<s:checkbox  fieldValue="false"  value="false"   name="model.is_Active"  cssClass="checkboxstyle"   theme="simple"  requiredposition="right"  />
					
						</td>
				</tr>
				<tr>
					<td class="buttonareanew" colspan="2">
					<s:submit align="left"  theme="simple"  type="button" cssClass="buttonleft" method="create" value="Create"/> 
					<s:submit align="right"  theme="simple" type="button" cssClass="buttonright"  onClick="window.close()" value="Close"/> 
					</s:form>
					</td>
				</tr>
				<tr>	
					<td >&nbsp;</td>
				</tr>
	</table>
	</div>
			</div>
			</div>
	 		</td>
	 	</tr>
  </table>
</body>
</html>
