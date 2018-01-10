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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="panel panel-primary">
<div class="panel-heading">
	<div class="panel-title">
		<spring:message code="lbl.encloseddocuments"/>
	</div>
</div>
	<table class="table table-bordered">
	<c:if test="${not empty applicationDocList}">
		<thead>
			<tr>
				<th><spring:message code="lbl.slno" /></th>
				<th><spring:message code="lbl.documentname" /></th>
				<th><spring:message code="lbl.documentnumber" /></th>
				<th><spring:message code="lbl.documentdate" /></th>
				<c:if test="${mode!='ack'}">
					<th><spring:message code="lbl.files"/></th>
				</c:if>
			</tr>
		</thead>
		</c:if>
		<c:choose>
			<c:when test="${not empty applicationDocList}">
				<c:forEach items="${applicationDocList}" var="docs" varStatus="serialNo">
					<tbody>
						<tr>
							<td><c:out value="${serialNo.count}"/></td>
							<td><c:out value="${docs.documentNames.documentName}" /></td>
							<td><c:out value="${docs.documentNumber}" /></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy" value="${docs.documentDate}" var="docsDate"/><c:out value="${docsDate}" /></td>
							<c:if test="${mode!='ack'}">
							<td><c:forEach items="${docs.getSupportDocs()}" var="file">
									<a href="/egi/downloadfile?fileStoreId=${file.fileStoreId}&moduleName=WTMS" target="_blank"> 
									<c:out value="${file.fileName}"/></a>
								</c:forEach>
							</td>
							</c:if>
						</tr>
					</tbody>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<div class="col-md-12 col-xs-6  panel-title">No documents found</div>
			</c:otherwise>
		</c:choose>
	</table>
	</div>
