<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<div id="main">
<div class="row">
	<div class="col-md-12">
		<form:form  id="waterConnectionSuccess" method ="post" class="form-horizontal form-groups-bordered" modelAttribute="waterConnectionDetails" >
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title text-center no-float">
				<c:choose>
				<c:when test="${waterConnectionDetails.connection.parentConnection.id!=null && waterConnectionDetails.state.value=='Rejected' && waterConnectionDetails.applicationType.code=='CHANGEOFUSE'}">
						<spring:message code="msg.changeofuseconnection.rejection.success" />
						<div >${approverName}</div>
					</c:when>
					<c:when test="${waterConnectionDetails.connection.parentConnection.id!=null && waterConnectionDetails.connectionStatus == 'INPROGRESS' && waterConnectionDetails.applicationType.code=='CHANGEOFUSE'}">
						<spring:message code="msg.changeofuseconnection.ack.msg" />
						<div ><spring:message code="msg.sussess.forward" />${approverName}</div>
					</c:when>
					<c:when test="${waterConnectionDetails.connection.parentConnection.id!=null && waterConnectionDetails.connectionStatus == 'INPROGRESS' && waterConnectionDetails.applicationType.code=='ADDNLCONNECTION'}">
						<spring:message code="msg.addconnection.ack.msg" />
						<div ><spring:message code="msg.sussess.forward" />${approverName}</div>
					</c:when>
					<c:when test="${waterConnectionDetails.connection.parentConnection.id!=null && waterConnectionDetails.connectionStatus == 'ACTIVE' && waterConnectionDetails.status.code == 'SANCTIONED'}">
					<div>	<spring:message code="msg.addconnection.approved" />${approverName}</div>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${waterConnectionDetails.status.code == 'APPROVED' && waterConnectionDetails.connectionStatus == 'INPROGRESS' }">
							<div><spring:message code="msg.connection.approved.success" />${approverName}</div>
								<%-- <spring:message code="msg.sussess.forward" /><br>${approvalUser} --%>
							</c:when>
							<c:when test="${waterConnectionDetails.connectionStatus== 'INACTIVE' && waterConnectionDetails.applicationType.code=='CHANGEOFUSE' }">
							<div><spring:message code="msg.changeofuseconnection.cancel.success" /></div>
							</c:when>
							<c:when test="${waterConnectionDetails.state.value== 'Rejected' }">
							<div><spring:message code="msg.newapplication.rejection.success" />${approverName}</div>
							</c:when>
							<c:when test="${waterConnectionDetails.connectionStatus == 'INACTIVE' }">
							<div><spring:message code="msg.newapplication.cancel.success" /></div>
							</c:when>
							<c:when test="${waterConnectionDetails.status.code == 'SANCTIONED' && waterConnectionDetails.connectionStatus == 'ACTIVE' }">
								<spring:message code="msg.connection.sanctioned.success" />
								<%-- <spring:message code="msg.sussess.forward" /><br>${approvalUser} --%>
							</c:when>
							<c:otherwise>
								<div><spring:message code="msg.newconnection.ack.success" /></div>
								<div ><spring:message code="msg.sussess.forward" />${approverName}</div>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
				</div>
			</div>
				<jsp:include page="commonappdetails-view.jsp"></jsp:include>
		</div>
<jsp:include page="connectiondetails-view.jsp"></jsp:include>
	<div class="panel panel-default" data-collapsed="0">
		<div class="panel-body">
			<div class="row">
			<c:choose>
			<c:when test="${waterConnectionDetails.status.code!='SANCTIONED'}">
				<div class="col-xs-6 add-margin"><spring:message code="lbl.disposal.date" /> : <fmt:formatDate pattern="dd/MM/yyyy" value="${waterConnectionDetails.disposalDate}" /></div>
				<!-- <div class="col-xs-3 add-margin view-content">
					
				</div> -->
				<div class="col-xs-6 view-content text-right"><spring:message code="msg.sign"/><br>
					<spring:message code="msg.ack.assistant"/><br>${cityName}</div>
			</c:when>	
			<c:otherwise>
				<div class="col-xs-12 view-content text-right"><spring:message code="msg.sign"/><br>
					<spring:message code="msg.ack.assistant"/><br>${cityName}</div>
			</c:otherwise>
			</c:choose>
			</div>
		</div>
	</div>
		</form:form>
	</div>					
</div>					
</div>
<div class="row text-center">
	<div class="add-margin">
		<button type="submit" class="btn btn-default print" id="printBtn" onclick="printDiv('main')"><spring:message code="lbl.print" /></button>
		<c:choose>
			<c:when test="${waterConnectionDetails.connectionStatus == 'ACTIVE' }">
				<a href="javascript:void(0)" class="btn btn-default inboxload" ><spring:message code="lbl.close" /></a>
			</c:when>
			<c:otherwise>
				<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<script src="<c:url value='/resources/js/app/applicationsuccess.js'/>"></script>
<script type="text/javascript">
function printDiv(divName) {
    var printContents = document.getElementById(divName).innerHTML;
    var originalContents = document.body.innerHTML;
    document.body.innerHTML = printContents;
    window.print();
    document.body.innerHTML = originalContents;
}
</script>