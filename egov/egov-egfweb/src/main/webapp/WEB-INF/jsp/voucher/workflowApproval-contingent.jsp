<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<div align="center">
	<br>
	<div class="subheadnew">Approval Information</div>
	<br>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="greybox" id="deptLabel">Approver Department<span
				class="mandatory1">*</span></td>
			<td class="greybox"><s:select name="departmentid"
					id="departmentid" list="dropdownData.departmentList" listKey="id"
					listValue="name" onchange="populateDesg()" /></td>
			<egov:ajaxdropdown id="designationId" fields="['Text','Value']"
				dropdownId="designationId" url="voucher/common-ajaxLoadDesg.action" />
			<td class="greybox">Approver Designation<span class="mandatory1">*</span></td>
			<td class="greybox"><s:select name="designationId"
					id="designationId" list="dropdownData.designationList"
					listKey="designationId" listValue="designationName" headerKey="-1"
					headerValue="----Choose----" value="designationId"
					onchange="populateUser()" /></td>
		</tr>

		<tr>
			<egov:ajaxdropdown id="approverUserId" fields="['Text','Value']"
				dropdownId="approverUserId" url="voucher/common-ajaxLoadUser.action" />
			<td class="bluebox" width="13%">Approver<span class="mandatory1">*</span></td>
			<td class="bluebox" width="33%"><s:select id="approverUserId"
					name="approverUserId" list="dropdownData.userList" headerKey="-1"
					headerValue="----Choose----" listKey="id" listValue="userName"
					value="id" /></td>

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
	 if( (value == 'Approve' || value=='Send for Approval' || value == 'Save & Forward' || value == 'Forward') && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
		document.getElementById('lblError').innerHTML ="Please Select the user";
		return false;
	}
</s:if>
	return true;
}

</script>
