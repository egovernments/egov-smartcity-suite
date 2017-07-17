<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<form:form role="form" method="post"
	cssClass="form-horizontal form-groups-bordered" id="usageform"
	modelAttribute="certificate">
	<div>


		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="title.registration.search" />
				</div>
			</div>

			<div class="panel-body custom-form">
				<div class="form-group">
					<label for="registrationNo" class="col-sm-2 control-label"><spring:message
							code="lbl.registration.no" /></label>

					<div class="col-sm-3 add-margin">
						<form:input id="registrationNo" path="registration.registrationNo"
							type="text"
							cssClass="form-control low-width is_valid_alphnumeric" />
						<form:errors path="registration.registrationNo"
							cssClass="error-msg" />
					</div>


					<label class="col-sm-2 control-label"> <spring:message
							code="lbl.Boundary" /></span>
					</label>
					<div class="col-sm-3 add-margin">
						<form:select path="registration.zone" id="select-registrationzone"
							cssClass="form-control" cssErrorClass="form-control error">
							<form:option value="">
								<spring:message code="lbl.default.option" />
							</form:option>
							<form:options items="${zones}" itemValue="id" itemLabel="name" />
						</form:select>
						<form:errors path="registration.zone"
							cssClass="add-margin error-msg" />
					</div>
				</div>
				<div class="form-group">
					<label for="certificateNo" class="col-sm-2 control-label"><spring:message
							code="lbl.certificate.type" /></label>

					<div class="col-sm-3 add-margin">
						<form:select path="certificateType" id="certificateType"
							cssClass="form-control" cssErrorClass="form-control error">
							<form:option value="">
								<spring:message code="lbl.select" />
							</form:option>

							<form:options items="${certificateType}" />
						</form:select>
						<form:errors path="certificateType" cssClass="error-msg" />
					</div>
					<label for="field-1" class="col-sm-2 control-label"><spring:message
							code="lbl.registrationunit" /> </label>
					<div class="col-sm-3 add-margin">
						<form:select path="registration.marriageRegistrationUnit"
							id="select-registrationunit" cssClass="form-control"
							cssErrorClass="form-control error">
							<form:option value="">
								<spring:message code="lbl.default.option" />
							</form:option>
							<form:options items="${marriageRegistrationUnit}" itemValue="id"
								itemLabel="name" />
						</form:select>
						<form:errors path="registration.marriageRegistrationUnit"
							cssClass="add-margin error-msg" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"> <spring:message
							code="lbl.fromDate" />
					</label>
					<div class="col-sm-3">
						<form:input path="fromDate" id="txt-fromdate" type="text"
							class="form-control low-width datepicker" data-date-end-date="0d"
							data-date-today-highlight="true" placeholder=""
							autocomplete="off" />
						<form:errors path="fromDate" cssClass="add-margin error-msg" />
					</div>
					<label class="col-sm-2 control-label"> <spring:message
							code="lbl.toDate" />
					</label>
					<div class="col-sm-3">
						<form:input path="toDate" id="txt-todate" type="text"
							class="form-control low-width datepicker" data-date-end-date="0d"
							data-date-today-highlight="true" placeholder=""
							autocomplete="off" />
						<form:errors path="toDate" cssClass="add-margin error-msg" />
					</div>
				</div>

			</div>

			<div class="row">
				<div class="text-center">
					<button type="button" id="btnregcertificatesearch"
						class="btn btn-primary add-margin">
						<spring:message code="lbl.button.search" />
					</button>
					<button type="reset" class="btn btn-danger">
						<spring:message code="lbl.reset" />
					</button>
					<a href="javascript:void(0)" class="btn btn-default"
						onclick="self.close()"><spring:message code="lbl.close" /></a>
				</div>
			</div>

		</div>
</form:form>
<br />
<br />
<div class="row display-hide report-section" id="table_container">
	<div class="col-md-12 table-header text-left">The Certificate
		Search result is</div>
	<br />
	<div class="panel-heading">
		<div class="displayCount panel-title"></div>
	</div>
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id="marriagecertificate_table">
			<thead>
				<th><spring:message code="lbl.registration.no"/></th>
				<th><spring:message code="lbl.certificate.no"/></th>
				<th><spring:message code="lbl.Boundary" /></th>
				<th>certificateDate</th>
				<th><spring:message code="lbl.certificate.type" /><</th>
				<th>Registration Date</th>
				<th>Marriage Date</th>
				<th><spring:message code="lbl.husband.name"/></th>
				<th><spring:message code="lbl.wife.name"/></th>
				<th>Remarks</th>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
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
	src="<cdn:url value='/resources/js/app/registration-certificate-report.js?rnd=${app_release_no}'/> "></script>