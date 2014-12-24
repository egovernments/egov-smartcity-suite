<%@ include file="/includes/taglibs.jsp" %>


<html>
<head >
	<title>Complaint Type Index</title>
	<script type="text/javascript">
	
	 function checkName() {
	  	var CGname = document.getElementById("model.ComplaintGroupId").value;
		if( CGname == null ||  CGname == "" || CGname == 0)
		{
			alert("please select Complaint Group name");
			return ;
		}
		var CTname = document.getElementById("model.complaintTypeId").value;
		if( CTname == null ||  CTname == "" || CTname == 0)
		{
			alert("please select Complaint Type name");
			return false;
		}
	}
	function getSelected(){
		var CompGroupId=document.getElementById("model.ComplaintGroupId").value;
		//alert(CompGroupId);
			loadSelectData('/pgr/commonyui/egov/loadComboAjax.jsp', 'eggr_complainttypes', 'COMPLAINTTYPEID', 'COMPLAINTTYPENAME', 'COMPLAINTGROUP_ID='+CompGroupId, 'model.ComplaintGroupId', 'model.complaintTypeId');
	}
	</script>
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
			Edit Complaint Type 
			</td>
		</tr>
		<tr>	
			<td >&nbsp;</td>
		</tr>		
		<tr>
			<td>
			<s:actionerror/>
			<s:form action="complaintTypeMaster.action"  >
			<s:select list="complaintGpMap" label="%{getText('Masters.complaintgroup')}" key="complantGroup_Id"  value="complaintGroupName"  headerKey="0"   headerValue="-------please Select-------" name="model.ComplaintGroupId" id="model.ComplaintGroupId" onchange="getSelected();"></s:select >
			
			
			<s:select list="complaintTypeNamesMap" label="%{getText('Masters.complainttype.name')}" required="true" name="complaintTypeId" id="model.complaintTypeId" listkey="complaintTypeId" listvalue="complaintTypeName" headerKey="0"   headerValue="-------please Select-------"  ></s:select>
			

			
			<s:hidden key="model.is_Active" name="model.is_Active" value="model.is_Active" />
	  		</td>
	  	</tr>
	  	<tr>
			<td class="buttonareanew" colspan="2">	
			<s:submit align="left"  theme="simple"   cssClass="buttonleft" method="edit" value="Edit" onClick="return checkName()"/> 
			<s:submit align="right"  theme="simple"  cssClass="buttonright"  onClick="window.close()" value="Close"/> 
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
