<%@ include file="/includes/taglibs.jsp" %>
<c:set value="no" var="isTenant"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom" id="floorDetails">
    <tr>
		<th class="bluebgheadtd"><s:text name="UnitNo"/></th>
		<th class="bluebgheadtd"><s:text name="unitType"/></th>
		<th class="bluebgheadtd"><s:text name="unitTypeCategory"/></th>
		<th class="bluebgheadtd"><s:text name="FloorNo"/></th>
		<th class="bluebgheadtd"><s:text name="TaxExmRsn" /></th>
		<th class="bluebgheadtd"><s:text name="type"/></th>
		<th class="bluebgheadtd"><s:text name="OccupierName"/></th>
		<th class="bluebgheadtd"><s:text name="AssableArea"/></th>
		<th class="bluebgheadtd"><s:text name="Usage"/><a onclick="openWindow('UsageMaster.jsp');"> <img src="../image/help.gif" style="border: none" /> </a></th>
		<th class="bluebgheadtd"><s:text name="Occupancy"/></th>
		<th class="bluebgheadtd"><s:text name="GenWaterRate"/></th>
		<th class="bluebgheadtd"><s:text name="OccupationDate"/> <s:text name="dateFormat"/></th>
		<th class="bluebgheadtd"><s:text name="ConstructionType"/><a onclick="openWindow('ConstType.jsp');"> <img src="../image/help.gif" style="border: none" /> </a></th>
		<th class="bluebgheadtd"><s:text name="ConstructionYear"/></th>
		<th class="bluebgheadtd"><s:text name="Rent"/></th>
		<th class="bluebgheadtd"><s:text name="Width"/></th>
		<th class="bluebgheadtd"><s:text name="Length"/></th>
		<th class="bluebgheadtd"><s:text name="BuiltUpArea"/></th>
		<th class="bluebgheadtd"><s:text name="manualAlv"/></th>
		<s:if test="modifyRsn != 'DATA_UPDATE'">
			<th class="bluebgheadtd"><s:text name="rentAgreementDetails"/></th>
		</s:if>   
      	
    </tr>
    <s:iterator value="floorDetails" status="floorsstatus">
        <tr id="Floorinfo">
		<td class="blueborderfortd" width="5%">
			<div align="center"><s:property default="N/A" value="%{extraField1}" /></div>
	    </td>
	    <td class="blueborderfortd" width="5%">
	    	<div align="center"> 
	    		<s:property default="N/A" value="%{unitType.type}" />
	        </div>
	    </td>
	    <td class="blueborderfortd" width="5%">
	    	<div align="center"> 	    		
	    		<script type="text/javascript">
	    			var category = '<s:property default="N/A" value="%{@org.egov.ptis.actions.common.CommonServices@getUnitTypeCategory(unitType.code, unitTypeCategory)}" />';
	    			var categoryStrings = category.split(" ");
	    			for (var k = 0; k < categoryStrings.length; k++) {
	    				document.write(" " + categoryStrings[k]);	
	    			}						    			
	    		</script>	
	        </div>
	    </td>
	    <td class="blueborderfortd" width="5%">
	    	<div align="center"> 
	    	<s:property default="N/A" value="%{floorNoStr[#floorsstatus.index]}" />
	        </div>
	    </td>
	     <td class="blueborderfortd" width="5%">
	    	<div align="center"> 	    		
	    		<s:property default="N/A" value="%{taxExemptedReason}" />
	        </div>
	    </td>
	    <td class="blueborderfortd" width="5%">
			<div align="center"><s:property default="N/A" value="%{extraField7}" /></div>
	    </td>
		<td class="blueborderfortd" width="5%">
			<div align="center"><s:property default="N/A" value="%{extraField2}" /></div>
	    </td>
		<td class="blueborderfortd" width="5%">
			<div align="center"><s:property default="N/A" value="%{builtUpArea.area}" /></div>
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
		<td class="blueborderfortd" widh>
			<div align="center">
				<s:property default="N/A" value="%{@org.egov.ptis.actions.common.CommonServices@getWaterMeterDtls(waterRate)}" />
			</div>
		</td>		
	    <td class="blueborderfortd" width="4%">
	    <div align="center">
			<s:date name="extraField3" var="cdFormat" format="dd/MM/yyyy"/>
        <s:property default="N/A" value="%{extraField3}" />        </div>
        </td>
	    <td class="blueborderfortd" width="5%">
	    	<div align="center">
	    	<s:property default="N/A" value="%{structureClassification.typeName}" /> 
	        </div>
	    </td>
		<td class="blueborderfortd" width="5%">
			<div align="center"><s:property default="N/A" value="%{depreciationMaster.depreciationName}" /></div>
	    </td>	    
        <td class="blueborderfortd" width="5%">
        <div align="center">
        	<s:property default="N/A" value="%{rentPerMonth}" />
        </div>
        </td>
        <td class="blueborderfortd" width="5%">
        <div align="center">
        	<s:property default="N/A" value="%{extraField4}" />
        </div>
        </td>
        <td class="blueborderfortd" width="5%">
        <div align="center">
        	<s:property default="N/A" value="%{extraField5}" />
        </div>
        </td>
        <td class="blueborderfortd" width="5%">
        <div align="center">
        	<s:property default="N/A" value="%{extraField6}" />
        </div>
        </td>
        <td class="blueborderfortd" width="5%">
	        <div align="center">
	        	<s:property default="N/A" value="%{manualAlv}" />
	        </div>
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

