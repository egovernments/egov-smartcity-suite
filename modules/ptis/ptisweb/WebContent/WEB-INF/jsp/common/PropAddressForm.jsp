<%@ include file="/includes/taglibs.jsp" %>
<div id="PropAddressDiv">
	<tr>
		<td colspan="5"><div class="headingsmallbg"><span class="bold"><s:text name="PropertyAddress"/></span></div></td>
	</tr>
	
  <tr>
  	<td class="greybox2">&nbsp;</td>
  	<egov:ajaxdropdown id="areaId" fields="['Text','Value']" dropdownId="areaId" url="common/ajaxCommon!areaByWard.action" />
	<td class="greybox"><s:text name="Area"/><span class="mandatory1">*</span> : </td>
	<td class="greybox"><s:select name="areaId" id="areaId" list="dropdownData.areaList"
	listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}" value="%{areaId}"/>
	</td>
    <td class="greybox" colspan="2">&nbsp;</td>
  </tr>

	<tr>
		<td class="bluebox2" width="8%">&nbsp;</td>
	    <td class="bluebox" width="8%"><s:text name="HouseNo"/><span class="mandatory1">*</span> : </td>
	    <td class="bluebox"><s:textfield name="houseNumber" value="%{houseNumber}" maxlength="50" onblur="validatePlotNo(this,'Plot No/House No');"/></td>
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
