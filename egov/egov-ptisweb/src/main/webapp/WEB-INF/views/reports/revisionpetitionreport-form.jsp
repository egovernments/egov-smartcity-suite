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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<%@ include file="/includes/taglibs.jsp"%>

<div id="dcbError" class="errorstyle" style="color: red"></div>
<form:form name="SearchRequest" role="form" action=""
	modelAttribute="RevisionPetitionReport"
	class="form-horizontal form-groups-bordered">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title" style="text-align: left;">
						<spring:message code="lbl.rp.report.title" />
					</div>
				</div>
				<input type="hidden" name="drillDownType" id="drillDownType"
					value="" /> <input type="hidden" name="selectedModeBndry"
					id="selectedModeBndry" value="" /> <input type="hidden"
					name="revenueWardName" id="revenueWardName" value="" />
				<div class="panel-body">
					<div class="panel-body custom-form">
						<div class="form-group">
							<label for="field-1" class="col-sm-2 control-label"><spring:message
									code="lbl.fromDate" /><span class="mandatory"></span></label>
							<div class="col-sm-3 add-margin">
								<fmt:formatDate value="${currDate}" var="fromDate"
									pattern="dd/MM/yyyy" />
								<form:input path="" name="fromDate" id="fromDate"
									cssClass="form-control datepicker" value="${fromDate}"
									cssErrorClass="form-control error" />
									<form:input type="hidden" path="" name="fromDate" id="currentDate"
									cssClass="form-control datepicker" value="${fromDate}"
									cssErrorClass="form-control error" />
									
							</div>
							<label for="field-1" class="col-sm-2 control-label"><spring:message
									code="lbl.toDate" /><span class="mandatory"></span></label>
							<div class="col-sm-3 add-margin">
								<fmt:formatDate value="${currDate}" var="toDate"
									pattern="dd/MM/yyyy" />
								<form:input path="" name="toDate" id="toDate"
									cssClass="form-control datepicker" value="${toDate}"
									cssErrorClass="form-control error" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<button type="button" id="btnsearch" class="btn btn-primary">
						Search</button>
					<button type="button" id="btnclose" class="btn btn-default"
						onclick="window.close();">Close</button>
				</div>
			</div>
			<br />
			<div class="col-sm-12 table-header text-center" id="reportTitle">
				Register of Revision Petitions<br />
				<div class="col-sm-12 table-header text-center" id="reportDates">	
				</div>
			</div></form:form>
<jsp:include page="revisionpetition-searchresult.jsp" />
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/revisionpetitionreport.js?rnd=${app_release_no}'/>"></script>



