<%@ include file="/includes/taglibs.jsp"%>
<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>
<script>
jQuery.noConflict();
jQuery(document).on('change', ".applicationcheckbox", function () {
	var applicationCheckVal = jQuery('#applicationCheck').prop("checked");
	 if(applicationCheckVal)
		 jQuery('#Forward').removeAttr("disabled");
	 else
		 jQuery('#Forward').attr('disabled', 'disabled');
});
    
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
		<s:if test="%{getNextAction()!='END'}">
	    if((name=="Forward" || name=="forward") && approverPosId && (approverPosId.value == -1 || approverPosId.value == "")) {
	        bootbox.alert("Please Select the Employee ");
			return false;
	    }
	    if ((name=="Reject" || name=="reject")) {
	    	var approverComments = document.getElementById("approverComments").value;
	    	if (approverComments == null || approverComments == "") {
	    		bootbox.alert("Please Enter Remarks ");
				return false;
	    	}
		}
	    </s:if>
	    return  onSubmit();
	}

	jQuery(document).ready(function(e){
		if(jQuery('#applicationCheck').length == 0)
			jQuery('#Forward').removeAttr('disabled');
	});
	
	var popupWindow=null;
	
	function openReassignWindow()
	{ 
		var modelIdAndAppTypeParam = '<s:property value="%{transactionType}"/>'+"&"+'<s:property value="%{stateAwareId}"/>';
		popupWindow = window.open('/ptis/reassign/'
				+ modelIdAndAppTypeParam,
				'_blank', 'width=650, height=500, scrollbars=yes', false);
		jQuery('.loader-class').modal('show', {backdrop: ''});
	}
	
	function closeAllWindows(){
		popupWindow.close();
		self.close();
	}

	function closeChildWindow(){
		jQuery('.loader-class').modal('hide');
		popupWindow.close();
	}

	function validateEndorsement(){
		if((document.getElementById("approverComments").value).trim()=="" || document.getElementById("approverComments").value==null){
			bootbox.alert("Please add Remarks");
			return false;
		}
		else
		    openEndorsementNotice();
	}

	function openEndorsementNotice(){
		var modelNo = '<s:property value="%{property.getId}"/>';
 		var remarks = document.getElementById("approverComments").value;
		if('<s:property value="%{transactionType}"/>' == 'New_Assessment'){
		popupWindow = window.open('/ptis/endorsementNotice?'
				+ 'applicantName='+encodeURIComponent('<s:property value="%{ownersName}"/>')+'&serviceName='+encodeURIComponent('<s:property value="%{transactionType}"/>')+'&remarks='+encodeURIComponent(remarks)+'&assessmentNo=<s:property value="%{stateAwareId}"/>&applicationNo=<s:property value="%{applicationNumber}"/>',
				'_blank', 'width=650, height=500, scrollbars=yes', false);
		}
		else
			{
			popupWindow = window.open('/ptis/endorsementNotice?'
					+ 'applicantName='+encodeURIComponent('<s:property value="%{ownersName}"/>')+'&serviceName='+encodeURIComponent('<s:property value="%{transactionType}"/>')+'&remarks='+encodeURIComponent(remarks)+'&assessmentNo=<s:property value="%{assessmentNumber}"/>&applicationNo=<s:property value="%{applicationNumber}"/>',
					'_blank', 'width=650, height=500, scrollbars=yes', false);
			}
		popupWindow.opener.close();
	}

</script>
<div class="buttonbottom" align="center">
	<s:hidden id="workFlowAction" name="workFlowAction" />
	<table style="width: 100%; text-align: center;">
		<tr>
			<td><s:iterator value="%{getValidActions()}" var="name">
					<s:if test="%{name!=''}">
						<s:if test="%{#name.equalsIgnoreCase('Forward')}">
							<s:submit type="submit" value="%{name}" cssClass="buttonsubmit"
								id="%{name}" name="%{name}" disabled="true"
								onclick="return validateWorkFlowApprover('%{name}','jsValidationErrors');" />
						</s:if>
						<s:else>
							<s:submit type="submit" cssClass="buttonsubmit" value="%{name}"
								id="%{name}" name="%{name}"
								onclick="return validateWorkFlowApprover('%{name}','jsValidationErrors');" />
						</s:else>
					</s:if>
				</s:iterator> <s:if test="%{(model.currentState.value).endsWith('NEW')}">
					<s:if test="%{isReassignEnabled}">
						<input type="button" value="Reassign" class="buttonsubmit"
							id="Reassign" name="Reassign"
							onclick="return openReassignWindow();" />
					</s:if>
				</s:if>
				<s:if test="%{endorsementRequired}">
						<input type="button" value="Endorsement" class="buttonsubmit"
							id="Endorsement" name="Endorsement"
							onclick="return validateEndorsement();" />
					</s:if>
					 <input type="button" name="button2" id="button2" value="Close"
				class="button" onclick="window.close();" /></td>
		</tr>
	</table>
</div>