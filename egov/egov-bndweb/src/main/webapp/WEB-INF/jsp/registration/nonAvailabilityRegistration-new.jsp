#-------------------------------------------------------------------------------
# /*******************************************************************************
#  *    eGov suite of products aim to improve the internal efficiency,transparency,
#  *    accountability and the service delivery of the government  organizations.
#  *
#  *     Copyright (C) <2015>  eGovernments Foundation
#  *
#  *     The updated version of eGov suite of products as by eGovernments Foundation
#  *     is available at http://www.egovernments.org
#  *
#  *     This program is free software: you can redistribute it and/or modify
#  *     it under the terms of the GNU General Public License as published by
#  *     the Free Software Foundation, either version 3 of the License, or
#  *     any later version.
#  *
#  *     This program is distributed in the hope that it will be useful,
#  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
#  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  *     GNU General Public License for more details.
#  *
#  *     You should have received a copy of the GNU General Public License
#  *     along with this program. If not, see http://www.gnu.org/licenses/ or
#  *     http://www.gnu.org/licenses/gpl.html .
#  *
#  *     In addition to the terms of the GPL license to be adhered to in using this
#  *     program, the following additional terms are to be complied with:
#  *
#  * 	1) All versions of this program, verbatim or modified must carry this
#  * 	   Legal Notice.
#  *
#  * 	2) Any misrepresentation of the origin of the material is prohibited. It
#  * 	   is required that all modified versions of this material be marked in
#  * 	   reasonable ways as different from the original version.
#  *
#  * 	3) This license does not grant any rights to any user of the program
#  * 	   with regards to rights under trademark law for use of the trade names
#  * 	   or trademarks of eGovernments Foundation.
#  *
#  *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#  ******************************************************************************/
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="sx" uri="/WEB-INF/struts-dojo-tags.tld"%> 
<%@ include file="/includes/taglibs.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
 <title>Non Availability (NA) Form </title>
 <sx:head />
 <div class="formheading"/></div>
 
  <head>
  
    <jsp:include page='/WEB-INF/jsp/registration/nonAvailabilityRegistration.jsp'/>  
   
  </head>
  
 <body onload="bodyonload();">
 <div class="errorstyle" id="nonavailableform_error" style="display: none;">
</div>
<div class="errorstyle" style="display:none" id="deathRegNumCheck">
			<s:text name="registration.number.exists"/>
