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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<style>
body
{
  font-family:regular !important;
  font-size:14px;
}
</style>
<script type="text/javascript" src="<cdn:url value='/resources/javascript/validations.js'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/javascript/dateValidation.js'/>"></script>
<script type="text/javascript">
function addMonths(dateObj) {
	var stringDate = dateObj.value;
	var pattern = /^(\d{1,2})\/(\d{1,2})\/(\d{4})$/;
	if(!pattern.test(stringDate)){
		return;
	}
	var arrayDate = stringDate.match(pattern);
	var fromDate = new Date(arrayDate[3], arrayDate[2] - 1, arrayDate[1]);
    var temp = new Date(fromDate.getFullYear(), fromDate.getMonth(), 1);
    temp.setMonth(temp.getMonth() + (7));
    temp.setDate(temp.getDate() - 1); 
    if (fromDate.getDate() < temp.getDate()) { 
        temp.setDate(fromDate.getDate()); 
    }
    temp.setDate(temp.getDate() - 1); 
    var todate = temp.getDate() + '/' + (temp.getMonth() + 1) + '/' + temp.getFullYear();
    jQuery('#toDate').val(todate);
    
};
jQuery(document).on('click', "#Forward", function () {
	jQuery('#toDate').removeAttr('disabled');
	return true;
});
</script>
<form:form id="editVacancyRemissionForm" method="post"
	class="form-horizontal form-groups-bordered" modelAttribute="vacancyRemission">
	<div class="page-container" id="page-container">
			<form:hidden path="" id="workFlowAction" name="workFlowAction" />
			<c:if test="${allowUpdate}">
			<div class="add-margin error-msg"  style="text-align: center">
			  <font size="5">   <spring:message code="lbl.validate.first.monthly.update" /></font> 
			</div>
			</c:if>
			<c:if test="${!allowUpdate && updated==true}">
			<div class="add-margin error-msg"  style="text-align: center">
			  <font size="5">   <spring:message code="lbl.vr.monthlyupdate.validation" /></font> 
			</div>
			</c:if>
        	<div class="main-content">
				<jsp:include page="../common/commonPropertyDetailsView.jsp"></jsp:include>
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary" data-collapsed="0">
							<form:hidden path="" name="propertyByEmployee" id="propertyByEmployee" value="${propertyByEmployee}" />
							<div class="panel-heading" style="text-align: left">
								<div class="panel-title"><spring:message code="lbl.vacancyremission.details" /></div>
							</div>
							<c:choose>
							<c:when test="${!detailsHistory.isEmpty()}">
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
							</c:when>
							</c:choose>
							<div class="panel-body custom-form">
							<c:if test="${attachedDocuments.size() > 0}">
								<div>
									<%@ include
										file="/WEB-INF/views/vacancyremission/vr-documents-view.jsp"%>
								</div>
							</c:if>
							<div class="form-group">
									<label class="col-sm-3 control-label text-right">
										<spring:message code="lbl.fromDate" /> <span class="mandatory"></span>
									</label>
									<c:choose>	
								<c:when test="${(currentState.endsWith('NEW'))}">
									<div class="col-sm-3 add-margin">
										<form:input path="vacancyFromDate" id="fromDate" type="text" class="form-control datepicker" data-date-start-date="0d" required="required" onChange="addMonths(this);"/>
										<form:errors path="vacancyFromDate" cssClass="add-margin error-msg"/>
									</div>
									</c:when>
									<c:otherwise>
									<div class="col-sm-3 add-margin">
										<form:input path="vacancyFromDate" id="fromDate" type="text" class="form-control datepicker" data-date-start-date="0d" required="required" readonly="true" disabled="true"/>
										<form:errors path="vacancyFromDate" cssClass="add-margin error-msg"/>
									</div>
									</c:otherwise>
									</c:choose>
                                    <label class="col-sm-2 control-label text-right">
                                    	<spring:message code="lbl.toDate" /> <span class="mandatory"></span>
									</label>
									<div class="col-sm-3 add-margin">
										<form:input path="vacancyToDate" id="toDate" type="text" class="form-control datepicker" data-date-start-date="0d" required="required" readonly="true" disabled="true"/>
										<form:errors path="vacancyToDate" cssClass="add-margin error-msg"/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label text-right">
										<spring:message code="lbl.vacancy.comments" /> <span class="mandatory"></span>
									</label>
									<div class="col-sm-8 add-margin">
										<form:textarea path="vacancyComments" class="form-control" required="required" readonly="true" disabled="true"/>
										<form:errors path="vacancyComments" cssClass="add-margin error-msg"/>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<c:choose>	
				<c:when test="${!fn:containsIgnoreCase(userDesignationList, designation)}">
				<jsp:include page="../common/commonWorkflowMatrix.jsp"/>
				</c:when>
				<c:otherwise>
					<c:choose>
					<c:when test="${!endorsementNotices.isEmpty()}"> 
 						<jsp:include page="/WEB-INF/views/common/endorsement_history.jsp"/>
					</c:when>
					</c:choose>
				</c:otherwise>
				</c:choose>
				 <c:choose>
            <c:when test="${nextAction == 'END' || fn:containsIgnoreCase(userDesignationList, designation)}">
		     <div class="row">
					<label class="col-sm-3 control-label text-right"><spring:message code="lbl.comments"/></label>
					<div class="col-sm-8 add-margin">
						<form:textarea class="form-control" path=""  id="approvalComent" name="approvalComent" />
					</div>
				</div>
		   </c:when>
		   </c:choose>
		
				<div class="buttonbottom" align="center">
					<jsp:include page="../common/commonWorkflowMatrix-button.jsp" />
				</div>
			</div> <!-- end of main-content -->
		</div>
</form:form>

<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
