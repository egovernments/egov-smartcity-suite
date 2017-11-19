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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<div class="row">
	<div class="col-md-12">
		<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
		</c:if>
		<form:form id="agencywisehoardingformview" action="" class="form-horizontal form-groups-bordered" modelAttribute="advertisementPermitDetail" 
		commandName="advertisementPermitDetail" >
				<div class="panel-heading">
					<div class="panel-title" style="color: orange;">
						<strong><spring:message code="lbl.hoardingReport.Result" /></strong>
					</div>
				</div>
				<div class="panel-body custom-form">
						
							<div class="col-md-12 add-margin text-left"><spring:message code="lbl.agency.name"/> <b class="view-content">&nbsp;&nbsp;${agency}</b></div><br/><br/>
								<table  cellpadding="10" class="table table-bordered datatable dt-responsive multiheadertbl" role="grid" id="agencyReportTable" sortable="sortable">
									<thead>
										<tr role="row">
											<th><spring:message code="lbl.srl.no"/></th>
											<th><spring:message code="lbl.advertisement.application.no"/></th>
											<th><spring:message code="lbl.advertisement.permission.no"></spring:message></th>
											<th><spring:message code="lbl.owner.Details"></spring:message></th>
											<th><spring:message code="lbl.hoarding.category"/></th>
											<th><spring:message code="lbl.hoarding.subcategory"/></th>
											<th><spring:message code="lbl.hoardingReport.demandAmount"/></th>
											<th><spring:message code="lbl.hoardingReport.collectedAmt"/></th>
											<th><spring:message code="lbl.agencywise.pendingAmount"/></th>
											<th><spring:message code="lbl.agencywise.penaltyAmount"/></th>
											<th><spring:message code="lbl.additionalTax"/></th>
										</tr>
									</thead>
									<tbody>
										<c:set var="totalDemandAmount" value="${0}" />
										<c:set var="totalCollectionAmount" value="${0}"/>
										<c:set var="totalPendingAmount" value="${0}"/>
										<c:set var="totalPenaltyAmount" value="${0}"/>
										<c:set var="totalAdditionalTaxAmount" value="${0}"/>
										<c:forEach var="dcb" items="${dcbResult}" varStatus="status" >	
											<tr class="odd" role="row">
												<td align="right" >${status.index+1}</td>
												<td align="right">${dcb.applicationNumber}</td>
												<td align="right">${dcb.permissionNumber}</td>
												<td align="right">${dcb.ownerDetail}</td>
												<td align="right">${dcb.category}</td>
												<td align="right">${dcb.subcategory}</td>
												<td align="right">${dcb.demandAmount}</td>
												<td align="right">${dcb.collectedAmount}</td>
												<td align="right">${dcb.pendingAmount}</td>
												<td align="right">${dcb.penaltyAmount}</td>
												<td align="right">${dcb.additionalTaxAmount}</td>
												<c:set var="totalDemandAmount" value="${totalDemandAmount + dcb.demandAmount}" />
												<c:set var="totalCollectionAmount" value="${totalCollectionAmount+dcb.collectedAmount}" />
												<c:set var="totalPendingAmount" value="${totalPendingAmount+dcb.pendingAmount}" />
												<c:set var="totalPenaltyAmount" value="${totalPenaltyAmount+dcb.penaltyAmount}" />
												<c:set var="totalAdditionalTaxAmount" value="${totalAdditionalTaxAmount+dcb.additionalTaxAmount}" />
												
											</tr>
										</c:forEach> 
										<tfoot>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td align="right"><span style="font-weight: bolder;">TOTAL</span></td>
											<td align="right"><span style="font-weight: bolder;">${totalDemandAmount}</span></td>
											<td align="right"><span style="font-weight: bolder;">${totalCollectionAmount}</span></td>
											<td align="right"><span style="font-weight: bolder;">${totalPendingAmount}</span></td>
											<td align="right"><span style="font-weight: bolder;">${totalPenaltyAmount}</span></td>
											<td align="right"><span style="font-weight: bolder;">${totalAdditionalTaxAmount}</span></td>
										</tfoot>
									</tbody>
								</table>
							</div>
			<div class="text-center">
		    	<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
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
<script src="<cdn:url value='/resources/app/js/agencywiseReport.js?rnd=${app_release_no}'/>"></script>