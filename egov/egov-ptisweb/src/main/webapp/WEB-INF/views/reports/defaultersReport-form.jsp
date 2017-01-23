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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<div id="defaultersError" class="errorstyle" style="color: red"></div>
<form:form name="SearchRequest" role="form" action=""
	modelAttribute="DefaultersReport" id="defaultersReportSearch"
	class="form-horizontal form-groups-bordered">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title" style="text-align: left;">
						<c:choose>
							<c:when test="${mode == 'PT'}">
								<spring:message code="lbl.title.defaultersReportPT.report" />
							</c:when>
							<c:otherwise>
								<spring:message code="lbl.title.defaultersReportVLT.report" />
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<input type="hidden" name="mode" id="mode" value="${mode}" />
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"> <spring:message
								code="lbl.ward" /> :
						</label>
						<div class="col-sm-3 add-margin">
							<form:select name="wardId" id="wardId" path=""
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="-1">
									<spring:message code="lbl.default.all" />
								</form:option>
								<form:options items="${wards}" itemValue="id" itemLabel="name" />
							</form:select>
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.yrsDefaulters" /> : </label>
						<div class="col-sm-3 add-margin">
							<form:select path="" data-first-option="false" id="noofyr"
								class="form-control">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${noofyrs}" />
							</form:select>
						</div>
					</div>
					<div class="form-group">
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.fromAmount" /> : </label>
						<div class="col-sm-3 add-margin">
							<form:input path="" name="fromAmount" id="fromAmount"
								cssClass="form-control patternvalidation" data-pattern="number"
								value="${fromAmount}"
								cssErrorClass="form-control error" />
						</div>
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.toAmount" /> : </label>
						<div class="col-sm-3 add-margin">
							<form:input path="" name="toAmount" id="toAmount"
								cssClass="form-control patternvalidation" data-pattern="number"
								value="${toAmount}"
								cssErrorClass="form-control error" />
						</div>

					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.topDefaulters" /> : </label>
						<div class="col-sm-3 add-margin">
							<form:select path="" data-first-option="false" id="limit"
								class="form-control">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${limits}" />
							</form:select>
						</div>
						<c:if test="${mode == 'PT'}">
							<label class="col-sm-2 control-label text-right"> <spring:message
									code="lbl.category.ownership" /> :
							</label>
							<div class="col-sm-3 add-margin">
								<form:select path="" data-first-option="false"
									id="ownershipCategory" class="form-control">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<form:options items="${categories}" />
								</form:select>
							</div>
						</c:if>
					</div>

				</div>
			</div>
		</div>
	</div>
</form:form>
<div class="row">
	<div class="text-center">
		<button type="button" id="btnsearch" class="btn btn-primary">
			Search</button>
		<button type="button" id="btnclose" class="btn btn-default"
			onclick="window.close();">Close</button>
	</div>
</div>
<br />
<div class="row display-hide report-section">
	<br />
	<spring:message code="reports.note.text" />
	<input type="hidden"
		value="${sessionScope.citymunicipalityname}, ${sessionScope.districtName} District"
		id="pdfTitle" />
	<div class="col-md-12 table-header text-left" id="reportTitle">
		<spring:message code="lbl.title.defaultersReport.generatedOn" />
		<fmt:formatDate value="${currDate}" pattern="dd/MM/yyyy" />
	</div>
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id="tblDefaultersReport" />
	</div>
</div>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/defaultersReport.js?rnd=${app_release_no}'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/egov/patternvalidation.js?rnd=${app_release_no}' context='/egi'/>"></script>

