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

<div class="panel panel-primary" data-collapsed="0"
			style="text-align: left">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="title.mb.details" />
				</div>
			</div>
		
<!-- <div class="panel panel-primary" data-collapsed="0"> -->
	<!-- <div class="panel-body"> -->
	<c:choose>
		<c:when test="${mBHeader.mbDetails.size() != 0}">
		<c:forEach items="${mBHeader.getMbDetails()}" var="mbsordetails" varStatus="item">
		<c:choose>
		<c:when test="${mbsordetails.workOrderActivity.activity.schedule != null }">
		<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-body"> 
		<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.sor" />
				</div>
			</div>
		<table class="table table-bordered" id="tblsor">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.sor.category" /></th>
					<th><spring:message code="lbl.sorcode" /></th>
					<th><spring:message code="lbl.description.item" /></th>
					<th><spring:message code="lbl.uom" /></th>
					<th><spring:message code="lbl.approved.quantity" /></th>
					<th><spring:message code="lbl.approved.rate" /></th>
					<th><spring:message code="lbl.cumulative.previous.entry" /></th>
					<th><spring:message code="lbl.current.entry" /></th>
					<th><spring:message	code="lbl.cumulative.quantity.current.entry" /></th>
					<th><spring:message code="lbl.amount.current.entry" /></th>
					<th><spring:message code="lbl.cumulative.amount.current.entry" /></th>
					<th><spring:message code="lbl.approved.amount" /></th>
					<th><spring:message code="lbl.remarks" /></th>
				</tr>
			</thead>
			<tbody>
						<tr >
							<c:set var="slNo" value="${0}" scope="session" />
							<c:set var="slNo" value="${item.index + 1}" />  
							<td><span class="spansno"><c:out value="${item.index + 1}" /></span></td>
							<td><c:out value="${mbsordetails.workOrderActivity.activity.schedule.scheduleCategory.code}"></c:out></td>
							<td><c:out value="${mbsordetails.workOrderActivity.activity.schedule.code}"></c:out></td>
							<td><c:out value="${mbsordetails.workOrderActivity.activity.schedule.description}"></c:out></td>
							<td><c:out value="${mbsordetails.workOrderActivity.activity.uom.uom}"></c:out></td>
							<td><c:out value="${mbsordetails.workOrderActivity.approvedQuantity}"/></td>
							<td><c:out value="${mbsordetails.workOrderActivity.activity.estimateRate}"/></td>
							<td><c:out value="${mbsordetails.workOrderActivity.prevCumlvQuantity}"/></td>
							<td><c:out value="${mbsordetails.quantity}"/></td> 
							<td><c:out value="${mbsordetails.workOrderActivity.prevCumlvQuantity + mbsordetails.quantity}"/></td> 
							<td></td>
							<td></td>
							<td><c:out value="${mbsordetails.workOrderActivity.approvedAmount}"/></td>
							<td><c:out value="${mbsordetails.remarks}"/></td>
						</tr>
			</tbody>
			<tfoot>
			<c:set var="total" value="${0}" scope="session" />
			</tfoot>
		</table>
		</div>
		</div>
		</c:when>
		<c:otherwise>
		<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-body"> 
		<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.nonsor" />
				</div>
			</div>
		<table class="table table-bordered" id="tblnonsor">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.description.item" /></th>
					<th><spring:message code="lbl.uom" /></th>
					<th><spring:message code="lbl.approved.quantity" /></th>
					<th><spring:message code="lbl.approved.rate" /></th>
					<th><spring:message code="lbl.cumulative.previous.entry" /></th>
					<th><spring:message code="lbl.current.entry" /></th>
					<th><spring:message	code="lbl.cumulative.quantity.current.entry" /></th>
					<th><spring:message code="lbl.amount.current.entry" /></th>
					<th><spring:message code="lbl.cumulative.amount.current.entry" /></th>
					<th><spring:message code="lbl.approved.amount" /></th>
					<th><spring:message code="lbl.remarks" /></th>
				</tr>
			</thead>
			<tbody>
				<tr >
				<c:set var="slNo" value="${0}" scope="session" />
				<c:set var="slNo" value="${item.index + 1}" />  
				<td><span class="spansno"><c:out value="${item.index + 1}" /></span></td>
				<td><c:out value="${mbsordetails.workOrderActivity.activity.schedule.description}"></c:out></td>
				<td><c:out value="${mbsordetails.workOrderActivity.activity.uom.uom}"></c:out></td>
				<td><c:out value="${mbsordetails.workOrderActivity.approvedQuantity}"/></td>
				<td><c:out value="${mbsordetails.workOrderActivity.activity.estimateRate}"/></td>
				<td><c:out value="${mbsordetails.workOrderActivity.prevCumlvQuantity}"/></td>
				<td><c:out value="${mbsordetails.quantity}"/></td> 
				<td><c:out value="${mbsordetails.workOrderActivity.prevCumlvQuantity + mbsordetails.quantity}"/></td> 
				<td></td>
				<td></td>
				<td><c:out value="${mbsordetails.workOrderActivity.approvedAmount}"/></td>
				<td><c:out value="${mbsordetails.remarks}"/></td>
				</tr>
				</tbody>
				</table>
				</div>
				</div>
		</c:otherwise>
		</c:choose>
		</c:forEach>
		</c:when>
		</c:choose>
	<!-- </div> -->
<!-- </div> -->
</div>
