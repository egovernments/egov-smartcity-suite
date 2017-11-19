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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<form:hidden path="billamount" name="billamount" id="billamount" />
<input type="hidden" id="errorBillDateFinYear" value="<spring:message code='error.billdate.finyear' />" />
<input type="hidden" id="errorBillDateWorkOrder" value="<spring:message code='error.billdate.workorderdate' />" />
<input type="hidden" id="errorPartyBillDateBillDate" value="<spring:message code='error.partybilldate.billdate' />" />
<input type="hidden" id="errorWorkCompletionDateGreaterThanBillDate" value="<spring:message code='error.workcompletiondate.billdate' />" />
<input type="hidden" id="errorWorkCompletionDateGreaterThanWorkOrderDate" value="<spring:message code='error.workcompletiondate.workorderdate' />" />
<input type="hidden" id="errorWorkCompletionDateFutureDate" value="<spring:message code='error.workcompletiondate.futuredate' />" /> 
<input type="hidden" id="errorSpilloverNoRefund" value="<spring:message code='error.contractorBill.spillover.norefund' />" /> 
<input type="hidden" id="errorNonSpilloverNoRefund" value="<spring:message code='error.contractorBill.nonspillover.norefund' />" /> 
<input type="hidden" id="hiddenbilldate" value='<fmt:formatDate value="${contractorBillRegister.billdate }"/>'/>
<input type="hidden" id="errorBillCutOffDate" value="<spring:message code='error.billdate.cutoff' />" />
<div class="form-group">
	<!-- TODO: remove this condition to make billdate editable after user finishes data entry -->
	<c:choose>
		<c:when test="${lineEstimateDetails.lineEstimate.spillOverFlag }">
			<label class="col-sm-3 control-label text-right"><spring:message code="lbl.billdate" /><span class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:input id="billdate" path="billdate" class="form-control datepicker" data-date-format="dd/mm/yyyy" data-date-end-date="0d" required="required" />
				<form:errors path="billdate" cssClass="add-margin error-msg" />
			</div>
		</c:when>
		<c:otherwise>
			<label class="col-sm-3 control-label text-right"><spring:message code="lbl.billdate" /><span class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:input id="billdate" path="billdate" class="form-control" data-date-format="dd/mm/yyyy" data-date-end-date="0d" required="required" readonly="true" />
				<form:errors path="billdate" cssClass="add-margin error-msg" />
			</div>
		</c:otherwise>
	</c:choose>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.billtype" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select path="billtype" data-first-option="false" id="billtype" class="form-control" required="required">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${billTypes}" />
		</form:select>
		<form:errors path="billtype" cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.partyname" /></label>
	<div class="col-sm-3 add-margin" style="margin-bottom: 0;">
			<input type="text" id="contractorName" value="${workOrder.contractor.name}" class="form-control" disabled > 
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.contractor.code" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="contractorCode" value="${workOrder.contractor.code}" disabled>
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.party.billnumber" /></label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation" data-pattern="alphanumerichyphenbackslash" id="partyBillNumber" path="egBillregistermis.partyBillNumber" maxlength="32" />
		<form:errors path="egBillregistermis.partyBillNumber" cssClass="add-margin error-msg" />		
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.party.billdate" /></label>
	<div class="col-sm-3 add-margin">
		<form:input id="partyBillDate" path="egBillregistermis.partyBillDate" class="form-control datepicker" data-date-end-date="0d" />
		<form:errors path="egBillregistermis.partyBillDate" cssClass="add-margin error-msg" />
		<input type="hidden" id="errorPartyBillDate" value="<spring:message code='error.validate.partybilldate.lessthan.loadate' />" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.loanumber" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="loaNumber" name="loaNumber" value="${workOrder.workOrderNumber}" readonly="true"> 
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.agreement.amount" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control text-right" id="workOrderAmount" value="${workOrder.workOrderAmount}" disabled> 
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.fund" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="fund" value="${lineEstimateDetails.lineEstimate.fund.name}" disabled> 
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.function" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="function" value="${lineEstimateDetails.lineEstimate.function.name}" disabled> 
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.department" /></label>
	<div class="col-sm-3 add-margin">	
		<input type="text" class="form-control" id="department" value="${lineEstimateDetails.lineEstimate.executingDepartment.name}" disabled>
	</div>
    <label class="col-sm-2 control-label text-right"><spring:message code="lbl.narration" /></label>
    <div class="col-sm-3 add-margin">
    	<form:textarea name="narration" path="narration" id="narration" class="form-control" maxlength="1024" ></form:textarea>
		<form:errors path="narration" cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.workidentificationnumber" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="projectCode" value="${lineEstimateDetails.projectCode.code}" disabled> 
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.estimatenumber" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="estimateNumber" value="${lineEstimateDetails.estimateNumber}" disabled> 
	</div>
</div>