<%@ include file="/includes/taglibs.jsp"%>
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
	    if(approverPosId && approverPosId.value != -1 && approverPosId.value != "") {
			var approver = approverPosId.options[approverPosId.selectedIndex].text; 
			document.getElementById("approverName").value= approver.split('~')[0];
		}   
	    if ((name=="Reject" || name=="reject")) {
	    	var approverComments = document.getElementById("approverComments").value;
	    	if (approverComments == null || approverComments == "") {
	    		alert("Please Enter Approver Remarks ");
				return false;
	    	}
		}
		<s:if test="%{getNextAction()!='END'}">
	    if((name=="Forward" || name=="forward") && approverPosId && (approverPosId.value == -1 || approverPosId.value == "")) {
	        alert("Please Select the Approver ");
			return false;
	    }
	   
	    </s:if>
	    return  onSubmit();
	}
</script>
<div class="buttonbottom" >
	<s:hidden id="workFlowAction" name="workFlowAction" />
	<div class="row text-center">
			<div class="col-md-5 col-sm-3"></div>
			<s:iterator value="%{getValidActions()}" var="validAction">
			<div class="col-md-1 col-sm-2">
					<s:if test="%{validAction!=''}">
						<s:submit type="submit" cssClass="buttonsubmit" value="%{validAction}"
							id="%{validAction}" name="%{validAction}"
							onclick="return validateWorkFlowApprover('%{validAction}','jsValidationErrors');" />
					</s:if>
					</div>
				</s:iterator>
		<div class="col-md-1 col-sm-2">
			<input type="button" name="button2" id="button2" value="Close"
				class="button" onclick="window.close();" />
		</div>
	</div>
</div>
<style>
div#wwctrl_Forward,div#wwctrl_Reject{
	text-align:center;
}
</style>