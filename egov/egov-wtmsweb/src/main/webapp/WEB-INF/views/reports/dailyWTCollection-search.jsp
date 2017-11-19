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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<style>
body
{
  font-family:regular !important;
  font-size:14px;
}
</style>
<div class="row" id="page-content">
	<div class="col-md-12">
		<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert">
				<spring:message code="${message}" />
			</div>
		</c:if>
		<form:form class="form-horizontal form-groups-bordered"
			id="dailyCollectionform" modelAttribute="dailyWTCollectionReport"
			method="post">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.dailyCollection.report" />
					</div>
				</div>
				<div class="panel-body custom-form">
					<div class="form-group">
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.dailyReport.fromDate" /><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
						<fmt:formatDate value="${currentDate}" var="fromDate" pattern="dd/MM/yyyy"/>
							<form:input path="" name="fromDate" id="fromDate"
								cssClass="form-control datepicker" value="${fromDate}"
								cssErrorClass="form-control error" required="required"/>
						</div>

						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.dailyReport.toDate" /><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
						<fmt:formatDate value="${currentDate}" var="toDate" pattern="dd/MM/yyyy"/>
							<form:input path="" name="toDate" id="toDate"
								cssClass="form-control datepicker" value="${toDate}"
								cssErrorClass="form-control error" required="required"/>
						</div>

					</div>
				</div>

				<div class="panel-body custom-form">
					<div class="form-group">
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.collectioMode" /></label>
						<div class="col-sm-3 add-margin">
							<form:select id="collectionMode" name="collectionMode" path=""
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${collectionMode}" />
							</form:select>
						</div>
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.collectionOperator" /></label>
						<div class="col-sm-3 add-margin">
							<form:select name="collectionOperator" id="collectionOperator" path=""
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${operators}" id="collectionOperator" name="collectionOperator" itemValue="name" itemLabel="name" />
							</form:select>
						</div>

					</div>
				</div>
				<div class="panel-body custom-form">
					<div class="form-group">
						<label for="field-1" class="col-sm-2 control-label"><spring:message
								code="lbl.status" /></label>
						<div class="col-sm-3 add-margin">
							<form:select id="status" name="status" path=""
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${status}"  itemValue="description" itemLabel="description" />
							</form:select>
						</div>
						
						<label for="field-1" class="col-sm-2 control-label"><spring:message code="lbl.ward" /></label>
						<div class="col-sm-3 add-margin">
							<form:select name="revenueWard" id="revenueward" path=""
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${wards}" id="revenueWard" name="revenueWard" itemValue="name" itemLabel="name" />
							</form:select>
						</div>
				</div>
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<button type="button" class="btn btn-primary"
						id="dailyCollectionReportSearch">
						<spring:message code="lbl.submit" />
					</button>
					<a href="javascript:void(0)" class="btn btn-default"
						data-dismiss="modal" onclick="self.close()"><spring:message
							code="lbl.close" /></a>
				</div>
			</div>
		</form:form>
		<div id="dailyCollectionReport-header" class="col-md-12 table-header text-left">
			<fmt:formatDate value="${currentDate}" var="currDate"
				pattern="dd-MM-yyyy" />
			<spring:message code="lbl.dailyCollection.report.details" />:
			<c:out value="${currDate}"></c:out>
		   </div>
		<table class="table table-bordered table-hover multiheadertbl"
			id="dailyCollReport-table" width="200%">
			<tbody>
		   
			<tfoot id="report-footer">
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td>Total</td>  
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
				</tfoot> 
			</tbody>
		</table>
	</div>
</div>

<link rel="stylesheet" href="<cdn:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.columnFilter.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>" ></script>
<script	src="<cdn:url value='/resources/js/app/dailyWTCollectionReport.js?rnd=${app_release_no}'/>"></script>
