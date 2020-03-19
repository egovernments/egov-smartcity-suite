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
<%@ include file="/includes/taglibs.jsp"%>

<html>
<head>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/writeoff.js'/>">
	
</script>

<style>
body {
	font-family: regular !important;
	font-size: 14px;
}

div.overflow-x-scroll {
	overflow-x: scroll;
}

div.floors-tbl-freeze-column-div {
	margin-right: 80px;
}

.freeze-action-th {
	position: absolute;
	right: 20px;
	padding: 4px 2px !important;
	min-height: 40px;
	min-width: 90px !important;
	line-height: 30px;
}

.freeze-action-td {
	position: absolute;
	right: 20px;
	padding: 4px 2px;
	padding-bottom: 4px;
	height: 40px;
	width: 90px;
}

.postion {
	padding-left: 50px;
}
</style>
<title><spring:message code="lbl.writeOff.title" /></title>
</head>
<body>

	<c:if test="${not empty errorMsg}">
		<div class="alert alert-danger" role="alert">
			<c:forEach var="error" items="${errorMsg}" varStatus="status">
				<spring:message code="${error.value}" />
				<br />
			</c:forEach>
		</div>
	</c:if>

	<form:form method="post" id="writeOffForm" commandName="writeOff"
		theme="simple" enctype="multipart/form-data" modelAttribute="writeOff">
		<form:hidden path="" name="loggedUserIsEmployee"
			id="loggedUserIsEmployee" value="${loggedUserIsEmployee}" />
		<form:hidden path="" name="instString" id="instString"
			value="${instString}" />
		<form:hidden path="" name="state" id="state"
			value="${writeOff.getState().getValue()}" />
		<form:hidden path="" name="propertyDeactivateFlag"
			id="propertyDeactivateFlag"
			value="${writeOff.propertyDeactivateFlag}" />
		<div class="panel-heading">
			<ul class="nav nav-tabs" id="tabs">
				<li class="First Active"><a data-toggle="tab"
					href="#writeoffdetails" data-tabidx="0" aria-expanded="true"><spring:message
							code="lbl.writeOff.title" /></a></li>
				<li class="" id="demand"><a data-toggle="tab"
					href="#demanddetails" data-tabidx="1" aria-expanded="false""><spring:message
							code="lbl.cv.demand" /></a></li>
			</ul>
		</div>
		<div class="panel-body custom-form">
			<div class="tab-content">

				<div class="tab-pane fade active in" id="writeoffdetails">
					<div class="row">
						<div class="col-md-12">
							<div class="panel panel-primary" data-collapsed="0"
								style="text-align: left">
								<div class="panel-heading">
									<div class="panel-title">
										<spring:message code="lbl.hdr.propertydetails" />
									</div>
								</div>
								<div class="panel-body">
									<div class="row add-border">
										<div class="col-xs-3 add-margin">
											<spring:message code="lbl.assmtno" />
										</div>
										<div class="col-xs-3 add-margin view-content">
											<c:out value="${property.basicProperty.upicNo}">
											</c:out>
										</div>
										<div class="col-xs-3 add-margin">
											<spring:message code="lbl.cv.doorNo" />
										</div>
										<div class="col-xs-3 add-margin view-content">
											<c:out
												value="${property.basicProperty.address.houseNoBldgApt }"></c:out>
										</div>
									</div>
									<div class="row add-border">
										<div class="col-xs-3 add-margin">
											<spring:message code="lbl.cv.ownerName" />
										</div>
										<div class="col-xs-3 add-margin view-content">
											<c:out
												value="${property.basicProperty.primaryOwner.name.toString()} "></c:out>
										</div>
										<div class="col-xs-3 add-margin">
											<spring:message code="lbl.cv.propAdd" />
										</div>
										<div class="col-xs-3 add-margin view-content">
											<c:out value="${property.basicProperty.address }"></c:out>
										</div>
									</div>
								</div>

							</div>
						</div>
					</div>
					<!-- Water Connection Details -->
					<div class="row">
						<div class="col-md-12">
							<div class="panel panel-primary" data-collapsed="0"
								style="text-align: left">
								<div class="panel-heading">
									<div class="panel-title">
										<spring:message code="lbl.cv.wcdetails" />
									</div>
								</div>
								<c:if test="${not empty wcDetails}">
									<c:forEach items="${wcDetails}" var="wc">
										<div class="panel-body">
											<c:if
												test="${wc.connectionStatus == 'ACTIVE' || wc.connectionStatus == 'active'}">
												<div class="row add-border">
													<div class="col-xs-3 add-margin">
														<spring:message code="lbl.cv.consumerNo" />
													</div>
													<div class="col-xs-3 add-margin view-content">
														<c:out value="${wc.consumerCode}"></c:out>
													</div>
													<div class="col-xs-3 add-margin">
														<spring:message code="lbl.cv.connStatus" />
													</div>
													<div class="col-xs-3 add-margin view-content">
														<c:out value="${wc.connectionStatus }"></c:out>
													</div>
												</div>
												<div class="row add-border">
													<div class="col-xs-3 add-margin">
														<spring:message code="lbl.cv.connType" />
													</div>
													<div class="col-xs-3 add-margin view-content">
														<c:out value="${wc.connectionType}"></c:out>
													</div>
													<div class="col-xs-3 add-margin">
														<spring:message code="lbl.cv.hlfyearcharg" />
													</div>
													<div class="col-xs-3 add-margin view-content">
														<c:out value="${wc.halfYearlyTax}"></c:out>
													</div>
												</div>
												<div class="row add-border">
													<div class="col-xs-3 add-margin">
														<spring:message code="lbl.cv.wtrchrgdue" />
													</div>
													<div class="col-xs-3 add-margin view-content">
														<c:out value="${wc.totalTaxDue}"></c:out>
													</div>
												</div>
											</c:if>
											<c:if
												test="${wc.connectionStatus == 'INPROGRESS' || wc.connectionStatus == 'inprogress'}">
												<div class="row add-border">
													<div class="col-xs-3 add-margin">
														<spring:message code="lbl.applicationNo" />
													</div>
													<div class="col-xs-3 add-margin view-content">
														<c:out value="${wc.applicationNumber }"></c:out>
													</div>
													<div class="col-xs-3 add-margin">
														<spring:message code="lbl.wo.applicationdate" />
													</div>
													<div class="col-xs-3 add-margin view-content">
														<c:out value="${wc.applicationDate }"></c:out>
													</div>
												</div>
												<div class="row add-border">
													<div class="col-xs-3 add-margin">
														<spring:message code="lbl.cv.connStatus" />
													</div>
													<div class="col-xs-3 add-margin view-content">
														<c:out value="${wc.connectionStatus }"></c:out>
													</div>
													<div class="col-xs-3 add-margin">
														<spring:message code="lbl.cv.connType" />
													</div>
													<div class="col-xs-3 add-margin view-content">
														<c:out value="${wc.connectionType}"></c:out>
													</div>
												</div>
											</c:if>
										</div>
									</c:forEach>
								</c:if>
								<c:if test="${empty wcDetails}">
									<div class="row add-border">
										<div class="col-xs-3">*No Water Tap Connection Details</div>
									</div>
								</c:if>
							</div>
						</div>
					</div>
					<!-- Sewerage Connection Details -->
					<div class="row">
						<div class="col-md-12">
							<div class="panel panel-primary" data-collapsed="0"
								style="text-align: left">
								<div class="panel-heading">
									<div class="panel-title">
										<spring:message code="lbl.cv.sewdetails" />
									</div>
								</div>
								<c:if test="${hasActiveSwg == true}">
									<c:forEach items="${sewConnDetails}" var="sc">
										<div class="panel-body">
											<div class="row add-border">
												<div class="col-xs-3 add-margin">
													<spring:message code="lbl.cv.sewConnNo" />
												</div>
												<div class="col-xs-3 add-margin view-content">
													<c:out value="${sc.consumerCode.NEWSEWERAGECONNECTION}"></c:out>
												</div>
												<div class="col-xs-3 add-margin">
													<spring:message code="lbl.cv.closets" />
												</div>
												<div class="col-xs-3 add-margin view-content">
													<c:out value="${sc.noOfClosets}"></c:out>
												</div>
											</div>
											<div class="row add-border">
												<div class="col-xs-3 add-margin">
													<spring:message code="lbl.cv.hlfyearcharg" />
												</div>
												<div class="col-xs-3 add-margin view-content">
													<c:out value="${sc.halfYearlyTax}"></c:out>
												</div>
												<div class="col-xs-3 add-margin">
													<spring:message code="lbl.cv.sewchrgdue" />
												</div>
												<div class="col-xs-3 add-margin view-content">
													<c:out value="${sc.totalTaxDue}"></c:out>
												</div>
											</div>
										</div>
									</c:forEach>
								</c:if>
								<c:if test="${hasActiveSwg == false}">
									<div class="row add-border">
										<div class="col-xs-3">*No Sewerage Connection Details</div>
									</div>
								</c:if>
							</div>
						</div>
					</div>
					<!-- Write-Off Details -->

					<div class="row">
						<div class="col-md-12">
							<div class="panel panel-primary" data-collapsed="0"
								style="text-align: left">
								<div class="panel-heading">
									<div class="panel-title">
										<spring:message code="lbl.writeOff.details" />
									</div>
								</div>
								<div class="panel-body">
									<div class="row add-border">
										<div class="col-xs-2 add-margin" style="padding-left: 50px;">
											<spring:message code="lbl.writeOffTypes" />
											<span class="mandatory"></span>
										</div>
										<div class="col-xs-2 add-margin view-content">
											<form:select path="writeOffType.code" id="writeOffType"
												cssClass="form-control"
												onchange="enablecheckbox();displayreasons();">
												<form:option value="">
													<spring:message code="lbl.select" />
												</form:option>
												<form:options items="${type}" itemValue="code"
													itemLabel="mutationDesc" />
											</form:select>
											<form:errors path="writeOffType.mutationDesc"
												cssClass="add-margin error-msg" />
										</div>
										<div class="col-xs-3 add-margin" style="padding-left: 150px;">
											<spring:message code="lbl.writeOff.reasons" />
											<span class="mandatory"></span>
										</div>
										<div class="col-xs-3 add-margin view-content">
											<form:select id="reasons" path="writeOffReasons.code"
												cssClass="form-control" style="width: 80%">
												<form:option value="">
													<spring:message code="lbl.select" />
												</form:option>
												<form:options items="${reasonsList}" itemValue="code"
													itemLabel="name" />
											</form:select>
											<form:errors path="writeOffReasons.name"
												cssClass="add-margin error-msg" />
										</div>
									</div>
									<c:if test="${writeOff.propertyDeactivateFlag == false}">
										<div id="check" style="display: none;">
											<spring:message code="lbl.writeoff.deactivation.checkbox" />
											<input type="checkbox" id="fullwriteoffcheckbox"
												class="check_box" name="checkbox">
										</div>
									</c:if>
									<c:if test="${writeOff.propertyDeactivateFlag == true}">
										<div id="check" style="display: block;">
											<spring:message code="lbl.writeoff.deactivation.checkbox" />
											<input type="checkbox" id="fullwriteoffcheckbox"
												class="check_box" name="checkbox" checked="checked">
										</div>
									</c:if>
								</div>

							</div>
						</div>
					</div>

					<!-- Document Details -->
					<div class="row">
						<div class="col-md-12">
							<div class="panel panel-primary" data-collapsed="0"
								style="text-align: left">
								<div class="panel-heading">
									<div class="panel-title">
										<spring:message code="lbl.doc.details" />
									</div>
								</div>
								<div class="panel-body">
									<div class="row add-border">
										<div class="col-xs-2 add-margin postion">
											<spring:message code="lbl.resolution.type" />
											<span class="mandatory"></span>
										</div>
										<div class="col-xs-2 add-margin view-content">
											<form:select cssClass="form-control" path="resolutionType"
												id="resolutionType" name="resolutionType"
												cssStyle="width: 100%">
												<form:option value="">Select</form:option>
												<form:option value="MPl. Council">Council</form:option>
												<form:option value="Standing Committee">Standing Committee</form:option>
											</form:select>
											<form:errors path="resolutionType"
												cssClass="add-margin error-msg" />
										</div>
										<div class="col-xs-2 add-margin postion">
											<spring:message code="lbl.resolution.no" />
											<span class="mandatory"></span>
										</div>
										<div class="col-xs-2 add-margin view-content">
											<form:input id="resolutionNo" path="resolutionNo" type="text"
												value="${resolutionNo}" cssClass="form-control"
												onchange="getcouncilrequest()" />
											<form:errors path="resolutionNo"
												cssClass="add-margin error-msg" />
										</div>
										<div class="col-xs-2 add-margin postion">
											<spring:message code="lbl.resolution.date" />
										</div>
										<div class="col-xs-2 add-margin">
											<form:input id="resolutionDate" path="resolutionDate"
												value="${resolutionDate}" cssClass="form-control"
												readonly="true" />
										</div>
										<div class="col-xs-3 add-margin" id="viewlink">
											<a id="url" href="#" target="_blank">Council management
												verification link</a>
										</div>
									</div>

								</div>
							</div>
						</div>
					</div>

					<div class="panel panel-primary">
						<div class="panel-body custom-form">
							<%@ include
								file="../../transactions/writeOff/writeoff-document-upload.jsp"%>
						</div>

					</div>
				</div>
				<div id="demanddetails" class="tab-pane fade">
					<%@ include
						file="../../transactions/writeOff/Writeoff-demand-details.jsp"%>
				</div>

			</div>
			<c:if test="${state != null}">
				<tr>
					<jsp:include page="../../common/workflowHistoryView.jsp" />
				<tr>
			</c:if>
			<div class="row">
				<div class="col-md-12">
					<jsp:include page="/WEB-INF/views/common/commonWorkflowMatrix.jsp" />
				</div>
			</div>

			<div id="loadingMask" style="display: none">
				<p align="center">
					<img src="/ptis/resources/erp2/images/bar_loader.gif"> <span
						id="message"><p style="color: red">Please wait....</p></span>
				</p>
			</div>
			<div class="buttonbottom" align="center">
				<%@ include
					file="/WEB-INF/views/common/commonWorkflowMatrix-button.jsp"%>
			</div>
	</form:form>
</body>
</html>