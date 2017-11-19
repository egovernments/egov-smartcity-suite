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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div class="panel panel-primary" data-collapsed="0">
<c:choose>
<c:when test="${!documentNamesList.isEmpty()}">
<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
	<spring:message code="lbl.encloseddocuments"/> - <spring:message code="lbl.checklist"/>
	</div>
</div>
<div class="panel-body">
	<div class="form-group col-sm-12 view-content header-color hidden-xs">
		<div class="col-sm-3"><spring:message code="lbl.documentname"/></div>											
		<div class="col-sm-3 text-center"><spring:message code="lbl.documentnumber"/>(<span class="mandatory"></span>)</div>										
		<div class="col-sm-3 text-center"><spring:message code="lbl.documentdate"/>(<span class="mandatory"></span>)</div>
		<div class="col-sm-3 text-center"><spring:message code="lbl.attachdocument"/>(<span class="mandatory"></span>)<br><small class="error-msg"><spring:message code="lbl.mesg.document"/></small></div>																							
	</div>
	<c:forEach var="docs" items="${documentNamesList}" varStatus="status">	  
<div class="form-group">	
 	<div class="col-sm-3 add-margin check-text">
		<c:choose>
			<c:when test="${docs.documentTypeMaster.isMandatory}">
				<input type="checkbox" checked disabled required="required">&nbsp;<c:out value="${docs.documentTypeMaster.description}" />  
			</c:when>
			<c:otherwise>
				<input type="checkbox" disabled>&nbsp;<c:out value="${docs.documentTypeMaster.description}"/> 
			</c:otherwise>		
		</c:choose> 
		<form:hidden id="appDetailsDocument${status.index}documentTypeMaster.id" path="appDetailsDocument[${status.index}].documentTypeMaster.id" value="${docs.documentTypeMaster.id}" /> 
		<form:hidden id="appDetailsDocument${status.index}documentTypeMaster" path="appDetailsDocument[${status.index}].documentTypeMaster.isMandatory" value="${docs.documentTypeMaster.isMandatory}"/> 
		<form:hidden id="appDetailsDocument${status.index}documentTypeMaster.description" path="appDetailsDocument[${status.index}].documentTypeMaster.description" /> 

	</div>
	<div class="col-sm-3 add-margin text-center">
		<c:choose>
			<c:when test="${docs.documentTypeMaster.isMandatory}">
				<form:input class="form-control patternvalidation" data-pattern="alphanumerichyphenbackslash" id="appDetailsDocument${status.index}documentNumber" path="appDetailsDocument[${status.index}].documentNumber" maxlength="20" required="required" isMandatory="isMandatory" />
			</c:when>
			<c:otherwise>
				<form:input class="form-control patternvalidation" data-pattern="alphanumerichyphenbackslash" id="appDetailsDocument${status.index}documentNumber" path="appDetailsDocument[${status.index}].documentNumber" maxlength="20" />
			</c:otherwise>		
		</c:choose> 
		<form:errors path="appDetailsDocument[${status.index}].documentNumber" cssClass="add-margin error-msg" />
	</div>
	<div class="col-sm-3 add-margin text-center">
		<c:choose>
			<c:when test="${docs.documentTypeMaster.isMandatory}">
				<form:input class="form-control datepicker" data-date-end-date="0d" id="appDetailsDocument${status.index}documentDate" path="appDetailsDocument[${status.index}].documentDate" required="required" isMandatory="isMandatory"/>
			</c:when>
			<c:otherwise>
				<form:input class="form-control datepicker" data-date-end-date="0d" id="appDetailsDocument${status.index}documentDate" path="appDetailsDocument[${status.index}].documentDate" />
			</c:otherwise>		
		</c:choose> 
			<form:errors path="appDetailsDocument[${status.index}].documentDate" cssClass="add-margin error-msg" />
	</div>
	
	<%-- <div class="col-sm-3 add-margin text-center">
		<c:choose>
			<c:when test="${docs.documentTypeMaster.isMandatory}">
				<input type="file" id="file${status.index}id" name="appDetailsDocument[${status.index}].files" class="file-ellipsis upload-file" required="required" isMandatory="isMandatory">
			</c:when>
			<c:otherwise>
				<input type="file" id="file${status.index}id" name="appDetailsDocument[${status.index}].files" class="file-ellipsis upload-file">
			</c:otherwise>		
		</c:choose> 
		<form:errors path="appDetailsDocument[${status.index}].files" cssClass="add-margin error-msg" />
	</div>  --%>
	
	 
	 <div class="col-sm-3 add-margin text-center">
		<c:choose>
			<c:when test="${docs.documentTypeMaster.isMandatory}">
							<c:choose>
								<c:when test="${mode == 'editOnReject' && docs.documentTypeMaster.isMandatory}">
									<c:forEach items="${docs.getFileStore()}" var="file">
										<a href="/egi/downloadfile?fileStoreId=${file.fileStoreId}&moduleName=STMS" target="_blank"> 
										<c:out value="${file.fileName}"/></a>
									</c:forEach>
									
									<c:if test="${fn:length(docs.getFileStore()) eq 0}">
										       <input type="file" id="file${status.index}id" name="appDetailsDocument[${status.index}].files"  class="file-ellipsis upload-file" required="required" isMandatory="isMandatory">
								   </c:if>
							   </c:when>
							   <c:otherwise>
								   <input type="file" id="file${status.index}id" name="appDetailsDocument[${status.index}].files" class="file-ellipsis upload-file" required="required" isMandatory="isMandatory">
							   </c:otherwise>
						  </c:choose>
			</c:when>
			<c:otherwise>
								<c:choose>
									<c:when test="${mode == 'editOnReject'}">
										<c:forEach items="${docs.getFileStore()}" var="file">
											<a href="/egi/downloadfile?fileStoreId=${file.fileStoreId}&moduleName=STMS" target="_blank"> 
											<c:out value="${file.fileName}"/></a>
										</c:forEach>
											
										<c:if test="${fn:length(docs.getFileStore()) eq 0}">
										    <input type="file" id="file${status.index}id" name="appDetailsDocument[${status.index}].files" class="file-ellipsis upload-file">
										  </c:if>
									</c:when>
									<c:otherwise>
										  <input type="file" id="file${status.index}id" name="appDetailsDocument[${status.index}].files" class="file-ellipsis upload-file">
									</c:otherwise>
							</c:choose>
			</c:otherwise>		
		</c:choose> 
		<form:errors path="appDetailsDocument[${status.index}].files" cssClass="add-margin error-msg" />
	</div> 
</div>
</c:forEach> 
</div>




</c:when>
</c:choose>
</div>