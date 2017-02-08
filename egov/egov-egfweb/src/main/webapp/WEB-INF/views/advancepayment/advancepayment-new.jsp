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
<%@ taglib uri="/WEB-INF/tags/cdn.tld" prefix="cdn"%>
<form:form name="advanceBillPayForm" role="form" action="create"
	modelAttribute="voucherHeader" id="voucherHeader"
	class="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<div class="alert text-left error-msg" id="errorMessage" hidden="true"></div>
	<input type="hidden"
		value="<spring:message code="error.select.bank.account" />"
		id="errorSelectBank" />
	<spring:hasBindErrors name="voucherHeader">
		<div class="alert alert-danger">
			<form:errors path="*" />
			<br />
		</div>
	</spring:hasBindErrors>
	<ul class="nav nav-tabs" id="settingstab">
		<li class="active"><a data-toggle="tab" href="#advancebillheader"
			data-tabidx=0><spring:message code="lbl.header" /></a></li>
		<li><a data-toggle="tab" href="#advancebilldetail" data-tabidx=1><spring:message
					code="lbl.advancebill.list" /> </a></li>
	</ul>

	<div class="tab-content">
		<div class="tab-pane fade in active" id="advancebillheader">
			<jsp:include page="advancepayment-header.jsp" />
			<div class="panel panel-primary" data-collapsed="0">
				<jsp:include page="advancepayment-filter-details.jsp" />
			</div>
		</div>
		<div class="tab-pane fade" id="advancebilldetail">
			<jsp:include page="advancepayment-advancebills.jsp" />
		</div>
		<form:hidden path="" id="cutOffDate" value="${cutOffDate}" />
		<form:hidden path="" name="mode" id="mode" value="${mode}" />
		<jsp:include page="../common/commonworkflowmatrix.jsp" />
		<div class="buttonbottom" align="center">
			<jsp:include page="../common/commonworkflowmatrix-button.jsp" />
		</div>
	</div>

</form:form>
<script
	src="<cdn:url value='/resources/app/js/advancepayment/advancepayment.js?rnd=${app_release_no}'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>