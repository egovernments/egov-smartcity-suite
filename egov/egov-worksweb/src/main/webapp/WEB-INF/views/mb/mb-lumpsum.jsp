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
<div id="baseLumpSumTable" class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="title.lumpsum" />
		</div>
	</div>
	<div class="panel-body" id="lumpSumHeaderTable">
		<table class="table table-bordered" style="overflow: auto;" id="tblLumpSum">
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
			<tbody id="lumpSumTable">
				<c:choose>
					<c:when test="${mbHeader.lumpSumMbDetails.size() == 0}">
						<tr id="lumpSumMessage">
					</c:when>
					<c:otherwise>
						<tr id="lumpSumMessage" hidden="true">
					</c:otherwise>
				</c:choose>
					<td colspan="15"><spring:message code="msg.mb.sor.table"/></td>
				</tr>
				<c:choose>
					<c:when test="${mbHeader.lumpSumMbDetails.size() == 0 }">
						<tr id="lumpSumRow" class="lumpSumRow" nonsorinvisible="true" hidden="true" align="center">
							<td>
								<span class="spanlumpsumslno">1</span>
								<form:input type="hidden" path="lumpSumMbDetails[0].id" id="lumpSumMbDetailsId_0" class="lumpSumMbDetailsId" />
								<form:input type="hidden" path="lumpSumMbDetails[0].workOrderActivity.id" id="lumpSumWorkOrderActivity_0" class="lumpSumWorkOrderActivity" />
							</td>
							<td align="left">
								<span class="lumpSumSummary_0"></span>&nbsp
								<span class="hintanchor lumpSumDescription_0"/></span>
							</td>
							<td>
								<span class="lumpSumUom_0"></span>
							</td>
							<td>
								<span class="lumpSumApprovedQuantity_0"></span>
							</td>
							<td align="right">
								<span class="lumpSumApprovedRate_0"></span>
								<span class="lumpSumUnitRate_0" hidden="true"></span>
							</td>
							<td>
								<span class="lumpSumCumulativePreviousEntry_0"></span>
							</td>
							<td>
								<form:input type="hidden" path="lumpSumMbDetails[0].rate" id="lumpSumUnitRate_0" class="form-control table-input text-right"/>
								<form:input type="hidden" path="lumpSumMbDetails[0].amount" id="lumpSumAmount_0" class="form-control table-input text-right"/>
								<div class="input-group" style="width:150px">
									<form:input path="lumpSumMbDetails[0].quantity" id="lumpSumQuantity_0" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" required="required" class="form-control input-sm text-right quantity" maxlength="64" onblur="calculateLumpSumAmounts(this);" onkeyup="validateQuantityInput(this);"/>
									<span class="input-group-addon openmbsheet" name="lumpSumMbDetails[0].msadd" id="lumpSumMbDetails[0].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"></i></span>
								</div>
							</td>
							<td hidden="true">
	                            <input class="classmspresent" type="hidden" disabled="disabled" name="lumpSumMbDetails[0].mspresent" id="lumpSumMbDetails[0].mspresent" data-idx="0"/>
	                            <input class="classmsopen" type="hidden" disabled="disabled" name="lumpSumMbDetails[0].msopen" id="lumpSumMbDetails[0].msopen" data-idx="0"/>
								<span  class="lumpSumMbDetails[0].mstd" id="lumpSumMbDetails[0].mstd" data-idx="0"></span>
							</td>
							<td>
								<span class="lumpSumCumulativeIncludingCurrentEntry_0"></span>
							</td>
							<td align="right">
								<span class="lumpSumAmountCurrentEntry lumpSumAmountCurrentEntry_0"></span>
							</td>
							<td align="right">
								<span class="lumpSumAmountIncludingCurrentEntry lumpSumAmountIncludingCurrentEntry_0"></span>
							</td>
							<td align="right">
								<span class="lumpSumApprovedAmount_0"></span>
							</td>
							<td>
								<form:textarea path="lumpSumMbDetails[0].remarks" id="lumpSumRemarks_0" data-idx="0" data-optional="1" class="form-control table-input" maxlength="1024"></form:textarea>
							</td>
							<td>
								<span class="add-padding lumpSumDelete lumpSumDelete_0" onclick="deleteLumpSum(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${mbHeader.lumpSumMbDetails}" var="details" varStatus="item">
							<tr id="lumpSumRow" class="lumpSumRow" align="center">
								<td>
									<span class="spanlumpsumslno">${item.index + 1 }</span>
									<form:input type="hidden" path="lumpSumMbDetails[${item.index }].id" id="lumpSumMbDetailsId_${item.index }" class="lumpSumMbDetailsId" value="${details.id }" />
									<form:input type="hidden" path="lumpSumMbDetails[${item.index }].workOrderActivity.id" id="lumpSumWorkOrderActivity_${item.index }" class="lumpSumWorkOrderActivity" value="${details.workOrderActivity.id }" />
								</td>
								<td align="left">
									<span class="lumpSumSummary_${item.index }">${details.workOrderActivity.activity.nonSor.description }</span>&nbsp
									<span class="hintanchor lumpSumDescription_${item.index }"/><a href="#" class="hintanchor" title="${details.workOrderActivity.activity.nonSor.description }"><i class="fa fa-question-circle" aria-hidden="true"></i></a></span>
								</td>
								<td>
									<span class="lumpSumUom_${item.index }">${details.workOrderActivity.activity.nonSor.uom.uom }</span>
								</td>
								<td>
									<span class="lumpSumApprovedQuantity_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.workOrderActivity.approvedQuantity }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="lumpSumApprovedRate_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.workOrderActivity.activity.estimateRate }</fmt:formatNumber></span>
									<span class="lumpSumUnitRate_${item.index }" hidden="true">${details.rate }</span>
								</td>
								<td>
									<span class="lumpSumCumulativePreviousEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.prevCumlvQuantity }</fmt:formatNumber></span>
								</td>
								<td>
									<form:input type="hidden" path="lumpSumMbDetails[${item.index }].rate" value="${details.rate }" id="lumpSumUnitRate_${item.index }" class="form-control table-input text-right"/>
									<form:input type="hidden" path="lumpSumMbDetails[${item.index }].amount" value="${details.amount }" id="lumpSumAmount_${item.index }" class="form-control table-input text-right"/>
									<div class="input-group" style="width:150px">
										<c:choose>
				                    		<c:when test="${!details.measurementSheets.isEmpty() }">
				                    			<form:input path="lumpSumMbDetails[${item.index }].quantity" readonly="true" value="${details.quantity }" id="lumpSumQuantity_${item.index }" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0" required="required" class="form-control input-sm text-right quantity" maxlength="64" onblur="calculateLumpSumAmounts(this);" onkeyup="validateQuantityInput(this);"/>
				                    		</c:when>
				                    		<c:otherwise>
				                    			<form:input path="lumpSumMbDetails[${item.index }].quantity" value="${details.quantity }" id="lumpSumQuantity_${item.index }" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0" required="required" class="form-control input-sm text-right quantity" maxlength="64" onblur="calculateLumpSumAmounts(this);" onkeyup="validateQuantityInput(this);"/>
				                    		</c:otherwise>
				                    	</c:choose>
										<c:choose>
											<c:when test="${!details.measurementSheets.isEmpty() }">
												<span class="input-group-addon openmbsheet" name="lumpSumMbDetails[${item.index }].msadd" id="lumpSumMbDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"></i></span>
											</c:when>
											<c:otherwise>
												<span style="display: none;" class="input-group-addon openmbsheet" name="lumpSumMbDetails[${item.index }].msadd" id="lumpSumMbDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"></i></span>
											</c:otherwise>
										</c:choose>
									</div>
								</td>
								<td hidden="true">
									<c:set var="net" value="0" />
									<c:set var="total" value="0" />
		                            <input class="classmspresent" type="hidden" disabled="disabled" name="lumpSumMbDetails[${item.index }].mspresent" id="lumpSumMbDetails[${item.index }].mspresent" data-idx="0"/>
		                            <input class="classmsopen" type="hidden" disabled="disabled" name="lumpSumMbDetails[${item.index }].msopen" id="lumpSumMbDetails[${item.index }].msopen" data-idx="0"/>
									<span  class="lumpSumMbDetails[${item.index }].mstd" id="lumpSumMbDetails[${item.index }].mstd" data-idx="0">
										<%@ include file="../measurementsheet/mb-lumpsum-measurementsheet-formtable-edit.jsp"%>
									</span>
								</td>
								<td>
									<span class="lumpSumCumulativeIncludingCurrentEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.prevCumlvQuantity + details.quantity }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="lumpSumAmountCurrentEntry lumpSumAmountCurrentEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.rate * details.quantity }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="lumpSumAmountIncludingCurrentEntry lumpSumAmountIncludingCurrentEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.rate * (details.prevCumlvQuantity + details.quantity) }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="lumpSumApprovedAmount_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.workOrderActivity.approvedAmount }</fmt:formatNumber></span>
								</td>
								<td>
									<form:textarea path="lumpSumMbDetails[${item.index }].remarks" value="${details.remarks }" id="lumpSumRemarks_${item.index }" data-idx="${item.index }" data-optional="1" class="form-control table-input" maxlength="1024"></form:textarea>
								</td>
								<td>
									<span class="add-padding lumpSumDelete lumpSumDelete_${item.index }" onclick="deleteLumpSum(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="8" class="text-right"><spring:message code="lbl.total" /></td>
					<td class="text-right"> <span id="lumpSumTotal">0.00</span> </td>
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