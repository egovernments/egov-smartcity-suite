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
  <c:set var="total" value="0" />
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.work.order.boq" />
		</div>
		<div align="right" class="openCloseAll">
		<input type="button" value="Close All Measurements" class="btn btn-sm btn-secondary"
			onclick="closeAllmsheet()" /> <input type="button" class="btn btn-sm btn-secondary"
			value="Open All Measurements" onclick="openAllmsheet()" />
		</div>
	</div>
	<div class="panel-body">
		<table class="table table-bordered" style="overflow: auto;">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.sor.category" /></th>
					<th><spring:message code="lbl.sorcode" /></th>
					<th><spring:message code="lbl.description" /></th>
					<th><spring:message code="lbl.uom" /></th>
					<th><spring:message code="lbl.rate" /></th>
					<th><spring:message code="lbl.workorderquantity" /></th>
					<th><spring:message code="lbl.current.entry" /></th>
					<th><spring:message code="lbl.amount" /></th>
				</tr>
			</thead>
			<tbody id="sorTable">
				<c:forEach items="${contractorMB.workOrderBOQs }" var="details" varStatus="item">
					<tr align="center">
						<td>
							<span class="spansorslno">${item.index + 1 }</span>
						</td>
						<td>
							<span class="category_${item.index }">${details.workOrderActivity.activity.schedule.scheduleCategory.code }</span>
						</td>
						<td>
							<span class="code_${item.index }">${details.workOrderActivity.activity.schedule.code }</span>
						</td>
						<td align="left">
							<c:choose>
								<c:when test="${details.workOrderActivity.activity.schedule != null }">
									<span class="summary_${item.index }">${details.workOrderActivity.activity.schedule.getSummary() }</span>&nbsp
									<span class="hintanchor description_${item.index }"/><a href="#" class="hintanchor" title="${details.workOrderActivity.activity.schedule.description }"><i class="fa fa-question-circle" aria-hidden="true"></i></a></span>
								</c:when>
								<c:otherwise>
									<span class="nonSorSummary_${item.index }">${details.workOrderActivity.activity.nonSor.description }</span>&nbsp
									<span class="hintanchor nonSorDescription_${item.index }"/><a href="#" class="hintanchor" title="${details.workOrderActivity.activity.nonSor.description }"><i class="fa fa-question-circle" aria-hidden="true"></i></a></span>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:choose>
								<c:when test="${details.workOrderActivity.activity.schedule != null }">
									<span class="uom_${item.index }">${details.workOrderActivity.activity.schedule.uom.uom }</span>
								</c:when>
								<c:otherwise>
									<span class="nonSorUom_${item.index }">${details.workOrderActivity.activity.nonSor.uom.uom }</span>
								</c:otherwise>
							</c:choose>
						</td>
						<td align="right">
							<span class="rate_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.workOrderActivity.activity.estimateRate }</fmt:formatNumber></span>
						</td>
						<td align="right">
							<span class="workOrderQuantity_${item.index }">${details.workOrderActivity.approvedQuantity }</span>
						</td>
						<td align="right">
							<span class="spansorslno">${details.quantity }</span>
							<c:choose>
								<c:when test="${!details.measurementSheets.isEmpty() }">
									<button class="btn btn-default openmbsheet" name="contractorMBDetails[${item.index }].msadd" id="contractorMBDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true"></i></button>
								</c:when>
								<c:otherwise>
									<button style="display: none;" class="btn btn-default openmbsheet" name="contractorMBDetails[${item.index }].msadd" id="contractorMBDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true"></i></button>
								</c:otherwise>
							</c:choose>
						</td>
						<td hidden="true">
                            <input class="classmspresent" type="hidden" disabled="disabled" name="contractorMBDetails[${item.index }].mspresent" id="contractorMBDetails[${item.index }].mspresent" data-idx="0"/>
                            <input class="classmsopen" type="hidden" disabled="disabled" name="contractorMBDetails[${item.index }].msopen" id="contractorMBDetails[${item.index }].msopen" data-idx="0"/>
							<span  class="contractorMBDetails[${item.index }].mstd" id="contractorMBDetails[${item.index }].mstd" data-idx="0">
								<%@ include file="contractormb-measurementsheet-view.jsp"%>
							</span>
						</td>
						<td align="right">
							<c:choose>
								<c:when test="${details.workOrderActivity.activity.revisionType == 'NON_TENDERED_ITEM' || details.workOrderActivity.activity.revisionType == 'LUMP_SUM_ITEM' }">
									<span class="nontendered" id="amount_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.amount }</fmt:formatNumber></span>
									<c:set var="total" value="${total + details.amount }" />
								</c:when>
								<c:otherwise>
									<span class="tendered" id="amount_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.amount }</fmt:formatNumber></span>
									<c:set var="total" value="${total + (details.amount + details.amount * contractorMB.workOrderEstimate.workOrder.tenderFinalizedPercentage / 100) }" />
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="8" class="text-right"><spring:message code="lbl.total" /> (<spring:message code="lbl.mbamount.note" />)</td>
					<td class="text-right"> <span id="mbTotal">
						<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" groupingUsed="false" value="${total }" />
					</span> </td>
				</tr>
			</tfoot>
		</table>
		<div class="add-margin error-msg" style="width:50%;float:right"><font size="2"><spring:message code="msg.contractormb.foot.note" /></font></div>
	</div>
</div>
