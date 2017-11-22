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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
				</div>
			</div>

			<div class="panel-body custom-form">
				<div class="form-group">
					<label for="registrationNo" class="col-sm-2 control-label"><spring:message
							code="lbl.registration.number" /></label>

					<div class="col-sm-3 add-margin">
						<form:input id="registrationNo" path="registrationNo" type="text"
							cssClass="form-control low-width is_valid_alphnumeric"
							autocomplete="off" required="required" />
						<form:errors path="registrationNo" cssClass="error-msg" />
					</div>
					<label for="registrationNo" class="col-sm-2 control-label"><spring:message
							code="lbl.application.no" /></label>

					<div class="col-sm-3 add-margin">
						<form:input id="applicationNo" path="applicationNo" type="text"
							cssClass="form-control low-width is_valid_alphnumeric"
							autocomplete="off" required="required" />
						<form:errors path="applicationNo" cssClass="error-msg" />
					</div>
				</div>

				<div class="form-group">
					<label for="field1" class="col-sm-2 control-label"><spring:message
							code="lbl.husband.name" /></label>

					<div class="col-sm-3 add-margin">
						<form:input id="husbandName" path="husband.name.firstName"
							type="text" cssClass="form-control is_valid_alphabet" />
						<form:errors path="husband.name.firstName" cssClass="error-msg" />
					</div>
					<label for="field1" class="col-sm-2 control-label"><spring:message
							code="lbl.wife.name" /></label>

					<div class="col-sm-3 add-margin">
						<form:input id="wifeName" path="wife.name.firstName" type="text"
							cssClass="form-control is_valid_alphabet" />
						<form:errors path="wife.name.firstName" cssClass="error-msg" />
					</div>
				</div>

				<div class="form-group">
					<label for="dateOfMarriage" class="col-sm-2 control-label"><spring:message
							code="lbl.from.date" /></label>

					<div class="col-sm-3 add-margin">
						<form:input id="fromDate" path="fromDate" type="text"
							cssClass="form-control datepicker"
							data-date-today-highlight="true" data-date-end-date="0d" />
						<form:errors path="fromDate" cssClass="error-msg" />
					</div>
					<label for="dateOfMarriage" class="col-sm-2 control-label"><spring:message
							code="lbl.to.date" /></label>

					<div class="col-sm-3 add-margin">
						<form:input id="toDate" path="toDate" type="text"
							cssClass="form-control datepicker"
							data-date-today-highlight="true" data-date-end-date="0d" />
						<form:errors path="toDate" cssClass="error-msg" />
					</div>

				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"> <spring:message
							code="lbl.registrationunit" />
					</label>
					<div class="col-sm-3 add-margin">
						<form:select path="marriageRegistrationUnit"
							id="select-registrationunit" cssClass="form-control"
							cssErrorClass="form-control error">
							<form:option value="">
								<spring:message code="lbl.default.option" />
							</form:option>
							<form:options items="${marriageRegistrationUnit}" itemValue="id"
								itemLabel="name" />
						</form:select>
						<form:errors path="marriageRegistrationUnit.id"
							cssClass="add-margin error-msg" />
					</div>
					<label for="dateOfMarriage" class="col-sm-2 control-label"><spring:message
							code="lbl.date.of.marriage" /></label>
					<div class="col-sm-3 add-margin">
						<form:input id="dateOfMarriage" path="dateOfMarriage" type="text"
							cssClass="form-control datepicker"
							data-date-today-highlight="true" data-date-end-date="0d" />
						<form:errors path="dateOfMarriage" cssClass="error-msg" />
					</div>
				</div>

				<c:if test="${mode=='collectmrfee'}">
					<div class="form-group">
						<label for="mrType" class="col-sm-2 control-label"><spring:message
								code="lbl.marriageRegistration.type" /></label>
						<div class="col-sm-3 add-margin">
							<select name="marriageRegistrationType"
								id="marriageRegistrationType" class="form-control"
								data-first-option="false">
								<c:forEach items="${marriageRegistrationTypes}" var="mrt">
									<option value="${mrt}">${mrt}</option>
								</c:forEach>
							</select>
							<%-- <form:errors path="marriageRegistrationType" cssClass="error-msg"/> --%>
						</div>

					</div>
				</c:if>

			</div>
		</div>
		<div class="form-group">
			<div class="text-center">
				<button type="button" id="btnregistrationsearch"
					class="btn btn-primary add-margin">
					<spring:message code="lbl.search" />
				</button>
				<button type="reset" class="btn btn-danger">
					<spring:message code="lbl.reset" />
				</button>
				<a href="javascript:void(0)" class="btn btn-default"
					onclick="self.close()"><spring:message code="lbl.close" /></a>
			</div>
		</div>
	</div>
</div>