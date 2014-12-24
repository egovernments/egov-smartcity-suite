<%@ include file="/includes/taglibs.jsp" %>

<html>
<head>
	<title>Complaint Receiving Center Index</title>
	<script type="text/javascript">
		
		 function checkName() {
		  	var name = document.getElementById("receivingCenterId").value;
			if( name == null ||  name == "" || name == 0)
			{
				alert("please select Complaint Receiving Center name");
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
					Complaint Receiving Center List
					</td>
				</tr>
				<tr>	
					<td >&nbsp;</td>
				</tr>	
				<tr>
					<td >
					<s:actionerror/>
					<s:form action="complaintReceivingCenterMaster.action" >
					<s:select list="complaintReceivingCenterMap" label="%{getText('Masters.complaintreceivingcenter.name')}" required="true" name="receivingCenterId" id="receivingCenterId" listkey="receivingCenterId" listvalue="receivingCenterName" headerKey="0"   headerValue="%{getText('Please.Choose')}"  ></s:select>
					</td>
				</tr>
				<tr>
					<td class="buttonareanew" colspan="2">	
					<s:submit align="left"  theme="simple"  type="button" cssClass="buttonleft" method="edit" value="Edit" onClick="return checkName()"/> 
					<s:submit align="right"  theme="simple" type="button" cssClass="buttonright"  onClick="window.close()" value="Close"/> 
					</s:form>
					</td>
				</tr>
				<tr>	
					<td >&nbsp;</td>
				</tr>
			</div>
			</div>
			</div>
			</td>
		</tr>
</table>
</body>
</html>
