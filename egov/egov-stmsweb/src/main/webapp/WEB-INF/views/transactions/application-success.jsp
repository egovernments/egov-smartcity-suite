<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<div id="main">
<div class="row">
	<div class="col-md-12">
		<form:form  id="sewarageConnectionSuccess" method ="post" class="form-horizontal form-groups-bordered" modelAttribute="sewerageApplicationDetails" >
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title text-center">
					<c:if test="${sewerageApplicationDetails.applicationType.code == 'NEWSEWERAGECONNECTION'}">
						<c:choose>
							<c:when test="${sewerageApplicationDetails.state.value != 'Closed'
							&& sewerageApplicationDetails.state.value != 'Rejected'}">
								<span><spring:message code="msg.newconnection.ack.success" arguments="${sewerageApplicationDetails.applicationNumber}"/></span>
								<span ><spring:message code="msg.sussess.forward" />${approverName}~${nextDesign}</span>
							</c:when>
							<c:when test="${sewerageApplicationDetails.state.value == 'Closed' && sewerageApplicationDetails.status.code != 'CANCELLED'}">
								<span><spring:message code="msg.connection.process.success" arguments="${sewerageApplicationDetails.applicationNumber}" /></span>
							</c:when>
							<c:when test="${sewerageApplicationDetails.state.value == 'Rejected'}">
								<span><spring:message code="msg.connection.rejected" arguments="${sewerageApplicationDetails.applicationNumber}"/>${approverName}~${nextDesign}</span>
							</c:when>
							<c:when test="${sewerageApplicationDetails.status.code == 'CANCELLED'}">
								<span><spring:message code="msg.connection.cancelled" arguments="${sewerageApplicationDetails.applicationNumber}" /></span>
							</c:when>
						</c:choose>
					</c:if>
				</div> 
			</div>
			</div>
			<jsp:include page="commonApplicationDetails-view.jsp"/>
			<jsp:include page="documentdetails-view.jsp"></jsp:include>
			<jsp:include page="connectionDetails-view.jsp"></jsp:include> 
			<c:choose>
				<c:when test="${sewerageApplicationDetails.status.code == 'CREATED' || 
								sewerageApplicationDetails.status.code == 'COLLECTINSPECTIONFEE' ||
									sewerageApplicationDetails.status.code == 'INSPECTIONFEEPENDING' ||
										sewerageApplicationDetails.status.code == 'INSPECTIONFEEPAID'}">
					<c:if test="${inspectionFeesCollectionRequired!=null && inspectionFeesCollectionRequired == 'true'}">
							<jsp:include page="inspectionChargesView.jsp"></jsp:include>
					</c:if>
			</c:when>
			<c:otherwise>
				<jsp:include page="estimationdetails-view.jsp"></jsp:include>
				<jsp:include page="seweragechargesdetails.jsp"/> 
			</c:otherwise>
			</c:choose>
		
			
		<div class="panel panel-default" data-collapsed="0">
			<div class="panel-body">
				<div class="row">
				<c:choose>
					<c:when test="${sewerageApplicationDetails.status.code!='SANCTIONED' && sewerageApplicationDetails.status.code!='CANCELLED'}">
						<div class="col-xs-6 add-margin">
							<spring:message code="lbl.disposal.date" /> : <fmt:formatDate pattern="dd/MM/yyyy" value="${sewerageApplicationDetails.disposalDate}" />
						</div>
						<div class="col-xs-6 view-content text-right"><spring:message code="msg.sign"/><br>
							${currentUserDesgn}<br>${cityName}
						</div>
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
		<c:if
			test="${sewerageApplicationDetails.status.code == 'FINALAPPROVED' }">
			<a href="javascript:void(0)" class="btn btn-default"
				onclick="renderWorkOrderPdf()">Preview</a>
		</c:if>
	</div>
	<div class="add-margin">
		<button type="submit" class="btn btn-default print" id="printBtn" onclick="printDiv('main')"><spring:message code="lbl.print" /></button>
		<c:choose>
			<c:when test="${sewerageApplicationDetails.status == 'ACTIVE' }">
				<a href="javascript:void(0)" class="btn btn-default inboxload" onclick="self.close()" ><spring:message code="lbl.close" /></a>
			</c:when>
			<c:otherwise>
				<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<script src="<cdn:url  value='/resources/js/transactions/applicationsuccess.js?rnd=${app_release_no}'/>"></script>
<script type="text/javascript">
function printDiv(divName) {
    var printContents = document.getElementById(divName).innerHTML;
    var originalContents = document.body.innerHTML;
    document.body.innerHTML = printContents;
    window.print();
    document.body.innerHTML = originalContents;
}
function renderWorkOrderPdf() {
    window.open("/stms/transactions/workordernotice?pathVar=${sewerageApplicationDetails.applicationNumber}", '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}
</script>
