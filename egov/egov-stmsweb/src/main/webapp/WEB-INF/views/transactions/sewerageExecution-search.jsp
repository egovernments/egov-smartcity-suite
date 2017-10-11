<%--
  ~ eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) <2017>  eGovernments Foundation
  ~
  ~  The updated version of eGov suite of products as by eGovernments Foundation
  ~  is available at http://www.egovernments.org
  ~
  ~  This program is free software: you can redistribute it and/or modify
  ~  it under the terms of the GNU General Public License as published by
  ~  the Free Software Foundation, either version 3 of the License, or
  ~  any later version.
  ~
  ~  This program is distributed in the hope that it will be useful,
  ~  but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~  GNU General Public License for more details.
  ~
  ~  You should have received a copy of the GNU General Public License
  ~  along with this program. If not, see http://www.gnu.org/licenses/ or
  ~  http://www.gnu.org/licenses/gpl.html .
  ~
  ~  In addition to the terms of the GPL license to be adhered to in using this
  ~  program, the following additional terms are to be complied with:
  ~
  ~      1) All versions of this program, verbatim or modified must carry this
  ~         Legal Notice.
  ~ 	Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~         derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~ 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~ 	For any further queries on attribution, including queries on brand guidelines,
  ~         please contact contact@egovernments.org
  ~
  ~      2) Any misrepresentation of the origin of the material is prohibited. It
  ~         is required that all modified versions of this material be marked in
  ~         reasonable ways as different from the original version.
  ~
  ~      3) This license does not grant any rights to any user of the program
  ~         with regards to rights under trademark law for use of the trade names
  ~         or trademarks of eGovernments Foundation.
  ~
  ~  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="row">
	<div class="col-md-12">
		<form:form id="sewerageSearchRequestForm" role="form"
			class="form-horizontal form-groups-bordered"
			modelAttribute="sewerageExecutionResult">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message code='title.sewerageTaxSearch' /></strong>
					</div>
				</div>
				<div class="form-group">
					<div class="row">
						<label class="col-sm-2 control-label"> <spring:message
								code="lbl.application.number" /></label>
						<div class="col-sm-3 add-margin">
							<input type="text" name="applicationNumber"
								id="applicationNumber" class="form-control"
								data-pattern="alphanumericwithspace" min="10" />
						</div>
						<label class="col-sm-2 control-label"> <spring:message
								code="lbl.shsc.number" /></label>
						<div class="col-sm-3 add-margin">
							<input type="text" name="shscNumber" id="shscNumber"
								class="form-control is_valid_number" maxlength="11"
								id="app-appcodo" min="11" />
						</div>
					</div>
					<div class="form-group">
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.fromDate" /></label>
						<div class="col-sm-3 add-margin">
							<input type="text" name="fromDate"
								class="form-control datepicker" id="fromDate" maxlength="10" />
						</div>
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.toDate" /></label>
						<div class="col-sm-3 add-margin">
							<input type="text" name="toDate" class="form-control datepicker"
								id="toDate" maxlength="10" />
						</div>
					</div>

					<div class="form-group">
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.revenue.ward" /></label>
						<div class="col-sm-3 add-margin">
							<select name="revenueWard" id="app-mobno" class="form-control"
								data-first-option="false">
								<option value="${ward.name}">Select</option>
								<c:forEach items="${revenueWards}" var="ward">
									<option value="${ward.name}">${ward.name}</option>
								</c:forEach>
							</select>
						</div>
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.application.type" /></label>
						<div class="col-sm-3 add-margin">
							<form:select path="applicationType" id="appType"
								class="form-control" data-first-option="false">
								<form:option value="">Select</form:option>
								<form:options items="${applicationtype}" itemValue="name"
									itemLabel="name" />
							</form:select>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<div class="text-center">
						<div class="add-margin">
							<button type="button" id="searchSewerageapplication"
								class="btn btn-primary search">
								<spring:message code='lbl.search' />
							</button>
							<button class="btn btn-danger" type="reset">
								<spring:message code='lbl.reset' />
							</button>
							<a href="javascript:void(0)" class="btn btn-default"
								id="searchSewerageapplication" onclick="self.close()">Close</a>
						</div>
					</div>
				</div>
			</div>
		</form:form>
	</div>

	<br /> <br />
	<div class="row">
		<div class=" result panel-body">
			<div class="col-md-12 form-group" id="searchResultDiv">
				<table
					class="table table-bordered datatable dt-responsive table-hover"
					id="aplicationSearchResults">
					<thead>
						<tr>
							<th text-align="centre">Select<input type="checkbox"
								name="select_all" class="allCheckBoxClass" /></th>
							<th><spring:message code="lbl.application.number" /></th>
							<th><spring:message code="lbl.shsc.number" /></th>
							<th><spring:message code="lbl.owner.name" /></th>
							<th><spring:message code="lbl.application.type" /></th>
							<th><spring:message code="lbl.status" /></th>
							<th><spring:message code="lbl.revenue.ward" /></th>
							<th><spring:message code="lbl.application.date" /></th>
							<th><spring:message code="lbl.executiondate" /></th>
						</tr>
					</thead>
				</table>
				<div class="text-center">
					<button type="button" class="btn btn-primary add-margin updateBtn"
						id="updateBtn">
						<spring:message code="lbl.update" />
					</button>
				</div>
			</div>
		</div>
	</div>
</div>

<link rel="stylesheet"
	href="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/moment.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/datetime-moment.js' context='/egi'/>"></script>
<script
	src="<cdn:url  value='/resources/javascript/helper.js?rnd=${app_release_no}'/>"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<cdn:url  value='/resources/js/transactions/seweragexecution.js?rnd=${app_release_no}'/>"></script>