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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row" id="page-content">
	<div class="col-md-12">
		<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert">
				<spring:message code="${message}" />
			</div>
		</c:if>

		<form:form class="form-horizontal form-groups-bordered"	id="onlinepaymentform" modelAttribute="onlinePaymentResult" method="post">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title"  style="text-align:center;">
						<spring:message code="title.online.payment.report" />
					</div>
				</div>
				<div class="panel-body custom-form">
				
				<div class="form-group">
						<label for="field-1" class="col-sm-2 control-label"><spring:message code="lbl.onlinepayment.districtname" /></label>
						<div class="col-sm-3 add-margin">
							<form:select name="districtname" id="districtname" path=""
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${districtname}"/>
							</form:select>
						</div>
						
						
						<label for="field-1" class="col-sm-2 control-label"><spring:message code="lbl.onlinepayment.ulbname" /></label>
						<div class="col-sm-3 add-margin">
							<form:select name="ulbname" id="ulbname" path=""
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
							</form:select>
						</div>
					</div>
					
					<div class="form-group">
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.fromDate" /></label>
						<div class="col-sm-3 add-margin">
						<fmt:formatDate value="${fromdate}" var="formatfromdate" pattern="dd/MM/yyyy"/>
							<form:input path="" name="fromdate" id="fromdate"
								cssClass="form-control datepicker" value="${formatfromdate}"
								cssErrorClass="form-control error" />
						</div>
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.toDate" /></label>
						<div class="col-sm-3 add-margin">
						<fmt:formatDate value="${todate}" var="formattodate" pattern="dd/MM/yyyy"/>
							<form:input path="" name="todate" id="todate"
								cssClass="form-control datepicker" value="${formattodate}"
								cssErrorClass="form-control error" />
						</div>
					</div>
					<div class="form-group">
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.onlinepayment.transid" /></label>
						<div class="col-sm-3 add-margin">
							<form:input path="" name="transid" id="transid"
								class="form-control patternvalidation" data-pattern="number" value="${transid}"
								cssErrorClass="form-control error" />
						</div>
					</div>
				</div>
				
				
				<div class="row">
	       			<div class="text-center">
	       				<button type="button" class="btn btn-primary" id="onlinePaymentReportSearch"><spring:message code="lbl.search"/></button>
	          		    <a href="javascript:void(0)" class="btn btn-default" data-dismiss="modal" onclick="self.close()"><spring:message code="lbl.close"/></a>
	          		</div>
        		</div>
        		
        		</div>
		</form:form>
		
		<div class="text-bold" id="resultinfo"></div>
		 <br/>
		
		<table class="table table-bordered datatable dt-responsive table-hover" id="onlinePaymentReport-table">
		</table>
		
	</div>
</div>

<link href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" rel="stylesheet" type="text/css" />
<script	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>" type="text/javascript"></script>
<link rel="stylesheet"	href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>" type="text/javascript"></script>
 <script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
 type="text/javascript"></script>
 <script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"
 type="text/javascript"></script>
 <script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"
 type="text/javascript"></script>
 <script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
 type="text/javascript"></script>
<script type="text/javascript" src="<cdn:url value='/resources/js/onlinePaymentReport.js?rnd=${app_release_no}'/>"></script>
