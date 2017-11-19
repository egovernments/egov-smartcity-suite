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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>

<div class="row" id="page-content">
	<div class="col-md-12">
		<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert">
				<spring:message code="${message}" />
			</div>
		</c:if>

		<form:form action="" name="deactivation-form"
			class="form-horizontal form-groups-bordered" id="deactivation-form"
			modelAttribute="propertyDeactivation" enctype="multipart/form-data">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.deactivation.title" />
					</div>
					<c:if test="${hasActiveWC}">
						<div class="panel-heading">
							<span style="font-size: 16px; color: red">
								<p class="text-center">
									<spring:message code="lbl.active.wcdetails.ack" />
								</p>
							</span>
						</div>
					</c:if>
				</div>

				<div class="panel-body custom-form">
					<div class="form-group">
						<label for="Field-1" class="col-sm-4 control-label"><spring:message
								code="lbl.assessment" /><span class="mandatory"></span></label>

						<div class="col-sm-3 add-margin">
							<form:input id="basicproperty" path="basicproperty" type="text"
								value="${basicproperty}" cssClass="form-control is_valid_number"
								autocomplete="off" required="required" maxlength="10" />
							<form:errors path="basicproperty" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-4 control-label"><spring:message
								code="lbl.deactivation.reason" /><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select id="reasonMaster" path="reasonMaster"
								cssClass="form-control" cssErrorClass="form-control error"
								required="required" value="${reasonMaster}">

								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${deactivationReasons}" />
							</form:select>

							<form:errors path="reasonMaster" cssClass="error-msg" />

						</div>
						<div style="display: none;" id="reasonMstrHidden"></div>
					</div>
					<div class="form-group display-hide" id="orgnlAssmnt">
						<label for="Field-1" class="col-sm-4 control-label"><spring:message
								code="lbl.original.assessment" /><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input id="originalAssessment" path="originalAssessment"
								type="text" value="${originalAssessment}"
								cssClass="form-control is_valid_number" autocomplete="off"
								required="required" maxlength="10" />
							<form:errors path="originalAssessment" cssClass="error-msg" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<button type="button" class="btn btn-primary add-margin"
						id="submitBtn">
						<spring:message code="lbl.submit" />
					</button>
					<button type="button" class="btn btn-default"
						onclick="self.close()">
						<spring:message code="lbl.close" />
					</button>
				</div>
			</div>
			<br>
			<c:if test="${propDetails!=null || orgPropDetails!=null}">
				<div class="panel panel-primary">
					<div class="panel-heading">
						<div class="panel-title">
							<p class="text-center">
								<spring:message code="lbl.hdr.propertytodeactivate" />
							</p>
							<p>
								<spring:message code="lbl.hdr.propertydetails" />
							</p>
						</div>
					</div>
					<c:if test="${propDetails!=null}">
						<div class="panel-body">
							<table class="table table-bordered" width="100%">
								<tr>
									<th align="center" class="bluebgheadtd">Assessment Number</th>
									<th align="center" class="bluebgheadtd">Owner Name</th>
									<th align="center" class="bluebgheadtd" style="width: 15%">Property
										Type</th>
									<th align="center" class="bluebgheadtd" style="width: 7%">Tax
										Due</th>
									<th align="center" class="bluebgheadtd">Address</th>
								</tr>
								<tr>
									<td align="center"><c:out
											value="${propDetails.AssessmentNo}" default="N/A" /></td>
									<td align="center"><c:out value="${propDetails.OwnerName}"
											default="N/A" /></td>
									<td align="center"><c:out
											value="${propDetails.PropertyType}" default="N/A" /></td>
									<td align="center"><c:out
											value="${propDetails.PropertyTaxDue}" default="N/A" /></td>
									<td align="center"><c:out value="${propDetails.Address}"
											default="N/A" /></td>
								</tr>
							</table>
							<div class="panel-heading">
								<div class="panel-title">
									<spring:message code="lbl.hdr.wcdetails" />
								</div>
							</div>

							<table class="table table-bordered" width="100%">
								<tr>
									<th align="center" class="bluebgheadtd">Consumer Number</th>
									<th align="center" class="bluebgheadtd">Connection Type</th>
									<th align="center" class="bluebgheadtd">Tax Due</th>
									<th align="center" class="bluebgheadtd"">status</th>
								</tr>
								<c:if test="${not empty wcDetails}">
									<c:forEach var="rowObj" items="${wcDetails}">
										<tr>
											<td align="center"><c:out value="${rowObj.consumerCode}"
													default="N/A" /></td>
											<td align="center"><c:out
													value="${rowObj.connectionType}" default="N/A" /></td>
											<td align="center"><c:out value="${rowObj.totalTaxDue}"
													default="N/A" /></td>
											<td align="center"><c:out
													value="${rowObj.connectionStatus}" default="N/A" /></td>
										</tr>
									</c:forEach>
								</c:if>
							</table>
						</div>
					</c:if>
				</div>
				<c:if
					test="${orgPropDetails!=null && reasonMaster!='Bogus Property'}">
					<div class="panel panel-primary">
						<div class="panel-heading">
							<div class="panel-title">
								<p class="text-center">
									<spring:message code="lbl.hdr.originalpropertydtls" />
								</p>
								<p>
									<spring:message code="lbl.hdr.propertydetails" />
								</p>
							</div>
						</div>

						<div class="panel-body ">
							<table class="table table-bordered" width="100%">
								<tr>
									<th align="center" class="bluebgheadtd">Assessment Number</th>
									<th align="center" class="bluebgheadtd">Owner Name</th>
									<th align="center" class="bluebgheadtd" style="width: 15%">Property
										Type</th>
									<th align="center" class="bluebgheadtd" style="width: 7%">Tax
										Due</th>
									<th align="center" class="bluebgheadtd">Address</th>
								</tr>
								<tr>
									<td align="center"><c:out
											value="${orgPropDetails.AssessmentNo}" default="N/A" /></td>
									<td align="center"><c:out
											value="${orgPropDetails.OwnerName}" default="N/A" /></td>
									<td align="center"><c:out
											value="${orgPropDetails.PropertyType}" default="N/A" /></td>
									<td align="center"><c:out
											value="${orgPropDetails.PropertyTaxDue}" default="N/A" /></td>
									<td align="center"><c:out
											value="${orgPropDetails.Address}" default="N/A" /></td>
								</tr>
							</table>


							<div class="panel-heading">
								<div class="panel-title">
									<spring:message code="lbl.hdr.wcdetails" />
								</div>
							</div>
							<c:if test="${not empty orgPropWCDetails}">
								<table class="table table-bordered" width="100%">
									<tr>
										<th align="center" class="bluebgheadtd">Consumer Number</th>
										<th align="center" class="bluebgheadtd">Connection Type</th>
										<th align="center" class="bluebgheadtd">Tax Due</th>
										<th align="center" class="bluebgheadtd"">Status</th>
									</tr>
									<c:forEach var="connObj" items="${orgPropWCDetails}">
										<tr>
											<td align="center"><c:out
													value="${connObj.consumerCode}" default="N/A" /></td>
											<td align="center"><c:out
													value="${connObj.connectionType}" default="N/A" /></td>
											<td align="center"><c:out value="${connObj.totalTaxDue}"
													default="N/A" /></td>
											<td align="center"><c:out
													value="${connObj.connectionStatus}" default="N/A" /></td>
										</tr>
									</c:forEach>

								</table>
							</c:if>

						</div>

					</div>
				</c:if>
				<c:if test="${hasActiveWC !=true}">
					<div class="panel panel-primary">
						<div class="panel-heading">
							<div class="panel-title">
								<p class="text-center">
									<spring:message code="lbl.council.doc" />
								</p>
							</div>
						</div>
						<%@ include file="/WEB-INF/jsp/common/documents-upload.jsp"%>
						<div class="panel-body">
							<label for="Field-1" class="col-sm-3 control-label"><spring:message
									code="lbl.council.no" /><span class="mandatory"></span></label>

							<div class="col-sm-3 add-margin">
								<form:input id="councilno" path="councilno" type="text"
									value="${councilno}" cssClass="form-control" autocomplete="off"
									required="required" maxlength="10" />
								<form:errors path="councilno" cssClass="error-msg" />
							</div>
							<label class="col-sm-2 control-label text-right"><spring:message
									code="lbl.council.date" /> <span class="mandatory"></span> </label>
							<div class="col-sm-2 add-margin">
								<form:input type="text" cssClass="form-control dateval"
									path="councilDate" id="councilDate" data-date-end-date="0d"
									required="required" />
								<form:errors path="councilDate" cssClass="error-msg" />
							</div>
						</div>
					</div>
					<br>
					<div class="row">
						<div class="text-center">
							<button type="button" class="btn btn-primary add-margin"
								id="deactBtn" align="center">
								<spring:message code="lbl.btn.deactivate" />
							</button>
							<button type="button" class="btn btn-default"
								onclick="self.close()">
								<spring:message code="lbl.close" />
							</button>
						</div>

					</div>
				</c:if>
			</c:if>
		</form:form>
	</div>
</div>
<br />
<br />

<script src="<cdn:url value='/resources/js/app/deactivation.js'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>">

