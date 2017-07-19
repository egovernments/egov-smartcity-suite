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
<div align="center" class="overflow-x-scroll">
	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		class="tablebottom" id="vacantLandTable">
		<tr>
			<th class="bluebgheadtd"><s:text name="surveyNumber" /><span
				class="mandatory1">*</span></th>
			<th class="bluebgheadtd"><s:text name="pattaNumber" /><span
				class="mandatory1">*</span></th>
			<th class="bluebgheadtd"><s:text name="vacantLandArea" /><span
				class="mandatory1">*</span></th>
			<th class="bluebgheadtd"><s:text name="MarketValue" /><span
				class="mandatory1">*</span></th>
			<th class="bluebgheadtd"><s:text name="currentCapitalValue" /><span
				class="mandatory1">*</span></th>
			<th class="bluebgheadtd"><s:text name="constCompl.date" /><span
				class="mandatory1">*</span></th>
			<th class="bluebgheadtd"><s:text name="lbl.vl.plotarea" /><span
				class="mandatory1">*</span></th>
			<th class="bluebgheadtd"><s:text name="lbl.layout.authority" /><span
				class="mandatory1">*</span></th>
			<th class="bluebgheadtd"><s:text name="lbl.layout.permitno" /><span
				class="mandatory1">*</span></th>
			<th class="bluebgheadtd"><s:text name="lbl.layout.permitdate" /><span
				class="mandatory1">*</span></th>
		</tr>

		<tr id="vacantLandRow">
			<td class="blueborderfortd" align="center"><s:textfield
					name="propertyDetail.surveyNumber" id="propertyDetail.surveyNumber"
					maxlength="15" value="%{propertyDetail.surveyNumber}"
					title="Survey number of vacant land" /></td>
			<td class="blueborderfortd" align="center"><s:textfield
					name="propertyDetail.pattaNumber" id="propertyDetail.pattaNumber"
					maxlength="15" value="%{propertyDetail.pattaNumber}"
					title="Patta number of vacant land" /></td>
			<td class="blueborderfortd" align="center"><s:textfield
					name="propertyDetail.sitalArea.area" id="vacantLandArea"
					title="Vacant land total area in meters" maxlength="10"
					value="%{propertyDetail.sitalArea.area}"
					onblur="trim(this,this.value);checkForTwoDecimals(this,'propertyDetail.sitalArea.area');checkZero(this,'propertyDetail.sitalArea.area');" />
			</td>
			<td class="blueborderfortd" align="center">
				<s:if test="propertyDetail.marketValue!=null">
					<s:text name="format.money" var="marketval">
						<s:param name="value" value="propertyDetail.marketValue" />
					</s:text>
					<s:textfield name="propertyDetail.marketValue" id="marketValue"
					title="Market value of the vacant land" maxlength="10"
					value="%{#marketval}"
					onblur="trim(this,this.value);checkForTwoDecimals(this,'propertyDetail.marketValue');checkZero(this,'propertyDetail.marketValue');" />

				</s:if>
				<s:else>
					<s:textfield name="propertyDetail.marketValue" id="marketValue"
						title="Market value of the vacant land" maxlength="10"
						value="%{propertyDetail.marketValue}"
						onblur="trim(this,this.value);checkForTwoDecimals(this,'propertyDetail.marketValue');checkZero(this,'propertyDetail.marketValue');" />

				</s:else>
			</td>

			<td class="blueborderfortd">
				<s:if test="propertyDetail.currentCapitalValue!=null">
					<s:text name="format.money" var="capitalval">
						<s:param name="value" value="propertyDetail.currentCapitalValue" />
					</s:text>
						<s:textfield name="propertyDetail.currentCapitalValue" id="currentCapitalValue"
							maxlength="15" value="%{#capitalval}"
							title="Current capital value of Land"
							onblur="trim(this,this.value);checkForTwoDecimals(this,'propertyDetail.currentCapitalValue');checkZero(this,'propertyDetail.currentCapitalValue');" />
				</s:if>
				<s:else>
					<s:textfield name="propertyDetail.currentCapitalValue" id="currentCapitalValue"
							maxlength="15" value="%{propertyDetail.currentCapitalValue}"
							title="Current capital value of Land"
							onblur="trim(this,this.value);checkForTwoDecimals(this,'propertyDetail.currentCapitalValue');checkZero(this,'propertyDetail.currentCapitalValue');" />

				</s:else>
			</td>

			<td class="blueborderfortd"><s:date
					name="%{propertyDetail.dateOfCompletion}" var="occupationDate"
					format="dd/MM/yyyy" /> <s:textfield
					name="propertyDetail.dateOfCompletion"
					id="propertyDetail.dateOfCompletion" value="%{#occupationDate}"
					autocomplete="off" cssClass="datepicker" data-date-end-date="0d"
					maxlength="10"></s:textfield></td>
			<td class="greybox"><s:select headerKey="-1"
					headerValue="%{getText('default.select')}"
					name="vacantLandPlotAreaId" id="vacantLandPlotAreaId" listKey="id"
					listValue="name" list="dropdownData.vacantLandPlotAreaList"
					value="%{vacantLandPlotAreaId}" cssClass="selectnew"
					onchange="makeMandatory();" /></td>
			<td class="greybox"><s:select headerKey="-1"
					headerValue="%{getText('default.select')}"
					name="layoutApprovalAuthorityId" id="layoutApprovalAuthorityId"
					listKey="id" listValue="name"
					list="dropdownData.layoutApprovalAuthorityList"
					value="%{layoutApprovalAuthorityId}" cssClass="selectnew"
					onchange="makeMandatory();" /></td>
			<td class="blueborderfortd" align="center"><s:textfield
					name="propertyDetail.layoutPermitNo" id="layoutPermitNo"
					maxlength="15" value="%{propertyDetail.layoutPermitNo}" /></td>
			<td class="blueborderfortd"><s:date
					name="%{propertyDetail.layoutPermitDate}" var="layoutPermitDate"
					format="dd/MM/yyyy" /> <s:textfield
					name="propertyDetail.layoutPermitDate" id="layoutPermitDate"
					value="%{#layoutPermitDate}" autocomplete="off"
					cssClass="datepicker" maxlength="10"></s:textfield></td>
		</tr>
	</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0"
	class="tablebottom" id="vacantLandTable">
	<tr>
	<tr>
		<td colspan="6">
			<table class="tablebottom" style="width: 100%;" id="boundaryData">
				<tbody>
					<tr>
						<td colspan="4">
							<div class="headingsmallbg">
								<span class="bold"><s:text name="boundaries" /></span>
							</div>
						</td>
					</tr>
					<tr>
						<th class="bluebgheadtd"><s:text name="North" /><span
							class="mandatory1">*</span></th>
						<th class="bluebgheadtd"><s:text name="East" /><span
							class="mandatory1">*</span></th>
						<th class="bluebgheadtd"><s:text name="West" /><span
							class="mandatory1">*</span></th>
						<th class="bluebgheadtd"><s:text name="South" /><span
							class="mandatory1">*</span></th>
					</tr>
					<tr>
						<td class="blueborderfortd" align="center"><s:textfield
								name="northBoundary" id="northBoundary" maxlength="32"
								value="%{northBoundary}" title="North side of Land" /></td>
						<td class="blueborderfortd" align="center"><s:textfield
								name="eastBoundary" id="eastBoundary" maxlength="32"
								value="%{eastBoundary}" title="East side of Land" /></td>
						<td class="blueborderfortd" align="center"><s:textfield
								name="westBoundary" id="westBoundary" maxlength="32"
								value="%{westBoundary}" title="West side of land" /></td>
						<td class="blueborderfortd" align="center"><s:textfield
								name="southBoundary" id="southBoundary" maxlength="32"
								value="%{southBoundary}" title="South side of land" /></td>
					</tr>
				</tbody>
			</table>
		</td>
	</tr>
</table>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/vacantland.js?rnd=${app_release_no}' context='/ptis'/>"></script>
