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
<div id="baseSORTable" class="panel panel-primary" data-collapsed="0">
	<input type="hidden" id="errorquantitieszero" value="<spring:message code='error.quantity.zero' />">
	<input type="hidden" id="errorcumulativequantity" value="<spring:message code='error.approved.quantity.cumulative' />">
	<input type="hidden" id="isMeasurementsExist" value="${isMeasurementsExist }">
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
	<div class="panel-body" id="sorHeaderTable">
		<table class="table table-bordered" style="overflow: auto;" id="tblsor">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.sor.category" /></th>
					<th><spring:message code="lbl.sorcode" /></th>
					<th><spring:message code="lbl.description" /></th>
					<th><spring:message code="lbl.uom" /></th>
					<th><spring:message code="lbl.rate" /></th>
					<th><spring:message code="lbl.workorderquantity" /></th>
					<th><spring:message code="lbl.current.entry" /><span class="mandatory"></span></th>
					<th><spring:message code="lbl.amount" /></th>
				</tr>
			</thead>
			<tbody id="sorTable">
				<c:forEach items="${contractorMB.contractorMBDetails }" var="details" varStatus="item">
					<tr id="sorRow" class="sorRow" align="center">
						<td>
							<span class="spansorslno">${item.index + 1 }</span>
							<form:input type="hidden" path="contractorMBDetails[${item.index }].id" id="contractorMBDetailsId_${item.index }" class="contractorMBDetailsId" value="${details.id }" />
							<form:input type="hidden" path="contractorMBDetails[${item.index }].workOrderActivity.id" id="workOrderActivity_0" class="workOrderActivity" value="${details.workOrderActivity.id }" />
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
						<td>
							<span class="workOrderQuantity_${item.index }">${details.workOrderActivity.approvedQuantity }</span>
						</td>
						<td>
							<form:input type="hidden" path="contractorMBDetails[${item.index }].rate" value="${details.workOrderActivity.activity.rate }" id="unitRate_${item.index }" class="form-control table-input text-right"/>
							
							<div class="input-group" style="width:150px">
								<c:choose>
	            	        		<c:when test="${!details.workOrderActivity.workOrderMeasurementSheets.isEmpty() }">
	                	    			<form:input path="contractorMBDetails[${item.index }].quantity" readonly="true" value="${details.quantity }" id="quantity_${item.index }" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0" required="required" class="form-control input-sm text-right quantity" maxlength="64" onblur="calculateActivityAmounts(this);" onkeyup="validateQuantityInput(this);"/>
	                    			</c:when>
	                    			<c:otherwise>
	                    				<form:input path="contractorMBDetails[${item.index }].quantity" value="${details.quantity }" id="quantity_${item.index }" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0" required="required" class="form-control input-sm text-right quantity" maxlength="64" onblur="calculateActivityAmounts(this);" onkeyup="validateQuantityInput(this);"/>
		                    		</c:otherwise>
		                    	</c:choose>
								<c:choose>
									<c:when test="${!details.workOrderActivity.workOrderMeasurementSheets.isEmpty() }">
										<span class="input-group-addon openmbsheet" name="contractorMBDetails[${item.index }].msadd" id="contractorMBDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"><i  class="fa fa-plus-circle" aria-hidden="true"></i></span>
									</c:when>
									<c:otherwise>
										<span style="display: none;" class="input-group-addon openmbsheet" name="contractorMBDetails[${item.index }].msadd" id="contractorMBDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"><i  class="fa fa-plus-circle" aria-hidden="true"></i></span>
									</c:otherwise>
								</c:choose>
							</div>
						</td>
						<td hidden="true">
							<c:set var="net" value="0" />
							<c:set var="total" value="0" />
                            <input class="classmspresent" type="hidden" disabled="disabled" name="contractorMBDetails[${item.index }].mspresent" id="contractorMBDetails[${item.index }].mspresent" data-idx="0"/>
                            <input class="classmsopen" type="hidden" disabled="disabled" name="contractorMBDetails[${item.index }].msopen" id="contractorMBDetails[${item.index }].msopen" data-idx="0"/>
							<span  class="contractorMBDetails[${item.index }].mstd" id="contractorMBDetails[${item.index }].mstd" data-idx="0">
								<%@ include file="contractormb-measurementsheet-edit.jsp"%>
							</span>
						</td>
						<td align="right">
							<form:input type="hidden" path="contractorMBDetails[${item.index }].amount" value="${details.amount }" id="hiddenAmount_${item.index }" class="form-control table-input text-right"/>
							<c:choose>
								<c:when test="${details.workOrderActivity.activity.revisionType == 'NON_TENDERED_ITEM' || details.workOrderActivity.activity.revisionType == 'LUMP_SUM_ITEM' }">
									<span class="nontendered" id="amount_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.amount }</fmt:formatNumber></span>
								</c:when>
								<c:otherwise>
									<span class="tendered" id="amount_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.amount }</fmt:formatNumber></span>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="8" class="text-right"><spring:message code="lbl.total" /></td>
					<td class="text-right"> <span id="mbTotal">0.00</span> </td>
				</tr>
			</tfoot>
		</table>
		<div class="add-margin error-msg" style="width:50%;float:right"><font size="2"><spring:message code="msg.contractormb.foot.note" /></font></div>
	</div>
</div>
