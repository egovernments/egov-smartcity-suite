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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
	
<div class="row" id="page-content">
	<div class="col-md-12">
		<div class="panel" data-collapsed="0">
			<div class="panel-body">
				 <c:if test="${not empty message}">
                    <div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
                </c:if>
		<form:form  method="post" action="/mrs/report/registrationstatus" class="form-horizontal form-groups-bordered" id="form-registrationstatus" modelAttribute="searchModel">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
							<strong><spring:message code="lbl.hdr.registrationstatus"/></strong>
					</div>
				</div> 
				
				<div class="panel-body custom-form">				
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.fromDate"/><span class="mandatory"></span>
						</label>
						<div class="col-sm-6">
							<form:input path="fromDate" id="txt-fromdate" type="text" class="form-control low-width datepicker today" data-date-end-date="0d" data-date-today-highlight="true" placeholder="" autocomplete="off" required="required"/>
            				<form:errors path="fromDate" cssClass="add-margin error-msg"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.toDate"/>
						</label>
						<div class="col-sm-6">
							<form:input path="toDate" id="txt-todate" type="text" class="form-control low-width datepicker today" data-date-end-date="0d" data-date-today-highlight="true" placeholder="" autocomplete="off" required="required"/>
            				<form:errors path="toDate" cssClass="add-margin error-msg"/>
						</div>
					</div>
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.registration.status" /><span class="mandatory"></span>
						</label>
						<div class="col-sm-6 add-margin">							
							<div class="checkbox-inline">
							  <label><input type="checkbox" id="cb-registrationapproved" name="registrationApproved" checked="checked">
							  	<spring:message code="lbl.status.approved" />
							  </label>
							</div>
							<div class="checkbox-inline">
							  <label><input type="checkbox" id="cb-registrationrejected" name="registrationRejected">
							  	<spring:message code="lbl.status.rejected" />
							  </label>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="text-center">					
					<button type="button" class="btn btn-primary" id="btn_registrationstatus_search"><spring:message code="lbl.search"/></button>
			        <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
				</div>
			</div>
		</form:form>
		</div>
        </div>
    </div>
</div>

<br /><br />
<div class="row" style="display:none;" id="regstatustable_container">   
	<div class="col-md-6 col-xs-6 table-header text-left">The Search result is</div>             
	<div class="col-sm-12">
	    <table class="table table-bordered table-striped datatable" id="registrationstatus_table">
	    	<thead>
	    		<tr>
	    			<th>Sr.No</th><th>Husband Name</th><th>Wife Name</th><th>Registration Date</th><th>Marriage Date</th>
	    			<th>Application Type</th><th>Marriage Fee</th><th>Status</th><th>Remarks</th>
	    		</tr>
	    	</thead>
	    	<tbody>
	    	</tbody>
		</table>
	</div>
</div>

<script	src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script	src="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script	src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>

<script src="<c:url value='/resources/js/app/registrationstatus.js'/> "></script>