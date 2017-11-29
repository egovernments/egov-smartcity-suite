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
		<spring:message code="lbl.credit.details" />
	</div>
</div>
<div class="panel-body">
	<table class="table table-bordered" id="tblcreditdetails">
		<thead>
			<tr>
				<th><spring:message code="lbl.account.code"/></th>
				<th><spring:message code="lbl.account.head"/></th>
				<th><spring:message code="lbl.credit.amount"/></th>
				<th><spring:message code="lbl.action"/></th> 					
			</tr>
		</thead>
		<tbody>
			<tr id="creditdetailsrow">
				<td>
					<input type="text" id="tempCreditDetails[0].creditGlcode" name="tempCreditDetails[0].creditGlcode" class="form-control table-input creditDetailGlcode creditGlcode"  data-errormsg="Account Code is mandatory!" data-idx="0" data-optional="0"   placeholder="Type first 3 letters of Account code" >
					<form:hidden path="" name="tempCreditDetails[0].glcode" id="tempCreditDetails[0].glcode" class="form-control table-input hidden-input creditaccountcode"/> 
					<form:hidden path="" name="tempCreditDetails[0].glcodeid" id="tempCreditDetails[0].glcodeid"  class="form-control table-input hidden-input creditdetailid"/>
					<form:hidden path="" name="tempCreditDetails[0].isSubLedger" id="tempCreditDetails[0].isSubLedger" class="form-control table-input hidden-input creditIsSubLedger"/>
					<form:hidden path="" name="tempCreditDetails[0].detailTypeId" id="tempCreditDetails[0].detailTypeId" class="form-control table-input hidden-input creditDetailTypeId"/>
					<form:hidden path="" name="tempCreditDetails[0].detailKeyId" id="tempCreditDetails[0].detailKeyId" class="form-control table-input hidden-input creditDetailKeyId"/> 
					<form:hidden path="" name="tempCreditDetails[0].detailTypeName" id="tempCreditDetails[0].detailTypeName" class="form-control table-input hidden-input creditDetailTypeName"/>
					<form:hidden path="" name="tempCreditDetails[0].detailKeyName" id="tempCreditDetails[0].detailKeyName" class="form-control table-input hidden-input creditDetailKeyName"/> 
				</td>
				<td>
					<input type="text" id="tempCreditDetails[0].creditAccountHead" name="tempCreditDetails[0].creditAccountHead"  class="form-control creditdetailname" disabled>  
				</td>
				<td>
					<form:input path="" name="tempCreditDetails[0].creditamount" id="tempCreditDetails[0].creditamount" data-errormsg="Credit Amount is mandatory!" onkeyup="decimalvalue(this);" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right creditAmount"   maxlength="12"  />
				</td> 
				<td class="text-center"><span style="cursor:pointer;" onclick="addCreditDetailsRow();"><i class="fa fa-plus"></i></span>
				 <span class="add-padding credit-delete-row" onclick="deleteCreditDetailsRow(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
			</tr>
		</tbody>
	</table>
</div>
