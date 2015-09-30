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
<div class="buttonbottom text-center">
	<s:hidden id="workFlowAction" name="workFlowAction"/>
	<div style="display:inline-block"><s:iterator value="%{getValidActions()}" var="name">
		<s:if test="%{name!=''}">
			<s:submit type="submit" cssClass="buttonsubmit" value="%{name}"
				id="%{name}" name="%{name}"
				onclick="return validateWorkFlowApprover('%{name}','jsValidationErrors');"  style="margin:0 10px"/>
		</s:if>
	</s:iterator></div> 
	<div style="display:inline-block"><input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();" /></div>
	
</div>