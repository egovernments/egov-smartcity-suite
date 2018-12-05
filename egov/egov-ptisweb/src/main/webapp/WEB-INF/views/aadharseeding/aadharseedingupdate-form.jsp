<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2018  eGovernments Foundation
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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="aadharSeedingError" class="errorstyle" style="color: red"></div>
<form:form role="form" action="" modelAttribute="aadharSeedingUpdate"
	method="POST" id="aadharUpdateForm"
	class="form-horizontal form-groups-bordered">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.hdr.propertydetails" />
					</div>
				</div>
				<div class="panel-body">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.assessmentNo" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out
									value="${aadharSeedingUpdate.assessmentNo}" /> </strong>

						</div>
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.category.ownership" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out
									value="${aadharSeedingUpdate.ownershipCategory}" /> </strong>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.doorNumber" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${aadharSeedingUpdate.doorNo}"
									default="N/A" /> </strong>
						</div>
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.address" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${aadharSeedingUpdate.address}" />
							</strong>
						</div>
					</div>
				</div>
			</div>
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.location.details" />
					</div>
				</div>
				<div class="panel-body">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.revenue.ward" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out
									value="${aadharSeedingUpdate.revenueWardName}" /> </strong>
						</div>
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.election.ward" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out
									value="${aadharSeedingUpdate.electionWardName}" default="N/A" />
							</strong>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.revenue.block" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${aadharSeedingUpdate.blockName}"
									default="N/A" /> </strong>
						</div>
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.locality" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${aadharSeedingUpdate.localty}"
									default="N/A" /> </strong>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.revenue.zone" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${aadharSeedingUpdate.zoneName}"
									default="N/A" /> </strong>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.latitude" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${aadharSeedingUpdate.latitude}"
									default="N/A" /> </strong>
						</div>
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.longitude" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${aadharSeedingUpdate.longitude}"
									default="N/A" /> </strong>
						</div>
					</div>
				</div>
			</div>
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.assessmentDetails" />
					</div>
				</div>
				<div class="panel-body">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.extentofsite" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out
									value="${aadharSeedingUpdate.extentOfSite}" default="N/A" /> </strong>
						</div>

						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.plinth.area" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${aadharSeedingUpdate.plinthArea}"
									default="N/A" /> </strong>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.propertytype" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out
									value="${aadharSeedingUpdate.propertyType}" default="N/A" /> </strong>
						</div>
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.surveyNumber" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:choose>
								<c:when test="${aadharSeedingUpdate.surveyNumber.isEmpty()}">
								<strong>N/A</strong>
								</c:when>
								<c:otherwise>
									<strong><c:out
											value="${!aadharSeedingUpdate.surveyNumber.isEmpty()}" default="N/A" />
									</strong>

								</c:otherwise>
								</c:choose>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.docNo" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${aadharSeedingUpdate.docNo}"
									default="N/A" /> </strong>
						</div>
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.docDate" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<fmt:formatDate value="${aadharSeedingUpdate.docDate}"
								pattern="dd/MM/yyyy" var="docdate" />
							<strong> <c:out value="${docdate}"
									default="N/A" />
							</strong>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<label><spring:message code="lbl.docType" /></label>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<strong><c:out value="${aadharSeedingUpdate.documentType}"
									default="N/A" /> </strong>
						</div>
					</div>
				</div>
			</div>
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.hdr.ownerdetails" />
					</div>
				</div>
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title">
							<spring:message code="lbl.as.per.erp" />
						</div>
					</div>

					<table class="table table-striped table-bordered"
						id="ownerDetailsERP">
						<thead>
							<tr>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.adharno" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.MobileNumber" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.OwnerName" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.gender" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.EmailAddress" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.Guardian" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.GuardianRelation" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${aadharSeedingUpdate.propertyOwnerInfo}"
								var="ownerInfo">
								<tr>
									<td>
										<div>
											<strong><c:out
													value="${ownerInfo.owner.aadhaarNumber}" default="N/A" />
											</strong>
										</div>
									</td>
									<td>
										<div>
											<strong><c:out
													value="${ownerInfo.owner.mobileNumber}" default="N/A" /> </strong>
										</div>
									</td>
									<td>
										<div>
											<strong><c:out value="${ownerInfo.owner.name}"
													default="N/A" /> </strong>
										</div>
									</td>
									<td>
										<div>
											<strong><c:out value="${ownerInfo.owner.gender}"
													default="N/A" /> </strong>
										</div>
									</td>
									<td>
										<div>
											<strong><c:out value="${ownerInfo.owner.emailId}"
													default="N/A" /> </strong>
										</div>
									</td>
									<td>
										<div>
											<strong><c:out value="${ownerInfo.owner.guardian}"
													default="N/A" /> </strong>
										</div>
									</td>
									<td>
										<div>
											<strong><c:out
													value="${ownerInfo.owner.guardianRelation}" default="N/A" />
											</strong>
										</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>

					</table>
				</div>
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title">
							<spring:message code="lbl.as.per.aadhar" />
						</div>
					</div>
					<table class="table table-striped table-bordered"
						id="ownerDetailsAadhar">
						<thead>
							<tr>

								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.adharno" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.MobileNumber" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.OwnerName" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.gender" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.EmailAddress" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.Guardian" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.GuardianRelation" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.image" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${aadharSeedingUpdate.propertyOwnerInfo}"
								var="aadharOwnerInfo" varStatus="status">
								<tr>
									<td class="blueborderfortd" align="center"><form:input
											path="propertyOwnerInfo[${status.index}].owner.aadhaarNumber"
											cssClass="form-control patternvalidation loaddetails"
											data-pattern="number" id="aadharNumber" maxlength="12"
											readonly="true" /></td>
									<td class="blueborderfortd" align="center"><form:input
											path="" id="mobileNumber" maxlength="10"
											cssClass="form-control patternvalidation"
											data-pattern="number" readonly="true" /></td>
									<td class="blueborderfortd" align="center"><form:input
											path="" id="name" maxlength="74" cssClass="form-control"
											readonly="true" /></td>

									<td class="blueborderfortd" align="center"><form:input
											path="" id="sgender" name="gender" data-first-option="false"
											cssClass="form-control" readonly="true" /></td>
									<td class="blueborderfortd" align="center"><form:input
											path="" id="emailId" maxlength="32" cssClass="form-control"
											readonly="true" /></td>
									<td class="blueborderfortd" align="center"><form:input
											path="" id="guardianRelation" name="guardianRelation"
											data-first-option="false" cssClass="form-control"
											readonly="true" /></td>
									<td class="blueborderfortd" align="center"><form:input
											path="" id="guardianName" maxlength="32"
											cssClass="form-control" readonly="true" /></td>
									<td class="blueborderfortd" align="center"><img
										height="100" width="100"
										src="data:image/jpg;base64,<c:out value=''/>" /></td>
								</tr>
							</c:forEach>
						</tbody>

					</table>
				</div>
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title">
							<spring:message code="lbl.update.aadhar" />
						</div>
					</div>
					<table class="table table-striped table-bordered" id="updateAadhar">
						<thead>
							<tr>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.adharno" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.MobileNumber" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.OwnerName" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.gender" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.EmailAddress" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.Guardian" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.GuardianRelation" /></th>
								<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
										code="lbl.image" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${aadharSeedingUpdate.propertyOwnerInfoProxy}"
								var="aadharUpdate" varStatus="status">
								<tr>
									<c:choose>
										<c:when test="${aadharSeedingDetails != null}">
											<c:forEach items="${aadharSeedingDetails}"
												var="aadharSeedingDetail" varStatus="status">
												<c:if
													test="${aadharUpdate.owner.id == aadharSeedingDetail.owner}">
													<td class="blueborderfortd" align="center"><form:input
															path="" value="${aadharSeedingDetail.aadharNo}"
															cssClass="form-control patternvalidation loaddetails"
															data-pattern="number" id="aadharNumber" required="true"
															maxlength="12" readonly="true" /></td>
												</c:if>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<td class="blueborderfortd" align="center"><form:input
													path="propertyOwnerInfoProxy[${status.index}].owner.aadhaarNumber"
													cssClass="form-control patternvalidation loaddetails"
													data-pattern="number" id="aadharNumber" required="true"
													onblur="getOwnerByAadharDetails(this);" maxlength="12"
													readonly="false" /></td>
											<form:hidden
												path="propertyOwnerInfoProxy[${status.index}].userId"
												id="propertyOwnerInfoProxy[${status.index}].userId"
												name="propertyOwnerInfoProxy[${status.index}].userId"
												value="${aadharUpdate.owner.id}" />
										</c:otherwise>
									</c:choose>
									<td class="blueborderfortd" align="center"><form:input
											path="" id="mobileNumber" maxlength="10"
											cssClass="form-control patternvalidation"
											data-pattern="number" readonly="true" /></td>
									<td class="blueborderfortd" align="center"><form:input
											path="" id="name" maxlength="74" cssClass="form-control"
											readonly="true" /></td>
									<td class="blueborderfortd" align="center"><form:input
											path="" id="sgender" name="gender" data-first-option="false"
											cssClass="form-control" readonly="true" /></td>
									<td class="blueborderfortd" align="center"><form:input
											path="" id="emailId" maxlength="32" cssClass="form-control"
											readonly="true" /></td>
									<td class="blueborderfortd" align="center"><form:input
											path="" id="guardianRelation" name="guardianRelation"
											data-first-option="false" cssClass="form-control"
											readonly="true" /></td>
									<td class="blueborderfortd" align="center"><form:input
											path="" id="guardianName" maxlength="32"
											cssClass="form-control" readonly="true" /></td>
									<td class="blueborderfortd" align="center"><img
										height="100" width="100" src="" /></td>
								</tr>
							</c:forEach>
						</tbody>

					</table>
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<c:if test="${aadharSeedingDetails == null}">
						<button type="submit" class="btn btn-primary add-margin"
							id="submitform" disabled="disabled">
							<spring:message code="lbl.update" />
						</button>
					</c:if>
					<a href="javascript:void(0)" class="btn btn-default"
						onclick="self.close()"><spring:message code="lbl.close" /></a>
				</div>
			</div>
		</div>
	</div>

