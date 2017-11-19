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
<%@ include file="/includes/taglibs.jsp"%>
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">FinancialYear</div>
				</div>
				<div class="panel-body custom">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.finyearrange" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${CFinancialYear.finYearRange}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.startingdate" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<fmt:formatDate pattern="dd/MM/yyyy"
								value="${CFinancialYear.startingDate}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.endingdate" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<fmt:formatDate pattern="dd/MM/yyyy"
								value="${CFinancialYear.endingDate}" />
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.isactive" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${CFinancialYear.isActive}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.isactiveforposting" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${CFinancialYear.isActiveForPosting}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.isclosed" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${CFinancialYear.isClosed}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.transferclosingbalance" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${CFinancialYear.transferClosingBalance}</div>
					</div>
					<input type="hidden" name="CFinancialYear"
						value="${CFinancialYear.id}" />
					<div id="labelAD" align="center">
						<table id="fiscalPeriodTable" width="60%" border=0 id="labelid"
							class="table table-bordered">
							<thead>
								<th>Fiscal Period Name</th>
								<th>Starting Date</th>
								<th>Ending Date</th>
								<th></th>
							</thead>
							<c:choose>
								<c:when test="${!CFinancialYear.cFiscalPeriod.isEmpty()}">
									<c:forEach items="${CFinancialYear.cFiscalPeriod}" var="var1"
										varStatus="counter">
										<tr id="fiscalPeroid">
											<td><input type="text"
														name="cFiscalPeriod[${counter.index}].name"
														value="${var1.name}"
														id="cFiscalPeriod[${counter.index}].name" size="10"
														readonly="readonly"
														class="form-control text-right patternvalidation" />
												</td>
											<td><fmt:formatDate value="${var1.startingDate}"
													var="startDate" pattern="dd/MM/yyyy" /> 
													<input type="text"
														name="cFiscalPeriod[${counter.index}].startingDate"
														 value="${startDate}"
														id="cFiscalPeriod[${counter.index}].startingDate"
														readonly="readonly" />
												</td>
											<td><fmt:formatDate value="${var1.endingDate}"
													var="endDate" pattern="dd/MM/yyyy" />
												  <input type="text"
														name="cFiscalPeriod[${counter.index}].endingDate"
														value="${endDate}"
														id="cFiscalPeriod[${counter.index}].endingDate"
														readonly="readonly" />
												</td>
											<td></td>
										</tr>
									</c:forEach>
								</c:when>
								</c:choose></table>
				</div>
				<div class="row text-center">
			<div class="add-margin">
				<a href="javascript:void(0)" class="btn btn-default"
					onclick="self.close()">Close</a>
			</div>
		</div>
			</div>
		</div>
		