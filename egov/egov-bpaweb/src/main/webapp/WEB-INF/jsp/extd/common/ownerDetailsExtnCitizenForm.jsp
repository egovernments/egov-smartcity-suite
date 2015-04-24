<%@ include file="/includes/taglibs.jsp" %>
	<tr> 
		<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="Applicant Details"/></span></div></td>
	</tr>
	
		
	<tr>
		<td class="bluebox" width="13%">&nbsp;</td>
		<td class="bluebox" width="13%"><s:text name="applicantName" /> : <span class="mandatory" >*</span></td>
		<s:if test="(userRole=='PORTALUSERSURVEYOR')">
		<td class="bluebox" width="26%"><s:textfield id="ownerFirstname" name="owner.firstName" value="%{owner.firstName}" onblur="return validateName(this);" onchange="checkRegnFormCheckBox();" /></td>
		</s:if>
		<s:else>
		<td class="bluebox" width="26%"><s:textfield id="ownerFirstname" name="owner.firstName" value="%{owner.firstName}" onblur="return validateName(this);"  /></td>
		</s:else>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox" width="20%"><s:text name="spouseOrFatheName" /> : <span class="mandatory" >*</span></td>
		<s:if test="(userRole=='PORTALUSERSURVEYOR')">
		<td class="bluebox"><s:textfield id="spouseOrFatheName" name="owner.fatherName" value="%{owner.fatherName}" onblur="return validateName(this);" onchange="checkRegnFormCheckBox();"/></td>
		
		</s:if>
		<s:else>
		<td class="bluebox"><s:textfield id="spouseOrFatheName" name="owner.fatherName" value="%{owner.fatherName}" onblur="return validateName(this);" /></td>
		
		</s:else>
		<s:hidden id="owner" name="owner" value="%{owner.citizenID}" /> 
	</tr>
	
	 
	<tr>
		<td class="greybox">&nbsp;</td> 
		<td class="greybox"><s:text name="applicantAddress.lbl" /> : <span class="mandatory" >*</span></td>
		<s:if test="(userRole=='PORTALUSERSURVEYOR')">
		<td class="greybox"><s:textarea cols="20" rows="2" id="applicantAddress1" name="applicantrAddress.streetAddress1" value="%{applicantrAddress.streetAddress1}" maxlength='512' onblur="filterAddress(this);" onchange="checkRegnFormCheckBox();"/></td>
		</s:if>
		<s:else>
		<td class="greybox"><s:textarea cols="20" rows="2" id="applicantAddress1" name="applicantrAddress.streetAddress1" value="%{applicantrAddress.streetAddress1}" maxlength='512' onblur="filterAddress(this);" /></td>
		</s:else>
		<td class="greybox">&nbsp;</td>
		
		<td class="greybox" style="display: none"><s:text name="applicantAddress2" /> : </td>
		<td class="greybox" style="display: none"><s:textarea  cols="20" rows="2" id="applicantAddress2" name="applicantrAddress.streetAddress2" value="%{applicantrAddress.streetAddress2}" maxlength='512' onblur="filterAddress(this);"/></td>
		<s:hidden id="applicantrAddress.id" name="applicantrAddress.id"  />
	</tr>
	 
	<tr>
		<td class="bluebox">&nbsp;</td> 
		<td class="bluebox"><s:text name="mobileNum" /> :</td> 
        <td class="bluebox"><s:property value="mobileNumber" />
       	<span class="mandatory" >**<s:text name="SMS is sent to this" /> </span> 
        </td>
		<td class="bluebox">&nbsp;</td> 
		<td class="bluebox"><s:text name="email" /> :</td>
        <td class="bluebox"><s:property value="emailId" />
       <span class="mandatory" >**<s:text name="mail is sent to this" /> </span>
        </td>
	</tr>
 