</div>
  
	<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="fieldError">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>

	<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
				<s:actionmessage />
		</div>
	</s:if>
	
	<s:form theme="css_xhtml" action="nonAvailabilityRegistration" onkeypress="return disableEnterKey(event);" name="nonavailabilityregistrationForm" onsubmit="" validate="true">	
	<s:token/>
	<s:push value="model">
	<div class="blueshadow"></div>

	
   <s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}"/>
   <s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}"/>
   <s:hidden id="createdDate" name="createdDate" value="%{createdDate}"/>
   <s:hidden id="id" name="id" value="%{id}"/>
   <s:hidden id="idTemp" name="idTemp" value="%{idTemp}" />
   <s:hidden id="status" name="status" value="%{status.id}" />
   <s:hidden id="mode" name="mode" value="%{mode}"/>
   <s:hidden name="test" id="test" value="%{birtDeathFlag}"/><br/>
   
     
	
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
    
    	<div align="center"><font color="red"><s:text name="fornodatalbl"/></font></div>
     <s:if test = "%{mode!=null}">
    <tr> 
	<td class="bluebox">&nbsp;</td>	
	<td class="bluebox"><s:text name="registration.no"/><span class="mandatory">*</span> </td>
	   			<td class="bluebox">
	   				<s:textfield name="registrationNo" id="registrationNo" value="%{registrationNo}"/>
	   			</td>
	<td class="bluebox">&nbsp;</td>
	<td class="bluebox">&nbsp;</td>
    </tr>
   </s:if>

	      <tr>
	            
	 			<td class="greybox">&nbsp;</td>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="nameofform.lbl"/><span class="mandatory">*</span></td>
	   			<td class="greybox">
	   				<s:radio list="formMap" name="birtDeathFlag" id="birtDeathFlag" value="%{birtDeathFlag}"  onclick=""/>
	   				
	   			</td>
	   			<td class="greybox">&nbsp;</td>
				
				
	     </tr>	     
	       <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="applicant.name.lbl"/><span class="mandatory">*</span></td>
	   			<td class="bluebox">
	   				<s:textfield name="applicantName" id="applicantName" value="%{applicantName}" onblur="checkSpecialCharacters(this)"/>
	   			
	   			</td>
	   			<td class="bluebox">
	   				<s:select  id="applicantRelationType" name="applicantRelationType"  value="%{applicantRelationType.id}" list="dropdownData.relationList" listKey="id" listValue="relatedAsConst"  onchange=""/>	   				
	   			    <s:text name="of"/><span class="mandatory">*</span>
	   			</td>
				<td class="bluebox">				
					<s:textfield name="applicantRelationName" id="applicantRelationName" value="%{applicantRelationName}" onblur="checkSpecialCharacters(this)"/>
				</td>
				
	     </tr>
	     
	           <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="citizen.name.lbl"/><span class="mandatory">*</span></td>
	   			<td class="greybox">
	   				<s:textfield name="citizenName" id="citizenName" value="%{citizenName}" onblur="checkSpecialCharacters(this)"/>
	   			
	   			</td>
	   			<td class="greybox">
	   				<s:select  id="citizenRelationType" name="citizenRelationType"  value="%{citizenRelationType.id}" list="dropdownData.relationList" listKey="id" listValue="relatedAsConst"  onchange=""/>
	   					<s:text name="of"/><span class="mandatory">*</span>
	   			</td>
				<td class="greybox">
					
					<s:textfield name="citizenRelationName" id="citizenRelationName" value="%{citizenRelationName}"  onblur="checkSpecialCharacters(this)"/>
				</td>
				
	     </tr>
	     
	         
	         <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="birth.death.lbl"/></td>
	   			<td class="bluebox">
	   				<s:text name="taluk.lbl"/><br/>
	   				<s:textfield name="talukName" id="talukName" value="%{talukName}" readonly="true"/><br/>	   				
	   			</td>
	   			<td class="bluebox">
	   				<s:text name="district.name.lbl"/><br>
	   				<s:textfield name="districtName" id="districtName" value="%{districtName}" readonly="true"/>
	   			</td>
				<td class="bluebox">
					<s:text name="state.name.lbl"/><br>
					<s:textfield name="stateName" id="stateName" value="%{stateName}" readonly="true"/>
				</td>
				
	     </tr>
	     
	     
	      <tr>
	 			<td class="greybox">&nbsp;</td>
	   			<td class="greybox">
	   				<s:text name="birth.death.year"/><span class="mandatory">*</span></td>
	   			<td class="greybox">
	   				
	   				<s:textfield name="yearOfEvent" id="yearOfEvent" value="%{yearOfEvent}" onblur="validateyear(this)"/><br/>
	   			</td>
	   			<td class="greybox">
	   				<s:text name="copies.lbl"/><span class="mandatory">*</span>
	   				<s:textfield name="no_Of_copies" id="no_Of_copies" value="%{no_Of_copies}" onblur="validatenoofcopy(this)"/>
	   			</td>
				<td class="greybox">
					<s:text name="cert.cost.lbl"/><span class="mandatory">*</span>
					<s:textfield name="cost" id="cost" value="%{cost}" onblur="checkdecimalval(this)"  readonly="true"/>
				</td>
				
	     </tr>
	
	    <tr>
	 			<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="total.fee"/><span class="mandatory">*</span></td>
	   			<td class="bluebox">
	   				
	   				<s:textfield name="totalFee" id="totalFee" value="%{totalFee}"  readonly="true"/><br/>
	   			</td>
	   			<td class="bluebox" >
	   				<s:text name="death.remarks"/></td>
	   				<td class="bluebox" >
	   				<s:textarea name="remarks" id="remarks" value="%{remarks}" rows="5" cols="40"/>
	   			</td>
	   			
			
	     </tr>
			   
  </table>	
				    	
			<div class="buttonbottom" align="center">
			<table>
					<tr>
					    <s:if test="%{mode!='view'&&mode!='edit'}">
					      <td>
								<s:submit cssClass="buttonsubmit" id="savesubmit" name="savesubmit" value="Save" method="create" onclick="return validateForm('save');" />
						 </td>
						 </s:if>
						 
						  <s:if test="%{mode=='edit'}">
					      <td>
								<s:submit cssClass="buttonsubmit" id="savesubmit" name="savesubmit" value="Save" method="edit" onclick="return validateForm('save');" />
						 </td>
						 </s:if>
						 
						 <td>
								<input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();" />
						 </td>
					</tr>
			</table><br>
			<div align="center"><font color="red"><s:text name="warning.lbl"/></font></div>
		</div>
	
	
	</s:push>
	</s:form>
  </body>
  
</html>
