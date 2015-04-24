<%@ include file="/includes/taglibs.jsp" %>
<s:if test="%{mode=='noEdit' || mode=='view' }">
	<tr id="existingApplDtls0">
		<td class="greybox" width="13%">&nbsp;</td>
        <td class="greybox" width="13%"><s:text name="existingPPANum.txt" /> :</td>
        <td class="greybox" ><s:textfield name="existingPPANum" id="existingPPANum" value="%{existingPPANum}"   /></td>
      	<td class="greybox">&nbsp;</td>
        <td class="greybox"><s:text name="existingBANum.txt" /> :</td>
        <td class="greybox"><s:textfield name="existingBANum" id="existingBANum" value="%{existingBANum}"  /></td>
   </tr>
   </s:if>
   <s:else>
   <tr id="existingApplDtls0">
		<td class="greybox" width="13%">&nbsp;</td>
        <td class="greybox" width="13%"><s:text name="existingPPANum.txt" /> :</td>
        <td class="greybox" ><s:textfield name="existingPPANum" id="existingPPANum" value="%{existingPPANum}" onchange="checkValidPpaNumber()"/></td>
      	<td class="greybox">&nbsp;</td>
        <td class="greybox"><s:text name="existingBANum.txt" /> :</td>
        <td class="greybox"><s:textfield name="existingBANum" id="existingBANum" value="%{existingBANum}" onchange="checkValidBaNumber()"/></td>
   </tr>
   </s:else>
   