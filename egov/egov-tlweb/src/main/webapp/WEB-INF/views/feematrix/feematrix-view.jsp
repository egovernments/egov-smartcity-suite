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
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<script
	src="<cdn:url  value='/resources/js/app/feematrix.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url  value='/resources/js/app/helper.js'/>"></script>
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">FeeMatrix</div>
			</div>
			<div class="panel-body">
				<form:form role="form" action="feematrix/create"
					modelAttribute="feeMatrix" id="feematrix-new" name="feematrix-new"
					cssClass="form-horizontal form-groups-bordered"
					enctype="multipart/form-data">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.licensecategory" /></label>
						<div class="col-sm-3 add-margin">
							<form:select path="licenseCategory" id="licenseCategory"
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${licenseCategorys}" itemValue="id"
									itemLabel="name" />
							</form:select>
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.subcategory" /></label>
						<div class="col-sm-3 add-margin">
							<form:select path="subCategory" id="subCategory"
										 cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
							</form:select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.financialyear" /> </label>
						<div class="col-sm-3 add-margin">
							<form:select path="financialYear" id="financialYear"
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${financialYears}" itemValue="id"
									itemLabel="finYearRange" />
							</form:select>
						</div>
					</div>
				</form:form>
				<div class="form-group text-center">
					<button type='button' class='btn btn-primary' id="search">
						<spring:message code='lbl.search' />
					</button>
					<button type="button" class="btn btn-default" data-dismiss="modal"
						onclick="window.close();">Close</button>
				</div>
				<div class="row display-hide report-section">
					<div class="col-md-12 table-header text-left">
						<spring:message code="title.feematrix.result" />
					</div>
					<div class="col-md-12 form-group report-table-container">
						<table class="table table-bordered table-hover multiheadertbl"
							id="resultTable">
							<thead>
								<tr>
									<th><spring:message code="lbl.licensecategory" /></th>
									<th><spring:message code="lbl.subcategory" /></th>
									<th><spring:message code="lbl.unitofmeasurement" /></th>
									<th><spring:message code="lbl.rateType" /></th>
									<th><spring:message code="lbl.financialyear" /></th>
									<th><spring:message code="lbl.uomfrom" /></th>
									<th><spring:message code="lbl.uomto" /></th>
									<th><spring:message code="lbl.rate" /></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	jQuery('#search').click(function(e) {
		callAjaxSearch();
	});
	function callAjaxSearch() {
		viewMatrixContainer = jQuery("#resultTable");
		jQuery('.report-section').removeClass('display-hide');
		reportdatatable = viewMatrixContainer
				.dataTable({
					ajax : {
						url : "/tl/feematrix/viewresult?category="
								+ $('#licenseCategory').val() + "&subCategory="
								+ $("#subCategory").val() + "&finyear="
								+ $("#financialYear").val(),
						type : "POST"
					},
					dom : "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-3 col-xs-6 text-right'B><'col-md-4 col-xs-6 text-right'p>>",
					"autoWidth" : false,
					"bDestroy" : true,
					buttons : [ {
						extend : 'pdf',
						title : 'License Fee Matrix',
						filename : 'License Fee Matrix',
						orientation : 'landscape',
						footer : true,
						pageSize : 'A3',
						exportOptions : {
							columns : ':visible'
						}
					}, {
						extend : 'excel',
						filename : 'License Fee Matrix',
						footer : true,
						exportOptions : {
							columns : ':visible'
						}
					}, {
						extend : 'print',
						title : 'License Fee Matrix',
						filename : 'License Fee Matrix',
						footer : true,
						exportOptions : {
							columns : ':visible'
						}
					} ],
					columns : [ {
						"data" : "licenseCategory",
						"sClass" : "text-left"
					}, {
						"data" : "subCategory",
						"sClass" : "text-left"
					}, {
						"data" : "uom",
						"sClass" : "text-left"
					}, {
						"data" : "rateType",
						"sClass" : "text-right"
					}, {
						"data" : "financialYear",
						"sClass" : "text-right"
					}, {
						"data" : "uomfrom",
						"sClass" : "text-right"
					}, {
						"data" : "uomto",
						"sClass" : "text-right"
					}, {
						"data" : "rate",
						"sClass" : "text-right"
					} ]
				});
	}
</script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/dataTables.buttons.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/buttons.bootstrap.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/buttons.flash.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/jszip.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/pdfmake.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/vfs_fonts.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/buttons.html5.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/buttons.print.min.js' context='/egi'/>"></script>