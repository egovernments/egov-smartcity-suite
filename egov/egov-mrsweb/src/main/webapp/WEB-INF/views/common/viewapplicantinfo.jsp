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
		<spring:message code="${param.header}"/>
	</div>
</div>
<div class="panel-body">
<div class="row add-border">
	<div  class="col-sm-3 "><spring:message code="lbl.fullname"/></div>
	<div class="col-sm-3 add-margin view-content" >
		<c:out value="${param.appFirstName}"></c:out>&nbsp; &nbsp;
		<c:out value="${param.appMiddleName}"></c:out>&nbsp; &nbsp;
		<c:out value="${param.appLastName}"></c:out>
	</div>
		
	<%-- <div  class="col-sm-3 "><spring:message code="lbl.signature"/></div>
	<div class="col-sm-3 add-margin view-content">
		<c:choose>
			<c:when test="${param.signature == null || param.signature == ''}">
				N/A
			</c:when>
			<c:otherwise>
				<input type="hidden" id="signaturecontent${status.index}" value="${param.signature}">
				<a id="signaturelink${status.index}">Click to download</a>
			</c:otherwise>
		</c:choose>
	</div> --%>
</div>

<div class="row add-border">
<%-- 	<div  class="col-sm-3 "><spring:message code="lbl.othername"/></div>
	<div class="col-sm-3 add-margin view-content"><c:out value="${param.appOtherName}"></c:out></div> --%>
	<div  class="col-sm-3 "><spring:message code="lbl.religion"/></div>
	<div class="col-sm-3 add-margin view-content"><c:out value="${param.appReligion}"></c:out></div>
</div>
	<div class="row add-border">
	<div class="col-sm-3 add-margin"><spring:message code="lbl.parentsName"/></div>
	<div class="col-sm-3 add-margin view-content"><c:out value="${param.appparentsName}" default="N/A"></c:out></div>
	
	<div class="col-sm-3 add-margin"><spring:message code="lbl.street"/></div>
	<div class="col-sm-3 add-margin view-content"><c:out value="${param.appstreet}" default="N/A"></c:out></div>
</div>

<div class="row add-border">
	<div class="col-sm-3 add-margin"><spring:message code="lbl.locality"/></div>
	<div class="col-sm-3 add-margin view-content"><c:out value="${param.applocality}" default="N/A"></c:out></div>
	
	<div class="col-sm-3 add-margin"><spring:message code="lbl.city"/></div>
	<div class="col-sm-3 add-margin view-content"><c:out value="${param.appcity}" default="N/A"></c:out></div>
</div>
<div class="row add-border">
	<%-- <div  class="col-sm-3 "><spring:message code="lbl.religiontype"/></div>
	<div class="col-sm-3 add-margin view-content"><c:out value="${param.appReligionPractice}"></c:out></div> --%>
	<div  class="col-sm-3 "><spring:message code="lbl.ageason.marriage"/></div>
	<div class="col-sm-3 add-margin view-content" ><c:out value="${param.appAgeInYears}"></c:out> Years, <c:out value="${param.appAgeInMonths}"></c:out> Months</div>
</div>


<div class="row add-border">
		<div class="col-sm-3 "><spring:message code="lbl.applicant.status"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${param.appPresentRelation}"></c:out></div>
		<div class="col-sm-3 "><spring:message code="lbl.occupation"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${param.appOccupation}"></c:out></div>
</div>

<div class="row add-border">
		<div class="col-sm-3 "><spring:message code="lbl.residence.address"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${param.appResidenceAddress}"></c:out></div>
		<div class="col-sm-3 "><spring:message code="lbl.office.address"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${param.appOfficeAddress}"></c:out></div>
</div>

<div class="row add-border">
		<div class="col-sm-3 "><spring:message code="lbl.phoneno"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${param.appMobileNo}" ></c:out></div>
		<div class="col-sm-3 "><spring:message code="lbl.email"/></div>
		<div class="col-sm-3 add-margin view-content"><c:out value="${param.appEmail}" ></c:out></div>
</div>
<div class="row add-border">
	<div class="col-sm-3 "><spring:message code="lbl.nationality"/></div>
	<div class="col-sm-3 add-margin view-content"><c:out value="${param.appNationality}" ></c:out></div>
	<div class="col-sm-3"><spring:message code="lbl.handicapped"/></div>
	<div class="col-sm-3 add-margin view-content"><c:choose><c:when test="${param.appHandicapped}">Yes</c:when><c:otherwise>No</c:otherwise></c:choose></div>
	<div  class="col-sm-3 "><spring:message code="lbl.photo"/></div>
	<div class="col-sm-3 add-margin">
		<img class="add-border" id="${applicant}-photo" height="150" width="130" src="data:image/jpeg;base64,${param.photo}">
	</div>
</div>
</div>