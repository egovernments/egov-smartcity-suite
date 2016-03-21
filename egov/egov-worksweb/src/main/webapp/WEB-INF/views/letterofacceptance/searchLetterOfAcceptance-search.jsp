<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!-- Need to change title for search criteria and result, add auto populate for the fields -->
<form:form action="search" modelAttribute="milestoneResult"	class="form-horizontal">
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading">
			<div class="panel-title" style="text-align: center;">
				<spring:message code="milestone.search.estimate" />
			</div>
		</div>
		<div class="panel-body">
			<div class="form-group">
				<label class="col-sm-3 control-label text-right" for="tags"><spring:message code="lbl.search.estimate" /></label>
				<div class="col-sm-3 add-margin">
					<input type="workIdentificationNumber" class="form-control" id="tags">
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.department" /> <span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="department" data-first-option="false" class="form-control" id="department" required="required">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${executingDepartments}" itemValue="id" itemLabel="name" />
					</form:select>
					<form:errors path="department" cssClass="add-margin error-msg" />
				</div>
			</div>
			<input type="hidden" id="mode" name="mode" value="${mode}" />
			<div class="form-group">
				<label class="col-sm-3 control-label text-right"><spring:message code="lbl.estimateNumber" /></label>
				<div class="col-sm-3 add-margin">
					<form:input path="estimateNumber" class="form-control" />
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.adminsanctionfromdate" /></label>
				<div class="col-sm-3 add-margin">
					<form:input path="adminSanctionFromDate" class="form-control datepicker" id="adminSanctionFromDate" data-inputmask="'mask': 'd/m/y'"/>
 				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label text-right"><spring:message code="lbl.loanumber" /></label>
				<div class="col-sm-3 add-margin">
					<input type="LOANumber" class="form-control" id="LOANumber">
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.adminsanctiontodate" /></label>
				<div class="col-sm-3 add-margin">
						<input type="adminSanctionToDate" class="form-control datepicker" id="adminSanctionToDate" data-inputmask="'mask': 'd/m/y'" value>
				</div>
			</div>
		</div>
		<div align="center">
			<button type='button' class='btn btn-primary' id="searchLoa">
				<spring:message code='lbl.search' />
			</button>
			<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
		</div>
	</div>
</form:form>
<div class="row display-hide report-section">
	<div class="col-md-12 table-header text-left"><spring:message code='milestone.search.result' /></div>
		<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id="resultTable">
			<thead>
				<tr>
					<th><spring:message code="lbl.estimateNumber" /></th>
					<th><spring:message code="lbl.estimate.date" /></th>
					<th><spring:message code="lbl.nameofwork" /></th>
					<th><spring:message code="lbl.workidentificationnumber" /></th>
					<th><spring:message code="lbl.loanumber" /></th>
					<th><spring:message code="lbl.LOADate" /></th>
					<th><spring:message code="lbl.workvalue" /></th>
				</tr>
			</thead>
			</table>
		</div>
		</div>
		
<script type="text/javascript" src="<c:url value='/resources/js/searchletterofacceptance.js'/>"></script>
  