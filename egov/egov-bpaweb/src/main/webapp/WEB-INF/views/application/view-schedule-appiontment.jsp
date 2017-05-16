<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2017>  eGovernments Foundation
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

<%@page import="org.python.modules.jarray"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.existing.schedule.scrutiny" />
	</div>
</div>
<div class="panel-body" data-collapsed="0">
	<c:forEach items="${appointmentScheduledList}" var="appoimnt"
		varStatus="counter">
		<div class="panel-heading custom_form_panel_heading">
			<div class="panel-title">
				Scheduled Appointment
				<c:out value="${counter.index+1}"></c:out>
			</div>
		</div>
		<div class="row add-border">
			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.purpose" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${appoimnt.purpose}" default="N/A"></c:out>
			</div>
			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.appmnt.date" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${appoimnt.appointmentDate}" default="N/A"></c:out>
			</div>
		</div>

		<div class="row add-border">
			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.appmnt.time" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${appoimnt.appointmentTime}" default="N/A"></c:out>
			</div>
			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.appmnt.location" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${appoimnt.appointmentLocation}" default="N/A"></c:out>
			</div>
		</div>

		<div class="row add-border">
			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.remarks" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${appoimnt.remarks}" default="N/A"></c:out>
			</div>
			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.ispostponed" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${appoimnt.postponed ? 'YES' : 'NO'}" default="N/A"></c:out>
			</div>
		</div>

		<div class="row add-border">
			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.postpone.reason" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${appoimnt.postponementReason}" default="N/A"></c:out>
			</div>
		</div>
	</c:forEach>
</div>
