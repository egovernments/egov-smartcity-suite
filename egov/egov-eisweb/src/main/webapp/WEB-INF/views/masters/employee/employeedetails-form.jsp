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
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<script
	src="<cdn:url value='/resources/js/app/employeecontactdetails.js'/>"></script>
<script
	src="<cdn:url value='/resources/js/app/ajaxCommonFunctions.js'/>"></script>



<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
			<script src="/egi/resources/global/js/ie8/html5shiv.min.js"></script>
			<script src="/egi/resources/global/js/ie8/respond.min.js"></script>
		<![endif]-->

<div class="row">
	<div class="col-md-12">
		<form:form method="post" action=""
			class="form-horizontal form-groups-bordered"
			modelAttribute="employee" id="employeeForm"
			enctype="multipart/form-data">
			<c:if test="${not empty message}">
				<div id="message" class="success">${message}</div>
				<div class="alert alert-success" role="alert">${message}</div>
			</c:if>

			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title"></div>

				</div>
				<div class="panel-body">
					<div class="row add-border">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.name" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<c:out value="${employee.name}" />
						</div>

						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.gender" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<c:out value="${employee.gender}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.mobile" />
							<span class="mandatory">
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<div class="input-group">
								<span class="input-group-addon">+91</span>
								<form:input type="text" path="mobileNumber" id="mobileNumber"
									data-inputmask="'mask': '9999999999'" required="required"
									cssClass="form-control" maxlength="10"
									placeholder="Mobile Number" />
							</div>
							<form:errors path="mobileNumber" cssClass="add-margin error-msg" />
						</div>

						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.email" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<form:input type="text" cssClass="form-control" id="emial"
								path="emailId" placeholder="abc@xyz.com" />
							<form:errors path="emailId" cssClass="error-msg" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.altcontact" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<div class="input-group">
								<form:input type="text" path="altContactNumber" id="altcontact"
									cssClass="form-control" data-inputmask="'mask': '9999999999'"
									maxlength="10" placeholder="Mobile Number" />
							</div>
						</div>


						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.DOB" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<fmt:formatDate value="${employee.dob}" var="DOB"
								pattern="dd/MM/yyyy" />
							<c:out value="${DOB}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.DOA" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<joda:format value="${employee.dateOfAppointment}" var="DOA"
								pattern="dd-MM-yyyy" />
							<c:out value="${DOA}" />
						</div>

						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.status" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<c:out value="${employee.employeeStatus}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.emptype" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<c:out value="${employee.employeeType.name}" />
						</div>

						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.code" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<c:out value="${employee.code}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.pan" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<c:out value="${employee.pan}" />
						</div>

						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.aadhar" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<c:out value="${employee.aadhaarNumber}" />
						</div>
					</div>

					<div class="row">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.useractive.view" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<c:if test="${employee.active==true}">
												Yes
											</c:if>
							<c:if test="${employee.active==false}">
												No
											</c:if>
						</div>

						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.username" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<c:out value="${employee.username}" />
						</div>
					</div>
				</div>
			</div>
	</div>



	<div class="row">
		<div class="text-center">
			<button type="submit" id="submit" class="btn btn-primary">
				<spring:message code="lbl.submit" />
			</button>
			<a href="javascript:void(0);" id="com_cancel" class="btn btn-default"
				onclick="self.close()"><spring:message code="lbl.close" /></a>
		</div>
	</div>



	</form:form>
</div>
</div>