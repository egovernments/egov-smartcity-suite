<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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

<div class="row">
	<div class="col-md-12">
	<form:form id="sewerageSearchRequestForm" method="get" class="form-horizontal form-groups-bordered" modelAttribute="sewerage" commandName="">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<strong><spring:message code='title.sewerageTaxSearch' /></strong>
					
				</div>
			</div>
			<div class="panel-body">
				<div class="form-group">
					<label  class="col-md-4 control-label"> 
					<spring:message code="lbl.application.number" /></label>
						<div class="col-md-4 add-margin">
							<input type="text" name="consumerNumber" id="consumerNumber" class="form-control" maxlength="15" data-pattern="alphanumericwithspace" min="10"  />
						</div>
				</div> 
				<div class="form-group">
					<label  class="col-md-4 control-label"> 
					<spring:message code="lbl.shsc.number" /></label>
						<div class="col-md-4 add-margin">
							<input type="text" name="shscNumber" id="shscNumber" class="form-control is_valid_number" maxlength="11"  id="app-appcodo" min="11"  />
						</div>
				</div> 
				<div class="form-group">
					<label  class="col-md-4 control-label"> 
					<spring:message code="lbl.applicantname" /></label>
						<div class="col-md-4 add-margin">
							<input type="text" name="applicantName" id="applicantName" class="form-control patternvalidationclass" data-pattern="alphanumericwithspacespecialcharacters" maxlength="100" id="app-mobno" />
						</div>
				</div>
				<div class="form-group">
					<label  class="col-md-4 control-label"> 
					<spring:message code="lbl.mobileNo" /></label>
						<div class="col-md-4 add-margin">
							<input type="text" name="mobileNumber" id="mobileNumber" class="form-control is_valid_number" maxlength="10" data-inputmask="'mask': '9999999999'" id="app-appcodo" min="10"  />
						</div>
				</div>
				<div class="form-group">
					<label for="field-1" class="col-md-4 control-label"><spring:message code="lbl.revenue.ward" /></label>
						<div class="col-md-4 add-margin">
						 	<select name="revenueWard" id="app-mobno" class="form-control" data-first-option="false">
								<option value="${ward.name}"></option>
								  <c:forEach items="${revenueWards}" var="ward">
                                    <option value="${ward.name}"> ${ward.name} </option>
                                </c:forEach>
					     	</select>
						</div>
				</div>
			
 				<div class="form-group">
						<label  class="col-md-4 control-label"><spring:message code="lbl.doornumber" /></label>
						<div class="col-md-4 add-margin">
							<input  type="text" name="doorNumber" class="form-control "  id="app-appcodo" maxlength="24" />
						</div>
				</div> 
					<div class="form-group">
						<div class="text-center">
							<a href="javascript:void(0);" id="searchSewerageapplication"
								class="btn btn-primary"><spring:message code='lbl.search'/></a>
								
							<button class="btn btn-danger" type="reset"><spring:message code='lbl.reset'/></button>
							<a href="javascript:void(0);" id="closeComplaints"
								class="btn btn-default" onclick="self.close()"><spring:message code='lbl.close' /></a>
						</div>
				</div>
			</div>		
		</div>
		
		<div class="row">
					<div class="col-md-6 col-xs-6 table-header"><spring:message code='lbl.searchresult'/></div>
					<div class="col-md-6 col-xs-6 add-margin text-right">
						<span class="inline-elem"><spring:message code='lbl.search'/></span>
						<span class="inline-elem"><input type="text" id="searchwatertax"  class="form-control input-sm"></span>
					</div>
					<!-- <div class="col-md-12 add-margin text-center error-msg display-hide" id="search-exceed-msg">Search result exceeds the limit(<span id="search-exceed-count">1900</span>). Please, refine your search</div> -->
					<div class="col-md-12 add-margin text-center error-msg display-hide" id="search-exceed-msg">Result contains more than 1000 records, please refine your search criteria.</div>
	<div class="col-md-12" id="searchResultDiv">	
	<table class="table table-bordered datatable dt-responsive"
		id="aplicationSearchResults">
				
	</table>
	</div>
</div>
	</form:form>
	</div>
</div>

<link rel="stylesheet"
	href="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>" 
	type="text/javascript"></script>
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/moment.min.js' context='/egi'/>"></script>
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/datetime-moment.js' context='/egi'/>"></script>


<script src="<cdn:url  value='/resources/js/search/seweragecollectchargessearch.js?rnd=${app_release_no}'/>"
	type="text/javascript"></script>
<script src="<cdn:url  value='/resources/javascript/helper.js?rnd=${app_release_no}'/>"
	type="text/javascript"></script>
