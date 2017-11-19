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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<div class="row">
<div class="col-md-12">

<form:form method="post" modelAttribute="donationChargesDCBReportSearch"
	commandName="donationChargesDCBReportSearch"
	id="donationChargeDCBReportForm"
	cssClass="form-horizontal form-groups-bordered">


	
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title"><spring:message
								code="title.search.donation" /></div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.dailyReport.fromDate" /></label>

						<div class="col-sm-3 add-margin">
							<form:input path="fromDate" id="fromDate"
								cssClass="form-control datepicker"
								cssErrorClass="form-control error" />
							<form:errors path="fromDate" cssClass="add-margin error-msg" />
						</div>

						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.dailyReport.toDate"></spring:message></label>

						<div class="col-sm-3 add-margin">
							<form:input path="toDate" id="toDate"
								cssClass="form-control datepicker"
								cssErrorClass="form-control error" />
							<form:errors path="toDate" cssClass="add-margin error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"> <spring:message
								code="lbl.fromAmount" />
						</label>
						<div class="col-sm-3 add-margin">
							<form:input path="fromAmount"
								id="fromAmount" cssClass="form-control patternvalidation"
								data-pattern="decimalvalue" cssErrorClass="form-control error" />
							<form:errors path="fromAmount" cssClass="add-margin error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"> <spring:message
								code="lbl.toAmount" />
						</label>
						<div class="col-sm-3 add-margin">
							<form:input path="toAmount" id="toAmount"
								cssClass="form-control patternvalidation"
								data-pattern="decimalvalue" cssErrorClass="form-control error" />
							<form:errors path="toAmount" cssClass="add-margin error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"> <spring:message
								code="lbl.pendingforpaymentonly"></spring:message>
						</label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="pendingForPaymentOnly"
								id="pendingForPaymentOnly" value="active" />
							<form:errors path="pendingForPaymentOnly"
								cssClass="add-margin error-msg" />
						</div>
					</div>
					<div class="form-group">
						<div class="text-center">
							<button type="button" class="btn btn-primary" id="searchDonation">
								<spring:message code="lbl.submit" />
							</button>
							<a href="javascript:void(0)" class="btn btn-default"
								data-dismiss="modal" onclick="self.close()"><spring:message
									code="lbl.close" /></a>
						</div>
					</div>
				</div>
			</div>
			<div class="row display-hide report-section">
	<div class="col-md-12 table-header text-left">Donation Charges
		DCB Search Result</div>
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id="resultTable">
			<thead>
				<tr>
					<th><spring:message code="lbl1.consumer.number" /></th>
					<th><spring:message code="lbl.assesmentnumber" /></th>
					<th><spring:message code="lbl.nameofowner" /></th>
					<th><spring:message code="lbl.mobileNo" /></th>
					<th><spring:message code="lbl.propertyaddress" /></th>
					<th><spring:message code="lbl.total.donationamount" /></th>
					<th><spring:message code="lbl.paid.donationamount" /></th>
					<th><spring:message code="lbl.balance.donationamount" /></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
			<tfoot id="report-footer">
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td>Total</td>
					<td></td>
					<td></td>
					<td></td>
					
				</tr>
			</tfoot>
		</table>
	</div>
</div>
</form:form>
	</div>
	</div>
	

<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/js/app/helper.js?rnd=${app_release_no}'/>"></script>
<script
	src="<cdn:url value='/resources/js/app/viewDonationDCBReport.js?rnd=${app_release_no}'/>"></script>
	