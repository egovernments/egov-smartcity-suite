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
				<div align="center"
					class="overflow-x-scroll floors-tbl-freeze-column-div">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="table table-bordered" id="floorDetails">
						<tbody>
							<tr>
								<th class="bluebgheadtd"><spring:message
										code="lbl.cv.floorNo" /><span class="mandatory1">*</span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.cv.clsfbuild" /><span class="mandatory1"
									id="constTypeMdtry">*</span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.cv.usage" /><span class="mandatory1"
									id="usageMdtry">*</span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.cv.occupancy" /><span class="mandatory1"
									id="occMdtry">*</span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.cv.constDate" /><span class="mandatory1">*</span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.cv.effectFrom" /><span class="mandatory1">*</span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.cv.plinth" /><span class="mandatory1">*</span></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.cv.buildplanNo" /></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.cv.buildplanDate" /></th>
								<th class="bluebgheadtd"><spring:message
										code="lbl.cv.buildplanPlinth" /></th>
								<th class="bluebgheadtd freeze-action-th"><spring:message
										code="lbl.cv.addDel" /></th>
							</tr>


							<tr>

								<td class="greybox"><form:select cssClass="form-control"
										path="property.propertyDetail.floorDetailsProxy[0].floorNo"
										id="floorNo" cssStyle="width:100%">
										<form:option value="">--select--</form:option>
										<c:forEach items="${flrNoMap}" var="flrNoMap">
											<form:option value="${flrNoMap.key}">${flrNoMap.value}</form:option>
										</c:forEach>
									</form:select></td>
								<td class="greybox"><form:select cssClass="form-control"
										path="property.propertyDetail.floorDetailsProxy[0].structureClassification.id"
										id="structure" cssStyle="width:100%">
										<form:option value="">--select--</form:option>
										<c:forEach items="${structureList}" var="structure">
											<form:option value="${structure.id}">${structure.typeName}</form:option>
										</c:forEach>
									</form:select></td>
								<td class="greybox"><form:select cssClass="form-control"
										path="property.propertyDetail.floorDetailsProxy[0].propertyUsage.id"
										id="usage" cssStyle="width:100%">
										<form:option value="">--select--</form:option>
										<c:forEach items="${usageList}" var="usageList">
											<form:option value="${usageList.id}">${usageList.usageName}</form:option>
										</c:forEach>
									</form:select></td>
								<td class="greybox"><form:select cssClass="form-control"
										path="property.propertyDetail.floorDetailsProxy[0].propertyOccupation.id"
										id="occupation" cssStyle="width:100%">
										<form:option value="">--select--</form:option>
										<c:forEach items="${propOccList}" var="occupation">
											<form:option value="${occupation.id}">${occupation.occupation}</form:option>
										</c:forEach>
									</form:select></td>
								<td class="greybox"><form:input
										cssClass="form-control datepicker" id="constructionDate"
										path="property.propertyDetail.floorDetailsProxy[0].constructionDate"
										cssStyle="width:100%" /></td>
								<td class="greybox"><form:input
										cssClass="form-control datepicker" id="occupancyDate"
										path="property.propertyDetail.floorDetailsProxy[0].occupancyDate"
										cssStyle="width:100%" /></td>
								<td class="greybox"><form:input cssClass="form-control"
										path="property.propertyDetail.floorDetailsProxy[0].builtUpArea.area"
										cssStyle="width:100%" /></td>
								<td class="greybox"><form:input cssClass="form-control"
										path="property.propertyDetail.floorDetailsProxy[0].buildingPermissionNo"
										cssStyle="width:100%" /></td>
								<td class="greybox"><form:input
										cssClass="form-control datepicker"
										path="property.propertyDetail.floorDetailsProxy[0].buildingPermissionDate" /></td>
								<td class="greybox"><form:input cssClass="form-control"
										path="property.propertyDetail.floorDetailsProxy[0].buildingPlanPlinthArea.area"
										cssStyle="width:100%" /></td>
								<td class="greybox freeze-action-td" id="AddRemoveFloor"><span
									id="addF" alt="AddF" class="tblactionicon add"
									onclick="addFloor(); return false;"> <i
										class="fa fa-plus-circle"></i>
								</span> &nbsp; <span id="dDelF" alt="DelF" class="tblactionicon delete"
									onclick="delFloor(this);return false;"> <i
										class="fa fa-minus-circle"></i>
								</span></td>
							</tr>

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
