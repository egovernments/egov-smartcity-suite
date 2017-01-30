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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<form:hidden path="" name="retentionMoneyPerForPartBill" id="retentionMoneyPerForPartBill" value="${retentionMoneyPerForPartBill }" class="form-control table-input hidden-input"/>
<form:hidden path="" name="retentionMoneyPerForFinalBill" id="retentionMoneyPerForFinalBill" value="${retentionMoneyPerForFinalBill }" class="form-control table-input hidden-input"/>
<input id="erroradvancegreaterpaid" type="hidden" value="<spring:message code="error.advance.greater.paid" />" />
<input id="erroradvancegreaterbillamount" type="hidden" value="<spring:message code="error.advance.greater.billamount" />" />
<input id="erroradjustedgreaterpaid" type="hidden" value="<spring:message code="error.adjusted.greater.paid" />" />
<input id="errorfinaladjustedzero" type="hidden" value="<spring:message code="error.finalbill.adjusted.zero" />" />
<input id="errorfinaladjustremaining" type="hidden" value="<spring:message code="error.finalbill.adjust.remaining" />" />
<c:set var="isStatutaryDeductionsPresent" value="${false}" scope="session" />
<c:set var="isOtherDeductionsPresent" value="${false}" scope="session" />
<c:set var="isRetentionMoneyDeductionsPresent" value="${false}" scope="session" />
<c:set var="isContractorAdvanceDeductionPresent" value="${false}" scope="session" />
<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.credit.details" />
	</div>
</div>


