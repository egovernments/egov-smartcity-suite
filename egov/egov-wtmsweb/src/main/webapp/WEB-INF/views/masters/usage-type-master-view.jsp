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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<form:form method="post" action="" class="form-horizontal form-groups-bordered" id="usagetype-view" 
 cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">
	<input type="hidden" name="usageTypeList" id="usageTypeList" value="${usageTypeList}">
	<input type="hidden" id="usagetypeid" name="usagetypeid" value="${usageType}" />
	<input type="hidden" value="${mode}" id="mode" />
	<c:if test="${not empty message}">
                <div role="alert">${message}</div>
             </c:if>
			<c:choose>
				<c:when test="${usageTypeList.isEmpty()}">
					<div class="form-group" align="center">No Master Data</div>
				</c:when>
			<c:otherwise>
				<table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="table table-bordered datatable" id="usageTypeTbl">
					<thead>
						<tr>
							<th colspan="1" class="text-center">
								<spring:message code="lbl.code" />
							</th>
							<th colspan="1" class="text-center">
								<spring:message code="lbl.usagetype" />
							</th>
							<th align="center" colspan="1" class="text-center">
								<spring:message code="lbl.status"/>
							</th>
							
							 <c:if test="${mode == 'edit'}"> 
							<th colspan="1" class="text-center">
								<spring:message code="lbl.edit" />
							</th>
							 </c:if> 
						</tr>
					</thead>
					<c:forEach var="usageType" items="${usageTypeList}">
						<tr>
							<td colspan="1">
								<div align="center">
									<c:out value="${usageType.code}" />
								</div>
							</td>
							<td colspan="1">
								<div align="center">
									<c:out value="${usageType.name}" />
								</div>
							</td>
							<td colspan="1">
								<div align="center">
								<c:choose>
									<c:when test="${usageType.active == 'true'}">
										<c:out value="ACTIVE" />
									</c:when> 
									<c:otherwise>
										<c:out value="INACTIVE" />
									</c:otherwise>
								</c:choose>
								</div>
							</td>
							 <c:if test="${mode == 'edit'}">
							<td colspan="1">
								<div align="center">
									<a href="javascript:void(0);" onclick="edit('<c:out value="${usageType.id}" />');">Edit</a>
								</div>
							</td>
							</c:if>
						</tr>
					</c:forEach>
				</table>
			</c:otherwise>
			</c:choose>
			<div class="form-group text-center">
				 <a onclick="self.close()" class="btn btn-default" href="javascript:void(0)"><spring:message code="lbl.close" /></a> 
			</div>
		
</form:form>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url value='/resources/js/app/usage-type-master.js?rnd=${app_release_no}'/>"></script>