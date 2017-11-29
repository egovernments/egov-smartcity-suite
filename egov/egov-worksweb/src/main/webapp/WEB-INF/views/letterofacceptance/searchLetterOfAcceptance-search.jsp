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
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<input type="hidden" value="<spring:message code='error.letterofacceptance.select' />" id='selectLOA'>
<form:form name="SearchRequest" role="form" action="" modelAttribute="searchRequestLetterOfAcceptance" id="searchRequestLetterOfAcceptance" class="form-horizontal form-groups-bordered">
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading">
			<div class="panel-title" style="text-align: center;">
				<spring:message code="title.search.letterofacceptance" />
			</div>
		</div>
 		<div class="panel-body">
			<div class="form-group">
				<label class="col-sm-3 control-label text-right" for="tags"><spring:message code="lbl.workidentificationnumber" /></label>
				<div class="col-sm-3 add-margin">
					<form:input path="workIdentificationNumber" id="workIdentificationNumber" class="form-control" placeholder="Type first 3 letters of Work Identification Number"/>
				<form:errors path="workIdentificationNumber" cssClass="add-margin error-msg" />
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.department" /> <span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="departmentName" data-first-option="false" class="form-control" id="department" required="required">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${departments}" itemValue="id" itemLabel="name" />
					</form:select>
					<form:errors path="departmentName" cssClass="add-margin error-msg" />
				</div>
			</div>
			<input type="hidden" id="mode" name="mode" value="${mode}" />
			<div class="form-group">
				<label class="col-sm-3 control-label text-right"><spring:message code="lbl.estimatenumber" /></label>
				<div class="col-sm-3 add-margin">
					<form:input path="estimateNumber" id="estimateNumber" class="form-control" placeholder="Type first 3 letters of Abstract Estimate Number"/>
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.loanumber" /></label>
				<div class="col-sm-3 add-margin">
					<form:input path="workOrderNumber" id="workOrderNumber" class="form-control" placeholder="Type first 3 letters of LOA Number"/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label text-right"><spring:message code="lbl.adminsanctionfromdate" /></label>
				<div class="col-sm-3 add-margin">
					<form:input path="adminSanctionFromDate" class="form-control datepicker" id="adminSanctionFromDate" data-inputmask="'mask': 'd/m/y'"/>
 				</div>
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.adminsanctiontodate" /></label>
				<div class="col-sm-3 add-margin">
						<input type="adminSanctionToDate" class="form-control datepicker" id="adminSanctionToDate" data-inputmask="'mask': 'd/m/y'" value>
				</div>
			</div>
			
			<div class="form-group">
			<label class="col-sm-3 control-label text-right"><spring:message code="lbl.typeofwork" /></label>
				<div class="col-sm-3 add-margin">
				<form:select path="typeOfWork" data-first-option="false" id="typeofwork" class="form-control"  >
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${typeOfWork}" itemLabel="description" itemValue="id" />
				</form:select>
				<form:errors path="typeOfWork" cssClass="add-margin error-msg" />
			</div>
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.subtypeofwork" /></label>
			<div class="col-sm-3 add-margin">
			<input type="hidden" id="subTypeOfWorkValue" value=""/>
				<form:select path="subTypeOfWork" data-first-option="false" id="subTypeOfWork" class="form-control" >
					<form:option value=""><spring:message code="lbl.select"/></form:option>
				</form:select>
				<form:errors path="subTypeOfWork" cssClass="add-margin error-msg" />
			</div>
			</div>
		</div>
		<div align="center">
			<button type='button' class='btn btn-primary' id="btnsearch">
				<spring:message code='lbl.search' />
			</button>
			<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a> 
				<input type="button" class="btn btn-default" value="Reset"
				id="button" name="Reset" onclick="this.form.reset();">
		</div>
	</div>
</form:form>
<div class="row display-hide report-section">
	<div class="col-md-12 table-header text-left">
		<spring:message code='title.searchresult' />
	</div>
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover"
			id="resultTable">
			<thead>
				<tr>
					<th><spring:message code="lbl.selectonly" /></th>
					<th><spring:message code="lbl.slno" /></th>
					<th><spring:message code="lbl.estimatenumber" /></th>
					<th><spring:message code="lbl.typeofwork" /></th>
					<th><spring:message code="lbl.subtypeofwork" /></th>
					<th><spring:message code="lbl.dateofproposal" /></th>
					<th><spring:message code="lbl.nameofwork" /></th>
					<th><spring:message code="lbl.workidentificationnumber" /></th>
					<th><spring:message code="lbl.loanumber" /></th>
					<th><spring:message code="lbl.loadate" /></th>
					<th><spring:message code="lbl.workvalue" /></th>
				</tr>
			</thead>
		</table>
	</div>
	<div align="center">
		<button type='button' class='btn btn-primary' id="createMilestone">
			<spring:message code='lbl.createmilestone' />
		</button>
		<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a> 
	</div>
</div>

<script type="text/javascript"
	src="<cdn:url value='/resources/js/searchletterofacceptance.js'/>"></script>
