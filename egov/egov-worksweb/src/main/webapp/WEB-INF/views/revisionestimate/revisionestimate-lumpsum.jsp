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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>  

<div id="baseLumpSumTable" class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="title.lumpsum" />
			<div class="pull-right">
				<a id="addlumpSumRow" href="javascript:void(0);" class="btn btn-primary">
					<i class="fa fa-plus"></i><spring:message code="lbl.addlumpsum" />
				</a>
			</div>
		</div>
	</div>
	<input type="hidden" id="errorlumpsumquantitieszero" value="<spring:message code='error.lumpsum.quantity.zero' />">
	<input type="hidden" id="errordescription" value="<spring:message code='error.nonsor.description' />">
	<input type="hidden" id="erroruom" value="<spring:message code='error.nonsor.uom' />">
	<input type="hidden" value="${revisionEstimate.lumpSumActivities.size() }" id="lumpSumActivitiesSize" />
	<div class="panel-body" id="lumpSumHeaderTable">
		<table class="table table-bordered" id="tblLumpSum">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.description" /><span class="mandatory" /></th>
					<th><spring:message code="lbl.uom" /><span class="mandatory" /></th>
					<th><spring:message code="lbl.rate" /><span class="mandatory" /></th>
					<th><spring:message code="lbl.estimatedquantity" /><span class="mandatory" /></th>
					<th><spring:message code="lbl.estimatedamount" /></th>
					<th hidden="true" id="lumpSumServiceVatHeader"><spring:message code="lbl.service.vat" /></th>
					<th hidden="true" id="lumpSumVatAmountHeader"><spring:message code="lbl.service.vat.amount" /></th>
					<th><spring:message code="lbl.total" /></th>
					<th><spring:message code="lbl.delete" /></th>
				</tr>
			</thead>
			<tbody id="lumpSumTable">
				<c:choose>
					<c:when test="${revisionEstimate.lumpSumActivities.size() == 0}">
						<tr id="lumpSumMessage">
					</c:when>
					<c:otherwise>
						<tr id="lumpSumMessage" hidden="true">
					</c:otherwise>
				</c:choose>
				<c:if test="${isServiceVATRequired == true }">
					<td colspan="10"><spring:message code="msg.lumpsum.table"/></td>
				</c:if>
				<c:if test="${isServiceVATRequired == false }">
					<td colspan="8"><spring:message code="msg.lumpsum.table"/></td>
				</c:if>
				</tr>
				<c:choose>
					<c:when test="${revisionEstimate.lumpSumActivities.size() == 0 }">
						<tr id="lumpSumRow" class="lumpSumRow" lumpsuminvisible="true" hidden="true" align="center">
							<td>
								<span class="spanlumpsumslno">1</span>
								<form:hidden path="lumpSumActivities[0].id" id="activityid_0" class="activityid" />
								<form:hidden path="lumpSumActivities[0].nonSor.id" id="lumpSumId_0" class="lumpSumId"/>
							</td>
							<td>
								<form:input path="lumpSumActivities[0].nonSor.description" id="lumpSumDesc_0" class="form-control table-input text-left lumpSumDesc" maxlength="256"/>
							</td>
							<td>
								<form:select path="lumpSumActivities[0].nonSor.uom" id="lumpSumUom_0" data-idx="0" data-first-option="false" class="form-control lumpSumUom" onchange="updateUom(this);">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<c:forEach items="${uoms }" var="uom">
										<form:option value="${uom.id }">${uom.uomCategory.category } -- ${uom.uom }</form:option>
									</c:forEach>
								</form:select>
								<form:hidden path="lumpSumActivities[0].uom" id="lumpSumUomid_0" class="uomhiddenid"/>
							</td>
							<td align="right">
								<form:input path="lumpSumActivities[0].estimateRate" id="lumpSumEstimateRate_0" data-pattern="decimalvalue" class="activityEstimateRate form-control table-input text-right lumpSumEstimateRate" maxlength="256" onblur="calculateLumpSumEstimateAmount(this);" onkeyup="validateInput(this);"/>
								<form:hidden path="lumpSumActivities[0].rate" id="lumpSumRate_0"  class="activityRate form-control table-input text-right lumpSumRate" />

							</td>

							<td>
							<div class="input-group" style="width:150px">
								<form:input path="lumpSumActivities[0].quantity" id="lumpSumQuantity_0" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right lumpSumQuantity" maxlength="64" onchange="calculateLumpSumEstimateAmount(this);" onkeyup="validateQuantityInput(this);"/>
								<span class="input-group-addon openmsheet" name="lumpSumActivities[0].msadd" id="lumpSumActivities[0].msadd" data-idx="0" onclick="addMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"></i></span>
						    </div>
							</td>
                             <td hidden="true">
                             <input class="classmspresent" type="hidden" disabled="disabled" name="lumpSumActivities[0].mspresent" id="lumpSumActivities[0].mspresent" data-idx="0"/>
                             <input class="classmsopen" type="hidden" disabled="disabled" name="lumpSumActivities[0].msopen" id="lumpSumActivities[0].msopen" data-idx="0"/>
                            
                             <span name="lumpSumActivities[0].mstd" class="lumpSumActivities[0].mstd" id="lumpSumActivities[0].mstd" data-idx="0"></span>
                             
                             </td>
				 
							<td align="right">
								<span class="lumpSumAmount_0 lumpsumamount"></span>
							</td>
							<td hidden="true" class="lumpSumServiceTaxPerc">
								<form:input path="lumpSumActivities[0].serviceTaxPerc" id="lumpSumServiceTaxPerc_0" data-pattern="decimalvalue" data-idx="0" data-optional="1" class="form-control table-input text-right lumpSumServiceTaxPerc" maxlength="64" onblur="calculateLumpSumVatAmount(this);" onkeyup="validateInput(this);"/>
							</td>
							<td hidden="true" align="right" class="lumpSumVatAmount">
								<span class="lumpSumVatAmount_0 lumpSumVatAmt"></span>
							</td>
							<td align="right">
								<span class="lumpSumTotal_0 lumpSumTotal"></span>
							</td>
							<td>
								<span class="add-padding delete_0" onclick="deleteLumpSum(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${revisionEstimate.lumpSumActivities}" var="activity" varStatus="item">
								<tr id="lumpSumRow" class="lumpSumRow" align="center">
									<td>
										<span class="spanlumpsumslno">${item.index + 1 }</span>
										<form:hidden path="lumpSumActivities[${item.index }].id" id="activityid_${item.index }" class="activityid" value="${activity.id }" />
										<form:hidden path="lumpSumActivities[${item.index }].nonSor.id" id="lumpSumId_${item.index }" class="lumpSumId" value="${activity.nonSor.id }"/>
									</td>
									<td>
										<form:input path="lumpSumActivities[${item.index }].nonSor.description" id="lumpSumDesc_${item.index }" value="${activity.nonSor.description }" class="form-control table-input text-left lumpSumDesc" maxlength="256"/>
									</td>
									<td>
										<form:select path="lumpSumActivities[${item.index }].nonSor.uom" id="lumpSumUom_${item.index }" data-idx="${item.index }" data-first-option="false" class="form-control lumpSumUom" onchange="updateUom(this);">
											<form:option value="">
												<spring:message code="lbl.select" />
											</form:option>
											<c:forEach items="${uoms }" var="uom">
												<c:if test="${uom.id == activity.uom.id }">
													<form:option value="${uom.id }" selected="selected" >${uom.uomCategory.category } -- ${uom.uom }</form:option>
												</c:if>
												<c:if test="${uom.id != activity.uom.id }">
													<form:option value="${uom.id }">${uom.uomCategory.category } -- ${uom.uom }</form:option>
												</c:if>
											</c:forEach>
										</form:select>
										<form:hidden path="lumpSumActivities[${item.index }].uom" value="${activity.uom.id }" id="lumpSumUomid_${item.index }" class="uomhiddenid"/>
									</td>
									<td align="right">
										<form:input path="lumpSumActivities[${item.index }].estimateRate" id="lumpSumEstimateRate_${item.index }" value="${activity.estimateRate }" data-pattern="decimalvalue" class="activityEstimateRate form-control table-input text-right lumpSumEstimateRate" maxlength="256" onblur="calculateLumpSumEstimateAmount(this);" onkeyup="validateInput(this);"/>
										<form:hidden path="lumpSumActivities[${item.index }].rate" id="lumpSumRate_${item.index }" class="activityRate form-control table-input text-right lumpSumRate"  />
									</td>
									<c:set var="isreadonly" value="false"/>
									<c:if test="${activity.measurementSheetList.size() > 0 }">
										<c:set var="isreadonly" value="true"/>
									</c:if>
									<td>
										<div class="input-group" style="width:150px">
										<form:input path="lumpSumActivities[${item.index }].quantity" readonly="${isreadonly}" id="lumpSumQuantity_${item.index }" value="${activity.quantity }" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0"  class="form-control table-input text-right lumpSumQuantity" maxlength="64" onchange="calculateLumpSumEstimateAmount(this);" onkeyup="validateQuantityInput(this);" />
									     <span class="input-group-addon openmsheet" name="lumpSumActivities[${item.index}].msadd" id="lumpSumActivities[${item.index}].msadd" data-idx="${item.index }" onclick="addMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true"  ></i></button>
								</div>
									</td>       
 
									<%@ include file="../measurementsheet/lumpsum-measurementsheet-formtableedit.jsp"%>  
									
									<td align="right">
										<span class="lumpSumAmount_${item.index } lumpsumamount"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2">${activity.rate * activity.quantity }</fmt:formatNumber></span>
									</td>
									<td hidden="true" class="lumpSumServiceTaxPerc">
										<form:input path="lumpSumActivities[${item.index }].serviceTaxPerc" value="${activity.serviceTaxPerc }" id="lumpSumServiceTaxPerc_${item.index }" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="1" class="form-control table-input text-right lumpSumServiceTaxPerc" maxlength="64" onblur="calculateLumpSumVatAmount(this);" onkeyup="validateInput(this);"/>
									</td>
									<td hidden="true" align="right" class="lumpSumVatAmount">
										<span class="lumpSumVatAmount_${item.index } lumpSumVatAmt"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2">${(activity.rate * activity.quantity) * (activity.serviceTaxPerc / 100) }</fmt:formatNumber></span>
									</td>
									<td align="right">
										<span class="lumpSumTotal_${item.index } lumpSumTotal"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2">${(activity.rate * activity.quantity) + ((activity.rate * activity.quantity) * (activity.serviceTaxPerc / 100)) }</fmt:formatNumber></span>
									</td>
									<td>
										<span class="add-padding delete_${item.index }" onclick="deleteLumpSum(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
									</td>
								</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="5" class="text-right"><spring:message code="lbl.total" /></td>
					<td class="text-right"> <span id="lumpSumEstimateTotal">0.00</span> </td>
					<td hidden="true" class="emptytd"></td>
					<td hidden="true" class="text-right lumpSumServiceVatAmt"> <span id="lumpSumServiceVatAmtTotal">0.00</span> </td>
					<td class="text-right"> <span id="lumpSumTotal">0.00</span> </td>
					<td></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
 