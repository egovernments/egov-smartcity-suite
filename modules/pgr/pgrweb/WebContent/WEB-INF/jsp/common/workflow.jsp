<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

	
	  
	  	 <tr>
			<td class="whiteboxwk" id="deptLabel">Approver Department<span class="mandatory">*</span></td>
			<td class="whitebox2wk"><s:select name="departmentid" id="departmentid" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="-1" 
			headerValue="----Choose----"  value="%{departmentId}"  onchange= "populateDesg()"/></td>
			 <egov:ajaxdropdown id="designationId"fields="['Text','Value']" dropdownId="designationId" url="common/ajaxPgr!ajaxLoadDesg.action" />
			<td class="whiteboxwk">Approver Designation<span class="mandatory">*</span></td>
			<td class="whitebox2wk"><s:select name="designationId" id="designationId" list="dropdownData.designationList"
			 listKey="designationId" listValue="designationName" headerKey="-1" headerValue="----Choose----"  value="designationId"
			 onchange= "populateUser()" /></td>
		</tr>
		
		 <tr>
		 <egov:ajaxdropdown id="approverUserId"fields="['Text','Value']" dropdownId="approverUserId" url="common/ajaxPgr!ajaxLoadUser.action" />
			 <td class="greyboxwk" width="13%">Approver<span class="mandatory">*</span></td>
			  <td class="greybox2wk" width="33%"><s:select id="approverUserId"  name="approverUserId" list="dropdownData.userList" headerKey="-1"
			  headerValue="----Choose----" listKey="id" listValue="userName" value="id" onChange="validateButton(this);" /> 
			 </td><td class="greyboxwk"></td> <td class="greybox2wk"></td>
			 	
		
<script>


function populateDesg(){
	
	populatedesignationId({departmentId:document.getElementById("departmentid").value});
}

function populateUser(){

	populateapproverUserId({departmentId:document.getElementById("departmentid").value, 
	designationId:document.getElementById("designationId").value});
		
}

function validateButton(obj){
	if(null != dom.get('forward') && obj.value != -1){
		dom.get('forward').disabled = false;
	}
	
}
</script>