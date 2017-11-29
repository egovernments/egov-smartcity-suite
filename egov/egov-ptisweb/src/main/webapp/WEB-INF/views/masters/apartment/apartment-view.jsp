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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="row" id="page-content">
	<div class="col-md-12" align="justify">
		<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert">
				<spring:message code="${message}" />
			</div>
		</c:if>
		<form:form method="post" class="form-horizontal form-groups-bordered"
			modelAttribute="apartment" id="apartmentViewForm">
			<div class="panel panel-primary" data-collapsed="0"
				style="text-align: left">
				<div class="panel-heading">
					<div class="panel-title">Apartment Details</div>
				</div>
				<div class="panel-body">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.name" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${apartment.name}" /> </strong>
						</div>
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.code" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${apartment.code}" /> </strong>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.apartment.type" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${apartment.type}" /> </strong>
						</div>
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.totalFloors" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${apartment.totalFloors}" /> </strong>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.totalFlats" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${apartment.totalProperties}" />
							</strong>
						</div>
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.sourceOfWater" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${apartment.sourceOfWater}" /> </strong>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.totalResidentialFlats" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out
									value="${apartment.totalResidentialProperties}" 
									default="N/A" /> </strong>
						</div>
						<div class="col-xs-3 add-margin">
							<label><spring:message
									code="lbl.totalNonResidentialFlats" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out
									value="${apartment.totalNonResidentialProperties}"
									default="N/A" /> </strong>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.liftFacility" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out
									value="${apartment.liftFacility ? 'Yes' :'No'}" /> </strong>
						</div>
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.powerBackup" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out
									value="${apartment.powerBackup ? 'Yes' :'No'}" /> </strong>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.parkingFacility" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out
									value="${apartment.parkingFacility ? 'Yes' :'No'}" /> </strong>
						</div>
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.fireFightingFacility" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out
									value="${apartment.fireFightingFacility ? 'Yes' :'No'}" /> </strong>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.builtUpArea" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${apartment.builtUpArea}" /> </strong>
						</div>
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.openSpaceArea" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${apartment.openSpaceArea}" 
							default="N/A" /> </strong>
						</div>
					</div>
					<div class="row">
						<div class="text-center">

							<input type="button" value="Close" class="btn btn-default"
								onclick="self.close()">
						</div>
					</div>
		</form:form>
	</div>
</div>