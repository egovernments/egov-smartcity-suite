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

<div class="panel-heading" style="text-align: left">
	<div class="panel-title">
		<s:text name="amenities" />
	</div>
</div>

<div class="panel-body">
	<div class="form-group">
		<%@ include file="../common/amenitiesForm.jsp"%>
	</div>
</div>

<div class="panel-heading" style="text-align: left">
	<div class="panel-title">
		<s:text name="title.constructiontypes" />
	</div>
</div>

<div class="panel-body">
	<div class="form-group">
		<label class="col-sm-3 control-label text-right"> <s:text
				name="floortype" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:select headerKey="-1" title="Floor type of the property"
				headerValue="%{getText('default.select')}" name="floorTypeId"
				id="floorTypeId" listKey="id" listValue="name"
				list="dropdownData.floorType" value="%{floorTypeId}"
				cssClass="form-control" />
		</div>
		<label class="col-sm-2 control-label text-right"> <s:text
				name="rooftype" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:select headerKey="-1" title="Roof type of the property"
				headerValue="%{getText('default.select')}" name="roofTypeId"
				id="roofTypeId" listKey="id" listValue="name"
				list="dropdownData.roofType" value="%{roofTypeId}"
				cssClass="form-control" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label text-right"> <s:text
				name="walltype" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:select headerKey="-1" title="Wall type"
				headerValue="%{getText('default.select')}" name="wallTypeId"
				id="wallTypeId" listKey="id" listValue="name"
				list="dropdownData.wallType" value="%{wallTypeId}"
				cssClass="form-control" />
		</div>
		<label class="col-sm-2 control-label text-right"> <s:text
				name="woodtype" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:select headerKey="-1" title="Wood type"
				headerValue="%{getText('default.select')}" name="woodTypeId"
				id="woodTypeId" listKey="id" listValue="name"
				list="dropdownData.woodType" value="%{woodTypeId}"
				cssClass="form-control" />
		</div>
	</div>

</div>

<div class="panel-heading" style="text-align: left">
	<div class="panel-title">
		<s:text name="FloorDetailsHeader" />
	</div>
</div>

