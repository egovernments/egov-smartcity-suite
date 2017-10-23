<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="panel-heading slide-document-menu">
	<div class="panel-title">
		<spring:message code="lbl.applicant.docs" />
	</div>
	<div class="history-icon">
		<i class="fa fa-angle-up fa-2x" id="toggle-his-icon"></i>
	</div>
</div>
<div class="documentAttach-slide display-hide">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.common.docs" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-offset-5 view-content"> <spring:message
				code="lbl.submitted.by.couple" />
		</label>
	</div>

	<c:forEach var="doc" items="${generalDocuments}" varStatus="status">
		<div class="form-group">
			<div class="col-sm-1"></div>
			<form:hidden id="documents[${status.index}].id"
				path="documents[${status.index}].id" value="${doc.id}" />
			<div class="col-sm-4 add-margin text-right">
				<c:out value="${doc.name}"></c:out>
			</div>
			<div class="col-sm-2 add-margin text-center">
				<input type="file" id="file${status.index}id"
					name="documents[${status.index}].file"
					class="file-ellipsis upload-file">
				<form:errors path="documents[${status.index}].file"
					cssClass="add-margin error-msg" />
			</div>
			<div class="col-sm-2">
				<c:set value="false" var="isDocFound"></c:set>
				<c:forEach items="${marriageRegistration.registrationDocuments}"
					var="regdoc" varStatus="loopStatus">
					<c:if test="${regdoc.document.id == doc.id}">
						<c:set value="true" var="isDocFound"></c:set>
						<input type="hidden" id="registrationfile${status.index}"
							value="${regdoc.fileStoreMapper.fileName}|${regdoc.fileStoreMapper.contentType}|${regdoc.base64EncodedFile}">
						<a id="regdoc${status.index}">
							${regdoc.fileStoreMapper.fileName}</a>
					</c:if>
				</c:forEach>
				<%-- <c:if test="${!isDocFound}">
				NA
			</c:if> --%>
			</div>
		</div>
	</c:forEach>

	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.indi.docs" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-offset-5 col-sm-1 view-content">
			<spring:message code="lbl.husband" />
		</div>
		<div class="col-sm-5 text-center view-content">
			<spring:message code="lbl.wife" />
		</div>
	</div>

	<c:forEach var="doc" items="${individualDocuments}" varStatus="status">

		<div class="form-group">
			<div class="col-sm-1"></div>
			<form:hidden id="husband.documents[${status.index}].id"
				path="husband.documents[${status.index}].id" value="${doc.id}" />
			<form:hidden id="wife.documents[${status.index}].id"
				path="wife.documents[${status.index}].id" value="${doc.id}" />
			<div class="col-sm-4 add-margin text-right">
				<c:out value="${doc.name}"></c:out>
			</div>
			<div class="col-sm-2 add-margin text-center">
				<input type="file" id="file${status.index}id"
					name="husband.documents[${status.index}].file"
					class="file-ellipsis upload-file">
				<form:errors path="husband.documents[${status.index}].file"
					cssClass="add-margin error-msg" />
				&nbsp;&nbsp;
				<c:set value="false" var="isDocFound"></c:set>
				<c:forEach
					items="${marriageRegistration.husband.applicantDocuments}"
					var="appdoc" varStatus="loopStatus">
					<c:if test="${appdoc.document.id == doc.id}">
						<c:set value="true" var="isDocFound"></c:set>
						<input type="hidden" id="husbandfile${status.index}"
							value="${appdoc.fileStoreMapper.fileName}|${appdoc.fileStoreMapper.contentType}|${appdoc.base64EncodedFile}">
						<a id="husbanddoc${status.index}">
							${appdoc.fileStoreMapper.fileName}</a>
					</c:if>
				</c:forEach>
				<%-- <c:if test="${!isDocFound}">
				NA
			</c:if> --%>
			</div>
			<div class="col-sm-1"></div>
			<div class="col-sm-2 add-margin text-center">
				<input type="file" id="file${status.index}id"
					name="wife.documents[${status.index}].file"
					class="file-ellipsis upload-file">
				<form:errors path="wife.documents[${status.index}].file"
					cssClass="add-margin error-msg" />

				<c:set value="false" var="isDocFound"></c:set>
				<c:forEach items="${marriageRegistration.wife.applicantDocuments}"
					var="appdoc" varStatus="loopStatus">
					<c:if test="${appdoc.document.id == doc.id}">
						<c:set value="true" var="isDocFound"></c:set>
						<input type="hidden" id="wifefile${status.index}"
							value="${appdoc.fileStoreMapper.fileName}|${appdoc.fileStoreMapper.contentType}|${appdoc.base64EncodedFile}">
						<a id="wifedoc${status.index}">
							${appdoc.fileStoreMapper.fileName}</a>
					</c:if>
				</c:forEach>
				<%-- <c:if test="${!isDocFound}">
				NA
			</c:if> --%>
			</div>
		</div>
	</c:forEach>
</div>

<script
	src="<cdn:url value='/resources/js/app/documentsupload.js?rnd=${app_release_no}'/>"></script>
<script
	src="<cdn:url value='/resources/js/app/viewdocumentsupload.js?rnd=${app_release_no}'/>"></script>