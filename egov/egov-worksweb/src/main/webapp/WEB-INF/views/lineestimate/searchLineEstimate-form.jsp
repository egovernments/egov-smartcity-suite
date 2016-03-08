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
<%@ include file="/includes/taglibs.jsp" %> 
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script src="<egov:url path='resources/js/helper.js'/>"></script>
<script src="<egov:url path='resources/js/works.js'/>"></script>
<div class="page-container" id="page-container">
	<div class="main-content">
		<form:form name="lineEstimateSearchForm" role="form" action="/egworks/lineestimate/search" modelAttribute="lineEstimateSearchRequest" id="lineEstimateSearchRequest" class="form-horizontal form-groups-bordered">
			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title" style="text-align:center;"><spring:message code="title.search.lineestimate" /></div>
						</div>
						<div class="panel-body">
							<div class="form-group">
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.administartive.sanctionno" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:input path="adminSanctionNumber" id="adminSanctionNumber" class="form-control"/>
									<form:errors path="adminSanctionNumber" cssClass="add-margin error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.executingdepartment" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:select path="executingDepartment" data-first-option="false" id="executingDepartments" class="form-control" required="required">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${executingDepartments}" itemValue="id" itemLabel="name" />
									</form:select>
									<form:errors path="executingDepartment" cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.adminsanctioned.fromdate" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:input path="adminSanctionFromDate" class="form-control datepicker"	id="adminSanctionFromDate" data-inputmask="'mask': 'd/m/y'" />
									<form:errors path="adminSanctionFromDate" cssClass="add-margin error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.adminsanctioned.todate" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:input path="adminSanctionToDate" class="form-control datepicker"	id="v" data-inputmask="'mask': 'd/m/y'" />
									<form:errors path="adminSanctionToDate" cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.fund" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:select path="fund" data-first-option="false" class="form-control" id="fund" required="required">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${funds}" itemValue="id" itemLabel="name"/>
									</form:select>
									<form:errors path="fund" cssClass="add-margin error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.function" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:select path="function" data-first-option="false" name="function" class="form-control" id="function" required="required">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${functions}" itemValue="id" itemLabel="name"/>
									</form:select>
									<form:errors path="function" cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.budgethead" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:select path="budgetHead" data-first-option="false" id="budgetHead" class="form-control" required="required">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${budgetHeads}" itemValue="id" itemLabel="name"/>
									</form:select>
									<form:errors path="budgetHead" cssClass="add-margin error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message code="lineestimate.estimateno" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:input path="estimateNumber" id="estimateNumber" class="form-control"/>
									<form:errors path="estimateNumber" cssClass="add-margin error-msg" />
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 text-center">
					<form:button type="submit" name="submit" class="btn btn-primary" value="Search"><spring:message code="lineestimate.btn.submit"/></form:button>
					<form:button type="button" class="btn btn-default" id="button2" onclick="window.close();"><spring:message code="lineestimate.btn.close"/></form:button>
				</div>
			</div>
		</form:form>  
	</div>
</div>
<script src="<c:url value='/resources/js/searchlineestimate.js'/>"></script>