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
		<div class="panel-title">
			<spring:message code="lbl.advancebill.list" />
		</div>
	</div>
	<div class="panel-body">
		<table class="table table-bordered" id="tbladvancebill">
			<thead>
				<tr>
					<th><spring:message code="lbl.billnumber" /></th>
					<th><spring:message code="lbl.billdate" /></th>
					<th><spring:message code="lbl.bill.vouchernumber" /></th>
					<th><spring:message code="lbl.bill.voucherdate" /></th>
					<th><spring:message code="lbl.payeename" /></th>
					<th class="text-right"><spring:message code="lbl.netamount" /></th>
					<th class="text-right"><spring:message code="lbl.earlierpayment" /></th>
					<th class="text-right"><spring:message code="lbl.payableamount" /></th>
					<th class="text-right"><spring:message code="lbl.paymentamount" /></th>
				</tr>

			</thead>
			<tbody>
				<tr id="advancebilldetailrow">
					<td><c:if test="${voucherHeader != null}">
							<div class="add-margin view-content">
								<a href='javascript:void(0)'
									onclick="viewBill('<c:out value="${egBillregister.egBillregistermis.sourcePath}"/>')">
									<c:out value="${egBillregister.billnumber}" />
								</a>
							</div>
						</c:if></td>
					<td>${egBillregister.billdate}</td>
					<td><c:if
							test="${egBillregister.egBillregistermis.voucherHeader != null}">
							<div class="add-margin view-content">
								<a href='javascript:void(0)'
									onclick="viewBillVoucher('<c:out value="${egBillregister.egBillregistermis.voucherHeader.id}"/>')">
									<c:out
										value="${egBillregister.egBillregistermis.voucherHeader.voucherNumber}" />
								</a>
							</div>
						</c:if></td>
					<td>${egBillregister.egBillregistermis.voucherHeader.voucherDate}</td>
					<td>${egBillregister.egBillregistermis.payto}</td>
					<td class="text-right">${egBillregister.billamount}</td>
					<td class="text-right">0.0</td>
					<td class="text-right">${egBillregister.billamount}</td>
					<td class="text-right">${egBillregister.billamount}</td>

				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td class="text-right" colspan="8">Total Amount</td>
					<td class="text-right">${egBillregister.billamount}</td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>