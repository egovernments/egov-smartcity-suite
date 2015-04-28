#-------------------------------------------------------------------------------
# <!-- #-------------------------------------------------------------------------------
# # eGov suite of products aim to improve the internal efficiency,transparency, 
# #    accountability and the service delivery of the government  organizations.
# # 
# #     Copyright (C) <2015>  eGovernments Foundation
# # 
# #     The updated version of eGov suite of products as by eGovernments Foundation 
# #     is available at http://www.egovernments.org
# # 
# #     This program is free software: you can redistribute it and/or modify
# #     it under the terms of the GNU General Public License as published by
# #     the Free Software Foundation, either version 3 of the License, or
# #     any later version.
# # 
# #     This program is distributed in the hope that it will be useful,
# #     but WITHOUT ANY WARRANTY; without even the implied warranty of
# #     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# #     GNU General Public License for more details.
# # 
# #     You should have received a copy of the GNU General Public License
# #     along with this program. If not, see http://www.gnu.org/licenses/ or 
# #     http://www.gnu.org/licenses/gpl.html .
# # 
# #     In addition to the terms of the GPL license to be adhered to in using this
# #     program, the following additional terms are to be complied with:
# # 
# # 	1) All versions of this program, verbatim or modified must carry this 
# # 	   Legal Notice.
# # 
# # 	2) Any misrepresentation of the origin of the material is prohibited. It 
# # 	   is required that all modified versions of this material be marked in 
# # 	   reasonable ways as different from the original version.
# # 
# # 	3) This license does not grant any rights to any user of the program 
# # 	   with regards to rights under trademark law for use of the trade names 
# # 	   or trademarks of eGovernments Foundation.
# # 
# #   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
# #------------------------------------------------------------------------------- -->
#-------------------------------------------------------------------------------
<%@ include file="/includes/taglibs.jsp" %>
	<tr> 
		<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="Applicant Details"/></span></div></td>
	</tr>
	
	<tr>
		<td class="bluebox" width="13%">&nbsp;</td>
		<td class="bluebox" width="13%"   ><s:text name="applicantName" /> : <span class="mandatory" >*</span></td>
		<td class="bluebox"  style="font-weight:bold;font-size:13px" width="26%"><s:textfield id="ownerFirstname" name="owner.firstName" value="%{owner.firstName}" onblur="return validateName(this);" /></td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox" width="20%"   ><s:text name="spouseOrFatheName" /> : <span class="mandatory" >*</span></td>
		<td class="bluebox"  style="font-weight:bold;font-size:13px"><s:textfield id="spouseOrFatheName" name="owner.fatherName" value="%{owner.fatherName}" onblur="return validateName(this);" /></td>
		<s:hidden id="owner" name="owner" value="%{owner.citizenID}" /> 
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td> 
		<td class="greybox"   ><s:text name="applicantAddress1" /> : <span class="mandatory" >*</span></td>
		<td class="greybox"  style="font-weight:bold;font-size:13px"><s:textarea cols="20" rows="2" id="applicantAddress1" name="applicantrAddress.streetAddress1" value="%{applicantrAddress.streetAddress1}" maxlength='512' onblur="filterAddress(this);"/></td>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"   ><s:text name="applicantAddress2" /> : </td>
		<td class="greybox"  style="font-weight:bold;font-size:13px"><s:textarea  cols="20" rows="2" id="applicantAddress2" name="applicantrAddress.streetAddress2" value="%{applicantrAddress.streetAddress2}" maxlength='512' onblur="filterAddress(this);"/></td>
		<s:hidden id="applicantrAddress.id" name="applicantrAddress.id"  />
	</tr>
	<tr>
		<td class="bluebox">&nbsp;</td> 
		<td class="bluebox"   ><s:text name="phoneNum" /> :</td>
        <td class="bluebox"  style="font-weight:bold;font-size:13px">
               <s:property value="%{owner.homePhone}"/></td>
				<td style="display: none;">
        <s:textfield id="contactNo" name="owner.homePhone" value="%{owner.homePhone}" maxlength="10" onblur="return validateContactNumber(this);" /></td>
        <td class="bluebox">&nbsp;</td>
		<td class="bluebox"   ><s:text name="mobileNum" /> :</td>
        <td class="bluebox"  style="font-weight:bold;font-size:13px">
        <s:property value="%{mobileNumber}"/>&nbsp; <span class="mandatory" >**<s:text name="SMS is sent to this" /> </span></td>
				<td style="display: none;">
        <s:textfield id="mobileNo" name="mobileNumber" value="%{mobileNumber}" maxlength="15" onblur="return validateMobileNumber(this);"/>
      	<span class="mandatory"   style="font-weight:bold;font-size:13px"  >**<s:text name="SMS is sent to this" /> </span>
        </td>

	</tr>
	<tr>
	<td class="greybox">&nbsp;</td> 
		<td class="greybox"   ><s:text name="email" /> :</td>
        <td class="greybox"  style="font-weight:bold;font-size:13px">
          <s:property value="%{%{emailId}}"/> &nbsp;<span class="mandatory" >**<s:text name="mail is sent to this" /> </span></td>
				<td style="display: none;">
        <s:textfield id="email" name="emailId" value="%{emailId}" onblur="return validateEmail();" />
       <span class="mandatory"   style="font-weight:bold;font-size:13px" >**<s:text name="mail is sent to this" /> </span>
        </td>
        <td class="greybox">&nbsp;</td> 
        <td class="greybox">&nbsp;</td> 
<td class="greybox">&nbsp;</td> 
	</tr>

	
	
	
	
