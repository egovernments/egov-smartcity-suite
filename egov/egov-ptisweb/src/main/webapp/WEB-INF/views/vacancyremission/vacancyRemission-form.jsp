<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~      accountability and the service delivery of the government  organizations.
  ~
  ~       Copyright (C) 2016  eGovernments Foundation
  ~
  ~       The updated version of eGov suite of products as by eGovernments Foundation
  ~       is available at http://www.egovernments.org
  ~
  ~       This program is free software: you can redistribute it and/or modify
  ~       it under the terms of the GNU General Public License as published by
  ~       the Free Software Foundation, either version 3 of the License, or
  ~       any later version.
  ~
  ~       This program is distributed in the hope that it will be useful,
  ~       but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~       GNU General Public License for more details.
  ~
  ~       You should have received a copy of the GNU General Public License
  ~       along with this program. If not, see http://www.gnu.org/licenses/ or
  ~       http://www.gnu.org/licenses/gpl.html .
  ~
  ~       In addition to the terms of the GPL license to be adhered to in using this
  ~       program, the following additional terms are to be complied with:
  ~
  ~           1) All versions of this program, verbatim or modified must carry this
  ~              Legal Notice.
  ~
  ~           2) Any misrepresentation of the origin of the material is prohibited. It
  ~              is required that all modified versions of this material be marked in
  ~              reasonable ways as different from the original version.
  ~
  ~           3) This license does not grant any rights to any user of the program
  ~              with regards to rights under trademark law for use of the trade names
  ~              or trademarks of eGovernments Foundation.
  ~
  ~     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
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
<c:if test="${errorMsg != ''}">
 	<div class="panel-heading">
				<div class="add-margin error-msg" style="text-align:center;">
					<strong><c:out value="${errorMsg}"/></strong>
				</div>
	</div>
</c:if>
<form:form id="vacancyRemissionForm" method="post"
	class="form-horizontal form-groups-bordered" modelAttribute="vacancyRemission" enctype="multipart/form-data">
	<div class="page-container" id="page-container">
        	<div class="main-content">
			<jsp:include page="../common/commonPropertyDetailsView.jsp"></jsp:include>
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary" data-collapsed="0">
							<form:hidden path="" name="propertyByEmployee" id="propertyByEmployee" value="${propertyByEmployee}" />
							<form:hidden path="" name="applicationSource" value="${applicationSource}"/>
							<div class="panel-heading" style="text-align: left">
								<div class="panel-title"><spring:message code="lbl.vacancyremission.details" /></div>
							</div>
							<div class="panel-body custom-form">
							       <div class="panel-body custom-form">
							       	<jsp:include page="vacancy-remission-documents.jsp"></jsp:include>
							     </div>
								<div class="form-group">
									<label class="col-sm-3 control-label text-right">
										<spring:message code="lbl.fromDate" /> <span class="mandatory"></span>
									</label>
									<div class="col-sm-3 add-margin">
										<form:input path="vacancyFromDate" id="fromDate" type="text" class="form-control datepicker" data-date-start-date="0d" required="required" />
										<form:errors path="vacancyFromDate" cssClass="add-margin error-msg"/>
									</div>
                                    <label class="col-sm-2 control-label text-right">
                                    	<spring:message code="lbl.toDate" /> <span class="mandatory"></span>
									</label>
									<div class="col-sm-3 add-margin">
										<form:input path="vacancyToDate" id="toDate" type="text" class="form-control datepicker" data-date-start-date="0d" required="required" />
										<form:errors path="vacancyToDate" cssClass="add-margin error-msg"/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label text-right">
										<spring:message code="lbl.vacancy.comments" /> <span class="mandatory"></span>
									</label>
									<div class="col-sm-8 add-margin">
										<form:textarea path="vacancyComments" class="form-control" required="required" />
										<form:errors path="vacancyComments" cssClass="add-margin error-msg"/>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<c:if test="${propertyByEmployee == true}">
					<jsp:include page="../common/commonWorkflowMatrix.jsp"/>
				</c:if>
				<div class="buttonbottom" align="center">
					<jsp:include page="../common/commonWorkflowMatrix-button.jsp" />
				</div>
			</div> <!-- end of main-content -->
		</div>
</form:form>

<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>

