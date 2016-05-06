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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>


<div class="row" id="page-content">
	<div class="col-md-12">
        <c:if test="${not empty message}">
            <div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
        </c:if>
        
		<form:form role="form" method="post" cssClass="form-horizontal form-groups-bordered" id="usageform" modelAttribute="searchModel">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="title.registration.search" />
					</div>
				</div>
				
				<div class="panel-body custom-form">
					<div class="row">
					<div class="form-group">
						<label for="registrationNo" class="col-sm-2 control-label"><spring:message
								code="lbl.application.no" /></label>

						<div class="col-sm-3 add-margin">
							<form:input id="registrationNo" path="registrationNo" type="text" cssClass="form-control low-width is_valid_alphnumeric" autocomplete="off" required="required" />
							<form:errors path="registrationNo" cssClass="error-msg"/>
						</div>
						<label for="dateOfMarriage" class="col-sm-2 control-label"><spring:message
								code="lbl.date.of.marriage" /></label>
						<div class="col-sm-3 add-margin">
							<form:input id="dateOfMarriage" path="dateOfMarriage" type="text" cssClass="form-control datepicker" data-date-today-highlight="true" data-date-end-date="0d" />
							<form:errors path="dateOfMarriage" cssClass="error-msg"/>
						</div>
					</div>
					</div>
					<div class="row">
						<div class="form-group">
							<label for="field1" class="col-sm-2 control-label"><spring:message
									code="lbl.husband.name" /></label>
	
							<div class="col-sm-3 add-margin">
								<form:input id="husbandName" path="husbandName" type="text" cssClass="form-control is_valid_alphabet" />
								<form:errors path="husbandName" cssClass="error-msg"/>
							</div>
							<label for="field1" class="col-sm-2 control-label"><spring:message
									code="lbl.husband.name" /></label>
	
							<div class="col-sm-3 add-margin">
								<form:input id="wifeName" path="wifeName" type="text" cssClass="form-control is_valid_alphabet" />
								<form:errors path="wifeName" cssClass="error-msg"/>
							</div>
						</div>
					</div>
					<div class="row">

						<div class="form-group">
							<label for="dateOfMarriage" class="col-sm-2 control-label"><spring:message code="lbl.date.of.marriage" /></label>
	
							<div class="col-sm-3 add-margin">
								<form:input id="registrationDate" path="registrationDate" type="text" cssClass="form-control datepicker" data-date-today-highlight="true" data-date-end-date="0d" />
								<form:errors path="registrationDate" cssClass="error-msg"/>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="text-center">						
					<button type="button" id="btnregistrationsearch"
						class="btn btn-primary add-margin">
						<spring:message code="lbl.button.search" />
					</button>
					<a href="javascript:void(0)" class="btn btn-default"
						onclick="self.close()"><spring:message code="lbl.close" /></a>
				</div>
			</div>
		</form:form>
		</div>
</div>
<br /><br />
<div class="row" style="display:none;" id="table_container">   
	<div class="col-md-6 col-xs-6 table-header text-left">The Search result is</div>             
	<div class="col-sm-12">
	    <table class="table table-bordered table-striped datatable" id="registration_table">
	    	<thead>
	    		<tr>
	    			<th>id</th><th>Registration No</th><th>Registration Date</th><th>Marriage Date</th><th>Husband Name</th><th>Wife Name</th>
	    			<th>Is Certificate issued?</th><th>Status</th><th>Marriage Fee</th><th>Is fee collected?</th><th>Action</th>
	    		</tr>
	    	</thead>
	    	<tbody>
	    	</tbody>
		</table>
	</div>
</div>
<%-- <div class="row hidden" id="btn_searchresults">
	<div class="text-center">					
		<button type="submit" class="btn btn-primary disabled" id="btn_viewdetails"><spring:message code="lbl.view.details"/></button>
		<c:if test="${isCollectionOperator}">
			<button type="submit" class="btn btn-primary disabled" id="btn_collectfee"><spring:message code="lbl.collect.fee"/></button>
		</c:if>
	</div>
</div> --%>
						
						
<script	src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script	src="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script	src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>

<script src="<c:url value='/resources/js/app/registrationsearch.js'/> "></script>