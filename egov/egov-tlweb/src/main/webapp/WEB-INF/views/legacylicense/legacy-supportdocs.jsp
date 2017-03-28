<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2017>  eGovernments Foundation
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
  --%>

<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<div class="col-md-12">
	<div class="form-group view-content header-color hidden-xs">
		<div class="col-sm-1">
			<spring:message code="doctable.sno" />
		</div>
		<div class="col-sm-3">
			<spring:message code="doctable.docname" />
		</div>
		<div class="col-sm-4">
			<spring:message code="doctable.attach.doc" />
		</div>
		<div class="col-sm-3">
			<spring:message code="license.remarks" />
		</div>
	</div>
	<c:forEach items="${documentTypes}" varStatus="stat" var="documentType">
		<div class="form-group">
			<div class="col-sm-1">
				<c:out value="${stat.index + 1}" />
			</div>
			<div class="col-sm-3">
				<span class="docname"><c:out value="${documentType.name}" /></span>
				<c:if test="mandatory">
					<span class="mandatory"></span>
				</c:if>
				<form:hidden path="documents[${stat.index}].type.id"
					id="documents${stat.index}type.id" value="${documentType.id}" />
				<form:hidden path="documents[${stat.index}].type.name"
					id="documents${stat.index}type.name" value="${documentType.name}" />
			</div>
			<div class="col-sm-4">
				<input type="file" name="files" id="uploadFile${stat.index}"
					class="file-ellipsis upload-file" />
				<else> <c:forEach
					items="${tradeLicense.documents[stat.index].files}"
					var="getdocuments">
					<a href="javascript:viewDocument('<c:out value="${getdocuments.fileStoreId}"/>')">

						<c:out value="${getdocuments.fileName}" />
					</a>
				</c:forEach> </else>
				<form:errors path="documents[${stat.index}].files"
					class="add-margin error-msg" />
			</div>
			<div class="col-sm-3 add-margin">
				<form:textarea path="documents[${stat.index}].description"
					class="form-control" />
			</div>
		</div>
	</c:forEach>
</div>

<script type="text/javascript"
	src="<cdn:url  value='/resources/js/app/license-support-docs.js?rnd=${app_release_no}'/>"></script>