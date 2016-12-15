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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

		<div class="panel-heading custom_form_panel_heading">
			<div class="panel-title"><spring:message  code="lbl.debit.details"/></div>
		</div>
		<div class="panel-body">
		<table class="table table-bordered" id="tbldebitdetails">
			<thead>
				<tr>
					<th><spring:message code="lbl.account.code"/></th>
					<th><spring:message code="lbl.account.head"/></th>
					<th><spring:message code="lbl.debit.amount"/><span class="mandatory"></span></th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${billDetailsMap == null || !isDebitPresent}"> 
						<tr>
							<td>
								<input type="text" id="debitGlcode" value="${workOrderEstimate.estimate.financialDetails[0].budgetGroup.minCode.glcode}" class="form-control" readonly="readonly"required"> 
								<form:hidden path="billDetailes[0].glcodeid"  name="billDetailes[0].glcodeid" id="debitGlcodeId" value="${workOrderEstimate.estimate.financialDetails[0].budgetGroup.minCode.id}" /> 
								<form:errors path="billDetailes[0].glcodeid" cssClass="add-margin error-msg" />
							</td>
							<td>
								<input type="text" id="debitAccountHead" value="${workOrderEstimate.estimate.financialDetails[0].budgetGroup.minCode.name}" class="form-control" readonly="readonly" > 
							</td>
							<td>
								<c:choose>
									<c:when test="${isBillEditable == true }">
										<form:input path="billDetailes[0].debitamount" id="debitamount" name="billDetailes[0].debitamount" value="${debitBillDetails.amount }" data-errormsg="Debit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="1" class="form-control table-input text-right debitamount validateZero" onblur="calculateNetPayableAmount();calculateRetentionMoneyDeductionAmounts();" maxlength="12" required="required" readonly="true" />
									</c:when>
									<c:otherwise>
										<form:input path="billDetailes[0].debitamount" id="debitamount" name="billDetailes[0].debitamount" value="${debitBillDetails.amount }" data-errormsg="Debit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="1" class="form-control table-input text-right debitamount validateZero" onblur="calculateNetPayableAmount();calculateRetentionMoneyDeductionAmounts();" maxlength="12" required="required" />
									</c:otherwise>
								</c:choose>
								<form:errors path="billDetailes[0].debitamount" cssClass="add-margin error-msg" /> 
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${billDetailsMap}" var="debitBillDetails" varStatus="item">
							<c:if test="${debitBillDetails.isDebit && !debitBillDetails.isRefund}">
								<tr>
									<td>
										<%-- <form:hidden path="billDetailes[0].id" value="${debitBillDetails.id}"/> --%>
										<input type="text" id="debitGlcode" value="${debitBillDetails.glcode}" class="form-control" readonly="readonly" required="required"> 
										<form:hidden path="billDetailes[0].glcodeid"  name="billDetailes[0].glcodeid" id="debitGlcodeId" value="${debitBillDetails.glcodeId}" /> 
										<form:errors path="billDetailes[0].glcodeid" cssClass="add-margin error-msg" />
									</td>
									<td>
										<input type="text" id="debitAccountHead" value="${debitBillDetails.accountHead}" class="form-control" readonly="readonly" > 
									</td>
									<td>
										<c:choose>
											<c:when test="${isBillEditable == true }">
												<form:input path="billDetailes[0].debitamount" id="debitamount" name="billDetailes[0].debitamount" value="${debitBillDetails.amount }" data-errormsg="Debit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="1" class="form-control table-input text-right debitamount validateZero" onblur="calculateNetPayableAmount();calculateRetentionMoneyDeductionAmounts();" maxlength="12" required="required" readonly="true" />
											</c:when>
											<c:otherwise>
												<form:input path="billDetailes[0].debitamount" id="debitamount" name="billDetailes[0].debitamount" value="${debitBillDetails.amount }" data-errormsg="Debit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="1" class="form-control table-input text-right debitamount validateZero" onblur="calculateNetPayableAmount();calculateRetentionMoneyDeductionAmounts();" maxlength="12" required="required" />
											</c:otherwise>
										</c:choose>
										<form:errors path="billDetailes[0].debitamount" cssClass="add-margin error-msg" /> 
									</td>
								</tr>
							</c:if>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
</div>
