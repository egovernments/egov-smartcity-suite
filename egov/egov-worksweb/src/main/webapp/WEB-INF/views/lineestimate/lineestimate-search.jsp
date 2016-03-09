<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<form:form role="form" action="search" modelAttribute="lineEstimate"
	id="lineEstimatesearchform"
	cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<div class="main-content">
		<div class="row">
			<div class="col-md-12">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title">SearchLineEstimate</div>
					</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-sm-3 control-label text-right"><spring:message
									code="lineestimate.administartive.sanctionno" /> </label>
							<div class="col-sm-3 add-margin">
								<form:input path="lineEstimateNumber"
									class="form-control text-left patternvalidation"
									data-pattern="alphanumeric" maxlength="50" />
								<form:errors path="lineEstimateNumber" cssClass="error-msg" />
							</div>
							<label class="col-sm-3 control-label text-right"><spring:message
									code="lineestimate.fund" /> </label>
							<div class="col-sm-3 add-margin">
								<form:select path="fund.id" id="fund.id" cssClass="form-control"
									cssErrorClass="form-control error">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<form:options items="${funds}" itemValue="id" itemLabel="name" />
								</form:select>
								<form:errors path="fund" cssClass="error-msg" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label text-right"><spring:message
									code="lineestimate.function" /> </label>
							<div class="col-sm-3 add-margin">
								<form:select path="function.id" id="function.id"
									cssClass="form-control" cssErrorClass="form-control error">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<form:options items="${functions}" itemValue="id"
										itemLabel="name" />
								</form:select>
								<form:errors path="function" cssClass="error-msg" />
							</div>
							<label class="col-sm-3 control-label text-right"><spring:message
									code="lineestimate.budgethead" /> </label>
							<div class="col-sm-3 add-margin">
								<form:select path="budgetHead.id" id="budgetHead.id"
									cssClass="form-control" cssErrorClass="form-control error">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<form:options items="${budgetHeads}" itemValue="id"
										itemLabel="name" />
								</form:select>
								<form:errors path="budgetHead" cssClass="error-msg" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label text-right"><spring:message
									code="lineestimate.executingdepartment" /> </label>
							<div class="col-sm-3 add-margin">
								<form:select path="executingDepartment.id"
									id="executingDepartment.id" cssClass="form-control"
									cssErrorClass="form-control error">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<form:options items="${executingDepartments}" itemValue="id"
										itemLabel="name" />
								</form:select>
								<form:errors path="executingDepartment" cssClass="error-msg" />
							</div>
							<input type="hidden" id="mode" name="mode" value="${mode}" />
							<div class="form-group">
								<div class="text-center">
									<button type='button' class='btn btn-primary' id="btnsearch">
										<spring:message code='lineestimate.btn.search' />
									</button>
									<a href='javascript:void(0)' class='btn btn-default'
										onclick='self.close()'><spring:message code='lineestimate.btn.close' /></a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</form:form>
<div class="row display-hide report-section">
	<div class="col-md-12 table-header text-left">LineEstimate Search
		Result</div>
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id="resultTable">
			<thead>
				<tr>
					<th><spring:message code="lbl.lineestimatenumber" /></th>
					<th><spring:message code="lbl.fund" /></th>
					<th><spring:message code="lbl.function" /></th>
					<th><spring:message code="lbl.budgethead" /></th>
					<th><spring:message code="lbl.executingdepartment" /></th>
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
	src="<c:url value='/resources/js/lineestimate/lineEstimateHelper.js'/>"></script>

