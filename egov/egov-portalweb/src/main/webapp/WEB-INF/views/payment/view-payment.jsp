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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<strong><spring:message code="lbl.viewpayments" /> </strong>
				</div>
			</div>
			<c:if test="${not empty errorMsg}">
				<div class="panel-body"
					style="text-align: center; color: red; font-size: 20px">
					<div class="mandatory" style="text-align: center;">
						<spring:message code="${errorMsg}" />
					</div>
				</div>
			</c:if>
			<div class="panel-body">
				<form:form class="form-horizontal form-groups-bordered"
					id="viewPaymentsSearchForm" modelAttribute="pastPaymentRequest"
					action="">
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label"> <spring:message
								code="lbl.Service" /><span class="mandatory"> <font
								color="red"> * </font></span></label>
						<div class="col-sm-3 add-margin">
							<form:select name="serviceName" path="" data-first-option="false"
								id="serviceName" cssClass="form-control" required="required">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${serviceList}" />
							</form:select>
						</div>
						<label for="field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.fromdate" /></label>
						<div class="col-sm-3 add-margin">
							<form:input path="fromDate" name="fromDate" id="fromDate"
								cssClass="form-control datepicker" value="${fromDate}"
								cssErrorClass="form-control error" />
						</div>
					</div>
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.toDate" /><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="toDate" name="toDate" id="toDate"
								cssClass="form-control datepicker" value="${toDate}"
								cssErrorClass="form-control error" />
						</div>
					</div>
					<input type="hidden" id="serviceNameHidden"
						value="${pastPaymentRequest.serviceName}">
					<input type="hidden" id="fromDateHidden"
						value="${pastPaymentRequest.fromDate}">
					<input type="hidden" id="toDateHidden"
						value="${pastPaymentRequest.toDate}">

					<div class="form-group">
						<div class="text-center">
							<button type="button" class="btn btn-primary"
								id="searchPastPayment">
								<spring:message code='lbl.search' />
							</button>
							<a href="javascript:void(0);" class="btn btn-default"
								onclick="window.close()"><spring:message code='lbl.close' /></a>
						</div>
					</div>

				</form:form>
			</div>
		</div>
	</div>
</div>
<div class="row display-hide report-section">
	<div class="col-md-12 table-header text-left">
		<spring:message code="title.searchresult" />
	</div>
	<div class="col-md-12 form-group report-table-container">
		<c:choose>
			<c:when test="${not empty receipts}">
				<table class="table table-bordered" width="100%">
					<tr>
						<th>S.No</th>
						<th><spring:message code="lbl.consumerno" /></th>
						<th><spring:message code="lbl.paymentdate" /></th>
						<th><spring:message code="lbl.totalamount" /></th>
						<th><spring:message code="lbl.receiptno" /></th>

					</tr>

					<c:forEach var="receipt" items="${receipts}" varStatus="record">
						<tr>
							<td align="left"><c:out value="${record.index+1}" />.</td>
							<td align="left"><c:out value="${receipt.consumerCode}"
									default="N/A" /></td>
							<td align="left"><fmt:formatDate pattern="dd/MM/yyyy"
									value="${receipt.receiptdate}" />
							<td align="left"><fmt:formatNumber type="number"
									maxFractionDigits="0" value="${receipt.totalAmount}" /></td>
							<td align="left"><c:out value="${receipt.receiptnumber}"
									default="N/A" /></td>
						</tr>
					</c:forEach>
				</table>
			</c:when>
			<c:otherwise>
				<div class="panel-body">No receipts found.</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>">
<script
	src="<cdn:url  value='/resources/js/searchPastPayment.js?rnd=${app_release_no}'/>"
	type="text/javascript"></script>
