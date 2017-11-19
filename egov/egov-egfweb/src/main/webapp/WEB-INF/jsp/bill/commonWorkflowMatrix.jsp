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


<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script>
	function getUsersByDesignationAndDept() {
		populateapproverPositionId({
			approverDepartmentId : document
					.getElementById('approverDepartment').value,
			designationId : document.getElementById('approverDesignation').value
		});
	}

	function callAlertForDepartment() {
		var value = document.getElementById("approverDepartment").value;
		if (value == "-1") {
			bootbox.alert("Please select the Approver Department");
			document.getElementById("approverDepartment").focus();
			return false;
		}
	}

	function callAlertForDesignation() {
		var value = document.getElementById("approverDesignation").value;
		if (value == "-1") {
			bootbox.alert("Please select the approver designation");
			document.getElementById("approverDesignation").focus();
			return false;
		}
	}

	function loadDesignationByDeptAndType(typeValue, departmentValue,
			currentStateValue, amountRuleValue, additionalRuleValue,
			pendingActionsValue) {
		var designationObj = document.getElementById('approverDesignation');
		designationObj.options.length = 0;
		designationObj.options[0] = new Option("----Choose----", "-1");
		var approverObj = document.getElementById('approverPositionId');
		approverObj.options.length = 0;
		approverObj.options[0] = new Option("----Choose----", "-1");
		populateapproverDesignation({
			departmentRule : departmentValue,
			type : typeValue,
			amountRule : amountRuleValue,
			additionalRule : additionalRuleValue,
			currentState : currentStateValue,
			pendingAction : pendingActionsValue
		});
	}

	function loadDesignationFromMatrix() {
		var e = dom.get('approverDepartment');
		var dept = e.options[e.selectedIndex].text;
		var currentState = dom.get('currentState').value;
		var amountRule = dom.get('amountRule').value;
		var additionalRule = dom.get('additionalRule').value;
		var pendingAction = document.getElementById('pendingActions').value;
		var stateType = 'EgBillregister';
		loadDesignationByDeptAndType(stateType, dept, currentState, amountRule,
				additionalRule, pendingAction);
	}

	function populateApprover() {
		getUsersByDesignationAndDept();
	}

	function setDesignation() {
		document.getElementById("approverDesignation").value = '<s:property value="%{approverDesignation}"/>';
		populateApprover();
	}

	function setApprover() {
		document.getElementById("approverPositionId").value = '<s:property value="%{approverPositionId}"/>';
	}
</script>
<s:if test="%{getNextAction()!='END'}">
	<s:if test="%{getCurrentState()!='Closed'}">
		<s:hidden id="currentState" name="currentState"
			value="%{getCurrentState()}" />
	</s:if>
	<s:else>
		<s:hidden id="currentState" name="currentState" value="" />
	</s:else>
	<s:hidden id="currentDesignation" name="currentDesignation"
		value="%{currentDesignation}" />
	<s:hidden id="additionalRule" name="additionalRule"
		value="%{additionalRule}" />
	<s:hidden id="amountRule" name="amountRule" value="%{amountRule}" />
	<s:hidden id="workFlowDepartment" name="workFlowDepartment"
		value="%{workFlowDepartment}" />
	<s:hidden id="pendingActions" name="pendingActions"
		value="%{pendingActions}" />
	<s:hidden id="approverName" name="approverName" />

	<s:if test="%{#request.approverOddTextCss==null}">
		<c:set var="approverOddTextCss" value="greybox" scope="request" />
		<c:set var="approverOddCSS" value="greybox" scope="request" />
	</s:if>

	<s:if test="%{#request.approverEvenTextCSS==null}">
		<c:set var="approverEvenTextCSS" value="bluebox" scope="request" />
		<c:set var="approverEvenCSS" value="bluebox" scope="request" />
	</s:if>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<div class="headingsmallbg">
				<span class="bold"><s:text name="title.approval.information" /></span>
			</div>

		</tr>
		</br>
		<tr>
			<td class="${approverOddCSS}" width="5%">&nbsp;</td>
			<td class="${approverOddCSS}" id="deptLabel" width="14%"><s:text
					name="wf.approver.department" />:</td>
			<td class="${approverOddTextCss}" width="14%"><s:select
					name="approverDepartment" id="approverDepartment"
					list="dropdownData.approverDepartmentList" listKey="id"
					listValue="name" headerKey="-1" headerValue="----Choose----"
					value="%{approverDepartment}"
					onchange="loadDesignationFromMatrix();" cssClass="dropDownCss" />
				<egov:ajaxdropdown fields="['Text','Value']"
					url="workflow/ajaxWorkFlow-getDesignationsByObjectType.action"
					id="approverDesignation" dropdownId="approverDesignation"
					contextToBeUsed="/eis" afterSuccess="setDesignation();" /></td>
			<td class="${approverOddCSS}" width="14%"><s:text
					name="wf.approver.designation" />:</td>
			<td class="${approverOddTextCss}" width="14%"><s:select
					id="approverDesignation" name="approverDesignation"
					list="dropdownData.designationList" listKey="designationId"
					headerKey="-1" listValue="designationName"
					headerValue="----Choose----" onchange="populateApprover();"
					onfocus="callAlertForDepartment();" cssClass="dropDownCss" /> <egov:ajaxdropdown
					id="approverPositionId" fields="['Text','Value']"
					dropdownId="approverPositionId"
					url="workflow/ajaxWorkFlow-getPositionByPassingDesigId.action"
					contextToBeUsed="/eis" afterSuccess="setApprover();" /></td>
			<td class="${approverOddCSS}" width="14%"><s:text
					name="wf.approver" />:</td>
			<td class="${approverOddTextCss}" width="14%"><s:select
					id="approverPositionId" name="approverPositionId"
					list="dropdownData.approverList" headerKey="-1"
					headerValue="----Choose----" listKey="id" listValue="firstName"
					onfocus="callAlertForDesignation();" value="%{approverPositionId}"
					cssClass="dropDownCss" /></td>
			<td class="${approverOddCSS}" width="5%">&nbsp;</td>
		</tr>
	</table>
</s:if>
<br />

<div id="workflowCommentsDiv" align="center">
	<table width="100%">
		<tr>
			<td width="10%" class="${approverEvenCSS}">&nbsp;</td>
			<td width="20%" class="${approverEvenCSS}">&nbsp;</td>
			<td class="${approverEvenCSS}" width="13%"><s:text
					name="wf.approver.remarks" />:</td>
			<td class="${approverEvenTextCSS}"><textarea
					id="approverComments" name="approverComments" rows="2" cols="35" onMouseUp="setReadonly()"></textarea>
			</td>
			<td class="${approverEvenCSS}">&nbsp;</td>
			<td width="10%" class="${approverEvenCSS}">&nbsp;</td>
			<td class="${approverEvenCSS}">&nbsp;</td>
		</tr>
	</table>
</div>
<script type="text/javascript">

function setReadonly()
{
var text = document.getElementById('approverComments');
text.removeAttribute('readonly');
}
</script>

