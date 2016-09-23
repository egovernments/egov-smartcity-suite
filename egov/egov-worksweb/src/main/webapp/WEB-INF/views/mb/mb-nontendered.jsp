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
<div id="baseNonTenderedTable" class="panel panel-primary" data-collapsed="0">
	<input type="hidden" id="errorquantitieszero" value="<spring:message code='error.quantity.zero' />">
	<input type="hidden" id="errorcumulativequantity" value="<spring:message code='error.approved.quantity.cumulative' />">
	<input type="hidden" id="isMeasurementsExist" value="${isMeasurementsExist }">
	<div class="panel-heading">
		<div class="position_alert3"><spring:message code="lbl.pagetotal" /> : &#8377 <span id="nonTenderedPageTotal"></span></div>
		<div class="panel-title">
			<spring:message code="title.mb.details" />
			<div class="pull-right">
				<a id="searchNonTenderedAndAdd" href="javascript:void(0);" class="btn btn-primary">
					<i class="fa fa-plus"></i><spring:message code="lbl.add" />
				</a>
				<a id="addAllNonTendered" href="javascript:void(0);" class="btn btn-primary">
					<i class="fa fa-plus"></i><spring:message code="lbl.addall" />
				</a>
			</div>
		</div>
	</div>
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="title.nontendered" />
		</div>
	</div>
	<div align="right" class="openCloseAll">
		<input type="button" value="Close All Measurements" class="btn btn-sm btn-secondary"
			onclick="closeAllmsheet()" /> <input type="button" class="btn btn-sm btn-secondary"
			value="Open All Measurements" onclick="openAllmsheet()" />
	</div>
	<div class="panel-body" id="nonTenderedHeaderTable">
		<table class="table table-bordered" style="overflow: auto;" id="tblNonTendered">
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
			<tbody id="nonTenderedTable">
				<c:if test="${mbHeader.nonTenderedMbDetails.size() == 0}">
					<tr id="nonTenderedMessage">
				</c:if>
				<c:if test="${mbHeader.nonTenderedMbDetails.size() != 0}">
					<tr id="nonTenderedMessage" hidden="true">
				</c:if>
					<td colspan="15"><spring:message code="msg.mb.sor.table"/></td>
				</tr>
				<c:choose>
					<c:when test="${mbHeader.nonTenderedMbDetails.size() == 0}">
						<tr id="nonTenderedRow" class="nonTenderedRow" sorinvisible="true" hidden="true" align="center">
							<td>
								<span class="spannontenderedslno">1</span>
								<form:input type="hidden" path="nonTenderedMbDetails[0].id" id="nonTenderedMbDetailsId_0" class="nonTenderedMbDetailsId" />
								<form:input type="hidden" path="nonTenderedMbDetails[0].workOrderActivity.id" id="nonTenderedworkOrderActivity_0" class="nonTenderedworkOrderActivity" />
							</td>
							<td>
								<span class="nonTenderedsorCategory_0"></span>
							</td>
							<td>
								<span class="nonTenderedsorCode_0"></span>
							</td>
							<td align="left">
								<span class="nonTenderedsummary_0"></span>&nbsp
								<span class="hintanchor nonTendereddescription_0"/></span>
							</td>
							<td>
								<span class="nonTendereduom_0"></span>
							</td>
							<td>
								<span class="nonTenderedapprovedQuantity_0"></span>
							</td>
							<td align="right">
								<span class="nonTenderedapprovedRate_0"></span>
							</td>
							<td>
								<span class="nonTenderedcumulativePreviousEntry_0"></span>
							</td>
							<td>
								<form:input type="hidden" path="nonTenderedMbDetails[0].rate" id="nonTenderedunitRate_0" class="form-control table-input text-right"/>
								<form:input type="hidden" path="nonTenderedMbDetails[0].amount" id="nonTenderedamount_0" class="form-control table-input text-right"/>
								<div class="input-group" style="width:150px">
									<form:input path="nonTenderedMbDetails[0].quantity" id="nonTenderedquantity_0" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" required="required" class="form-control input-sm text-right quantity" maxlength="64" onblur="calculateNonTenderedAmounts(this);" onkeyup="validateQuantityInput(this);"/>
									<span class="input-group-addon openmbsheet" name="nonTenderedMbDetails[0].msadd" id="nonTenderedMbDetails[0].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"></i></span>
								</div>
							</td>
							<td hidden="true">
	                            <input class="classmspresent" type="hidden" disabled="disabled" name="nonTenderedMbDetails[0].mspresent" id="nonTenderedMbDetails[0].mspresent" data-idx="0"/>
	                            <input class="classmsopen" type="hidden" disabled="disabled" name="nonTenderedMbDetails[0].msopen" id="nonTenderedMbDetails[0].msopen" data-idx="0"/>
								<span  class="nonTenderedMbDetails[0].mstd" id="nonTenderedMbDetails[0].mstd" data-idx="0"></span>
							</td>
							<td>
								<span class="nonTenderedcumulativeIncludingCurrentEntry_0"></span>
							</td>
							<td align="right">
								<span class="nonTenderedAmountCurrentEntry nonTenderedAmountCurrentEntry_0"></span>
							</td>
							<td align="right">
								<span class="nonTenderedamountIncludingCurrentEntry nonTenderedamountIncludingCurrentEntry_0"></span>
							</td>
							<td align="right">
								<span class="nonTenderedapprovedAmount_0"></span>
							</td>
							<td>
								<form:textarea path="nonTenderedMbDetails[0].remarks" id="nonTenderedremarks_0" data-idx="0" data-optional="1" class="form-control table-input" maxlength="1024"></form:textarea>
							</td>
							<td>
								<span class="add-padding nonTenderedDelete nonTendereddelete_0" onclick="deleteNonTendered(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${mbHeader.nonTenderedMbDetails}" var="details" varStatus="item">
							<tr id="nonTenderedRow" class="nonTenderedRow" align="center">
								<td>
									<span class="spannontenderedslno">${item.index + 1 }</span>
									<form:input type="hidden" path="nonTenderedMbDetails[${item.index }].id" id="nonTenderedMbDetailsId_${item.index }" class="nonTenderedMbDetailsId" value="${details.id }" />
									<form:input type="hidden" path="nonTenderedMbDetails[${item.index }].workOrderActivity.id" id="workOrderActivity_${item.index }" class="nonTenderedworkOrderActivity" value="${details.workOrderActivity.id }" />
								</td>
								<td>
									<span class="nonTenderedsorCategory_${item.index }">${details.workOrderActivity.activity.schedule.scheduleCategory.code }</span>
								</td>
								<td>
									<span class="nonTenderedsorCode_${item.index }">${details.workOrderActivity.activity.schedule.code }</span>
								</td>
								<td align="left">
									<span class="nonTenderedsummary_${item.index }">${details.workOrderActivity.activity.schedule.getSummary() }</span>&nbsp
									<span class="hintanchor nonTendereddescription_${item.index }"/><a href="#" class="hintanchor" title="${details.workOrderActivity.activity.schedule.description }"><i class="fa fa-question-circle" aria-hidden="true"></i></a></span>
								</td>
								<td>
									<span class="nonTendereduom_${item.index }">${details.workOrderActivity.activity.schedule.uom.uom }</span>
								</td>
								<td>
									<span class="nonTenderedapprovedQuantity_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.workOrderActivity.approvedQuantity }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="nonTenderedapprovedRate_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.workOrderActivity.activity.estimateRate }</fmt:formatNumber></span>
								</td>
								<td>
									<span class="nonTenderedcumulativePreviousEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.prevCumlvQuantity }</fmt:formatNumber></span>
								</td>
								<td>
									<form:input type="hidden" path="nonTenderedMbDetails[${item.index }].rate" value="${details.rate }" id="nonTenderedunitRate_${item.index }" class="form-control table-input text-right"/>
									<form:input type="hidden" path="nonTenderedMbDetails[${item.index }].amount" value="${details.amount }" id="nonTenderedamount_${item.index }" class="form-control table-input text-right"/>
									<div class="input-group" style="width:150px">
										<c:choose>
			            	        		<c:when test="${!details.measurementSheets.isEmpty() }">
			                	    			<form:input path="nonTenderedMbDetails[${item.index }].quantity" readonly="true" value="${details.quantity }" id="nonTenderedquantity_${item.index }" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0" required="required" class="form-control input-sm text-right quantity" maxlength="64" onblur="calculateNonTenderedAmounts(this);" onkeyup="validateQuantityInput(this);"/>
			                    			</c:when>
			                    			<c:otherwise>
			                    				<form:input path="nonTenderedMbDetails[${item.index }].quantity" value="${details.quantity }" id="nonTenderedquantity_${item.index }" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0" required="required" class="form-control input-sm text-right quantity" maxlength="64" onblur="calculateNonTenderedAmounts(this);" onkeyup="validateQuantityInput(this);"/>
				                    		</c:otherwise>
				                    	</c:choose>
										<c:choose>
											<c:when test="${!details.measurementSheets.isEmpty() }">
												<span class="input-group-addon openmbsheet" name="nonTenderedMbDetails[${item.index }].msadd" id="nonTenderedMbDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"><i  class="fa fa-plus-circle" aria-hidden="true"></i></span>
											</c:when>
											<c:otherwise>
												<span style="display: none;" class="input-group-addon openmbsheet" name="nonTenderedMbDetails[${item.index }].msadd" id="nonTenderedMbDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"><i  class="fa fa-plus-circle" aria-hidden="true"></i></span>
											</c:otherwise>
										</c:choose>
									</div>
								</td>
								<td hidden="true">
									<c:set var="net" value="0" />
									<c:set var="total" value="0" />
		                            <input class="classmspresent" type="hidden" disabled="disabled" name="nonTenderedMbDetails[${item.index }].mspresent" id="nonTenderedMbDetails[${item.index }].mspresent" data-idx="0"/>
		                            <input class="classmsopen" type="hidden" disabled="disabled" name="nonTenderedMbDetails[${item.index }].msopen" id="nonTenderedMbDetails[${item.index }].msopen" data-idx="0"/>
									<span  class="nonTenderedMbDetails[${item.index }].mstd" id="nonTenderedMbDetails[${item.index }].mstd" data-idx="0">
										<%@ include
											file="../measurementsheet/mb-nontendered-measurementsheet-formtable-edit.jsp"%>
								</span>
								</td>
								<td>
									<span class="nonTenderedcumulativeIncludingCurrentEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.prevCumlvQuantity + details.quantity }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="nonTenderedAmountCurrentEntry nonTenderedAmountCurrentEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.rate * details.quantity }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="nonTenderedamountIncludingCurrentEntry nonTenderedamountIncludingCurrentEntry_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.rate * (details.prevCumlvQuantity + details.quantity) }</fmt:formatNumber></span>
								</td>
								<td align="right">
									<span class="nonTenderedapprovedAmount_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.workOrderActivity.approvedAmount }</fmt:formatNumber></span>
								</td>
								<td>
									<form:textarea path="nonTenderedMbDetails[${item.index }].remarks" value="${details.remarks }" id="nonTenderedremarks_${item.index }" data-idx="${item.index }" data-optional="1" class="form-control table-input" maxlength="1024"></form:textarea>
								</td>
								<td>
									<span class="add-padding nonTenderedDelete nonTendereddelete_${item.index }" onclick="deleteNonTendered(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="10" class="text-right"><spring:message code="lbl.total" /></td>
					<td class="text-right"> <span id="nonTenderedTotal">0.00</span> </td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
