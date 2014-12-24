<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<div align="center"  ><br>
	
	   <table width="100%" border="0" cellspacing="0" cellpadding="0" >
	  	 <tr>
			<td class="greyboxwk" id="deptLabel">Approver Department<span class="mandatory">*</span></td>
			<td class="greybox2wk"><s:select name="departmentid" id="departmentid" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="-1" 
			headerValue="----Choose----"  value="%{departmentId}"  onchange= "populateDesg()"/></td>
			 <egov:ajaxdropdown id="designationId"fields="['Text','Value']" dropdownId="designationId" url="serviceOrder/common!ajaxLoadDesg.action" />
			<td class="greyboxwk">Approver Designation<span class="mandatory">*</span></td>
			<td class="greybox2wk"><s:select name="designationId" id="designationId" list="dropdownData.designationList"
			 listKey="designationId" listValue="designationName" headerKey="-1" headerValue="----Choose----"  value="designationId"
			 onchange= "populateUser()" /></td>
		</tr>
		
		 <tr>
		 <egov:ajaxdropdown id="approverUserId"fields="['Text','Value']" dropdownId="approverUserId" url="serviceOrder/common!ajaxLoadUser.action" />
			 <td class="whiteboxwk" width="13%">Approver<span class="mandatory">*</span></td>
			  <td class="whitebox2wk" width="33%"><s:select id="approverUserId"  name="approverUserId" list="dropdownData.userList" headerKey="-1"
			  headerValue="----Choose----" listKey="id" listValue="userName" value="id"  /> 
			 </td>
			 	
			 </tr>	
		</table>
		<s:hidden name="actionName" id="actionName"></s:hidden>
		
	</div>
<script>
designationIdFailureHandler=function(){
}

function populateDesg(){
	populatedesignationId({departmentId:document.getElementById("departmentid").value})
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
	
	 if( (value='Save & Forward' || value == 'Approve' || value=='Send for Approval' || value == 'Forward') && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
		
		document.getElementById('jserrorid').style.display='block';
		document.getElementById('lblError').innerHTML ="Please Select the user";
		return false;
	}
</s:if>
	return true;
}

</script>