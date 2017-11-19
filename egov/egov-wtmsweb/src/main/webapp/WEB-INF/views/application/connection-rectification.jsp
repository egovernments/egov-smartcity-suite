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
 <form:form method ="post" action="" class="form-horizontal form-groups-bordered"  modelAttribute="connectionRectification" id="connectionRectificationform"
			cssClass="form-horizontal form-groups-bordered"
			enctype="multipart/form-data">
			<input type="hidden" value="${mode}" id="mode" />	
			<div class="row">
						<div class="col-md-12">

							<div class="panel panel-primary" data-collapsed="0">
								
								<div class="panel-heading">
									<div class="panel-title">
										Applicant Particulars
									</div>
									
								</div>
								<div class="panel-body custom-form">
									<form role="form" class="form-horizontal form-groups-bordered" method="POST">
										<div class="form-group">
											<label for="field-1" class="col-sm-4 control-label"><spring:message
			code="lbl.assesmentnumber" />:<span class="mandatory"></span></label>
				
											<div class="col-sm-4 add-margin">
												<form:input class="form-control patternvalidation" data-pattern="number" maxlength="50" id="assessmentNo" path="assessmentNo" />
		<form:errors path="assessmentNo" cssClass="add-margin error-msg" />		
											</div>
				
										</div>
										<div class="form-group">
											<label for="field-1" class="col-sm-4 control-label"><spring:message
			code="lbl1.consumer.number" />:<span class="mandatory"></span></label>
				
											<div class="col-sm-4 add-margin">
												<form:input class="form-control patternvalidation" data-pattern="number" maxlength="50" id="consumerNo" path="consumerNo" />
		<form:errors path="consumerNo" cssClass="add-margin error-msg" />		
								
											</div>
				
										</div>
										<div class="form-group">
											<div class="text-center">
												<button type="button" class="btn btn-primary" id="buttonid"><spring:message code="lbl.search"/></button>
								<a onclick="self.close()" class="btn btn-default" href="javascript:void(0)"><spring:message code="lbl.close"/></a>
											</div>
										</div>
									</form>
								</div>
							</div>
							 <c:if test="${mode == 'enable'}"> 
		
							<div class="panel panel-primary" data-collapsed="0" id ="assessmentdetailsid">
								
								<div class="panel-heading">
									<div class="panel-title"><spring:message code="lbl.assessmentdetails" /></div>
									
								</div>
								<div class="panel-body">
									<div class="row add-border">
										<div class="col-xs-3 add-margin">
											<spring:message
			code="lbl.assesmentnumber" />
										</div>
										<div class="col-xs-3 add-margin view-content">
											<c:out value="${connectionRectification.propertyAssessmentDetails.assessmentNumber}" />	
										</div>
										<div class="col-xs-3 add-margin">
											<spring:message
			code="lbl.assessmentstatus" />
										</div>
										<div class="col-xs-3 add-margin view-content">
											<c:choose>
									<c:when test="${connectionRectification.propertyAssessmentDetails.status == 'ACTIVE'}">
										<c:out value="ACTIVE" />
									</c:when> 
									<c:otherwise>
										<c:out value="INACTIVE" />
									</c:otherwise>
								</c:choose>
										</div>
									</div>
									
									<div class="row add-border">
										<div class="col-xs-3 add-margin">
											<spring:message
			code="lbl.digitalSignature.ownerName" />
										</div>
										<div class="col-xs-9 add-margin view-content">
											<c:out value="${connectionRectification.propertyAssessmentDetails.ownerName}" />	
										</div>
									</div>
<div class="row add-border">
										<div class="col-xs-3 add-margin"><spring:message
			code="lbl.propertyaddress" /></div>
										<div class="col-xs-9 add-margin view-content">
											<c:out value="${connectionRectification.propertyAssessmentDetails.address}" />	
										</div>
									</div>
									
									
									
								</div>
							</div>
						

							<div class="panel panel-primary" data-collapsed="0" id="connectiondetailsid">
								
								<div class="panel-heading">
									<div class="panel-title">Connection Details</div>
									
								</div>
								<div class="panel-body no-margin-bottom">
									<div class="form-group no-margin-bottom">
										<c:choose>
				<c:when test="${connectionRectification.connectionDetailsList.isEmpty()}">
					<div class="form-group" align="center" >No Data</div>
				</c:when>
			<c:otherwise>
				<table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="table table-bordered datatable" id="waterSourceTbl">
					<thead>
						<tr>
							<th colspan="1" class="text-center">
								<spring:message code="lbl1.consumer.number" />
							</th>
							<th colspan="1" class="text-center">
								<spring:message code="lbl.isprimary" />
							</th>
							<th colspan="1" class="text-center">
								<spring:message code="lbl.demanddue" />
							</th>
							<th colspan="1" class="text-center">
								<spring:message code="lbl.status"/>
							</th>
							<th colspan="1" class="text-center">
								<spring:message code="lbl.digitalSignature.action" />
							</th>

						</tr>
					</thead>
					<c:forEach var="connectionDetail" items="${connectionRectification.connectionDetailsList}">
						<tr>
							<td colspan="1">
								<div align="center">
									<c:out value="${connectionDetail.consumerNumber}" />
								</div>
							</td>
							<td colspan="1">
								<div align="center">
								<c:out value="${connectionDetail.isPrimary}" />
								</div>
							</td>
							<td colspan="1">
								<div align="center">
									<c:out value="${connectionDetail.demandDue}" />
								</div>
							</td>
							<td colspan="1">
								<div align="center">
									<c:out value="${connectionDetail.status}" />
								</div>
							</td>
							<td colspan="1">
								<div align="center">
									<select id="selectBox" class="form-control action-dropdown">
									    <option value="">Select any value</option>
                                        <option value="/wtms/application/activateConsumerCode/<c:out value="${connectionDetail.consumerNumber}" />">Activate Connection</option>

                                     </select>
								</div>
							</td>

						</tr>
					</c:forEach>
				</table>
			</c:otherwise>
			</c:choose>

									</div>
								</div>
							</div>
							</c:if>
						
						</div>
				</div>
		</form:form>			
				<link rel="stylesheet" href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
				<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
				<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
                <script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	            type="text/javascript"></script>
                <script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	            type="text/javascript"></script>
                <script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
	            type="text/javascript"></script>
	            <script src="<cdn:url value='/resources/js/app/connection-rectification.js?rnd=${app_release_no}'/>"></script>