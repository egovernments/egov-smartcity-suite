<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<div class="row">
	<div class="col-md-12">
		<form:form method="get" modelAttribute="dailyBoardReportResult"
			id="dailyboardreportForm"
			class="form-horizontal form-groups-bordered"
			enctype="multipart/form-data">
			<div class="row">
				<div class="panel panel-primary" data-collapsed="0">

					<div class="panel-heading">
						<div class="panel-title">Daily Board Report</div>

					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label"><spring:message
								code="lbl.casecategory" />:</label>
						<div class="col-sm-3 add-margin">
							<form:select name="caseCatogory" path=""
								data-first-option="false" id="caseCatogory"
								cssClass="form-control">
								<form:option value="">
									<spring:message code="lbls.select" />
								</form:option>
								<form:options items="${caseTypeList}" itemValue="id"
									itemLabel="caseType" />
							</form:select>
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.officerincharge" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input class="form-control" maxlength="50"
								id="officerIncharge" path="officerIncharge" />
							<form:errors path="officerIncharge"
								cssClass="add-margin error-msg" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label text-right"> <spring:message
								code="lbl.fromDate" />:</label>
						<div class="col-sm-3 add-margin">
							<input type="text" name="fromDate"
								class="form-control datepicker" data-date-end-date="0d"
								id="fromDate" data-inputmask="'mask': 'd/m/y' onblur="onchnageofDate()"/>
						</div>
						<label class="col-sm-2 control-label text-right"> <spring:message
								code="lbl.toDate" />:</label>
						<div class="col-sm-3 add-margin">
							<input type="text" name="toDate"
								class="form-control datepicker today" data-date-end-date="0d"
								id="toDate" data-inputmask="'mask': 'd/m/y'" />
						</div>

					</div>


					<div class="row">

						<div class="text-center">
							<button type="button" id="dailyBoardReportSearch"
								class="btn btn-primary">Search</button>
							<a href="javascript:void(0)" class="btn btn-default"
								onclick="self.close()"> Close</a>
						</div>
					</div>
				</div>
		</form:form>

		<div class="row">
			<div class="col-md-6 col-xs-6 table-header">The Search result
				is</div>
			<div class="col-md-12" id="searchResultDiv">
				<table class="table table-bordered datatable dt-responsive"
					id="dailyBoardReportsResults">

				</table>
			</div>
		</div>
	</div>
</div>


<link rel="stylesheet"
	href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>">

<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.columnFilter.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/egov/custom.js?rnd=${app_release_no}' context='/egi'/>"></script>

<script
	src="<cdn:url value='/resources/js/app/dailyBoardReport.js?rnd=${app_release_no}'/>"
	type="text/javascript"></script>

