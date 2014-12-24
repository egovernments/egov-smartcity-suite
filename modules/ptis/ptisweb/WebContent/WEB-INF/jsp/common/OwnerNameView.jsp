<%@ include file="/includes/taglibs.jsp" %>
  <tr>
  	<td class="greybox2" width="5%">&nbsp;</td>
	<td class="greybox" width="20%"><s:text name="OwnerName" /> : </td>
    <td class="greybox" width="40%">
    <table width="40%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="nameTable">
    <s:iterator value="propOwnerProxy" status="ownerStatus">
			<tr id="nameRow">
        		<td class="greybox" align="center" nowrap="true">
        			<span class="bold"><s:property value="%{firstName}" /></span>
        		</td>
        		<td class="greybox" nowrap="true">
        		</td>
        	</tr>
     </s:iterator>
      </table>
      </td>
      <td class="greybox" width="20%">&nbsp;</td>
      <td class="greybox" width="25%">&nbsp;</td>
  </tr>