<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.statutorydeductions" />
	</div>
	</div>
	<input type="hidden" value="${billDetailsMap == null ? contractorBillRegister.statutoryDeductionDetailes.size() : billDetailsMap.size() - 2}" id="detailsSize" /> 
		<table class="table table-bordered" id="tblstatutorydeductioncreditdetails">
			<thead>
				<tr>
					<th><spring:message code="lbl.account.code"/></th>
					<th><spring:message code="lbl.credit.amount"/></th>
					<th><spring:message code="lbl.action"/></th> 					
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${billDetailsMap != null && billDetailsMap.size() >= 1}">			
						<c:forEach items="${billDetailsMap}" var="billDetail" varStatus="item" >	
							<c:if test="${!billDetail.isDebit && !billDetail.isNetPayable && !billDetail.isRetentionMoneyDeduction && billDetail.isStatutoryDeduction && !billDetail.isContractorAdvanceDeduction}">
								<c:set var="isStatutaryDeductionsPresent" value="${true}" scope="session" />
							</c:if>	
							<c:if test="${!billDetail.isDebit && !billDetail.isNetPayable && !billDetail.isRetentionMoneyDeduction && !billDetail.isStatutoryDeduction && !billDetail.isContractorAdvanceDeduction}">
								<c:set var="isOtherDeductionsPresent" value="${true}" scope="session" />
							</c:if>	
							<c:if test="${!billDetail.isDebit && !billDetail.isNetPayable && billDetail.isRetentionMoneyDeduction && !billDetail.isStatutoryDeduction && !billDetail.isContractorAdvanceDeduction}">
								<c:set var="isRetentionMoneyDeductionsPresent" value="${true}" scope="session" />
							</c:if>
							<c:if test="${!billDetail.isDebit && !billDetail.isNetPayable && !billDetail.isRetentionMoneyDeduction && !billDetail.isStatutoryDeduction && billDetail.isContractorAdvanceDeduction}">
								<c:set var="isContractorAdvanceDeductionPresent" value="${true}" scope="session" />
							</c:if>	
						</c:forEach>	
					</c:when>
					<c:otherwise>							
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${billDetailsMap == null || !isStatutaryDeductionsPresent}">
						<tr id="statutorydeductionrow">
							<td> <form:select path="statutoryDeductionDetailes[0].glcodeid" name="statutoryDeductionDetailes[0].creditGlcode" id="statutoryDeductionDetailes[0].creditGlcode"  data-errormsg="Account Code is mandatory!" data-idx="0" data-optional="0"  class="form-control table-input creditGlcode" onchange="resetCreditAccountDetails(this);" >
									<form:option value=""> <spring:message code="lbl.select" /> </form:option>
										<c:forEach var="coa" items="${statutoryDeductionAccounCodes}">
											<form:option value="${coa.id}">
												<c:out value="${coa.glcode} - ${coa.name}" />
											</form:option>
										</c:forEach>
								</form:select>
								<form:hidden path="statutoryDeductionDetailes[0].glcodeid"  name="statutoryDeductionDetailes[0].glcodeid" id="statutoryDeductionDetailes[0].glcodeid" value="${egBilldetailes.glcodeid}" class="form-control table-input hidden-input statutorydeductionid"/> 
								<form:errors path="statutoryDeductionDetailes[0].glcodeid" cssClass="add-margin error-msg" /> 
							</td>
							<td>
								<form:input path="statutoryDeductionDetailes[0].creditamount" id="statutoryDeductionDetailes[0].creditamount" name="statutoryDeductionDetailes[0].creditamount" data-errormsg="Credit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right creditAmount validateZero" onblur="calculateNetPayableAmount();"  maxlength="12"  />
								<form:errors path="statutoryDeductionDetailes[0].creditamount" cssClass="add-margin error-msg" /> 
							</td> 
							<td class="text-center"><span style="cursor:pointer;" onclick="addStatutoryDeductionRow();"><i class="fa fa-plus"></i></span>
							 <span class="add-padding" onclick="deleteStatutoryDeductionRow(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
						</tr>
					</c:when>
					<c:otherwise>						
						<c:set var="rowIndex" value="${0}" scope="session" />
						<c:forEach items="${billDetailsMap }" var="billDetail" varStatus="item" >
							<c:if test="${!billDetail.isDebit && !billDetail.isNetPayable && !billDetail.isRetentionMoneyDeduction && billDetail.isStatutoryDeduction && !billDetail.isContractorAdvanceDeduction}">
								<tr id="statutorydeductionrow">
									<td>
										<form:select path="statutoryDeductionDetailes[${rowIndex }].glcodeid" data-first-option="false" name="statutoryDeductionDetailes[${rowIndex }].creditGlcode" id="statutoryDeductionDetailes[${rowIndex }].creditGlcode"  class="form-control table-input" onchange="resetCreditAccountDetails(this);">
											<form:option value=""> <spring:message code="lbl.select" /> </form:option>
											<c:forEach var="coa" items="${statutoryDeductionAccounCodes}">
												<c:if test="${billDetail.glcodeId == coa.id }">
													<form:option value="${coa.id}" selected="selected">
														<c:out value="${coa.glcode} - ${coa.name}" />
													</form:option>
												</c:if>
												<c:if test="${billDetail.glcodeId != coa.id }">
													<form:option value="${coa.id}">
														<c:out value="${coa.glcode} - ${coa.name}" />
													</form:option>
												</c:if>
											</c:forEach>
										</form:select> 
										<form:hidden path="statutoryDeductionDetailes[${rowIndex }].glcodeid" name="statutoryDeductionDetailes[${rowIndex }].glcodeid" value="${billDetail.glcodeId}" id="statutoryDeductionDetailes[${rowIndex }].glcodeid" class="form-control table-input hidden-input creditglcodeid"/> 
										<form:errors path="statutoryDeductionDetailes[${rowIndex }].glcodeid" cssClass="add-margin error-msg" /> 
									</td>
									<td>
										<form:input path="statutoryDeductionDetailes[${rowIndex }].creditamount" id="statutoryDeductionDetailes[${rowIndex }].creditamount" name="statutoryDeductionDetailes[${rowIndex }].creditamount" value="${billDetail.amount }" data-errormsg="Credit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right creditAmount validateZero" onblur="calculateNetPayableAmount();"  maxlength="12"  />
										<form:errors path="statutoryDeductionDetailes[${rowIndex }].creditamount" cssClass="add-margin error-msg" /> 
									</td> 
									<td class="text-center"><span style="cursor:pointer;" onclick="addStatutoryDeductionRow();"><i class="fa fa-plus"></i></span>
									 <span class="add-padding" onclick="deleteStatutoryDeductionRow(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
								</tr>
								<c:set var="rowIndex" value="${rowIndex+1}" scope="session" />
							</c:if>							
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>

