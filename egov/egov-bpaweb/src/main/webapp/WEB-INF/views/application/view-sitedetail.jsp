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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.site.details" />
	</div>
</div>
<div class="panel-body">
	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.plot.door.no" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].plotdoornumber}" default="N/A"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.plot.landmark" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].plotlandmark}"
				default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.plot.no" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].plotnumber}" default="N/A"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.plot.survey.no" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].plotsurveynumber}"
				default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.survey.no.type" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].surveynumberType}" default="N/A"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.old.survey.no.type" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].oldSurveyNumber}"
				default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.street.address1" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].streetaddress1}" default="N/A"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.street.address2" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].streetaddress2}"
				default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.area" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].area}" default="N/A"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.city" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].citytown}" default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.district" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].district}" default="N/A"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.taluk" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].taluk}" default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.state" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].state}" default="N/A"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.pincode" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].sitePincode}"
				default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.nature.of.ownership" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].natureofOwnership}" default="N/A"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.extentin.sqmts" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].extentinsqmts}"
				default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.registrar.office" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].registrarOffice}" default="N/A"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.nearest.build.no" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].nearestbuildingnumber}"
				default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.subdiv.no" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].subdivisionNumber}" default="N/A"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.approved.layout.details" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].approvedLayoutDetail}"
				default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.encroach.remarks" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].encroachmentRemarks}" default="N/A"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.set.back.front" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].setBackFront}"
				default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.set.back.rear" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].setBackRear}" default="N/A"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.set.back.side1" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].setBackSide1}"
				default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.set.back.side2" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${bpaApplication.siteDetail[0].setBackSide2}" default="N/A"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.encroch.issue.present" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out
				value="${bpaApplication.siteDetail[0].encroachmentIssuesPresent ? 'Yes' : 'No'}"
				default="N/A"></c:out>
		</div>
	</div>
</div>