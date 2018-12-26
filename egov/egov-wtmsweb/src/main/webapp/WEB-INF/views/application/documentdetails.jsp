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

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<c:choose>
<c:when test="${not empty documentNamesList}">
<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
	<spring:message code="lbl.encloseddocuments"/> - <spring:message code="lbl.checklist"/>
	</div>
</div>
<input type="hidden" name="documentList" id="documentList" value="${documentNamesList}"/>
<div class="row">
	<div class="col-md-12">
		<table class="table table-bordered table-hover multiheadertbl sorted-table" id="documentListTable">
			<thead>
				<tr>
				<th width="1%"></th>
				<th width="5%"><spring:message code="lbl.documentname"/></th>
				<th width="5%"><spring:message code="lbl.documentnumber"/></th>
				<th width="5%"><spring:message code="lbl.documentdate"/></th>
				<th width="5%"><spring:message code="lbl.attachdocument"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="docs" items="${documentNamesList}" varStatus="status">
					<form:hidden id="applicationDocs${status.index}documentNames.id" path="applicationDocs[${status.index}].documentNames.id" value="${docs.id}" /> 
					<form:hidden id="applicationDocs${status.index}documentNames" path="applicationDocs[${status.index}].documentNames.required" value="${docs.required}" /> 
					<form:hidden id="applicationDocs${status.index}documentNames.documentName" path="applicationDocs[${status.index}].documentNames.documentName" value="${docs.documentName}" /> 
					
				<tr>
					<c:choose>
						<c:when test="${docs.required}">
							<td>
								<input type="checkbox" class="check_box" checked disabled/> 
							</td>
						</c:when>
						<c:otherwise>
							<td>
								<input type="checkbox" class="check_box" disabled/> 
							</td>
						</c:otherwise>		
					</c:choose> 
					<td>
						<div class="row add-border">
						<div class="col-md-12 add-margin">
							<c:out value="${docs.documentName}" />
						</div>
						</div>
					</td>
					<c:choose>
						<c:when test="${docs.required}">
							<td>
								<form:input class="form-control patternvalidation" data-pattern="alphanumerichyphenbackslash" id="applicationDocs${status.index}documentNumber" path="applicationDocs[${status.index}].documentNumber" minlength="3" maxlength="50" required="required" />
							</td>
						</c:when>
						<c:otherwise>
							<td>
								<form:input class="form-control patternvalidation" data-pattern="alphanumerichyphenbackslash" id="applicationDocs${status.index}documentNumber" path="applicationDocs[${status.index}].documentNumber" minlength="3" maxlength="50" />
							</td>						
						</c:otherwise>		
					</c:choose> 
					<form:errors path="applicationDocs[${status.index}].documentNumber" cssClass="add-margin error-msg" />
					<c:choose>
						<c:when test="${docs.required}">
							<td>
								<form:input class="form-control datepicker" data-date-end-date="0d" id="applicationDocs${status.index}documentDate" path="applicationDocs[${status.index}].documentDate" required="required"/>
							</td>
						</c:when>
						<c:otherwise>
							<td>
								<form:input class="form-control datepicker" data-date-end-date="0d" id="applicationDocs${status.index}documentDate" path="applicationDocs[${status.index}].documentDate" />
							</td>
						</c:otherwise>		
					</c:choose> 
					<form:errors path="applicationDocs[${status.index}].documentDate" cssClass="add-margin error-msg" />
					<c:choose>
						<c:when test="${docs.required}">
							<td>
								<input type="file" id="file${status.index}id" name="applicationDocs[${status.index}].files" class="file-ellipsis upload-file" required="required">
							</td>
						</c:when>
						<c:otherwise>
							<td>
								<input type="file" id="file${status.index}id" name="applicationDocs[${status.index}].files" class="file-ellipsis upload-file">
							</td>
						</c:otherwise>		
					</c:choose> 
						<form:errors path="applicationDocs[${status.index}].files" cssClass="add-margin error-msg" />
					<div class="add-margin error-msg text-left display-hide" >
						<font size="2"><spring:message code="lbl.mesg.document"/></font>
					</div>
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
</c:when>
</c:choose>

<script src="<cdn:url value='/resources/js/app/documentsupload.js?rnd=${app_release_no}'/>"></script>