<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.otherdeductions" />
	</div>
	</div>
	<input type="hidden" value="${billDetailsMap == null ? contractorBillRegister.statutoryDeductionDetailes.size() : billDetailsMap.size() - 2}" id="detailsSize" /> 
		<table class="table table-bordered" id="tblotherdeductioncreditdetails">
			<thead>
				<tr>
					<th><spring:message code="lbl.account.code"/></th>
					<th><spring:message code="lbl.account.head"/></th>
					<th><spring:message code="lbl.credit.amount"/></th>
					<th><spring:message code="lbl.action"/></th> 					
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${billDetailsMap == null || !isOtherDeductionsPresent}">
						<tr id="otherdeductionrow">
							<td>
								<input type="text" id="otherDeductionDetailes[0].creditGlcode" name="otherDeductionDetailes[0].creditGlcode" class="form-control table-input otherDeductionCreditGlcode creditGlcode"  data-errormsg="Account Code is mandatory!" data-idx="0" data-optional="0"   placeholder="Type first 3 letters of Account code" onblur="resetCreditAccountDetails(this);"> 
								<form:hidden path="otherDeductionDetailes[0].glcodeid"  name="otherDeductionDetailes[0].glcodeid" id="otherDeductionDetailes[0].glcodeid" value="${egBilldetailes.glcodeid}" class="form-control table-input hidden-input otherdeductionid"/> 
								<form:errors path="otherDeductionDetailes[0].glcodeid" cssClass="add-margin error-msg" /> 
							</td>
							<td>
								<input type="text" id="otherDeductionDetailes[0].creditAccountHead" name="otherDeductionDetailes[0].creditAccountHead" value="${otherDeductionDetailes[0].creditAccountHead}" class="form-control otherdeductionname" disabled>  
							</td>
							<td>
								<form:input path="otherDeductionDetailes[0].creditamount" id="otherDeductionDetailes[0].creditamount" name="otherDeductionDetailes[0].creditamount" data-errormsg="Credit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right creditAmount validateZero" onblur="calculateNetPayableAmount();"  maxlength="12"  />
								<form:errors path="otherDeductionDetailes[0].creditamount" cssClass="add-margin error-msg" /> 
							</td> 
							<td class="text-center"><span style="cursor:pointer;" onclick="addOtherDeductionRow();"><i class="fa fa-plus"></i></span>
							 <span class="add-padding" onclick="deleteOtherDeductionRow(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
						</tr>
					</c:when>
					<c:otherwise>

						<c:set var="rowIndex" value="${0}" scope="session" />
						<c:forEach items="${billDetailsMap }" var="billDetail" varStatus="item" >						
							<c:if test="${!billDetail.isDebit && !billDetail.isNetPayable && !billDetail.isStatutoryDeduction && !billDetail.isRetentionMoneyDeduction && !billDetail.isContractorAdvanceDeduction}">
								<tr id="otherdeductionrow">
									<td>
										<input type="text" id="otherDeductionDetailes[${rowIndex }].creditGlcode" name="otherDeductionDetailes[${rowIndex }].creditGlcode" value="${billDetail.glcode} ~ ${billDetail.accountHead}" class="form-control table-input otherDeductionCreditGlcode creditGlcode" data-errormsg="Account Code is mandatory!" data-idx="0" data-optional="0"  placeholder="Type first 3 letters of Account code" onblur="resetCreditAccountDetails(this);"> 
										<form:hidden path="otherDeductionDetailes[${rowIndex }].glcodeid" name="otherDeductionDetailes[${rowIndex }].glcodeid" value="${billDetail.glcodeId}" id="otherDeductionDetailes[${rowIndex }].glcodeid" class="form-control table-input hidden-input otherdeductionid"/> 
										<form:errors path="otherDeductionDetailes[${rowIndex }].glcodeid" cssClass="add-margin error-msg" /> 
									</td>
									<td>
										<input type="text" id="otherDeductionDetailes[${rowIndex }].creditAccountHead" name="otherDeductionDetailes[${rowIndex }].creditAccountHead" value="${billDetail.accountHead}" class="form-control otherdeductionname" disabled>  
									</td>
									<td>
										<form:input path="otherDeductionDetailes[${rowIndex }].creditamount" id="otherDeductionDetailes[${rowIndex }].creditamount" name="otherDeductionDetailes[${rowIndex }].creditamount" value="${billDetail.amount }" data-errormsg="Credit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right creditAmount validateZero" onblur="calculateNetPayableAmount();"  maxlength="12"  />
										<form:errors path="otherDeductionDetailes[${rowIndex }].creditamount" cssClass="add-margin error-msg" /> 
									</td> 
									<td class="text-center"><span style="cursor:pointer;" onclick="addOtherDeductionRow();"><i class="fa fa-plus"></i></span>
									 <span class="add-padding" onclick="deleteOtherDeductionRow(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
								</tr>								
								<c:set var="rowIndex" value="${rowIndex+1}" scope="session" />
							</c:if>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>

