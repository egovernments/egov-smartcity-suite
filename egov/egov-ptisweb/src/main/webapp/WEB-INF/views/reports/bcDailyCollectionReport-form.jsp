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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<div class="row">
	<div class="col-md-12">
		<form:form class="form-horizontal form-groups-bordered" action=""
			id="bcDailyCollectionReportForm" modelAttribute="bcDailyCollectionReportResult" commandName="bcDailyCollectionReportResult"
			method="get">
		
		<div class="panel-body">
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"> Region </label>
					<div class="col-sm-3 add-margin">
						<form:select id="region" name="region" path="region"
							cssClass="form-control" cssErrorClass="form-control error">
							<form:option value="">
								<spring:message code="lbl.default.all" />
							</form:option>
							<form:options items="${regions}" itemValue="name" itemLabel="name" />
						</form:select>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"> District
						</label>
						<div class="col-sm-2 add-margin">
							<form:select path="district" id="districtId"
								data-first-option="false" cssClass="form-control">
								<form:option value="">
									<spring:message code="lbl.default.all" />
								</form:option>
								<form:options items="${districts}" itemValue="name"
									itemLabel="name" />
							</form:select>
						</div>
					</div>

				</div>
			</div>	
			<div class="panel-body">
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"> Ulb Name</label>
					<div class="col-sm-3 add-margin">
						<form:select id="cityId" name="cityId" path="city"
							cssClass="form-control" cssErrorClass="form-control error">
							<form:option value="">
								<spring:message code="lbl.default.all" />
							</form:option>
							<form:options items="${cities}" itemValue="name" itemLabel="name" />
						</form:select>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"> Category of ownership 
						</label>
						<div class="col-sm-2 add-margin">
							<form:select path="type" 
								data-first-option="false" cssClass="form-control">
								<form:option value="ALL">
									<spring:message code="lbl.default.all" />
								</form:option>
								<form:option value="PRIVATE">
									PRIVATE
								</form:option>
									<form:option value="COURTCASE">
									COURTCASE
								</form:option>
							</form:select>
						</div>
					</div>

				</div>
			</div>			
		<div class="row">
				<div class="text-center">
					<button type="button" class="btn btn-primary"
						id="bcdailyCollectionReportSearch">
						<spring:message code="lbl.search" />
					</button>
					<a href="javascript:void(0)" class="btn btn-default"
						data-dismiss="modal" onclick="self.close()"><spring:message
							code="lbl.close" /></a>
				</div>
			</div>
		
		
		
		</form:form>
		<div >
	<div id="tblbcDailycollectionheader">
		<div class="col-md-8 table-header text-left"><spring:message code="lbl.bcCollectorDaily.report.details" /> ${bcDailyCollectionReportResult.generatedDate}   </div>
		<div class="col-md-4 text-right">Date : <%= new java.util.Date() %></div>
		<div class="col-md-4 text-right">Amount in Lakhs</div>
	</div>
				<div class="col-md-12 form-group report-table-container">
				<table class="table table-bordered datatable multiheadertbl" id="tblbcDailycollection">
				
				<thead>
					<tr>
					<th colspan="6"></th>
					<th colspan="9">Present Fianacial Year <c:out value="${currentFinancialYear.finYearRange}" /> </th>
					<th colspan="2">Previous Financial Year <c:out value="${previousFinancialYear.finYearRange}" /></th>
					<th colspan="2"></th>
					
				</tr>
				<tr>
					<th colspan="4">City Detail</th>
					<th colspan="2">Bill Collector</th>
					<th colspan="3">Target Demand</th>
					<th colspan="2">Daily Target</th>
					<th colspan="4">Cumulative Collection</th>
					<th colspan="2">Collection</th>
					<th colspan="2">Comparision With Last Year <c:out value="${previousFinancialYear.finYearRange}" /></th>
				</tr>

				<tr>
					<th>Sl.no</th>
					<th>District</th>
					<th>Ulb Name</th>
					<th>Ulb Code</th>
					<th>Name</th>
					<th>Mobile Number</th>
					<th>Arrears</th>
					<th>Current</th>
					<th>Total</th>
					<th>Day Target</th>
					<th>Day Collection</th>
					<th>Arrears</th>
					<th>Current</th>
					<th>Total</th>
					<th>%</th>
					<th>Today</th>
						<th>Cummulative</th>
					<th>Increase of collection</th>
					<th>% of growth</th>
							

				</tr>
			</thead>
				<tfoot id="report-footer">
				<tr>
					<td colspan="6" align="center">Total</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</tfoot>
				</table>
			</div>
		</div>
				
		
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
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/dailyBillCollectorReport.js'/>"></script>