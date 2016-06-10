<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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


<div id="baseSORTable" class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="title.sor" />
		</div>
	</div>
	<div class="panel-body" id="sorHeaderTable">
		
		<table class="table table-bordered" id="tblsor">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.code" /></th>
					<th><spring:message code="lbl.description" /></th>
					<th><spring:message code="lbl.uom" /></th>
					<th><spring:message code="lbl.rate" /></th>
					<th><spring:message code="lbl.estimatedquantity" /></th>
					<th><spring:message code="lbl.estimatedamount" /></th>
				</tr>
			</thead>
			<tbody id="sorTable">
				<c:choose>
					<c:when test="${abstractEstimate.activities.size() != 0}">
						<c:forEach items="${abstractEstimate.getActivities()}" var="sorDtls" varStatus="item">
							<c:if test="${sorDtls.schedule != null}">
								<tr >
									<td><span class="spansno"><c:out value="${item.index + 1}" /></span></td>
									<td><c:out value="${sorDtls.schedule.code}"></c:out></td>
								 	<td>
								 		<c:out value="${sorDtls.schedule.getSummary()}"></c:out>
								 		<a href="#" class="hintanchor" title="${sorDtls.schedule.description }"><i class="fa fa-question-circle" aria-hidden="true"></i></a>
								 	</td> 
								 	<td><c:out value="${sorDtls.uom.uom}"></c:out></td>
								 	<td><c:out value="${sorDtls.rate}"></c:out></td>
								 	<td><c:out value="${sorDtls.quantity}"></c:out></td>
								 	<td class="text-right"><c:out value="${sorDtls.rate * sorDtls.quantity}"></c:out></td>
								</tr>
							</c:if>
						</c:forEach>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose> 
			</tbody>
			<tfoot>
				<c:set var="total" value="${0}" scope="session" />
				<c:if test="${abstractEstimate.getActivities() != null}">
					<c:forEach items="${abstractEstimate.getActivities()}" var="sor">
						<c:if test="${sor.schedule != null}">
						<c:set var="total"	value="${total + (sor.rate * sor.quantity) }" />
						</c:if>
					</c:forEach>
				</c:if>
				<tr>
					<td colspan="6" class="text-right"><spring:message
							code="lbl.total" /></td>
					<td class="text-right"><span id="sorTotal"><c:out
								value="${total}" /></span></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
