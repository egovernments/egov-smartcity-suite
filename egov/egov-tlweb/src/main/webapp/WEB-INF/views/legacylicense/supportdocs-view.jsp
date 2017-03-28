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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<div class="col-md-12 col-sm-12 col-xs-12">
<c:if test="${tradeLicense.documents.size()>0}">
	<div class="row form-group view-content header-color hidden-xs">
		<div class="col-md-1 col-xs-1">
			<spring:message code="doctable.sno" />
		</div>
		<div class="col-md-2 col-xs-2">
			<spring:message code="doctable.docname" />
		</div>
		<div class="col-md-3 col-xs-3 text-center">
			<spring:message code="doctable.docenclosed" />
		</div>
		<div class="col-md-3 col-xs-3">
			<spring:message code="doctable.attach.doc" />
		</div>
		<div class="col-md-3 col-xs-3">
			<spring:message code="license.remarks" />
		</div>
	</div>
</c:if>
	<c:forEach items="${tradeLicense.documents}" varStatus="status"
		var="document">
		<div class="row form-group">
			<div class="col-md-1 col-xs-1">
				<c:out value="${status.index + 1}" />
			</div>
			<div class="col-md-2 col-xs-2">
				<c:out value="${tradeLicense.documents[status.index].type.name}" />
			</div>
			<div class="col-md-3 col-xs-3 text-center">
				<c:choose>
					<c:when test="${tradeLicense.documents[status.index].enclosed}">Yes</c:when>
					<c:otherwise>No</c:otherwise>
				</c:choose>
			</div>
			<div class="col-md-3 col-xs-3">
				<c:if test="${tradeLicense.documents[status.index].files.isEmpty()}">
				N/A
			</c:if>
				<else> <c:forEach
					items="${tradeLicense.documents[status.index].files}"
					var="getdocuments">
					<a href="javascript:viewDocument('<c:out value="${getdocuments.fileStoreId}"/>')">
						<c:out value="${getdocuments.fileName}" />
					</a>
				</c:forEach> </else>
			</div>
			<div class="col-md-3 col-xs-3">
				<textarea name="documents[${status.index}].description"
					class="form-control" readonly="true">${document.description}</textarea>
			</div>
		</div>
	</c:forEach>
</div>
<script
	src="<cdn:url  value='/resources/js/app/license-support-docs.js?rnd=${app_release_no}'/>"></script>