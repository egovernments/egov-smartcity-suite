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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%> 
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%> 
	
<div class="row">
	<div class="col-md-12">
		<form:form class="form-horizontal form-groups-bordered" action=""
			id="digitalSignatureSearchForm" modelAttribute="digitalSignatureReportList"
			method="get">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message
							code="lbl.digitalSignature.report.title" /></strong>
					</div>
				</div>
				<div class="panel-body">
					<div class="main-content">
						<div class="row">
							<div class="col-md-12">
								<table class="table table-bordered"  id="digSignDetailsTab" name="digSignDetailsTab">
						    		<thead>
							      		<tr>
											<%-- <th><spring:message code="lbl.digitalSignature.slno"/></th> --%>
											<th><input type="checkbox" id="selectAll" /><spring:message code="lbl.digitalSignature.select"/></th>
											<th><spring:message code="lbl.digitalSignature.module"/></th>
											<th><spring:message code="lbl.digitalSignature.type"/></th>
											<th><spring:message code="lbl.digitalSignature.details"/></th>
											<th><spring:message code="lbl.digitalSignature.sign"/></th> 
										</tr>
									</thead>
									<c:choose>
										<c:when test="${!digitalSignatureReportList.isEmpty()}">
											<c:forEach items="${digitalSignatureReportList}" var="record" varStatus="counter">
												<tr id="digSignInfo">
													<td class="blueborderfortd">
														<input type="checkbox" id="rowCheckBox" name="rowCheckBox"/>
													</td>
													<td class="blueborderfortd" >	
														<c:out value="${record.module}"/>
										 				<input type="hidden" id="objectId" name="objectId" value="${record.objectId}" />
										 				<input type="hidden" id="currentState" name="currentState" value="${record.status}" />
										 			</td>
										 			<td class="blueborderfortd" >	
										 				<c:out value="${record.type}"/>
										 				<input type="hidden" id="natureOfTask" name="natureOfTask" value="${record.type}" />	
										 			</td> 
										 			<td class="blueborderfortd" >	
										 				<c:out value="${record.details}"/>
										 			</td>
										 			<td class="blueborderfortd" >	
														 <span class="add-padding"><button type="button" id="previewButn" onclick="generateNotice(this, 'Preview', '<c:out value="${record.status}"/>');" class="btn btn-default">Preview</button></span>
														 <span class="add-padding"><button type="button" id="signButn" onclick="generateNotice(this, 'Sign', '<c:out value="${record.status}"/>')" class="btn btn-default">Sign</button></span>
										 			</td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>No records found</c:otherwise>
									</c:choose>		
								</table>
								<div class="text-center">
								<c:choose>
										<c:when test="${!digitalSignatureReportList.isEmpty()}">
									<button type="button" class="btn btn-primary" id="submitButton">Sign All</button>
									</c:when></c:choose>
									<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()">Close</a> 
								</div> 
							</div>
						</div>
					</div> 
				</div>
			</div>
		</form:form>
	</div>
</div>
<script type="text/javascript" src="<cdn:url value='/resources/js/app/digitalSignatureReport.js'/>"></script>
<script src="<cdn:url value='/resources/javascript/helper.js' context='/ptis'/>"></script>