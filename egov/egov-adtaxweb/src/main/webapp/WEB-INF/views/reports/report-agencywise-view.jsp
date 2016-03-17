<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="row">
	<div class="col-md-12">
		<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
		</c:if>
		<form:form id="agencywisehoardingformview" action="" class="form-horizontal form-groups-bordered" modelAttribute="advertisementPermitDetail" 
		commandName="advertisementPermitDetail" >
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<ul class="nav nav-tabs" id="settingstab">
						<li class="active"><a data-toggle="tab"
						href="#hoardingdetails" data-tabidx="0" aria-expanded="false"><spring:message code="lbl.hoardingReport.Result"/></a></li>
					</ul>
				</div>
				<div class="panel-body custom-form">
					<div class="tab-content">
						<div class="tab-pane fade active in" id="hoardingdetails">
							<div class="col-md-3 add-margin text-right"><spring:message code="lbl.hoardingReport.agency"/></div>
							<div class="col-sm-1 add-margin view-content">${agency}</div><br/><br/>
							<div>
								<table class="table table-bordered datatable dt-responsive dataTable no-footer" role="grid">
									<thead>
										<tr role="row">
											<th class="sorting" tabindex="0" rowspan="1" colspan="1" aria-controls="adtax_searchagencywiserecord"><spring:message code="lbl.srl.no"/></th>
											<th><spring:message code="lbl.advertisement.application.no"/></th>
											<th><spring:message code="lbl.advertisement.permission.no"></spring:message></th>
											<th><spring:message code="lbl.hoarding.category"/></th>
											<th><spring:message code="lbl.hoarding.subcategory"/></th>
											<th><spring:message code="lbl.hoardingReport.demandAmount"/></th>
											<th><spring:message code="lbl.hoardingReport.collectedAmt"/></th>
											<th><spring:message code="lbl.agencywise.pendingAmount"/></th>
											<th><spring:message code="lbl.agencywise.penaltyAmount"/></th>
										</tr>
									</thead>
									<tbody>
										<c:set var="totalDemandAmount" value="${0}" />
										<c:set var="totalCollectionAmount" value="${0}"/>
										<c:set var="totalPendingAmount" value="${0}"/>
										<c:set var="totalPenaltyAmount" value="${0}"/>
										<c:forEach var="dcb" items="${dcbResult}" varStatus="status" >	
											<tr class="odd" role="row">
												<td>${status.index+1}</td>
												<td>${dcb.applicationNumber}</td>
												<td>${dcb.permissionNumber}</td>
												<td>${dcb.category}</td>
												<td>${dcb.subcategory}</td>
												<td>${dcb.demandAmount}</td>
												<td>${dcb.collectedAmount}</td>
												<td>${dcb.pendingAmount}</td>
												<td>${dcb.penaltyAmount}</td>
												<c:set var="totalDemandAmount" value="${totalDemandAmount + dcb.demandAmount}" />
												<c:set var="totalCollectionAmount" value="${totalCollectionAmount+dcb.collectedAmount}" />
												<c:set var="totalPendingAmount" value="${totalPendingAmount+dcb.pendingAmount}" />
												<c:set var="totalPenaltyAmount" value="${totalPenaltyAmount+dcb.penaltyAmount}" />
											</tr>
										</c:forEach> 
										<tfoot>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td><span style="font-weight: bolder;">TOTAL</span></td>
											<td><span style="font-weight: bolder;">${totalDemandAmount}</span></td>
											<td><span style="font-weight: bolder;">${totalCollectionAmount}</span></td>
											<td><span style="font-weight: bolder;">${totalPendingAmount}</span></td>
											<td><span style="font-weight: bolder;">${totalPenaltyAmount}</span></td>
										</tfoot>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="text-center">
		    	<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
			</div>
		</form:form>
	</div>
</div>
