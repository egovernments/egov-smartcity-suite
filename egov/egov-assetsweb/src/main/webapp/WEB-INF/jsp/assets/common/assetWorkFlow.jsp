<%@ taglib prefix="s" uri="/struts-tags"%>
	
	   <table width="100%" border="0" cellspacing="0" cellpadding="0" >
	  	 <tr>
			<td class="greyboxwk" id="deptLabel">Approver Department<span class="mandatory">*</span></td>
			<td class="greybox2wk"><s:select name="wfDepartmentId" id="wfDepartmentId" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="-1" 
			headerValue="----Choose----"  value="%{wfDepartmentId}"  onchange= "populateDesg()"/></td>
			
			<td class="greyboxwk">Approver Designation<span class="mandatory">*</span></td>
			<td class="greybox2wk"><s:select name="wfDesignationId" id="wfDesignationId" list="dropdownData.designationList"
			 listKey="designationId" listValue="designationName" headerKey="-1" headerValue="----Choose----"  value="%{wfDesignationId}"
			 onchange= "populateUser()" /></td>
		</tr>
		
		 <tr>
		 	 <egov:ajaxdropdown id="wfUserId"fields="['Text','Value']" dropdownId="wfUserId" url="/assetmaster/ajaxAsset!ajaxLoadUser.action" />
			 <td class="whiteboxwk" width="13%">Approver<span class="mandatory">*</span></td>
			  <td class="whitebox2wk" width="33%"><s:select id="wfUserId"  name="wfUserId" list="dropdownData.userList" headerKey="-1"
			  headerValue="----Choose----" listKey="id" listValue="userName" value="%{wfUserId}"  /> 
			 </td>
			 	
			 </tr>	
		</table>
<script>
function populateUser(){
	
	var desgFuncry = document.getElementById("wfDesignationId").value;
	var array = desgFuncry.split("-");
	var functionary = array[1];
	var desgId = array[0];
	if(desgId==""){ // when user doesnot selects any value in the designation drop down.
		desgId=-1;
	}
	populatewfUserId({departmentId:document.getElementById("wfDepartmentId").value,
	designationId:desgId,functionaryName:functionary})
		
}
function validateUser(name,value){
<s:if test="%{wfitemstate !='END'}">
	 if( (value=='Save & Forward' || value == 'Forward') && null != document.getElementById("wfUserId") && document.getElementById("wfUserId").value == -1){
		return false;
	}
</s:if>
	return true;
}
</script>