<%@ include file="/includes/taglibs.jsp" %>
  <tr>
  	<td class="greybox2">&nbsp;</td>
	<td class="greybox"><s:text name="OwnerName" /><span class="mandatory1">*</span> : </td>
    <td class="greybox">
    <table width="" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="nameTable"">
    <s:if test="propertyOwnerProxy.size()==0">
      <tr id="nameRow" >
        <td class="greybox" align="center">
        	<s:textfield name="propertyOwnerProxy[0].firstName" maxlength="512" size="20" id="ownerName"  value="%{propertyOwnerProxy[0].firstName}" 
        		onblur="trim(this,this.value);checkSpecialCharForName(this);"/>
        </td>
        <td class="greybox">
        	<img id="addOwnerBtn" name="addOwnerBtn" src="${pageContext.request.contextPath}/image/addrow.gif" onclick="javascript:addOwner(); return false;" alt="Add" width="18" height="18" border="0" />
      		<img id="removeOwnerBtn" name="removeOwnerBtn" src="${pageContext.request.contextPath}/image/removerow.gif" onclick="javascript:deleteOwner(this); return false;" alt="Remove" width="18" height="18" border="0" />
        </td>
        </tr>
     </s:if>
      <s:else>
        <s:iterator value="(propertyOwnerProxy.size).{#this}" status="ownerStatus">
			<tr id="nameRow">
        		<td class="greybox" align="center">
        			<s:textfield name="propertyOwnerProxy[%{#ownerStatus.index}].firstName" maxlength="512" size="20" id="ownerName" value="%{propertyOwnerProxy[#ownerStatus.index].firstName}" 
        				onblur="trim(this,this.value);checkSpecialCharForName(this);"/>
        		</td>
        		<td class="greybox">
        			<img id="addOwnerBtn" name="addOwnerBtn" src="${pageContext.request.contextPath}/image/addrow.gif" onclick="javascript:addOwner(); return false;" alt="Add" width="18" height="18" border="0" />
      				<img id="removeOwnerBtn" name="removeOwnerBtn" src="${pageContext.request.contextPath}/image/removerow.gif" onclick="javascript:deleteOwner(this); return false;" alt="Remove" width="18" height="18" border="0" />
        		</td>
        	</tr>
        </s:iterator>
      </s:else>
      </table>
      </td>
      <td class="greybox">&nbsp;</td>
      <td class="greybox">&nbsp;</td>
  </tr>
