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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.deductions" />
		</div>
	</div>
	<div class="panel-body">

<table class="table table-bordered" >
			<thead>
				<tr>
					<th><spring:message code="lbl.account.code" /></th>
					<th><spring:message code="lbl.account.head"/></th>
					<th><spring:message code="lbl.percentage"/></th>
					<th><spring:message code="lbl.amtinrs"/></th>
									</tr>
			</thead>
 			<tbody>
					 <c:if test="${abstractEstimate.absrtractEstimateDeductions.size() != 0}">
						<c:forEach items="${abstractEstimate.getAbsrtractEstimateDeductions()}" var="deductionsDtls" varStatus="item"> 
								<tr >
									<td><c:out value="${deductionsDtls.chartOfAccounts.glcode}"></c:out></td>
									<td><c:out value="${deductionsDtls.chartOfAccounts.name}"></c:out></td>
									<td class="text-right"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out value="${deductionsDtls.percentage}" /></fmt:formatNumber></td>
								 	<td class="text-right"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out value="${deductionsDtls.amount}" /></fmt:formatNumber></td>
								</tr>
						 </c:forEach>
					</c:if>  
			</tbody>
			<tfoot>
				<c:set var="deductiontotal" value="${0}" scope="session" />
				 <c:if test="${abstractEstimate.getAbsrtractEstimateDeductions() != null}">
					<c:forEach items="${abstractEstimate.getAbsrtractEstimateDeductions()}" var="deduction">
						<c:set var="deductiontotal"	value="${deductiontotal + deduction.amount }" />
					</c:forEach>
				</c:if>
				 <tr>
					<td colspan="3" class="text-right"><spring:message
							code="lbl.total" /></td>
					<td class="text-right">
						<span><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out value="${deductiontotal}" /></fmt:formatNumber></span>
					</td>
				</tr> 
			</tfoot>
	 	</table>
		</div>
</div>