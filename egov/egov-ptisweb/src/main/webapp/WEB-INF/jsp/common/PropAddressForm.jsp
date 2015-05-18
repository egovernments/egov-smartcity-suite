#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ include file="/includes/taglibs.jsp" %>
<div id="PropAddressDiv">
	<tr>
		<td colspan="5"><div class="headingsmallbg"><span class="bold"><s:text name="PropertyAddress"/></span></div></td>
	</tr>
	
  <tr>
  	<td class="greybox2">&nbsp;</td>
  	<egov:ajaxdropdown id="areaId" fields="['Text','Value']" dropdownId="areaId" url="common/ajaxCommon-streetByWard.action" />
	<td class="greybox"><s:text name="Area"/><span class="mandatory1">*</span> : </td>
	<td class="greybox"><s:select name="areaId" id="areaId" list="dropdownData.streetList"
	listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}" value="%{areaId}"/>
	</td>
    <td class="greybox" colspan="2">&nbsp;</td>
  </tr>

	<tr>
		<td class="bluebox2" width="8%">&nbsp;</td>
	    <td class="bluebox" width="8%"><s:text name="HouseNo"/><span class="mandatory1">*</span> : </td>
	    <td class="bluebox"><s:textfield name="houseNumber" value="%{houseNumber}" maxlength="50" onblur="return checkHouseNoStartsWithNo(this); validatePlotNo(this,'Plot No/House No');"/></td>
	    <td class="bluebox" width="10%"><s:text name="OldNo"/> : </td>
	    <td class="bluebox"><s:textfield name="oldHouseNo" value="%{oldHouseNo}" maxlength="50" onblur="validatePlotNo(this,'Old No');"/></td>
	</tr>
	<tr>
		<td class="greybox2" width="8%">&nbsp;</td>
	    <td class="greybox" width="8%"><s:text name="Address"/> : </td>
	    <td class="greybox"><s:textfield name="addressStr" value="%{addressStr}" maxlength="512" onblur="validateAddress(this);"/></td>
	    <td class="greybox" width="10%"><s:text name="PinCode"/> : </td>
	    <td class="greybox"><s:textfield name="pinCode" value="%{pinCode}" onchange="trim(this,this.value);" maxlength="6" onblur = "validNumber(this);checkZero(this);"  /></td>
	</tr>
	
	<tr>
		<td class="bluebox2" width="8%">&nbsp;</td>
	    <td class="bluebox" width="8%"><s:text name="address.khasraNumber"/> : </td>
	    <td class="bluebox"><s:textfield id="khasraNumber" name="khasraNumber" value="%{khasraNumber}"  maxlength="128" /></td>
	    <td class="bluebox" width="10%"><s:text name="address.Mauza"/> : </td>
	    <td class="bluebox"><s:textfield  id="mauza"  name="mauza" value="%{mauza}" maxlength="128" /></td>
	</tr>
	
	<tr>
		<td class="greybox2" width="8%">&nbsp;</td>
	    <td class="greybox" width="8%"><s:text name="address.citySurveyNumber"/> : </td>
	    <td class="greybox"><s:textfield id="citySurveyNumber" name="citySurveyNumber" value="%{citySurveyNumber}" maxlength="128" /></td>
	    <td class="greybox" width="10%"><s:text name="address.sheetNumber"/> : </td>
	    <td class="greybox"><s:textfield id="sheetNumber" name="sheetNumber" value="%{sheetNumber}" maxlength="128" /></td>
	</tr>
</div>
