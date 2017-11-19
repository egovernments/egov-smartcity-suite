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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<form:form role="form" method="post" modelAttribute="waterConnectionDetails" 
id="editWaterConnectionform" cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">				
<div class="page-container" id="page-container">
	<form:hidden id="mode" path="" name="mode" value="${mode}"/> 
	<form:hidden path="" id="approvalPositionExist" value="${approvalPositionExist}"/>
	<form:hidden path="" id="wfstateDesc" value="${waterConnectionDetails.state.value}"/> 
	<form:hidden path="" id="statuscode" value="${waterConnectionDetails.status.code}"/>
	<form:hidden path="" id="isCommissionerLoggedIn" value="${isCommissionerLoggedIn}"/>
	<form:hidden path="" id="wfstate" value="${waterConnectionDetails.state.id}"/> 
	<form:hidden path="" id="waterconnectiondetailid" value="${waterConnectionDetails.id}"/>
	<input type="hidden" id="closerConnection" value="${waterConnectionDetails.closeConnectionType}"/> 
	<input type="hidden" id="currentUser" value="${currentUser}"/>  
	<input type="hidden" id="waterTaxDueforParent" value="${waterTaxDueforParent}" name="waterTaxDueforParent"/>  
	<input type="hidden" id ="typeOfConnection"  value="${typeOfConnection}"/>
	<input type="hidden" id="meterFocus" value="${meterFocus}"/>
	<input type="hidden" id="isSanctionedDetailEnable" value="${isSanctionedDetailEnable}"/>
	<input type="hidden" id="proceedWithoutDonation" value="${proceedWithoutDonation}"/>
	<form:hidden path="" id="workFlowAction" name="workFlowAction"/>
	<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message  code="lbl.basicdetails"/>
				</div>
			</div>
			<jsp:include page="commonappdetails-view.jsp"></jsp:include>
	</div>
	<c:if test="${waterConnectionDetails.status.code != 'CREATED'}">
		<jsp:include page="connectiondetails-view.jsp"></jsp:include>
	</c:if>
	<c:if test="${(waterConnectionDetails.status.code =='CREATED'  )&& mode=='fieldInspection'}">
		<div class="panel panel-primary" data-collapsed="0">
			<jsp:include page="connectiondetails.jsp"></jsp:include> 	
		</div>
		<jsp:include page="documentdetails-view.jsp"></jsp:include> 
			<jsp:include page="estimationdetails.jsp"></jsp:include>
	</c:if>	
			
	<c:if test="${waterConnectionDetails.status.code =='CREATED' && mode=='edit' }">
		<div class="panel panel-primary" data-collapsed="0">
			<jsp:include page="connectiondetails.jsp"></jsp:include> 	
		</div>
		<jsp:include page="documentdetails-view.jsp"></jsp:include> 
			</c:if>
	
	<c:if test="${waterConnectionDetails.status.code =='CREATED' && mode=='' && waterConnectionDetails.state.value =='Rejected'}">
		<div class="panel panel-primary" data-collapsed="0">
			<jsp:include page="connectiondetails.jsp"></jsp:include> 	
		</div>
		<jsp:include page="documentdetails-view.jsp"></jsp:include> 
		
	</c:if>			
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message  code="lbl.apphistory"/>
				</div>
			</div>
			<jsp:include page="applicationhistory-view.jsp"></jsp:include>
		</div>	
		<c:if test="${ (isSanctionedDetailEnable == true && waterConnectionDetails.status.code == 'ESTIMATIONAMOUNTPAID') }">
		<jsp:include page="sanctiondetails.jsp"></jsp:include>
		</c:if>	
		<c:if test="${waterConnectionDetails.status.code == 'WORKORDERGENERATED'}">
			<jsp:include page="tapexecutiondetails-form.jsp"></jsp:include>
		</c:if>
		
		<c:if test="${(waterConnectionDetails.status.code =='CLOSERINITIATED'  ||   waterConnectionDetails.status.code =='CLOSERINPROGRESS'||waterConnectionDetails.status.code =='CLOSERSANCTIONED') }">
			<jsp:include page="closerForm-details.jsp"></jsp:include>
			<jsp:include page="closuredocumentdetails-view.jsp"></jsp:include>
			</div>
			</c:if>
			
		<c:if test="${(waterConnectionDetails.status.code =='RECONNECTIONINPROGRESS'    || waterConnectionDetails.status.code =='RECONNECTIONSANCTIONED'||waterConnectionDetails.status.code =='RECONNECTIONINITIATED') }">			
	<jsp:include page="reconnection-details.jsp"></jsp:include> 
	<jsp:include page="closuredocumentdetails-view.jsp"></jsp:include>
	</c:if>
	
	 	<jsp:include page="../common/commonWorkflowMatrix.jsp"/>
	 	<jsp:include page="../common/commonWorkflowMatrix-button.jsp"/>
	<c:if test="${hasJuniorOrSeniorAssistantRole}">
		<jsp:include page="application-reassignment.jsp"></jsp:include>
	</c:if>
</form:form>
<script src="<cdn:url value='/resources/js/app/applicationsuccess.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/js/app/newconnectionupdate.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>