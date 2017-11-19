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

<div class="alert alert-danger" id="approver_error" style="display:none;"></div>

	
	

	<div id="workflowDetials" class="panel panel-primary" data-collapsed="0" style="text-align:left">
			<div class="panel-heading">
				<div class="panel-title"><s:text name="workflow.header" /></div>
			</div>
			<div class="panel-body no-margin-bottom">
				<div class="row show-row" id="approverDetailHeading">
				<div class="show-row form-group">
					<label class="col-sm-2 control-label text-right"><s:text name="workflow.approver.department"/><span class="mandatory"></span></label>
					<div class="col-sm-3 add-margin">
						<s:select name="workflowDepartmentId" cssClass="form-control" id="departmentid" list="dropdownData.executingDepartmentList" listKey="id" listValue="deptName" headerKey="-1" 
						headerValue="%{getText('estimate.default.select')}"  value="%{workflowDepartmentId}"  onchange= "populateDesignation()"/>
						<egov:ajaxdropdown id="designationId"fields="['Text','Value']" dropdownId="designationId" url="workflow/ajaxWorkflow!getDesgByDeptAndType.action" />
					</div>
					<label class="col-sm-2 control-label text-right"><s:text name="workflow.approver.designation"/><span class="mandatory"></span></label>
					<div class="col-sm-3 add-margin">
						<s:select name="workflowDesignationId" cssClass="form-control" id="designationId" list="{}"
						 listKey="designationId" listValue="designationName" headerKey="-1" headerValue="%{getText('estimate.default.select')}"  value="%{workflowDesignationId}" onchange= "populateUser()" />
						<egov:ajaxdropdown id="approverUserId"fields="['Text','Value']" dropdownId="approverUserId" url="workflow/ajaxWorkflow!getWorkFlowUsers.action" />					
					</div>
				</div>
				<div class="show-row form-group">
					<label class="col-sm-2 control-label text-right"><s:text name="workflow.approver"/><span class="mandatory"></span></label>
					<div class="col-sm-3 add-margin">
						<s:select id="approverUserId" cssClass="form-control" name="workflowApproverUserId" list="{}" headerKey="-1" 
			  			headerValue="%{getText('estimate.default.select')}" listKey="id" listValue="firstName" value="%{workflowApproverUserId}"  />		
					</div> 
				</div>
				</div>
				
				<div class="row" id="approverCommentsRow" style="display:none;">
					<label class="col-sm-2 control-label text-right"><s:text name="workflow.approver.comments" /></label>
					<div class="col-sm-8 add-margin">
						<s:textarea name="workflowapproverComments" cols="80" cssClass="form-control" id="approverComments" value="%{approverComments}"/>
					</div>
				</div>
			</div>
	</div>

<script>
function populateDesignation(){	
	populatedesignationId({workflowDepartmentId:document.getElementById("departmentid").value,objectId:'<s:property value='%{model.id}'/>',
	scriptName:dom.get('scriptName').value})
}

function populateUser(){
	var wardId='';
	var funcId='';
	if(document.getElementById("workflowWardId")!=null)
		wardId=document.getElementById("workflowWardId").value;
	if(document.getElementById("workflowFunctionaryId")!=null)
		funcId=document.getElementById("workflowFunctionaryId").value;		
	if(funcId!=''&& !isNaN(funcId))
		populateapproverUserId({workflowDepartmentId:document.getElementById("departmentid").value,
		workflowDesignationId:document.getElementById("designationId").value,workflowWardId:wardId,workflowFunctionaryId:funcId});
	else
		populateapproverUserId({workflowDepartmentId:document.getElementById("departmentid").value,
		workflowDesignationId:document.getElementById("designationId").value,workflowWardId:wardId});
}

function validateUser(name){
	document.getElementById('approver_error').style.display ='none';
	if(name != 'reject' && name != 'cancel' && name!='save') {
	 	<s:if test="%{model.currentState==null || (model.currentState.nextAction!='Pending Admin Sanction' && model.currentState.nextAction!='Pending for Approval')}" >
		if(null != document.getElementById("designationId") && document.getElementById("designationId").value == -1){
			document.getElementById('approver_error').style.display ='';	
			document.getElementById('approver_error').innerHTML ="";
			document.getElementById('approver_error').innerHTML ='<s:text name="workflow.approver.designation.null"/>';
			return false;
		}
		if(null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
			document.getElementById('approver_error').style.display ='';	
			document.getElementById('approver_error').innerHTML ="";
			document.getElementById('approver_error').innerHTML ='<s:text name="workflow.approver.null"/>';
			return false;
		}
	 	</s:if>
	}
	return true; 
}
</script>
