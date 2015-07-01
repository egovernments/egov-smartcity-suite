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
<div id="PropAddressDiv">
	<tr>
		<td colspan="5"><div class="headingsmallbg"><span class="bold"><s:text name="PropertyAddress"/></span></div></td>
	</tr>
	
  <tr>
  	<td class="greybox2">&nbsp;</td>
  	<td class="greybox"><s:text name="locality"/> : </td>
	<td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.locality.name}" /> </span>
	</td>
    <td class="greybox" colspan="2">&nbsp;</td>
  </tr>

	<tr>
		<td class="bluebox2" width="8%">&nbsp;</td>
	    <td class="bluebox" width="8%"><s:text name="zone"/> : </td>
	    <td class="bluebox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.zone.name}" /> </span></td>
	    <td class="bluebox" width="10%"><s:text name="revwardno"/> : </td>
	    <td class="bluebox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.ward.name}" /> </span></td>
	</tr>
	<tr>
		<td class="greybox2" width="8%">&nbsp;</td>
	    <td class="greybox" width="8%"><s:text name="blockno"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.area.name}" /> </span></td>
	    <td class="greybox" width="10%"><s:text name="Street"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.Street.name}" /> </span></td>
	</tr>
	<tr>
		<td class="bluebox2" width="8%">&nbsp;</td>
	    <td class="bluebox" width="8%"><s:text name="elec.wardno"/> : </td>
	    <td class="bluebox"><span class="bold"><s:property default="N/A" value="%{basicProp.address.extraField1}" /> </span></td>
	    <td class="bluebox" width="10%"><s:text name="doorno"/> : </td>
	    <td class="bluebox"><span class="bold"><s:property default="N/A" value="%{basicProp.address.extraField2}" /> </span></td>
	</tr>
	
	<tr>
		<td class="greybox2" width="8%">&nbsp;</td>
	    <td class="greybox" width="8%"><s:text name="Address"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProp.address.extraField3}" /> </span></td>
	    <td class="greybox" width="10%"><s:text name="PinCode"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProp.address.extraField4}" /> </span></td>
	</tr>
</div>
