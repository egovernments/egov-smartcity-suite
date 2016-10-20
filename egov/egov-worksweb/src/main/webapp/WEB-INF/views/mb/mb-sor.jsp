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
		<div class="position_alert3"><spring:message code="lbl.pagetotal" /> : &#8377 <span id="pageTotal"></span></div>
		<div class="panel-title">
			<spring:message code="title.mb.details" />
			<div class="pull-right">
				<a id="searchAndAdd" href="javascript:void(0);" class="btn btn-primary">
					<i class="fa fa-plus"></i><spring:message code="lbl.add" />
				</a>
				<a id="addAll" href="javascript:void(0);" class="btn btn-primary">
					<i class="fa fa-plus"></i><spring:message code="lbl.addall" />
				</a>
			</div>
		</div>
	</div>
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="title.sor" />
		</div>
	</div>
	<div align="right" class="openCloseAll">
		<input type="button" value="Close All Measurements" class="btn btn-sm btn-secondary"
			onclick="closeAllmsheet()" /> <input type="button" class="btn btn-sm btn-secondary"
			value="Open All Measurements" onclick="openAllmsheet()" />
	</div>
	<div class="panel-body" id="sorHeaderTable">
		<table class="table table-bordered" style="overflow: auto;" id="tblsor">
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
					<th><spring:message code="lbl.cumulative.quantity.current.entry" /></th>
					<th><spring:message code="lbl.amount.current.entry" /></th>
					<th><spring:message code="lbl.cumulative.amount.current.entry" /></th>
					<th><spring:message code="lbl.approved.amount" /></th>
					<th><spring:message code="lbl.remarks" /></th>
					<th><spring:message code="lbl.delete" /></th>
				</tr>
			</thead>
			<tbody id="sorTable">
				<c:if test="${mbHeader.sorMbDetails.size() == 0}">
					<tr id="message">
				</c:if>
				<c:if test="${mbHeader.sorMbDetails.size() != 0}">
					<tr id="message" hidden="true">
				</c:if>
					<td colspan="15"><spring:message code="msg.mb.sor.table"/></td>
				</tr>
				<c:choose>
					<c:when test="${mbHeader.sorMbDetails.size() == 0}">
						<tr id="sorRow" class="sorRow" sorinvisible="true" hidden="true" align="center">
							<td>
								<span class="spansorslno">1</span>
								<form:input type="hidden" path="sorMbDetails[0].id" id="sorMbDetailsId_0" class="sorMbDetailsId" />
								<form:input type="hidden" path="sorMbDetails[0].workOrderActivity.id" id="workOrderActivity_0" class="workOrderActivity" />
							</td>
							<td>
								<span class="sorCategory_0"></span>
							</td>
							<td>
								<span class="sorCode_0"></span>
							</td>
							<td align="left">
								<span class="summary_0"></span>&nbsp
								<span class="hintanchor description_0"/></span>
							</td>
							<td>
								<span class="uom_0"></span>
							</td>
							<td>
								<span class="approvedQuantity_0"></span>
							</td>
							<td align="right">
								<span class="approvedRate_0"></span>
							</td>
							<td>
								<span class="cumulativePreviousEntry_0"></span>
							</td>
							<td>
								<form:input type="hidden" path="sorMbDetails[0].rate" id="unitRate_0" class="form-control table-input text-right"/>
								<form:input type="hidden" path="sorMbDetails[0].amount" id="amount_0" class="form-control table-input text-right"/>
								<div class="input-group" style="width:150px">
									<form:input path="sorMbDetails[0].quantity" id="quantity_0" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" required="required" class="form-control input-sm text-right quantity" maxlength="64" onblur="calculateSorAmounts(this);" onkeyup="validateQuantityInput(this);"/>
									<span class="input-group-addon openmbsheet" name="sorMbDetails[0].msadd" id="sorMbDetails[0].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"></i></span>
								</div>
							</td>
							<td hidden="true">
	                            <input class="classmspresent" type="hidden" disabled="disabled" name="sorMbDetails[0].mspresent" id="sorMbDetails[0].mspresent" data-idx="0"/>
	                            <input class="classmsopen" type="hidden" disabled="disabled" name="sorMbDetails[0].msopen" id="sorMbDetails[0].msopen" data-idx="0"/>
								<span  class="sorMbDetails[0].mstd" id="sorMbDetails[0].mstd" data-idx="0"></span>
							</td>
							<td>
								<span class="cumulativeIncludingCurrentEntry_0"></span>
							</td>
							<td align="right">
								<span class="amountCurrentEntry amountCurrentEntry_0"></span>
							</td>
							<td align="right">
								<span class="amountIncludingCurrentEntry amountIncludingCurrentEntry_0"></span>
							</td>
							<td align="right">
								<span class="approvedAmount_0"></span>
							</td>
							<td>
								<form:textarea path="sorMbDetails[0].remarks" id="remarks_0" data-idx="0" data-optional="1" class="form-control table-input" maxlength="1024"></form:textarea>
							</td>
							<td>
								<span class="add-padding sordelete delete_0" onclick="deleteSor(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${mbHeader.sorMbDetails}" var="details" varStatus="item">
							<tr id="sorRow" class="sorRow" align="center">
								<td>
									<span class="spansorslno">${item.index + 1 }</span>
									<form:input type="hidden" path="sorMbDetails[${item.index }].id" id="sorMbDetailsId_${item.index }" class="sorMbDetailsId" value="${details.id }" />
									<form:input type="hidden" path="sorMbDetails[${item.index }].workOrderActivity.id" id="workOrderActivity_${item.index }" class="workOrderActivity" value="${details.workOrderActivity.id }" />
								</td>
								<td>
									<span class="sorCategory_${item.index }">${details.workOrderActivity.activity.schedule.scheduleCategory.code }</span>
								</td>
								<td>
									<span class="sorCode_${item.index }">${details.workOrderActivity.activity.schedule.code }</span>
								</td>
								<td align="left">
									<span class="summary_${item.index }">${details.workOrderActivity.activity.schedule.getSummary() }</span>&nbsp
									<span class="hintanchor description_${item.index }"/><a href="#" class="hintanchor" title="${details.workOrderActivity.activity.schedule.description }"><i class="fa fa-question-circle" aria-hidden="true"></i></a></span>
								</td>
								<td>
									<span class="uom_${item.index }">${details.workOrderActivity.activity.schedule.uom.uom }</span>
								</td>
								<td>
									<span class="approvedQuantity_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.workOrderActivity.approvedQuantity }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="approvedRate_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.workOrderActivity.activity.estimateRate }</fmt:formatNumber></span>
								</td>
								<td>
									<span class="cumulativePreviousEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.prevCumlvQuantity }</fmt:formatNumber></span>
								</td>
								<td>
									<form:input type="hidden" path="sorMbDetails[${item.index }].rate" value="${details.rate }" id="unitRate_${item.index }" class="form-control table-input text-right"/>
									<form:input type="hidden" path="sorMbDetails[${item.index }].amount" value="${details.amount }" id="amount_${item.index }" class="form-control table-input text-right"/>
									<div class="input-group" style="width:150px">
										<c:choose>
			            	        		<c:when test="${!details.measurementSheets.isEmpty() }">
			                	    			<form:input path="sorMbDetails[${item.index }].quantity" readonly="true" value="${details.quantity }" id="quantity_${item.index }" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0" required="required" class="form-control input-sm text-right quantity" maxlength="64" onblur="calculateSorAmounts(this);" onkeyup="validateQuantityInput(this);"/>
			                    			</c:when>
			                    			<c:otherwise>
			                    				<form:input path="sorMbDetails[${item.index }].quantity" value="${details.quantity }" id="quantity_${item.index }" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0" required="required" class="form-control input-sm text-right quantity" maxlength="64" onblur="calculateSorAmounts(this);" onkeyup="validateQuantityInput(this);"/>
				                    		</c:otherwise>
				                    	</c:choose>
										<c:choose>
											<c:when test="${!details.measurementSheets.isEmpty() }">
												<span class="input-group-addon openmbsheet" name="sorMbDetails[${item.index }].msadd" id="sorMbDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"><i  class="fa fa-plus-circle" aria-hidden="true"></i></span>
											</c:when>
											<c:otherwise>
												<span style="display: none;" class="input-group-addon openmbsheet" name="sorMbDetails[${item.index }].msadd" id="sorMbDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"><i  class="fa fa-plus-circle" aria-hidden="true"></i></span>
											</c:otherwise>
										</c:choose>
									</div>
								</td>
								<td hidden="true">
									<c:set var="net" value="0" />
									<c:set var="total" value="0" />
		                            <input class="classmspresent" type="hidden" disabled="disabled" name="sorMbDetails[${item.index }].mspresent" id="sorMbDetails[${item.index }].mspresent" data-idx="0"/>
		                            <input class="classmsopen" type="hidden" disabled="disabled" name="sorMbDetails[${item.index }].msopen" id="sorMbDetails[${item.index }].msopen" data-idx="0"/>
									<span  class="sorMbDetails[${item.index }].mstd" id="sorMbDetails[${item.index }].mstd" data-idx="0">
										<%@ include file="../measurementsheet/mb-sor-measurementsheet-formtable-edit.jsp"%>
									</span>
								</td>
								<td>
									<span class="cumulativeIncludingCurrentEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.prevCumlvQuantity + details.quantity }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="amountCurrentEntry amountCurrentEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.rate * details.quantity }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="amountIncludingCurrentEntry amountIncludingCurrentEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.rate * (details.prevCumlvQuantity + details.quantity) }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="approvedAmount_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.workOrderActivity.approvedAmount }</fmt:formatNumber></span>
								</td>
								<td>
									<form:textarea path="sorMbDetails[${item.index }].remarks" value="${details.remarks }" id="remarks_${item.index }" data-idx="${item.index }" data-optional="1" class="form-control table-input" maxlength="1024"></form:textarea>
								</td>
								<td>
									<span class="add-padding sordelete delete_${item.index }" onclick="deleteSor(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="10" class="text-right"><spring:message code="lbl.total" /></td>
					<td class="text-right"> <span id="sorTotal">0.00</span> </td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
