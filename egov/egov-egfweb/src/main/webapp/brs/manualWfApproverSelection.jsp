<!--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  -->
<script language="JavaScript"  type="text/JavaScript">
	
	function populateApproverEmp(){		
		var deptId = document.getElementById('approverDept').value;
		var desigId = document.getElementById('approverDesig').value;
		if(deptId!='' && desigId!=''){
			populateapproverEmpAssignmentId({departmentId:deptId,designationId:desigId,functionaryName:'UAC'});
		}
	}

	function validateForMandatory(){
		if(document.getElementById('approverDept').value == ""){
			bootbox.alert("Select approver department");
			document.getElementById('approverDept').focus();
			return "false";
		}
		if(document.getElementById('approverDesig').value == ""){
			bootbox.alert("Select approver designation");
			document.getElementById('approverDesig').focus();
			return "false";
		}
		if(document.getElementById('approverEmpAssignmentId').value == ""){
			bootbox.alert("Select approver employee");
			document.getElementById('approverEmpAssignmentId').focus();
			return "false";
		}	
		return "true";
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
    	<html:select property="approverDept" styleId="approverDept" styleClass="selectwk" >
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
    	<egovtags:ajaxdropdown id="approverEmpAssignmentId" fields="['Text','Value']" dropdownId="approverEmpAssignmentId" url="voucher/common!ajaxLoadUser.action"/>
		    	
		
    	<html:select property="approverEmpAssignmentId" styleId="approverEmpAssignmentId" styleClass="selectwk" style="width:201px">
			<html:option value="">---------------choose----------------</html:option>			
		</html:select>
	</td> 
  </tr>
  
  </table>  	
