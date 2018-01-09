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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.applicant.details" />
		</div>
	</div>
	<div>
		<spring:hasBindErrors name="sewerageApplicationDetails">
			<div class="alert alert-danger col-md-10 col-md-offset-1">
				<form:errors path="*" />
				<br />
			</div>
		</spring:hasBindErrors>
		<br />
	</div>
	<div class="panel-body">
		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.shsc.number" />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<c:out value="${sewerageApplicationDetails.connection.shscNumber}"
					default="N/A" />
			</div>
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.ack.number" />
			</div>
			<div class="col-xs-3 add-margin view-content" id="applicationNumber">
				<c:out value="${sewerageApplicationDetails.applicationNumber}"
					default="N/A" />
			</div>
		</div>

		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.ptassesmentnumber" />
			</div>
			<div class="col-xs-3 add-margin view-content" id='propertyIdentifier'>
				<c:out
					value="${sewerageApplicationDetails.connectionDetail.propertyIdentifier}"
					default="N/A" />
				<input type="hidden" id="assessmentNo"
					value="${sewerageApplicationDetails.connectionDetail.propertyIdentifier}" />
				<input type="hidden" id="applNumber"
					value="${sewerageApplicationDetails.applicationNumber}" /> <input
					type="hidden" id="shscNumber"
					value="${sewerageApplicationDetails.connection.shscNumber}" />
			</div>
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.application.date" />
			</div>
			<div class="col-xs-3 add-margin view-content" id="applicationDate">
				<fmt:formatDate pattern="dd/MM/yyyy"
					value="${sewerageApplicationDetails.applicationDate}" />
			</div>
		</div>
		<c:forEach items="${propertyOwnerDetails.ownerNames}" var="owner">
			<div class="row add-border">
				<div class="col-xs-3 add-margin">
					<spring:message code="lbl.mobileNo" />
				</div>
				<div class="col-xs-3 add-margin view-content" id="mobileNumber">
					<c:out value="${owner.mobileNumber}" default="N/A" />
				</div>
				<div class="col-xs-3 add-margin">
					<spring:message code="lbl.email" />
				</div>
				<div class="col-xs-3 add-margin view-content" id="email">
					<c:choose>
						<c:when test="${not empty owner.emailId}">
							<c:out value="${owner.emailId}" />
						</c:when>
						<c:otherwise>
							<spring:message code="lb.NA.code" />
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="row add-border">
				<div class="col-xs-3 add-margin">
					<spring:message code="lbl.applicantname" />
				</div>
				<div class="col-xs-3 add-margin view-content" id="applicantname">
					<c:out value="${owner.ownerName}" default="N/A" />
				</div>
				<div class="col-xs-3 add-margin">
					<spring:message code="lbl.locality" />
				</div>
				<div class="col-xs-3 add-margin view-content" id="locality">
					<c:out value="${propertyOwnerDetails.boundaryDetails.localityName}"
						default="N/A" />
				</div>
			</div>
			<div class="row add-border">
				<div class="col-xs-3 add-margin">
					<spring:message code="lbl.address" />
				</div>
				<div class="col-xs-3 add-margin view-content" id="propertyaddress">
					<c:out value="${propertyOwnerDetails.propertyAddress}"
						default="N/A" />
				</div>
				<div class="col-xs-3 add-margin">
					<spring:message code="lbl.zonewardblock" />
				</div>
				<div class="col-xs-3 add-margin view-content" id="zonewardblock">
					<c:out value="${propertyOwnerDetails.boundaryDetails.zoneName}" />
					<c:out value=" / " />
					<c:out value="${propertyOwnerDetails.boundaryDetails.wardName}" />
					<c:out value=" / " />
					<c:out value="${propertyOwnerDetails.boundaryDetails.blockName}" />
				</div>
			</div>
			<div class="row add-border">
				<div class="col-xs-3 add-margin">
					<spring:message code="lbl.aadhar" />
				</div>
				<div class="col-xs-3 add-margin view-content" id="aadhaar">
					<c:out value="${owner.aadhaarNumber}" default="N/A" />
				</div>
				<div class="col-xs-3 add-margin"></div>
				<div class="col-xs-3 add-margin view-content"></div>
			</div>
		</c:forEach>
		<div class="row">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.properttax" />
			</div>
			<div class="col-xs-3 add-margin view-content" id="propertytaxdue">
				<c:choose>
					<c:when test="${null!= mode && propertyTax > 0}">
						<c:out value="${propertyTax}" />
					</c:when>
					<c:otherwise>
						<spring:message code="lbl.zero.code" />
					</c:otherwise>
				</c:choose>
			</div>
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.current.due" />
			</div>
			<div class="col-xs-3 add-margin view-content"></div>
			<div class="col-xs-3 add-margin view-content">
				<c:choose>
					<c:when test="${null!= mode && sewerageTaxDue > 0}">
						<c:out value="${sewerageTaxDue}" />
					</c:when>
					<c:otherwise>
						<spring:message code="lbl.zero.code" />
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</div>