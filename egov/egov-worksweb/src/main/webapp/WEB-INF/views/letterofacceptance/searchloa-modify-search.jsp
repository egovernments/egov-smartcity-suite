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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
		<form:form name="SearchRequest" role="form" action="" modelAttribute="searchRequestLetterOfAcceptance" id="SearchRequestLetterOfAcceptance" class="form-horizontal form-groups-bordered">
			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title" style="text-align:center;"><spring:message code="title.search.letterofacceptance" /></div>
						</div>
						<input type="hidden" id="workorderid" value="${workOrder.id }"/>
						<div class="panel-body">
							<div class="form-group">
								<label class="col-sm-3 control-label text-right"><spring:message code="lbl.loanumber" /></label>
								<div class="col-sm-3 add-margin">
									<form:input path="workOrderNumber" id="workOrderNumber" class="form-control" placeholder="Type first 3 letters of LOA Number"/>
									<form:errors path="workOrderNumber" cssClass="add-margin error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message code="lbl.filenumber" /></label>
								<div class="col-sm-3 add-margin">
									<form:input path="fileNumber" id="fileNumber" class="form-control"/>
									<form:errors path="fileNumber" cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right"><spring:message code="lbl.fromdate" /></label>
								<div class="col-sm-3 add-margin">
									<form:input path="fromDate" class="form-control datepicker"	id="fromDate" data-inputmask="'mask': 'd/m/y'" />
									<form:errors path="fromDate" cssClass="add-margin error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message code="lbl.todate" /></label>
								<div class="col-sm-3 add-margin">
									<form:input path="toDate" class="form-control datepicker"	id="toDate" data-inputmask="'mask': 'd/m/y'" />
									<form:errors path="toDate" cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
							<label class="col-sm-3 control-label text-right"><spring:message code="lbl.contractor" /></label>
								<div class="col-sm-3 add-margin">
									<form:input path="name" id="contractorSearch" class="form-control" placeholder="Type first 3 letters of Contractor Name Or Code"/>
									<form:errors path="name" cssClass="add-margin error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message code="lbl.department" /></label>
								<div class="col-sm-3 add-margin">
									<form:select path="departmentName" data-first-option="false" id="departments" class="form-control">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${departments}" itemValue="id" itemLabel="name" />
									</form:select>
									<form:errors path="departmentName" cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
							<label class="col-sm-3 control-label text-right"><spring:message code="lbl.estimateno" /></label>
								<div class="col-sm-3 add-margin">
									<form:input path="estimateNumber" id="estimateNumber" class="form-control" placeholder="Type first 3 letters of Abstract/Detailed Estimate Number"/>
									<form:errors path="estimateNumber" cssClass="add-margin error-msg" />
								</div>
							<label class="col-sm-2 control-label text-right"><spring:message code="lbl.status" /></label>
								<div class="col-sm-3 add-margin">
									<form:input path="egwStatus" id="egwStatus" class="form-control" value="APPROVED" disabled="true" />
									<form:errors path="egwStatus" cssClass="add-margin error-msg" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 text-center">
					<button type='button' class='btn btn-primary' id="btnsearch">
						<spring:message code='lbl.search' />
					</button>
					<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
				</div>
			</div>
		</form:form>  
	<jsp:include page="searchloa-modify-searchresult.jsp"/>
<script>
	$('#btnsearch').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
</script>
<script src="<cdn:url value='/resources/js/searchloa-modify.js?rnd=${app_release_no}'/>"></script>