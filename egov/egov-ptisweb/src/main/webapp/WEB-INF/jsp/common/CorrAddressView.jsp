<%@ include file="/includes/taglibs.jsp" %>
<script>
</script>
<div id="CorrAddressDiv">
    <tr>
      <td colspan="5" width="5%"><div class="headingsmallbg"><span class="bold"><s:text name="CorrAddr"/></span></div></td>
    </tr>
    
    <tr>
      <td class="bluebox2" width="5%">&nbsp;</td>
      <td class="bluebox" width="10%"><s:text name="Address1"/>:</td>
      <td class="bluebox" width="15%">
        <span class="bold"><s:property default="N/A" value="%{corrAddress.streetAddress1}" /> </span>
      </td>
      <td class="bluebox" colspan="2" width="20%">&nbsp;</td>
    </tr>

    <tr>
      <td class="greybox2" width="6%">&nbsp;</td>
      <td class="greybox" width="10%"><s:text name="Address2"/>:</td>
      <td class="greybox" width="8%"><span class="bold"><s:property default="N/A" value="%{corrAddress.streetAddress2}" /> </span></td>
      <td class="greybox" width="8%"><s:text name="PinCode"/>:</td>
      <td class="greybox" width="15%"><span class="bold"><s:property default="N/A" value="%{corrAddress.pinCode}" /> </span></td>
    </tr>
</div>