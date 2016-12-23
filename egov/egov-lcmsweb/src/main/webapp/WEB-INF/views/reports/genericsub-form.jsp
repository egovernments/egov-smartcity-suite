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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<div class="row" id="page-content">
	<div class="col-md-12">
		<form:form class="form-horizontal form-groups-bordered"
			id="genericSubregisterform" name="genericSubregisterform"
			modelAttribute="genericSubReportResult" method="get">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Generic Sub Report</div>
				</div>
				<div class="panel-body custom-form">
				<div class="form-group" id="aggregated">
					<div class="form-group">
						<label for="field-1" class="col-sm-4 control-label"><spring:message
								code="lbl.aggregatedby" /> :</span></label>
						<div class="col-sm-3 add-margin">
							<div class="col-sm-12 add-margin">
								<form:select id="aggregatedBy" name="aggregatedBy"
									path="aggregatedBy" cssClass="form-control">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<c:forEach items="${aggregatedByList}" var="aggregatedByvalue">
										<form:option value="${aggregatedByvalue}"> ${aggregatedByvalue} </form:option>
									</c:forEach>
								</form:select>
							</div>
						</div>
					</div>
					</div>
				</div>
				<div class="panel-heading">
					<div class="panel-title">Reports Criteria</div>
				</div>
				<div class="form-group" id="reportscriteria">
				<div class="form-group">
					<label class="col-sm-2 control-label"><spring:message
							code="lbl.casecatagory" /> :</label>
					<div class="col-sm-3 add-margin">
						<form:select name="casecategoryId" path=""
							data-first-option="false" id="casecategoryId"
							cssClass="form-control">
							<form:option value="">
								<spring:message code="lbls.select" />
							</form:option>
							<form:options items="${caseTypeList}" itemValue="id"
								itemLabel="caseType" />
						</form:select>
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.standingcons" /> :</label>
					<div class="col-sm-3 add-margin">
						<form:input class="form-control" maxlength="50" name="standingCounsel"
							id="standingCounsel" path="" />
						<form:errors path="standingCounsel"
							cssClass="add-margin error-msg" />
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><spring:message
							code="lbl.courttype" /> :</label>
					<div class="col-sm-3 add-margin">
						<form:select name="courtType" path=""
							data-first-option="false" id="courtType" cssClass="form-control">
							<form:option value="">
								<spring:message code="lbls.select" />
							</form:option>
							<form:options items="${courtTypeList}" itemValue="id"
								itemLabel="courtType" />
						</form:select>
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.court" /> :</label>
					<div class="col-sm-3 add-margin">
						<form:select name="courtId" path=""
							data-first-option="false" id="courtId" cssClass="form-control">
							<form:option value="">
								<spring:message code="lbls.select" />
							</form:option>
							<form:options items="${courtsList}" itemValue="id"
								itemLabel="name" />
						</form:select>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><spring:message
							code="lbl.judgmentype" /> :</label>
					<div class="col-sm-3 add-margin">
						<form:select name="judgmentTypeId" path=""
							data-first-option="false" id="judgmentTypeId"
							cssClass="form-control">
							<form:option value="">
								<spring:message code="lbls.select" />
							</form:option>
							<form:options items="${judgmentTypeList}" itemValue="id"
								itemLabel="name" />
						</form:select>
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.petitiontype" /> :</label>
					<div class="col-sm-3 add-margin">
						<form:select name="petitionTypeId" path=""
							data-first-option="false" id="petitionTypeId"
							cssClass="form-control">
							<form:option value="">
								<spring:message code="lbls.select" />
							</form:option>
							<form:options items="${petitiontypeList}" itemValue="id"
								itemLabel="petitionType" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.casestatus" /> :</label>
					<div class="col-sm-3 add-margin">
						<form:select name="caseStatus" path=""
							data-first-option="false" id="caseStatus" cssClass="form-control">
							<form:option value="">
								<spring:message code="lbls.select" />
							</form:option>
							<form:options items="${statusList}" itemValue="code"
								itemLabel="description" />
						</form:select>
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.officerincharge" />:</label>
					<div class="col-sm-3 add-margin">
						<form:input class="form-control" maxlength="50"
							id="officerIncharge" path=""  name="officerIncharge"/>
					</div>
				</div>
				<div class="form-group" id="reportstatus">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.reportstatus" />:</label>
					<div class="col-sm-3 add-margin">
						<form:select name="reportStatusId" path=""
							data-first-option="false" id="reportStatusId"
							cssClass="form-control">
							<form:option value="">
								<spring:message code="lbls.select" />
							</form:option>
							<form:options items="${reportStatusList}" itemValue="id"
								itemLabel="name" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label text-right"> <spring:message
							code="lbl.fromDate" /> :
					</label>
					<div class="col-sm-3 add-margin">
						<input type="text" name="fromDate" path=""
							class="form-control datepicker" data-date-end-date="0d"
							id="fromDate" data-inputmask="'mask': 'd/m/y' onblur=" onchnageofDate()"/>
					</div>
					<label class="col-sm-2 control-label text-right"> <spring:message
							code="lbl.toDate" /> :
					</label>
					<div class="col-sm-3 add-margin">
						<input type="text" name="toDate" path=""
							class="form-control datepicker " data-date-end-date="0d"
							id="toDate" data-inputmask="'mask': 'd/m/y'" />
					</div>
				</div>
				</div>


				<div class="row">
					<div class="text-center">
						<button type="button" class="btn btn-primary"
							id="searchid">
							<spring:message code="lbl.search" />
						</button>
						<a href="javascript:void(0)" class="btn btn-default"
							data-dismiss="modal" onclick="self.close()"><spring:message
								code="lbl.close" /></a>
					</div>
				</div>
			</div>

		</form:form>

		<div id="reportgeneration-header"
			class="col-md-12 table-header text-left">
			<fmt:formatDate value="${currDate}" var="currDate"
				pattern="dd-MM-yyyy" />
			<spring:message code="lbl.reportgeneration" />
			:
			<c:out value="${currDate}"></c:out>
		</div>
		<table class="table table-bordered table-hover multiheadertbl"
			id="genericSubReport-table" width="200%">
		</table>
	</div>
</div>

<link rel="stylesheet"
	href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css?rnd=${app_release_no}' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css?rnd=${app_release_no}' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css?rnd=${app_release_no}' context='/egi'/>">
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js?rnd=${app_release_no}' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js?rnd=${app_release_no}' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js?rnd=${app_release_no}' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js?rnd=${app_release_no}' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js?rnd=${app_release_no}' context='/egi'/>"
	type="text/javascript"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/moment.min.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/datetime-moment.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js?rnd=${app_release_no}' context='/egi'/>"></script>
	<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css?rnd=${app_release_no}' context='/egi'/>" />
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/genericSubReport.js?rnd=${app_release_no}'/>"></script>