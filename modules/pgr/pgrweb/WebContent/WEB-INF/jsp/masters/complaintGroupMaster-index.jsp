<%@ include file="/includes/taglibs.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Complaint Group Index</title>
<script type="text/javascript">
 function checkName() {
  	var name = document.getElementById("model.complantGroup_Id").value;
	if( name == null ||  name == "" || name == 0)
	{
		alert("please select Complaint Group name");
		return false;
	}
}
</script>
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
						Complaint Group List
					</td>
				</tr>
				<tr>	
					<td >&nbsp;</td>
				</tr>
				<tr>
					<td >
					<s:actionerror/><s:fielderror/>
					<s:form action="complaintGroupMaster.action">
					<s:select list="complaintGroupList" cssClass="select" label="%{getText('Masters.complaintgroup.name')}" required="true" name="model.complantGroup_Id" id="model.complantGroup_Id" listKey="complantGroup_Id" listValue="complaintGroupName" headerKey="0"   headerValue="%{getText('Please.Choose')}" ></s:select>
					<s:hidden key="model.is_Active" name="model.is_Active" value="model.is_Active" />
					</td>
				</tr>	
				<tr>
					<td class="buttonareanew" colspan="2">	
					<s:submit align="left"  theme="simple"  type="button" cssClass="buttonleft" method="edit" value="Edit" onClick="return checkName()" /> 
					<s:submit align="right"  theme="simple" type="button" cssClass="buttonright"   value="Close" onClick="window.close()"/> 
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
