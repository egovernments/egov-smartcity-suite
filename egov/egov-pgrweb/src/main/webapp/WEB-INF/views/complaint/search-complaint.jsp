
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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
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
				<form:form  class="form-horizontal form-groups-bordered" id="searchComplaintForm" 
				modelAttribute="complaintSearchRequest" action="">
					<div class="form-group">
						<div class="col-md-3 add-margin">
							<input type="text" name="complaintNumber" class="form-control patternvalidation" data-pattern="alphanumericwithhyphen"
								id="ct-ctno"
								placeholder="<spring:message code='lbl.complaint.number'/>" />
						</div>
						<div class="col-md-3 add-margin">
							<input type="text" name="location" class="form-control" id="ct-location"
								placeholder="<spring:message code='lbl.location'/> " />
						</div>
						<input type="hidden" name="employeeposition" class="form-control" id="employeeposition" 
						value="${employeeposition}"/>
						<input type="hidden" name="currentLoggedUser" class="form-control" id="currentLoggedUser"
							 value="${currentLoggedUser}"/>
						<input type="hidden" name="currentUlb" class="form-control" id="currentUlb" value="${currentUlb}"/>
						<input type="hidden" name="isgorole" class="form-control" id="isgorole" value="${isGrievanceOfficer}"/>
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
								<option value="all"><spring:message	code='lbl.complaint.search.all' /></option>
								<option value="lastsevendays"><spring:message code='lbl.complaint.search.l7d' /></option>
								<option value="lastthirtydays"><spring:message code='lbl.complaint.search.l30d' /></option>
								<option value="lastninetydays"><spring:message code='lbl.complaint.search.l90d' /></option>
								<option value="today"><spring:message code='lbl.today' /></option>
							</select>
						</div>
						<div class="col-md-3 add-margin">
							<input type="text" name="fromDate" class="form-control datepicker checkdate" id="start_date" 
							data-inputmask="'mask': 'd/m/y'" placeholder="<spring:message code='lbl.fromDate'/>" />
						</div>
						<div class="col-md-3 add-margin">
							<input type="text" name="toDate" class="form-control datepicker checkdate" id="end_date" 
							data-inputmask="'mask': 'd/m/y'" placeholder="<spring:message code='lbl.toDate'/>" />
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-3 col-xs-12 add-margin">
							<a href="javascript:void(0);" id="toggle-searchcomp"
								class="btn btn-secondary"><spring:message code='lbl.more' />..</a>
						</div>
					</div>
					<input type="hidden" value="${isMore }" id="isMore" />
					<div class="form-group show-searchcomp-more display-hide">
						<div class="col-md-3 add-margin">
							<input type="text" name="complainantName" class="form-control patternvalidation" data-pattern="alphabetwithspace" id="ct-name"
								placeholder="<spring:message code='lbl.name'/>" />
						</div>
						<div class="col-md-3 add-margin">
							<input type="text" name="complainantPhoneNumber" class="form-control patternvalidation" data-pattern="number" id="ct-mobno"
								placeholder="<spring:message code='lbl.phoneNumber'/>" maxlength="10"/>
						</div>
						<div class="col-md-3 add-margin">
							<input type="text" name="complainantEmail" class="form-control" id="ct-email"
								placeholder="<spring:message code='lbl.email'/>" />
						</div>
							<%--<div class="col-md-3 add-margin">
                                <form:select name="complaintDepartment" path="" data-first-option="false" cssClass="form-control" >
                                    <form:option value=""><spring:message code="lbl.complaintDepartment" /></form:option>
                                    <form:options items="${complaintTypeDepartments}" itemValue="name" itemLabel="name" />
                                </form:select>
                            </div>--%>
						<div class="col-md-3 add-margin">
							<form:select name="complaintType" path="" data-first-option="false" cssClass="form-control">
								<form:option value=""><spring:message code="lbl.complaintType"/></form:option>
								<form:options items="${complaintTypedropdown}" itemValue="name" itemLabel="name"/>
							</form:select>
						</div>
					</div>
					<div class="form-group show-searchcomp-more display-hide">
						<div class="col-md-3 add-margin">
							<form:select name="complaintStatus" path="" data-first-option="false" cssClass="form-control" >
								<form:option value=""><spring:message code="lbl.status" /></form:option>
								<form:options items="${complaintStatuses}" itemValue="name" itemLabel="name" />  
							</form:select>
						</div>
						<div class="col-md-3 add-margin">
							<form:select name="receivingMode" path="" data-first-option="false" cssClass="form-control" >
								<form:option value=""><spring:message code="lbl.receivingmode" /></form:option>
								<form:options items="${complaintReceivingModes}"  itemValue="code" itemLabel="name"/>
							</form:select>
						</div>
					</div>
			</div>
		</div>
		<div class="form-group">
			<div class="text-center">
				<button type="button" id="searchComplaints" class="btn btn-primary">
				<spring:message code='lbl.search' /></button>
				<button type="reset" class="btn btn-default"><spring:message code="lbl.reset" /></button>
				<a href="javascript:void(0);" id="closeComplaints" class="btn btn-default" onclick="self.close()">
				<spring:message code='lbl.close' /></a>
			</div>
		</div>
		</form:form>
	</div>
</div>
<div>
	<strong class="head-font table-header"><spring:message code="msg.search.result"/></strong>
	<table class="table table-bordered datatable dt-responsive" id="complaintSearchResults"></table>
</div>

<link rel="stylesheet" href="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url  value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>" type="text/javascript"></script>
<link rel="stylesheet" href="<cdn:url  value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>">
<script src="<cdn:url  value='/resources/js/app/search-complaint.js?rnd=${app_release_no}'/>" type="text/javascript"></script>
<c:if test="${not empty param.crn}">
<script>
$("#ct-ctno").val('${param.crn}');
$( "#searchComplaints").trigger("click");
</script>
</c:if>