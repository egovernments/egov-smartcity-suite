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
		<form:form modelAttribute="legalCaseSearchResult"
			name="searchlegalcaseForm" id="searchlegalcaseForm"
			class="form-horizontal form-groups-bordered">
			<div class="panel panel-primary" data-collapsed="0">

				<div class="panel-heading">
					<div class="panel-title">Search Legal Case</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message
							code="lbl.casenumber" />:</label>
					<div class="col-sm-3 add-margin">
						<input type="text" name="caseNumber"
							class="form-control patternvalidation"
							data-pattern="alphanumerichyphenbackslash" maxlength="50"
							id="caseNumber" />
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.lcnumber" />:</label>
					<div class="col-sm-3 add-margin">
						<input type="text" name="lcNumber"
							class="form-control patternvalidation"
							data-pattern="alphanumerichyphenbackslash" maxlength="50"
							id="lcNumber" />
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message
							code="lbl.casecategory" />:</label>
					<div class="col-sm-3 add-margin">
						<form:select name="casecategory" path="" data-first-option="false"
							id="casecategory" cssClass="form-control">
							<form:option value="">
								<spring:message code="lbls.select" />
							</form:option>
							<form:options items="${caseTypeList}" itemValue="id"
								itemLabel="caseType" />
						</form:select>
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.standingcons" />:</label>
					<div class="col-sm-3 add-margin">
						<input type="text" name="standingCouncil"
							class="form-control patternvalidation" data-pattern="string"
							id="standingCouncil" />
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message
							code="lbl.courttype" />:</label>
					<div class="col-sm-3 add-margin">
						<form:select name="courtType" path="" data-first-option="false"
							id="courtType" cssClass="form-control">
							<form:option value="">
								<spring:message code="lbls.select" />
							</form:option>
							<form:options items="${courtTypeList}" itemValue="id"
								itemLabel="courtType" />
						</form:select>
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.court" />:</label>
					<div class="col-sm-3 add-margin">
						<form:select name="courtName" path="" data-first-option="false"
							id="courtName" cssClass="form-control">
							<form:option value="">
								<spring:message code="lbls.select" />
							</form:option>
							<form:options items="${courtsList}" itemValue="id"
								itemLabel="name" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message
							code="lbl.casestartdate" />:</label>
					<div class="col-sm-3 add-margin">
						<input type="text" name="caseFromDate"
							class="form-control datepicker" data-date-end-date="0d"
							id="caseFromDate" data-inputmask="'mask': 'd/m/y'"
							onblur="onchnageofDate()" />
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.caseenddate" />:</label>
					<div class="col-sm-3 add-margin">
						<input type="text" name="caseToDate"
							class="form-control datepicker today" data-date-end-date="0d"
							id="caseToDate" />
					</div>

				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message
							code="lbl.casestatus" />:</label>
					<div class="col-sm-3 add-margin">
						<form:select name="statusId" path="" data-first-option="false"
							id="statusId" cssClass="form-control">
							<form:option value="">
								<spring:message code="lbls.select" />
							</form:option>
							<form:options items="${statusList}" itemValue="id"
								itemLabel="description" />
						</form:select>
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.petitiontype" />:</label>
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
				<div class="form-group" id="reportstatus">
					<label class="col-sm-3 control-label text-right"><spring:message
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
				<div class="form-group" >
				<label class="col-sm-3 control-label text-right"></label>
				<div class="col-sm-3 add-margin"></div>
					<label class="col-sm-5 control-label text-right"><spring:message
							code="lbl.exclude.closed.case" />:</label>
					 <div class="col-sm-1 add-margin">
						<input type="checkbox" name="isStatusExcluded"
							value="isStatusExcluded"  checked="checked" />
					</div> 
				</div>
				<div class="row">
					<div class="text-center">
						<a href="javascript:void(0);" id="legalcaseReportSearch"
							class="btn btn-primary"><spring:message code='lbl.search' /></a>

						<a href="javascript:void(0)" class="btn btn-default"
							onclick="self.close()"><spring:message code="lbl.close" /></a>
					</div>
				</div>
			</div>
		</form:form>

		<div class="row">
			<div class="col-md-6 col-xs-6 table-header">The Search result
				is</div>
			<div class="col-md-6 col-xs-6 add-margin text-right">
				<span class="inline-elem"><spring:message code='lbl.search' /></span>
				<span class="inline-elem"><input type="text" id="searchapp"
					class="form-control input-sm"></span>
			</div>
			<div class="col-md-12" id="searchResultDiv">
				<table class="table table-bordered datatable dt-responsive"
					id="legalCaseResults">

				</table>
			</div>
		</div>
	</div>
</div>


<link rel="stylesheet"
	href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/js/app/legalcaseSearch.js?rnd=${app_release_no}'/>"
	type="text/javascript"></script>