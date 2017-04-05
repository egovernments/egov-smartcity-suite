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
			<spring:message code="lbl.applicant.name" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.name}"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.code" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.code}" default="N/A"></c:out>
		</div>
	</div>


	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.DOB" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.dob}" default="N/A"></c:out>
		</div>

		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.gender" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.gender}" default="N/A"></c:out>
		</div>
	</div>
	
	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.isActive" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.isActive}"></c:out>
		</div>
	</div>
	<c:forEach items="${stakeHolder.address}" var="address">
		<div class="row add-border">
			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.address.type" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${address.type}" default="N/A"></c:out>
			</div>
		</div>
		<div class="row add-border">
			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.addr.dno" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${address.houseNoBldgApt}" default="N/A"></c:out>
			</div>

			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.addr.Steet.name" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${address.streetRoadLine}" default="N/A"></c:out>
			</div>
		</div>
		<div class="row add-border">
			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.locality" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${address.areaLocalitySector}" default="N/A"></c:out>
			</div>

			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.city" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${address.cityTownVillage}" default="N/A"></c:out>
			</div>
		</div>
		<div class="row add-border">
			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.district" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${address.district}" default="N/A"></c:out>
			</div>

			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.state" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${address.state}" default="N/A"></c:out>
			</div>
		</div>
		<div class="row add-border">
			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.pincode" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${address.pinCode}" default="N/A"></c:out>
			</div>
		</div>
	</c:forEach>
	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.mobilenumber" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.mobileNumber}"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.emailid" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.emailId}" default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.business.lic.no" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.businessLicenceNumber}"></c:out>
		</div>

		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.business.lic.due" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.businessLicenceDueDate}" default="N/A"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.coa.enrol.no" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.coaEnrolmentNumber}"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.coa.renew.date" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.coaEnrolmentDueDate}"></c:out>
		</div>
	</div>


	<div class="row add-border">
		<div class="col-sm-3 control-div add-margin">
			<spring:message code="lbl.enrol.with.local.body" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.isEnrolWithLocalBody}" default="NA"></c:out>
		</div>
		<div class="col-sm-3 control-div add-margin">
			<spring:message code="lbl.tin.no" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.tinNumber}" default="NA"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 control-div add-margin">
			<spring:message code="lbl.aadhar.no" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.aadhaarNumber}" default="NA"></c:out>
		</div>
		<div class="col-sm-3 add-margin">
			<spring:message code="lbl.pan.no" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.pan}" default="NA"></c:out>
		</div>
	</div>

	<div class="row add-border">
		<div class="col-sm-3 control-div add-margin">
			<spring:message code="lbl.behalf.org" />
		</div>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${stakeHolder.isOnbehalfOfOrganization}" default="NA"></c:out>
		</div>
	</div>
	<c:if test="${stakeHolder.isOnbehalfOfOrganization}">
		<div class="row add-border">
			<div class="col-sm-3 control-div add-margin">
				<spring:message code="lbl.nameof.org" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${stakeHolder.organizationName}" default="NA"></c:out>
			</div>
			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.org.contact.no" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${stakeHolder.organizationMobNo}" default="NA"></c:out>
			</div>
		</div>

		<div class="row add-border">
			<div class="col-sm-3 control-div add-margin">
				<spring:message code="lbl.addressof.org" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${stakeHolder.organizationAddress}" default="NA"></c:out>
			</div>
			<div class="col-sm-3 add-margin">
				<spring:message code="lbl.org.url" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${stakeHolder.organizationUrl}" default="NA"></c:out>
			</div>
		</div>
	</c:if>

</div>


