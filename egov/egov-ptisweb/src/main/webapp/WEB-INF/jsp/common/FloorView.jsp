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
<%@ include file="/includes/taglibs.jsp" %>
<c:set value="no" var="isTenant"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom" id="floorDetails">
    <tr>
		<th class="bluebgheadtd"><s:text name="FloorNo" /><span	class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="ConstructionType" /><span class="mandatory1" id="constTypeMdtry">*</span><a
			onclick="openWindow('ConstType.jsp');"> <img src="../resources/image/help.gif" style="border: none" /></a></th>
		<th class="bluebgheadtd"><s:text name="Usage" /><span class="mandatory1" id="usageMdtry">*</span><a
			onclick="openWindow('UsageMaster.jsp');"> <img src="../resources/image/help.gif" style="border: none" /></a></th>
		<th class="bluebgheadtd"><s:text name="Occupancy" /><span class="mandatory1" id="occMdtry">*</span></th>
		<th class="bluebgheadtd"><s:text name="Bldgage" /><span	class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="constrdate" /><span	class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="Width" /></th>
		<th class="bluebgheadtd"><s:text name="Length" /></th>
		<th class="bluebgheadtd"><s:text name="PlinthArea" /><span class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="capitalvalue"></s:text></th>
		<th class="bluebgheadtd"><s:text name="planappr" /></th>
		<s:if test="modifyRsn != 'DATA_UPDATE'">
			<th class="bluebgheadtd"><s:text name="rentAgreementDetails"/></th>
		</s:if>   
      	
    </tr>
    <s:iterator value="floorDetails" status="floorsstatus">
        <tr id="Floorinfo">
		
	    <td class="blueborderfortd" width="5%">
	    	<div align="center"> 
	    	<s:property default="N/A" value="%{floorNoStr[#floorsstatus.index]}" />
	        </div>
	    </td>
	     <td class="blueborderfortd" width="5%">
	    	<div align="center">
	    	<s:property default="N/A" value="%{structureClassification.typeName}" /> 
	        </div>
	    </td>
	    <td class="blueborderfortd" width="5%">
	    	<div align="center">
	    	<s:property default="N/A" value="%{propertyUsage.usageName}" />
	        </div>
	    </td>
	    <td class="blueborderfortd" width="5%">
	    	<div align="center">
	    	<s:property default="N/A" value="%{propertyOccupation.occupation}" />  
	        </div>
	    </td>	
	    <td class="blueborderfortd" width="5%">
			<div align="center"><s:property default="N/A" value="%{depreciationMaster.depreciationName}" /></div>
	    </td>	
	    <td class="blueborderfortd" width="5%">
	    <s:date name="createdDate" var="cdFormat" format="dd/MM/yyyy"/>
	    <s:property default="N/A" value="%{cdFormat}"/>
	    </td>
	    <td class="blueborderfortd" width="5%">
        <div align="center">
        	<s:property default="N/A" value="%{extraField5}" />
        </div>
        </td>
         <td class="blueborderfortd" width="5%">
        <div align="center">
        	<s:property default="N/A" value="%{extraField4}" />
        </div>
        </td>
        <td class="blueborderfortd" width="5%">
			<div align="center"><s:property default="N/A" value="%{builtUpArea.area}" /></div>
	    </td>
	    <td class="blueborderfortd" width="5%">
	    <s:property default="N/A" value="%{capitalValue}" />
	    </td>
	    <td class="blueborderfortd" width="5%">
	    <s:property default="N/A" value="%{planApproved}" />
	    </td>
        
        <td class="blueborderfortd">
        	<s:if test="rentPerMonth != null">
				<img id="agreementDetails" name="agreementDetails"
						src="${pageContext.request.contextPath}/image/bulletgo.gif"
						alt="Remove" onclick="openRentAgreementWindow(this, 'view');"
						width="18" height="18" border="0" />
				<c:set value="yes" var="isTenant"/>
			</s:if>
			<s:hidden id="agreementPeriod"
					name="rentAgreementDetail.agreementPeriod"
					value="%{rentAgreementDetail.agreementPeriod}" />
			<s:hidden id="agreementDate"
						name="rentAgreementDetail.agreementDate"
						value="%{rentAgreementDetail.agreementDate}" />
			<s:hidden id="incrementInRent"
						name="rentAgreementDetail.incrementInRent"
						value="%{rentAgreementDetail.incrementInRent}" />
		</td>
	</tr>
   </s:iterator>
</table>

<script>
   var val = "${isTenant}";
   if (val == 'no') {
 		var floorTable = document.getElementById("floorDetails");
		var headerRow = floorTable.rows[0];
		jQuery(headerRow.lastElementChild).hide();
   }
</script>

