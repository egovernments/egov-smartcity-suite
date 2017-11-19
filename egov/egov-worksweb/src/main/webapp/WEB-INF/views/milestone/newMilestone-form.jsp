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
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<body onload="replacePercentageValue();">
<form:form name="milestoneForm" role="form" action="milestone-save" modelAttribute="milestone" id="milestone" class="form-horizontal form-groups-bordered" enctype="multipart/form-data">
	<form:hidden id="mode" path=""  value="${mode}"/>
	<input type="hidden" value="${workOrder.id}" id="workOrderId" name="workOrderId"/>
	<input type="hidden" value="<spring:message code="error.milestone.altleastone.milestonedetails.needed" />" id="errorMilestoneDeatail" />
	<input type="hidden" value="<spring:message code="error.milestone.total.percentage" />" id="errorTotalPercentage" />
	<input type="hidden" value="<spring:message code="error.milestone.templatecode" />" id="errorTemplateCode" />
	<input type="hidden" value="<spring:message code="error.milestone.scheduleandloa.date" />" id="errorScheduleLOADate" />
	<input type="hidden" value="<spring:message code="error.milestone.scheduledates.date" />" id="errorScheduleDates" />
	<input type="hidden" value="<spring:message code="error.milestone.scheduleenddates.startdate" />" id="errorScheduleEndDates" />
		
		<div class="row">
		<div class="col-md-12">
			<c:if test="${mode == 'view'}">
				<jsp:include page="milestoneTemplate-view.jsp" />
			</c:if>
			<jsp:include page="milestoneHeader-view.jsp" />
			<jsp:include page="milestoneTemplate-search.jsp" />
			<jsp:include page="milestoneDetails.jsp" />
		</div>
	</div>
	<div class="row">
		<div class="col-sm-12 text-center">
			<form:button type="submit" name="submit" id="save"
				class="btn btn-primary" value="Save">
				<spring:message code="lbl.save" />
			</form:button>
			<form:button type="button" class="btn btn-default" id="button2"
				onclick="window.close();">
				<spring:message code="lbl.close" />
			</form:button>
		</div>
	</div>
</form:form>  
</body>
<script src="<cdn:url value='/resources/js/milestone/milestone.js?rnd=${app_release_no}'/>"></script>