<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.retentionmoneydeductions" />
	</div>
	</div>
	<input type="hidden" value="${billDetailsMap == null ? contractorBillRegister.statutoryDeductionDetailes.size() : billDetailsMap.size() - 2}" id="detailsSize" /> 
		<table class="table table-bordered" id="tblretentionmoneydeductioncreditdetails">
			<thead>
				<tr>
					<th><spring:message code="lbl.account.code"/></th>
					<th><spring:message code="lbl.credit.amount"/></th>
					<th><spring:message code="lbl.action"/></th> 					
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${billDetailsMap == null || !isRetentionMoneyDeductionsPresent}">
						<tr id="retentionmoneydeductionrow">
							<td> <form:select path="retentionMoneyDeductionDetailes[0].glcodeid" name="retentionMoneyDeductionDetailes[0].creditGlcode" id="retentionMoneyDeductionDetailes[0].creditGlcode"  data-errormsg="Account Code is mandatory!" data-idx="0" data-optional="0"  class="form-control table-input creditGlcode"  onchange="resetCreditAccountDetails(this);calculateRetentionMoneyDeductionAmount(this);calculateNetPayableAmount();">
									<form:option value=""> <spring:message code="lbl.select" /> </form:option>
										<c:forEach var="coa" items="${retentionMoneyDeductionAccounCodes}">
											<form:option value="${coa.id}">
												<c:out value="${coa.glcode} - ${coa.name}" />
											</form:option>
										</c:forEach>
								</form:select>
								<form:hidden path="retentionMoneyDeductionDetailes[0].glcodeid"  name="retentionMoneyDeductionDetailes[0].glcodeid" id="retentionMoneyDeductionDetailes[0].glcodeid" value="${egBilldetailes.glcodeid}" class="form-control table-input hidden-input statutorydeductionid"/> 
								<form:errors path="retentionMoneyDeductionDetailes[0].glcodeid" cssClass="add-margin error-msg" /> 
							</td>
							<td>
								<form:input path="retentionMoneyDeductionDetailes[0].creditamount" id="retentionMoneyDeductionDetailes[0].creditamount" name="retentionMoneyDeductionDetailes[0].creditamount" data-errormsg="Credit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right creditAmount validateZero" onblur="calculateNetPayableAmount();"  maxlength="12"  />
								<form:errors path="retentionMoneyDeductionDetailes[0].creditamount" cssClass="add-margin error-msg" /> 
							</td> 
							<td class="text-center"><span style="cursor:pointer;" onclick="addRetentionMoneyDeductionRow();"><i class="fa fa-plus"></i></span>
							 <span class="add-padding" onclick="deleteRetentionMoneyDeductionRow(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
						</tr>
					</c:when>
					<c:otherwise>					
						<c:set var="rowIndex" value="${0}" scope="session" />
						<c:forEach items="${billDetailsMap }" var="billDetail" varStatus="item" >
							<c:if test="${!billDetail.isDebit && !billDetail.isNetPayable && !billDetail.isStatutoryDeduction && billDetail.isRetentionMoneyDeduction && !billDetail.isContractorAdvanceDeduction}">
								<tr id="retentionmoneydeductionrow">
									<td>
										<form:select path="retentionMoneyDeductionDetailes[${rowIndex }].glcodeid" data-first-option="false" name="retentionMoneyDeductionDetailes[${rowIndex }].creditGlcode" id="retentionMoneyDeductionDetailes[${rowIndex }].creditGlcode"  class="form-control table-input" onchange="resetCreditAccountDetails(this);calculateRetentionMoneyDeductionAmount(this);calculateNetPayableAmount();">
											<form:option value=""> <spring:message code="lbl.select" /> </form:option>
											<c:forEach var="coa" items="${retentionMoneyDeductionAccounCodes}">
												<c:if test="${billDetail.glcodeId == coa.id }">
													<form:option value="${coa.id}" selected="selected">
														<c:out value="${coa.glcode} - ${coa.name}" />
													</form:option>
												</c:if>
												<c:if test="${billDetail.glcodeId != coa.id }">
													<form:option value="${coa.id}">
														<c:out value="${coa.glcode} - ${coa.name}" />
													</form:option>
												</c:if>
											</c:forEach>
										</form:select> 
										<form:hidden path="retentionMoneyDeductionDetailes[${rowIndex }].glcodeid" name="retentionMoneyDeductionDetailes[${rowIndex }].glcodeid" value="${billDetail.glcodeId}" id="retentionMoneyDeductionDetailes[${rowIndex }].glcodeid" class="form-control table-input hidden-input creditglcodeid"/> 
										<form:errors path="retentionMoneyDeductionDetailes[${rowIndex }].glcodeid" cssClass="add-margin error-msg" /> 
									</td>
									<td>
										<form:input path="retentionMoneyDeductionDetailes[${rowIndex }].creditamount" id="retentionMoneyDeductionDetailes[${rowIndex }].creditamount" name="retentionMoneyDeductionDetailes[${rowIndex }].creditamount" value="${billDetail.amount }" data-errormsg="Credit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right creditAmount validateZero" onblur="calculateNetPayableAmount();"  maxlength="12"  />
										<form:errors path="retentionMoneyDeductionDetailes[${rowIndex }].creditamount" cssClass="add-margin error-msg" /> 
									</td> 
									<td class="text-center"><span style="cursor:pointer;" onclick="addRetentionMoneyDeductionRow();"><i class="fa fa-plus"></i></span>
									 <span class="add-padding" onclick="deleteRetentionMoneyDeductionRow(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
								</tr>
								<c:set var="rowIndex" value="${rowIndex+1}" scope="session" />
							</c:if>							
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		
<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.advance.adjustments" />
	</div>
