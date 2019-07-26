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
					<spring:message code="lbl.cv.flrDtls" />
				</div>
			</div>
			<div class="panel-body">
				<div align="center">
					<table class="table table-bordered" id="floorDetails">
						<tr>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.floorNo" /><span class="mandatory1">*</span></th>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.clsfbuild" /><span class="mandatory1"
								id="constTypeMdtry">*</span></th>
							<th class="bluebgheadtd"><spring:message code="lbl.cv.usage" /><span
								class="mandatory1" id="usageMdtry">*</span></th>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.occupancy" /><span class="mandatory1"
								id="occMdtry">*</span></th>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.constDate" /><span class="mandatory1">*</span></th>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.effectFrom" /><span class="mandatory1">*</span></th>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.unstructuredLand" /><span class="mandatory1">*</span></th>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.length" /><span class="mandatory1">*</span></th>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.breadth" /><span class="mandatory1">*</span></th>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.plinth" /><span class="mandatory1">*</span></th>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.buildplanNo" /></th>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.buildplanDate" /></th>
							<th class="bluebgheadtd"><spring:message
									code="lbl.cv.buildplanPlinth" /></th>
						</tr>
						<c:forEach items="${floor}" var="floor" varStatus="status">
							<tr>
								<td class="blueborderfortd" style="padding: 2px 2px">
									<div align="center">
										<span class="bold"><c:out default="N/A"
												value="${floorMap[status.index ]}"></c:out></span>
									</div>
								</td>
								<td class="blueborderfortd" style="padding: 2px 2px">
									<div align="center">
										<span class="bold"><c:out default="N/A"
												value="${floor.structureClassification.typeName}"></c:out></span>
									</div>
								</td>
								<td class="blueborderfortd" style="padding: 2px 2px">
									<div align="center">
										<span class="bold"><c:out default="N/A"
												value="${floor.propertyUsage.usageName}"></c:out></span>
									</div>
								</td>
								<td class="blueborderfortd" style="padding: 2px 2px">
									<div align="center">
										<span class="bold"><c:out default="N/A"
												value="${floor.propertyOccupation.occupation}"></c:out></span>
									</div>
								</td>
								<td class="blueborderfortd" style="padding: 2px 2px">
									<div align="center">
										<span class="bold"><fmt:formatDate type="date"
												pattern="dd-MM-yyyy" value="${floor.constructionDate}"></fmt:formatDate></span>
									</div>
								</td>
								<td class="blueborderfortd" style="padding: 2px 2px">
									<div align="center">
										<span class="bold"><fmt:formatDate type="date"
												pattern="dd-MM-yyyy" value="${floor.occupancyDate}"></fmt:formatDate></span>
									</div>
								</td>
								<td class="blueborderfortd" style="padding: 2px 2px">
									<div align="center">
										<span class="bold"><c:out default="N/A"
												value="${floor.unstructuredLand}"></c:out></span>
									</div>
								</td>
								<td class="blueborderfortd" style="padding: 2px 2px">
									<div align="center">
										<span class="bold"><c:out default="N/A"
												value="${floor.builtUpArea.length}"></c:out></span>
									</div>
								</td>
								<td class="blueborderfortd" style="padding: 2px 2px">
									<div align="center">
										<span class="bold"><c:out default="N/A"
												value="${floor.builtUpArea.breadth}"></c:out></span>
									</div>
								</td>
								<td class="blueborderfortd" style="padding: 2px 2px">
									<div align="center">
										<span class="bold"><c:out default="N/A"
												value="${floor.builtUpArea.area}"></c:out></span>
									</div>
								</td>
								<td class="blueborderfortd" style="padding: 2px 2px">
									<div align="center">
										<span class="bold"><c:out default="N/A"
												value="${floor.buildingPermissionNo}"></c:out></span>
									</div>
								</td>
								<td class="blueborderfortd" style="padding: 2px 2px">
									<div align="center">
										<span class="bold"><c:out default="N/A"
												value="${floor.buildingPermissionDate}"></c:out></span>
									</div>
								</td>
								<td class="blueborderfortd" style="padding: 2px 2px">
									<div align="center">
										<span class="bold"><c:out default="N/A"
												value="${floor.buildingPlanPlinthArea.area}"></c:out></span>
									</div>
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>