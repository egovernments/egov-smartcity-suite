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

<%@ include file="/includes/taglibs.jsp"%>
<div align="center">
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="5" width="5%">
				<div class="headingsmallbg">
					<span class="bold"><s:text name='approval.details.title' />
					</span>
				</div>
			</td>
		</tr>
		<tr>
			<td class="bluebox" width="6%">
				&nbsp;
			</td>
			<td class="bluebox" width="10%">
				<s:text name='approver.department' />
				<span class="mandatory1">*</span>
			</td>
			<td class="bluebox" width="8%">
				<s:select name="workflowBean.departmentId" id="departmentId"
					list="workflowBean.departmentList" listKey="id"
					listValue="name" headerKey="-1" headerValue="----Choose----"
					value="%{workflowBean.departmentId}"
					onchange="populateDesignations()" />
			</td>
			<egov:ajaxdropdown id="designationId" fields="['Text','Value']"
				dropdownId="designationId"
				url="common/ajaxCommon-populateDesignationsByDept.action" />
			<td class="bluebox" width="8%">
				<s:text name='approver.designation' />
				<span class="mandatory1">*</span>
			</td>
			<td class="bluebox" width="15%">
				<s:select name="workflowBean.designationId" id="designationId"
					list="workflowBean.designationList" listKey="designationId"
					listValue="designationName" headerKey="-1"
					headerValue="----Choose----" value="%{workflowBean.designationId}"
					onchange="populateUser()" />
			</td>
		</tr>
		<tr>
			<td class="greybox" width="5%">
				&nbsp;
			</td>
			<td class="greybox" width="10%">
				<s:text name='approver.position' />
				<span class="mandatory1">*</span>
			</td>
			<td class="greybox" width="15%">
				<s:select id="approverUserId" name="workflowBean.approverUserId"
					list="workflowBean.appoverUserList" headerKey="-1"
					headerValue="----Choose----" listKey="id" listValue="userName"
					value="%{workflowBean.approverUserId}" />
				<egov:ajaxdropdown id="approverUserId" fields="['Text','Value']"
					dropdownId="approverUserId"
					url="common/ajaxCommon-populateUsersByDeptAndDesignation.action" />
			</td>
			<td class="greybox" colspan="2" width="20%">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="bluebox" width="6%">
				&nbsp;
			</td>
			<td class="bluebox" width="10%">
				<s:text name='approver.comments' />
			</td>
			<td class="bluebox" width="8%">
				<s:textarea name="workflowBean.comments" id="comments" rows="3"
					cols="80" onblur="checkLength(this);" />
			</td>
			<td class="bluebox" width="15%" colspan="2"></td>
		</tr> 
		<s:hidden name="workflowBean.actionName" id="workflowBean.actionName" /> 
	</table>
</div>

<script>
	designationIdFailureHandler = function() {
	}

	function populateDesignations() {
		populatedesignationId( {
			departmentId : document.getElementById("departmentId").value
		})
	}
	function populateUser() {
		populateapproverUserId( {
			designationId : document.getElementById("designationId").value,
			departmentId : document.getElementById("departmentId").value
		})
	}
</script>