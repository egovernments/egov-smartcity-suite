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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<div class="row" id="page-content">
	<div class="col-md-12">
		<div class="panel" data-collapsed="0">
			<div class="panel-body">
				<form:form method="post" action=""
					class="form-horizontal form-groups-bordered"
					id="form-registrationstatus" modelAttribute="certificate">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title">
								<strong><spring:message code="title.search.certificate" /></strong>
							</div>
						</div>

						<div class="panel-body custom-form">
							<div class="form-group">
								<label for="certificateNo" class="col-sm-2 control-label"><spring:message
										code="lbl.certificate.no" /></label>

								<div class="col-sm-3 add-margin">
									<form:input id="certificateNo" path="certificateNo" type="text"
										cssClass="form-control low-width is_valid_alphnumeric" />
									<form:errors path="certificateNo" cssClass="error-msg" />
								</div>
								<label for="certificateNo" class="col-sm-2 control-label"><spring:message
										code="lbl.certificate.type" /></label>

								<div class="col-sm-3 add-margin">
									<form:select path="certificateType" id="certificateType"
										cssClass="form-control" cssErrorClass="form-control error">
										<form:option value="">
											<spring:message code="lbl.select" />
										</form:option>

										<%--<form:option value="REJECTED">Rejected</form:option>
									<form:option value="REISSUE">Reissue</form:option>--%>
										<form:options items="${certificateType}" />

									</form:select>
									<form:errors path="certificateType" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label for="registrationNo" class="col-sm-2 control-label"><spring:message
										code="lbl.registration.no" /></label>

								<div class="col-sm-3 add-margin">
									<form:input id="registrationNo"
										path="registration.registrationNo" type="text"
										cssClass="form-control low-width is_valid_alphnumeric" />
									<form:errors path="registration.registrationNo"
										cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label"> Frequency </label>
								<div class="col-sm-3 add-margin">
									<form:select path="frequency" id="frequency"
										cssClass="form-control" cssErrorClass="form-control error">
										<form:option value="">
											<spring:message code="lbl.select" />
										</form:option>
										<form:option value="ALL">All</form:option>
										<form:option value="LATEST">Latest</form:option>
									</form:select>
									<form:errors path="frequency" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"> <spring:message
										code="lbl.fromDate" />
								</label>
								<div class="col-sm-3">
									<form:input path="fromDate" id="txt-fromdate" type="text"
										class="form-control low-width datepicker"
										data-date-end-date="0d" data-date-today-highlight="true"
										placeholder="" autocomplete="off" />
									<form:errors path="fromDate" cssClass="add-margin error-msg" />
								</div>
								<label class="col-sm-2 control-label"> <spring:message
										code="lbl.toDate" />
								</label>
								<div class="col-sm-3">
									<form:input path="toDate" id="txt-todate" type="text"
										class="form-control low-width datepicker"
										data-date-end-date="0d" data-date-today-highlight="true"
										placeholder="" autocomplete="off" />
									<form:errors path="toDate" cssClass="add-margin error-msg" />
								</div>
							</div>
						</div>
					</div>
			</div>
			<div>
				<div class="text-center">
					<button type="button" class="btn btn-primary"
						id="btn_marriagecertificate_search">
						<spring:message code="lbl.search" />
					</button>
					<button type="reset" class="btn btn-danger">
						<spring:message code="lbl.reset" />
					</button>
					<a href="javascript:void(0)" class="btn btn-default"
						onclick="self.close()"><spring:message code="lbl.close" /></a>
				</div>
			</div>
			</form:form>
		</div>
	</div>
</div>

<br />
<br />
<div class="row display-hide report-section" id="table_container">
	<div class="col-md-12 table-header text-left">The Certificate
		Search result is</div>
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id="marriagecertificate_table">
			<thead>
				<tr>
					<th>Registration No</th>
					<th>Certificate No</th>
					<th>certificateDate</th>
					<th>certificateType</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
</div>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<%-- <link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>"> --%>

<script
	src="<cdn:url value='/resources/js/app/registration-certificate.js?rnd=${app_release_no}'/> "></script>