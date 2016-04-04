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
	    document.getElementById("workFlowAction").value=name;
	    var approverPosId = document.getElementById("approverPositionId");
	    if(approverPosId && approverPosId.value != -1) {
			var approver = approverPosId.options[approverPosId.selectedIndex].text; 
			document.getElementById("approverName").value= approver.split('~')[0];
		}   
		<s:if test="%{getNextAction()!='END'}">
	    if((name=="Forward" || name=="forward") && approverPosId && approverPosId.value == -1) {
	        alert("Please Select the Approver ");
			return false;
	    }
	    if ((name=="Reject" || name=="reject")) {
	    	var approverComments = document.getElementById("approverComments").value;
	    	if (approverComments == null || approverComments == "") {
	    		alert("Please Enter Approver Remarks ");
				return false;
	    	}
		}
	    </s:if>
	    return  onSubmit();
	}
</script>
<div class="form-group text-center" align="center">
	<s:hidden id="workFlowAction" name="workFlowAction"/>
     <s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
 					<s:submit type="submit" cssClass="btn btn-primary" value="Save" id="Save" name="Save" 
						onclick="document.getElementById('workFlowAction').value='Save';return validate('Save');" />
						&nbsp;
	 			</s:if>
				<s:iterator value="%{getValidActions()}" var="buttonName">
					<s:if test="%{buttonName!=''}">
						<s:submit type="submit" cssClass="btn btn-primary" value="%{buttonName}"
							id="%{buttonName}" name="%{buttonName}"
							onclick="return validateWorkFlowApprover('%{buttonName}','jsValidationErrors');" />&nbsp;
					</s:if>
				</s:iterator> 
				<input type="button" name="button2" id="button2" value="Close"
				class="btn btn-default" onclick="window.close();" />
</div>