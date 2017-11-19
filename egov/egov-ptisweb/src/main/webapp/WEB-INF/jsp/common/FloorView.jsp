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

<%@ include file="/includes/taglibs.jsp" %>
<c:set value="no" var="isTenant"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom" id="floorDetails">
    <tr>
		<th class="bluebgheadtd"><s:text name="FloorNo" /></th>
		<th class="bluebgheadtd"><s:text name="ConstructionType" /></th>
		<th class="bluebgheadtd"><s:text name="Usage"/></th>
		<th class="bluebgheadtd"><s:text name="firmName"/></th>
		<th class="bluebgheadtd"><s:text name="Occupancy"/></th>
		<th class="bluebgheadtd"><s:text name="Occupantname"/></th>
		<th class="bluebgheadtd"><s:text name="constrdate" /></th>
		<th class="bluebgheadtd"><s:text name="effectiveDate" /></th>
		<th class="bluebgheadtd"><s:text name="unstructuredLand" /></th>
		<th class="bluebgheadtd"><s:text name="plinthLength" /></th>
		<th class="bluebgheadtd"><s:text name="plinthBreadth" /></th>
		<th class="bluebgheadtd"><s:text name="PlinthArea" /></th>
		<th class="bluebgheadtd"><s:text name="building.permNo" />
		<th class="bluebgheadtd"><s:text name="buildingpermdate" />
		<th class="bluebgheadtd"><s:text name="buildingpermplintharea" />
		<th class="bluebgheadtd"><s:text name="unit.rate.amount" /></th>
    </tr>
    <s:iterator value="floorDetails" status="floorsstatus">
        <tr id="Floorinfo">
		
	    <td class="blueborderfortd" style="padding: 2px 2px">
	    	<div align="center"> 
	    	<span class="bold"><s:property default="N/A" value="%{floorNoStr[#floorsstatus.index]}" /></span>
	        </div>
	    </td>
	     <td class="blueborderfortd" style="padding: 2px 2px">
	    	<div align="center">
	    	<span class="bold"><s:property default="N/A" value="%{structureClassification.typeName}" /> </span>
	        </div>
	    </td>
	    <td class="blueborderfortd" style="padding: 2px 2px">
	    	<div align="center">
	    	<span class="bold"><s:property default="N/A" value="%{propertyUsage.usageName}" /></span>
	        </div>
	    </td>
	     <td class="blueborderfortd" style="padding: 2px 2px">
	    	<div align="center">
		    	<span class="bold">
		    		<s:if test="%{firmName == ''}">N/A</s:if>
		    		<s:else><s:property default="N/A" value="%{firmName}" /></s:else>
		    	</span>
	        </div>
	    </td>	
	    <td class="blueborderfortd" style="padding: 2px 2px">
	    	<div align="center">
	    	<span class="bold"><s:property default="N/A" value="%{propertyOccupation.occupation}" /> </span>
	        </div>
	    </td>	
	    <td class="blueborderfortd" style="padding: 2px 2px">
	    	<div align="center">
		    	<span class="bold">
		    		<s:if test="%{occupantName == ''}">N/A</s:if>
		    		<s:else><s:property default="N/A" value="%{occupantName}" /></s:else>
		    	</span>
	        </div>
	    </td>	

		<td class="blueborderfortd" style="padding: 2px 2px">
		    <s:date name="constructionDate" var="cdFormat" format="dd/MM/yyyy"/>
		    <span class="bold"><s:property default="N/A" value="%{cdFormat}"/></span>
	    </td>
	    
	    <td class="blueborderfortd" style="padding: 2px 2px">
		    <s:date name="occupancyDate" var="cdFormat" format="dd/MM/yyyy"/>
		    <span class="bold"><s:property default="N/A" value="%{cdFormat}"/></span>
	    </td>
	    
	     <td class="blueborderfortd" style="padding: 2px 2px">
			<span class="bold"><div align="center">
			<s:if test="%{unstructuredLand}">Yes</s:if> 
			<s:else>No</s:else>
			</div></span>
	    </td>
	    
	     <td class="blueborderfortd" style="padding: 2px 2px">
			<span class="bold"><div align="center"><s:property default="N/A" value="%{builtUpArea.length}" /></div></span>
	    </td>
	    
	     <td class="blueborderfortd" style="padding: 2px 2px">
			<span class="bold"><div align="center"><s:property default="N/A" value="%{builtUpArea.breadth}" /></div></span>
	    </td>
	    
	   
        <td class="blueborderfortd" style="padding: 2px 2px">
			<span class="bold"><div align="center"><s:property default="N/A" value="%{builtUpArea.area}" /></div></span>
	    </td>
	     
	    <td class="blueborderfortd" style="padding: 2px 2px">
			<span class="bold"><div align="center"><s:property default="N/A" value="%{buildingPermissionNo}" /></div></span>
	    </td>
	    
	    <td class="blueborderfortd" style="padding: 2px 2px">
			<s:date name="buildingPermissionDate" var="bpdate" format="dd/MM/yyyy"/>
	    <span class="bold"><s:property default="N/A" value="%{bpdate}"/></span> 
	    </td>
	    
	    <td class="blueborderfortd" style="padding: 2px 2px">
			<span class="bold"><div align="center"><s:property default="N/A" value="%{buildingPlanPlinthArea.area}" /></div></span>
	    </td>
	    
	    <td class="blueborderfortd" style="padding: 2px 2px">
			<span class="bold"><div align="center"><s:property default="N/A" value="%{floorDmdCalc.categoryAmt}" /></div></span>
	    </td>
	</tr>
   </s:iterator> 
</table>

<script>
   var val = "${isTenant}";
   if (val == 'no') {
 		var floorTable = document.getElementById("floorDetails");
		var headerRow = floorTable.rows[0];
   }
</script>

