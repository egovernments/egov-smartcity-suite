<%@ include file="/includes/taglibs.jsp" %>
<script>
	function validateWorkFlowApprover(name,errorDivId) {
		document.getElementById("workFlowAction").value=name;
	    var approverPosId = document.getElementById("approverPositionId");
	    if(approverPosId) {
			var approver = approverPosId.options[approverPosId.selectedIndex].text; 
			document.getElementById("approverName").value= approver.split('~')[0];
		}     
	   return  onSubmit();
	}

	function validateWorkFlowApprover(name) {
		if(!onSubmitValidations()){
			return false;
		}
	    document.getElementById("workFlowAction").value=name;
	    var approverDeptId =document.getElementById("approverDepartment");
	    var approverDesgId = document.getElementById("approverDesignation");
	    var approverPosId = document.getElementById("approverPositionId");
	    var approverComments = document.getElementById("approverComments").value;
	    if(approverPosId && approverPosId.value != -1) {
			var approver = approverPosId.options[approverPosId.selectedIndex].text; 
			document.getElementById("approverName").value= approver.split('~')[0];
		}   
		<s:if test="%{getNextAction()!='END'}">
	    if(name=="Forward" || name=="forward") {
	    	if(approverDeptId && approverDeptId.value == -1){
		        alert("Please Select the Approver Department ");
				return false;
		    } else if(approverDesgId && approverDesgId.value == -1){
		        alert("Please Select the Approver Designation ");
				return false;
		    } else if(approverPosId && approverPosId.value == -1){
		        alert("Please Select the Approver ");
				return false;
		    }  
	    }
	    </s:if>
	    if(name=="Forward" || name=="forward" || name=="approve" || name=="Approve") {
	    	 if (approverComments == null || approverComments == "") { 
	    		alert("Please Enter Approver Remarks ");
				return false;
	    	}  
	    }
	    if ((name=="Reject" || name=="reject")) {
	    	if (approverComments == null || approverComments == "") {
	    		alert("Please Enter Rejection Remarks ");
				return false;
	    	}
		}
	    return  onSubmit();
	}
</script>

<div class="buttonbottom" align="center">
	<s:hidden id="workFlowAction" name="workFlowAction"/>
	<table>
		<tr>
			<td><s:iterator value="%{getValidActions()}" var="name">
					<s:if test="%{name!=''}">
					<td>
						<s:submit type="submit" cssClass="buttonsubmit custom-button" value="%{name}"
							id="%{name}" name="%{name}"
							onclick="return validateWorkFlowApprover('%{name}','jsValidationErrors');" style="margin:0 5px"/></td>
					</s:if>
				</s:iterator> <td><input type="button" name="button2" id="button2" value="Close"
				class="button" onclick="window.close();" style="margin:0 5px"/></td></td>
		</tr>
	</table>
</div>