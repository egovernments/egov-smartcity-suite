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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<div class="row">
	<div class="col-md-12">
		<form:form class="form-horizontal form-groups-bordered" action=""
			id="natureOfUSageSearchForm" modelAttribute="natureOfUsageResult"
			method="get">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message
								code="lbl.natureOfUsage.report.title" /></strong>
					</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"> <spring:message
								code="lbl.usage.nature" />
						</label>
						<div class="col-sm-3 add-margin">
							<form:select path="natureOfUsage" id="natureOfUsage"
								data-first-option="false" cssClass="form-control">
								<form:option value="-1">
									<spring:message code="lbl.default.all" />
								</form:option>
								<form:options items="${natureOfUsages}" itemValue="id"
									itemLabel="usageName" />
							</form:select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"> <spring:message
								code="lbl.ward" />
						</label>
						<div class="col-sm-3 add-margin">
							<form:select path="ward" id="ward" data-first-option="false"
								cssClass="form-control">
								<form:option value="-1">
									<spring:message code="lbl.default.all" />
								</form:option>
								<form:options items="${wards}" itemValue="id" itemLabel="name" />
							</form:select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"> <spring:message
								code="lbl.block" />
						</label>
						<div class="col-sm-3 add-margin">
							<form:select path="block" id="block" data-first-option="false"
								cssClass="form-control">
								<form:option value="-1">
									<spring:message code="lbl.default.all" />
								</form:option>
								<form:options items="${blocks}" itemValue="id" itemLabel="name" />
							</form:select>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<button type="button" id="btnsearch" class="btn btn-primary">
						<spring:message code="lbl.search" />
					</button>
					<button type="button" class="btn btn-default" data-dismiss="modal"
						onclick="window.close();">
						<spring:message code="lbl.close" />
					</button>
				</div>
			</div>
		</form:form>
		<div >
			<br />
			<spring:message code="reports.note.text" />
			<div id="natureOfUsage-header" class="col-md-12 table-header text-left">
				<input type="hidden" value="${sessionScope.citymunicipalityname}, ${sessionScope.districtName} District" id="pdfTitle"/>
				<div class="col-md-12 table-header text-left" id="reportTitle">Nature of usage report details</div>
			</div>
			<div class="col-md-12 form-group report-table-container">
				<table class="table table-bordered datatable" id="tblnatureofusage">
				</table>
			</div>
		</div>
	</div>
</div>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/natureOfUsageReport.js'/>"></script>