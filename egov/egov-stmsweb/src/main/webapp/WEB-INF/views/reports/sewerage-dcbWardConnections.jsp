<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2016>  eGovernments Foundation
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<div class="row">
	<div class="col-md-12">
		<form:form class="form-horizontal form-groups-bordered" id="dcbDrillDownWardwiseReportForm" modelAttribute="dcbReportWardwiseResult" commandName="dcbReportWardwiseResult" method="get">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message code="title.sewerage.dcbDrillDownWardwiseReport"></spring:message></strong>
					</div>
				</div>
				<div class="panel-body">
					<div class="col-sm-3 add-margin text-right"><spring:message code="lbl.revenue.ward"/></div>	
					<div class="col-sm-3 add-margin view-content" id="revenueWard">
	                	 ${revenueWard} 
	                </div>
					<br/><br/>
					<br/><br/>
					<div id="dcbconnections-report-header" class="col-md-12 table header text-left">
						<fmt:formatDate value="${currentDate}" var="currDate" pattern="dd-MM-yyyy"/>
						<spring:message  code = "lbl.sewerage.dcb.connection.report"/>
						<c:out value="${currDate}"/> 
					</div>
					<table class="table table-bordered datatable dt-responsive multiheadertbl" role="grid" id="sewerageWardDCBTable" >
							<thead>
								<tr>
									<th colspan="1"></th>
									<th colspan="1"></th>
									<th colspan="3"><spring:message code="lbl.demand"/></th>
									<th colspan="3"><spring:message code="lbl.collection"/></th>
									<th colspan="3"><spring:message code="lbl.balance"/></th>
									<th colspan="1"/>
								</tr>
								<tr role="row">
									<th><spring:message code="lbl.shsc.number"/></th>
									<th><spring:message code="lbl.owner.name"/></th>
									<th><spring:message code="lbl.arrear"/></th>
									<th><spring:message code="lbl.current"/></th>
									<th><spring:message code="lbl.total"/></th>
									<th><spring:message code="lbl.arrear"/></th>
									<th><spring:message code="lbl.current"/></th>
									<th><spring:message code="lbl.total"/></th>
									<th><spring:message code="lbl.arrear"/></th>
									<th><spring:message code="lbl.current"/></th>
									<th><spring:message code="lbl.total"/></th>
									<th><spring:message code="lbl.advance"/></th>
								</tr>
							</thead>
							<tbody>
								<c:set var="totalDemand" value="${0}" />
								<c:set var="totalCollection" value="${0}" />
								<c:set var="totalBalance" value="${0}"/>
								<c:set var="totalArrearDemandAmount" value="${0}"/>
								<c:set var="totalCurrentDemandAmount" value="${0}" />
								<c:set var="totalDemandAmount" value="${0}" />
								<c:set var="totalArrearCollectedAmount" value="${0}"/>
								<c:set var="totalCurrentCollectedAmount" value="${0}" />
								<c:set var="totalCollectedAmount" value="${0}" />
								<c:set var="advanceAmount" value="${0}"/>
								<c:set var="totalArrearBalanceAmount" value="${0}" />
								<c:set var="totalCurrentBalanceAmount" value="${0}" />
								<c:set var="totalBalanceAmount" value="${0}" />
								<c:set var="totalAdvanceAmount" value="${0}" />
								<c:forEach var="dcb" items="${dcbResultList}" varStatus="status">
									<tr role="row">
										<td align="center">${dcb.shscnumber}</td>
										<td align="center">${dcb.ownerName}</td>
										<td align="right">${dcb.arr_demand}</td>
										<td align="right">${dcb.curr_demand}</td>
										<td align="right">${dcb.arr_demand+dcb.curr_demand}</td>
										<td align="right">${dcb.arr_collection}</td>
										<td align="right">${dcb.curr_collection}</td>
										<td align="right">${dcb.arr_collection+dcb.curr_collection}</td>
										<td align="right">${dcb.arr_balance}</td>
										<td align="right">${dcb.curr_balance}</td>
										<td align="right">${dcb.arr_balance+dcb.curr_balance}</td>
										<td align="right">${dcb.advanceAmount}</td>
									</tr>
									<c:set var="totalArrearDemandAmount" value="${totalArrearDemandAmount+dcb.arr_demand}"/>
									<c:set var="totalCurrentDemandAmount" value="${totalCurrentDemandAmount+dcb.curr_demand}" />
									<c:set var="totalDemandAmount" value="${totalDemandAmount+dcb.arr_demand+dcb.curr_demand}" />
									<c:set var="totalArrearCollectedAmount" value="${totalArrearCollectedAmount+dcb.arr_collection}"/>
									<c:set var="totalCurrentCollectedAmount" value="${totalCurrentCollectedAmount+dcb.curr_collection}" />
									<c:set var="totalCollectedAmount" value="${totalCollectedAmount+dcb.arr_collection+dcb.curr_collection}" />
									<c:set var="totalArrearBalanceAmount" value="${totalArrearBalanceAmount+dcb.arr_balance}" />
									<c:set var="totalCurrentBalanceAmount" value="${totalCurrentBalanceAmount+dcb.curr_balance}" />
									<c:set var="totalBalanceAmount" value="${totalBalanceAmount+dcb.arr_balance+dcb.curr_balance}" />
									<c:set var="totalAdvanceAmount" value="${totalAdvanceAmount+dcb.advanceAmount}" />
								</c:forEach>
							</tbody>
							<tfoot>
								<tr>
									<td></td>
									<td align="right" style="font-size : small; font-weight: bold"><spring:message code="lbl.grandtotal"/></td>
									<td align="right" style="font-size: small;font-weight: bold;">${totalArrearDemandAmount}</td>
									<td align="right" style="font-size: small;font-weight: bold;">${totalCurrentDemandAmount}</td>
									<td align="right" style="font-size: small;font-weight: bold;">${totalDemandAmount}</td>
									<td align="right" style="font-size: small;font-weight: bold;">${totalArrearCollectedAmount}</td>
									<td align="right" style="font-size: small;font-weight: bold;">${totalCurrentCollectedAmount}</td>
									<td align="right" style="font-size: small;font-weight: bold;">${totalCollectedAmount}</td>
									<td align="right" style="font-size: small;font-weight: bold;">${totalArrearBalanceAmount}</td>
									<td align="right" style="font-size: small;font-weight: bold;">${totalCurrentBalanceAmount}</td>
									<td align="right" style="font-size: small;font-weight: bold;">${totalBalanceAmount}</td>
									<td align="right" style="font-size: small;font-weight: bold;">${totalAdvanceAmount}</td>
								</tr>
							</tfoot>
					</table>	
				</div>
				<div class="row">
					<div class="text-center">
						<a href="javascript:void(0);" onclick="self.close();" class="btn btn-primary"><spring:message code="lbl.close"/></a>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</div>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript" src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/moment.min.js' context='/egi'/>"></script>
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/datetime-moment.js' context='/egi'/>"></script>
<script  src="<cdn:url  value='/resources/js/reports/sewerageDCBWardwise.js?rnd=${app_release_no}' /> " ></script>