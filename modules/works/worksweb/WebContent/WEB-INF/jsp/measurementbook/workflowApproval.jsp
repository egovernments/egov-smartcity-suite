
<div class="rbcontent2">
<div id="workflowDetials">
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
			<egov:ajaxdropdown id="designationId" fields="['Text','Value']" dropdownId="designationId" url="measurementbook/ajaxMeasurementBook!getDesgByDeptAndType.action" />
			</td>
			
			<td class="greyboxwk"><span class="mandatory">*</span><s:text name="workflow.approver.designation"/></td>
			<td class="greybox2wk"><s:select name="designationId" id="designationId" list="#{}"
			 listKey="designationId" listValue="designationName" headerKey="-1" headerValue="%{getText('estimate.default.select')}"  value="designationId" onchange= "populateUser()" />
			<egov:ajaxdropdown id="approverUserId" fields="['Text','Value']" dropdownId="approverUserId" url="measurementbook/ajaxMeasurementBook!getWorkFlowUsers.action" />
			</td>
		</tr>
		
		 <tr>		 
			 <td class="whiteboxwk" width="13%"><span class="mandatory">*</span><s:text name="workflow.approver"/></td>
			  <td class="whitebox2wk" width="33%" colspan="3"><s:select id="approverUserId"  name="approverUserId" list="#{}" headerKey="-1" headerValue="%{getText('estimate.default.select')}" listKey="id" listValue="firstName" value="id"  />
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
		               <td width="53%" class="greybox2wk" colspan="3"><s:textarea name="approverComments" cols="80" cssClass="selectwk" id="approverComments" value="%{approverComments}"/></td>
		             </tr>
				  </table>
          			</div>
          		</td>
          	</tr>
	</div>
<script>

function populateDesignation(){	
	var state='<s:property value='%{model.currentState.value}'/>';
	var model_id = '<s:property value='%{model.id}'/>';
	if(state=='')
		state='DEFAULT';
	populatedesignationId({departmentId:document.getElementById("departmentid").value,scriptName:'MBHeader.nextDesignation',
	stateName:state,modelId:model_id})
		
}

function populateUser(){
	model_id = '<s:property value='%{model.id}'/>';
	var ward_id = -1;
	populateapproverUserId({departmentId:document.getElementById("departmentid").value,
	designationId:document.getElementById("designationId").value,wardId:ward_id})
		
}
function validateUser(name){
	document.getElementById('approver_error').style.display ='none';
	if(name != 'reject' && name != 'cancel' && name != 'save') {
		<s:if test="%{model.id==null || model.currentState.nextAction!='Pending for Approval'}" > {
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
		}
		</s:if>
	}	
	return true;
}

</script>
