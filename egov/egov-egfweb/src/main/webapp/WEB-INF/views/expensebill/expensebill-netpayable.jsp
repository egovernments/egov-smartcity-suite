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
		<spring:message code="lbl.netpayable" />
	</div>
</div>

<div style="padding: 0 15px;">
	<table class="table table-bordered" id="tblnetpayable">
		<thead>
			<tr>
				<th><spring:message code="lbl.account.code" /></th>
				<th><spring:message code="lbl.credit.amount" /></th>
			</tr>
		</thead>
		<tbody>
			<tr id="netpayablerow">
				<td>
					<form:hidden path="" name="netPayableAccountId" id="netPayableAccountId" value="${netPayableAccountId}"/>
					<form:hidden path="" name="netPayableAccountCodeId" id="netPayableAccountCodeId" value="${netPayableAccountCodeId}"/>
					<form:hidden path="" name="netPayableGlcode" id="netPayableGlcode" />
					<form:hidden path="" name="netPayableAccountHead" id="netPayableAccountHead" />
					<form:hidden path="" name="netPayableIsSubLedger" id="netPayableIsSubLedger" />
					<form:hidden path="" name="netPayableDetailTypeId" id="netPayableDetailTypeId" />
					<form:hidden path="" name="netPayableDetailKeyId" id="netPayableDetailKeyId" />
					<form:hidden path="" name="netPayableDetailTypeName" id="netPayableDetailTypeName" />
					<form:hidden path="" name="netPayableDetailKeyName" id="netPayableDetailKeyName" />
					<form:select path="" data-first-option="false" name="netPayableAccountCode" id="netPayableAccountCode" class="form-control" >
						<form:option value=""> <spring:message code="lbl.select" /> </form:option>
					</form:select>
				</td>
				<td><input type="text" id="netPayableAmount" name="netPayableAmount" value="${netPayableAmount}" class="form-control text-right" onkeyup="decimalvalue(this);" data-pattern="decimalvalue"> 
				</td>
			</tr>
		</tbody>
	</table>
</div>

<div class="text-center">
	<button type="button" id="populateAccountDetails" class="btn btn-secondary" align="center"><spring:message code="lbl.done"/></button>
</div>
