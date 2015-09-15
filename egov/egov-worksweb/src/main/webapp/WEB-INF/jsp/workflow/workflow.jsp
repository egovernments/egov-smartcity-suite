<!-- -------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It
# 	   is required that all modified versions of this material be marked in
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program
# 	   with regards to rights under trademark law for use of the trade names
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<div class="errorstyle" id="approver_error" style="display:none;"></div>
	<div id="workflowDetials">	      
	   <table width="100%" border="0" cellspacing="0" cellpadding="0" >
		<tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="/egi/resources/erp2/images/arrow.gif" /></div>
                <div class="headplacer"><s:text name="workflow.header" />:</div></td>
                </tr>
	  	 <tr>
	  	 	<td class="greyboxwk" id="deptLabel"><s:text name="workflow.approver.department"/></td>
			<td class="greybox2wk"><s:select name="workflowDepartmentId" id="departmentid" list="dropdownData.executingDepartmentList" listKey="id" listValue="deptName" headerKey="-1" 
			headerValue="%{getText('estimate.default.select')}"  value="%{workflowDepartmentId}"  onchange= "populateDesignation()"/>
			<egov:ajaxdropdown id="designationId"fields="['Text','Value']" dropdownId="designationId" url="workflow/ajaxWorkflow!getDesgByDeptAndType.action" />
			</td>
			<td class="greyboxwk"><span class="mandatory">*</span><s:text name="workflow.approver.designation"/></td>
			<td class="greybox2wk"><s:select name="workflowDesignationId" id="designationId" list="{}"
			 listKey="designationId" listValue="designationName" headerKey="-1" headerValue="%{getText('estimate.default.select')}"  value="%{workflowDesignationId}" onchange= "populateUser()" />
			<egov:ajaxdropdown id="approverUserId"fields="['Text','Value']" dropdownId="approverUserId" url="workflow/ajaxWorkflow!getWorkFlowUsers.action" />
			</td>
		</tr>
		<tr>		 
			 <td class="whiteboxwk" width="13%"><span class="mandatory">*</span><s:text name="workflow.approver"/></td>
			  <td class="whitebox2wk" width="33%" colspan="3"><s:select id="approverUserId"  name="workflowApproverUserId" list="{}" headerKey="-1" 
			  headerValue="%{getText('estimate.default.select')}" listKey="id" listValue="firstName" value="%{workflowApproverUserId}"  />
			 </td>
		</tr>
		</table>
		</div>
		<tr  align="center">
            		<td>
            		<div id="approverCommentsRow" style="display:none;">
		          <table id="commentsTable" width="100%" border="0" cellspacing="0" cellpadding="0">
			      <tr>
		               <td width="11%" class="greyboxwk"><s:text name="workflow.approver.comments" />:</td>
		               <td width="53%" class="greybox2wk" colspan="3"><s:textarea name="workflowapproverComments" cols="80" cssClass="selectwk" id="approverComments" value="%{approverComments}"/></td>
		             </tr>
				  </table>
          			</div>
          		</td>
          	</tr>

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
