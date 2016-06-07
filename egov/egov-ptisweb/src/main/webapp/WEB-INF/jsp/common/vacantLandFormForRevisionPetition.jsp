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
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="vacantLandTable">
	
	<tr>
	    <th class="bluebgheadtd" style="height: 45px;"><s:text name="surveyNumber" /><span class="mandatory1">*</span></th>
	    <th class="bluebgheadtd"><s:text name="pattaNumber" /><span class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="vacantLandArea" /><span class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="MarketValue" /><span class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="currentCapitalValue" /><span	class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="constCompl.date" /><span	class="mandatory1">*</span></th>
    </tr>
	
	<tr id="vacantLandRow">
        <td class="blueborderfortd" align="center">
		 <s:textfield name="property.propertyDetail.surveyNumber" id="property.propertyDetail.surveyNumber"
				maxlength="15" value="%{property.propertyDetail.surveyNumber}" />
		</td>
        <td class="blueborderfortd" align="center">
        	<s:textfield name="property.propertyDetail.pattaNumber" id="property.propertyDetail.pattaNumber"
				maxlength="15" value="%{property.propertyDetail.pattaNumber}" />
        </td>
        <td class="blueborderfortd" align="center">
        	<s:textfield name="property.propertyDetail.sitalArea.area"  id="vacantLandArea"
				maxlength="15" value="%{property.propertyDetail.sitalArea.area}"
				onblur="trim(this,this.value);checkForTwoDecimals(this,'property.propertyDetail.sitalArea.area');checkZero(this,'property.propertyDetail.sitalArea.area');" />
        </td>
        <td class="blueborderfortd" align="center">
        	<s:textfield name="property.propertyDetail.marketValue" id="marketValue"
				maxlength="15" value="%{property.propertyDetail.marketValue}"
				onblur="trim(this,this.value);checkForTwoDecimals(this,'property.propertyDetail.marketValue');checkZero(this,'property.propertyDetail.marketValue');" />
		</td>
        
        <td class="blueborderfortd">
        	<s:textfield name="property.propertyDetail.currentCapitalValue"
				id="currentCapitalValue" maxlength="15"  readOnly="true" value="%{property.propertyDetail.currentCapitalValue}"
				onblur="trim(this,this.value);checkForTwoDecimals(this,'property.propertyDetail.currentCapitalValue');checkZero(this,'property.propertyDetail.currentCapitalValue');" />
        </td>
        
        <td class="blueborderfortd">
		   <s:date name="%{property.propertyDetail.dateOfCompletion}" var="occupationDate" format="dd/MM/yyyy" /> <s:textfield
				name="property.propertyDetail.dateOfCompletion" id="property.propertyDetail.dateOfCompletion" value="%{#occupationDate}" autocomplete="off" cssClass="datepicker"
				size="10" maxlength="10"></s:textfield>
		</td>
    </tr>
    
   <tr>
		<td colspan="6"><br />
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
						<th class="bluebgheadtd"><s:text name="North" /><span class="mandatory1">*</span></th>
						<th class="bluebgheadtd"><s:text name="East" /><span class="mandatory1">*</span></th>
						<th class="bluebgheadtd"><s:text name="West" /><span class="mandatory1">*</span></th>
						<th class="bluebgheadtd"><s:text name="South" /><span class="mandatory1">*</span></th>
					</tr>
					<tr>
						<td class="blueborderfortd" align="center"><s:textfield name="northBoundary" id="northBoundary" maxlength="64"
								value="%{northBoundary}" /></td>
						<td class="blueborderfortd" align="center"><s:textfield name="eastBoundary" id="eastBoundary" maxlength="64"
								value="%{eastBoundary}" /></td>
						<td class="blueborderfortd" align="center"><s:textfield name="westBoundary" id="westBoundary" maxlength="64"
								value="%{westBoundary}" /></td>
						<td class="blueborderfortd" align="center"><s:textfield	name="southBoundary" id="southBoundary" maxlength="64"
								value="%{southBoundary}" /></td>
					</tr>

				</tbody>
			</table>
			</td>
	</tr>
	
</table>
<script type="text/javascript">
	 jQuery("#marketValue").blur(function() {
		 var vacantLandArea = jQuery("#vacantLandArea").val();
		 var marketValue =  jQuery("#marketValue").val();
		 var capitalValue = vacantLandArea * marketValue;
		 jQuery("#currentCapitalValue").val(roundoff(capitalValue));
	 });
</script>