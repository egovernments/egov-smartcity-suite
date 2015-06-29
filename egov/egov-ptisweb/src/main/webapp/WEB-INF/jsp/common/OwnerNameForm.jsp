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
  	<%-- <td class="greybox2">&nbsp;</td>
	<td class="greybox"><s:text name="OwnerName" /><span class="mandatory1">*</span> : </td>
    <td class="greybox"> --%>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="nameTable" >
    <tr>
    <th class="bluebgheadtd"><s:text name="adharno"/><span class="mandatory1">*</span></th>
    <th class="bluebgheadtd"><s:text name="OwnerName"/><span class="mandatory1">*</span></th>
	<th class="bluebgheadtd"><s:text name="MobileNumber" /> (without +91)</th>
	<th class="bluebgheadtd"><s:text name="EmailAddress"/></th>
	<th class="bluebgheadtd"><s:text name="Add/Delete" /></th>
    </tr>
    <s:if test="%{propertyOwnerInfo.isEmpty()}">
      <tr id="nameRow" >
       <!-- <td class="greybox" width="10%"> </td>  -->
      <%-- <td class="" align="center">
        	<s:textfield name="title" maxlength="512" size="20" id="title"  value="%{propertyOwnerInfo[0].owner.name}" 
        		onblur="trim(this,this.value);checkSpecialCharForName(this);"/>
        </td> --%>
        <td class="blueborderfortd" align="center">
		   <s:textfield name="propertyOwnerInfo[0].owner.aadhaarNumber" id="propertyOwnerInfo[0].owner.aadhaarNumber" size="12" maxlength="12"></s:textfield>
		</td>
        <td class="blueborderfortd" align="center">
        	<s:textfield name="propertyOwnerInfo[0].owner.name" maxlength="512" size="20" id="ownerName"  value="%{propertyOwnerInfo[0].owner.name}" 
        		onblur="trim(this,this.value);checkSpecialCharForName(this);"/>
        </td>
        <td class="blueborderfortd" align="center">
        	<s:textfield name="propertyOwnerInfo[0].owner.mobileNumber" maxlength="10" size="20" id="mobileNumber"  value="%{propertyOwnerInfo[0].owner.mobileNumber}" 
        		onblur="validNumber(this);checkZero(this,'Mobile Number');"/>
        </td>
        <td class="blueborderfortd" align="center">
        	<s:textfield name="propertyOwnerInfo[0].owner.emailId" maxlength="64" size="20" id="emailId"  value="%{propertyOwnerInfo[0].owner.emailId}" 
        		onblur="trim(this,this.value);validateEmail(this);"/>
        </td>
        
        <td class="blueborderfortd">
        	<img id="addOwnerBtn" name="addOwnerBtn" src="${pageContext.request.contextPath}/resources/image/addrow.gif" onclick="javascript:addOwner(); return false;" alt="Add" width="18" height="18" border="0" />
      		<img id="removeOwnerBtn" name="removeOwnerBtn" src="${pageContext.request.contextPath}/resources/image/removerow.gif" onclick="javascript:deleteOwner(this); return false;" alt="Remove" width="18" height="18" border="0" />
        </td>
        </tr>
     </s:if>
      <s:else>
        <s:iterator value="propertyOwnerInfo" status="ownerStatus">
			<tr id="nameRow">
			  <td class="blueborderfortd" align="center">
			  <s:textfield name="propertyOwnerInfo[%{#ownerStatus.index}].owner.aadhaarNumber" id="propertyOwnerInfo[%{#ownerStatus.index}].owner.aadhaarNumber" size="12" maxlength="12"></s:textfield>
			  </td>
        		<td class="blueborderfortd" align="center">
        			<s:textfield name="propertyOwnerInfo[%{#ownerStatus.index}].owner.name" maxlength="512" size="20" id="ownerName" value="%{propertyOwnerInfo[#ownerStatus.index].owner.name}" 
        				onblur="trim(this,this.value);checkSpecialCharForName(this);"/>
        		</td>
        		<td class="blueborderfortd" align="center">
        			<s:textfield name="propertyOwnerInfo[%{#ownerStatus.index}].owner.mobileNumber" maxlength="10" size="20" id="mobileNo" value="%{propertyOwnerInfo[#ownerStatus.index].owner.mobileNumber}" 
        				onblur="validNumber(this);checkZero(this,'Mobile Number');"/>
        		</td>
        		<td class="blueborderfortd" align="center">
        			<s:textfield name="propertyOwnerInfo[%{#ownerStatus.index}].owner.emailId" maxlength="64" size="20" id="emailId" value="%{propertyOwnerInfo[#ownerStatus.index].owner.emailId}" 
        				onblur="trim(this,this.value);validateEmail(this);"/>
        		</td>
        		<td class="blueborderfortd">
        			<img id="addOwnerBtn" name="addOwnerBtn" src="${pageContext.request.contextPath}/resources/image/addrow.gif" onclick="javascript:addOwner(); return false;" alt="Add" width="18" height="18" border="0" />
      				<img id="removeOwnerBtn" name="removeOwnerBtn" src="${pageContext.request.contextPath}/resources/image/removerow.gif" onclick="javascript:deleteOwner(this); return false;" alt="Remove" width="18" height="18" border="0" />
        		</td>
        	</tr>
        </s:iterator>
      </s:else>
      </table>
