<script language="JavaScript"  type="text/JavaScript">
	
	function populateApproverEmp(){		
			var deptId = document.getElementById('approverDept').value;
			var desigId = document.getElementById('approverDesig').value;

			if(deptId!='' && desigId!=''){
				populateapproverEmpPositionId({approverDeptId:deptId,approverDesigId:desigId});
			}
	}
	
	function validateForMandatory(){				
		if(document.getElementById('approverDept').value == ""){
			alert("Select approver department");
			document.getElementById('approverDept').focus();
			return "false";
		}
		if(document.getElementById('approverDesig').value == ""){
			alert("Select approver designation");
			document.getElementById('approverDesig').focus();
			return "false";
		}
		if(document.getElementById('approverEmpPositionId').value == ""){
			alert("Select approver employee");
			document.getElementById('approverEmpPositionId').focus();
			return "false";
		}		
	}


</script>

<table width="100%" cellpadding ="0" cellspacing ="0" border = "0" >
    <tr>
		<td colspan="4" class="headingwk">
			<div class="arrowiconwk"><img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
            <div class="headplacer">Select Approver</div>
		</td>
     </tr>
   <tr>
    <td class="whiteboxwk" >Department<span class="mandatory">*</span></td>
    <td class="whitebox2wk">
    	<s:select name="approverDept" id="approverDept" list="dropdownData.departmentList" listKey="id" listValue="deptName" 
    								headerKey="" headerValue="------Select------" onblur="populateApproverEmp();"/>
	</td>
	<td class="whiteboxwk" >Designation<span class="mandatory">*</span></td>
    <td class="whitebox2wk">
    	<s:select name="approverDesig" id="approverDesig" list="dropdownData.designationList" listKey="designationId" listValue="designationName" 
    															headerKey="" headerValue="------Select------" onblur="populateApproverEmp();"/>
	</td>
  </tr>  
   <tr>
    <td class="whiteboxwk" >Employee<span class="mandatory">*</span></td>
    <td class="whitebox2wk">
    	<egovtags:ajaxdropdown id="employeeDropdown" fields="['Text','Value']" dropdownId="approverEmpPositionId" url="common/employeeSearch!approverEmpList.action"/>
		    	
		<s:select name="approverEmpPositionId" id="approverEmpPositionId" list="dropdownData.approverEmpList" listKey="id" listValue="employeeName" 
    																				headerKey="" headerValue="------Select------"/>
    	
	</td> 
  </tr>
  
  </table>  	