<%@ include file="/includes/taglibs.jsp" %>

<html>
<head>
	<title>Complaint Receiving Center Edit</title>
	
</head>
<body >
<table align="center" >
		<tr>
			<td >
			<div id="main">
			<div id="m2">
			<div id="m3">
			 <table class="tableStylenew">
				<tr>
					<td  class="tableheadernew" >
						Edit Complaint Receiving Center 
					</td>
				</tr>
				<tr>	
					<td >&nbsp;</td>
				</tr>	
				<tr>
					<td >
					<s:actionerror/>
					<s:form action="complaintReceivingCenterMaster.action" >
					<%@ include file='complaintReceivingCenterMaster-form.jsp'%>
					<s:hidden name="receivingCenterId" id="receivingCenterId"/>
					</td>
				</tr>
				<tr>
										
						<td class="checkboxareanew" colspan="2">
						<span margin-left:0px;padding-left:11px>Active</span>
						<s:if test="%{model.is_Active != 1}">
							<s:checkbox  fieldValue="false" theme="simple" value="false"   name="model.is_Active"  cssStyle="margin-left:15px;margin-right:70px;"     requiredposition="right"  />
						</s:if>
						<s:else>
							<s:checkbox   fieldValue="true" theme="simple" value="true"   name="model.is_Active"  cssStyle="margin-left:15px;margin-right:70px;"   requiredposition="right"  />
						</s:else>
						</td>
					</tr>	
				<tr>
					<td class="buttonareanew" colspan="2">	
					<s:submit align="left"  theme="simple"  type="button" cssClass="buttonleft" method="save" value="Update"/> 
					<s:submit align="right"  theme="simple" type="button" cssClass="buttonright"  onClick="window.close()" value="Close"/> 
					</s:form>
					
					</td>
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
