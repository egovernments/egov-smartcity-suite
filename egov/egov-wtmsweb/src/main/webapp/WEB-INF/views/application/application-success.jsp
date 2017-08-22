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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div id="main">
<div class="row">
	<div class="col-md-12">
		<form:form  id="waterConnectionSuccess" method ="post" class="form-horizontal form-groups-bordered" modelAttribute="waterConnectionDetails" >
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title text-center">
				
				<c:choose>
				<c:when test="${ waterConnectionDetails.applicationType.code == 'CHANGEOFUSE' && waterConnectionDetails.closeConnectionType==null && waterConnectionDetails.state.value=='Rejected' }">
						<spring:message code="msg.changeofuseconnection.rejection.success" /><span> ${approverName}~${nextDesign}</span>
					</c:when>
					<c:when test="${ waterConnectionDetails.status.code!='APPROVED' && waterConnectionDetails.status.code!='SANCTIONED' && waterConnectionDetails.connectionStatus == 'INPROGRESS' && waterConnectionDetails.applicationType.code=='CHANGEOFUSE'}">
						<spring:message code="msg.changeofuseconnection.ack.msg" />
						<span ><spring:message code="msg.sussess.forward" />${approverName}</span>
					</c:when>
					<c:when test="${waterConnectionDetails.applicationType.code=='ADDNLCONNECTION' && waterConnectionDetails.state.value== 'Rejected'}">
					<span><spring:message code="msg.additonalconnection.rejection.success" /> ${approverName}~${nextDesign}</span>
					</c:when>
					<c:when test="${ waterConnectionDetails.state.value!= 'Rejected' &&  waterConnectionDetails.status.code != 'APPROVED' && waterConnectionDetails.connectionStatus == 'INPROGRESS' && waterConnectionDetails.applicationType.code=='ADDNLCONNECTION'}">
						<spring:message code="msg.addconnection.ack.msg" />
						<span ><spring:message code="msg.sussess.forward" />${approverName}~${nextDesign}</span>
					</c:when>
						<c:when test="${waterConnectionDetails.status.code == 'APPROVED' && waterConnectionDetails.connectionStatus == 'INPROGRESS' }">
							<span><spring:message code="msg.connection.approved.success" />${approverName}~${nextDesign}</span>
								<%-- <spring:message code="msg.sussess.forward" /><br>${approvalUser} --%>
							</c:when>
							<c:when test="${waterConnectionDetails.connectionStatus== 'INACTIVE' && waterConnectionDetails.applicationType.code=='CHANGEOFUSE' }">
							<span><spring:message code="msg.changeofuseconnection.cancel.success" /></span>
							</c:when>
							<c:when test="${waterConnectionDetails.connectionStatus== 'INACTIVE' && waterConnectionDetails.applicationType.code=='ADDNLCONNECTION' }">
							<span><spring:message code="msg.additionalconnection.cancel.success" /></span>
							</c:when>
							<c:when test="${waterConnectionDetails.state.value== 'Rejected' && waterConnectionDetails.applicationType.code=='NEWCONNECTION' && waterConnectionDetails.status.code=='CANCELLED'}">
							<span><spring:message code="msg.newapplication.cancel.success" /></span>
							</c:when>
							<c:when test="${waterConnectionDetails.state.value== 'Rejected' && waterConnectionDetails.applicationType.code=='NEWCONNECTION' && waterConnectionDetails.closeConnectionType==null}">
							<span><spring:message code="msg.newapplication.rejection.success" />${approverName}~${nextDesign}</span>
							</c:when>
							<c:when test="${waterConnectionDetails.connectionStatus == 'INACTIVE' && waterConnectionDetails.applicationType.code=='NEWCONNECTION'}">
							<span><spring:message code="msg.newapplication.cancel.success" /></span>
							</c:when>
								<c:when test="${waterConnectionDetails.status.code == 'SANCTIONED' && waterConnectionDetails.connectionStatus == 'ACTIVE' && waterConnectionDetails.previousApplicationType !=null  }">
								<spring:message code="msg.connection.disconnected" />
								<%-- <spring:message code="msg.sussess.forward" /><br>${approvalUser} --%>
							</c:when>
							
							<c:when test="${waterConnectionDetails.status.code == 'SANCTIONED' && waterConnectionDetails.connectionStatus == 'ACTIVE'  }">
								<spring:message code="msg.connection.sanctioned.success" />
								<%-- <spring:message code="msg.sussess.forward" /><br>${approvalUser} --%>
							</c:when>
							<c:when test="${ waterConnectionDetails.connectionStatus == 'INPROGRESS' && waterConnectionDetails.applicationType.code=='NEWCONNECTION'}">
							<span><spring:message code="msg.newconnection.ack.success" /></span>
								<span ><spring:message code="msg.sussess.forward" />${approverName}~${nextDesign}</span>
							</c:when>
						<c:when test="${waterConnectionDetails.reConnectionReason ==null && waterConnectionDetails.status.code != 'CLOSERSANCTIONED' && waterConnectionDetails.closeConnectionType != null &&  waterConnectionDetails.state.value != 'Rejected' && waterConnectionDetails.status.code!= 'CLOSERAPPROVED'}">
							<span><spring:message code="msg.closeconnection.ack.success" /></span>
								<span ><spring:message code="msg.sussess.forward" />${approverName}~${nextDesign}</span>
						</c:when>
							<c:when test="${waterConnectionDetails.closeConnectionType != null && waterConnectionDetails.status.code== 'CLOSERAPPROVED'}">
							<span><spring:message code="msg.closreConn.approved.success" />${approverName}~${nextDesign}</span>
								
						</c:when>
						<c:when test="${waterConnectionDetails.state.value== 'Rejected' && waterConnectionDetails.reConnectionReason==null && waterConnectionDetails.closeConnectionType != null}">
							<span><spring:message code="msg.closure.rejection.success" />${approverName}~${nextDesign}</span>
							</c:when>
							<c:when test="${waterConnectionDetails.reConnectionReason !=null && waterConnectionDetails.closeConnectionType == 'T' &&  waterConnectionDetails.state.value != 'Rejected' && waterConnectionDetails.status.code!= 'RECONNECTIONAPPROVED'}">
							<span><spring:message code="msg.reconnection.ack.success" /></span>
								<span ><spring:message code="msg.sussess.forward" />${approverName}~${nextDesign}</span>
						</c:when>
							<c:when test="${waterConnectionDetails.closeConnectionType == 'T' && waterConnectionDetails.status.code== 'RECONNECTIONAPPROVED'}">
							<span><spring:message code="msg.reconnection.approved.success" />${approverName}~${nextDesign}</span>
						</c:when>
						<c:when test="${waterConnectionDetails.state.value== 'Rejected' && waterConnectionDetails.applicationType.code=='RECONNECTION' && waterConnectionDetails.closeConnectionType == 'T'}">
							<span><spring:message code="msg.reconnection.rejection.success" />${approverName}~${nextDesign}</span>
							</c:when>
						<c:when test="${waterConnectionDetails.status.code == 'CLOSERSANCTIONED' && waterConnectionDetails.connectionStatus == 'CLOSED'  }">
								<spring:message code="msg.reconnection.disconnected" />
								<%-- <spring:message code="msg.sussess.forward" /><br>${approvalUser} --%>
							</c:when>
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
					${currentUserDesgn}<br>${cityName}</div>
			</c:when>	
			<c:otherwise>
				<div class="col-xs-12 view-content text-right"><spring:message code="msg.sign"/><br>
					${currentUserDesgn}<br>${cityName}</div>
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
				<a href="javascript:void(0)" class="btn btn-default inboxload" onclick="self.close()" ><spring:message code="lbl.close" /></a>
			</c:when>
			<c:otherwise>
				<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<script src="<cdn:url value='/resources/js/app/applicationsuccess.js?rnd=${app_release_no}'/>"></script>
<script type="text/javascript">
function printDiv(divName) {
    var printContents = document.getElementById(divName).innerHTML;
    var originalContents = document.body.innerHTML;
    document.body.innerHTML = printContents;
    window.print();
    document.body.innerHTML = originalContents;
}
</script>