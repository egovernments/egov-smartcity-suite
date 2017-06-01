<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) 2017  eGovernments Foundation
  ~
  ~  The updated version of eGov suite of products as by eGovernments Foundation
  ~  is available at http://www.egovernments.org
  ~
  ~  This program is free software: you can redistribute it and/or modify
  ~  it under the terms of the GNU General Public License as published by
  ~  the Free Software Foundation, either version 3 of the License, or
  ~  any later version.
  ~
  ~  This program is distributed in the hope that it will be useful,
  ~  but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~  GNU General Public License for more details.
  ~
  ~  You should have received a copy of the GNU General Public License
  ~  along with this program. If not, see http://www.gnu.org/licenses/ or
  ~  http://www.gnu.org/licenses/gpl.html .
  ~
  ~  In addition to the terms of the GPL license to be adhered to in using this
  ~  program, the following additional terms are to be complied with:
  ~
  ~      1) All versions of this program, verbatim or modified must carry this
  ~         Legal Notice.
  ~
  ~      2) Any misrepresentation of the origin of the material is prohibited. It
  ~         is required that all modified versions of this material be marked in
  ~         reasonable ways as different from the original version.
  ~
  ~      3) This license does not grant any rights to any user of the program
  ~         with regards to rights under trademark law for use of the trade names
  ~         or trademarks of eGovernments Foundation.
  ~
  ~  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<form:form method="post" action=""
	class="form-horizontal form-groups-bordered"
	modelAttribute="meteredRates" id="meteredRatesForm">
	<div class="row">
		<div class="col-md-12">
			<c:if test="${not empty message}">
				<div class="alert alert-success" role="alert">${message}</div>
			</c:if>
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="title.meteredrates" />
					</div>
				</div>
				<div class="panel-body">
					<div class="row add-border">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.slabname" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<c:out value="${meteredRates.slabName}" default="N/A" />
						</div>
						<br/><br/>
						<table
							class="table table-bordered datatable dt-responsive table-hover multiheadertbl tblmeteredrate"
							id="new-row-table">
							<thead>
								<tr>
									<th><spring:message code="lbl.rateamount" /></th>
									<th><spring:message code="lbl.flatamount" /></th>
									<th><spring:message code="lbl.isrecursive" /></th>
									<th><spring:message code="lbl.recursivefactor" /></th>
									<th><spring:message code="lbl.recursiveamount" /></th>
									<th><spring:message code="lbl.effective.fromdate" /></th>
									<th><spring:message code="lbl.effective.todate" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${meteredRates.ratesDetail}"
									var="ratesDetailValue" varStatus="counter">
									<tr id="meteredRateRow">
										<td class="text-center"><c:out
												value="${ratesDetailValue.rateAmount}" /></td>
										<td class="text-center"><c:out
												value="${ratesDetailValue.flatAmount}" /></td>
										<td class="text-center"><c:choose>
												<c:when test="${ratesDetailValue.recursive=='true'}">
													<c:out value="TRUE" />
												</c:when>
												<c:otherwise>
													<c:out value="FALSE" />
												</c:otherwise>
											</c:choose></td>
										<td class="text-center"><c:out
												value="${ratesDetailValue.recursiveFactor}" /></td>
										<td class="text-center"><c:out
												value="${ratesDetailValue.recursiveAmount}" /></td>
										<td class="text-center"><fmt:formatDate
												pattern="dd/MM/yyyy" value="${ratesDetailValue.fromDate}" />
										</td>
										<td class="text-center"><fmt:formatDate
												pattern="dd/MM/yyyy" value="${ratesDetailValue.toDate}" /></td>
									</tr>
								</c:forEach>
							</tbody>
							<t:footer>
							</t:footer>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<br />
	<div class="row text-center">
		<div class="row">
			<c:if test="${mode == 'create'}">
				<button type="button" class="btn btn-primary" id="addnewid">
					<spring:message code="lbl.addnew" />
				</button>
			</c:if>
			<a href="javascript:void(0)" class="btn btn-default"
				onclick="self.close()"><spring:message code="lbl.close" /></a>
		</div>
	</div>
</form:form>
<script
	src="<cdn:url value='/resources/js/app/metered-rates-master.js?rnd=${app_release_no}'/>"></script>
