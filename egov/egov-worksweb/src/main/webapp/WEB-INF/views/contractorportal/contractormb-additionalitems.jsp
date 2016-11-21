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

<div id="baseNonSORTable" class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.additional.items" />
			<div class="pull-right">
				<a id="addnonSorRow" href="javascript:void(0);" class="btn btn-primary">
					<i class="fa fa-plus"></i><spring:message code="lbl.add.item" />
				</a>
			</div>
		</div>
	</div>
	<input type="hidden" id="errordescription" value="<spring:message code='error.nonsor.description' />">
	<input type="hidden" id="erroruom" value="<spring:message code='error.nonsor.uom' />">
	<input type="hidden" value="${contractorMB.additionalMBDetails.size() }" id="additionalMBDetailsSize" />
	<div class="panel-body" id="nonSorHeaderTable">
		<table class="table table-bordered" id="tblNonSor">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.description" /><span class="mandatory" /></th>
					<th><spring:message code="lbl.uom" /><span class="mandatory" /></th>
					<th><spring:message code="lbl.rate" /><span class="mandatory" /></th>
					<th><spring:message code="lbl.current.entry" /><span class="mandatory" /></th>
					<th><spring:message code="lbl.amount" /></th>
					<th><spring:message code="lbl.delete" /></th>
				</tr>
			</thead>
			<tbody id="nonSorTable">
				<c:choose>
					<c:when test="${contractorMB.additionalMBDetails.size() == 0}">
						<tr id="nonSorMessage">
					</c:when>
					<c:otherwise>
						<tr id="nonSorMessage" hidden="true">
					</c:otherwise>
				</c:choose>
				<td colspan="8"><spring:message code="msg.additional.item.table"/></td>
				</tr>
				<c:choose>
					<c:when test="${contractorMB.additionalMBDetails.size() == 0 }">
						<tr id="nonSorRow" class="nonSorRow" nonsorinvisible="true" hidden="true" align="center">
							<td>
								<span class="spannonsorslno">1</span>
								<form:hidden path="additionalMBDetails[0].id" id="detailid_0" class="detailid" />
							</td>
							<td>
								<form:input path="additionalMBDetails[0].description" id="nonSorDesc_0" class="form-control table-input text-left nonSorDesc" maxlength="256"/>
							</td>
							<td>
								<form:select path="additionalMBDetails[0].uom" id="nonSorUom_0" data-idx="0" data-first-option="false" class="form-control nonSorUom">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<c:forEach items="${uoms }" var="uom">
										<form:option value="${uom.id }">${uom.uomCategory.category } -- ${uom.uom }</form:option>
									</c:forEach>
								</form:select>
							</td>
							<td align="right">
								<form:input path="additionalMBDetails[0].rate" id="nonSorRate_0"  class="rate form-control table-input text-right" />
							</td>

							<td>
							<div class="input-group" style="width:150px">
								<form:input path="additionalMBDetails[0].quantity" id="nonSorQuantity_0" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right nonSorQuantity" maxlength="64" onchange="calculateNonSorEstimateAmount(this);" onkeyup="validateQuantityInput(this);"/>
								<span class="input-group-addon openmbsheet" name="additionalMBDetails[0].msadd" id="additionalMBDetails[0].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"></i></span>
						    </div>
							</td>
                             <td hidden="true">
                             <input class="classmspresent" type="hidden" disabled="disabled" name="additionalMBDetails[0].mspresent" id="additionalMBDetails[0].mspresent" data-idx="0"/>
                             <input class="classmsopen" type="hidden" disabled="disabled" name="additionalMBDetails[0].msopen" id="additionalMBDetails[0].msopen" data-idx="0"/>
                            
                             <span name="additionalMBDetails[0].mstd" class="additionalMBDetails[0].mstd" id="additionalMBDetails[0].mstd" data-idx="0"></span>
                             
                             </td>
				 
							<td align="right">
								<form:input type="hidden" path="additionalMBDetails[0].amount" value="" id="hiddenNonSorAmount_0" class="form-control table-input text-right"/>
								<span class="nonSorAmount_0 nonsoramount"></span>
							</td>
							<td>
								<span class="add-padding delete_0" onclick="deleteNonSor(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${contractorMB.additionalMBDetails}" var="detail" varStatus="item">
							<tr id="nonSorRow" class="nonSorRow" align="center">
								<td>
									<span class="spannonsorslno">${item.index + 1 }</span>
									<form:hidden path="additionalMBDetails[${item.index }].id" id="detailid_${item.index }" class="detailid" value="${detail.id }" />
								</td>
								<td>
									<form:input path="additionalMBDetails[${item.index }].description" id="nonSorDesc_${item.index }" value="${detail.description }" class="form-control table-input text-left nonSorDesc" maxlength="256"/>
								</td>
								<td>
									<form:select path="additionalMBDetails[${item.index }].uom" id="nonSorUom_${item.index }" data-idx="${item.index }" data-first-option="false" class="form-control nonSorUom">
										<form:option value="">
											<spring:message code="lbl.select" />
										</form:option>
										<c:forEach items="${uoms }" var="uom">
											<c:if test="${uom.id == detail.uom.id }">
												<form:option value="${uom.id }" selected="selected" >${uom.uomCategory.category } -- ${uom.uom }</form:option>
											</c:if>
											<c:if test="${uom.id != detail.uom.id }">
												<form:option value="${uom.id }">${uom.uomCategory.category } -- ${uom.uom }</form:option>
											</c:if>
										</c:forEach>
									</form:select>
								</td>
								<td align="right">
									<form:input path="additionalMBDetails[${item.index }].rate" id="nonSorRate_${item.index }" class="rate form-control table-input text-right"  />
								</td>
								<td>
									<div class="input-group" style="width:150px">
									<form:input path="additionalMBDetails[${item.index }].quantity" id="nonSorQuantity_${item.index }" value="${detail.quantity }" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0"  class="form-control table-input text-right nonSorQuantity" maxlength="64" onchange="calculateNonSorEstimateAmount(this);" onkeyup="validateQuantityInput(this);" />
								     <span class="input-group-addon openmbsheet" name="additionalMBDetails[${item.index}].msadd" id="additionalMBDetails[${item.index}].msadd" data-idx="${item.index }" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true"  ></i></button>
							</div>
								</td>       

								<%@ include file="additional-items-msheet-edit.jsp"%>  
								
								<td align="right">
									<form:input type="hidden" path="additionalMBDetails[${item.index }].amount" value="${details.amount }" id="hiddenNonSorAmount_${item.index }" class="form-control table-input text-right"/>
									<span class="nonSorAmount_${item.index } nonsoramount"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2">${detail.rate * detail.quantity }</fmt:formatNumber></span>
								</td>
								<td>
									<span class="add-padding delete_${item.index }" onclick="deleteNonSor(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="5" class="text-right"><spring:message code="lbl.total" /></td>
					<td class="text-right"> <span id="nonSorEstimateTotal">0.00</span> </td>
					<td hidden="true" class="emptytd"></td>
					<td hidden="true" class="text-right nonSorServiceVatAmt"> <span id="nonSorServiceVatAmtTotal">0.00</span> </td>
					<td></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
 