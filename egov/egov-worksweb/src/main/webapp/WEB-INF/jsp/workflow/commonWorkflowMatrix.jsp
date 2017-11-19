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

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script>

function getUsersByDesignationAndDept() {
	populateapproverPositionId({approverDepartmentId:document.getElementById('approverDepartment').value,designationId:document.getElementById('approverDesignation').value});
}

function callAlertForDepartment() {
    var value=document.getElementById("approverDepartment").value;
	if(value=="-1") {
		alert("Please select the Approver Department");
		document.getElementById("approverDepartment").focus();
		return false;
	}
}

function callAlertForDesignation() {
	var value=document.getElementById("approverDesignation").value;
	if(value=="-1") {
		alert("Please select the approver designation");
		document.getElementById("approverDesignation").focus();
		return false;
	}
}
	
function loadDesignationByDeptAndType(typeValue,departmentValue,currentStateValue,amountRuleValue,additionalRuleValue,pendingActionsValue) {
	  var designationObj =document.getElementById('approverDesignation');
	  designationObj.options.length = 0;
	  designationObj.options[0] = new Option("----Choose----","-1");
	  var approverObj = document.getElementById('approverPositionId');
	  approverObj.options.length = 0;
	  approverObj.options[0] = new Option("----Choose----","-1");
	  populateapproverDesignation({departmentRule:departmentValue,type:typeValue,amountRule:amountRuleValue,additionalRule:additionalRuleValue,
	  													currentState:currentStateValue,pendingAction:pendingActionsValue});
}

function loadDesignationFromMatrix() {
	var e = dom.get('approverDepartment');
	var dept = e.options[e.selectedIndex].text;
	var currentState = dom.get('currentState').value;
	var amountRule = dom.get('amountRule').value;
	var additionalRule = dom.get('additionalRule').value;
	var pendingAction = document.getElementById('pendingActions').value;
	var stateType = '<s:property value="%{stateType}"/>';
	loadDesignationByDeptAndType(stateType,dept,currentState,amountRule,additionalRule,pendingAction); 
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
<s:if test="%{!'Closed'.equalsIgnoreCase(state.value)}">
	<s:hidden id="currentState" name="currentState" value="%{state.value}"/>
</s:if>
<s:else>
	<s:hidden id="currentState" name="currentState" value=""/>
</s:else>
<s:hidden id="currentDesignation" name="currentDesignation" value="%{currentDesignation}"/>
<s:hidden id="additionalRule" name="additionalRule" value="%{additionalRule}"/>
<s:hidden id="amountRule" name="amountRule" value="%{amountRule}"/>
<s:hidden id="workFlowDepartment" name="workFlowDepartment" value="%{workFlowDepartment}"/>
<s:hidden id="pendingActions" name="pendingActions" value="%{pendingActions}"/>
<s:hidden id="approverName" name="approverName" />

<div class="panel panel-primary" data-collapsed="0" style="text-align:left">
	<div class="panel-heading">
		<div class="panel-title">
		   <s:text name="title.approval.information"/>
		</div>
	</div>
	<div class="panel-body">
	   
	  <div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <s:text name="wf.approver.department"/><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<s:select name="approverDepartment" id="approverDepartment" list="dropdownData.approverDepartmentList" 
					listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  
					value="%{approverDepartment}"  onchange="loadDesignationFromMatrix();"
					cssClass="form-control" />
				<egov:ajaxdropdown fields="['Text','Value']" url="workflow/ajaxWorkFlow-getDesignationsByObjectType.action" id="approverDesignation" dropdownId="approverDesignation" 
					contextToBeUsed="/eis" afterSuccess="setDesignation();"/> 
			</div>
			<label class="col-sm-2 control-label text-right">
			    <s:text name="wf.approver.designation"/><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<s:select id="approverDesignation" name="approverDesignation" list="dropdownData.designationList" listKey="designationId" headerKey="-1" listValue="designationName" headerValue="----Choose----" 
					onchange="populateApprover();" onfocus="callAlertForDepartment();" cssClass="form-control" />
				<egov:ajaxdropdown id="approverPositionId" fields="['Text','Value']" dropdownId="approverPositionId" 
					url="workflow/ajaxWorkFlow-getPositionByPassingDesigId.action" contextToBeUsed="/eis" afterSuccess="setApprover();"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <s:text name="wf.approver"/><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<s:select id="approverPositionId"  name="approverPositionId" list="dropdownData.approverList" headerKey="-1" headerValue="----Choose----" listKey="id" listValue="firstName"  onfocus="callAlertForDesignation();" 
			  			value="%{approverPositionId}" cssClass="form-control" />
			</div>
		</div>	
		
		<div class="form-group"> 
			<label class="col-sm-2 control-label text-right">
			    <s:text name="wf.approver.remarks"/>
			</label>
			<div class="col-sm-8 add-margin">
				<textarea id="approverComments" name="approverComments" rows="2" cols="35" class="form-control"></textarea> 
			</div>
		</div>	
     </div>
  </div> 
</s:if>	
<s:else>
		<div class="form-group"> 
			<label class="col-sm-2 control-label text-right">
			    <s:text name="wf.approver.remarks"/>
			</label>
			<div class="col-sm-8 add-margin">
				<textarea id="approverComments" name="approverComments" rows="2" cols="35" class="form-control"></textarea> 
			</div>
		</div>
</s:else>