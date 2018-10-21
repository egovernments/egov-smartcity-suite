<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2018  eGovernments Foundation
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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<form:form id="applicationSearchForm" method="get"
	class="form-horizontal form-groups-bordered"
	modelAttribute="applicationSearch">
	<div class="row">
		<div class="col-md-12">
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-2 col-md-2 control-label "> <spring:message
								code="lbl.application.number" /></label>
						<div class="col-sm-3 col-md-3 add-margin">
							<form:input path="" type="text" name="applicationNumber" id="applicationNumber"
								class="form-control typeahead" autocomplete="off" maxlength="13" 
								data-pattern="alphanumeric" min="10" />
							<%-- <form:hidden path="applicationNumber" id="applicationNo"/> --%>
						</div>
						<label class="col-sm-2 col-md-2 control-label"> <spring:message
								code="lbl.shsc.number" /></label>
						<div class="col-sm-3 col-md-3 add-margin">
							<form:input path="" type="text" name="shscNumber" id="shscNumber"
								class="form-control typeahead" autocomplete="off" maxlength="11"/>
						</div>
					</div>
					<div class="form-group ">
						<label class="col-sm-2 col-md-2 control-label "> <spring:message
								code="lbl.propertyid" /></label>
						<div class="col-sm-3 col-md-3 add-margin">
							<form:input path="" type="text" name="propertyId" id="propertyId" autocomplete="off"
								class="form-control typeahead" maxlength="13"
								data-pattern="form-control is_valid_number"  />
						</div>
						<label class="col-sm-2 col-md-2 control-label"> <spring:message
								code="lbl.mobileNo" />
						</label>
						<div class="col-sm-3 col-md-3 add-margin">
							<form:input path="" type="text" name="mobileNo" id="mobileNo"
								class="form-control is_valid_number" maxlength="10"
								data-inputmask="'mask': '9999999999'"  min="10" />
						</div>
					</div>
					<div class="form-group">
                      <div class="col-md-3 add-margin" style="margin-left: 10%">
                         <a href="javascript:void(0);" id="toggle-searchmore"
                            class="btn btn-secondary"><spring:message code='lbl.more' />..</a>
                      </div>
                    </div>
					<div class="form-group show-search-more display-hide">
					  <div class="form-group">
						 <label class="col-sm-2 col-md-2 control-label"> <spring:message
								code="lbl.applicantname" />
						</label>
					    <div class="col-sm-3 col-md-3 add-margin">
							<form:input path="" type="text" name="applicantName" id="applicantName"
								class="form-control typeahead" maxlength="100"  />
					   </div> 
					   <label for="field-1" class="col-md-2 col-sm-2 control-label"><spring:message
								code="lbl.revenue.ward" /></label>
						 <div class="col-sm-3 col-md-3 add-margin">
						 <form:select name="revenueWard" id="revenueWard" path=""
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${revenueWards}" id="revenueWard" name="revenueWard"
									itemValue="name" itemLabel="name" />
							</form:select>
						 </div>
					 </div>
					 <div class="form-group">
						<label class="col-sm-2 col-md-2 control-label"><spring:message
								code="lbl.fromDate" /></label>
						<div class="col-sm-3 col-md-3 add-margin">
							<form:input path="" type="text"  name="fromDate"  class="form-control datepicker"
								id="fromDate" maxlength="24" pattern="dd-MM-yyyy" />
						</div>
						
						<label class="col-sm-2 col-md-2 control-label"><spring:message
								code="lbl.toDate" /></label>
						<div class="col-sm-3 col-md-3 add-margin">
							<form:input path="" type="text"  name="toDate"  class="form-control datepicker"
								id="toDate" maxlength="24" pattern="dd-MM-yyyy" />
								<form:errors path="toDate" cssClass="add-margin error-msg" />
							<div id="todateerror" class="error-msg display-hide">To Date should be greater than From Date</div>
						</div>
					</div> 
					<div class="form-group">
						<label for="field-1" class="col-md-2 col-sm-2 control-label"><spring:message
								code="lbl.connection.ststus" /></label>
						<div class="col-sm-3 add-margin">
							<form:select name="connectionStatus" id="connectionStatus" path="connectionStatus"
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${connectionStatus}" />
							</form:select>
						</div>
						<label for="field-1" class="col-md-2 col-sm-2 control-label"><spring:message
								code="lbl.application.types" /></label>
						<div class="col-sm-3 add-margin">
							<form:select name="applicationType" id="applicationType" path=""
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${applicationTypes}" />
							</form:select>
						</div>
					</div>
			</div>
			<div class="form-group">
				<div class="text-center">
					<a href="javascript:void(0);" id="searchSewerageapplication"
						class="btn btn-primary btnSearch"><spring:message code='lbl.search' /></a>

					<button class="btn btn-danger" type="reset">
						<spring:message code='lbl.reset' />
					</button>
					<a href="javascript:void(0);" id="close"
						class="btn btn-default" onclick="self.close()"><spring:message
							code='lbl.close' /></a>
				</div>
			</div>
			</div>
		</div>
	</div>
</form:form>
<div class="panel-footer">
  <spring:message code="search.application.note"/>
</div>
<div class="row display-hide report-section" id="table_container">
	<div class="col-md-12 table-header text-left">
		<spring:message code='lbl.searchresult' />
	</div>
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered datatable dt-responsive"
			id="aplicationSearchResults">
			 <thead>
				 <tr>
				 </tr>
			 </thead>
		</table>
	</div>
</div>
<link rel="stylesheet"
      href="<cdn:url  value='/resources/global/css/bootstrap/typeahead.css' context='/egi'/>">
<script type="text/javascript"
        src="<cdn:url  value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<link rel="stylesheet"
	href="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/moment.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/datetime-moment.js' context='/egi'/>"></script>
<script
	src="<cdn:url  value='/resources/javascript/helper.js?rnd=${app_release_no}'/>"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/search/applicationsearch.js?rnd=${app_release_no}'/>"></script> 
