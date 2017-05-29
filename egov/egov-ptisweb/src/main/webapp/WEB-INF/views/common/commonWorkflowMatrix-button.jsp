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
<script type="text/javascript">

function EnableForwardButton(){
	if(jQuery('#applicationCheck').prop("checked"))
		jQuery('#Forward').removeAttr("disabled");
	else
		jQuery('#Forward').attr('disabled','disabled');
}

jQuery(document).ready(function(e){

	if(jQuery('#applicationCheck').length == 0){
		jQuery('#Forward').removeAttr('disabled');
	}
	
});

function validateWorkFlowApprover(name) {
	document.getElementById("workFlowAction").value=name;
    var approverPosId = document.getElementById("approvalPosition");
	var rejectbutton=document.getElementById("workFlowAction").value;
	if(rejectbutton!=null && rejectbutton=='Reject')
		{
		$('#approvalDepartment').removeAttr('required');
		$('#approvalDesignation').removeAttr('required');
		$('#approvalPosition').removeAttr('required');
		$('#approvalComent').attr('required', 'required');	
		} 
   document.forms[0].submit;
   return true;
}

</script>

<div class="buttonbottom" align="center">
	<table>
	<form:hidden path="" id="workFlowAction" name="workFlowAction"/>		
		<tr>
			<td>
		<c:forEach items="${validActionList}" var="validButtons">
					<c:choose>
						<c:when test="${validButtons == 'Forward'}">
							<form:button type="submit" disabled="true" id="${validButtons}"
								class="btn btn-primary" value="${validButtons}"
								onclick="validateWorkFlowApprover('${validButtons}');">
								<c:out value="${validButtons}" />
							</form:button>
						</c:when>
						<c:otherwise>
							<form:button type="submit" disabled="false" id="${validButtons}"
								class="btn btn-primary" value="${validButtons}"
								onclick="validateWorkFlowApprover('${validButtons}');">
								<c:out value="${validButtons}" />
							</form:button>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<input type="button" name="button2" id="button2" value="Close"
				class="btn btn-default" onclick="window.close();" /></td>
		</tr>
	</table>
</div>