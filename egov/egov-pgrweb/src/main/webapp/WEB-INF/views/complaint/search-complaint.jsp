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
# 	   with regards to rights under tradecomplaintTypeServicemark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
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
					<strong><spring:message code='title.searchComplaints' />
					</strong>
				</div>

			</div>

			<div class="panel-body">

				<form role="form" class="form-horizontal form-groups-bordered"
					id="searchComplaintForm" action="">

					<div class="form-group">

						<div class="col-md-6 add-margin">
							<input type="text" name="searchText" class="form-control"
								id="ct-search"
								placeholder="<spring:message code='lbl.complaint.search.searchText' /> " />
						</div>

						<div class="col-md-6 add-margin">
							<input type="text" name="" class="form-control" id="ct-location"
								placeholder="<spring:message code='lbl.location'/> " />
						</div>

					</div>

					<div class="form-group">

						<div class="col-md-3 add-margin">
							<label><spring:message code='lbl.when' /> </label>
						</div>
						<div class="col-md-3 add-margin">
							<select name="complaintDate" id="when_date" class="form-control"
								data-first-option="false">
								<option value=""><spring:message code='lbl.select' />
								</option>
								<option value="all"><spring:message
										code='lbl.complaint.search.all' /></option>
								<option value="lastsevendays"><spring:message
										code='lbl.complaint.search.l7d' /></option>
								<option value="lastthirtydays"><spring:message
										code='lbl.complaint.search.l30d' /></option>
								<option value="lastninetydays"><spring:message
										code='lbl.complaint.search.l90d' /></option>
								<option value="today"><spring:message code='lbl.today' /></option>
							</select>
						</div>
						<div class="col-md-3 add-margin">
							<input type="text" name="fromDate" class="form-control datepicker checkdate"
								id="start_date" data-inputmask="'mask': 'd/m/y'"
								placeholder="<spring:message code='lbl.fromDate'/>" />
						</div>
						<div class="col-md-3 add-margin">
							<input type="text" name="toDate" class="form-control datepicker checkdate"
								id="end_date" data-inputmask="'mask': 'd/m/y'"
								placeholder="<spring:message code='lbl.toDate'/>" />
						</div>

					</div>

					<div class="form-group">
						<div class="col-md-3 col-xs-12 add-margin">
							<a href="javascript:void(0);" id="toggle-searchcomp"
								class="btn btn-secondary"><spring:message code='lbl.more' />..</a>
						</div>
					</div>

					<div class="form-group show-searchcomp-more display-hide">

						<div class="col-md-4 add-margin">
							<input type="text" name="complaintNumber" class="form-control"
								id="ct-ctno"
								placeholder="<spring:message code='lbl.complaint.number'/>" />
						</div>
						<div class="col-md-4 add-margin">
							<input type="text" name="complainantName" class="form-control" id="ct-name"
								placeholder="<spring:message code='lbl.name'/>" />
						</div>
						<div class="col-md-4 add-margin">
							<input type="text" name="complainantPhoneNumber" class="form-control" id="ct-mobno"
								placeholder="<spring:message code='lbl.phoneNumber'/>" />
						</div>

					</div>

					<div class="form-group show-searchcomp-more display-hide">

						<div class="col-md-4 add-margin">
							<input type="text" name="complainantEmail" class="form-control" id="ct-email"
								placeholder="<spring:message code='lbl.email'/>" />
						</div>
						<div class="col-md-4 add-margin">
							<input type="text" name="complaintType" class="form-control" id="ct-type"
								placeholder="<spring:message code='lbl.complaintType'/>" />
						</div>
						<div class="col-md-4 add-margin">
							<select name="test" id="ct-sel-dept" class="form-control"
								data-first-option="false">
								<option><spring:message code='lbl.department' /></option>
								<option value="1">HEALTH</option>
								<option value="2">ELECTRICAL</option>
							</select>
						</div>
					</div>

					<div class="form-group show-searchcomp-more display-hide">

						<div class="col-md-4 add-margin">
							<select name="complaintStatus" id="ct-sel-status" class="form-control"
								data-first-option="false" >
								<option value=""><spring:message code='lbl.status' /></option>
								<option value="REGISTERED">REGISTERED</option>
								<option value="FORWARDED">FORWARDED</option>
								<option value="CLOSED">CLOSED</option>
								
							</select>
						</div>
						<div class="col-md-4 add-margin">
							<select name="receivingCenter" id="ct-sel-recenter" class="form-control"
								data-first-option="false">
								<option  value=""><spring:message code='lbl.receivingcenter' /></option>
								<option value="CALL">Complaint Cell</option>
								<option value="SMS">SMS</option>
								<option value="WEBSITE">Website</option>
								<option value="EMAIL">Email</option>
								<option value="PAPER">Paper</option>
								<option value="MOBILE">Mobile</option>
							</select>
						</div>
					</div>

					<div class="form-group show-searchcomp-more display-hide">

						


						

					</div>


					<div class="form-group">
						<div class="text-center">
							<a href="javascript:void(0);" id="searchComplaints"
								class="btn btn-primary"><spring:message code='lbl.search' /></a>
							<button type="reset" class="btn btn-default">
				<spring:message code="lbl.reset" />
			</button>
							<a href="javascript:void(0);" id="closeComplaints"
								class="btn btn-default" onclick="self.close()"><spring:message code='lbl.close' /></a>
						</div>
					</div>


				</form>


			</div>


		</div>

	</div>
</div>

<div>
	<strong class="head-font">The search result is</strong>
	<table class="table table-bordered datatable dt-responsive"
		id="complaintSearchResults">

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
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.columnFilter.js' context='/egi'/>"
	type="text/javascript"></script>

<script
	src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"
	type="text/javascript"></script>

<script src="<c:url value='/resources/js/app/search-complaint.js'/>"
	type="text/javascript"></script>
