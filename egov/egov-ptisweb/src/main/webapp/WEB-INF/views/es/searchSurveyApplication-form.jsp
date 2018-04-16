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

<div class="row" id="page-content">
	<div class="col-md-12">
		<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert">
				<spring:message code="${message}" />
			</div>
		</c:if>
		<form:form class="form-horizontal form-groups-bordered"
			id="searchSurveyApplication-form" modelAttribute="surveyApplication"
			method="get">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<input type="hidden"
							value="${sessionScope.citymunicipalityname}, ${sessionScope.districtName} "
							id="pdfTitle" />
						<spring:message code="lbl.search.survey.application" />
					</div>
				</div>
				<div class="panel-body custom-form">
					<div class="form-group">
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.applicationType" /></label>
						<div class="col-sm-3 add-margin">
							<form:select id="applicationType" 
								path="applicationType" cssClass="form-control"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.option.select" />
								</form:option>
								<form:options items="${applicationTypes}" />
							</form:select>
						</div>
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.status" /></label>
						<div class="col-sm-3 add-margin">
							<form:select id="applicationStatus"
								path="applicationStatus" cssClass="form-control"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.option.select" />
								</form:option>
								<form:options items="${applicationStatus}" />
							</form:select>
						</div>
					</div>
				</div>

				<div class="panel-body custom-form">
					<div class="form-group">
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.applicationNo" /></label>
						<div class="col-sm-3 add-margin">
							<input type="text" name="applicationNo" class="form-control low-width" id="applicationNo"/>
						</div>
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.assessmentNo" /></label>
						<div class="col-sm-3 add-margin">
							<input type="text" name="assessmentNo" class="form-control low-width" id="assessmentNo" />
						</div>
					</div>
				</div>

				<div class="panel-body custom-form">
					<div class="form-group">
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.fromDate" /></label>
						<div class="col-sm-3 add-margin">
							<fmt:formatDate value="${fromDate}" var="fromDate"
								pattern="yyyy-MM-dd" />
							<form:input path="" name="fromDate" id="fromDate"
								cssClass="form-control datepicker" value="${fromDate}"
								cssErrorClass="form-control error" />
							<c:set value="${fromDate}" var="fromhidden" />
						</div>
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.toDate" /></label>
						<div class="col-sm-3 add-margin">
							<fmt:formatDate value="${toDate}" var="toDate"
								pattern="yyyy-MM-dd" />
							<form:input path="" name="toDate" id="toDate"
								cssClass="form-control datepicker" value="${toDate}"
								cssErrorClass="form-control error" />
						</div>
					</div>
				</div>

				<div class="row">
					<div class="text-center">
						<button type="button" class="btn btn-primary"
							id="searchSurveyApplication">
							<spring:message code="lbl.search" />
						</button>
						<a href="javascript:void(0)" class="btn btn-default"
							data-dismiss="modal" onclick="self.close()"><spring:message
								code="lbl.close" /></a>
					</div>
				</div>
				<br>
		</form:form>
	</div>
	<div class="col-md-15" id="searchResultDiv">
		<table class="table table-bordered datatable dt-responsive "
			id="search-table" width="220%">
			<thead>
				<tr>
					<th>Application Number</th>
					<th>Application Type</th>
					<th>Assessment No</th>
					<th>Application Date</th>
					<th>Address</th>
					<th>Status</th>
					<th>Functionary Name</th>
				</tr>
			</thead>

		</table>
	</div>
</div>

<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/searchSurveyApplication.js?rnd=${app_release_no}'/>"></script>