<div class="form group">
	<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="tablebottom" id="floorDetailsTbl">
	<thead>
	<tr>
		<th class="bluebgheadtd"><s:text name="FloorNo" /><span	class="mandatory">*</span></th>
		<th class="bluebgheadtd"><s:text name="ConstructionType" /><span class="mandatory">*</span></th>
		<th class="bluebgheadtd"><s:text name="Usage" /><span class="mandatory">*</span></th>
		<th class="bluebgheadtd"><s:text name="firmName" /><span	class="mandatory">*</span></th>
		<th class="bluebgheadtd"><s:text name="Occupancy" /><span class="mandatory">*</span></th>
		<th class="bluebgheadtd"><s:text name="Occupantname" /></th>
		<th class="bluebgheadtd"><s:text name="constrdate" /><span	class="mandatory">*</span> </th>
		<th class="bluebgheadtd"><s:text name="effectiveDate" /><span	class="mandatory">*</span></th>
		
		<th class="bluebgheadtd"><s:text name="unstructuredLand" /><span class="mandatory">*</span></th>
		<th class="bluebgheadtd"><s:text name="plinthLength" /><span class="mandatory">*</span></th>
		<th class="bluebgheadtd"><s:text name="plinthBreadth" /><span class="mandatory">*</span></th>
		<th class="bluebgheadtd"><s:text name="PlinthArea" /><span	class="mandatory">*</span></th>
		<th class="bluebgheadtd"><s:text name="building.permNo" />
		<th class="bluebgheadtd"><s:text name="buildingpermdate" />
		<th class="bluebgheadtd"><s:text name="buildingpermplintharea" />
		
		<th class="bluebgheadtd freeze-action-th" height="150" id="addDelFloors"><s:text name="Add/Delete" /></th>
	</tr>
	</thead>
        <s:if test="!propertyDetail.floorDetailsProxy.isEmpty()">
				<s:iterator value="(propertyDetail.floorDetailsProxy.size).{#this}"
					status="floorsstatus">
					<tr id="floorDetailsRow">
						<s:hidden
							name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].floorUid"
							id="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].floorUid"
							value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].floorUid}"></s:hidden>
							
							
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select headerKey=""
						headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].floorNo" listKey="key" id="floorNo"
						value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].floorNo}"
						listValue="value" list="floorNoMap" cssClass="selectnew" cssStyle="width:100%"
						data-optional="0" data-errormsg="Floor Number is mandatory!" title="Floor number of the property"/>
				</div>
			</td>
			<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:if test="%{#floorsstatus.index == 0}">
							<s:select headerKey=""
								headerValue="%{getText('default.select')}"
								name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].structureClassification.id"
								listKey="id" id="floorConstType"
								value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].structureClassification.id}"
								listValue="typeName" list="dropdownData.StructureList" 
								data-errormsg="Classification of building is required!"
								data-optional="0" cssClass="selectnew" cssStyle="width:100%" title="Classification of the Building"/>
						</s:if>
						<s:else>
							<s:select headerKey=""
								headerValue="%{getText('default.select')}"
								name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].structureClassification.id"
								listKey="id" id="floorConstType%{#floorsstatus.index-1}"
								value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].structureClassification.id}"
								listValue="typeName" list="dropdownData.StructureList"
								data-errormsg="Classification of building is required!"
								data-optional="0" cssClass="selectnew" cssStyle="width:100%" title="Classification of the Building"/>
						</s:else>
					</div>
				</td>
				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
							<s:select headerKey=""
								headerValue="%{getText('default.select')}"
								name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].propertyUsage.id"
								listKey="id" id="floorUsage" data-idx="%{#floorsstatus.index}"
								listValue="usageName" list="dropdownData.UsageList"
								cssClass="selectnew floorusage" data-optional="0"
								data-errormsg="Nature of usage is required!"
								value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].propertyUsage.id}"
								cssStyle="width:100%"  title="Nature of usage of the property"/>
					</div>
				</td>
				
				<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].firmName"
						id="firmName" size="25" maxlength="32"
						value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].firmName}"
						cssStyle="width:100%" title="Name of the Firm"/>
				</div>
				</td>

				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:select headerKey=""
							headerValue="%{getText('default.select')}"
							name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].propertyOccupation.id"
							listKey="id" id="floorOccupation" listValue="occupation"
							list="dropdownData.OccupancyList" cssClass="selectnew"
							data-optional="0" data-errormsg="Occupancy is required!"
							value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].propertyOccupation.id}"
							cssStyle="width:100%"  title="Property occupied by"/>
					</div>
				</td>
               <td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:textfield name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].occupantName"
							id="occupantname" size="25" maxlength="32" value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].occupantName}"
							cssStyle="width:100%"  title="Name of the occupied by person"/>
					</div>
				</td>
 
 				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:date name="propertyDetail.floorDetailsProxy[#floorsstatus.index].constructionDate" var="constrDate" format="dd/MM/yyyy"/>
						<s:textfield autocomplete="off"
							name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].constructionDate" 
							value="%{constrDate}" data-optional="0"
							data-errormsg="Construction date is required!"
							id="propertyDetail.floorDetailsProxy[%#floorsstatus.index].constructionDate" size="10"
							maxlength="10" cssStyle="width:100%" cssClass="datepicker"  title="Construction Date"></s:textfield>
					</div>
				</td>
 				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:date name="propertyDetail.floorDetailsProxy[#floorsstatus.index].occupancyDate" var="occDate" format="dd/MM/yyyy"/>
						<s:textfield autocomplete="off"
							name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].occupancyDate" 
							value="%{occDate}" data-optional="0"
								data-errormsg="Occupancy date is required!"
							id="propertyDetail.floorDetailsProxy[%#floorsstatus.index].occupancyDate" size="10"
							maxlength="10" cssStyle="width:100%" cssClass="datepicker occupancydate"  title="Tax effective from entered installment"></s:textfield>
					</div>
				</td>
				
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].unstructuredLand" 
					id="unstructuredLand"  list="#{'false':'No','true':'Yes' }"
					value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].unstructuredLand}"
						data-idx="%{#floorsstatus.index}" cssClass="selectnew unstructuredland" data-optional="0" title="Unstructured Land">
					</s:select>
				</div>
			</td>
		<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].builtUpArea.length" data-idx="%{#floorsstatus.index}"
						maxlength="10" size="10" id="builtUpArealength" value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].builtUpArea.length}"
						data-pattern="decimalvalue" cssClass="form-control patternvalidation builtuplength"
						cssStyle="width:100%" data-optional="1" data-errormsg="Length is mandatory!" title="Length" />
				</div>
			</td>
		<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].builtUpArea.breadth" data-idx="%{#floorsstatus.index}" 
						maxlength="10" size="10" id="builtUpAreabreadth" value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].builtUpArea.breadth}"
						data-pattern="decimalvalue" cssClass="form-control patternvalidation builtupbreadth"
						cssStyle="width:100%" data-optional="1" data-errormsg="Breadth is mandatory!" title="Breadth"/>
				</div>
			</td> 
				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:textfield
							name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].builtUpArea.area"
							maxlength="10" size="10" id="builtUpArea" data-optional="0" data-errormsg="Plinth area is mandatory!"
							value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].builtUpArea.area}"
							data-pattern="decimalvalue" cssClass="form-control patternvalidation builtuparea"
							cssStyle="width:100%"  title="Length X Width"/>
					</div>
				</td>
				
				<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].buildingPermissionNo" 
						maxlength="16" size="16" id="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].buildingPermissionNo" 
						value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].buildingPermissionNo}"
						onblur="checkZero(this);" onchange="trim(this,this.value);"
						cssClass="form-control buildingpermissionno"
						cssStyle="width:100%" title="Building Permission Number"/>
				</div>
			</td>
			
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
				<s:date name="propertyDetail.floorDetailsProxy[#floorsstatus.index].buildingPermissionDate" var="blngPlinthArea" format="dd/MM/yyyy"/>
					<s:textfield name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].buildingPermissionDate" 
						maxlength="16" size="16" id="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].buildingPermissionDate" 
						value="%{blngPlinthArea}"
						cssClass="datepicker buildingpermissiondate"
						cssStyle="width:100%" title="Building Permission Date"/>
				</div>
			</td>
			
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].buildingPlanPlinthArea.area" 
						maxlength="10" size="10" id="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].buildingPlanPlinthArea.area"
						value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].buildingPlanPlinthArea.area}"
						cssClass="form-control buildingplanplintharea patternvalidation"
						cssStyle="width:100%" title="Plinth area in building plan"/>
				</div>
			</td>
			
		<td class="blueborderfortd freeze-action-td"><a href="javascript:void(0);"
							class="btn-sm btn-default" onclick="addFloors();"><span
								style="cursor: pointer;"><i class="fa fa-plus"></i></span></a> 
							<span id="deleteFloor" name="deleteFloorBtn"
							class="btn-sm btn-default" alt="removeFloorBtn"> <i
								class="fa fa-trash"></i>							
						</td>
			</tr>
		</s:iterator>
		</s:if>
</table>
</div>