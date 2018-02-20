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

<script src="<cdn:url value='/resources/js/app/viewregistration.js'/> "></script>

<div class="row" id="page-content">
	<div class="col-md-12"> 
		<div class="text-right error-msg" style="font-size:14px;"></div>
		<form:form role="form" action="/mrs/registration/createdataentry"
			modelAttribute="marriageRegistration" id="form-registration"
			cssClass="form-horizontal form-groups-bordered"
			enctype="multipart/form-data">
			<input type="hidden" id="registrationId" value="${marriageRegistration.id}" />
			<input type="hidden" id="registrationStatus" value="${marriageRegistration.status}" />
			<input type="hidden" name="marriageRegistration" value="${marriageRegistration.id}" />
			 <ul class="nav nav-tabs" id="settingstab">
			    <li class="active"><a data-toggle="tab" href="#applicant-info" data-tabidx=0>Applicant's Information</a></li>
			    <li><a data-toggle="tab" href="#witness-info" data-tabidx=1>Witnesses Information</a></li>
			    <li><a data-toggle="tab" href="#checklist-info" data-tabidx=2>Checklist</a></li>
			  </ul>
					   
			<div class="tab-content">	 
				<div id="applicant-info" class="tab-pane fade in active">
						<div class="panel panel-primary" data-collapsed="0">
							<br>
							<div class="form-group">
						    	<label class="col-sm-3 control-label text-right"><spring:message
										code="lbl.appl.number" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:input path="applicationNo" id="applicationNum"
										class="form-control text-left patternvalidation" data-pattern="alphanumericwithspecialcharacters" maxlength="20" required="required" />
									<form:errors path="applicationNo" 
										cssClass="add-margin error-msg" />
								</div>
								
								<label class="col-sm-2 control-label text-right"><spring:message
										code="lbl.registration.number" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:input path="registrationNo" id="registrationNum"
										class="form-control text-left patternvalidation" data-pattern="number" maxlength="20" required="required" />
									<form:errors path="registrationNo" 
										cssClass="add-margin error-msg" />
								</div>
							</div>
			    			<jsp:include page="generalinfo.jsp"></jsp:include>
			    		</div>
			    </div>
			    <div id="witness-info" class="tab-pane fade">
			    	<div class="panel panel-primary" data-collapsed="0">
			    		<c:set value="witnesses[0]" var="witness" scope="request"></c:set>
			    		<form:hidden path="witnesses[0].id"/>
			    		<form:hidden path="witnesses[0].applicantType" value="Husband" />	
				    	<jsp:include page="witnessinfo.jsp">
							<jsp:param value="subheading.witness1.info" name="subhead" />
								<jsp:param value="husbandside.witness" name="header" />
							
						</jsp:include>
						
						<c:set value="witnesses[1]" var="witness" scope="request"></c:set>
						<form:hidden path="witnesses[1].id"/>
						<form:hidden path="witnesses[1].applicantType" value="Husband" />	
						<jsp:include page="witnessinfo.jsp">
							<jsp:param value="subheading.witness2.info" name="subhead" />
							<jsp:param value="" name="header" />
							
						</jsp:include>
						
						<c:set value="witnesses[2]" var="witness" scope="request"></c:set>
						<form:hidden path="witnesses[2].id"/>
						<form:hidden path="witnesses[2].applicantType" value="Wife" />	
						<jsp:include page="witnessinfo.jsp">
							<jsp:param value="subheading.witness3.info" name="subhead" />
							<jsp:param value="wifeside.witness" name="header" />
							
						</jsp:include>	
						
						<c:set value="witnesses[3]" var="witness" scope="request"></c:set>
							<form:hidden path="witnesses[3].id"/>
							<form:hidden path="witnesses[3].applicantType" value="Wife" />	
							<jsp:include page="witnessinfo.jsp">
								<jsp:param value="subheading.witness4.info" name="subhead" />
								<jsp:param value="" name="header" />
							</jsp:include>	
						<jsp:include page="priestinfo.jsp"></jsp:include>
					</div>
			    </div>
			    <div id="checklist-info" class="tab-pane fade">
			    	<div class="panel panel-primary" data-collapsed="0">
			    		<jsp:include page="checklist.jsp"></jsp:include>
			    	</div>
			    	<div class="panel panel-primary" data-collapsed="0">
			    		<jsp:include page="documentdetails.jsp"></jsp:include>
		    		</div>
			    </div>
			</div><br />
			<div class="text-center">
				<button type="submit" class="btn btn-primary" id ="dataEntrySubmit"><spring:message code="lbl.submit"/></button>
			    <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
			</div>
		</form:form>
	</div>
</div>

<script src="<cdn:url value='/resources/global/js/egov/inbox.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/js/app/registrationformvalidation.js?rnd=${app_release_no}'/> "></script>
<script src="<cdn:url value='/resources/js/app/mrgregdataentry.js?rnd=${app_release_no}'/> "></script> 