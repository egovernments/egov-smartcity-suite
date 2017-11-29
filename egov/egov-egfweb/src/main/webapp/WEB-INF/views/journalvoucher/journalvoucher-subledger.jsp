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

<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.subledger.details" />
	</div>
</div>

<div style="padding: 0 15px;">
	<table class="table table-bordered" id="tblsubledger">
		<thead>
			<tr>
				<th><spring:message code="lbl.account.code" /></th>
				<th><spring:message code="lbl.type" /></th>
				<th><spring:message code="lbl.code" /></th>
				<th><spring:message code="lbl.name" /></th>
				<th><spring:message code="lbl.amount" /></th>
				<th><spring:message code="lbl.action" /></th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${voucherHeader.subLedgerDetails.size() == 0}">
					<tr id="subledgerrow">

						<td>
							<form:select path="subLedgerDetails[0].generalLedgerId.glcodeId" data-first-option="false"  name="subLedgerDetails[0].generalLedgerId.glcodeId" id="subLedgerDetails[0].generalLedgerId.glcodeId" class="form-control subLedgerAccountCode" >
								<form:option value=""> <spring:message code="lbl.select" /> </form:option>
							</form:select>
						</td>

						<td>
							<form:select path="subLedgerDetails[0].detailTypeId" data-first-option="false" name="subLedgerDetails[0].detailTypeId" id="subLedgerDetails[0].detailTypeId" class="form-control subLedgerAccountDetailType" >
								<form:option value=""> <spring:message code="lbl.select" /> </form:option>
							</form:select>
						</td>

						<td>
							<form:hidden path="subLedgerDetails[0].detailKeyId" name="subLedgerDetails[0].detailKeyId" id="subLedgerDetails[0].detailKeyId"  class="subLedgerDetailDetailKeyId "/>
							<input type="text" id="subLedgerDetails[0].detailkeyname" name="subLedgerDetails[0].detailkeyname"  class="form-control text-right subLedgerDetailKeyName" placeholder="Type first 3 letters of code or name">
						</td>
						
						<td>
							<input type="text" id="subLedgerDetails[0].name" name="subLedgerDetails[0].name"  class="form-control text-right subLedgerName" disabled>
						</td>

						<td>
							<input type="text" id="subLedgerDetails[0].amount" name="subLedgerDetails[0].amount"  class="form-control text-right subLedgerAmount" onkeyup="decimalvalue(this);" data-pattern="decimalvalue">
						</td>

						<td class="text-center">
							<span style="cursor:pointer;" onclick="addSubLedgerRow();">
								<i class="fa fa-plus" aria-hidden="true"></i>
							</span>
						 	<span class="add-padding subledger-delete-row" onclick="deleteSubLedgerRow(this);">
						 		<i class="fa fa-trash"  aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Delete!"></i>
						 	</span> 
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach items="${voucherHeader.subLedgerDetails}" var="subLedgerDetail" varStatus="item">
						<tr id="subledgerrow">

							<td>
								<form:select path="subLedgerDetails[${item.index }].generalLedgerId.glcodeId" data-first-option="false" name="subLedgerDetails[${item.index }].generalLedgerId.glcodeId" id="subLedgerDetails[${item.index }].generalLedgerId.glcodeId" class="form-control subLedgerAccountCode" value="${subLedgerDetail.generalLedgerId.glcodeId.id }">
									<form:option value=""> <spring:message code="lbl.select" /> </form:option>
								</form:select>
							</td>

							<td>
								<form:select path="subLedgerDetails[${item.index }].detailTypeId" data-first-option="false" name="subLedgerDetails[${item.index }].detailTypeId" id="subLedgerDetails[${item.index }].detailTypeId" class="form-control subLedgerAccountDetailType" value="${subLedgerDetail.detailTypeId.id }">
									<form:option value=""> <spring:message code="lbl.select" /> </form:option>
								</form:select>
							</td>

							<td>
								<form:hidden path="subLedgerDetails[${item.index }].detailKeyId" name="subLedgerDetails[${item.index }].detailKeyId" id="subLedgerDetails[${item.index }].detailKeyId" value="${subLedgerDetail.detailKeyId }" class="subLedgerDetailDetailKeyId "/>
								<input type="text" id="subLedgerDetails[${item.index }].detailkeyname" name="subLedgerDetails[${item.index }].detailkeyname"  class="form-control text-right subLedgerDetailKeyName" placeholder="Type first 3 letters of code or name">
							</td>

							<td>
								<input type="text" id="subLedgerDetails[${item.index }].amount" name="subLedgerDetails[${item.index }].amount"  class="form-control text-right subLedgerAmount" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" value="${subLedgerDetail.amount}">
							</td>
							
							<td class="text-center">
								<span style="cursor:pointer;" onclick="addSubLedgerRow();">
									<i class="fa fa-plus" aria-hidden="true"></i>
								</span>
							 	<span class="add-padding subledger-delete-row" onclick="deleteSubLedgerRow(this);">
							 		<i class="fa fa-trash"  aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Delete!"></i>
							 	</span> 
							</td>

						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
</div>