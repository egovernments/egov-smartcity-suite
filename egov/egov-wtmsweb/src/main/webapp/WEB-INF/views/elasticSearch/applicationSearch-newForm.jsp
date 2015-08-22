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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
					<label for="field-1" class="col-sm-3 control-label"> <spring:message
										code="lbl.Service" /></label>
						<div class="col-sm-3 add-margin">
						<form:select name="moduleName" path="" data-first-option="false" id="moduleName"
							cssClass="form-control" >
							<form:option value="">
								Select from below
							</form:option>
							<form:options items="${modulesList}"  />  
						</form:select>
						</div>
						<label for="field-1" class="col-sm-2 control-label"><spring:message
										code="lbl.appType" /></label>
						<div class="col-sm-3 add-margin">
						<form:select name="applicationType" path="" data-first-option="false" id="applicationType"
							cssClass="form-control" >
							<form:option value="">
								Select from below
							</form:option>
							<form:options items="${applicationTypeList}"  />  
						</form:select>
						</div>
						</div>
						
						<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label"> <spring:message
										code="lbl.applicant.name" /></label>
						<div class="col-sm-3 add-margin">
							<input type="text" name="applicationName" class="form-control" id="app-mobno"
								 />
						</div>
							<label for="field-1" class="col-sm-2 control-label"><spring:message
										code="lbl.application.no" />
								</label>
						<div class="col-sm-3 add-margin">
						<input type="text" name="applicationNumber" class="form-control" id="app-mobno"
								 />
						</div>
						</div>
						<div class="form-group">
							<label for="field-1" class="col-sm-3 control-label"><spring:message
										code="lbl1.consumer.number" />
								</label>
						<div class="col-sm-3 add-margin">
							<input type="text" name="applicationCode" class="form-control" id="app-appcodo"
								 />
						</div>
								<label for="field-1" class="col-sm-2 control-label"> <spring:message
										code="lbl.application.mobileNo" />
								</label>
							<div class="col-sm-3 add-margin">
							<input type="text" name="appMobileNo" class="form-control" id="app-appcodo" min="10" maxlength="12" />
						</div>
						</div>
						
						<div class="form-group">
							<label for="field-1" class="col-sm-3 control-label"> <spring:message
										code="lbl.fromDate" />
								</label>
						<div class="col-sm-3 add-margin">
						<input type="text" name="fromDate" class="form-control datepicker" data-date-end-date="0d" 
								id="start_date" data-inputmask="'mask': 'd/m/y'"
								/>
						</div>
						<label for="field-1" class="col-sm-2 control-label"> <spring:message code="lbl.toDate" /></label>
						<div class="col-sm-3 add-margin">
						<input type="text" name="toDate" class="form-control datepicker today" data-date-end-date="0d" 
								id="end_date" data-inputmask="'mask': 'd/m/y'" />
						</div>

						</div>
						
					
					<div class="form-group">
						<div class="text-center">
							<a href="javascript:void(0);" class="btn btn-primary" id="searchapplication" 
								><spring:message code='lbl.search' /></a>
								
							<button class="btn btn-danger" type="reset">Reset</button>
							<a href="javascript:void(0);" id="closeComplaints"
								class="btn btn-default" onclick="self.close()"><spring:message code='lbl.close' /></a>
						</div>
				</div>


				</form:form>


			</div>


		</div>

	</div>
</div>



	<div class="row">
					<div class="col-md-6 col-xs-6 table-header">The Search result is</div>
					<div class="col-md-6 col-xs-6 add-margin text-right">
						<span class="inline-elem">Search</span>
						<span class="inline-elem"><input type="text" id="searchapp" class="form-control input-sm"></span>
					</div>
	<div class="col-md-12">	
	<table class="table table-bordered datatable dt-responsive"
		id="aplicationSearchResults">

	</table>
	</div>
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
