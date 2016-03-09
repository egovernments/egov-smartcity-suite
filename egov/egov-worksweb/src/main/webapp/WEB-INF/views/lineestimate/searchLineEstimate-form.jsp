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
<%@ include file="/includes/taglibs.jsp" %> 
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script src="<egov:url path='resources/js/helper.js'/>"></script>
<script src="<egov:url path='resources/js/works.js'/>"></script>
<div class="page-container" id="page-container">
	<div class="main-content">
		<form:form name="lineEstimateSearchForm" role="form" action="/egworks/lineestimate/search" modelAttribute="lineEstimateSearchRequest" id="lineEstimatesearchform" class="form-horizontal form-groups-bordered">
			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title" style="text-align:center;"><spring:message code="title.search.lineestimate" /></div>
						</div>
						<div class="panel-body">
							<div class="form-group">
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.administartive.sanctionno" /></label>
								<div class="col-sm-3 add-margin">
									<form:input path="adminSanctionNumber" id="adminSanctionNumber" class="form-control"/>
									<form:errors path="adminSanctionNumber" cssClass="add-margin error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.executingdepartment" /></label>
								<div class="col-sm-3 add-margin">
									<form:select path="executingDepartment" data-first-option="false" id="executingDepartments" class="form-control">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${executingDepartments}" itemValue="id" itemLabel="name" />
									</form:select>
									<form:errors path="executingDepartment" cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.adminsanctioned.fromdate" /></label>
								<div class="col-sm-3 add-margin">
									<form:input path="adminSanctionFromDate" class="form-control datepicker"	id="adminSanctionFromDate" data-inputmask="'mask': 'd/m/y'" />
									<form:errors path="adminSanctionFromDate" cssClass="add-margin error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.adminsanctioned.todate" /></label>
								<div class="col-sm-3 add-margin">
									<form:input path="adminSanctionToDate" class="form-control datepicker"	id="v" data-inputmask="'mask': 'd/m/y'" />
									<form:errors path="adminSanctionToDate" cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.fund" /></label>
								<div class="col-sm-3 add-margin">
									<form:select path="fund" data-first-option="false" class="form-control" id="fund">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${funds}" itemValue="id" itemLabel="name"/>
									</form:select>
									<form:errors path="fund" cssClass="add-margin error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.function" /></label>
								<div class="col-sm-3 add-margin">
									<form:select path="function" data-first-option="false" name="function" class="form-control" id="function">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${functions}" itemValue="id" itemLabel="name"/>
									</form:select>
									<form:errors path="function" cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.budgethead" /></label>
								<div class="col-sm-3 add-margin">
									<form:select path="budgetHead" data-first-option="false" id="budgetHead" class="form-control">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${budgetHeads}" itemValue="id" itemLabel="name"/>
									</form:select>
									<form:errors path="budgetHead" cssClass="add-margin error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.estimateno" /></label>
								<div class="col-sm-3 add-margin">
									<form:input path="estimateNumber" id="estimateNumber" class="form-control"/>
									<form:errors path="estimateNumber" cssClass="add-margin error-msg" />
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 text-center">
					<button type='button' class='btn btn-primary' id="btnsearch">
						<spring:message code='lineestimate.btn.search' />
					</button>
					<a href='javascript:void(0)' class='btn btn-default'
						onclick='self.close()'><spring:message code='lineestimate.btn.close' /></a>
				</div>
			</div>
		</form:form>  
	</div>
</div>
<div class="row display-hide report-section">
	<div class="col-md-12 table-header text-left">LineEstimate Search
		Result</div>
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id="resultTable">
			<thead>
				<tr>
					<th><spring:message code="lineestimate.administartive.sanctionno" /></th>
					<th><spring:message code="lbl.executingdepartment" /></th>
					<th><spring:message code="lbl.fund" /></th>
					<th><spring:message code="lbl.function" /></th>
					<th><spring:message code="lbl.budgethead" /></th>
					<th><spring:message code="lineestimate.createdby" /></th>
					<th><spring:message code="lineestimate.totalamount" /></th>
				</tr>
			</thead>
		</table>
	</div>
</div>
<script>
	$('#btnsearch').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
</script>
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
<script type="text/javascript"
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.columnFilter.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script
	src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<c:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<c:url value='/resources/js/lineestimate/searchlineestimatehelper.js'/>"></script>