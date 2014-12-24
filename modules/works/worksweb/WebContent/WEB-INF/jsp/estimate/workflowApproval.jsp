
<div class="rbcontent2">
	<div class="errorstyle" id="approver_error" style="display:none;"></div>	      
	   <table width="100%" border="0" cellspacing="0" cellpadding="0" >
		<tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
                <div class="headplacer"><s:text name="workflow.header" />:</div></td>
                </tr>
	  	 <tr>
			<td class="greyboxwk" id="deptLabel"><s:text name="workflow.approver.department"/></td>
			<td class="greybox2wk"><s:select name="departmentid" id="departmentid" list="dropdownData.executingDepartmentList" listKey="id" listValue="deptName" headerKey="-1" 
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
		<tr> 
		    <td>
		    <div id="resolution_details" style="display:none;">
		         <%@ include file="estimate-resolutionData.jsp"%>   
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
