<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message  code="lbl.credit.details"/>
		</div>
	</div>
	
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message  code="lbl.deductions"/>
		</div>
	</div>
	<input type="hidden" value="${billDetailes.size()}" id="detailsSize" /> 
	<div class="panel-body">
		<table class="table table-bordered" id="tblcreditdetails">
			<thead>
				<tr>
					<th><spring:message code="lbl.account.code"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.account.head"/></th>
					<th><spring:message code="lbl.credit.amount"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.action"/></th> 					
				</tr>
			</thead>
			<tbody>
				<tr id="deductionRow">
					<td>
						<input type="text" id="creditGlcode" name="billDetailes[1].creditGlcode" class="form-control table-input patternvalidation creditGlcode" data-pattern="number" data-errormsg="Account Code is mandatory!" data-idx="0" data-optional="0" maxlength="9" required="required" placeholder="Type first 3 letters of Account code"> 
						<form:hidden path="billDetailes[1].glcodeid"  name="billDetailes[1].glcodeid" id="billDetailes[1].creditglcodeid" value="${egBilldetailes.glcodeid}" class="form-control table-input hidden-input creditglcodeid"/> 
						<form:errors path="billDetailes[1].glcodeid" cssClass="add-margin error-msg" /> 
					</td>
					<td>
						<input type="text" id="billDetailes[1].creditAccountHead" name="billDetailes[1].creditAccountHead" value="${billDetailes[1].creditAccountHead}" class="form-control creditAccountHead" disabled> 
					</td>
					<td>
						<form:input path="billDetailes[1].creditamount" id="billDetailes[1].creditamount" name="billDetailes[1].creditamount" data-errormsg="Credit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right creditAmount" onblur="calculateNetPayableAmount();"  maxlength="12" required="required" />
						<form:errors path="billDetailes[1].creditamount" cssClass="add-margin error-msg" /> 
					</td> 
					<td class="text-center"><span style="cursor:pointer;" onclick="addDeductionRow();"><i class="fa fa-plus"></i></span>
					 <span class="add-padding" onclick="deleteDeductionRow(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
				</tr>
			</tbody>
		</table>
	<%-- 	<div class="col-sm-12 text-center">
			<button id="addRowBtn" type="button" class="btn btn-primary" onclick="addDeductionRow()"><spring:message code="lbl.addrow" /></button>
		</div> --%>
	</div>
	
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message  code="lbl.netpayable"/>
		</div>
	</div>
	<div class="panel-body">
		<table class="table table-bordered" id="tblnetpayable">
			<thead>
				<tr>
					<th><spring:message code="lbl.account.code"/><span class="mandatory"></span></th>
				<%-- 	<th><spring:message code="lbl.account.head"/></th> --%>
					<th><spring:message code="lbl.credit.amount"/></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>
						<form:select path="" data-first-option="false" name="netPayableAccountCode" id="netPayableAccountCode" class="form-control" required="required">
							<form:option value="">
								<spring:message code="lbl.select" />
							</form:option>
							<c:forEach var="coa" items="${netPayableAccounCodes}">
						        <form:option value="${coa.id}"><c:out value="${coa.glcode} - ${coa.name}"/></form:option>  
						    </c:forEach>   
							<%-- <form:options items="${netPayableAccounCodes}" itemLabel="glcode" itemValue="id" />  --%>
						</form:select>
						<%-- <form:errors path="" cssClass="add-margin error-msg" /> --%>
					</td>
					<!-- <td> 
						<input type="text" id="creditAccountHead" class="form-control" disabled> 
					</td> -->
					<td>
						<input type="text" id="netPayableAmount" name="netPayableAmount" value="${netPayableAmount}" class="form-control text-right" readonly="true">
						<%-- <form:errors path="" cssClass="add-margin error-msg" /> --%>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>