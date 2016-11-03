<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
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
									code="lbl.financialyear" /> </label>
							<div class="col-sm-3 add-margin">
								<form:select path="budget.financialYear" id="financialYear"
									cssClass="form-control" cssErrorClass="form-control error">
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
					</tr>
				</thead>
			</table>
			<div>
				<div class="col-sm-11 add-margin">
					<div class="text-center">
						<a href="javascript:void(0);" id="approve" class="btn btn-primary"><spring:message
								code='lbl.approve' /></a>
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
	src="<c:url value='/resources/app/js/budgetApprovalHelper.js'/>"></script>