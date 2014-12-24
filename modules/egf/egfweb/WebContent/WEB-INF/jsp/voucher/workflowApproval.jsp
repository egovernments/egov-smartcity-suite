<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<div align="center"  ><br>
	<div  class="subheadnew" >Approval Information</div><br>
	   <table width="100%" border="0" cellspacing="0" cellpadding="0" >
	  	 <tr>
			<td class="greybox" id="deptLabel">Approver Department<span class="mandatory">*</span></td>
			<td class="greybox"><s:select name="departmentid" id="departmentid" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="-1" 
			headerValue="----Choose----"  value="%{departmentId}"  onchange= "populateDesg()"/></td>
			 <egov:ajaxdropdown id="designationId"fields="['Text','Value']" dropdownId="designationId" url="voucher/common!ajaxLoadDesg.action" />
			<td class="greybox">Approver Designation<span class="mandatory">*</span></td>
			<td class="greybox"><s:select name="designationId" id="designationId" list="dropdownData.designationList"
			 listKey="designationId" listValue="designationName" headerKey="-1" headerValue="----Choose----"  value="designationId"
			 onchange= "populateUser()" /></td>
		</tr>
		
		 <tr>
		 <egov:ajaxdropdown id="approverUserId"fields="['Text','Value']" dropdownId="approverUserId" url="voucher/common!ajaxLoadUser.action" />
			 <td class="bluebox" width="13%">Approver<span class="mandatory">*</span></td>
			  <td class="bluebox" width="33%"><s:select id="approverUserId"  name="approverUserId" list="dropdownData.userList" headerKey="-1"
			  headerValue="----Choose----" listKey="id" listValue="userName" value="id"  /> 
			 </td>
			 	
			 </tr>	
		</table>
		<s:hidden name="type" id="type"></s:hidden>
		
	</div>
<script>
designationIdFailureHandler=function(){
}

function populateDesg(){
	if(null != document.getElementById("scriptName")){
		if(document.getElementById("billRegisterId"))
			populatedesignationId({departmentId:document.getElementById("departmentid").value,scriptName:document.getElementById("scriptName").value,billRegisterId:document.getElementById("billRegisterId").value})	
		else
			populatedesignationId({departmentId:document.getElementById("departmentid").value,scriptName:document.getElementById("scriptName").value})
	}
}

function populateUser(){
	
	var desgFuncry = document.getElementById("designationId").value;
	var array = desgFuncry.split("-");
	var functionary = array[1];
	var desgId = array[0];
	if(desgId==""){ // when user doesnot selects any value in the designation drop down.
		desgId=-1;
	}
	populateapproverUserId({departmentId:document.getElementById("departmentid").value,
	designationId:desgId,functionaryName:functionary})
		
}
function validateUser(name,value){
	document.getElementById("actionName").value= name;
	document.getElementById('lblError').innerHTML ="";
<s:if test="%{wfitemstate !='END'}">
	 if( (value == 'Approve' || value=='Send for Approval' || value == 'Forward') && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
		document.getElementById('lblError').innerHTML ="Please Select the user";
		return false;
	}
</s:if>
	return true;
}

</script>