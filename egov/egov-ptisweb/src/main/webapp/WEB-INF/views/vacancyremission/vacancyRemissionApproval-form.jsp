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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<script type="text/javascript" src="<cdn:url value='/resources/javascript/validations.js'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/javascript/dateValidation.js'/>"></script>

<form:form id="vacancyRemissionApprovalForm" method="post"
	class="form-horizontal form-groups-bordered" modelAttribute="vacancyRemissionApproval">
	<div class="page-container" id="page-container">
        	<form:hidden path="" id="workFlowAction" name="workFlowAction"/>	
			<jsp:include page="../common/commonPropertyDetailsView.jsp"></jsp:include>
				<form:hidden path="" name="propertyByEmployee" id="propertyByEmployee" value="${propertyByEmployee}" />
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary" data-collapsed="0">
							<div class="panel-heading" style="text-align: left">
								<div class="panel-title"><spring:message code="lbl.vacancyremission.details" /></div>
							</div>
							<div class="panel-body custom-form">
								<div class="form-group">
									<label class="col-sm-3 control-label text-right">
										<spring:message code="lbl.vacancyremission.details.comments" /> <span class="mandatory"></span>
									</label>
									<div class="col-sm-8 add-margin">
										<form:textarea path="comments" class="form-control" required="required" />
										<form:errors path="comments" cssClass="add-margin error-msg"/>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="panel panel-primary" data-collapsed="0">
			<div>
				<%@ include
					file="/WEB-INF/views/vacancyremission/vr-documents-view.jsp"%>
			</div>
			<div class="panel-heading">
						<div class="panel-title">
							VR Details History
							<div class="panel-body history-slide">
							<div class="row add-margin visible-sm visible-md visible-lg header-color">
								<div class="col-xs-3 add-margin"><spring:message code="lbl.date"/></div>
								<div class="col-xs-3 add-margin"><spring:message code="lbl.comments" /></div>
							</div>
							<c:choose>
									<c:when test="${!detailsHistory.isEmpty()}">
										<c:forEach items="${detailsHistory}" var="history">
										<div class="row add-margin">
											<div class="col-sm-2 col-xs-12 add-margin">
												<fmt:formatDate value="${history.checkinDate}" var="historyDate"
													pattern="dd/MM/yyyy" />
												<c:out value="${historyDate}" />
											</div>
											<div class="col-sm-2 col-xs-12 add-margin">
												<c:out value="${history.comments}" />&nbsp;
											</div>
										</div>
										</c:forEach>
									</c:when>
								</c:choose>
							
						</div>
						</div>
					</div>
				</div>
				
			</div> <!-- end of main-content -->
			
		<jsp:include page="../common/commonWorkflowMatrix.jsp" />
		<div class="buttonbottom" align="center">
			<jsp:include page="../common/commonWorkflowMatrix-button.jsp" />
		</div>
</form:form>

<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>

