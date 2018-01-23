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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<div class="panel-heading">
	<div class="panel-title">
		<spring:message code="subheading.general.info"/>
		<input id="registrationId"type="hidden" name="id" value="${registration.id}" />
	</div>
</div>

<div class="panel-body">
	<div class="row add-border">
		<div class="col-sm-3 add-margin"><spring:message code="lbl.application.no"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${registration.applicationNo}"></c:out></div>
		<div class="col-sm-3 add-margin"><spring:message code="lbl.registration.no"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${registration.registrationNo!=null?registration.registrationNo:'N/A'}"></c:out></div>
	</div>
	
	<div class="row add-border">
		<div class="col-sm-3 add-margin"><spring:message code="lbl.date.of.marriage"/></div>
		<div class="col-sm-3 add-margin view-content">
			<fmt:formatDate value="${registration.dateOfMarriage}" pattern="dd-MM-yyyy" var="dateOfMarriage"/>
			<c:out value="${dateOfMarriage}"></c:out>
			</div>
		<div class="col-sm-3 add-margin"><spring:message code="lbl.application.date"/></div>
		<div class="col-sm-3 add-margin view-content">
			<fmt:formatDate value="${registration.applicationDate}" pattern="dd-MM-yyyy" var="applicationDate"/>
			<c:out value="${applicationDate}">
		</c:out></div>
	</div>
	<c:if test="${registration.venue !='Residence'}">
	<div class="row add-border">
	<%-- 	<div class="col-sm-3 add-margin"><spring:message code="lbl.law"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${registration.marriageAct.name}"></c:out></div> --%>
		<div class="col-sm-3 add-margin"><spring:message code="lbl.place.of.marriage"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${registration.placeOfMarriage}"></c:out></div>
	</div>
	</c:if>
	
	<div class="row add-border">
	    <div class="col-sm-3 add-margin"><spring:message code="lbl.registrationunit"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${registration.marriageRegistrationUnit.name}"></c:out></div>
		<div class="col-sm-3 add-margin"><spring:message code="lbl.Boundary"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${registration.zone.name}"></c:out></div>
	</div>
	<div class="row add-border">
	    <div class="col-sm-3 add-margin"><spring:message code="lbl.marriage.venue"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${registration.venue}"></c:out></div>
		<div class="col-sm-3 add-margin"><spring:message code="lbl.street"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${registration.street}"></c:out></div>
	</div>
	<div class="row add-border">
	    <div class="col-sm-3 add-margin"><spring:message code="lbl.city"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${registration.city}"></c:out></div>
		<div class="col-sm-3 add-margin"><spring:message code="lbl.locality"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${registration.locality.name}"></c:out></div>
	</div>
	<div class="row add-border">
		<div class="col-sm-3 add-margin"><spring:message code="lbl.marriage.photo"/></div>
	<div class="col-sm-3 add-margin">
	<c:set value="${registration.encodedMarriagePhoto}" var="ph"/>
	<img class="add-border" id="marrige-photo" height="150" width="130" src="data:image/jpeg;base64,${registration.encodedMarriagePhoto}">
	</div>
	</div>
</div>


<div class="panel-heading">
	<div class="panel-title">
		<spring:message code="subheading.husband.info" />
	</div>
