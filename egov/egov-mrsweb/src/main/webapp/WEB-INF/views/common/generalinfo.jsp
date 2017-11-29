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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<br />
<div class="panel-heading">
	<div class="panel-title">
		<spring:message code="subheading.general.info" />
	</div>
</div>
<div class="panel-body">
	<form:hidden path="id" />
	<div class="row add-border">
		<div class="col-sm-3 ">
			<spring:message code="lbl.reissue.application.no" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${reIssue.applicationNo}" />
		</div>
		<div class="col-sm-3 ">
			<spring:message code="lbl.application.no" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${reIssue.registration.applicationNo}" />
		</div>
	</div>
	<div class="row add-border">
		<div class="col-sm-3 ">
			<spring:message code="lbl.registration.no" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${reIssue.registration.registrationNo}" />
		</div>
		<div class="col-sm-3 ">
			<spring:message code="lbl.date.of.marriage" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${reIssue.registration.dateOfMarriage}" />
		</div>
	</div>
	<div class="row add-border">
		<div class="col-sm-3 ">
			<spring:message code="lbl.Boundary" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${reIssue.registration.zone.name}" />
		</div>
	<%-- 	<div class="col-sm-3 ">
			<spring:message code="lbl.law" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${reIssue.registration.marriageAct.name}" />
		</div> --%>
	</div>
	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.marriage.venue" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${reIssue.registration.venue}"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.street" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${reIssue.registration.street}"></c:out>
		</div>
	</div>
	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.city" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${reIssue.registration.city}"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.locality" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${reIssue.registration.locality.name}"></c:out>
		</div>
	</div>
	<div class="row add-border">
		<div class="col-sm-3 ">
			<spring:message code="lbl.place.of.marriage" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${reIssue.registration.placeOfMarriage}" />
		</div>
		<div class="col-sm-3 ">
			<spring:message code="lbl.registrationunit" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${reIssue.registration.marriageRegistrationUnit.name}" />
		</div>
	</div>
	<div class="row add-border">
		<div class="col-sm-3 add-margin"><spring:message code="lbl.marriage.photo"/></div>
	<div class="col-sm-3 add-margin">
	<img class="add-border" id="marrige-photo" height="150" width="130" src="data:image/jpeg;base64,${reIssue.registration.encodedMarriagePhoto}">
	</div>
	</div>
</div>

<c:set value="registration.husband" var="applicant" scope="request"></c:set>
<form:hidden path="registration.husband.id" />
<jsp:include page="viewapplicantinfo.jsp">
	<jsp:param value="subheading.husband.info" name="header" />
	<jsp:param value="${reIssue.registration.husband.name.firstName}"
		name="appFirstName" />
	<jsp:param value="${reIssue.registration.husband.name.middleName}"
		name="appMiddleName" />
	<jsp:param value="${reIssue.registration.husband.name.lastName}"
		name="appLastName" />
	<jsp:param value="${reIssue.registration.husband.parentsName}"
		name="appparentsName" />
	<jsp:param value="${reIssue.registration.husband.encodedSignature}"
		name="signature" />
	<jsp:param value="${reIssue.registration.husband.encodedPhoto}"
		name="photo" />
	<jsp:param value="${reIssue.registration.husband.otherName}"
		name="appOtherName" />
	<jsp:param value="${reIssue.registration.husband.religion.name}"
		name="appReligion" />
	<jsp:param value="${reIssue.registration.husband.religionPractice}"
		name="appReligionPractice" />
	<jsp:param
		value="${reIssue.registration.husband.ageInYearsAsOnMarriage}"
		name="appAgeInYears" />
	<jsp:param
		value="${reIssue.registration.husband.ageInMonthsAsOnMarriage}"
		name="appAgeInMonths" />
	<jsp:param value="${reIssue.registration.husband.maritalStatus}"
		name="appPresentRelation" />
	<jsp:param value="${reIssue.registration.husband.occupation}"
		name="appOccupation" />
	<jsp:param value="${reIssue.registration.husband.street}"
		name="appstreet" />
	<jsp:param value="${reIssue.registration.husband.locality}"
		name="applocality" />
	<jsp:param value="${reIssue.registration.husband.city}" name="appcity" />
	<jsp:param value="${reIssue.registration.husband.nationality.name}"
		name="appNationality" />
	<jsp:param
		value="${reIssue.registration.husband.contactInfo.residenceAddress}"
		name="appResidenceAddress" />
	<jsp:param
		value="${reIssue.registration.husband.contactInfo.officeAddress}"
		name="appOfficeAddress" />
	<jsp:param value="${reIssue.registration.husband.contactInfo.mobileNo}"
		name="appMobileNo" />
	<jsp:param value="${reIssue.registration.husband.contactInfo.email}"
		name="appEmail" />
	<jsp:param value="${reIssue.registration.husband.handicapped}"
		name="appHandicapped" />
</jsp:include>

<c:set value="registration.wife" var="applicant" scope="request"></c:set>
<form:hidden path="registration.wife.id" />
<jsp:include page="viewapplicantinfo.jsp">
	<jsp:param value="subheading.wife.info" name="header" />
	<jsp:param value="${reIssue.registration.wife.name.firstName}"
		name="appFirstName" />
	<jsp:param value="${reIssue.registration.wife.name.middleName}"
		name="appMiddleName" />
	<jsp:param value="${reIssue.registration.wife.name.lastName}"
		name="appLastName" />
	<jsp:param value="${reIssue.registration.wife.parentsName}"
		name="appparentsName" />
	<jsp:param value="${reIssue.registration.wife.encodedSignature}"
		name="signature" />
	<jsp:param value="${reIssue.registration.wife.encodedPhoto}"
		name="photo" />
	<jsp:param value="${reIssue.registration.wife.otherName}"
		name="appOtherName" />
	<jsp:param value="${reIssue.registration.wife.religion.name}"
		name="appReligion" />
	<jsp:param value="${reIssue.registration.wife.religionPractice}"
		name="appReligionPractice" />
	<jsp:param value="${reIssue.registration.wife.ageInYearsAsOnMarriage}"
		name="appAgeInYears" />
	<jsp:param value="${reIssue.registration.wife.ageInMonthsAsOnMarriage}"
		name="appAgeInMonths" />
	<jsp:param value="${reIssue.registration.wife.maritalStatus}"
		name="appPresentRelation" />
	<jsp:param value="${reIssue.registration.wife.occupation}"
		name="appOccupation" />
	<jsp:param value="${reIssue.registration.wife.street}" name="appstreet" />
	<jsp:param value="${reIssue.registration.wife.locality}"
		name="applocality" />
	<jsp:param value="${reIssue.registration.wife.city}" name="appcity" />
	<jsp:param value="${reIssue.registration.wife.nationality.name}"
		name="appNationality" />
	<jsp:param
		value="${reIssue.registration.wife.contactInfo.residenceAddress}"
		name="appResidenceAddress" />
	<jsp:param
		value="${reIssue.registration.wife.contactInfo.officeAddress}"
		name="appOfficeAddress" />
	<jsp:param value="${reIssue.registration.wife.contactInfo.mobileNo}"
		name="appMobileNo" />
	<jsp:param value="${reIssue.registration.wife.contactInfo.email}"
		name="appEmail" />
	<jsp:param value="${reIssue.registration.wife.handicapped}"
		name="appHandicapped" />
</jsp:include>
