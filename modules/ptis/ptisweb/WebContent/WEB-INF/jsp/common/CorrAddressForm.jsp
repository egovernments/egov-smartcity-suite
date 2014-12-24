<%@ include file="/includes/taglibs.jsp" %>
<div id="CorrAddressDiv">
	<tr>
      <td class="bluebox2" width="8%">&nbsp;</td>
      <td class="bluebox" colspan="3"><span class="bold"><s:text name="CorrAddrConfirm"/></span>
     	<s:checkbox name="chkIsCorrIsDiff" id="chkIsCorrIsDiff" onclick="enableCorresAddr();"/> <!--  have to check this -->
   	  </td>
      <td class="bluebox" width="20%">&nbsp;</td>
    </tr>
    <tr id="corrAddrHdr">
      <td colspan="5" width="5%"><div class="headingsmallbg"><span class="bold"><s:text name="CorrAddr"/></span></div></td>
    </tr>
    
    <tr id="add1Row">
      <td class="bluebox2" width="5%">&nbsp;</td>
      <td class="bluebox" width="10%"><s:text name="Address1"/>:</td>
      <td class="bluebox" width="15%">
        <s:textfield name="corrAddress1" id="corrAddress1" maxlength="512" onblur="trim(this,this.value);checkZero(this,'Corr Address1');validateAddress(this);"/>
      </td>
      <td class="bluebox" colspan="2" width="20%">&nbsp;</td>
    </tr>

    <tr id="add2Row">
      <td class="greybox2" width="6%">&nbsp;</td>
      <td class="greybox" width="10%"><s:text name="Address2"/>:</td>
      <td class="greybox" width="8%"><s:textfield name="corrAddress2" id="corrAddress2" maxlength="512" onblur="trim(this,this.value);checkZero(this,'Corr Address2');validateAddress(this);"/></td>
      <td class="greybox" width="8%"><s:text name="PinCode"/>:</td>
      <td class="greybox" width="15%"><s:textfield name="corrPinCode" id="corrPinCode" maxlength="6" styleId = "CorrPinCode" onchange="trim(this,this.value);" onblur = "validNumber(this);checkZero(this);"  value="%{corrPinCode}"/></td>
    </tr>
</div>