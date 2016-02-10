<!---------------------------------------------------------------------------------
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
--------------------------------------------------------------------------------->
<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="row">
	<div class="col-md-12">
		<form:form class="form-horizontal form-groups-bordered" action=""
			id="bcDailyCollectionReportForm" modelAttribute="bcDailyCollectionReportResult"
			method="get">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
								
						<strong><spring:message
								code="lbl.bcCollectorDaily.report.title" /> <c:out value="${currentFinancialYear.finYearRange}" /></strong>
									
					</div>
				</div>
			
			</div>
	
		</form:form>
		<div >
				<div class="col-md-12 form-group report-table-container">
				<table class="table table-bordered datatable" id="tblbcDailycollection">
				
				 <thead>

						<!-- <tr>
							<th></th>
							<th></th>
						
						<tr>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th colspan="9" style="font-family: verdana; font-size: 14px;">Present
								Fianacial Year 2015-16</th>

							<th colspan="2" style="font-family: verdana; font-size: 14px;">Previous
								Financial Year 2014-15</th>
							<th colspan="2" style="font-family: verdana; font-size: 14px;">&nbsp;</th>
						</tr>
						<th></th>
						<th></th>
						<th colspan="2" style="font-family: verdana; font-size: 14px;">Commissioner</th>
						<th colspan="3" style="font-family: verdana; font-size: 14px;">Target
							Demand</th>
						<th colspan="2" style="font-family: verdana; font-size: 14px;">Daily
							Target</th>
						<th colspan="4" style="font-family: verdana; font-size: 14px;">Cumulative
							Collection</th>
						<th colspan="2" style="font-family: verdana; font-size: 14px;">Collection</th>
						<th colspan="2" style="font-family: verdana; font-size: 14px;">Comparision
							With Last Year 2014-15</th>
						</tr>
					 -->
					</thead>
				</table>
			</div>
		</div>
				
		<div class="row">
	
				<div class="text-center">
						<button type="button" class="btn btn-default" data-dismiss="modal"
						onclick="window.close();">
						<spring:message code="lbl.close" />
					</button>
				</div>
			</div>
	</div>
</div>
<link rel="stylesheet"
	href="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<script
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<c:url value='/resources/js/app/dailyBillCollectorReport.js'/>"></script>