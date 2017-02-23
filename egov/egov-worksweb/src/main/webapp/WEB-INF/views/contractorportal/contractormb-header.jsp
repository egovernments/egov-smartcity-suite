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

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<input type="hidden" id="workOrderEstimateId" value="${contractorMB.workOrderEstimate.id }" />
<input type="hidden" id="lineEstimateDetailsId" value="${contractorMB.workOrderEstimate.estimate.lineEstimateDetails.id }" />
<input type="hidden" id="abstractEstimateId" value="${contractorMB.workOrderEstimate.estimate.id }" />
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title" style="text-align: left;">
			<spring:message code="lbl.workdetails" />
		</div>
	</div>
	<div class="panel-body">
		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.mb.nameofwork" />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<c:out default="N/A" value="${contractorMB.workOrderEstimate.estimate.name}" />
			</div>
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.workordernumber" />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<input name="workOrder.id" type="hidden" id="workOrderId" value="${contractorMB.workOrderEstimate.workOrder.id }" />
				 <span name="workOrderNumber" id="workOrderNumber">${contractorMB.workOrderEstimate.workOrder.workOrderNumber }</span>
			</div>
		</div>


		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.workorderdate" />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<span name=workOrderDate id="workOrderDate">
				<fmt:formatDate value="${contractorMB.workOrderEstimate.workOrder.workOrderDate }" pattern="dd/MM/yyyy" /></span>
			</div>
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.tenderfinalizedpercentage" />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<span name="tenderFinalizedPercentage" id="tenderFinalizedPercentage">${contractorMB.workOrderEstimate.workOrder.tenderFinalizedPercentage }</span>
			</div>
		</div>

		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.workorderamount" />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<span name=workOrderAmount id="workOrderAmount">
				<fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
							minFractionDigits="2" value="${contractorMB.workOrderEstimate.workOrder.workOrderAmount }" /></span>
			</div>
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.total.bills.paid" />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<span name="totalBillsPaid" id="totalBillsPaid">${totalBillsPaidSoFar }</span>
			</div>
		</div>

		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.status" />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<span name="mileStoneStatus" id="mileStoneStatus">${mileStoneStatus }</span>
			</div>
			<c:if test="${mode != 'view' }">
				<div class="row add-border">
					<div class="col-md-2 add-margin"><a href="javascript:void(0);" id="contractormbs"><spring:message code="lbl.old.contractormbs" /></a></div>
				</div>
			</c:if>
		</div>
		<div class="row add-border">
			<div class="col-xs-3 add-margin">
			</div>
			<div class="col-xs-3 add-margin view-content">
			</div>
			<c:if test="${mode != 'view' }">
				<div class="row add-border">
					<div class="col-md-3 add-margin"><a href="javascript:void(0);" id="uploadEstimatePhotograph"><spring:message code="lbl.uploadestimatephotograph" /></a></div>
					<div class="col-md-3 add-margin"><a href="javascript:void(0);" id="viewEstimatePhotograph"><spring:message code="lbl.viewestimatephotograph" /></a></div>
				</div>
			</c:if>
		</div>
	</div>
</div>