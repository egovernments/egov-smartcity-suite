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
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title" style="text-align: left;">
			<spring:message code="lbl.measurementbook.details" />
		</div>
	</div>
	<div class="panel-body custom-form">
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.mb.referencenumber" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<input name="mbRefNo" id="mbRefNo" class="form-control" maxlength="16" required="required"/>
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.mbentry.date" />
			</label>
			<div class="col-sm-3 add-margin">
				<input name="mbDate" class="form-control datepicker" id="mbDate" data-inputmask="'mask': 'd/m/y'" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.from.page.number" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<input name="fromPageNo" id="fromPageNo" class="form-control" maxlength="5" data-pattern="decimalvalue" required="required" />
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.to.page.number" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<input name="toPageNo" id="toPageNo" class="form-control" maxlength="5" data-pattern="decimalvalue" required="required" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.measurement.abstract" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<textarea name="mbAbstract" id="mbAbstract" class="form-control" maxlength="1056" required="required"></textarea>
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
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.estimatenumber" />
			</label>
			<div class="col-sm-3 add-margin">
				<input name="estimateNumber" id="estimateNumber" class="form-control" disabled="disabled" />
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.mb.nameofwork" />
			</label>
			<div class="col-sm-3 add-margin">
				<input name="nameOfWork" id="nameOfWork" class="form-control" disabled="disabled" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.workidnumber" />
			</label>
			<div class="col-sm-3 add-margin">
				<input name="projectCode" id="projectCode" class="form-control" disabled="disabled" />
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.loanumber" />
			</label>
			<div class="col-sm-3 add-margin">
				<input name="workOrderNumber" id="workOrderNumber" class="form-control" disabled="disabled" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.contractor.name" />
			</label>
			<div class="col-sm-3 add-margin">
				<input name="contractorName" id="contractorName" class="form-control" disabled="disabled"/>
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.work.assigned" />
			</label>
			<div class="col-sm-3 add-margin">
				<input name="workOrderAssignedTo" id="workOrderAssignedTo" class="form-control" disabled="disabled"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.mb.issued.date" />
			</label>
			<div class="col-sm-3 add-margin">
				<input name="mbIssuedDate" class="form-control datepicker" id="mbIssuedDate" data-inputmask="'mask': 'd/m/y'" data-date-end-date="0d" />
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.contractor.comments" />
			</label>
			<div class="col-sm-3 add-margin">
				<textarea name="contractorComments" id="contractorComments" class="form-control"></textarea>
			</div>
		</div>
	</div>
</div>