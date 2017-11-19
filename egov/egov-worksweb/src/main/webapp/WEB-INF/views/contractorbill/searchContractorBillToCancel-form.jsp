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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title" style="text-align:center;"><spring:message code="title.search.contractorbill" /></div>
	</div>
	<input id="confirm" type="hidden" value='<spring:message code="msg.cancel.bill.conform" />' />
	<input id="voucherCreatedMessage" type="hidden" value='<spring:message code="error.contractorbill.voucher.created" />' />
	<div class="form-group">
		<label class="col-sm-2 control-label text-right"><spring:message code="lbl.department" /></label>
		<div class="col-sm-3 add-margin">
			<form:select path="department" data-first-option="false" id="department" class="form-control">
				<form:option value=""><spring:message code="lbl.select" /></form:option>
				<form:options items="${departments}" itemValue="id" itemLabel="name" />
			</form:select>
			<form:errors path="department" cssClass="add-margin error-msg" />
		</div>
		<label class="col-sm-2 control-label text-right"><spring:message code="lbl.billnumber" /></label>
		<div class="col-sm-3 add-margin">
			<form:input path="billNumber" id="billNumber" class="form-control" placeholder="Type first 3 letters of Bill Number" />
			<form:errors path="billNumber" cssClass="add-margin error-msg" />
		</div>		
	</div>
	
	<div class="form-group">
		<label class="col-sm-2 control-label text-right"><spring:message code="lbl.workidentificationnumber" /></label>
		<div class="col-sm-3 add-margin">
			<form:input path="workIdentificationNumber" class="form-control" id="workIdentificationNumber" placeholder="Type first 3 letters of Work Identification Number" />
			<form:errors path="workIdentificationNumber" cssClass="add-margin error-msg" />
		</div>
		<label class="col-sm-2 control-label text-right"><spring:message code="lbl.loanumber" /></label>
		<div class="col-sm-3 add-margin">
			<form:input path="workOrderNumber" class="form-control" id="workOrderNumber" />
			<form:errors path="workOrderNumber" cssClass="add-margin error-msg" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-2 control-label text-right"><spring:message code="lbl.status" /></label>
		<div class="col-sm-3 add-margin">
			<form:input path="status" id="status" class="form-control" value="APPROVED" readonly="true" />
			<form:errors path="status" cssClass="add-margin error-msg" />
		</div>
	</div>
</div>
