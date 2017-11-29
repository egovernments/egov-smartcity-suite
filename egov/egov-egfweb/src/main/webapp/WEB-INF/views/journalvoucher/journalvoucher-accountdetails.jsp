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

<div class="panel-heading">
	<div class="panel-title">
		<spring:message code="lbl.accountdetails" />
	</div>
</div>
<div class="panel-body">
	<table class="table table-bordered" id="tblaccountdetails">
		<thead>
			<tr>
				<th><spring:message code="lbl.account.code"/></th>
				<th><spring:message code="lbl.account.head"/></th>
				<th><spring:message code="lbl.debit.amount"/></th>
				<th><spring:message code="lbl.credit.amount"/></th>
				<th><spring:message code="lbl.action"/></th> 					
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${voucherHeader.accountDetails.size() == 0}">
					<tr id="accountdetailsrow">
						<td>
							<input type="text" id="accountDetails[0].accountDetailGlcode"  class="form-control table-input accountDetailGlcode"  data-errormsg="Account Code is mandatory!" data-idx="0" data-optional="0"   placeholder="Type first 3 letters of Account code" />
							<form:hidden path="accountDetails[0].glcode" name="accountDetails[0].glcode" id="accountDetails[0].glcode" class="form-control table-input hidden-input accountglcode"/> 
							<form:hidden path="accountDetails[0].glcodeId.id" name="accountDetails[0].glcodeId.id" id="accountDetails[0].glcodeId.id" class="form-control table-input hidden-input accountglcodeid"/> 
							<form:hidden path="accountDetails[0].isSubLedger" name="accountDetails[0].isSubLedger" id="accountDetails[0].isSubLedger" class="form-control table-input hidden-input accountglcodeissubledger"/>
						</td>
						<td>
							<input type="text" id="accountDetails[0].accountHead" name="accountDetails[0].accountHead" class="form-control accountglname" disabled>  
						</td>
						<td>
							<form:input path="accountDetails[0].debitAmount" name="accountDetails[0].debitAmount" id="accountDetails[0].debitAmount"  data-errormsg="Debit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right debitAmount" onblur="calculateDebitAmount(this);" maxlength="12"  />
						</td> 
						<td>
							<form:input path="accountDetails[0].creditAmount" name="accountDetails[0].creditAmount" id="accountDetails[0].creditAmount"  data-errormsg="Credit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right creditAmount" onblur="calculateCreditAmount(this);"  maxlength="12"  />
						</td> 
						<td class="text-center"><span style="cursor:pointer;" onclick="addAccountDetailsRow();"><i class="fa fa-plus" aria-hidden="true"></i></span>
						 <span class="add-padding accountdetail-delete-row" onclick="deleteAccountDetailsRow(this);"><i class="fa fa-trash"  aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
					</tr>
					<tr id="accountdetailsrow">
						<td>
							<input type="text" id="accountDetails[1].accountDetailGlcode" class="form-control table-input accountDetailGlcode"  data-errormsg="Account Code is mandatory!" data-idx="0" data-optional="0"   placeholder="Type first 3 letters of Account code" />
							<form:hidden path="accountDetails[1].glcode" name="accountDetails[1].glcode" id="accountDetails[1].glcode" class="form-control table-input hidden-input accountglcode"/> 
							<form:hidden path="accountDetails[1].glcodeId" name="accountDetails[1].glcodeId" id="accountDetails[1].glcodeId" class="form-control table-input hidden-input accountglcodeid"/> 
							<form:hidden path="accountDetails[1].isSubLedger" name="accountDetails[1].isSubLedger" id="accountDetails[1].isSubLedger" class="form-control table-input hidden-input accountglcodeissubledger"/>
						</td>
						<td>
							<input type="text" id="accountDetails[1].accountHead" name="accountDetails[1].accountHead" class="form-control accountglname" disabled>  
						</td>
						<td>
							<form:input path="accountDetails[1].debitAmount" name="accountDetails[1].debitAmount" id="accountDetails[1].debitAmount"  data-errormsg="Debit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right debitAmount" onblur="calculateDebitAmount(this);" maxlength="12"  />
						</td> 
						<td>
							<form:input path="accountDetails[1].creditAmount" name="accountDetails[1].creditAmount" id="accountDetails[1].creditAmount"  data-errormsg="Credit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right creditAmount" onblur="calculateCreditAmount(this);"  maxlength="12"  />
						</td> 
						<td class="text-center"><span style="cursor:pointer;" onclick="addAccountDetailsRow();"><i class="fa fa-plus" aria-hidden="true"></i></span>
						 <span class="add-padding accountdetail-delete-row" onclick="deleteAccountDetailsRow(this);"><i class="fa fa-trash"  aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach items="${voucherHeader.accountDetails}" var="accountDeatils" varStatus="item">
						<tr id="accountdetailsrow">
							<td>
								<input type="text" id="accountDetails[${item.index }].accountCode" name="accountDetails[${item.index }].accountCode" class="form-control table-input accountDetailGlcode"  data-errormsg="Account Code is mandatory!" data-idx="0" data-optional="0"   placeholder="Type first 3 letters of Account code" value="${accountDeatils.glcode }" />
								<form:hidden path="accountDetails[${item.index }].glcode" name="accountDetails[${item.index }].glcode" id="accountDetails[${item.index }].glcode" class="form-control table-input hidden-input accountglcode" value="${accountDeatils.glcode }"/> 
								<form:hidden path="accountDetails[${item.index }].glcodeId" name="accountDetails[${item.index }].glcodeId" id="accountDetails[${item.index }].glcodeId" class="form-control table-input hidden-input accountglcodeid" value="${accountDeatils.glcodeId.id}"/> 
								<form:hidden path="accountDetails[${item.index }].isSubLedger" name="accountDetails[${item.index }].isSubLedger" id="accountDetails[${item.index }].isSubLedger" class="form-control table-input hidden-input accountglcodeissubledger" value="${accountDeatils.isSubLedger }"/>
							</td>
							<td>
								<input type="text" id="accountDetails[${item.index }].accountHead" name="accountDetails[${item.index }].accountHead" class="form-control accountglname" value="${accountDeatils.glcodeId.name }" disabled>  
							</td>
							<td>
								<form:input path="accountDetails[${item.index }].debitAmount" name="accountDetails[${item.index }].debitAmount" id="accountDetails[${item.index }].debitAmount"  data-errormsg="Debit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right debitAmount" onblur="calculateDebitAmount(this);"  value="${accountDeatils.debitAmount }" maxlength="12"  />
							</td> 
								<td>
								<form:input path="accountDetails[${item.index }].creditAmount" name="accountDetails[${item.index }].creditAmount" id="accountDetails[${item.index }].creditAmount"  data-errormsg="Credit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right creditAmount" onblur="calculateCreditAmount(this);"  value="${accountDeatils.creditAmount }"  maxlength="12"  />
							</td> 
							<td class="text-center"><span style="cursor:pointer;" onclick="addAccountDetailsRow();"><i class="fa fa-plus" aria-hidden="true"></i></span>
							 <span class="add-padding accountdetail-delete-row" onclick="deleteAccountDetailsRow(this);"><i class="fa fa-trash"  aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
</div>
