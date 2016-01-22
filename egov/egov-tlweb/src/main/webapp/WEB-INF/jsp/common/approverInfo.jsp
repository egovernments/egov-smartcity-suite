<%--
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
  --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script>

	function getUsersByDeisgnation(value) {
		populateapproverName({departmentId:document.getElementById("departmentid").value,designationId:document.getElementById("approverDesg").value});
	}

	function callAlertForDepartment() {
    	var value=document.getElementById("departmentid").value;
		if(value=="-1"){
			bootbox.alert("Please select a Approver Department");
			document.getElementById("departmentid").focus();
			return false;
		}
	}
	
	function callAlertForDesignation() {
		var value=document.getElementById("approverDesg").value;
		if(value=="") {
			bootbox.alert("Please select a Approver Designation");
			document.getElementById("approverDesg").focus();
			return false;
		}
	}
	
	function loadDesignationByDeptId() {
		  var designationObj =document.getElementById('approverDesg');
	 	  designationObj.options.length = 0;
	 	  designationObj.options[0] = new Option("----Choose----","");
	 	  populateapproverDesg({departmentId:document.getElementById("departmentid").value});	
	}	
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="bluebox" width="16%">
			&nbsp;
		</td>
		<td class="bluebox" id="deptLabel">
			<s:text name="workflow.approver.department" />
		</td>
		<td class="bluebox">
			<s:select name="departmentid" id="departmentid" list="dropdownData.departmentIdList" listKey="id" listValue="deptName" value="%{departmentId}" onchange="loadDesignationByDeptId();" />
			<egov:ajaxdropdown fields="['Text','Value']" url="transactions/ajaxTradeLicense-getDesignationByDeptId.action" id="approverDesg" dropdownId="approverDesg" />
		</td>
		<td class="bluebox" width="10%">
			&nbsp;
		</td>
		<td class="bluebox" width="14%">
			Approver Designation:
		</td>
		<td class="bluebox" width="33%">
			<s:select id="approverDesg" name="approverDesg" list="dropdownData.desgnationList" listKey="designationId" listValue="designationName" onchange="getUsersByDeisgnation(this.value)" onfocus="callAlertForDepartment();" />
			<egov:ajaxdropdown id="approverName" fields="['Text','Value']" dropdownId="approverName" url="transactions/ajaxTradeLicense-getUsersByPassingDesigId.action" />
		</td>
		<td class="bluebox" width="10%">
			Approver:
			<span class="mandatory1">*</span>
		</td>
		<td class="bluebox" width="16%">
			<s:select id="approverName" name="approverName" list="dropdownData.approverList" headerKey="-1" headerValue="----Choose----" listKey="id" listValue="userName" value="%{approverName}" />
		</td>
		<td class="bluebox" width="16%">
			&nbsp;
		</td>
	</tr>

</table>
