<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.refund" />
	</div>
	<div class="panel-body">
		<table class="table table-bordered" id="tblrefunddetails">
			<thead>
				<tr>
					<th><spring:message code="lbl.account.code"/></th>
					<th><spring:message code="lbl.account.head"/></th>
					<th><spring:message code="lbl.withheld.amount"/></th>
					<th><spring:message code="lbl.refundedsofar.amount"/></th>
					<th><spring:message code="lbl.refund.amount"/></th>
					<th><spring:message code="lbl.action"/></th> 					
				</tr>
			</thead>
         <tbody>
         <c:if test="${billDetailsMap == null || billDetailsMap.size() >= 2}">
         <c:set var="isRefundPresent" value="${false}" scope="session" />
         <c:set var="isCreditsPreset" value="${false}" scope="session" />
         <c:forEach items="${billDetailsMap}" var="billDetail" varStatus="item" >
             <c:if test="${billDetail.isDebit && !billDetail.isNetPayable && billDetail.isRefund}">
                <c:set var="isRefundPresent" value="${true}" scope="session" />
             </c:if>
              <c:if test="${!billDetail.isDebit && !billDetail.isNetPayable}">
                <c:set var="isCreditsPreset" value="${true}" scope="session" />
             </c:if>
         </c:forEach>
         </c:if>
		 <c:choose>
		 <c:when test="${billDetailsMap == null || !isRefundPresent}">
		 <tr id="refundRow">
		 <td>
		   <form:select path="refundBillDetails[0].glcodeid" data-first-option="false" name="refundBillDetails[0].refundAccountCode" value="" id="refundBillDetails[0].refundAccountCode" class="form-control refundpurpose" data-errormsg="Account Code is mandatory!" data-optional="0" data-idx="0">
				<form:option value="">
					<spring:message code="lbl.select" />
				</form:option>
				<c:forEach var="coa" items="${refundAccounCodes}">
					<form:option value="${coa.id}">
						<c:out value="${coa.glcode} - ${coa.name}" />
					</form:option>
				</c:forEach></form:select>
		   <form:hidden path="refundBillDetails[0].glcodeid"  name="refundBillDetails[0].glcodeid" id="refundBillDetails[0].glcodeid" value="" /> 
		</td>
		<td>
			<input type="text" id="refundBillDetails[0].refundAccountHead" name="refundBillDetails[0].refundAccountHead" value="" class="form-control refundaccountheadname" disabled>  
		</td>
		<td>
			<input type="text" id="refundBillDetails[0].withHeldAmount" name="refundBillDetails[0].withHeldAmount" value="" class="form-control text-right withheldamount" onkeyup="decimalvalue(this);" disabled>  
		</td>
		<td>
			<input type="text" id="refundBillDetails[0].refundedAmount" name="refundBillDetails[0].refundedAmount" value="" class="form-control text-right refundedamount" onkeyup="decimalvalue(this);" disabled>  
		</td>
		<td>
		   <form:input path="" id="refundBillDetails[0].debitamount" name="refundBillDetails[0].debitamount" value="" data-errormsg="Refund Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right refundAmount validateZero" onblur="calculateNetPayableAmount();"  maxlength="12" />
		   <form:errors path="refundBillDetails[0].debitamount" cssClass="add-margin error-msg" /> 
		</td>
		    <td class="text-center"><span style="cursor:pointer;" onclick="addRefundRow();"><i class="fa fa-plus"></i></span>
		    <span class="add-padding" onclick="deleteRefundRow(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
		</tr>
		 </c:when>
		 <c:otherwise>
		 <c:set var="index" value="${0}" scope="session"/>
			<c:forEach items="${billDetailsMap}" var="billDetail" varStatus="item">
			<c:if test="${billDetail.isDebit && !billDetail.isNetPayable && billDetail.isRefund}">
				<tr id="refundRow">
				<td>
				<form:select path="refundBillDetails[${index}].glcodeid" data-first-option="false" name="refundBillDetails[${index}].refundAccountCode" value="${billDetail.glcodeId}" id="refundBillDetails[${index}].refundAccountCode" class="form-control refundpurpose" data-idx="${index}" data-errormsg="Account Code is mandatory!" data-optional="0" >
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<c:forEach var="coa" items="${refundAccounCodes}">
							<form:option value="${coa.id}">
								<c:out value="${coa.glcode} - ${coa.name}" />
							</form:option>
						</c:forEach></form:select>
				<form:hidden path="refundBillDetails[${index}].glcodeid" name="refundBillDetails[${index}].glcodeid" value="${billDetail.glcodeId}" id="refundBillDetails[${index}].glcodeid" class="form-control table-input hidden-input refundglcodeid"/>
				</td>
				<td>
					<input type="text" id="refundBillDetails[${index}].refundAccountHead" name="refundBillDetails[${index}].refundAccountHead" value="${billDetail.accountHead}" class="form-control refundaccountheadname" disabled>  
				</td>
				<td>
			        <input type="text" id="refundBillDetails[${index}].withHeldAmount" name="refundBillDetails[${index}].withHeldAmount" value="${billDetail.withHeldAmount}" class="form-control text-right withheldamount" onkeyup="decimalvalue(this);" disabled>  
		        </td>
		        <td>
			        <input type="text" id="refundBillDetails[${index}].refundedAmount" name="refundBillDetails[${index}].refundedAmount" value="${billDetail.RefundedAmount}" class="form-control text-right refundedamount" onkeyup="decimalvalue(this);" disabled>  
		        </td>
				<td>
					<form:input path="refundBillDetails[${index}].debitamount" id="refundBillDetails[${index}].debitamount" name="refundBillDetails[${rowIndex}].debitamount" value="${billDetail.amount}" data-errormsg="Refund Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="${index}" data-optional="0" class="form-control table-input text-right refundAmount validateZero" onblur="calculateNetPayableAmount();"  maxlength="12" />
					<form:errors path="refundBillDetails[${index}].debitamount" cssClass="add-margin error-msg" /> 
				</td> 
				   <td class="text-center"><span style="cursor:pointer;" onclick="addRefundRow();"><i class="fa fa-plus"></i></span>
				   <span class="add-padding" onclick="deleteRefundRow(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
				</tr>
				   <c:set var="index" value="${index+1}" scope="session" />
				</c:if>
		</c:forEach>
		</c:otherwise>
		</c:choose>
		</tbody>
</table>
</div>
</div>
