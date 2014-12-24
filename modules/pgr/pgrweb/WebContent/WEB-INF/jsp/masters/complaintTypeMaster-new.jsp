<%@ include file="/includes/taglibs.jsp" %>
<html>
<head >
	<title>Complaint Type </title>
	<script type="text/javascript">
		function uniquecheck(){
		var str=document.getElementById("model.ComplaintTypeCode").value;
		uniqueChecking('/pgr/commonyui/egov/uniqueCheckAjax.jsp','eggr_complainttypes','COMPLAINT_TYPE_CODE','model.ComplaintTypeCode','false','true');
	}
	</script>
</head>
<body >
	<table align="center" >
			<tr>
				<td>
				<div id="main">
				<div id="m2">
				<div id="m3">
				<table class="tableStylenew">
				 	<tr>
				 		<td  class="tableheadernew" >
				 		Create Complaint Type 
				 		</td>
				 	</tr>
				 	<tr>	
				 		<td >&nbsp;</td>
					</tr>
					<tr>
						<td >
						<s:actionerror/>
						<s:form action="complaintTypeMaster.action"  >
						<%@ include file='complaintTypeMaster-form.jsp'%>
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
						<s:submit align="left"  theme="simple"  type="button" cssClass="buttonleft" method="create" value="Add"/> 
						<s:submit align="right"  theme="simple" type="button" cssClass="buttonright"  onClick="window.close()" value="Close"/> 
						</s:form>
						</td>
					</tr>
					
				</div>
				</div>
				</div>
		 		</td>
		 	</tr>
	</table>
	
</body>

</html>
