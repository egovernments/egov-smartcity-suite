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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<form:form name="taxrate-update" id="taxrate-update"
	modelAttribute="taxRatesForm" method="post">
	<div class="row" id="page-content">
		<div class="col-md-12">
			<c:if test="${not empty message}">
				<div class="alert alert-danger" role="alert">
					<spring:message code="${message}" />
				</div>
			</c:if>
			<div class="row">
				<div class="col-md-12">
					<div class="panel-group">
						<div class="panel panel-primary">
							<div class="panel-heading slide-history-menu">
								<div class="panel-title">
									<strong><spring:message code="lbl.hdr.taxRates" /></strong>
								</div>

							</div>
							<div class="panel-body history-slide">
								<div class="form-group col-sm-6 col-sm-offset-3">
									<table class="table table-bordered table-hover">
										<thead>
											<tr>
												<th>S.No</th>
												<th class="text-center" colspan="2"><spring:message
														code="lbl.propertytype.resd" /></th>
												<th class="text-center" colspan="2"><spring:message
														code="lbl.isResidential" /></th>
											</tr>
											<tr>
												<th></th>
												<th class="text-center"><spring:message
														code="lbl.component" /></th>
												<th class="text-center"><spring:message
														code="lbl.percentage" /></th>
												<th class="text-center"><spring:message
														code="lbl.component" /></th>
												<th class="text-center"><spring:message
														code="lbl.percentage" /></th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<c:set var="count" value="1" />
												<c:set var="countResd" value="0" />
												<c:set var="countNresd" value="0" />
												<c:forEach var="taxRate"
													items="${taxRatesForm.demandReasonDetails}"
													varStatus="status">
													<c:if test="${status.index % 2 == 0}">
											</tr>
											<tr>
												<td><c:out value="${count}" /></td>
												<c:set var="count" value="${count + 1}" />
												</c:if>

												<form:input path="demandReasonDetails[${status.index}].id"
													type="hidden" value="${taxRate.id}" name="reasonDetailsId"
													id="reasonDetailsId" />
												<c:choose>
													<c:when
														test="${fn:endsWith(taxRate.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(), 'Non Residential')}">
														<td><c:out
																value="${fn:substringBefore(taxRate.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(), 'Non')}" /></td>
														<c:set var="countNresd"
															value="${taxRate.percentage + countNresd}" />
													</c:when>
													<c:otherwise>
														<td><c:out
																value="${fn:substringBefore(taxRate.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(), 'Residential')}" /></td>
														<c:set var="countResd"
															value="${taxRate.percentage + countResd}" />
													</c:otherwise>
												</c:choose>

												<td class="bluebox"><fmt:formatNumber
														var="formattedRate" type="number" minFractionDigits="2"
														maxFractionDigits="2" value="${taxRate.percentage}" /> <form:input
														path="demandReasonDetails[${status.index}].percentage"
														name="percentage" id="percentage"
														class="patternvalidation" data-pattern="decimalvalue"
														autocomplete="off" maxlength="5" value="${formattedRate}" /></td>
												</c:forEach>
											</tr>
											<tr>
												<td></td>
												<td><spring:message code="lbl.total.resd" /></td>

												<td><fmt:formatNumber var="countResdRate" type="number"
														minFractionDigits="2" maxFractionDigits="2"
														value="${countResd}" /> <input name="genTaxResd" id="sum"
													class="patternvalidation" data-pattern="decimalvalue"
													autocomplete="off" maxlength="5" value="${countResdRate}"
													readonly="true" /></td>
												<td><spring:message code="lbl.total.nresd" /></td>

												<td><fmt:formatNumber var="countNresdRate"
														type="number" minFractionDigits="2" maxFractionDigits="2"
														value="${countNresd}" /> <input name="genTaxNonResd"
													id="sum" class="patternvalidation"
													data-pattern="decimalvalue" autocomplete="off"
													maxlength="5" value="${countNresdRate}" readonly="true" /></td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<input type="submit" id="btnsubmit" value="Update"
						class="btn btn-primary"></input> <a href="javascript:void(0)"
						class="btn btn-default" onclick="self.close()"><spring:message
							code="lbl.close" /></a>
				</div>
			</div>
		</div>
	</div>
</form:form>
<script
	src="<cdn:url value='/resources/js/app/taxrates.js?rnd=${app_release_no}'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/egov/patternvalidation.js?rnd=${app_release_no}' context='/egi'/>"></script>