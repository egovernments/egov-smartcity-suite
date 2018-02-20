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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>


<div class="row">
	<div class="col-md-12"> 
		<div class="text-right error-msg"></div>
		
		<c:set value="/mrs/registration/workflow" var="actionUrl"></c:set>
		<c:if test="${registration.status == 'Approved'}">
			<c:set value="/mrs/certificate/registration?registrationId=${registration.id}" var="actionUrl"></c:set>
		</c:if>
		<form:form role="form" action="${actionUrl}"
			modelAttribute="registration" id="form-registrationview"
			cssClass="form-horizontal form-groups-bordered"
			enctype="multipart/form-data">

					  <ul class="nav nav-tabs" id="settingstab">
					    <li class="active"><a data-toggle="tab" href="#applicant-info">Applicant's Information</a></li>
					    <li><a data-toggle="tab" href="#witness-info">Witnesses Information</a></li>
					    <li><a data-toggle="tab" href="#checklist">Checklist</a></li>
					  </ul>
					  <div class="tab-content">
					    <div id="applicant-info" class="tab-pane fade in active">
					    	<div class="panel panel-primary" data-collapsed="0">
					    		<jsp:include page="viewgeneralinfo.jsp"></jsp:include>
					    	</div>
					    </div>
					    <div id="witness-info" class="tab-pane fade">
					    		<div class="panel panel-primary" data-collapsed="0">
						    		<jsp:include page="viewwitnessinfo.jsp">
						    		<jsp:param value="subheading.witness1.info" name="header" />
						    		</jsp:include>
						    	</div>
						    	<%-- <div class="panel panel-primary" data-collapsed="0">				    	
									<jsp:include page="viewpriestinfo.jsp"></jsp:include>
								</div> --%>
					    </div>
					    <div id="checklist" class="tab-pane fade">
						    <div class="panel panel-primary" data-collapsed="0">		
						    	<jsp:include page="viewchecklist.jsp"></jsp:include>
					    	</div>
					    </div>
					    		<jsp:include page="../../common/reg-reissue-wfhistory.jsp"></jsp:include>					    
					  </div>
					 
			<div class="buttonbottom" align="center"><a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a></div>
			<%-- <c:choose>
				<c:when test="${mode != 'view'}">			
					<c:set value="${registration.currentState.value}" var="stateValue"></c:set>
					<c:if test="${stateValue != 'Assistant Engineer Approved' && stateValue != 'Fee Collected' && stateValue != 'Revenue Clerk Approved'}">
						<jsp:include page="../../common/commonWorkflowMatrix.jsp"/>
					</c:if>
					<c:choose>
						<c:when test="${registration.currentState.nextAction == 'Asst. Engineer Approval Pending'}">
							<div class="row">
								<label class="col-sm-3 control-label text-right"><spring:message code="lbl.reason.rejection"/></label>
								<div class="col-sm-8 add-margin">
									<form:textarea class="form-control" path="approvalComent"  id="approvalComent" name="approvalComent"/><br />
									<form:hidden path="" id="workFlowAction" name="workFlowAction"/>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<form:hidden path="" id="workFlowAction" name="workFlowAction"/>
						</c:otherwise>
					</c:choose>
					<div class="buttonbottom" align="center">
						<jsp:include page="../../common/commonWorkflowMatrix-button.jsp" />
					</div>
				</c:when>
				<c:otherwise>
					<div class="buttonbottom" align="center"><a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a></div>
				</c:otherwise>
			</c:choose> --%>
		</form:form>
		
	</div>
</div>

<script src="<cdn:url value='/resources/global/js/egov/inbox.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/js/app/registration-common.js?rnd=${app_release_no}'/> "></script>
<script src="<cdn:url value='/resources/js/app/viewregistration.js?rnd=${app_release_no}'/> "></script>