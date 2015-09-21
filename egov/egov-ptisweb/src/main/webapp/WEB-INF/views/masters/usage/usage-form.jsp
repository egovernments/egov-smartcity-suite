<!--
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
	The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->

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
		<div class="panel" data-collapsed="0">
            <div class="panel-body">
                <c:if test="${not empty message}">
                    <div class="alert alert-success" role="alert">${message}</div>
                </c:if>
				<form:form  method="post" class="form-horizontal form-groups-bordered" id="usageform" modelAttribute="propertyUsage">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title">
								<spring:message code="lbl.property.usageMaster" />
							</div>
						</div>
						<div class="form-group">
							<label for="field-1" class="col-sm-3 control-label"><spring:message
									code="lbl.usage.nature" /><span class="mandatory"></span></label>
	
							<div class="col-sm-6 add-margin">
								<form:input path="usageName" type="text" cssClass="form-control is_valid_alphabet" autocomplete="off" required="required" />
								<form:errors path="usageName" cssClass="error-msg"/>
							</div>
	
						</div>

						<div class="form-group">
							<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.propertytype" /><span class="mandatory"></span>
							</label>
							<div class="col-sm-6 add-margin">
								<label class="radio-inline">
									<form:radiobutton path="resdOrNonResd" id="propertyType" value="NON_RESIDENTIAL" checked="true" />
									<spring:message code="lbl.propertytype.nonresd" />
								</label>
								<label class="radio-inline">
									<form:radiobutton path="resdOrNonResd" id="propertyType" value="RESIDENTIAL" />
									<spring:message code="lbl.propertytype.resd" />
								</label>
							</div>
	
						</div>
	
						<div class="form-group">
							<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.effectivefrom" /><span class="mandatory"></span>
							</label>
							<div class="col-sm-6 add-margin">
								<form:input path="fromDate" type="text" cssClass="form-control datepicker today" data-date-today-highlight="true" data-date-end-date="0d" />
								<form:errors path="usageName" cssClass="error-msg"/>
							</div>
						</div>					
						<div class="form-group">
							<div class="text-center">
								<button type="submit" id="usageAdd"
									class="btn btn-primary add-margin">
									<spring:message code="lbl.button.add" />
								</button>							
								<button type="submit" id="btnusagesearch"
									class="btn btn-primary add-margin">
									<spring:message code="lbl.button.search" />
								</button>
								<a href="javascript:void(0)" class="btn btn-default"
									onclick="self.close()"><spring:message code="lbl.close" /></a>
							</div>
						</div>
					</div>
				</form:form>
 			</div>
		</div>
	</div>
</div>

<div id="usageRow" class="row" style="display:  none;">
	<div class="col-md-6 col-xs-6 table-header">The Search result is</div>
 	<div class="col-md-6 col-xs-6 add-margin text-right">
		<span class="inline-elem">Search</span> <span class="inline-elem"><input
			type="text" id="search" class="form-control input-sm"></span>
	</div>
	<div class="col-sm-12">
		<table class="table table-bordered table-striped datatable"
			id="usages_table">
		</table>
	</div>
</div>


<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/> "></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/> "></script>
<script src="<c:url value='/resources/global/js/egov/custom.js' context='/egi'/> "></script>

<script src="<c:url value='/resources/js/app/viewusages.js'/> "></script>
