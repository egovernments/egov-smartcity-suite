<!---------------------------------------------------------------------------------
 	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
     Copyright (C) <2015>  eGovernments Foundation
 
     The updated version of eGov suite of products as by eGovernments Foundation 
     is available at http://www.egovernments.org
 
     This program is free software: you can redistribute it and/or modify
     it under the terms of the GNU General Public License as published by
     the Free Software Foundation, either version 3 of the License, or
     any later version.
 
     This program is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU General Public License for more details.
 
     You should have received a copy of the GNU General Public License
     along with this program. If not, see http://www.gnu.org/licenses/ or 
     http://www.gnu.org/licenses/gpl.html .
 
     In addition to the terms of the GPL license to be adhered to in using this
     program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
--------------------------------------------------------------------------------->
<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div class="row">
	<div class="col-md-12">
		<form:form class="form-horizontal form-groups-bordered" method="get"
			name="taxExemptionForm" id="taxExemptionForm" action=""
			modelAttribute="property">
			<div class="panel panel-primary" data-collapsed="0"
				style="text-align: left">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.property.details" />
					</div>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-3 add-margin">
							<spring:message code="lbl.assmtno" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:out default="N/A" value="${property.basicProperty.upicNo}" />
						</div>
						<div class="col-xs-3">
							<spring:message code="lbl.assmtno.parentproperty" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:out value="${parentAssessment}" default="N/A" />
						</div>
					</div>
					<div class="row">
						<div class="col-xs-3">
							<spring:message code="lbl.category.ownership" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:out value="${property.propertyDetail.propertyTypeMaster.type}"
								default="N/A" />
						</div>
						<div class="col-xs-3">
							<spring:message code="lbl.exemption.category" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:out value="${property.taxExemptedReason.name}" default="N/A" />
						</div>
					</div>
					<div class="row">
						<div class="col-xs-3">
							<spring:message code="lbl.annualvalue" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							Rs.
							<fmt:formatNumber pattern="#,##0.00" value="${arv}" />
						</div>
						<div class="col-xs-3">
							<spring:message code="lbl.effectivedate" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:choose>
								<c:when
									test="${property.basicProperty.propOccupationDate != null}">
									<fmt:formatDate
										value="${property.basicProperty.propOccupationDate}"
										pattern="dd/MM/yyyy" />
								</c:when>
								<c:otherwise>
									N/A
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-3">
							<spring:message code="lbl.propertytype" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:out default="N/A"
								value="${property.basicProperty.property.propertyDetail.categoryType}" />
						</div>
						<div class="col-xs-3">
							<spring:message code="lbl.appartmentorcomplex" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:out default="N/A"
								value="${property.basicProperty.property.propertyDetail.apartment.name}" />
						</div>
					</div>
					<div class="row">
						<div class="col-xs-3">
							<spring:message code="lbl.extentofsite" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:out default="N/A"
								value="${property.propertyDetail.sitalArea.area}" />
						</div>
						<div class="col-xs-3">
							<spring:message code="lbl.extent.appurtenant" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:out default="N/A"
								value="${property.propertyDetail.extentAppartenauntLand}" />
						</div>
					</div>
					<div class="row">
						<div class="col-xs-3">
							<spring:message code="lbl.superstructure" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:choose>
								<c:when test="${property.propertyDetail.structure == true}">
									Yes
								</c:when>
								<c:otherwise>
									No
								</c:otherwise>
							</c:choose>
						</div>
						<div class="col-xs-3">
							<spring:message code="lbl.siteowner" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:out default="N/A" value="${property.propertyDetail.siteOwner}" />
						</div>
					</div>
					<div class="row">
						<div class="col-xs-3">
							<spring:message code="lbl.registrationDoc.no" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:out default="N/A" value="${property.basicProperty.regdDocNo}" />
						</div>
						<div class="col-xs-3">
							<spring:message code="lbl.registrationDoc.date" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:choose>
								<c:when test="${property.basicProperty.regdDocDate != null}">
									<fmt:formatDate value="${property.basicProperty.regdDocDate}"
										pattern="dd/MM/yyyy" />
								</c:when>
								<c:otherwise>
									N/A
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-3">
							<spring:message code="lbl.bp.no" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:out default="N/A"
								value="${property.propertyDetail.buildingPermissionNo}" />
						</div>
						<div class="col-xs-3">
							<spring:message code="lbl.bp.date" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:choose>
								<c:when test="${property.propertyDetail.buildingPermissionDate}">
									<fmt:formatDate
										value="${property.propertyDetail.buildingPermissionDate}"
										pattern="dd/MM/yyyy" />
								</c:when>
								<c:otherwise>
									N/A
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-3">
							<spring:message code="lbl.deviation.percentage" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:out default="N/A"
								value="${property.propertyDetail.deviationPercentage}" />
						</div>
						<div class="col-xs-3">
							<spring:message code="lbl.reason.creation"></spring:message>
						</div>
						<div class="col-sm-3 add-margin view-content">
							<c:out
								value="${property.propertyDetail.propertyMutationMaster.mutationName}" />
						</div>
					</div>
				</div>
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.exemption.heading" />
					</div>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-xs-3">
							<spring:message code="lbl.exemption.reason" />
						</div>
						<div class="col-sm-3 add-margin">
							<form:select path="taxExemptedReason" id="taxExemptedReason"
								data-first-option="false" cssClass="form-control"
								required="required">
								<form:option value="-1">
									<spring:message code="lbl.option.select" />
								</form:option>
								<form:option value="-2">
									<spring:message code="lbl.option.none" />
								</form:option>
								<form:options items="${taxExemptionReasons}" itemValue="id"
									itemLabel="name" />
							</form:select>
							<form:errors path="taxExemptedReason"
								cssClass="add-margin error-msg" />
						</div>
					</div>
				</div>
				<div class="row">
					<div class="text-center">
						<button type="button" id="btnsubmit" class="btn btn-primary">
							<spring:message code="lbl.search" />
						</button>
						<button type="button" class="btn btn-default" data-dismiss="modal"
							onclick="window.close();">
							<spring:message code="lbl.close" />
						</button>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</div>
<script type="text/javascript"
	src="<c:url value='/resources/js/app/taxExemption.js'/>"></script>