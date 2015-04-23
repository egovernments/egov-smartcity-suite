<%@ include file="/includes/taglibs.jsp" %>
<div id="PropAddressDiv">
	<tr>
		<td colspan="5"><div class="headingsmallbg"><span class="bold"><s:text name="PropertyAddress"/></span></div></td>
	</tr>
	
  <tr>
  	<td class="greybox2">&nbsp;</td>
  	<td class="greybox"><s:text name="Area"/> : </td>
	<td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProp.address.block}" /> </span>
	</td>
    <td class="greybox" colspan="2">&nbsp;</td>
  </tr>

	<tr>
		<td class="bluebox2" width="8%">&nbsp;</td>
	    <td class="bluebox" width="8%"><s:text name="HouseNo"/> : </td>
	    <td class="bluebox"><span class="bold"><s:property default="N/A" value="%{basicProp.address.houseNo}" /> </span></td>
	    <td class="bluebox" width="10%"><s:text name="OldNo"/> : </td>
	    <td class="bluebox"><span class="bold"><s:property default="N/A" value="%{basicProp.address.doorNumOld}" /> </span></td>
	</tr>
	<tr>
		<td class="greybox2" width="8%">&nbsp;</td>
	    <td class="greybox" width="8%"><s:text name="Address"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProp.address.streetAddress1}" /> </span></td>
	    <td class="greybox" width="10%"><s:text name="PinCode"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProp.address.pinCode}" /> </span></td>
	</tr>
	<tr>
		<td class="bluebox2" width="8%">&nbsp;</td>
	    <td class="bluebox" width="8%"><s:text name="address.khasraNumber"/> : </td>
	    <td class="bluebox"><span class="bold"><s:property default="N/A" value="%{basicProp.address.extraField1}" /> </span></td>
	    <td class="bluebox" width="10%"><s:text name="address.Mauza"/> : </td>
	    <td class="bluebox"><span class="bold"><s:property default="N/A" value="%{basicProp.address.extraField2}" /> </span></td>
	</tr>
	
	<tr>
		<td class="greybox2" width="8%">&nbsp;</td>
	    <td class="greybox" width="8%"><s:text name="address.citySurveyNumber"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProp.address.extraField3}" /> </span></td>
	    <td class="greybox" width="10%"><s:text name="address.sheetNumber"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProp.address.extraField4}" /> </span></td>
	</tr>
</div>
