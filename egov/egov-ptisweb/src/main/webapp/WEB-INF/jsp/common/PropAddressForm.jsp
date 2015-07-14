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
<script>
 jQuery(document).ready(function(){
	 jQuery('#locality').change(function() {
		console.log("came jursidiction"+jQuery('#locality').val());
		jQuery.ajax({
			url: "/ptis/common/ajaxCommon-blockByLocality.action",
			type: "GET",
			data: {
				locality : jQuery('#locality').val()
			},
			cache: false,
			dataType: "json",
			success: function (response) {
				jQuery('#zoneId').val(response.zoneId);
				jQuery('#wardId').val(response.wardId);
				jQuery('#blockId').val(response.blockId);
				jQuery('#zoneName').val(response.zoneName);
				jQuery('#wardName').val(response.wardName);
				jQuery('#blockName').val(response.blockName);
			}, 
			error: function (response) {
				console.log("failed");
				alert("No boundary details mapped for locality")
			}
		});
	});
});
</script>
<div id="PropAddressDiv">
	<tr>
		<td colspan="5"><div class="headingsmallbg"><span class="bold"><s:text name="PropertyAddress"/></span></div></td>
	</tr>
	
	 <tr>
  	<td class="greybox2">&nbsp;</td>
	<td class="greybox"><s:text name="locality"></s:text> <span class="mandatory1">*</span> : </td>
	<td class="greybox"><s:select name="locality" id="locality" list="dropdownData.localityList"
	listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}" value="%{locality}"/>
	</td>
    <td class="greybox" colspan="2">&nbsp;</td>
  </tr>
	
	<tr>
		<td class="bluebox2">&nbsp;</td>
	    <td class="bluebox"><s:text name="zone"></s:text> <span class="mandatory1">*</span> : </td>
	    <td class="bluebox"><s:textfield name="zoneName" id="zoneName" value="%{zoneName}" maxlength="20" readOnly="true"/></td>
	    <s:hidden id="zoneId" name="zoneId" value="%{zoneId}"></s:hidden>
	    <td class="bluebox"><s:text name="revwardno"></s:text> <span class="mandatory1">*</span>: </td>
	    <td class="bluebox"><s:textfield name="wardName" id="wardName" value="%{wardName}" maxlength="20" readOnly="true" /></td>
	    <s:hidden id="wardId" name="wardId" value="%{wardId}"></s:hidden>
	</tr>
	
	<tr>
		<td class="bluebox2">&nbsp;</td>
	    <td class="bluebox"><s:text name="blockno"></s:text> <span class="mandatory1">*</span> :  </td>
	    <td class="bluebox"> <s:textfield name="blockName" id="blockName" value="%{blockName}"  maxlength="20" readOnly="true" /></td>
	    <s:hidden id="blockId" name="blockId" value="%{blockId}"  ></s:hidden>
	    <td class="bluebox"><s:text name="Street"></s:text> : </td>
	    <td class="bluebox"><s:textfield id="street" name="street" maxlength="128" value="%{basicProperty.propertyID.Street.name}"/></td>
	</tr>
	
	<tr>
		<td class="bluebox2">&nbsp;</td>
	    <td class="bluebox"><s:text name="elec.wardno"></s:text> : </td>
	    <td class="bluebox"><s:select name="electionWardId" id="electionWardId" list="dropdownData.electionWardList"
	listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}" value="%{electionWardId}"/></td>
	    <td class="bluebox"><s:text name="doorno"></s:text> <span class="mandatory1">*</span> : </td>
	    <td class="bluebox"><s:textfield name="houseNumber" value="%{houseNumber}" maxlength="50" onblur="return checkHouseNoStartsWithNo(this); validatePlotNo(this,'Plot No/House No');"/></td> 
	</tr>
 
	<tr>
	    <td class="bluebox2">&nbsp;</td>
	    <td class="greybox"><s:text name="enumerationblock"/> : </td>
	    <td class="greybox">
	     <s:select id="enumBlock" name="enumBlock" headerValue="%{getText('default.select')}" value="%{enumBlock}" listKey="id" listValue="name" headerKey="0" 
	     list="dropdownData.enumerationBlockList"/>
	    </td>
	    <td class="greybox"><s:text name="PinCode"/> : </td>
	    <td class="greybox"><s:textfield name="pinCode" value="%{pinCode}" onchange="trim(this,this.value);" maxlength="6" onblur = "validNumber(this);checkZero(this);"  /></td>
	</tr>
	
</div>
