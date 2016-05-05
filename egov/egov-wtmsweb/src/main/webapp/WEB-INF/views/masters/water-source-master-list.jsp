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

<form:form method="post" action="" class="form-horizontal form-groups-bordered" id="watersourcetype-view" 
 cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">
	<input type="hidden" name="waterSourceList" id="waterSourceList" value="${waterSourceList}">
	<input type="hidden" id="watersourceid" name="watersourceid" value="${waterSource.id}" />

	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-body custom-form ">
			<c:if test="${not empty message}">
                  <div class="alert alert-success" role="alert">${message}</div>
              </c:if>
			<c:choose>
				<c:when test="${connectionCategoryList.isEmpty()}">
					<div class="form-group" align="center">No Master Data</div>
				</c:when>
			<c:otherwise>
				<table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="table table-bordered">
					<thead>
						<tr>
							<th colspan="1">
								<div align="center"><spring:message code="lbl.code" /></div>
							</th>
							<th colspan="1">
								<div align="center"><spring:message code="lbl.watersourcetype" /></div>
							</th>
							<th align="center" colspan="1">
								<div align="center"><spring:message code="lbl.status"/></div>
							</th>
							<th colspan="1">
								<div align="center"><spring:message code="lbl.edit" /></div>
							</th>
						</tr>
					</thead>
					<c:forEach var="waterSource" items="${waterSourceList}">
						<tr>
							<td colspan="1">
								<div align="center">
									<c:out value="${waterSource.code}" />
								</div>
							</td>
							<td colspan="1">
								<div align="center">
									<c:out value="${waterSource.waterSourceType}" />
								</div>
							</td>
							<td colspan="1">
								<div align="center">
								<c:choose>
									<c:when test="${waterSource.active == 'true'}">
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
									<a href="javascript:void(0);" onclick="edit('<c:out value="${waterSource.id}" />');">Edit</a>
								</div>
							</td>
						</tr>
					</c:forEach>
				</table>
			</c:otherwise>
			</c:choose>
			<div class="form-group text-center">
				<a onclick="addNew()" class="btn btn-primary" href="javascript:void(0)"><spring:message code="lbl.addnew" /></a>
				<a onclick="self.close()" class="btn btn-default" href="javascript:void(0)"><spring:message code="lbl.close" /></a>
			</div>
		</div>
	</div>
</form:form>
<link rel="stylesheet" href="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
                <script src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	            type="text/javascript"></script>
                <script src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	            type="text/javascript"></script>
                <script src="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
	            type="text/javascript"></script>
	            <script src="<c:url value='/resources/js/app/water-source-master.js?rnd=${app_release_no}'/>"></script>