</form:form>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/aadharseeding.js?rnd=${app_release_no}'/>"></script>
<script type="text/javascript">
	jQuery(".loaddetails").each(function() {
		getOwnerByAadharDetails($(this));
	});

	function getOwnerByAadharDetails(obj) {
		var aadharNo = jQuery(obj).val();
		var tr = obj.closest('tr');
		jQuery
				.ajax({
					type : "GET",
					url : "/ptis/aadharseeding/peoplehubdata/" + aadharNo,
					cache : true
				})
				.done(
						function(value) {
							if (value.uid_NUM == aadharNo) {
								$(tr)
										.find('td:eq(1)')
										.find("input")
										.each(
												function() {
													$(this)
															.val(
																	value.mobile_NUMBER == "101" ? "NA"
																			: value.mobile_NUMBER);
												});
								$(tr).find('td:eq(2)').find("input").each(
										function() {
											$(this).val(value.citizen_NAME);
										});
								$(tr)
										.find('td:eq(3)')
										.find("input")
										.each(
												function() {
													$(this)
															.val(
																	value.gender
																			.toUpperCase());
												});
								$(tr)
										.find('td:eq(4)')
										.find("input")
										.each(
												function() {
													$(this)
															.val(
																	value.email_ID == "101" ? ""
																			: value.email_ID);
												});
								$(tr)
										.find('td:eq(5)')
										.find("input")
										.each(
												function() {
													$(this)
															.val(
																	value.care_of == "101" ? "NA"
																			: value.care_of
																					.substr(value.care_of
																							.indexOf(" ") + 1));
												});

								var relation = "";
								if (value.care_of.substring(0, 4) == "S/O:"
										|| value.care_of.substring(0, 4) == "D/O:") {
									relation = "Father";
								} else if (value.care_of.substring(0, 4) == "W/O:") {
									relation = "Husband";
								} else {
									relation = "Other";
								}
								$(tr).find('td:eq(6)').find("input").each(
										function() {
											$(this).val(relation);
										});
								var source = "data:image/jpg;base64,"
										+ <c:out value='value.base64file'/>;
								$(tr).find('td:eq(7)').find('img').attr("src",
										source)

								$('#submitform').attr('disabled', false);
							}  else {
								bootbox
										.alert("There is no Data found with the Aadhar Number!");
								$('#submitform').attr('disabled', true);
							} 
						});
	}
	window.onunload = refreshParent;
	function refreshParent() {
		window.opener.location.reload();
	}
</script>

