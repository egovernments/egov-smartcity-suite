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


<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<div class="panel-body">

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.fullname" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appFirstName}"></c:out>
			&nbsp; &nbsp;
			<c:out value="${param.appMiddleName}"></c:out>
			&nbsp; &nbsp;
			<c:out value="${param.appLastName}"></c:out>
		</div>
		<%-- <label class="col-sm-3 add-margin "> <spring:message
				code="lbl.signature" />
		</label>
		<div class="col-sm-3 add-margin view-content">
			<c:choose>
				<c:when test="${param.signature == null || param.signature == ''}">
				NA
			</c:when>
				<c:otherwise>
					<input type="hidden" id="signaturecontent${status.index}"
						value="${param.signature}">
					<a id="signaturelink${status.index}">Click to download</a>
				</c:otherwise>
			</c:choose>
		</div> --%>
	</div>


	<div class="row add-border">
	<%-- 	<div class="col-sm-3 add-margin">
			<spring:message code="lbl.othername" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appOtherName}" default="N/A"></c:out>
		</div> --%>

		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.religion" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appReligion}" default="N/A"></c:out>
		</div>
	</div>
	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.parentsName" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appparentsName}" default="N/A"></c:out>
		</div>

		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.street" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appstreet}" default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.locality" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.applocality}" default="N/A"></c:out>
		</div>

		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.city" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appcity}" default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<%-- <div class="col-sm-3 add-margin">
			<spring:message code="lbl.religiontype" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appReligionPractice}" default="NA"></c:out>
		</div> --%>

		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.ageason.marriage" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appAgeInYears}"></c:out>
			Years,
			<c:out value="${param.appAgeInMonths}"></c:out>
			Months
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.photo" />
		</div>
		<div class="col-sm-3 add-margin">
			<c:set value="${param.photo}" var="ph" />
			<img class="add-border" id="${applicant}-photo" height="150"
				width="130" src="data:image/jpeg;base64,${param.photo}">
			<!-- <script>
			var applicant = '<c:out value="${applicant}" />';
			var strData = '<c:out value="${ph}" />';
			$('#'+applicant+'-photo').prop('src', "data:image/jpeg;base64," + strData);
		</script> -->
		</div>
		
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.applicant.aadhaarNo" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appAadharno}"  default="N/A"></c:out>
		</div>
		
	</div>


	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.applicant.status" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appPresentRelation}"></c:out>
		</div>

		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.occupation" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appOccupation}" default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.residence.address" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appResidenceAddress}"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.office.address" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appOfficeAddress}"></c:out>
		</div>
	</div>


	<div class="row add-border">
		<div class="col-sm-3 control-div add-margin">
			<spring:message code="lbl.phoneno" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appMobileNo}" default="NA"></c:out>
		</div>
		<div class="col-sm-3 control-div add-margin">
			<spring:message code="lbl.email" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appEmail}" default="NA"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 control-div add-margin">
			<spring:message code="lbl.nationality" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${param.appNationality}" default="NA"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.handicapped" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:choose>
				<c:when test="${param.appHandicapped}">Yes</c:when>
				<c:otherwise>No</c:otherwise>
			</c:choose>
		</div>
	</div>

</div>


