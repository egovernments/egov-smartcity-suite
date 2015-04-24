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

	
	
	
	