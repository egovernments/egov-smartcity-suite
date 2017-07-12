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

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>



	function validateWorkFlowApprover(name) {
		document.getElementById("workFlowAction").value=name;
	    var approverPosId = document.getElementById("approvalPosition");
	    /* if(approverPosId && approverPosId.value != -1) {
			var approver = approverPosId.options[approverPosId.selectedIndex].text; 
			document.getElementById("approverName").value= approver.split('~')[0];
			validateWorkFlowApprover('Forward');
		}   */
		var rejectbutton=document.getElementById("workFlowAction").value;
		if(rejectbutton!=null && rejectbutton=='Proceed Without Donation') {
			
			$('#approvalDepartment').attr('required', 'required');
			$('#approvalDesignation').attr('required', 'required');
			$('#approvalPosition').attr('required', 'required');
			$('#approvalComent').removeAttr('required');
		}
		if(rejectbutton!=null && rejectbutton=='Reject')
			{
			$('#approvalDepartment').removeAttr('required');
			$('#approvalDesignation').removeAttr('required');
			$('#approvalPosition').removeAttr('required');
			$('#approvalComent').attr('required', 'required');	
			} 
		 if(rejectbutton!=null && rejectbutton=='Forward'){
			if($('#currentUser') || $('#citizenPortalUser') )
			{
				if($('#currentUser').val()=="true" || $('#citizenPortalUser').val()=="true")
				{
					$('#approvalDepartment').removeAttr('required');
					$('#approvalDesignation').removeAttr('required');
					$('#approvalPosition').removeAttr('required');
					$('#approvalComent').removeAttr('required');
					return true;
				}
			}
			$('#approvalDepartment').attr('required', 'required');
			$('#approvalDesignation').attr('required', 'required');
			$('#approvalPosition').attr('required', 'required');
			$('#approvalComent').removeAttr('required');
		} 
		 if(rejectbutton!=null && rejectbutton=='Approve'){
				$('#approvalComent').removeAttr('required');
			} 
	   document.forms[0].submit;
	   return true;
	}
</script>
<div class="buttonbottom" align="center">
	<table>
		<tr>
			<td>
				<c:if test="${proceedWithoutDonation==true && statuscode=='ESTIMATIONNOTICEGENERATED'}">
					<form:button  type="submit" id="proceedwithoutdonation" class="btn btn-primary workAction" onclick="validateWorkFlowApprover('Proceed Without Donation');"><c:out value="Proceed Without Donation"/></form:button>
				</c:if>
		<c:forEach items="${validActionList}" var="validButtons">
			<form:button type="submit" id="${validButtons}" class="btn btn-primary workAction"  value="${validButtons}" onclick="validateWorkFlowApprover('${validButtons}');">
						<c:out value="${validButtons}" /> </form:button>
			</c:forEach>
				<input type="button" name="button2" id="button2" value="Close"
				class="btn btn-default" onclick="window.close();" /></td>
		</tr>
	</table>
</div>