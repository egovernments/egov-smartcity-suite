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

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="panel panel-primary" data-collapsed="0">
	<input type="hidden" id="errorentrydate" value="<spring:message code='error.mb.entry.date.commenced.date' />">
	<input type="hidden" id="errorissueddate" value="<spring:message code='error.mb.issued.date.commenced.date' />">
	<input type="hidden" id="errorentryissueddate" value="<spring:message code='error.mb.issued.date.entry.date' />">
	<input type="hidden" id="errorfromtopage" value="<spring:message code='error.from.to.page' />">
	<input type="hidden" id="errorfromlasttopage" value="<spring:message code='error.from.last.to.page' />">
	<div class="panel-heading">
		<div class="panel-title" style="text-align: left;">
			<spring:message code="lbl.measurementbook.details" /> 
		</div>
		<c:if test="${showHistory}">
			<div style="text-align: right;">
				<a href="javascript:void(0);" onclick="viewMBHistory()"><spring:message code="lbl.mb.history" /></a>
			</div>
		</c:if>
	</div>
	<div class="panel-body custom-form">
		<div class="form-group">
			<label class="col-sm-3 control-label text-right">
			    <spring:message code="lbl.mb.referencenumber" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<form:input path="mbRefNo" id="mbRefNo" class="form-control required" maxlength="16" required="required"/>
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.mbentry.date" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<c:if test="${currentDate != null }">
					<input name="mbDate" class="form-control datepicker required" id="mbDate" value='<fmt:formatDate value="${currentDate}" pattern="dd/MM/yyyy" />' data-inputmask="'mask': 'd/m/y'" data-date-end-date="0d" required="required" />
				</c:if>
				<c:if test="${currentDate == null }">
					<form:input path="mbDate" class="form-control datepicker required" id="mbDate" data-inputmask="'mask': 'd/m/y'" data-date-end-date="0d" required="required" />
				</c:if>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right">
			    <spring:message code="lbl.from.page.number" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<form:input path="fromPageNo" id="fromPageNo" class="form-control required" maxlength="5" data-pattern="decimalvalue" required="required" onkeyup="decimalvalue(this);" />
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.to.page.number" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<form:input path="toPageNo" id="toPageNo" class="form-control required" maxlength="5" data-pattern="decimalvalue" required="required" onkeyup="decimalvalue(this);" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right">
			    <spring:message code="lbl.measurement.abstract" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<form:textarea path="mbAbstract" id="mbAbstract" class="form-control required patternvalidation" data-pattern="alphanumericwithallspecialcharacters" maxlength="1056" required="required"></form:textarea>
			</div>
		</div>
	</div>
</div>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title" style="text-align: left;">
			<spring:message code="lbl.workdetails" />
		</div>
	</div>
	<div class="panel-body custom-form">
		<div class="form-group">
			<label class="col-sm-3 control-label text-right">
			    <spring:message code="lbl.abstractestimatenumber" />
			</label>
			<div class="col-sm-3 add-margin view-content">
				<a href="javascript:void(0);" onclick="viewEstimate()"><span name="estimateNumber" id="estimateNumber">${mbHeader.workOrderEstimate.estimate.estimateNumber }</span></a>
				<input type="hidden" id="estimateId" value="${mbHeader.workOrderEstimate.estimate.id }">
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.mb.nameofwork" />
			</label>
			<div class="col-sm-4 add-margin view-content">
				<span name="nameOfWork" id="nameOfWork">${mbHeader.workOrderEstimate.estimate.name }</span>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right">
			    <spring:message code="lbl.workidnumber" />
			</label>
			<div class="col-sm-3 add-margin view-content">
				<span name="projectCode" id="projectCode">${mbHeader.workOrderEstimate.estimate.projectCode.code }</span>
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.loanumber" />
			</label>
			<div class="col-sm-3 add-margin view-content">
				<a href="javascript:void(0);" onclick="viewMBWorkOrder()"><span name="workOrderNumber" id="workOrderNumber">${mbHeader.workOrderEstimate.workOrder.workOrderNumber }</span></a>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right">
			    <spring:message code="lbl.name.contractor" />
			</label>
			<div class="col-sm-3 add-margin view-content">
				<span name="contractorName" id="contractorName">${mbHeader.workOrderEstimate.workOrder.contractor.name }</span>
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.work.assigned" />
			</label>
			<div class="col-sm-3 add-margin view-content">
				<span name="workOrderAssignedTo" id="workOrderAssignedTo">${mbHeader.workOrderEstimate.workOrder.engineerIncharge.name }</span>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right">
			    <spring:message code="lbl.mb.issued.date" />
			</label>
			<div class="col-sm-3 add-margin">
				<form:input path="mbIssuedDate" class="form-control datepicker" id="mbIssuedDate" data-inputmask="'mask': 'd/m/y'" data-date-end-date="0d" />
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.contractor.comments" />
			</label>
			<div class="col-sm-3 add-margin">
				<form:textarea path="contractorComments" id="contractorComments" class="form-control patternvalidation" data-pattern="alphanumericwithallspecialcharacters"></form:textarea>
			</div>
		</div>
	</div>
</div>