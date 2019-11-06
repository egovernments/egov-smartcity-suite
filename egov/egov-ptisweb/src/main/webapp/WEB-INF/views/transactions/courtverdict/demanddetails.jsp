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
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0"
			style="text-align: left">
			<div class="panel-heading">
				<div class="panel-title">

					<spring:message code="lbl.cv.dmndDet" />
				</div>
			</div>
			<div class="panel-body">
				<div align="center"
					class="overflow-x-scroll floors-tbl-freeze-column-div">
					<table class="table table-bordered" width="100%" id="demandDetails">
						<tr>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.instlmnt" /></th>
							<th class="bluebgheadtd">Tax Name</th>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.dmndAmt" /></th>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.uncolldmndAmt" /></th>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.collection" /></th>
						</tr>
						<c:forEach items="${demandDetailList}" var="demandDetails"
							varStatus="status">
							<c:if test="${demandDetails.reasonMaster ne 'Advance'}">
								<tr>
									<td class="greybox"><form:hidden
											path="demandDetailBeanList[${status.index }].installment.id" />
										<c:if
											test="${demandDetailList[status.index].installment.id == demandDetailList[status.index-1].installment.id}">
									&nbsp;
									</c:if> <c:if
											test="${demandDetailList[status.index].installment.id != demandDetailList[status.index-1].installment.id}">

											<c:out value="${demandDetails.installment}"></c:out>

										</c:if></td>
									<td class="greybox"><form:hidden
											path="demandDetailBeanList[${status.index }].reasonMaster" />
										<c:out value="${demandDetails.reasonMaster}"></c:out></td>
									<td class="greybox"><c:out
											value="${demandDetails.actualAmount}">
										</c:out></td>
									<td class="greybox"><c:out
											value="${demandDetails.revisedAmount}">
										</c:out></td>
									<td class="greybox"><c:out
											value="${demandDetails.actualCollection}">
										</c:out></td>
								</tr>
							</c:if>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>