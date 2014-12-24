<script language="JavaScript"  type="text/JavaScript">
	
	function populateApproverEmp(){		
		var deptId = document.getElementById('approverDept').value;
		var desigId = document.getElementById('approverDesig').value;
		if(deptId!='' && desigId!=''){
			populateapproverEmpAssignmentId({approverDeptId:deptId,approverDesigId:desigId});
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
		if(document.getElementById('approverEmpAssignmentId').value == ""){
			alert("Select approver employee");
			document.getElementById('approverEmpAssignmentId').focus();
			return "false";
		}	
	}

</script>

<table width="100%" cellpadding ="0" cellspacing ="0" border = "0" >
    <tr>
		<td colspan="4" class="headingwk">
			<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
            <div class="headplacer">Select Approver</div>
		</td>
     </tr>
   <tr>
    <td class="whiteboxwk" >Department<span class="mandatory">*</span></td>
    <td class="whitebox2wk">
    	<html:select property="approverDept" styleId="approverDept" styleClass="selectwk" onblur="populateApproverEmp();">
			<html:option value="">--------------choose---------------</html:option>
			<c:forEach var="department" items="${departmentList}">
				<html:option value="${department.id}" >${department.deptName}</html:option>
			</c:forEach>
		</html:select>
	</td>
	<td class="whiteboxwk" >Designation<span class="mandatory">*</span></td>
    <td class="whitebox2wk">
    	<html:select property="approverDesig" styleId="approverDesig" styleClass="selectwk" onblur="populateApproverEmp();">
			<html:option value="">-------------choose---------------</html:option>
			<c:forEach var="designation" items="${designationList}">
				<html:option value="${designation.designationId}" >${designation.designationName}</html:option>
			</c:forEach>
		</html:select>
	</td>
  </tr>  
   <tr>
    <td class="whiteboxwk" >Employee<span class="mandatory">*</span></td>
    <td class="whitebox2wk">
    	<egovtags:ajaxdropdown id="employeeDropdown" fields="['Text','Value']" dropdownId="approverEmpAssignmentId" url="common/employeeSearch!approverEmpList.action"/>
		    	
		
    	<html:select property="approverEmpAssignmentId" styleId="approverEmpAssignmentId" styleClass="selectwk" style="width:201px">
			<html:option value="">---------------choose----------------</html:option>			
		</html:select>
	</td> 
  </tr>
  
  </table>  	