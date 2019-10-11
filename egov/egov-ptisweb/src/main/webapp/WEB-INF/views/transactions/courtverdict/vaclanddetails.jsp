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
					<spring:message code="lbl.hdr.vacantland.details" />
				</div>
			</div>
			<div class="panel-body">
				<div align="center" class="overflow-x-scroll">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="table table-bordered" id="vacantLandTable">

						<tbody>
							<tr>
								<th class="bluebgheadtd"><spring:message
										code="lbl.surveyNumber" /><span class="mandatory"></span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.pattaNumber" /><span class="mandatory"></span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.vacantland.area" /><span class="mandatory"></span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.MarketValue" /><span class="mandatory"></span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.currentCapitalValue" /><span class="mandatory"></span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.effectivedate" /><span class="mandatory"></span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.plotArea" /><span class="mandatory"></span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.layout.authority" /><span class="mandatory"></span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.layout.permitNumber" /><span class="mandatory"></span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.layout.permitDate" /><span class="mandatory"></span></th>
							</tr>
							<tr>
								<td class="blueborderfortd">
									<div align="center">
										<c:out value="${property.propertyDetail.surveyNumber}"></c:out>
									</div>
								</td>
								<td class="blueborderfortd"><div align="center">
										<c:out value="${property.propertyDetail.pattaNumber}"></c:out>
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<c:out
											value="${property.propertyDetail.sitalArea.area.toString()}"></c:out>
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<c:out value="${property.propertyDetail.marketValue}"></c:out>
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<c:out value="${property.propertyDetail.currentCapitalValue}"></c:out>
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<fmt:formatDate type="date" pattern="dd-MM-yyyy"
											value="${property.propertyDetail.dateOfCompletion}"></fmt:formatDate>
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<c:out
											value="${property.propertyDetail.vacantLandPlotArea.name}"></c:out>
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<c:out
											value="${property.propertyDetail.layoutApprovalAuthority.name}"></c:out>
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<c:out value="${property.propertyDetail.layoutPermitNo}"></c:out>
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<c:out value="${property.propertyDetail.layoutPermitDate}"></c:out>
									</div></td>
							</tr>

						</tbody>
					</table>
				</div>
			</div>
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.cv.bndryDetails" />
				</div>
			</div>
			<div class="panel-body">
				<div align="center" class="overflow-x-scroll">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="table table-bordered" id="vacantLandBndryTable">

						<tbody>
							<tr>
								<th class="bluebgheadtd">North<span class="mandatory"></span></th>
								<th class="bluebgheadtd">South<span class="mandatory"></span></th>
								<th class="bluebgheadtd">East<span class="mandatory"></span></th>
								<th class="bluebgheadtd">West<span class="mandatory"></span></th>
							</tr>
							<tr>
								<td class="greybox" align="center"><c:out
										value="${property.basicProperty.propertyID.northBoundary}"></c:out>
								</td>
								<td class="greybox" align="center"><c:out
										value="${property.basicProperty.propertyID.southBoundary}"></c:out>
								</td>
								<td class="greybox" align="center"><c:out
										value="${property.basicProperty.propertyID.eastBoundary}"></c:out>
								</td>
								<td class="greybox" align="center"><c:out
										value="${property.basicProperty.propertyID.westBoundary}"></c:out>
								</td>

							</tr>

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>