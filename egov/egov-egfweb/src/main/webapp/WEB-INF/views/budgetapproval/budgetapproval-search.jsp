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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/tags/cdn.tld" prefix="cdn" %>
<form:form role="form" action="search" modelAttribute="budgetDetail"
	id="budgetapprovalsearchform"
	cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<div class="main-content">
		<div class="row">
			<div class="col-md-12">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title">Budget Approval</div>
					</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-sm-4 control-label text-right"><spring:message
									code="lbl.financialyear" /><span class="mandatory"></span> </label>
							<div class="col-sm-3 add-margin">
								<form:select path="budget.financialYear" id="financialYear"
									cssClass="form-control" cssErrorClass="form-control error"
									required="required">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<c:forEach items="${financialYearList}" var="mc">
										<option value="${mc.id}">${mc.finYearRange}</option>
									</c:forEach>
								</form:select>
								<form:errors path="budget.financialYear" cssClass="error-msg" />
							</div>
						</div>

						<div>
							<div class="form-group">
								<div class="col-sm-11 add-margin">
									<div class="text-center">
										<a href="javascript:void(0);" id="searchApproval"
											class="btn btn-primary"><spring:message code='lbl.search' /></a>
										</button>
										<a href='javascript:void(0)' class='btn btn-default'
											onclick='self.close()'><spring:message code='lbl.close' /></a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" id="financialYear"
		value="${budget.financialYear.id}" />


	<div class="row display-hide report-section">
		<div class="form-group">
			<div id="approvedBudget" class="col-md-6 table-header"></div>
			<div id="verifiedBudget" class="col-md-6 table-header"></div>
			<div id="notInitiated" class="col-md-6 table-header"></div>
		</div>
		<div class="row display-hide report-section">

			<div class="col-md-12 table-header text-left">List of Budgets
				pending for Administrative Approval</div>
			<div class="col-md-12 form-group report-table-container">
				<table class="table table-bordered table-hover multiheadertbl"
					id="resultTable">
					<thead>
						<tr>
							<th><spring:message code="lbl.select" /></th>
							<th><spring:message code="lbl.department" /></th>
							<th><spring:message code="lbl.rebudget" /></th>
							<th><spring:message code="lbl.bebudget" /></th>
							<th><spring:message code="lbl.reamount" /></th>
							<th><spring:message code="lbl.beamount" /></th>
							<th><spring:message code="lbl.budgetdetail.count" /></th>
						</tr>
					</thead>
				</table>
				<div class="row">
					<label class="col-sm-3 control-label text-right"><spring:message
							code="lbl.comments" /></label>
					<div class="col-sm-3 add-margin">
						<textarea class="form-control" id="comments" name="comments"></textarea>
					</div>
				</div>

			</div>
			<div>
				<div class="col-sm-11 add-margin">
					<div class="text-center">
						<a href="javascript:void(0);" id="approve" class="btn btn-primary"><spring:message
								code='lbl.approve' /></a>
						</button>
						<a href="javascript:void(0);" id="reject" class="btn btn-primary"><spring:message
								code='lbl.reject' /></a>
						</button>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="comments" value="${comments}" />
	</div>
</form:form>
<script>
	$('#searchApproval').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
</script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/app/js/budgetApprovalHelper.js'/>"></script>
