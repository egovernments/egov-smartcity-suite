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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>


<div class="row" id="page-content">
	<div class="col-md-12">
        <c:if test="${not empty message}">
            <div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
        </c:if>
        
		<form:form method="post" class="form-horizontal form-groups-bordered" id="usageform" modelAttribute="propertyUsage">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.create.uasge" />
					</div>
				</div>
				
				<div class="panel-body custom-form">
					<div class="form-group">
						<label for="usageName" class="col-sm-3 control-label"><spring:message
								code="lbl.usage.nature" /><span class="mandatory"></span></label>

						<div class="col-sm-6 add-margin">
							<form:input id="usageName" path="usageName" type="text" cssClass="form-control is_valid_alphanumericspecialcharacters" autocomplete="off" required="required" />
							<form:errors path="usageName" cssClass="error-msg"/>
						</div>

					</div>
					
					<div class="form-group">
						<label for="usageCode" class="col-sm-3 control-label"><spring:message
								code="lbl.code" /><span class="mandatory"></span></label>

						<div class="col-sm-6 add-margin">
							<form:input id="usageCode" path="usageCode" type="text" style="text-transform:uppercase" cssClass="form-control is_valid_alphanumeric" autocomplete="off" required="required" />
							<form:errors path="usageCode" cssClass="error-msg"/>
						</div>

					</div>

				    <div class="form-group">
						<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.propertytype" /><span class="mandatory"></span>
						</label>
						<div class="col-sm-6 add-margin">							
							<div class="radio-inline">
							  <label><input type="radio" id="optNonResd" name="propertyType" checked="checked" value="NON_RESIDENTIAL">
							  	<spring:message code="lbl.propertytype.nonresd" />
							  </label>
							</div>
							<div class="radio-inline">
							  <label><input type="radio" id="optResd" name="propertyType" value="RESIDENTIAL"><spring:message code="lbl.propertytype.resd" /></label>
							</div>
							<input id="isResidential" name="isResidential" type="hidden" value="false" />
						</div>

					</div>

					<div class="form-group">
						<label for="fromDate" class="col-sm-3 control-label"><spring:message code="lbl.effectivefrom" /><span class="mandatory"></span>
						</label>
						<div class="col-sm-6 add-margin">
							<form:input id="fromDate" path="fromDate" type="text" cssClass="form-control datepicker today" data-date-today-highlight="true" data-date-end-date="0d" />
							<form:errors path="fromDate" cssClass="error-msg"/>
						</div>
					</div>												
				</div>
			</div>
			<div class="row">
				<div class="text-center">
				<c:if test="${roleName.contains('PROPERTY ADMINISTRATOR')}">
					<button type="submit" 
						class="btn btn-primary add-margin">
						<spring:message code="lbl.button.create" />
					</button>	
				</c:if>	
					<button type="button" id="btnusagesearch"
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
	    <table class="table table-bordered datatable" id="usages_table">
		</table>
	</div>
</div>
						
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>

<script src="<cdn:url value='/resources/js/app/usage.js'/> "></script>
<script src="<cdn:url value='/resources/js/app/viewusages.js'/> "></script>