</div>

		<c:set value="husband" var="applicant" scope="request"></c:set>
		<jsp:include page="viewapplicantinfo.jsp">
		<jsp:param value="subheading.husband.info" name="header" />
		<jsp:param value="${registration.husband.name.firstName}" name="appFirstName"/>
		<jsp:param value="${registration.husband.name.middleName}" name="appMiddleName"/>
		<jsp:param value="${registration.husband.name.lastName}" name="appLastName"/>
		<jsp:param value="${registration.husband.parentsName}" name="appparentsName"/>
		<jsp:param value="${registration.husband.encodedSignature}" name="signature"/>
		<jsp:param value="${registration.husband.encodedPhoto}" name="photo"/>
		<jsp:param value="${registration.husband.otherName}" name="appOtherName"/>
		<jsp:param value="${registration.husband.religion.name}" name="appReligion"/>
		<jsp:param value="${registration.husband.religionPractice}" name="appReligionPractice"/>
		<jsp:param value="${registration.husband.ageInYearsAsOnMarriage}" name="appAgeInYears"/>
		<jsp:param value="${registration.husband.ageInMonthsAsOnMarriage}" name="appAgeInMonths"/>
		<jsp:param value="${registration.husband.maritalStatus}" name="appPresentRelation"/>
		<jsp:param value="${registration.husband.occupation}" name="appOccupation"/>
		<jsp:param value="${registration.husband.street}" name="appstreet"/>
		<jsp:param value="${registration.husband.locality}" name="applocality"/>
		<jsp:param value="${registration.husband.nationality.name}" name="appNationality"/>
		<jsp:param value="${registration.husband.city}" name="appcity"/>
		<jsp:param value="${registration.husband.contactInfo.residenceAddress}" name="appResidenceAddress"/>
		<jsp:param value="${registration.husband.contactInfo.officeAddress}" name="appOfficeAddress"/>
		<jsp:param value="${registration.husband.contactInfo.mobileNo}" name="appMobileNo"/>
		<jsp:param value="${registration.husband.contactInfo.email}" name="appEmail"/>
		<jsp:param value="${registration.husband.handicapped}" name="appHandicapped"/>
		<jsp:param value="${registration.husband.aadhaarNo}" name="appAadharno"/>
		</jsp:include>

<div class="panel-heading">
	<div class="panel-title">
		<spring:message code="subheading.wife.info" />
	</div>
</div>

<c:set value="wife" var="applicant" scope="request"></c:set>
	<jsp:include page="viewapplicantinfo.jsp">
	<jsp:param value="subheading.wife.info" name="header" />
	<jsp:param value="${registration.wife.name.firstName}" name="appFirstName"/>
	<jsp:param value="${registration.wife.name.middleName}" name="appMiddleName"/>
	<jsp:param value="${registration.wife.name.lastName}" name="appLastName"/>
	<jsp:param value="${registration.wife.parentsName}" name="appparentsName"/>
	<jsp:param value="${registration.wife.encodedSignature}" name="signature"/>
	<jsp:param value="${registration.wife.encodedPhoto}" name="photo"/>
	<jsp:param value="${registration.wife.otherName}" name="appOtherName"/>
	<jsp:param value="${registration.wife.religion.name}" name="appReligion"/>
	<jsp:param value="${registration.wife.religionPractice}" name="appReligionPractice"/>
	<jsp:param value="${registration.wife.ageInYearsAsOnMarriage}" name="appAgeInYears"/>
	<jsp:param value="${registration.wife.ageInMonthsAsOnMarriage}" name="appAgeInMonths"/>
	<jsp:param value="${registration.wife.maritalStatus}" name="appPresentRelation"/>
	<jsp:param value="${registration.wife.occupation}" name="appOccupation"/>
	<jsp:param value="${registration.wife.street}" name="appstreet"/>
	<jsp:param value="${registration.wife.locality}" name="applocality"/>
	<jsp:param value="${registration.wife.city}" name="appcity"/>
	<jsp:param value="${registration.wife.nationality.name}" name="appNationality"/>
	<jsp:param value="${registration.wife.contactInfo.residenceAddress}" name="appResidenceAddress"/>
	<jsp:param value="${registration.wife.contactInfo.officeAddress}" name="appOfficeAddress"/>
	<jsp:param value="${registration.wife.contactInfo.mobileNo}" name="appMobileNo"/>
	<jsp:param value="${registration.wife.contactInfo.email}" name="appEmail"/>
	<jsp:param value="${registration.wife.handicapped}" name="appHandicapped"/>
	<jsp:param value="${registration.wife.aadhaarNo}" name="appAadharno"/>	
	</jsp:include>
<div class="panel-body">
		<div class="row add-border">
		    <div class="col-sm-3 add-margin"><spring:message code="lbl.serial.no"/></div>
			<div class="col-sm-3 add-margin view-content"><c:out value="${registration.serialNo!=null?registration.serialNo:'N/A'}"></c:out></div>
			<div class="col-sm-3 add-margin"><spring:message code="lbl.page.no"/></div>
			<div class="col-sm-3 add-margin view-content"><c:out value="${registration.pageNo!=null?registration.pageNo:'N/A'}"></c:out></div>
		</div>
	</div>