<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<div class="row">
	<div class="col-md-12">
		<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
		</c:if>
		<form:form id="hoardingformview" action="" class="form-horizontal form-groups-bordered" modelAttribute="hoarding" 
		commandName="hoarding" >
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading custom_form_panel_heading">
					<div class="panel-title">
						<spring:message code="lbl.advertisement.details"/>
					</div>
				</div>	
				<div class="panel-body custom-form">
					<div class="tab-content">
						<div class="tab-pane fade active in" id="hoardingdetails">
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
									<spring:message code="lbl.hoarding.no"/>
								</label>
								<div class="col-sm-3 add-margin view-content">
									${hoarding.advertisementNumber}
								</div>
							</div>
							<div class="panel-body custom-form">
								<table  cellpadding="10" class="table table-bordered datatable dt-responsive multiheadertbl" role="grid" id="dcbReportTable" sortable="sortable">
									<thead>
										<tr>
											<th colspan="1"></th>
											<th colspan="4"><spring:message code="lbl.hoardingReport.demandAmount"></spring:message></th>
											<th colspan="4"><spring:message code="lbl.hoardingReport.collectedAmt"></spring:message></th>
										</tr>
										<tr role="row">
											<th><spring:message code="lbl.hoardingReport.year"></spring:message></th>
											<th><spring:message code="lbl.arrears"></spring:message></th>
											<th><spring:message code="lbl.hoardingReport.taxReason"></spring:message></th>
											<th><spring:message code="lbl.penalty"/></th>
											<th><spring:message code="lbl.additionalTax"/></th>
											<th><spring:message code="lbl.arrears"/></th>
											<th><spring:message code="lbl.hoardingReport.taxReason"/></th>
											<th><spring:message code="lbl.penalty"/></th>
											<th><spring:message code="lbl.additionalTax"/></th>
										</tr>
									</thead>
									<tbody>
										<c:set var="totalArrearAmount" value="${0}" />
										<c:set var="totalDemandAmount" value="${0}" />
										<c:set var="totalPenaltyAmount" value="${0}" />
										<c:set var="totalAdditionalTaxAmount" value="${0}" />
										<c:set var="totalCollectedArrearAmount" value="${0}" />
										<c:set var="totalCollectedDemandAmount" value="${0}" />
										<c:set var="totalCollectedPenaltyAmount" value="${0}" />
										<c:set var="totalCollectedAdditionalTaxAmount" value="${0}" />
										<c:forEach var="dcb" items="${dcbResult}" varStatus="status" >	
											<tr class="odd" role="row">
												<td align="right">${dcb.installmentYearDescription}</td>
												<td align="right">${dcb.arrearAmount}</td>
												<td align="right">${dcb.demandAmount}</td>
												<td align="right">${dcb.penaltyAmount}</td>
												<td align="right">${dcb.additionalTaxAmount}</td>												
												<td align="right">${dcb.collectedArrearAmount}</td>
												<td align="right">${dcb.collectedDemandAmount}</td>
												<td align="right">${dcb.collectedPenaltyAmount}</td>
												<td align="right">${dcb.collectedAdditionalTaxAmount}</td>
												<c:set var="totalDemandAmount" value="${totalDemandAmount+dcb.demandAmount}"/>
												<c:set var="totalArrearAmount" value="${totalArrearAmount+dcb.arrearAmount}" />
												<c:set var="totalPenaltyAmount" value="${totalPenaltyAmount+dcb.penaltyAmount}" />
												<c:set var="totalAdditionalTaxAmount" value="${totalAdditionalTaxAmount+dcb.additionalTaxAmount}" />
												<c:set var="totalCollectedDemandAmount" value="${totalCollectedDemandAmount+dcb.collectedDemandAmount}"/>
												<c:set var="totalCollectedArrearAmount" value="${totalCollectedArrearAmount+dcb.collectedArrearAmount }" />
												<c:set var="totalCollectedPenaltyAmount" value="${totalCollectedPenaltyAmount+dcb.collectedPenaltyAmount }" />
												<c:set var="totalCollectedAdditionalTaxAmount" value="${totalCollectedAdditionalTaxAmount+dcb.collectedAdditionalTaxAmount}" />
												<c:set var="totalBalanceAmount" value="${totalDemandAmount-totalCollectedAmount}" />
											</tr>
										</c:forEach>
									</tbody>
									<tfoot>
										<td align="right" style="font-size: small;font-weight: bold;"><spring:message code="lbl.dcbreport.total"></spring:message></td> 
										<td align="right" style="font-size: small;font-weight: bold;">${totalArrearAmount}</td>
										<td align="right" style="font-size: small;font-weight: bold;">${totalDemandAmount}</td>
										<td align="right" style="font-size: small;font-weight: bold;">${totalPenaltyAmount}</td>
											<td align="right" style="font-size: small;font-weight: bold;">${totalAdditionalTaxAmount}</td>
									
										<td align="right" style="font-size: small;font-weight: bold;">${totalCollectedArrearAmount}</td>
										<td align="right" style="font-size: small;font-weight: bold;">${totalCollectedDemandAmount}</td>
										<td align="right" style="font-size: small;font-weight: bold;">${totalCollectedPenaltyAmount}</td>
											<td align="right" style="font-size: small;font-weight: bold;">${totalCollectedAdditionalTaxAmount}</td>
									
									</tfoot>
								</table>
								<br/>
								<table align="center"  style="max-width: 12cm;" cellpadding="10" class="table table-bordered datatable dt-responsive multiheadertbl" role="grid" id="dcbReportTable" sortable="sortable">
									<thead>
										<tr role="row">
										<th> <spring:message code="lbl.dcbreport.totalDemand" ></spring:message></th>
										<th> <spring:message code="lbl.dcbreport.totalCollection" ></spring:message></th>
										<th> <spring:message code="lbl.dcbreport.totalBalance" ></spring:message></th>
										</tr>
									</thead>
									<tbody>
										<c:set var="totalDemandSum" value="${0}" />
										<c:set var="totalCollectionSum" value="${0}" />
										<c:set var="totalBalanceSum" value="${0}" />
										<c:set var="totalDemandSum" value="${totalArrearAmount+totalDemandAmount+totalPenaltyAmount+totalAdditionalTaxAmount}" />
										<c:set var="totalCollectionSum" value="${totalCollectedArrearAmount+totalCollectedDemandAmount+totalCollectedPenaltyAmount+totalCollectedAdditionalTaxAmount}" />
										<c:set var="totalBalanceSum" value="${totalDemandSum-totalCollectionSum}" />
										<td align="right" style="font-size: small;font-weight: bold;">${totalDemandSum}</td>
										<td align="right" style="font-size: small;font-weight: bold;">${totalCollectionSum}</td>
										<td align="right" style="font-size: small;font-weight: bold;">${totalBalanceSum}</td>
									</tbody>
								</table>
								<br/>
								<table align="center" style="max-width: 12cm;"  cellpadding="10" class="table table-bordered datatable dt-responsive multiheadertbl" role="grid" id="dcbReportTable" sortable="sortable">
									<c:set var="booleanVariable" value="true" />
									<c:forEach var="dcb" items="${dcbResult}" varStatus="counter">
										<c:forEach var="receipt" items="${dcb.collectReceiptMap}" varStatus="counter">
											<c:if test="${not empty dcb.collectReceiptMap}">
												<c:if test="${booleanVariable eq true}">
													<thead>
														<tr role="row">
															<th> <spring:message code="lbl.hoardingReport.receipts" ></spring:message></th>
															<th> <spring:message code="lbl.receipt.date" ></spring:message></th>
														</tr>
													</thead>
													<c:set var="booleanVariable" value="false" />
												</c:if>
											</c:if>
										</c:forEach>
									</c:forEach>
									<tbody>
										<c:forEach var="dcb" items="${dcbResult}" varStatus="counter" >
											<c:forEach var="receipt" items="${dcb.collectReceiptMap}" varStatus="counter" >
												<tr>
													<td align="center">${receipt.value}</td>
													<td align="center">${receipt.key}</td>
												</tr>
											</c:forEach>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="text-center">
		    		<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
				</div>
			</div>
		</form:form>
	</div>
</div>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/moment.min.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/datetime-moment.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/app/js/searchadtax.js?rnd=${app_release_no}'/>"></script>