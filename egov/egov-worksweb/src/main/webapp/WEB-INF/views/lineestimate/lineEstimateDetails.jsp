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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title"><spring:message code="lbl.workdetails" /></div>
	</div>
	<input type="hidden" value="${lineEstimate.lineEstimateDetails.size() }" id="detailsSize" />
	<div class="panel-body">
		<table class="table table-bordered" id="tblestimate">
			<thead>
				<tr>
					<th><spring:message code="lbl.slno"/></th>
					<th><spring:message code="lbl.nameofwork"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.estimatedamount"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.quantity"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.uom"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.expected.outcome"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.action"/></th>
				</tr>
			</thead>
			<tbody id="lineEstimateDetailsTbl">
				<c:choose>
					<c:when test="${lineEstimate.lineEstimateDetails.size() == 0}">
						<tr id="estimateRow">
							<td>
								<span class="spansno">1</span>
								<form:hidden path="lineEstimateDetails[0].id" name="lineEstimateDetails[0].id" value="${lineEstimateDetails[0].id}" class="form-control table-input hidden-input"/>
							</td>
							<td>
								<form:textarea path="lineEstimateDetails[0].nameOfWork" name="lineEstimateDetails[0].nameOfWork" value="${lineEstimateDetails[0].nameOfWork}" data-errormsg="Name of the work is mandatory!" data-idx="0" data-optional="0" class="form-control table-input" maxlength="1024" required="required"/>
								<form:errors path="lineEstimateDetails[0].nameOfWork" cssClass="add-margin error-msg" />
							</td>
							<td>
								<form:input path="lineEstimateDetails[0].estimateAmount" name="lineEstimateDetails[0].estimateAmount" value="${lineEstimateDetails[0].estimateAmount}" data-errormsg="Estimated amount is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right estimateAmount" onkeyup="calculateEstimatedAmountTotal();" onblur="calculateEstimatedAmountTotal();" required="required"/>
								<form:errors path="lineEstimateDetails[0].estimateAmount" cssClass="add-margin error-msg" />
							</td>
							<td>
								<form:input path="lineEstimateDetails[0].quantity" name="lineEstimateDetails[0].quantity" value="${lineEstimateDetails[0].quantity}" data-errormsg="quantity is mandatory!" data-idx="0" data-optional="0" class="form-control table-input text-right estimateAmount" maxlength="8" onkeyup="validateQuantity();" required="required"/>
								<form:errors path="lineEstimateDetails[0].quantity" cssClass="add-margin error-msg" />
							</td>
							<td>
								<form:input path="lineEstimateDetails[0].uom" name="lineEstimateDetails[0].uom" value="${lineEstimateDetails[0].uom}" data-errormsg="UOM is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input" maxlength="15"  required="required"/>
								<form:errors path="lineEstimateDetails[0].uom" cssClass="add-margin error-msg" />
							</td>
							<td>
								<form:input path="lineEstimateDetails[0].beneficiary" id="quantity" name="lineEstimateDetails[0].beneficiary" value="${lineEstimateDetails[0].beneficiary}" data-errormsg="Beneficiary is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input" maxlength="64" required="required"/>
								<form:errors path="lineEstimateDetails[0].beneficiary" cssClass="add-margin error-msg" />
							</td>
							<td> <span class="add-padding" onclick="deleteLineEstimate(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${lineEstimate.getLineEstimateDetails()}" var="lineEstimateDtls" varStatus="item">
							<tr id="estimateRow">
								<td> 
									<span class="spansno"><c:out value="${item.index + 1}" /></span>
									<form:hidden path="lineEstimateDetails[${item.index}].id" name="lineEstimateDetails[${item.index}].id" value="${lineEstimateDtls.id}" class="form-control table-input hidden-input"/>
								</td>
								<td>
									<form:textarea path="lineEstimateDetails[${item.index}].nameOfWork" name="lineEstimateDetails[${item.index}].nameOfWork" value="${lineEstimateDtls.nameOfWork}" data-errormsg="Name of the work is mandatory!" data-idx="0" data-optional="0" class="form-control table-input" maxlength="1024" required="required"/>
									<form:errors path="lineEstimateDetails[${item.index}].nameOfWork" cssClass="add-margin error-msg" />
								</td>
								<td>
									<form:input path="lineEstimateDetails[${item.index}].estimateAmount" name="lineEstimateDetails[${item.index}].estimateAmount" value="${lineEstimateDtls.estimateAmount}" data-errormsg="Estimated amount is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right estimateAmount" onkeyup="calculateEstimatedAmountTotal();" required="required"/>
									<form:errors path="lineEstimateDetails[${item.index}].estimateAmount" cssClass="add-margin error-msg" />
								</td>
								<td>
									<form:input path="lineEstimateDetails[${item.index}].quantity" name="lineEstimateDetails[${item.index}].quantity" value="${lineEstimateDtls.quantity}" onkeypress="" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right quantity"  onkeyup="validateQuantity();" required="required"/>
									<form:errors path="lineEstimateDetails[${item.index}].quantity" cssClass="add-margin error-msg" />
								</td>
								<td>
									<form:input path="lineEstimateDetails[${item.index}].uom" name="lineEstimateDetails[${item.index}].uom" value="${lineEstimateDtls.uom}" data-errormsg="UOM is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text" onkeyup="calculateEstimatedAmountTotal();" required="required"/>
									<form:errors path="lineEstimateDetails[${item.index}].uom" cssClass="add-margin error-msg" />
								</td>
								<td>
									<form:input path="lineEstimateDetails[${item.index}].beneficiary" name="lineEstimateDetails[${item.index}].beneficiary" value="${lineEstimateDtls.beneficiary}" data-errormsg="Beneficiary is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text" required="required"/>
									<form:errors path="lineEstimateDetails[${item.index}].beneficiary" cssClass="add-margin error-msg" />
								</td>
								<td> 
									<span class="add-padding" onclick="deleteLineEstimate(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> 
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
			<tfoot>
				<c:set var="total" value="${0}" scope="session"/>
				<c:if test="${lineEstimate.getLineEstimateDetails() != null}">
					<c:forEach items="${lineEstimate.getLineEstimateDetails()}" var="lineEstimateDtls">
						<c:set var="total" value="${total + lineEstimateDtls.estimateAmount}"/>
					</c:forEach>
				</c:if>
				<tr>
					<td colspan="2" class="text-right"><spring:message code="lbl.total" /></td>
					<td class="text-right"> <span id="estimateTotal"><c:out value="${total}"/></span> </td>
					<td></td>
				</tr>
			</tfoot>
		</table>
		<div id="documentDetails">
		</div>
		<div class="col-sm-12 text-center">
			<button id="addRowBtn" type="button" class="btn btn-primary" onclick="addLineEstimate()"><spring:message code="lbl.addrow" /></button>
		</div>
	</div>
</div>
