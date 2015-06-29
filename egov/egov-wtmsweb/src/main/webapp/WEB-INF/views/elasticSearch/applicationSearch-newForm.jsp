
<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<div class="row">
	<div class="col-md-12">

		<div class="panel panel-primary" data-collapsed="0">

			<div class="panel-heading">
				<div class="panel-title">
					<strong><spring:message code='title.AppliactionSearch' />
					</strong>
				</div>

			</div>

			<div class="panel-body">

				<form:form  class="form-horizontal form-groups-bordered"
					id="applicationSearchRequestForm" modelAttribute="applicationSearchRequest" action="">

					<div class="form-group">

						<div class="col-md-4 add-margin">
						<form:select name="moduleName" path="" data-first-option="false" 
							cssClass="form-control" >
							<form:option value="">
								<spring:message code="lbl.Service" /> 
							</form:option>
							<form:options items="${modulesList}"  />  
						</form:select>
						</div>

						<div class="col-md-4 add-margin">
						<form:select name="applicationType" path="" data-first-option="false" 
							cssClass="form-control" >
							<form:option value="">
								<spring:message code="lbl.appType" /> 
							</form:option>
							<form:options items="${applicationTypeList}"  />  
						</form:select>
						</div>
						</div>
						<div class="form-group">
					
						<div class="col-md-4 add-margin">
							<input type="text" name="applicationName" class="form-control" id="app-mobno"
								placeholder="<spring:message code='lbl.name'/>" />
						</div>
						
						<div class="col-md-4 add-margin">
							<input type="text" name="applicationNumber" class="form-control" id="app-mobno"
								placeholder="<spring:message code='lbl.application.no'/>" />
						</div>
						</div>
						<div class="form-group">
						<div class="col-md-4 add-margin">
							<input type="text" name="applicationCode" class="form-control" id="app-appcodo"
								placeholder="<spring:message code='lbl.application.code'/>" />
						</div>
							<div class="col-md-4 add-margin">
							<input type="text" name="appMobileNo" class="form-control" id="app-mobno"
								placeholder="<spring:message code='lbl.application.mobileNo'/>" />
						</div>
						</div>
						
						<div class="form-group">
						<div class="col-md-4 add-margin">
							<input type="text" name="fromDate" class="form-control datepicker checkdate"
								id="start_date" data-inputmask="'mask': 'd/m/y'"
								placeholder="<spring:message code='lbl.fromDate'/>" />
						</div>
						<div class="col-md-4 add-margin">
							<input type="text" name="toDate" class="form-control datepicker checkdate"
								id="end_date" data-inputmask="'mask': 'd/m/y'"
								placeholder="<spring:message code='lbl.toDate'/>" />
						</div>

						</div>
						
						<div class="form-group show-searchcomp-more display-hide">

						


						

					</div>
					<div class="form-group">
						<div class="text-center">
							<a href="javascript:void(0);" id="searchapplication"
								class="btn btn-primary"><spring:message code='lbl.search' /></a>
							<button type="reset" class="btn btn-default">
				<spring:message code="lbl.reset" />
			</button>
							<a href="javascript:void(0);" id="closeComplaints"
								class="btn btn-default" onclick="self.close()"><spring:message code='lbl.close' /></a>
						</div>
				</div>


				</form:form>


			</div>


		</div>

	</div>
</div>


<div>
	<strong class="head-font">The search result is</strong>
	<table class="table table-bordered datatable dt-responsive"
		id="aplicationSearchResults">

	</table>

</div>

<link rel="stylesheet"
	href="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<script
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
	type="text/javascript"></script>

<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"
	type="text/javascript"></script>

<script src="<c:url value='/resources/js/app/applicationsearch.js'/>"
	type="text/javascript"></script>
