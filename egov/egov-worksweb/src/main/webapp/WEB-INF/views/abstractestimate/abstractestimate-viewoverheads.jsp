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

<div id="overheadsHeaderTable" class="panel panel-primary" data-collapsed="0">

<div class="panel-body">
<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="tab.header.overhead" />
		</div>
	</div>

<table class="table table-bordered" >
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.name"/></th>
					<th><spring:message code="lbl.percentage"/></th>
					<th><spring:message code="lbl.amount"/></th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${abstractEstimate.overheadValues.size() != 0}">
						<c:forEach items="${abstractEstimate.getOverheadValues()}" var="overheadDtls" varStatus="item">
								<tr >
									<td><span class="spansno"><c:out value="${item.index + 1}" /></span></td>
									<td><c:out value="${overheadDtls.overhead.name}"></c:out></td>
									<c:forEach var="overheadrate" items="${overheadDtls.overhead.overheadRates}">
										<c:set var="estDate" value="<%=new java.util.Date()%>" scope="session"/>
										<c:set var="fromDate" value="${overheadrate.validity.startDate}" scope="session"/>
										<c:set var="toDate" value="${overheadrate.validity.endDate}" scope="session"/>
										<c:choose>
											<c:when  test="${overheadrate.validity != null}">
												<c:choose>
													<c:when  test="${fromDate <= estDate && (estDate <= toDate || toDate == null || toDate == '')}">
														<c:choose>
															<c:when test="${overheadrate.percentage > 0}">
															<td class="text-right"><c:out value="${overheadrate.percentage}"></c:out></td>
															</c:when>
															<c:otherwise>
																<td></td>
															</c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								 	<td class="text-right"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out value="${overheadDtls.amount}" /></fmt:formatNumber></td>
								</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose> 
			</tbody>
			<tfoot>
				<c:set var="overheadtotal" value="${0}" scope="session" />
				<c:if test="${abstractEstimate.getOverheadValues() != null}">
					<c:forEach items="${abstractEstimate.getOverheadValues()}" var="overhead">
						<c:set var="overheadtotal"	value="${overheadtotal + overhead.amount }" />
					</c:forEach>
				</c:if>
				<tr>
					<td colspan="3" class="text-right"><spring:message
							code="lbl.total" /></td>
					<td class="text-right">
						<span><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out value="${overheadtotal}" /></fmt:formatNumber></span>
					</td>
				</tr>
			</tfoot>
		</table>
		
</div>

</div>