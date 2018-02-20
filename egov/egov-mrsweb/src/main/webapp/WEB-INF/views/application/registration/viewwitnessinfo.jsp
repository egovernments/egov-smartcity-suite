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


<c:forEach items="${registration.witnesses}" varStatus="loop" var="witness">
<div class="panel-heading">
	<div class="panel-title">
	<c:if test="${witness.applicantType=='Husband' && loop.index==0}">
		<div>Bridegroom Side witness</div>
		
		</c:if>
		<%-- Information Of Witness #<c:out value="${loop.index+1}"></c:out> --%>
		<c:if test="${witness.applicantType=='Wife' && loop.index==2}">
		<div>Bride Side witness</div>
		
		</c:if>
		Information Of Witness #<c:out value="${loop.index+1}"></c:out>
		
	</div>
</div>
<div class="panel-body">
	<div class="row add-border">
			<div class="col-xs-3 add-margin"><spring:message code="lbl.fullname"/></div>
			<div class="col-xs-3 add-margin view-content">
				<c:out value="${witness.name.firstName}"></c:out>&nbsp; &nbsp;
				<c:out value="${witness.name.middleName}"></c:out>&nbsp; &nbsp;
				<c:out value="${witness.name.lastName}"></c:out>
			</div>
			<div class="col-xs-3 add-margin"><spring:message code="lbl.occupation"/></div>
			<div class="col-xs-3 add-margin view-content"><c:out value="${witness.occupation}" default="N/A"></c:out></div>
		</div>
		
		<div class="row add-border">
				<div class="col-xs-3 add-margin"><spring:message code="lbl.witness.info"/></div>
				<div class="col-xs-3 add-margin view-content"><c:out value="${witness.relativeName}" default="N/A"></c:out></div>
			</div>
		
		
		<div class="row add-border">
				<div class="col-xs-3 add-margin"><spring:message code="lbl.relationship.applicant"/></div>
				<div class="col-xs-3 add-margin view-content"><c:out value="${witness.relationshipWithApplicant}" default="N/A"></c:out></div>
				<div class="col-xs-3 add-margin"><spring:message code="lbl.age"/></div>
				<div class="col-xs-3 add-margin view-content"><c:out value="${witness.age}" default="NA"></c:out></div>
			</div>

		<div class="row add-border">
				<div class="col-xs-3 add-margin"><spring:message code="lbl.residence.address"/></div>
				<div class="col-xs-3 add-margin view-content"><c:out value="${witness.contactInfo.residenceAddress}" default="N/A"></c:out></div>
				<div class="col-xs-3 add-margin"><spring:message code="lbl.applicant.aadhaarNo"/></div>
				<div class="col-xs-3 add-margin view-content"><c:out value="${witness.aadhaarNo}" default="N/A"></c:out></div>
		</div>
		<div class="row add-border">
				<div class="col-xs-3 add-margin">
					<spring:message code="lbl.photo"/>
				</div>
				<div class="col-xs-3">			 	
					<c:set value="${witness.encodedPhoto}" var="ph"/>
					<c:set value="${loop.index}" var="index" />
					<img  id="witness${index}-imgphoto" height="160" width="140">
					<script>
						var strData = '<c:out value="${ph}" />';
						var index = '<c:out value="${index}" />';
						if (strData != null && strData.length > 0) {
							$('#witness'+index+'-imgphoto').prop('src', "data:image/jpg;base64," + strData);
						}
					</script>
				</div>
			</div>
	</div>	
</c:forEach>
