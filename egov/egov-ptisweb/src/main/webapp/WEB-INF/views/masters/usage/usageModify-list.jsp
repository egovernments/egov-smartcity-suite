<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<form:form method="post" class="form-horizontal form-groups-bordered" id="usage-list" 
cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">
<input type="hidden" name="propertyUsages" id="propertyUsages" value="${propertyUsages}">
<input type="hidden" id="propertyUsageid" name="propertyUsageid" value="${propertyUsage.id}" />

				<table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="table table-bordered datatable" id="propertyUsageTbl">
					<thead>
						<tr>
							<th colspan="1" class="text-center">
								<spring:message code="lbl.name" />
							</th>
							<th colspan="1" class="text-center">
								<spring:message code="lbl.code" />
							</th>
							<th colspan="1" class="text-center">
								<spring:message code="lbl.propertytype" />
							</th>
							<th colspan="1" class="text-center">
								<spring:message code="lbl.status"/>
							</th>
							<th colspan="1" class="text-center">
								<spring:message code="lbl.edit" />
							</th>
						</tr>
					</thead>
					<c:forEach var="propertyUsage" items="${propertyUsages}">
						<tr>
							<td colspan="1">
								<div align="center">
									<c:out value="${propertyUsage.usageName}" />
								</div>
							</td>
							<td colspan="1">
								<div align="center">
									<c:out value="${propertyUsage.usageCode}" />
								</div>
							</td>
							<td colspan="1">
								<div align="center">
								<c:choose>
									<c:when test="${propertyUsage.isResidential == 'true'}">
										<c:out value="Residential" />
									</c:when> 
									<c:otherwise>
										<c:out value="Non Residential" />
									</c:otherwise>
								</c:choose>
								</div>
							</td>
							<td colspan="1">
								<div align="center">
								<c:choose>
									<c:when test="${propertyUsage.isActive == 'true'}">
										<c:out value="ACTIVE" />
									</c:when> 
									<c:otherwise>
										<c:out value="INACTIVE" />
									</c:otherwise>
								</c:choose>
								</div>
							</td>
							<td colspan="1">
								<div align="center">
									<a href="javascript:void(0);" onclick="edit('<c:out value="${propertyUsage.id}" />');">Edit</a>
								</div>
							</td>
						</tr>
					</c:forEach>
				</table>
</form:form>

<link rel="stylesheet"
	href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url value='/resources/js/app/usage.js'/> "></script>