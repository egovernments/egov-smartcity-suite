
<!--
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
	The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->
<%@ include file="/includes/taglibs.jsp"%>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="tablebottom" id="floorDetails">
	<tr>
		<th class="bluebgheadtd"><s:text name="FloorNo" /><span	class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="ConstructionType" /><span class="mandatory1" id="constTypeMdtry">*</span></th>
		<th class="bluebgheadtd"><s:text name="Usage" /><span class="mandatory1" id="usageMdtry">*</span></th>
		<th class="bluebgheadtd"><s:text name="Occupancy" /><span class="mandatory1" id="occMdtry">*</span></th>
		<th class="bluebgheadtd"><s:text name="constrdate" /><span	class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="PlinthArea" /><span	class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="exemptioncategory" /></th>
		<th class="bluebgheadtd"><s:text name="drainage" /></th>
		<th class="bluebgheadtd"><s:text name="noOfSeats" /></th>
		<s:if test="modifyRsn != 'DATA_UPDATE'">
			<th class="bluebgheadtd"><s:text name="Add/Delete" /></th>
		</s:if>
	</tr>
	<s:if test="propertyDetail.floorDetails.size()==0">
		<tr id="Floorinfo">
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select headerKey=""
						headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetails[0].floorNo" listKey="key"	id="floorNo"
						value="%{propertyDetail.floorDetails[0].floorNo}"
						listValue="value" list="floorNoMap" cssClass="selectnew" cssStyle="width:100%"
						data-optional="0" data-errormsg="Floor Number is mandatory!" />
				</div>
			</td>
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select headerKey="" headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetails[0].structureClassification.id"
						listKey="id" id="floorConstType"
						value="%{propertyDetail.floorDetails[0].structureClassification.id}"
						listValue="typeName" list="dropdownData.StructureList"
						cssClass="selectnew" cssStyle="width:100%" 
						data-optional="0" data-errormsg="Classification of building is required!"/>
				</div>
			</td>
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select headerKey="" headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetails[0].propertyUsage.id"
						listKey="id" id="floorUsage"
						value="%{propertyDetail.floorDetails[0].propertyUsage.id}"
						listValue="usageName" list="dropdownData.UsageList"
						cssClass="selectnew"
						cssStyle="width:100%" 
						data-optional="0" data-errormsg="Nature of usage is required!"/>
				</div>
			</td>
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select headerKey="" headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetails[0].propertyOccupation.id"
						listKey="id" id="floorOccupation"
						value="%{propertyDetail.floorDetails[0].propertyOccupation.id}"
						listValue="occupation" list="dropdownData.OccupancyList"
						cssClass="selectnew"
						cssStyle="width:100%" 
						data-optional="0" data-errormsg="Occupancy is required!"/>
				</div>
			</td>
			

			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield autocomplete="off"
						name="propertyDetail.floorDetails[0].occupancyDate" data-optional="0" data-errormsg="Ocuupancy date is required!"
						id="propertyDetail.floorDetails[0].occupancyDate" value="%{propertyDetail.floorDetails[0].occupancyDate}" size="10"
						maxlength="10" cssStyle="width:100%" cssClass="datepicker"></s:textfield>
				</div>
			</td>
			

			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield
						name="propertyDetail.floorDetails[0].builtUpArea.area" 
						maxlength="15" size="10" id="propertyDetail.floorDetails[0].builtUpArea.area" value="%{propertyDetail.floorDetails[0].builtUpArea.area}"
						onblur="trim(this,this.value);checkForTwoDecimals(this,'Assessable Area');checkZero(this,'Assessable Area');"
						cssStyle="width:100%" />
				</div>
			</td>

			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select name="taxExemptedReason" id="taxExemptedReason" headerValue="select"
							headerKey="" list="dropdownData.taxExemptionReasonList" value="%{propertyDetail.floorDetails[0].taxExemptedReason.id}" 
							listKey="id" listValue="name"
							cssClass="selectnew" data-optional="1">
						</s:select>
				</div>
			</td>
			
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select name="propertyDetail.floorDetails[0].drainage" id="propertyDetail.floorDetails[0].drainage" headerValue="select"
							headerKey="" list="#{'true':'Yes','false':'No' }" value="%{propertyDetail.floorDetails[0].drainage}"
							cssClass="selectnew" data-optional="1">
					</s:select>
				</div>
			</td>
			
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield autocomplete="off" name="propertyDetail.floorDetails[0].noOfSeats" data-optional="1"
						id="propertyDetail.floorDetails[0].noOfSeats" value="%{propertyDetail.floorDetails[0].noOfSeats}" size="10"
						maxlength="10" cssStyle="width:100%"></s:textfield>
				</div>
			</td>


			<s:if test="modifyRsn != 'DATA_UPDATE'">
				<td class="blueborderfortd" id="AddRemoveFloor"><img id="addF"
					name="addF"
					src="${pageContext.request.contextPath}/resources/image/addrow.gif"
					alt="Add" onclick="javascript:addFloor(); return false;" width="18"
					height="18" border="0" /><img id="dDelF" name="dDelF"
					src="${pageContext.request.contextPath}/resources/image/removerow.gif"
					alt="Remove" onclick="javascript:delFloor(this);return false;"
					width="18" height="18" border="0" /></td>

			</s:if>
		</tr>
	</s:if>
	<s:else>
		<s:iterator value="(propertyDetail.floorDetails.size).{#this}"
			status="floorsstatus">
			<tr id="Floorinfo">

				<td class="blueborderfortd" style="padding: 2px 2px"><s:select
						headerKey="" headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetails[%{#floorsstatus.index}].floorNo"
						listKey="key" id="floorNo" listValue="value" list="floorNoMap"
						cssClass="selectnew"
						value="%{propertyDetail.floorDetails[#floorsstatus.index].floorNo}"
						cssStyle="width:100%" /></td>

				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:if test="%{#floorsstatus.index == 0}">
							<s:select headerKey=""
								headerValue="%{getText('default.select')}"
								name="propertyDetail.floorDetails[%{#floorsstatus.index}].structureClassification.id"
								listKey="id" id="floorConstType"
								value="%{propertyDetail.floorDetails[#floorsstatus.index].structureClassification.id}"
								listValue="typeName" list="dropdownData.StructureList"
								cssClass="selectnew" cssStyle="width:100%" />
						</s:if>
						<s:else>
							<s:select headerKey=""
								headerValue="%{getText('default.select')}"
								name="propertyDetail.floorDetails[%{#floorsstatus.index}].structureClassification.id"
								listKey="id" id="floorConstType%{#floorsstatus.index-1}"
								value="%{propertyDetail.floorDetails[#floorsstatus.index].structureClassification.id}"
								listValue="typeName" list="dropdownData.StructureList"
								cssClass="selectnew" cssStyle="width:100%" />
						</s:else>
					</div>
				</td>
				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:if test="%{#floorsstatus.index==0}">
							<s:select headerKey=""
								headerValue="%{getText('default.select')}"
								name="propertyDetail.floorDetails[%{#floorsstatus.index}].propertyUsage.id"
								listKey="id" id="floorUsage" listValue="usageName"
								list="dropdownData.UsageList" cssClass="selectnew"
								value="%{propertyDetail.floorDetails[#floorsstatus.index].propertyUsage.id}"
								cssStyle="width:100%" />
						</s:if>
						<s:else>
							<s:select headerKey=""
								headerValue="%{getText('default.select')}"
								name="propertyDetail.floorDetails[%{#floorsstatus.index}].propertyUsage.id"
								listKey="id" id="floorUsage%{#floorsstatus.index-1}"
								listValue="usageName" list="dropdownData.UsageList"
								cssClass="selectnew"
								value="%{propertyDetail.floorDetails[#floorsstatus.index].propertyUsage.id}"
								cssStyle="width:100%" />
						</s:else>
					</div>
				</td>

				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:select headerKey=""
							headerValue="%{getText('default.select')}"
							name="propertyDetail.floorDetails[%{#floorsstatus.index}].propertyOccupation.id"
							listKey="id" id="floorOccupation" listValue="occupation"
							list="dropdownData.OccupancyList" cssClass="selectnew"
							value="%{propertyDetail.floorDetails[#floorsstatus.index].propertyOccupation.id}"
							cssStyle="width:100%" />
					</div>
				</td>
				
				
				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
					<s:date name="%{propertyDetail.floorDetails[#floorsstatus.index].occupancyDate}" var="occupationDate" format="dd/MM/yyyy" />
						<s:textfield autocomplete="off"
							name="propertyDetail.floorDetails[%{#floorsstatus.index}].occupancyDate" value="%{#occupationDate}"
							id="propertyDetail.floorDetails[%#floorsstatus.index].occupancyDate" size="10"
							maxlength="10" cssStyle="width:100%" cssClass="datepicker"></s:textfield>
					</div>
				</td>

				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:textfield
							name="propertyDetail.floorDetails[%{#floorsstatus.index}].builtUpArea.area"
							maxlength="15" size="10" id="propertyDetail.floorDetails[%{#floorsstatus.index}].builtUpArea.area" readOnly="true"
							value="%{propertyDetail.floorDetails[#floorsstatus.index].builtUpArea.area}"
							onblur="trim(this,this.value);checkForTwoDecimals(this,'Assessable Area');checkZero(this,'Assessable Area');"
							cssStyle="width:100%" />
					</div>
				</td>

				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:select name="propertyDetail.floorDetails[%{#floorsstatus.index}].exemptioncategory" id="propertyDetail.floorDetails[0].exemptioncategory" headerValue="select"
								headerKey="" list="#{}" value="%{propertyDetail.floorDetails[%{#floorsstatus.index}].exemptioncategory}"
								cssClass="selectnew" data-optional="1">
							</s:select>
					</div>
				</td>
				
				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:select name="propertyDetail.floorDetails[%{#floorsstatus.index}].drainage" id="propertyDetail.floorDetails[%{#floorsstatus.index}].drainage" headerValue="select"
								headerKey="" list="#{'true':'Yes','false':'No' }" value="%{propertyDetail.floorDetails[%{#floorsstatus.index}].drainage}"
								cssClass="selectnew" data-optional="1">
						</s:select>
					</div>
				</td>
				
				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:textfield autocomplete="off"
							name="propertyDetail.floorDetails[%{#floorsstatus.index}].noofseats" data-optional="1"
							id="propertyDetail.floorDetails[%{#floorsstatus.index}].noofseats" value="%{propertyDetail.floorDetails[%{#floorsstatus.index}].noofseats}" size="10"
							maxlength="10" cssStyle="width:100%"></s:textfield>
					</div>
				</td>
				
				<s:if test="modifyRsn != 'DATA_UPDATE'">
					<td class="blueborderfortd" id="AddRemoveFloor"><img id="addF"
						name="addF"
						src="${pageContext.request.contextPath}/resources/image/addrow.gif"
						alt="Add" onclick="javascript:addFloor(); return false;"
						width="18" height="18" border="0" /><img id="dDelF" name="dDelF"
						src="${pageContext.request.contextPath}/resources/image/removerow.gif"
						alt="Remove" onclick="javascript:delFloor(this);return false;"
						width="18" height="18" border="0" /></td>

				</s:if>
			</tr>
		</s:iterator>
	</s:else>
</table>