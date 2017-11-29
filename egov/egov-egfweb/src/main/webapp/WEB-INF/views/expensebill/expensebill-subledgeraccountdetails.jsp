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
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading custom_form_panel_heading">
		<div class="panel-title">
			<spring:message code="lbl.subledger.details" />
		</div>
	</div>
	
	<div style="padding: 0 15px;">
		<table class="table table-bordered" id="tblsubledgerdetails">
			<thead>
				<tr>
					<th><spring:message code="lbl.account.code"/></th>
					<th><spring:message code="lbl.subledgertype"/></th>
					<th><spring:message code="lbl.subledger.name"/></th>
					<th><spring:message code="lbl.amount"/></th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${egBillregister.billPayeedetails.size() == 0}">
						<tr id="subledhgerrow" subledgerinvisible="true" hidden="true">
							<td>
								<form:hidden path="billPayeedetails[0].id" id="subLedgerDetailsId_0" class ="subLedgerDetailsId"/>
								<form:hidden path="billPayeedetails[0].egBilldetailsId.id" id="subLedgerBillDetailsId_0" class="subLedgerBillDetailsId"/>
								<form:hidden path="billPayeedetails[0].egBilldetailsId.glcodeid" id="subLedgerGlCodeId_0" class="subLedgerGlCodeIdId"/>
								<form:hidden path="billPayeedetails[0].debitAmount" id="subLedgerDebitAmount_0" class ="subledgerdebitamount"/>
								<form:hidden path="billPayeedetails[0].creditAmount" id="subLedgerCreditAmount_0" class ="subledgercreditamount"/>
								<form:hidden path="billPayeedetails[0].isDebit" id="subLedgerIsDebit_0" class ="subledgerisdebit"/>
								<form:hidden path="billPayeedetails[0].accountDetailTypeId" id="subLedgerDetailTypeId_0" class="form-control table-input hidden-input subLedgerDetailTypeId"/>
								<form:hidden path="billPayeedetails[0].accountDetailKeyId" id="subLedgerDetailKeyId_0" class="form-control table-input hidden-input subLedgerDetailKeyId"/>
								<span class="subLedgerGlCode_0"></span>
							</td>
							<td>
								<span class="subLedgerType_0"></span>
							</td>
							<td>
								<span class="subLedgerName_0"></span>
							</td>
							<td>
								<span class="subLedgerAmount_0 subLedgerAmount"></span>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${egBillregister.billPayeedetails}" var="billPayeeDeatils" varStatus="item">
							<tr id="subledhgerrow" accountdetailsinvisible="true">
								<td>
									<form:hidden path="billPayeedetails[${item.index }].id" id="subLedgerDetailsId_${item.index }" class ="subLedgerDetailsId" value="${billPayeeDeatils.id}"/>
									<form:hidden path="billPayeedetails[${item.index }].egBilldetailsId.id" id="subLedgerBillDetailsId_${item.index }" class="subLedgerBillDetailsId"  value="${billPayeeDeatils.egBilldetailsId.id}"/>
									<form:hidden path="billPayeedetails[${item.index }].egBilldetailsId.glcodeid" id="subLedgerGlCodeId_${item.index }" class="subLedgerGlCodeIdId"/>
									<form:hidden path="billPayeedetails[${item.index }].debitAmount" id="subLedgerDebitAmount_${item.index }" class ="subledgerdebitamount" value="${billPayeeDeatils.debitAmount}"/>
									<form:hidden path="billPayeedetails[${item.index }].creditAmount" id="subLedgerCreditAmount_${item.index }" class ="subledgercreditamount" value="${billPayeeDeatils.creditAmount}"/>
									<form:hidden path="billPayeedetails[${item.index }].isDebit" id="subLedgerIsDebit_${item.index }" class ="subledgerisdebit" value="${billPayeeDeatils.isDebit}"/>
									<form:hidden path="billPayeedetails[${item.index }].accountDetailTypeId" id="subLedgerDetailTypeId_${item.index }" class="form-control table-input hidden-input subLedgerDetailTypeId" value="${billPayeeDeatils.accountDetailTypeId}"/>
									<form:hidden path="billPayeedetails[${item.index }].accountDetailKeyId" id="subLedgerDetailKeyId_${item.index }" class="form-control table-input hidden-input subLedgerDetailKeyId" value="${billPayeeDeatils.accountDetailKeyId}"/>
									<span class="subLedgerGlCode_${item.index }">${billPayeeDeatils.egBilldetailsId.chartOfAccounts.glcode }</span>
								</td>
								<td>
									<span class="subLedgerType_${item.index }">${billPayeeDeatils.detailTypeName }</span>
								</td>
								<td>
									<span class="subLedgerName_${item.index }">${billPayeeDeatils.detailKeyName }</span>
								</td>
								<c:if test="${billPayeeDeatils.debitAmount > 0 }">
									<td>
										<span class="subLedgerAmount_${item.index } subLedgerAmount">${billPayeeDeatils.debitAmount}</span>
									</td>
								</c:if>
								
								<c:if test="${billPayeeDeatils.creditAmount > 0 }">
									<td>
										<span class="subLedgerAmount_${item.index } subLedgerAmount">${billPayeeDeatils.creditAmount}</span>
									</td>
								</c:if>
								
							</tr>
						</c:forEach>
					
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
</div>