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
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<div id="searchFormDiv">
	<form:form name="abstractEstimateForm" role="form" modelAttribute="searchRequestLetterOfAcceptance"
		id="searchRequestLetterOfAcceptance"	class="form-horizontal form-groups-bordered">
		<div class="alert text-left" style="color: red;" id="errorMessage" hidden="true"></div>
		<input type="hidden" id="errorselectloa" value="<spring:message code='error.loa.required' />">
		<input type="hidden" value="<spring:message code="error.date.todate.fromdate" />" id="errorToDateAndFromDate" />
		<div class="row">
			<div class="col-md-12">
				<div class="panel-heading"></div>
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title" style="text-align: center;">
						<spring:message code="title.search.letterofacceptance.tocreatemb" />
					</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message code="lbl.loanumber" /></label>
						<div class="col-sm-3 add-margin">
							<form:input path="workOrderNumber"	id="workOrderNumber" class="form-control"	placeholder="Type first 3 letters of LOA Number" />
							<form:errors path="workOrderNumber" cssClass="add-margin error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.abstractestimatenumber" /></label>
						<div class="col-sm-3 add-margin">
							<form:input path="estimateNumber"	id="estimateNumber" class="form-control"	placeholder="Type first 3 letters of Abstract/Detailed Estimate Number" />
							<form:errors path="estimateNumber" cssClass="add-margin error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message code="lbl.fromdate" /></label>
						<div class="col-sm-3 add-margin">
							<form:input path="fromDate" class="form-control datepicker"	id="fromDate" data-inputmask="'mask': 'd/m/y'" />
							<form:errors path="fromDate" cssClass="add-margin error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.todate" /></label>
						<div class="col-sm-3 add-margin">
							<form:input path="toDate" class="form-control datepicker" id="toDate" data-inputmask="'mask': 'd/m/y'" />
							<form:errors path="toDate" cssClass="add-margin error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message code="lbl.contractor" /></label>
							<div class="col-sm-3 add-margin">
				              <form:input path="contractor" id="contractorSearch" class="form-control" placeholder="Type first 3 letters of Contractor Name Or Code"/>
				              <form:errors path="contractor" cssClass="add-margin error-msg" />
			                  </div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.department" /></label>
						<div class="col-sm-3 add-margin">
							<form:select path="departmentName" data-first-option="false" id="departments" class="form-control">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${departments}" itemValue="id"	itemLabel="name" />
							</form:select>
							<form:errors path="departmentName" cssClass="add-margin error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message code="lbl.workidnumber" /></label>
						<div class="col-sm-3 add-margin">
						<form:input path="workIdentificationNumber" class="form-control" id="workIdentificationNumber"	placeholder="Type first 3 letters of Work Identification Number" />
						<form:errors path="workIdentificationNumber" cssClass="add-margin error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.status" /></label>
						<div class="col-sm-3 add-margin">
						<form:input path="egwStatus" class="form-control" id="egwStatus" value="" readonly="true" />
						</div>
					</div>
				</div>
			</div>
			</div>
		</div>
		
		<div class="row">
				<div class="col-sm-12 text-center">
					<button type='button' class='btn btn-primary' id="btnsearch">
						<spring:message code='lbl.search' />
					</button>
					<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
				</div>
		</div>
	</form:form>
	<jsp:include page="workorder-searchresult.jsp" />
	<script src="<cdn:url value='/resources/js/mb/searchloaformbheader.js?rnd=${app_release_no}'/>"></script>
</div>
<div id="successPage" hidden="true">
	<jsp:include page="mb-success.jsp"/>
</div>
<div id="measurementBookDiv" hidden="true">
	<jsp:include page="measurementbook-form.jsp"/>
</div>
<script src="<cdn:url value='/resources/js/mb/mbajaxsubmit.js?rnd=${app_release_no}'/>"></script>