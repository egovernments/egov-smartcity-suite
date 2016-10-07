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
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<div id="deductionHeaderTable" class="panel panel-primary" data-collapsed="0">
<div class="panel-heading custom_form_panel_heading" >
	<div class="panel-title">
	   <spring:message code="lbl.deductions" />
	</div>
</div>
<input type="hidden" value="<spring:message code="msg.abstractestimate.deduction.ammount" />" id="msgDeductionAmount" />
<input type="hidden" value="<spring:message code="msg.accountcode.empty" />" id="msgAccountCode" />
<input type="hidden" value="<spring:message code="msg.deduction.zero" />" id="msgAmountZero" />

<div class="panel-body">
		 <table class="table table-bordered" id="deductionTable">
			<thead>
				<tr>
					<th><spring:message code="lbl.account.code" /></th>
					<th><spring:message code="lbl.account.head"/></th>
					<th><spring:message code="lbl.percentage"/></th>
					<th><spring:message code="lbl.amtinrs"/></th>
					<th><spring:message code="lbl.action"/></th>
				</tr>
			</thead>
			<tbody>
			<c:choose>
				<c:when test="${abstractEstimate.tempDeductionValues.size() == 0}">
					<tr id="deductionRow">
						<td>
							<form:hidden path="tempDeductionValues[0].id" id="tempDeductionValues[0].id" class="dAEId"/>
							<input type="text" id="tempDeductionValues[0].accountCode" name="tempDeductionValues[0].accountCode"  data-idx="0" data-optional="0" class="form-control table-input text-left deductionAccountCode "  placeholder="Type first 3 letters of Account code" />
							<form:hidden path="tempDeductionValues[0].chartOfAccounts" id="tempDeductionValues[0].chartOfAccounts" class="deductionid"/>
						</td>
						<td>
							<input type="text" id="tempDeductionValues[0].accountHead" name="tempDeductionValues[0].accountHead"  data-idx="0" data-optional="0" class="form-control deductionAccountHead " disabled>  
						</td>
						<td>
							<form:input path="tempDeductionValues[0].percentage" id="tempDeductionValues[0].percentage" name="tempDeductionValues[0].percentage"  data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right deductionPercentage " maxlength="12" onblur="getDeductionAmountByPercentage();" />
							<form:errors path="tempDeductionValues[0].percentage" cssClass="add-margin error-msg" /> 
						</td> 
						<td>
							<form:input path="tempDeductionValues[0].amount" id="tempDeductionValues[0].amount" name="tempDeductionValues[0].amount"  data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right deductionAmount  " maxlength="12" onblur="calculateDeductionTotalAmount();" />
							<form:errors path="tempDeductionValues[0].amount" cssClass="add-margin error-msg" /> 
						</td>
					 	<td class="text-center">
							<button type="button" onclick="deleteDeductionRow(this);" class="btn btn-xs btn-secondary delete-row"><span class="glyphicon glyphicon-trash"></span> <spring:message code="lbl.delete"/></button>
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach items="${abstractEstimate.tempDeductionValues }" var="deductionDtls" varStatus="item">
					 	<tr id="deductionRow">
							<td>
								<form:hidden path="tempDeductionValues[${item.index}].id" id="tempDeductionValues[${item.index}].id" class="dAEId" value="${deductionDtls.id }" />
								<input type="text" id="tempDeductionValues[${item.index}].accountCode" name="tempDeductionValues[${item.index}].accountCode" data-idx="0" data-optional="0" class="form-control table-input text-left deductionAccountCode "  placeholder="Type first 3 letters of Account code" value="${deductionDtls.chartOfAccounts.glcode} ~ ${deductionDtls.chartOfAccounts.name}" />
								<form:hidden path="tempDeductionValues[${item.index}].chartOfAccounts" id="tempDeductionValues[${item.index}].chartOfAccounts" class="deductionid"/>
							</td>
							<td>
								<input type="text" id="tempDeductionValues[${item.index}].accountHead" name="tempDeductionValues[${item.index}].accountHead"  data-idx="0" data-optional="0" class="form-control deductionAccountHead " value="${deductionDtls.chartOfAccounts.name}" disabled>
								  
							</td>
							<td>
								<form:input path="tempDeductionValues[${item.index}].percentage" id="tempDeductionValues[${item.index}].percentage" name="tempDeductionValues[${item.index}].deductionPercentage"  data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right deductionPercentage " maxlength="12" onblur="getDeductionAmountByPercentage();" />
								<form:errors path="tempDeductionValues[${item.index}].percentage" cssClass="add-margin error-msg" /> 
							</td> 
							<td>
								<form:input path="tempDeductionValues[${item.index}].amount" id="tempDeductionValues[${item.index}].amount" name="tempDeductionValues[${item.index}].amount"  data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right deductionAmount " maxlength="12" onblur="calculateDeductionTotalAmount();" />
								<form:errors path="tempDeductionValues[${item.index}].amount" cssClass="add-margin error-msg" /> 
							</td> 
							<td class="text-center">
								<button type="button" onclick="deleteDeductionRow(this);" class="btn btn-xs btn-secondary delete-row"><span class="glyphicon glyphicon-trash"></span> <spring:message code="lbl.delete"/></button>
							</td>
						</tr> 
					</c:forEach>
				</c:otherwise>
				</c:choose> 
			</tbody>
 </table>
 <table class="table table-bordered" >
	<tr>
		<td width="66.5%" style="text-align:right"><spring:message code="lbl.total" /></td>
		<td class="text-right"> <span id="deductionTotalAmount">0.00</span> </td>
		<td width="8.8%"></td>
	</tr>
</table> 
<div class="panel-title">
			<div class="text-center">
				<a id="addDeductionRow" href="javascript:void(0);" class="btn btn-primary">
					<spring:message code="lbl.addrow" />
				</a>
			</div>
		</div>
</div>

</div>