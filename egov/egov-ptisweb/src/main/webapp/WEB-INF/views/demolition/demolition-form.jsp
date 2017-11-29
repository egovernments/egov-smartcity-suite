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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<style>
body
{
  font-family:regular !important;
  font-size:14px;
}
</style>
<script type="text/javascript"
	src="<cdn:url value='/resources/javascript/validations.js'/>"></script>

<form:form id="demolition" method="post"
	class="form-horizontal form-groups-bordered" modelAttribute="property">
	<div class="page-container" id="page-container">
		<div class="main-content">
			<div>
				<spring:hasBindErrors name="property"> 
		 			<div class="alert alert-danger col-md-10 col-md-offset-1">
		  				<form:errors path="*" cssClass="error-msg add-margin" /><br/>
		      		</div>
				</spring:hasBindErrors>
			</div>
			<jsp:include page="../common/commonPropertyDetailsView.jsp"></jsp:include>
			<jsp:include page="../common/ownerDetailsView.jsp"></jsp:include>

			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0">

						<div class="panel-heading" style="text-align: left">
							<div class="panel-title">
								<spring:message code="lbl.hdr.demolition.details" />
							</div>
						</div>
						<div class="panel-body custom-form">
							<div class="form-group">
								<label class="col-sm-3 control-label text-right"><spring:message
										code="lbl.demolition.reason" /><span class="mandatory"></span>
								</label>
								<div class="col-sm-8 add-margin">
									<form:textarea path="demolitionReason" class="form-control"></form:textarea>
									<form:errors path="demolitionReason"
										cssClass="add-margin error-msg" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0">

						<div class="panel-heading" style="text-align: left">
							<div class="panel-title">
								<spring:message code="lbl.hdr.vacantland.details" />
							</div>
						</div>
						<form:hidden path="" name="mode" id="mode" value="${mode}" />
						<table width="100%" border="0" cellspacing="0" cellpadding="0"
							class="table table-bordered" id="vacantLandTable">

							<tbody>
								<tr>
									<th class="bluebgheadtd"><spring:message
											code="lbl.surveyNumber" /><span class="mandatory"></span></th>
									<th class="bluebgheadtd"><spring:message
											code="lbl.pattaNumber" /><span class="mandatory"></span></th>
									<th class="bluebgheadtd"><spring:message
											code="lbl.vacantland.area" /><span class="mandatory"></span></th>
									<th class="bluebgheadtd"><spring:message
											code="lbl.MarketValue" /><span class="mandatory"></span></th>
									<th class="bluebgheadtd"><spring:message
											code="lbl.currentCapitalValue" /><span class="mandatory"></span></th>
								</tr>

								<tr id="vacantLandRow">
									<td class="blueborderfortd" align="center">
										<form:input
												path="propertyDetail.surveyNumber" id="surveyNumber"
												maxlength="15" cssClass="form-control" /> 
										<form:errors
												path="propertyDetail.surveyNumber"
												cssClass="add-margin error-msg" />
									</td>
									<td class="blueborderfortd" align="center">
									     <form:input
											path="propertyDetail.pattaNumber" id="pattaNumber"
											maxlength="15" 
											cssClass="form-control"/> 
											<form:errors
											path="propertyDetail.pattaNumber"
											cssClass="add-margin error-msg" />
									</td>
									<td class="blueborderfortd" align="center">
									    <form:input
											type="text" path="propertyDetail.sitalArea.area"
											maxlength="15" value="" id="vacantLandArea"
											cssClass="form-control"
											onblur="trim(this,this.value);checkForTwoDecimals(this,'propertyDetail.sitalArea.area');checkZero(this,'propertyDetail.sitalArea.area');" />
										<form:errors path="propertyDetail.sitalArea.area"
											cssClass="add-margin error-msg" />
									</td>
									<td class="blueborderfortd" align="center">
									      <form:input
											type="text" path="propertyDetail.marketValue" maxlength="15"
											value="" id="marketValue"
											cssClass="form-control"
											onblur="trim(this,this.value);checkForTwoDecimals(this,'propertyDetail.marketValue');checkZero(this,'propertyDetail.marketValue');" />
										<form:errors path="propertyDetail.marketValue"
											cssClass="add-margin error-msg" />
									</td>

									<td class="blueborderfortd">
									    <form:input type="text"
											path="propertyDetail.currentCapitalValue" maxlength="15"
											value="" id="currentCapitalValue"
											cssClass="form-control"
											onblur="trim(this,this.value);checkForTwoDecimals(this,'propertyDetail.currentCapitalValue');checkZero(this,'propertyDetail.currentCapitalValue');"/> 	
										<form:errors path="propertyDetail.currentCapitalValue"
											cssClass="add-margin error-msg" />
									</td>

								</tr>

								<tr>
									<td colspan="6"><br>
										<table class="table table-bordered" style="width: 100%;"
											id="boundaryData">
											<tbody>
												<tr>
													<th class="bluebgheadtd">North<span class="mandatory"></span></th>
													<th class="bluebgheadtd">East<span class="mandatory"></span></th>
													<th class="bluebgheadtd">West<span class="mandatory"></span></th>
													<th class="bluebgheadtd">South<span class="mandatory"></span></th>
												</tr>
												<tr>
													<td class="blueborderfortd" align="center">
													       <form:input
															type="text" id="northBoundary"
															path="basicProperty.propertyID.northBoundary"
															cssClass="form-control"
															maxlength="64"></form:input> <form:errors
															path="basicProperty.propertyID.northBoundary"
															cssClass="add-margin error-msg" /></td>
													<td class="blueborderfortd" align="center"><form:input
															type="text" id="eastBoundary"
															path="basicProperty.propertyID.eastBoundary"
															cssClass="form-control"
															maxlength="64"></form:input> <form:errors
															path="basicProperty.propertyID.eastBoundary"
															cssClass="add-margin error-msg" /></td>
													<td class="blueborderfortd" align="center"><form:input
															type="text" id="westBoundary"
															path="basicProperty.propertyID.westBoundary"
															cssClass="form-control"
															maxlength="64"></form:input> <form:errors
															path="basicProperty.propertyID.northBoundary"
															cssClass="add-margin error-msg" /></td>
													<td class="blueborderfortd" align="center"><form:input
															type="text" id="southBoundary"
															path="basicProperty.propertyID.southBoundary"
															cssClass="form-control"
															maxlength="64"></form:input> <form:errors
															path="basicProperty.propertyID.southBoundary"
															cssClass="add-margin error-msg" /></td>
												</tr>
											</tbody>
										</table></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<c:if test="${isEmployee}">
				<jsp:include page="../common/commonWorkflowMatrix.jsp" />
			</c:if>
			<jsp:include page="../common/commonWorkflowMatrix-button.jsp" />

		</div>
	</div>
</form:form>