</div>
<div>
	<table class="table table-bordered" id="advanceDetails">
		<thead>
			<tr>
				<th><spring:message code="lbl.advance.paid" /></th>
				<th><spring:message code="lbl.advance.adjusted" /></th>
				<th><spring:message code="lbl.balance.adjusted" /></th>
			</tr>
		</thead>
		<tbody>
			<tr id="advancedetailsrow">
				<td align="right" id="advancePaid"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
								minFractionDigits="2" value="${advancePaidTillNow }" /></td>
				<td align="right" id="advanceAdjusted"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
								minFractionDigits="2" value="${advanceAdjustedSoFar }" /></td>
				<td align="right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
								minFractionDigits="2" value="${advancePaidTillNow - advanceAdjustedSoFar }" /></td>
			</tr>
		</tbody>
	</table>
</div>
<table class="table table-bordered" id="tbladvanceadjustments">
	<thead>
		<tr>
			<th><spring:message code="lbl.account.code"/></th>
			<th><spring:message code="lbl.credit.amount"/></th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${billDetailsMap == null || !isContractorAdvanceDeductionPresent}">
				<tr id="advanceadjustmentsrow">
					<td> <form:select path="advanceAdjustmentDetails[0].glcodeid" name="advanceAdjustmentDetails[0].glcodeid" id="advanceAdjustmentDetails[0].glcodeid"  data-errormsg="Account Code is mandatory!" data-idx="0" data-optional="0"  class="form-control table-input creditGlcode"  onchange="calculateNetPayableAmount();">
							<form:option value=""> <spring:message code="lbl.select" /> </form:option>
								<c:forEach var="coa" items="${contractorAdvanceAccountCodes}">
									<form:option value="${coa.id}">
										<c:out value="${coa.glcode} - ${coa.name}" />
									</form:option>
								</c:forEach>
						</form:select>
						<form:hidden path="advanceAdjustmentDetails[0].glcodeid"  name="advanceAdjustmentDetails[0].glcodeid" id="retentionMoneyDeductionDetailes[0].glcodeid" value="${egBilldetailes.glcodeid}" class="form-control table-input hidden-input contractoradvanceglcodeid"/> 
						<form:errors path="advanceAdjustmentDetails[0].glcodeid" cssClass="add-margin error-msg" /> 
					</td>
					<td>
						<form:input path="advanceAdjustmentDetails[0].creditamount" id="advanceAmount" name="advanceAdjustmentDetails[0].creditamount" data-errormsg="Advance Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right creditAmount validateZero" onblur="calculateNetPayableAmount();"  maxlength="12"  />
						<form:errors path="advanceAdjustmentDetails[0].creditamount" cssClass="add-margin error-msg" /> 
					</td> 
				</tr>
			</c:when>
			<c:otherwise>					
				<c:set var="rowIndex" value="${0}" scope="session" />
				<c:forEach items="${billDetailsMap }" var="billDetail" varStatus="item" >
					<c:if test="${!billDetail.isDebit && !billDetail.isNetPayable && !billDetail.isOtherDeductionsPresent && !billDetail.isStatutoryDeduction && !billDetail.isRetentionMoneyDeduction && billDetail.isContractorAdvanceDeduction}">
						<tr id="advanceadjustmentsrow">
							<td>
								<form:select path="advanceAdjustmentDetails[${rowIndex }].glcodeid" data-first-option="false" name="advanceAdjustmentDetails[${rowIndex }].glcodeid" id="advanceAdjustmentDetails[${rowIndex }].glcodeid"  class="form-control table-input" onchange="calculateNetPayableAmount();">
									<form:option value=""> <spring:message code="lbl.select" /> </form:option>
									<c:forEach var="coa" items="${contractorAdvanceAccountCodes}">
										<c:if test="${billDetail.glcodeId == coa.id }">
											<form:option value="${coa.id}" selected="selected">
												<c:out value="${coa.glcode} - ${coa.name}" />
											</form:option>
										</c:if>
										<c:if test="${billDetail.glcodeId != coa.id }">
											<form:option value="${coa.id}">
												<c:out value="${coa.glcode} - ${coa.name}" />
											</form:option>
										</c:if>
									</c:forEach>
								</form:select> 
								<form:hidden path="advanceAdjustmentDetails[${rowIndex }].glcodeid" name="advanceAdjustmentDetails[${rowIndex }].glcodeid" value="${billDetail.glcodeId}" id="advanceAdjustmentDetails[${rowIndex }].glcodeid" class="form-control table-input hidden-input contractoradvanceglcodeid"/> 
								<form:errors path="advanceAdjustmentDetails[${rowIndex }].glcodeid" cssClass="add-margin error-msg" /> 
							</td>
							<td>
								<form:input path="advanceAdjustmentDetails[${rowIndex }].creditamount" id="advanceAmount" name="advanceAdjustmentDetails[${rowIndex }].creditamount" value="${billDetail.amount }" data-errormsg="Advance Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right creditAmount validateZero" onblur="calculateNetPayableAmount();"  maxlength="12"  />
								<form:errors path="advanceAdjustmentDetails[${rowIndex }].creditamount" cssClass="add-margin error-msg" /> 
							</td> 
						</tr>
						<c:set var="rowIndex" value="${rowIndex+1}" scope="session" />
					</c:if>							
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>

