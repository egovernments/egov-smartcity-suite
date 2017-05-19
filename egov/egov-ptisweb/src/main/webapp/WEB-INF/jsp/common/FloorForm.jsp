
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

<%@ include file="/includes/taglibs.jsp"%>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="tablebottom" id="floorDetails">
	<tr>
		<th class="bluebgheadtd"><s:text name="FloorNo" /><span	class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="ConstructionType" /><span class="mandatory1" id="constTypeMdtry">*</span></th>
		<th class="bluebgheadtd"><s:text name="Usage" /><span class="mandatory1" id="usageMdtry">*</span></th>
		<th class="bluebgheadtd"><s:text name="firmName" /><span	class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="Occupancy" /><span class="mandatory1" id="occMdtry">*</span></th>
		<th class="bluebgheadtd"><s:text name="Occupantname" /></th>
		<th class="bluebgheadtd"><s:text name="constrdate" /><span	class="mandatory1">*</span> </th>
		<th class="bluebgheadtd"><s:text name="effectiveDate" /><span	class="mandatory1">*</span></th>
		
		<th class="bluebgheadtd"><s:text name="unstructuredLand" /><span class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="plinthLength" /><span class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="plinthBreadth" /><span class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="PlinthArea" /><span	class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="building.permNo" />
		<th class="bluebgheadtd"><s:text name="buildingpermdate" />
		<th class="bluebgheadtd"><s:text name="buildingpermplintharea" />
		
		<th class="bluebgheadtd freeze-action-th"><s:text name="Add/Delete" /></th>
	</tr>
	<s:if test="propertyDetail.floorDetailsProxy.size()==0">
		<tr id="Floorinfo">
			<s:hidden name="propertyDetail.floorDetailsProxy[0].floorUid" id="propertyDetail.floorDetailsProxy[0].floorUid"
                       value="%{propertyDetail.floorDetailsProxy[0].floorUid}"></s:hidden>
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select headerKey=""
						headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetailsProxy[0].floorNo" listKey="key"	id="floorNo"
						value="%{propertyDetail.floorDetailsProxy[0].floorNo}"
						listValue="value" list="floorNoMap" cssClass="selectnew" cssStyle="width:100%"
						data-optional="0" data-errormsg="Floor Number is mandatory!" title="Floor number of the property"/>
				</div>
			</td>
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select headerKey="" headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetailsProxy[0].structureClassification.id"
						listKey="id" id="floorConstType"
						value="%{propertyDetail.floorDetailsProxy[0].structureClassification.id}"
						listValue="typeName" list="dropdownData.StructureList"
						cssClass="selectnew" cssStyle="width:100%" 
						data-optional="0" data-errormsg="Classification of building is required!" title="Classification of the Building"/>
				</div>
			</td>
			<egov:ajaxdropdown id="floorUsage" fields="['Text','Value']" dropdownId="floorUsage"
			url="/common/ajaxCommon-usageByPropType.action" afterSuccess="loadUsages"/>
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select headerKey="" headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetailsProxy[0].propertyUsage.id"
						listKey="id" id="floorUsage"
						value="%{propertyDetail.floorDetailsProxy[0].propertyUsage.id}"
						listValue="usageName" list="dropdownData.UsageList" onchange="enableDisableFirmName(this);"
						cssClass="selectnew"
						cssStyle="width:100%"  
						data-optional="0" data-errormsg="Nature of usage is required!" title="Nature of usage of the property"/>
				</div>
			</td>
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[0].firmName"
						id="firmName" size="20"
						value="%{propertyDetail.floorDetailsProxy[0].firmName}"
						maxlength="32" cssStyle="width:100%" title="Name of the Firm"/>
				</div>
			</td>
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select headerKey="" headerValue="%{getText('default.select')}" 
						name="propertyDetail.floorDetailsProxy[0].propertyOccupation.id"
						listKey="id" id="floorOccupation"
						value="%{propertyDetail.floorDetailsProxy[0].propertyOccupation.id}"
						listValue="occupation" list="dropdownData.OccupancyList"
						cssClass="selectnew"
						cssStyle="width:100%" 
						data-optional="0" data-errormsg="Occupancy is required!" title="Property occupied by"/>
				</div>
			</td>
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[0].occupantName"
						id="occupantName" size="20"
						value="%{propertyDetail.floorDetailsProxy[0].occupantName}"
						maxlength="32" cssStyle="width:100%" title="Name of the occupied by person"/>
				</div>
			</td>
			
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:date name="propertyDetail.floorDetailsProxy[0].constructionDate" var="constrDate" format="dd/MM/yyyy"/>
					<s:textfield autocomplete="off"
						name="propertyDetail.floorDetailsProxy[0].constructionDate" data-optional="0" 
						id="propertyDetail.floorDetailsProxy[0].constructionDate" value="%{constrDate}" size="10"
						maxlength="10" cssStyle="width:100%" cssClass="datepicker" title="Construction Date"></s:textfield>
				</div>
			</td>

			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:date name="propertyDetail.floorDetailsProxy[0].occupancyDate" var="occDate" format="dd/MM/yyyy"/>
					<s:textfield autocomplete="off"
						name="propertyDetail.floorDetailsProxy[0].occupancyDate" data-optional="0" data-errormsg="Ocuupancy date is required!"
						id="propertyDetail.floorDetailsProxy[0].occupancyDate" value="%{occDate}" size="10"
						maxlength="10" cssStyle="width:100%" cssClass="datepicker" title="Tax effective from entered installment"></s:textfield>
				</div>
			</td>

			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select name="propertyDetail.floorDetailsProxy[0].unstructuredLand" id="unstructuredLand"  list="#{'false':'No','true':'Yes' }" value="%{propertyDetail.floorDetailsProxy[0].unstructuredLand}"
						onchange="enableDisableLengthBreadth(this);" cssClass="selectnew" data-optional="0" title="Unstructured Land">
					</s:select>
				</div>
			</td>
		<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[0].builtUpArea.length" 
						maxlength="10" size="10" id="builtUpArealength" value="%{propertyDetail.floorDetailsProxy[0].builtUpArea.length}"
						onblur="trim(this,this.value);checkForTwoDecimals(this,'Length');checkZero(this,'Length'); calculatePlintArea(this);"
						  cssStyle="width:100%" data-optional="1" data-errormsg="Length is mandatory!" title="Length" />
				</div>
			</td>
		<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[0].builtUpArea.breadth" 
						maxlength="10" size="10" id="builtUpAreabreadth" value="%{propertyDetail.floorDetailsProxy[0].builtUpArea.breadth}"
						onblur="trim(this,this.value);checkForTwoDecimals(this,'Breadth');checkZero(this,'Breadth'); calculatePlintArea(this);"
						 cssStyle="width:100%" data-optional="1" data-errormsg="Breadth is mandatory!" title="Breadth"/>
				</div>
			</td>
		
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[0].builtUpArea.area" 
						maxlength="10" size="10" id="builtUpArea" value="%{propertyDetail.floorDetailsProxy[0].builtUpArea.area}"
						onblur="trim(this,this.value);checkForTwoDecimals(this,'Assessable Area');checkZero(this,'Assessable Area');"
						cssStyle="width:100%" data-optional="0" data-errormsg="Plinth area is mandatory!" title="Length X Width"/>
				</div>
			</td>
			
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">

					<s:textfield name="propertyDetail.floorDetailsProxy[0].buildingPermissionNo" 
						maxlength="16" size="16" id="propertyDetail.floorDetailsProxy[0].buildingPermissionNo" value="%{propertyDetail.floorDetailsProxy[0].buildingPermissionNo}"
						onblur="checkZero(this);" onchange="trim(this,this.value);"
						cssStyle="width:100%" title="Building Permission Number"/>
				</div>
			</td>
			
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">

					<s:textfield name="propertyDetail.floorDetailsProxy[0].buildingPermissionDate" 
						maxlength="16" size="16" id="propertyDetail.floorDetailsProxy[0].buildingPermissionDate" value="%{propertyDetail.floorDetailsProxy[0].buildingPermissionDate}"
						cssClass="datepicker" autocomplete="off"
						cssStyle="width:100%" title="Building Permission Date"/>
				</div>
			</td>
			
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[0].buildingPlanPlinthArea.area" 
						maxlength="10" size="10" id="propertyDetail.floorDetailsProxy[0].buildingPlanPlinthArea.area" value="%{propertyDetail.floorDetailsProxy[0].buildingPlanPlinthArea.area}"
						onblur="trim(this,this.value);checkForTwoDecimals(this,'Building paln plinth Area');checkZero(this,'Building paln plinth Area');"
						cssStyle="width:100%" title="Plinth area in building plan"/>
				</div>
			</td>
			
			<td class="blueborderfortd freeze-action-td" id="AddRemoveFloor">
			
			  <span id="addF" alt="AddF" class="tblactionicon add" 
			        onclick="javascript:addFloor(); return false;">
			           <i class="fa fa-plus-circle"></i>
			  </span>
			  &nbsp;
			  <span id="dDelF" alt="DelF" class="tblactionicon delete" 
			        onclick="javascript:delFloor(this);return false;">
			           <i class="fa fa-minus-circle"></i>
			  </span>
			
			</td>

		</tr>
	</s:if>
	<s:else>
		<s:iterator value="(propertyDetail.floorDetailsProxy.size).{#this}"
			status="floorsstatus">
			<tr id="Floorinfo">
				<s:hidden name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].floorUid" id="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].floorUid"
                       value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].floorUid}"></s:hidden>
				<td class="blueborderfortd" style="padding: 2px 2px"><s:select
						headerKey="" headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].floorNo"
						listKey="key" id="floorNo" listValue="value" list="floorNoMap"
						cssClass="selectnew"
						value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].floorNo}"
						cssStyle="width:100%" title="Floor number of the property"/></td>

				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:if test="%{#floorsstatus.index == 0}">
							<s:select headerKey=""
								headerValue="%{getText('default.select')}"
								name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].structureClassification.id"
								listKey="id" id="floorConstType"
								value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].structureClassification.id}"
								listValue="typeName" list="dropdownData.StructureList"
								cssClass="selectnew" cssStyle="width:100%" title="Classification of the Building"/>
						</s:if>
						<s:else>
							<s:select headerKey=""
								headerValue="%{getText('default.select')}"
								name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].structureClassification.id"
								listKey="id" id="floorConstType%{#floorsstatus.index-1}"
								value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].structureClassification.id}"
								listValue="typeName" list="dropdownData.StructureList"
								cssClass="selectnew" cssStyle="width:100%" title="Classification of the Building"/>
						</s:else>
					</div>
				</td>
				<egov:ajaxdropdown id="floorUsage" fields="['Text','Value']" dropdownId="floorUsage"
			          url="/common/ajaxCommon-usageByPropType.action" afterSuccess="loadUsages"/>
				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
							<s:select headerKey=""
								headerValue="%{getText('default.select')}"
								name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].propertyUsage.id"
								listKey="id" id="floorUsage"
								listValue="usageName" list="dropdownData.UsageList" onchange="enableDisableFirmName(this);"
								cssClass="selectnew"
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
							value="%{constrDate}"
							id="propertyDetail.floorDetailsProxy[%#floorsstatus.index].constructionDate" size="10"
							maxlength="10" cssStyle="width:100%" cssClass="datepicker"  title="Construction Date"></s:textfield>
					</div>
				</td>
			
 				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:date name="propertyDetail.floorDetailsProxy[#floorsstatus.index].occupancyDate" var="occDate" format="dd/MM/yyyy"/>
						<s:textfield autocomplete="off"
							name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].occupancyDate" 
							value="%{occDate}"
							id="propertyDetail.floorDetailsProxy[%#floorsstatus.index].occupancyDate" size="10"
							maxlength="10" cssStyle="width:100%" cssClass="datepicker"  title="Tax effective from entered installment"></s:textfield>
					</div>
				</td>
				
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].unstructuredLand" id="unstructuredLand"  list="#{'false':'No','true':'Yes' }" value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].unstructuredLand}"
						onchange="enableDisableLengthBreadth(this);" cssClass="selectnew" data-optional="0" title="Unstructured Land">
					</s:select>
				</div>
			</td>
			
		<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].builtUpArea.length" 
						maxlength="10" size="10" id="builtUpArealength" value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].builtUpArea.length}"
						onblur="trim(this,this.value);checkForTwoDecimals(this,'Length');checkZero(this,'Length'); calculatePlintArea(this);"
						cssStyle="width:100%" data-optional="1" data-errormsg="Length is mandatory!" title="Length" />
				</div>
			</td>
		<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].builtUpArea.breadth" 
						maxlength="10" size="10" id="builtUpAreabreadth" value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].builtUpArea.breadth}"
						onblur="trim(this,this.value);checkForTwoDecimals(this,'Breadth');checkZero(this,'Breadth'); calculatePlintArea(this);"
						cssStyle="width:100%" data-optional="1" data-errormsg="Breadth is mandatory!" title="Breadth"/>
				</div>
			</td> 
		
				
				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:textfield
							name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].builtUpArea.area"
							maxlength="10" size="10" id="builtUpArea"
							value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].builtUpArea.area}"
							onblur="trim(this,this.value);checkForTwoDecimals(this,'Assessable Area');checkZero(this,'Assessable Area');"
							cssStyle="width:100%"  title="Length X Width"/>
					</div>
				</td>
				
				<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].buildingPermissionNo" 
						maxlength="16" size="16" id="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].buildingPermissionNo" value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].buildingPermissionNo}"
						onblur="checkZero(this);" onchange="trim(this,this.value);"
						cssStyle="width:100%" title="Building Permission Number"/>
				</div>
			</td>
			
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
				<s:date name="propertyDetail.floorDetailsProxy[#floorsstatus.index].buildingPermissionDate" var="blngPlinthArea" format="dd/MM/yyyy"/>
					<s:textfield name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].buildingPermissionDate" 
						maxlength="16" size="16" id="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].buildingPermissionDate" 
						value="%{blngPlinthArea}"
						cssClass="datepicker" autocomplete="off"
						cssStyle="width:100%" title="Building Permission Date"/>
				</div>
			</td>
			
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].buildingPlanPlinthArea.area" 
						maxlength="10" size="10" id="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].buildingPlanPlinthArea.area"
						value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].buildingPlanPlinthArea.area}"
						onblur="trim(this,this.value);checkForTwoDecimals(this,'Building paln plinth Area');checkZero(this,'Building paln plinth Area');"
						cssStyle="width:100%" title="Plinth area in building plan"/>
				</div>
			</td>
			
				<td class="blueborderfortd freeze-action-td" id="AddRemoveFloor">
				  <span id="addF" alt="AddF" class="tblactionicon add" 
			        onclick="javascript:addFloor(); return false;">
			           <i class="fa fa-plus-circle"></i>
				  </span>
				  &nbsp;
				  <span id="dDelF" alt="DelF" class="tblactionicon delete" 
				        onclick="javascript:delFloor(this);return false;">
				           <i class="fa fa-minus-circle"></i>
				  </span>
				</td>
			</tr>
		</s:iterator>
	</s:else>
</table>