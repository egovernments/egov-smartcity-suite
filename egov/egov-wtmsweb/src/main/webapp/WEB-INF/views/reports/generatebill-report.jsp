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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row">
	<div class="col-md-12">

		<div class="panel panel-primary" data-collapsed="0">


			<div class="panel-body">

				<form:form  class="form-horizontal form-groups-bordered"
					id="waterSearchRequestForm" modelAttribute="generateConnectionBill" action="">

					
					<div class="form-group">
					<label for="field-1" class="col-md-4 control-label"><spring:message
										code="lbl.zone" /></label>
						<div class="col-md-4 add-margin">
						 <select name="zone" id="zone" class="form-control"
								data-first-option="false">
								<option value="${zone.name}"></option>
								  <c:forEach items="${zones}" var="zone">
                                    <option value="${zone.name}"> ${zone.name} </option>
                                </c:forEach>
					     </select>
						</div>
						</div>
						
							<div class="form-group">
					<label for="field-1" class="col-md-4 control-label"><spring:message
										code="lbl.revenue.ward" /></label>
						<div class="col-md-4 add-margin">
						 <select name="revenueWard" id="revenueWard" class="form-control"
								data-first-option="false">
								<option value="${ward.name}"></option>
								  <c:forEach items="${revenueWards}" var="ward">
                                    <option value="${ward.name}"> ${ward.name} </option>
                                </c:forEach>
					     </select>
						</div>
						</div>
						
						<div class="form-group">
						<label for="field-1" class="col-md-4 control-label"><spring:message
										code="lbl.propertytype" /></label>
						<div class="col-md-4 add-margin">
						 <form:select path="propertyType" name="propertyType" data-first-option="false" id="propertyType"
			cssClass="form-control" >
			<form:option value="">
			</form:option>
			<form:options items="${propertyTypes}" itemValue="name"
				itemLabel="name" />
		</form:select>		
						</div>
						
						</div>
						
						<div class="form-group">
						<label for="field-1" class="col-md-4 control-label"><spring:message
										code="lbl.applicationtype" /></label>
						<div class="col-md-4 add-margin">
						 <form:select path ="applicationType" name="applicationType" data-first-option="false" id="applicationType"
			cssClass="form-control"  >
			<form:option value="">
			</form:option>
			<form:options items="${applicationTypes}" itemValue="name"
				itemLabel="name" />
		</form:select>		
						</div>
						
						</div>
						
						<div class="form-group">
						<label for="field-1" class="col-md-4 control-label"><spring:message
										code="lbl.connectiontype" /></label>
						<div class="col-md-4 add-margin">
		<form:select path="connectionType" data-first-option="false" cssClass="form-control" id="connectionType"> 
			<form:option value="">
				
			</form:option>
			<form:options items="${connectionTypes}" />
		</form:select>
		<form:errors path="connectionType" cssClass="add-margin error-msg" />
						</div>
						</div>
						
										<div class="form-group">
				<label for="field-1" class="col-md-4 control-label"><spring:message code="lbl.hscno"/></label>
										  <div class="col-md-4 add-margin">
							<input type="text" name="consumerCode" class="form-control patternvalidation" data-pattern="number"  maxlength="10" id="consumerCode"/>
						</div>
						</div>

					
						<div class="form-group">
					<label for="field-1" class="col-md-4 control-label"> <spring:message
										code="lbl.assesmentnumber" /></label>
						<div class="col-md-4 add-margin">
							<input type="text" name="assessmentNumber" class="form-control patternvalidation" data-pattern="number" maxlength="10" id="assessmentNumber" />
						</div>
						</div>
						
						
						
						<div class="form-group">
						<label for="field-1" class="col-md-4 control-label"><spring:message
										code="lbl.houseno" /></label>
										<div class="col-md-4 add-margin">
							<input type="text" name="houseNumber" class="form-control patternvalidation" data-pattern="alphanumericwithspecialcharacters" maxlength="32" id="houseNumber"
								/>
						</div>
				     </div>
				     
				     <div class="form-group text-center" >
				     
						<form:button type="button" class="btn btn-primary" id="searchid"><spring:message code="lbl.search"/></form:button>
						<form:button type="button" class="btn btn-primary" id="mergeid"><spring:message code="lbl.mergedownload"/></form:button>
						<%-- <button type="submit" class="btn btn-primary" id="mergeid" onclick="merge()"><spring:message code="lbl.mergedownload"/></button> --%>
						<form:button type="button" class="btn btn-primary" id="zipid"><spring:message code="lbl.zip"/></form:button>
						<form:button type="reset" class="btn btn-primary"  ><spring:message code="lbl.reset"/></form:button>
						<a onclick="self.close()" class="btn btn-default" href="javascript:void(0)"><spring:message code="lbl.close"/></a>
					</div>

				</form:form>
					</div> 


			</div>
			
			<div class="row hide" id="warning-msg">
				   <div class="col-xs-12 col-md-6 col-md-offset-3">
				     <div class="alert alert-warning">Search result exceeds the limit 2000. Please, refine your search</div>
				   </div>
				</div>

			<table class="table table-bordered table-hover multiheadertbl"
			id="generateBill-table" width="200%">
					<tbody>
				   
					
					</tbody>
				</table>

		</div>

	</div>




<link rel="stylesheet"
	href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
	type="text/javascript"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/moment.min.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/datetime-moment.js' context='/egi'/>"></script>


<script src="<cdn:url value='/resources/js/app/generate-bill.js?rnd=${app_release_no}'/>"
	type="text/javascript"></script>
	
	

