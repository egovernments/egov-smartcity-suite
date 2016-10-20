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
<div id="baseNonSORTable" class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="title.nonsor" />
		</div>
	</div>
	<div class="panel-body" id="sorHeaderTable">
		<table class="table table-bordered" style="overflow: auto;" id="tblNonSor">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.description.item" /></th>
					<th><spring:message code="lbl.uom" /></th>
					<th><spring:message code="lbl.approved.quantity" /></th>
					<th><spring:message code="lbl.approved.rate" /></th>
					<th><spring:message code="lbl.cumulative.previous.entry" /></th>
					<th><spring:message code="lbl.current.entry" /></th>
					<th><spring:message code="lbl.cumulative.quantity.current.entry" /></th>
					<th><spring:message code="lbl.amount.current.entry" /></th>
					<th><spring:message code="lbl.cumulative.amount.current.entry" /></th>
					<th><spring:message code="lbl.approved.amount" /></th>
					<th><spring:message code="lbl.remarks" /></th>
					<th><spring:message code="lbl.delete" /></th>
				</tr>
			</thead>
			<tbody id="nonSorTable">
				<c:choose>
					<c:when test="${mbHeader.nonSorMbDetails.size() == 0}">
						<tr id="nonSorMessage">
					</c:when>
					<c:otherwise>
						<tr id="nonSorMessage" hidden="true">
					</c:otherwise>
				</c:choose>
					<td colspan="15"><spring:message code="msg.mb.sor.table"/></td>
				</tr>
				<c:choose>
					<c:when test="${mbHeader.nonSorMbDetails.size() == 0 }">
						<tr id="nonSorRow" class="nonSorRow" nonsorinvisible="true" hidden="true" align="center">
							<td>
								<span class="spannonsorslno">1</span>
								<form:input type="hidden" path="nonSorMbDetails[0].id" id="nonSorMbDetailsId_0" class="nonSorMbDetailsId" />
								<form:input type="hidden" path="nonSorMbDetails[0].workOrderActivity.id" id="nonSorWorkOrderActivity_0" class="nonSorWorkOrderActivity" />
							</td>
							<td align="left">
								<span class="nonSorSummary_0"></span>&nbsp
								<span class="hintanchor nonSorDescription_0"/></span>
							</td>
							<td>
								<span class="nonSorUom_0"></span>
							</td>
							<td>
								<span class="nonSorApprovedQuantity_0"></span>
							</td>
							<td align="right">
								<span class="nonSorApprovedRate_0"></span>
								<span class="nonSorUnitRate_0" hidden="true"></span>
							</td>
							<td>
								<span class="nonSorCumulativePreviousEntry_0"></span>
							</td>
							<td>
								<form:input type="hidden" path="nonSorMbDetails[0].rate" id="nonSorUnitRate_0" class="form-control table-input text-right"/>
								<form:input type="hidden" path="nonSorMbDetails[0].amount" id="nonSorAmount_0" class="form-control table-input text-right"/>
								<div class="input-group" style="width:150px">
									<form:input path="nonSorMbDetails[0].quantity" id="nonSorQuantity_0" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" required="required" class="form-control input-sm text-right quantity" maxlength="64" onblur="calculateNonSorAmounts(this);" onkeyup="validateQuantityInput(this);"/>
									<span class="input-group-addon openmbsheet" name="nonSorMbDetails[0].msadd" id="nonSorMbDetails[0].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"></i></span>
								</div>
							</td>
							<td hidden="true">
	                            <input class="classmspresent" type="hidden" disabled="disabled" name="nonSorMbDetails[0].mspresent" id="nonSorMbDetails[0].mspresent" data-idx="0"/>
	                            <input class="classmsopen" type="hidden" disabled="disabled" name="nonSorMbDetails[0].msopen" id="nonSorMbDetails[0].msopen" data-idx="0"/>
								<span  class="nonSorMbDetails[0].mstd" id="nonSorMbDetails[0].mstd" data-idx="0"></span>
							</td>
							<td>
								<span class="nonSorCumulativeIncludingCurrentEntry_0"></span>
							</td>
							<td align="right">
								<span class="nonSorAmountCurrentEntry nonSorAmountCurrentEntry_0"></span>
							</td>
							<td align="right">
								<span class="nonSorAmountIncludingCurrentEntry nonSorAmountIncludingCurrentEntry_0"></span>
							</td>
							<td align="right">
								<span class="nonSorApprovedAmount_0"></span>
							</td>
							<td>
								<form:textarea path="nonSorMbDetails[0].remarks" id="nonSorRemarks_0" data-idx="0" data-optional="1" class="form-control table-input" maxlength="1024"></form:textarea>
							</td>
							<td>
								<span class="add-padding nonSorDelete nonSorDelete_0" onclick="deleteNonSor(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${mbHeader.nonSorMbDetails}" var="details" varStatus="item">
							<tr id="nonSorRow" class="nonSorRow" align="center">
								<td>
									<span class="spannonsorslno">${item.index + 1 }</span>
									<form:input type="hidden" path="nonSorMbDetails[${item.index }].id" id="nonSorMbDetailsId_${item.index }" class="nonSorMbDetailsId" value="${details.id }" />
									<form:input type="hidden" path="nonSorMbDetails[${item.index }].workOrderActivity.id" id="nonSorWorkOrderActivity_${item.index }" class="nonSorWorkOrderActivity" value="${details.workOrderActivity.id }" />
								</td>
								<td align="left">
									<span class="nonSorSummary_${item.index }">${details.workOrderActivity.activity.nonSor.description }</span>&nbsp
									<span class="hintanchor nonSorDescription_${item.index }"/><a href="#" class="hintanchor" title="${details.workOrderActivity.activity.nonSor.description }"><i class="fa fa-question-circle" aria-hidden="true"></i></a></span>
								</td>
								<td>
									<span class="nonSorUom_${item.index }">${details.workOrderActivity.activity.nonSor.uom.uom }</span>
								</td>
								<td>
									<span class="nonSorApprovedQuantity_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.workOrderActivity.approvedQuantity }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="nonSorApprovedRate_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.workOrderActivity.activity.estimateRate }</fmt:formatNumber></span>
									<span class="nonSorUnitRate_${item.index }" hidden="true">${details.rate }</span>
								</td>
								<td>
									<span class="nonSorCumulativePreviousEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.prevCumlvQuantity }</fmt:formatNumber></span>
								</td>
								<td>
									<form:input type="hidden" path="nonSorMbDetails[${item.index }].rate" value="${details.rate }" id="nonSorUnitRate_${item.index }" class="form-control table-input text-right"/>
									<form:input type="hidden" path="nonSorMbDetails[${item.index }].amount" value="${details.amount }" id="nonSorAmount_${item.index }" class="form-control table-input text-right"/>
									<div class="input-group" style="width:150px">
										<c:choose>
				                    		<c:when test="${!details.measurementSheets.isEmpty() }">
				                    			<form:input path="nonSorMbDetails[${item.index }].quantity" readonly="true" value="${details.quantity }" id="nonSorQuantity_${item.index }" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0" required="required" class="form-control input-sm text-right quantity" maxlength="64" onblur="calculateNonSorAmounts(this);" onkeyup="validateQuantityInput(this);"/>
				                    		</c:when>
				                    		<c:otherwise>
				                    			<form:input path="nonSorMbDetails[${item.index }].quantity" value="${details.quantity }" id="nonSorQuantity_${item.index }" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0" required="required" class="form-control input-sm text-right quantity" maxlength="64" onblur="calculateNonSorAmounts(this);" onkeyup="validateQuantityInput(this);"/>
				                    		</c:otherwise>
				                    	</c:choose>
										<c:choose>
											<c:when test="${!details.measurementSheets.isEmpty() }">
												<span class="input-group-addon openmbsheet" name="nonSorMbDetails[${item.index }].msadd" id="nonSorMbDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"></i></span>
											</c:when>
											<c:otherwise>
												<span style="display: none;" class="input-group-addon openmbsheet" name="nonSorMbDetails[${item.index }].msadd" id="nonSorMbDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"></i></span>
											</c:otherwise>
										</c:choose>
									</div>
								</td>
								<td hidden="true">
									<c:set var="net" value="0" />
									<c:set var="total" value="0" />
		                            <input class="classmspresent" type="hidden" disabled="disabled" name="nonSorMbDetails[${item.index }].mspresent" id="nonSorMbDetails[${item.index }].mspresent" data-idx="0"/>
		                            <input class="classmsopen" type="hidden" disabled="disabled" name="nonSorMbDetails[${item.index }].msopen" id="nonSorMbDetails[${item.index }].msopen" data-idx="0"/>
									<span  class="nonSorMbDetails[${item.index }].mstd" id="nonSorMbDetails[${item.index }].mstd" data-idx="0">
										<%@ include file="../measurementsheet/mb-nonsor-measurementsheet-formtable-edit.jsp"%>
									</span>
								</td>
								<td>
									<span class="nonSorCumulativeIncludingCurrentEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.prevCumlvQuantity + details.quantity }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="nonSorAmountCurrentEntry nonSorAmountCurrentEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.rate * details.quantity }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="nonSorAmountIncludingCurrentEntry nonSorAmountIncludingCurrentEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.rate * (details.prevCumlvQuantity + details.quantity) }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="nonSorApprovedAmount_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.workOrderActivity.approvedAmount }</fmt:formatNumber></span>
								</td>
								<td>
									<form:textarea path="nonSorMbDetails[${item.index }].remarks" value="${details.remarks }" id="nonSorRemarks_${item.index }" data-idx="${item.index }" data-optional="1" class="form-control table-input" maxlength="1024"></form:textarea>
								</td>
								<td>
									<span class="add-padding nonSorDelete nonSorDelete_${item.index }" onclick="deleteNonSor(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="8" class="text-right"><spring:message code="lbl.total" /></td>
					<td class="text-right"> <span id="nonSorTotal">0.00</span> </td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
<div id="measurement" >
<%@ include file="../measurementsheet/mb-measurementsheet-formtable.jsp"%>
</div>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/mb/mbmeasurementsheet.js?rnd=${app_release_no}'/>"></script>