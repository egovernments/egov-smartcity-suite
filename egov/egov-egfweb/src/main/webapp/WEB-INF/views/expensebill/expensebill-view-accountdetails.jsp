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
	<div class="panel-heading custom_form_panel_heading">
		<div class="panel-title">
			<spring:message code="lbl.accountdetails" />
		</div>
	</div>
	
	 <div style="padding: 0 15px;">
		<table class="table table-bordered" id="tblaccountdetails">
			<thead>
				<tr>
					<th><spring:message code="lbl.account.code"/></th>
					<th><spring:message code="lbl.account.head"/></th>
					<th><spring:message code="lbl.debit.amount"/></th>
					<th><spring:message code="lbl.credit.amount"/></th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${egBillregister.billDetails!=null && egBillregister.billDetails.size() > 0}">
						 <c:forEach items="${egBillregister.billDetails}" var="billDeatils" varStatus="item">
							<tr id="accountdetailsrow">
								<td>
									<span class="accountDetailsGlCode_${item.index }">${billDeatils.chartOfAccounts.glcode }</span>
								</td>
								<td>
									<span class="accountDetailsAccountHead_${item.index }">${billDeatils.chartOfAccounts.name }</span>
								</td>
								<td class="text-right">
									<span class="accountDetailsDebitAmount_${item.index } accountDetailsDebitAmount">${billDeatils.debitamount }</span>
								</td>
								<td class="text-right">
									<span class="accountDetailsCreditAmount_${item.index } accountDetailsCreditAmount">${billDeatils.creditamount }</span>
								</td>
							</tr>
						</c:forEach> 
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
</div>