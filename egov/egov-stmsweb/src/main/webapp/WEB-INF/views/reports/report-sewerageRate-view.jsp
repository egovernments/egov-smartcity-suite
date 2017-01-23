<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~      accountability and the service delivery of the government  organizations.
  ~
  ~       Copyright (C) 2016  eGovernments Foundation
  ~
  ~       The updated version of eGov suite of products as by eGovernments Foundation
  ~       is available at http://www.egovernments.org
  ~
  ~       This program is free software: you can redistribute it and/or modify
  ~       it under the terms of the GNU General Public License as published by
  ~       the Free Software Foundation, either version 3 of the License, or
  ~       any later version.
  ~
  ~       This program is distributed in the hope that it will be useful,
  ~       but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~       GNU General Public License for more details.
  ~
  ~       You should have received a copy of the GNU General Public License
  ~       along with this program. If not, see http://www.gnu.org/licenses/ or
  ~       http://www.gnu.org/licenses/gpl.html .
  ~
  ~       In addition to the terms of the GPL license to be adhered to in using this
  ~       program, the following additional terms are to be complied with:
  ~
  ~           1) All versions of this program, verbatim or modified must carry this
  ~              Legal Notice.
  ~
  ~           2) Any misrepresentation of the origin of the material is prohibited. It
  ~              is required that all modified versions of this material be marked in
  ~              reasonable ways as different from the original version.
  ~
  ~           3) This license does not grant any rights to any user of the program
  ~              with regards to rights under trademark law for use of the trade names
  ~              or trademarks of eGovernments Foundation.
  ~
  ~     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<div class="row">
	<div class="col-md-12">
		<form:form method="post" id="sewerageRateDCBReportView" class="form-horizontal form-groups-bordered" modelAttribute="sewerageApplicationDetails">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message  code="title.sewerage.rate.dcbReport"/></strong>
					</div>
				</div>	
				<div class="panel-body">
					<div class="form-group">
						<div class="col-md-3">
							<spring:message code="lbl.application.number"/>
						</div>
						<div class="col-md-3 col-xs-6 view-content">
							<c:out value="${applicationNumber}"/>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-3 col-xs-6">
							<spring:message code="lbl.shsc.number"/>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:choose>
								<c:when test="${not empty sewerageApplicationDetails.connection.shscNumber}">
									<c:out value="${sewerageApplicationDetails.connection.shscNumber}" />
								</c:when>
								<c:otherwise><spring:message code="lb.NA.code"/></c:otherwise>
							</c:choose>
						</div>
						
						<c:forEach items="${propertyOwnerDetails.ownerNames}" var="owner">
								<div class="col-xs-3 add-margin"><spring:message code="lbl.owner.name"/></div>
								<div class="col-xs-3 add-margin view-content" id="applicantname"><c:out value="${owner.ownerName}"/></div>
								<div class="col-xs-3"><spring:message code="lbl.locality" /></div>
								<div class="col-md-3 view-content" id="locality"><c:out value="${propertyOwnerDetails.boundaryDetails.localityName}"/></div>
								<div class="col-xs-3 add-margin"><spring:message code="lbl.revenue.ward"/></div>
								<div class="col-xs-3 add-margin view-content" id="zonewardblock">
									<c:out value="${propertyOwnerDetails.boundaryDetails.wardName}"/>
								</div>
						
						<div class="col-md-3">
							<spring:message code="lbl.propertytype"/>
						</div>
						<div id="propertyType" class="col-md-3 col-xs-6 view-content">
							<c:out value="${propertyOwnerDetails.propertyDetails.propertyType}" /> 
						</div>
						<div class="col-md-3 col-xs-6">
							<spring:message code="lbl.assessmentnumber" />
						</div>
						<div class="col-md-3 col-xs-6 view-content">
							<c:out value="${assessmentnumber}"/>
						</div>
						</c:forEach>
					</div>
					<div class="form-group">
						<div class="col-md-3 col-xs-6">
							<spring:message code="lbl.currentDate.asOn"/>
							<c:out value="${currentDate}" /> 
						</div>
						
							
						
					</div>
					
					
						<table class="table table-bordered datatable dt-responsive multiheadertbl" role="grid" id="sewerageRateReportTable" sortable="sortable">
							<thead>
								<tr role="row">
								    <th colspan="2"></th>
									<th colspan="3"><spring:message code="lbl.demand"/></th>
									<th colspan="3"><spring:message code="lbl.collection"/></th>
									<th colspan="3"><spring:message code="lbl.balance"/></th>
								</tr>
								<tr>
									<th><spring:message code="lbl.serial.number"/></th>
									<th><spring:message code="lbl.installment"/></th>
									<th colspan="1"><spring:message code="lbl.arrear"/></th>
									<th colspan="1"><spring:message code="lbl.demand"/></th>
									<th colspan="1"><spring:message code="lbl.penalty"/></th>
									
									<th colspan="1"><spring:message code="lbl.arrear"/></th>
									<th colspan="1"><spring:message code="lbl.demand"/></th>
									<th colspan="1"><spring:message code="lbl.penalty"/></th>
								
									<th colspan="1"><spring:message code="lbl.arrear"/></th>
									<th colspan="1"><spring:message code="lbl.demand"/></th>
									<th colspan="1"><spring:message code="lbl.penalty"/></th>
									
								</tr>
							</thead>
							<tbody>
							<tr>
							 	<c:set var="totalDemandAmount" value="${0}" />
								<c:set var="totalCollectionAmount" value="${0}"/>
								<c:set var="totalPendingAmount" value="${0}"/>
								
								<c:set var="totalAdvanceAmount" value="${0}"/>
								
								<c:set var="totalArrearAmount" value="${0}" />
								<c:set var="totalCollectedArrearAmount" value="${0}"/>
								<c:set var="totalPendingArrearAmount" value="${0}" />
								
								<c:set var="totalDemandAmount" value="${0}" />
								<c:set var="totalCollectedDemandAmount" value="${0}"/>
								<c:set var="totalPendingDemandAmount" value="${0}" />
								
								<c:set var="totalPenaltymount" value="${0}" />
								<c:set var="totalCollectedPenaltyAmount" value="${0}"/>
								<c:set var="totalPendingPenaltyAmount" value="${0}" />
								
								<c:set var="pendingArrearAmount" value="${0}"/>
								<c:set var="pendingDemandAmount" value="${0}"/>
								<c:set var="pendingPenaltyAmount" value="${0}"/>
								
								<c:set var="advanceAmount" value="${0}" />
									
								<c:set var="totalArrearAmount" value="${0}"/>
								<c:set var="totalDemandAmount" value="${0}"/>
								<c:set var="totalPenaltyAmount" value="${0}"/>
								<c:set var="totalCollectedArrearAmount" value="${0}"/>
								<c:set var="totalCollectedDemandAmount" value="${0}"/>
								<c:set var="totalCollectedPenaltyAmount" value="${0}"/>         
								<c:set var="totalPendingArrearAmount" value="${0}" />
								<c:set var="totalPendingDemandAmount" value="${0}"/>		
								<c:set var="totalPendingPenaltyAmount" value="${0}"/>				
	
								<c:forEach var="dcb" items="${dcbResultList}" varStatus="status" >	
									<tr class="odd" role="row">
										<td align="center" >${status.index+1}</td>
										<td align="center" >${dcb.installmentYearDescription}</td>
										<td align="right">${dcb.arrearAmount}</td>
										<td align="right">${dcb.demandAmount}</td> 
										<td align="right">${dcb.penaltyAmount}</td> 
												
										<td align="right">${dcb.collectedArrearAmount}</td>
										<td align="right">${dcb.collectedDemandAmount}</td> 
										<td align="right">${dcb.collectedPenaltyAmount}</td> 

										<c:set var="pendingArrearAmount" value="${dcb.arrearAmount-dcb.collectedArrearAmount}" />
										<c:set var="pendingDemandAmount" value="${dcb.demandAmount-dcb.collectedDemandAmount}" />
										<c:set var="pendingPenaltyAmount" value="${dcb.penaltyAmount-dcb.collectedPenaltyAmount}" />
												
										<td align="right">${pendingArrearAmount}</td>
										<td align="right">${pendingDemandAmount}</td>
										<td align="right">${pendingPenaltyAmount}</td>

										<c:set var="totalArrearAmount" value="${totalArrearAmount+dcb.arrearAmount}"/>
										<c:set var="totalDemandAmount" value="${totalDemandAmount+dcb.demandAmount }"/>
										<c:set var="totalPenaltyAmount" value="${totalPenltyAmount+dcb.penaltyAmount}"/>
										<c:set var="totalAdvanceAmount" value="${dcb.collectedAdvanceAmount}" />
										<c:set var="totalCollectedArrearAmount" value="${totalCollectedArrearAmount+dcb.collectedArrearAmount}"/>
										<c:set var="totalCollectedDemandAmount" value="${totalCollectedDemandAmount+dcb.collectedDemandAmount}"/>
										<c:set var="totalCollectedPenaltyAmount" value="${totalCollectedPenaltyAmount+dcb.collectedPenaltyAmount}"/>         
										<c:set var="totalPendingArrearAmount" value="${totalArrearAmount-totalCollectedArrearAmount}" />
										<c:set var="totalPendingDemandAmount" value="${totalDemandAmount-totalCollectedDemandAmount}"/>		
										<c:set var="totalPendingPenaltyAmount" value="${totalPenaltyAmount-totalCollectedPenaltyAmount}"/>
									</tr>
								</c:forEach> 
							<tr>
						</tbody>
					<tfoot>
								<td></td>
								<td align="right"><span style="font-weight: bolder;">TOTAL</span></td>
								<td align="right"><span style="font-weight: bolder;">${totalArrearAmount}</span></td>
								<td align="right"><span style="font-weight: bolder;">${totalDemandAmount}</span></td>
								<td align="right"><span style="font-weight: bolder;">${totalPenaltyAmount}</span></td>
								<td align="right"><span style="font-weight: bolder;">${totalCollectedArrearAmount}</span></td>
								<td align="right"><span style="font-weight: bolder;">${totalCollectedDemandAmount}</span></td>
								<td align="right"><span style="font-weight: bolder;">${totalCollectedPenaltyAmount}</span></td>
								<td align="right"><span style="font-weight: bolder;">${totalPendingArrearAmount}</span></td>
								<td align="right"><span style="font-weight: bolder;">${totalPendingDemandAmount}</span></td>
								<td align="right"><span style="font-weight: bolder;">${totalPendingPenaltyAmount}</span></td>
							</tfoot>
						</table>
						
						<table align="center" style="max-width: 12cm" cellpadding="10" class="table table-bordered datatable dt-responsive multiheadertbl" id="totalDCBReportTable" role="grid">
							<thead>
								<tr role="row">
									<th><spring:message code="lbl.total.demand"/></th>
									<th><spring:message code="lbl.total.collection"/></th>
									<th><spring:message code="lbl.advance.amount"></spring:message></th>
									<th><spring:message code="lbl.total.balance"/></th>
								</tr>
							</thead>
							<tbody></tbody>
							<tfoot>
								<c:set var="totalDemandSum" value="${0}" />
								<c:set var="totalCollectionSum" value="${0}"/>
								<c:set var="totalBalanceSum" value="${0}"/>
								
								<c:set var="totalDemandSum" value="${totalArrearAmount+totalDemandAmount+totalPenaltyAmount}"/>
								<c:set var="totalCollectionSum" value="${totalCollectedArrearAmount+totalCollectedDemandAmount+totalCollectedPenaltyAmount}"/>
								<c:set var="totalBalanceSum" value="${totalPendingArrearAmount+totalPendingDemandAmount+totalPendingPenaltyAmount-totalAdvanceAmount}"/>
								
								<td align="center" style="font-weight : bolder;">${totalDemandSum}</td>	
								<td align="center" style="font-weight : bolder;">${totalCollectionSum}</td>
								<td align="center" style="font-weight : bolder;">${totalAdvanceAmount}</td>
								<td align="center" style="font-weight : bolder;">${totalBalanceSum}</td>	
							</tfoot>
						</table>
						
						
						<table align="center" style="max-width: 12cm;" cellpadding="10" class="table table-bordered datatable dt-responsive multiheadertbl" role="grid" id="dcbReportTable" sortable="sortable" >
							<tbody>
								<c:forEach var="dcb" items="${dcbResultList}" varStatus="counter">
									<c:if test="${not empty dcb.receipts}">
										<thead>
												<tr role="row">
													<th><spring:message code="lbl.sewerageRateReport.receipt"/></th>
													<th><spring:message code="lbl.amount"/></th>
													<th><spring:message code="lbl.receipt.date"/></th>
												</tr>
										</thead>
										<c:set var="totalReceiptAmount" value="${0}" />
										
										<c:forEach var="receiptApplMap" items="${dcb.receipts}">
											<c:forEach var="egdmCollectedReceipts" items = "${receiptApplMap.value}">
												<tr>
													<td align="center"><a class="open-popup" href="/collection/citizen/onlineReceipt-viewReceipt.action?receiptNumber=${egdmCollectedReceipts.key}&consumerCode=${receiptApplMap.key}&serviceCode=STAX" >${egdmCollectedReceipts.key}</a></td>
													<c:forEach var="receiptObject" items="${egdmCollectedReceipts.value}">
														<td align="center">${receiptObject.value}</td> 
														<td align="center">${receiptObject.key}</td>
														<c:set var="totalReceiptAmount" value="${totalReceiptAmount+receiptObject.value}" />
													</c:forEach>
												</tr>
											</c:forEach>
										</c:forEach>
										<tfoot>
											<td align="right">TOTAL</td>
											<td align="center">${totalReceiptAmount}</td>
											<td/>
										</tfoot>
									</c:if>
								</c:forEach>
							</tbody>
						</table>
				</div>	
			</div>
			
			<div class="text-center">
				<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
			</div>
		</form:form>
	</div>
</div>

