<%@ include file="/includes/taglibs.jsp" %>
<html>
<head>
	<title>Complaint Group Edit</title>
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
</head>
<body>
<table align="center" >
		<tr>
			<td >
			<div id="main">
			<div id="m2">
			<div id="m3">
			 <table class="tableStylenew">
				<tr>
					<td  class="tableheadernew" >
						Edit Complaint Group 
					</td>
				</tr>
				<tr>	
					<td >&nbsp;</td>
				</tr>			
				<tr>
					<td >
					<s:actionerror/>
					
					<s:form action="complaintGroupMaster.action">
					<%@ include file='complaintGroupMaster-form.jsp'%>
					<s:hidden name="model.complantGroup_Id" id="model.complantGroup_Id"/>
					</td>
				</tr>
			
				<tr>
					
					<td class="checkboxareanew" colspan="2">
					<span class="checkboxlabelstyle">Active</span>
					<s:if test="%{model.is_Active != 1}">
					<s:checkbox  fieldValue="false" theme="simple" value="false"   name="model.is_Active"  cssClass="checkboxstyle"   label="active"  requiredposition="right"  />
					</s:if>
					<s:else>
					<s:checkbox   fieldValue="true" theme="simple" value="true"   name="model.is_Active"  cssClass="checkboxstyle"   label="active"  requiredposition="right"  />
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
				<tr>	
									<td >&nbsp;</td>
				</tr>
				</table>
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

					
