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

<div class="rbcontent2">
	<div class="errorstyle" id="approver_error" style="display:none;"></div>	      
	   <table width="100%" border="0" cellspacing="0" cellpadding="0" >
		<tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div>
                <div class="headplacer"><s:text name="workflow.header" />:</div></td>
                </tr>
	  	 <tr>
			<td class="greyboxwk" id="deptLabel"><s:text name="workflow.approver.department"/></td>
			<td class="greybox2wk"><s:select name="departmentid" id="departmentid" list="dropdownData.executingDepartmentList" listKey="id" listValue="name" headerKey="-1" 
			headerValue="%{getText('estimate.default.select')}"  value="%{departmentId}"  onchange= "populateDesignation()"/>
			<egov:ajaxdropdown id="designationId"fields="['Text','Value']" dropdownId="designationId" url="estimate/ajaxEstimate!getDesgByDeptAndType.action" />
			</td>
			
			<td class="greyboxwk"><span class="mandatory">*</span><s:text name="workflow.approver.designation"/></td>
			<td class="greybox2wk"><s:select name="designationId" id="designationId" list="#{}"
			 listKey="designationId" listValue="designationName" headerKey="-1" headerValue="%{getText('estimate.default.select')}"  value="designationId" onchange= "populateUser()" />
			<egov:ajaxdropdown id="approverUserId"fields="['Text','Value']" dropdownId="approverUserId" url="estimate/ajaxEstimate!getWorkFlowUsers.action" />
			</td>
		</tr>
		
		 <tr>		 
			 <td class="whiteboxwk" width="13%"><span class="mandatory">*</span><s:text name="workflow.approver"/></td>
			  <td class="whitebox2wk" width="33%" colspan="3"><s:select id="approverUserId"  name="approverUserId" list="#{}" headerKey="-1" headerValue="%{getText('estimate.default.select')}" listKey="id" listValue="firstName" value="id"  />
			 </td>
			 	
		</tr>
		
		</table>
		<tr  align="center">
            		<td>
            		<div id="approverCommentsRow" style="display:none;">
		          <table id="commentsTable" width="100%" border="0" cellspacing="0" cellpadding="0">
			      <tr>
		               <td width="11%" class="greyboxwk"><s:text name="workflow.approver.comments" />:</td>
		               <td width="53%" class="greybox2wk" colspan="3"><s:textarea name="approverComments" cols="80" cssClass="selectwk" id="approverComments" value="%{approverComments}"/></td>
		             </tr>
				  </table>
          			</div>
          		</td>
          	</tr>
			
	</div>
<script>

function populateDesignation(){	
	var state='<s:property value='%{abstractEstimate.currentState.value}'/>';
	var estimateId
	if('<s:property value='%{abstractEstimate.id}'/>'!='')
		estimateId='<s:property value='%{abstractEstimate.id}'/>';
	else	
		estimateId='<s:property value='%{model.id}'/>';
	if(state=='')
		state='DEFAULT';
	populatedesignationId({departmentId:document.getElementById("departmentid").value,scriptName:'AbstractEstimate.nextDesignation',
	stateName:state,estimateId:estimateId})
		
}

function populateUser(){
	var estimateId='<s:property value='%{abstractEstimate.id}'/>';
	var wardId;
	if(estimateId!='')
		wardId='<s:property value='%{abstractEstimate.ward.id}'/>';
	else
		wardId=document.getElementById("wardID").value;
	populateapproverUserId({departmentId:document.getElementById("departmentid").value,
	designationId:document.getElementById("designationId").value,wardId:wardId})
		
}
function validateUser(name){
	//alert(":::"+name+":::");
	document.getElementById('approver_error').style.display ='none';
	if(name != 'reject' && name != 'cancel' && name!='save') {
	 	<s:if test="%{model.id==null || model.currentState.nextAction!='Pending Admin Sanction'}" >
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
	if(name != 'reject') {
		<s:if test="%{model.currentState.nextAction=='Pending Admin Sanction'}"> 
			/*if(!validateResolutionData())
		  		return false;*/
		</s:if>
	}
	return true;
}

</script>