<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.netpayable" />
	</div>
</div>

<div style="padding: 0 15px;">
	<table class="table table-bordered" id="tblnetpayable">
		<thead>
			<tr>
				<th><spring:message code="lbl.account.code" /><span
					class="mandatory"></span></th>
				<%-- 	<th><spring:message code="lbl.account.head"/></th> --%>
				<th><spring:message code="lbl.credit.amount" /></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>
					<form:hidden path="" name="netPayableAccountId" id="netPayableAccountId" value="${netPayableAccountId}"/>
					<form:hidden path="" name="netPayableAccountCodeId" id="netPayableAccountCodeId" value="${netPayableAccountCode}"/>
					<c:choose>
						<c:when test="${isBillEditable == true }">
							<form:select path="" data-first-option="false"
								name="netPayableAccountCode" id="netPayableAccountCode"
								class="form-control" required="required" disabled="true">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<c:forEach var="coa" items="${netPayableAccounCodes}">
									<form:option value="${coa.id}">
										<c:out value="${coa.glcode} - ${coa.name}" />
									</form:option>
								</c:forEach>
							</form:select>
						</c:when>
						<c:otherwise>
							<form:select path="" data-first-option="false"
								name="netPayableAccountCode" id="netPayableAccountCode"
								class="form-control" required="required">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<c:forEach var="coa" items="${netPayableAccounCodes}">
									<form:option value="${coa.id}">
										<c:out value="${coa.glcode} - ${coa.name}" />
									</form:option>
								</c:forEach>
								<%-- <form:options items="${netPayableAccounCodes}" itemLabel="glcode" itemValue="id" />  --%>
							</form:select> <%-- <form:errors path="netPayableAccountCode" cssClass="add-margin error-msg" /> --%>
						</c:otherwise>
					</c:choose>
				</td>
				<!-- <td> 
						<input type="text" id="creditAccountHead" class="form-control" disabled> 
					</td> -->
				<td><input type="text" id="netPayableAmount"
					name="netPayableAmount" value="${netPayableAmount}"
					class="form-control text-right" readonly="true"> <%-- <form:errors path="netPayableAmount" cssClass="add-margin error-msg" /> --%>
				</td>
			</tr>
		</tbody>
	</table>
</div>
