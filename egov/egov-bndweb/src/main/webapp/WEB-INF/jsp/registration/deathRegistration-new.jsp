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
 <title>Death Registration -  Form No. 2 </title>
 <sx:head />
  <head>
  
    <jsp:include page='/WEB-INF/jsp/registration/deathregistration.jsp'/>  
   
  </head>
  
 <body onload="bodyOnLoad();refreshInbox();">
 <div class="errorstyle" id="deathRegistration_error" style="display: none;">
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
	<div class="formheading"/></div>
	<s:form theme="css_xhtml" action="deathRegistration" onKeyPress="return disableEnterKey(event);"  name="deathregistrationForm" onsubmit="enablingFields();" validate="true">	
	<s:token/>
	<s:push value="model">
	<div class="blueshadow"></div>
	<h1 class="subhead" ><s:text name="regform.heading.part1"/></h1>
	
   <s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}"/>
   <s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}"/>
   <s:hidden id="createdDate" name="createdDate" value="%{createdDate}"/>
   <s:hidden id="defaultState" name="defaultState" value="%{defaultState.id}"/>
   <s:hidden id="id" name="id" value="%{id}"/>
   <s:hidden id="idTemp" name="idTemp" value="%{idTemp}" />
   <s:hidden id="status" name="status" value="%{status.id}" />
   <s:hidden id="mode" name="mode" value="%{mode}"/>
   <s:hidden id="registrarId" name="registrarId" value="%{registrarId.id}"/>
   <s:hidden id ="ismainregunit" name="ismainregunit" value="%{registrationUnit.ismainregunit}"/>
   <s:hidden id="workFlowType" name="workFlowType" value="%{workFlowType}"/> 	
 <s:hidden id="registrationMode" name="registrationMode" value="%{registrationMode}"/> 	
   	
   
     	<!-- TO KEEP HISTORY OF OLD DEATH RECORD WE ARE ADDING TEMPORARY VARIBALES-->
<s:if test="%{mode=='edit' || mode=='modify'}">
     		<jsp:include page="/WEB-INF/jsp/registration/deathRegistrationHistory.jsp"/>
     	</s:if>
     	<!-- END OF DECLARATION -->
	
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	   	 	    <tr>
	 			<td class="bluebox" width="19%">&nbsp;</td>
				<td class="bluebox" width="15%"><s:text name="registration.unit"/></td>
	   			<td class="bluebox" width="20%">
	   				<s:select name="registrationUnit" id ="registrationUnit" list="dropdownData.registrationUnitList" listKey="id" listValue="regUnitDesc" headerKey="-1" headerValue="----choose----" value="%{registrationUnit.id}" disabled="true"/>
	   			</td>
	   			<td class="bluebox" width="15%"><s:text name="registration.date"/><span class="mandatory">*</span></td>
				<td class="bluebox" width="15%" >
				 	<s:date name="registrationDate" format="dd/MM/yyyy" var="registrationDateTemp" />
				 	<s:textfield id="registrationDate" name="registrationDate" value="%{registrationDateTemp}" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDelayedRegistration();"/>
					<s:if test="%{mode!='view' && mode!='notmodify' && mode!='modify' && mode!='edit' && mode!='lock' && mode!='unlock'}">
					<s:if test="%{registrationMode=='offline' || registrationUnit.ismainregunit==true}">
					<a href="javascript:show_calendar('forms[0].registrationDate');" >
						<img src="${pageContext.request.contextPath}/common/image/calendaricon.gif"	border="0" align="absmiddle"/>
					</a>
					</s:if>
					</s:if>
					
				</td>
				<td class="bluebox" width="18%">&nbsp;</td>
				<td class="bluebox" width="18%">&nbsp;</td>
	     </tr>
	          <s:if test = "%{mode!=null}">
	     <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="registration.no"/><span class="mandatory">*</span> </td>
	   			<td class="greybox">
	   				<s:textfield name="registrationNo" id="registrationNo" value="%{registrationNo}" onblur="isDeathRegNumUnique();"/>
	   				<!--<egov:uniquecheck id="deathRegNumCheck" fieldtoreset="registrationNo" fields="['Value']"
										url='common/ajaxCommon!uniqueDeathRegNumberCheck.action' />-->
	   			</td>
	   			<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
	     </tr>
	     </s:if>
	        
	
	      	    <tr>
	 			<td colspan="7">
	 				<div class="blueshadow"></div>
	 				<div align="center">
	 					<h1 class="subhead" ><s:text name="deceased.details.heading"/></h1>
	 				</div>
	 			</td>
	     </tr> 
                 
                 <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="dateOfdeath"/><span class="mandatory">*</span></td>
	   			<td class="bluebox">
	   				<s:date name="dateOfEvent" format="dd/MM/yyyy" var="dateOfEventTemp" />
				 	<s:textfield id="dateOfEvent" name="dateOfEvent" value="%{dateOfEventTemp}" onkeyup="DateFormat(this,this.value,event,false,'3')"    onblur="validateDateFormat(this);validateDelayedRegistration();validateDeathdate();isDeathRegNumUnique()" />
					 <s:if test="%{mode!='view' && mode!='edit' && mode!='notmodify' && mode!='lock' && mode!='unlock'}">
					<a href="javascript:show_calendar('forms[0].dateOfEvent');" >
						<img src="${pageContext.request.contextPath}/common/image/calendaricon.gif"	border="0" align="absmiddle"/>
					</a>
					</s:if>
	   			</td>
	   			<td class="bluebox"><s:text name="sex.lbl"/><span class="mandatory">*</span></td>
				<td class="bluebox"><s:select id="citizen.sex" name="citizen.sex" list="dropdownData.sexTypeList" listKey="code" listValue="code" headerKey="" headerValue="-----choose----" onchange="enablestaticDetails();"/></td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	    	 </tr>
	         
	         
	        	 <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="nameofdeceased.lbl"/><span class="mandatory">*</span></td>
	   			<td class="greybox">	   			
	   				<s:radio list="optionMap" value="%{nameOfdeceasedFlag}" name="nameOfdeceasedFlag" id="nameOfdeceasedFlag"   onclick="populateCitizenName(); "/>
	   				<s:hidden id="citizen.citizenID" name="citizen.citizenID" value="%{citizen.citizenID}"/>
	   			</td>
	   			<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
	     </tr>
	      <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="firstName.lbl"/><span class="mandatory">*</span><br/>
	   				<s:textfield name="citizen.firstName" id="citizen.firstName" maxlength="512" /><br/>
	   				<font color="blue"><s:text name="fornodatalbl"/></font>
	   			</td>
	   			<td class="bluebox">
	   				<s:text name="middleName.lbl"/><br>
	   				<s:textfield name="citizen.middleName" id="citizen.middleName" maxlength="512" />
	   			</td>
				<td class="bluebox">
					<s:text name="lastName.lbl"/><br>
					<s:textfield name="citizen.lastName" id="citizen.lastName" maxlength="512"/>
				</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	      
	       <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="deceased.address.lbl"/><span class="mandatory">*</span></td>
	   			<td class="greybox"><s:radio list="optionMap" value="%{deceasedAddressFlag}"	name="deceasedAddressFlag" id="deceasedAddressFlag"  onclick="populateAddress(this);" /></td>
	   			<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
	     </tr>
	         
	          <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="address"/>:</td>
	   			<td class="bluebox">
	   				<s:text name="streetAddress1"/>:<span class="mandatory">*</span>
	   			</td>
	   			<td class="bluebox" colspan="2">
	   			 <s:hidden id="deceasedAddress.addressID" name="deceasedAddress.addressID" value="%{deceasedAddress.addressID}"/>
	   				<s:textarea name="deceasedAddress.streetAddress1" id="deceasedAddress.streetAddress1" value="%{deceasedAddress.streetAddress1}" rows="1" cols="46" onblur="imposeMaxLength(this,512)"/>
	   			</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	       <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="streetAddress2"/>:
	   			</td>
	   			<td class="bluebox" colspan="2">
	   				<s:textarea name="deceasedAddress.streetAddress2" id="deceasedAddress.streetAddress2" value="%{deceasedAddress.streetAddress2}" rows="1" cols="46" onblur="imposeMaxLength(this,512)"/>
	   			</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	      <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="taluk"/><br/>
	   				<s:textfield name="deceasedAddress.taluk" id="deceasedAddress.taluk" value="%{deceasedAddress.taluk}" maxlength="512"/>
	   			</td>
	   			<td class="bluebox">
	   				<s:text name="city.town.vill.lbl"/><span class="mandatory">*</span><br>
	   				<s:textfield name="deceasedAddress.cityTownVillage" id="deceasedAddress.cityTownVillage" value="%{deceasedAddress.cityTownVillage}" maxlength="512"/>
	   			</td>
				<td class="bluebox">
					<s:text name="pincode.lbl"/><br>
					<s:textfield name="deceasedAddress.pinCode" id="deceasedAddress.pinCode" value="%{deceasedAddress.pinCode}"  maxlength="6" onblur="validateAddressPincode(this);"/>
				</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	       <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="district.lbl"/><span class="mandatory">*</span><br/>
	   				<s:textfield name="deceasedAddress.district" id="deceasedAddress.district" value="%{deceasedAddress.district}" maxlength="512"/>
	   			</td>
	   			<td class="bluebox" colspan="2">
	   				<s:text name="state.lbl"/>:<span class="mandatory">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	   				<s:select name="deceasedAddress.state" id="deceasedAddress.state" list="dropdownData.stateList" value="%{deceasedAddress.state}" listKey="id" listValue="name" headerKey="" headerValue="---choose---"/>
	   			</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
             
	         
	         
	             <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="permanentAddress"/><span class="mandatory">*</span></td>
	   			<td class="greybox"><s:radio list="optionMap" value="%{permanentAddressFlag}"	name="permanentAddressFlag" id="permanentAddressFlag"   onclick="populateAddress(this);"/></td>
	   			<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
	     </tr>
	         
                     <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="address"/>:</td>
	   			<td class="bluebox">
	   				<s:text name="streetAddress1"/>:<span class="mandatory">*</span>
	   			</td>
	   			<td class="bluebox" colspan="2">
	   			<s:hidden id="permanentCitizenAddress.addressID" name="permanentCitizenAddress.addressID" value="%{permanentCitizenAddress.addressID}"/>
	   				<s:textarea name="permanentCitizenAddress.streetAddress1" id="permanentCitizenAddress.streetAddress1" value="%{permanentCitizenAddress.streetAddress1}" rows="1" cols="46" onblur="imposeMaxLength(this,512)"/>
	   			</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	       <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="streetAddress2"/>:
	   			</td>
	   			<td class="bluebox" colspan="2">
	   				<s:textarea name="permanentCitizenAddress.streetAddress2" id="permanentCitizenAddress.streetAddress2" value="%{permanentCitizenAddress.streetAddress2}" rows="1" cols="46" onblur="imposeMaxLength(this,512)"/>
	   			</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	      <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="taluk"/><br/>
	   				<s:textfield name="permanentCitizenAddress.taluk" id="permanentCitizenAddress.taluk" value="%{permanentCitizenAddress.taluk}" maxlength="512"/>
	   			</td>
	   			<td class="bluebox">
	   				<s:text name="city.town.vill.lbl"/><span class="mandatory">*</span><br>
	   				<s:textfield name="permanentCitizenAddress.cityTownVillage" id="permanentCitizenAddress.cityTownVillage" value="%{permanentCitizenAddress.cityTownVillage}" maxlength="512"/>
	   			</td>
				<td class="bluebox">
					<s:text name="pincode.lbl"/><br>
					<s:textfield name="permanentCitizenAddress.pinCode" id="permanentCitizenAddress.pinCode" value="%{permanentCitizenAddress.pinCode}"  maxlength="6" onblur="validateAddressPincode(this);"/>
				</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	       <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="district.lbl"/><span class="mandatory">*</span><br/>
	   				<s:textfield name="permanentCitizenAddress.district" id="permanentCitizenAddress.district" value="%{permanentCitizenAddress.district}" maxlength="512"/>
	   			</td>
	   			<td class="bluebox" colspan="2">
	   				<s:text name="state.lbl"/>:<span class="mandatory">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	   				<s:select name="permanentCitizenAddress.state" id="permanentCitizenAddress.state" list="dropdownData.stateList" value="%{permanentCitizenAddress.state}" listKey="id" listValue="name" headerKey="" headerValue="---choose---"/>
	   			</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	         
	        
                <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><b><s:text name="motherdetails.lbl"/>:</b></td>
	   			<td class="greybox" colspan="4">&nbsp;</td>
	   			<td class="greybox">&nbsp;</td>
			   </tr>  
	         
	              <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="mothername.lbl"/></td>
	   			<td class="bluebox">
	   				<s:text name="firstName.lbl"/><span class="mandatory">*</span><br/>
	   				<s:textfield name="mother.firstName" id="mother.firstName" value="%{mother.firstName}" maxlength="512" onblur="changeRelationnames();"/><br/>
	   				<s:hidden name="mother.citizenID" id="mother.citizenID" value="%{mother.citizenID}"/>
	   				<font color="blue"><s:text name="fornodatalbl"/></font>
	   			</td>
	   			<td class="bluebox">
	   				<s:text name="middleName.lbl"/><br>
	   				<s:textfield name="mother.middleName" id="mother.middleName" value="%{mother.middleName}" maxlength="512"  onblur="changeRelationnames();"/>
	   			</td>
				<td class="bluebox">
					<s:text name="lastName.lbl"/><br>
					<s:textfield name="mother.lastName" id="mother.lastName" value="%{mother.lastName}" maxlength="512"  onblur="changeRelationnames();"/>
				</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	     </tr>
	     
	       <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><b><s:text name="father.husbanddetails.lbl"/>:</b></td>	   			
	   			<td class="greybox" colspan="4"></td>
	   			<td class="greybox">&nbsp;</td>
			   </tr>  
			   
			    <tr>
	 			<td class="bluebox"></td>
				<td class="bluebox"><s:text name="deceased.relation.lbl"/></td>
			   	<td class="bluebox"><s:select  id="deceasedrelationType" name="deceasedrelationType"  value="%{deceasedrelationType.id}" list="dropdownData.relationList" listKey="id" listValue="relatedAsConst" headerKey="-1" headerValue="----Choose----" onchange="clearDeceasedRelation();"/>
	           </td>
	           <td class="bluebox"></td>
	           <td class="bluebox"></td>
	           <td class="bluebox"></td>
	            <td class="bluebox"></td>
	           </tr>
	           
	         <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="relation.name.lbl"/></td>
	   			<td class="bluebox">
	   				<s:text name="firstName.lbl"/><br/>
	   				<s:textfield name="deceasedrelation.firstName" id="deceasedrelation.firstName" value="%{deceasedrelation.firstName}" maxlength="512"  onblur="changeRelationnames();"/><br/>
	   				<s:hidden name="deceasedrelation.citizenID" id="deceasedrelation.citizenID" value="%{deceasedrelation.citizenID}"  />
	   				<font color="blue"><s:text name="fornodatalbl"/></font>
	   			</td>
	   			<td class="bluebox">
	   				<s:text name="middleName.lbl"/><br>
	   				<s:textfield name="deceasedrelation.middleName" id="deceasedrelation.middleName" value="%{deceasedrelation.middleName}" maxlength="512"  onblur="changeRelationnames();"/>
	   			</td>
				<td class="bluebox">
					<s:text name="lastName.lbl"/><br>
					<s:textfield name="deceasedrelation.lastName" id="deceasedrelation.lastName" value="%{deceasedrelation.lastName}" maxlength="512"  onblur="changeRelationnames();"/>
				</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	     </tr>
	     
	     
	      
                <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><b><s:text name="age.lbl"/>:</b></td> 			
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
			   </tr>  
	     
	      <tr>
	 			<td class="bluebox"></td>
				<td class="bluebox" ><s:text name="deceased.agetype.lbl"/><span class="mandatory">*</span></td>
				<td class="bluebox"><s:select  id="ageType" name="ageType" list="dropdownData.ageTypeList" value="%{ageType.id}" listKey="id" listValue="ageTypedesc" headerKey="" headerValue="----Choose----" onchange="checkAge();" /></td>
				<td class="bluebox"><s:textfield name="age" id="age" value="%{age}" maxlength="3"/></td>			   			
				<td class="bluebox">&nbsp;</td>
	   		    <td class="bluebox">&nbsp;</td>
	   		    <td class="bluebox">&nbsp;</td>
				</td>
	         </tr> 
	         
	           <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><b><s:text name="shamshan.lbl"/>:</b></td>
	   			<td class="greybox">&nbsp;</td>
	   			<td class="greybox">&nbsp;</td>
	   			<td class="greybox">&nbsp;</td>
	   			<td class="greybox">&nbsp;</td>
	   			<td class="greybox">&nbsp;</td>
			   </tr>  
			   
			    <tr>
	 			<td class="bluebox">&nbsp;</td>
	 			<td class="bluebox" width="10%" ><s:text name="shamshan.name.lbl"/></td>
			   	<td class="bluebox" width="26%" ><s:select  id="crematorium" name="crematorium" value="%{crematorium.id}" list="dropdownData.crematoriumList" listKey="id" listValue="crematoriumconst" headerKey="-1" headerValue="----Choose----" />
	           </td>
	           <td class="bluebox">&nbsp;</td>
	           <td class="bluebox">&nbsp;</td>
	            <td class="bluebox">&nbsp;</td>
	            <td class="bluebox">&nbsp;</td>
	           </tr>
			   
			   
			     <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="place.of.death.lbl"/>:<span class="mandatory">*</span></td>
	   			<s:hidden value="%{placeType.id}" name="placeType" id="placeType" />
	   			<td class="greybox" colspan="3"><s:radio list="dropdownData.placeTypeList" value="%{placeTypeTemp}"	name="placeTypeTemp" id="placeTypeTemp" listKey="desc" listValue="desc" onclick="clearHospitalDetails();"/></td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
	           </tr>
	           
	            <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="hospital.type.lbl"/>
	   				<s:select list="dropdownData.hospitalTypeList" name="hospitalType" id="hospitalType"  listKey="id" listValue="desc" value="%{establishment.type.id}" headerKey="-1" headerValue="---------choose-------------" onchange="populateEstablishment(this);"/>
	   			    <egov:ajaxdropdown fields="['Text','Value']" url="common/ajaxCommon!getHospitalNameByType.action" id="establishment" dropdownId="establishment" />
	   			</td>
	   			<td class="bluebox" colspan="2">
	   				<s:text name="hospital.name.lbl"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	   				<s:select name="establishment" id="establishment" list="dropdownData.hospitalList" value="%{establishment.id}" listKey="id" listValue="name" headerKey="-1" headerValue="---choose---" onchange="populateEventAddress(this);"/>
	   			</td>
	   			<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">&nbsp;</td>
	      </tr>
	      
	       <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="address.place.death"/>:</td>
	   			<td class="bluebox">
	   				<s:text name="streetAddress1"/>:<span class="mandatory">*</span>
	   			</td>
	   			<td class="bluebox" colspan="2">
	   			 <s:hidden id="eventAddress.addressID" name="eventAddress.addressID" value="%{eventAddress.addressID}"/>
	   				<s:textarea name="eventAddress.streetAddress1" id="eventAddress.streetAddress1" value="%{eventAddress.streetAddress1}" rows="1" cols="46" onblur="imposeMaxLength(this,512)"/>
	   			  
	   			</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	       <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="streetAddress2"/>:
	   			</td>
	   			<td class="bluebox" colspan="2">
	   				<s:textarea name="eventAddress.streetAddress2" id="eventAddress.streetAddress2" value="%{eventAddress.streetAddress2}" rows="1" cols="46" onblur="imposeMaxLength(this,512)"/>
	   			</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	      <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="taluk"/><br/>
	   				<s:textfield name="eventAddress.taluk" id="eventAddress.taluk" value="%{eventAddress.taluk}" maxlength="512"/>
	   			</td>
	   			<td class="bluebox">
	   				<s:text name="city.town.vill.lbl"/><span class="mandatory">*</span><br>
	   				<s:textfield name="eventAddress.cityTownVillage" id="eventAddress.cityTownVillage" value="%{eventAddress.cityTownVillage}" maxlength="512"/>
	   			</td>
				<td class="bluebox">
					<s:text name="pincode.lbl"/><br>
					<s:textfield name="eventAddress.pinCode" id="eventAddress.pinCode" value="%{eventAddress.pinCode}" maxlength="6" onblur="validateAddressPincode(this);"/>
				</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	        </tr>
	       <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="district.lbl"/><span class="mandatory">*</span><br/>
	   				<s:textfield name="eventAddress.district" id="eventAddress.district" value="%{eventAddress.district}"/>
	   			</td>
	   			<td class="bluebox" colspan="2">
	   				<s:text name="state.lbl"/>:<span class="mandatory">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	   				<s:select name="eventAddress.state" id="eventAddress.state" list="dropdownData.stateList" value="%{eventAddress.state}" listKey="id" listValue="name" headerKey="" headerValue="---choose---"/>
	   			</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	      
	      
	       <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><b><s:text name="informat.details.lbl"/></b></td>
	   			<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
			   </tr>  
	        
	         <tr>
	 			<td class="bluebox"></td>
				<td class="bluebox"><s:text name="deceased.informant.lbl"/><span class="mandatory">*</span></td>
			   	<td class="bluebox"><s:select  id="relationType" name="relationType" list="dropdownData.relationTypeList" value="%{relationType.id}" listKey="id" listValue="relatedAsConst" headerKey="-1" headerValue="----Choose----" onchange="populateInformant();"/>
	           </td>
	           <td class="bluebox"></td>
	           <td class="bluebox"></td>
	           <td class="bluebox"></td>
	           <td class="bluebox"></td>
	           </tr>  
	      
	        <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="informat.name.lbl"/>:</td>
	   			<td class="bluebox">
	   				<s:text name="firstName.lbl"/><span class="mandatory">*</span><br/>
	   				<s:hidden name="informantCitizen.citizenID" id="informantCitizen.citizenID" value="%{informantCitizen.citizenID}" />
	   				<s:textfield name="informantCitizen.firstName" id="informantCitizen.firstName" value="%{informantCitizen.firstName}" maxlength="512"/><br/>
	   				<font color="blue"><s:text name="fornodatalbl"/></font>
	   			</td>
	   			<td class="bluebox">
	   				<s:text name="middleName.lbl"/><br>
	   				<s:textfield name="informantCitizen.middleName" id="informantCitizen.middleName" value="%{informantCitizen.middleName}" maxlength="512"/>
	   			</td>
				<td class="bluebox">
					<s:text name="lastName.lbl"/><br>
					<s:textfield name="informantCitizen.lastName" id="informantCitizen.lastName" value="%{informantCitizen.lastName}" maxlength="512"/>
				</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	     </tr>
	      <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="address"/>:</td>
	   			<td class="bluebox">
	   				<s:text name="streetAddress1"/>:<span class="mandatory">*</span>
	   			</td>
	   			<td class="bluebox" colspan="2">
	   				<s:textarea name="informantAddress.streetAddress1" id="informantAddress.streetAddress1" value="%{informantAddress.streetAddress1}" rows="1" cols="46" onblur="imposeMaxLength(this,512)"/>
	   			   <s:hidden name="informantAddress.addressID" id="informantAddress.addressID" value="%{informantAddress.addressID}" />
	   			</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	       <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="streetAddress2"/>:
	   			</td>
	   			<td class="bluebox" colspan="2">
	   				<s:textarea name="informantAddress.streetAddress2" id="informantAddress.streetAddress2" value="%{informantAddress.streetAddress2}" rows="1" cols="46" onblur="imposeMaxLength(this,512)"/>
	   			</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	      <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="taluk"/><br/>
	   				<s:textfield name="informantAddress.taluk" id="informantAddress.taluk" value="%{informantAddress.taluk}"/>
	   			</td>
	   			<td class="bluebox">
	   				<s:text name="city.town.vill.lbl"/><span class="mandatory">*</span><br>
	   				<s:textfield name="informantAddress.cityTownVillage" id="informantAddress.cityTownVillage" value="%{informantAddress.cityTownVillage}"/>
	   			</td>
				<td class="bluebox">
					<s:text name="pincode.lbl"/><br>
					<s:textfield name="informantAddress.pinCode" id="informantAddress.pinCode" value="%{informantAddress.pinCode}" maxlength="6" onblur="validateAddressPincode(this);"/>
				</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	       <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="district.lbl"/><span class="mandatory">*</span><br/>
	   				<s:textfield name="informantAddress.district" id="informantAddress.district" value="%{informantAddress.district}"/>
	   			</td>
	   			<td class="bluebox" colspan="2">
	   				<s:text name="state.lbl"/>:<span class="mandatory">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	   				<s:select name="informantAddress.state" id="informantAddress.state" list="dropdownData.stateList" value="%{informantAddress.state}" listKey="id" listValue="name" headerKey="" headerValue="---choose---"/>
	   			</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
			   
  </table>	
		<s:if test="%{deathStatisticsInfoFlag == 1}">
		<jsp:include page='/WEB-INF/jsp/registration/deathStatisticalInformation.jsp'/> 
		</s:if>
    
		<table  width="100%" border="0" cellspacing="0" cellpadding="0"  class="tablebottom" >
		    	        
	         <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox" width="17%">&nbsp;</td>
	   			<td class="bluebox" colspan="8">
	   				<s:text name="death.remarks"/></td>
	   				<td class="bluebox" >
	   				<s:textarea name="remarks" id="remarks" value="%{remarks}" rows="3" cols="46"/>
	   			</td>
	   			
				<td class="bluebox">&nbsp;</td>
	      </tr>
	      </table>
	     
		<s:if test="%{mode!='view' && mode!='edit' && mode!='lock' && mode!='unlock'}">
		  <div id="approverInfo">
		       <c:set var="approverHeadCSS" value="subhead" scope="request" />
		       <c:set var="approverCSS" value="bluebox" scope="request" />
				<c:import url="/commons/commonWorkflow.jsp" context="/egi" />
		  </div>
		</s:if>

				<div class="buttonbottom" align="center" id="workFlowButtons">
					<table>
						<tr>
							<s:if
								test="%{mode!='view' && mode!='modify' && mode!='notmodify' && mode!='edit' && mode!='lock' && mode!='unlock'}">
								<s:if test="%{id==null && getNextAction()=='END'}">
									<td>
										<s:submit cssClass="buttonsubmit" id="savesubmit"
											name="savesubmit" value="Save" method="create"
											onclick="return validateForm('Approve');" />
									</td>
								</s:if>
								<s:else>
									<td>
										<s:submit cssClass="buttonsubmit" id="savesubmit"
											name="savesubmit" value="Save" method="create"
											onclick="return validateForm('save');" />
									</td>
									<s:iterator value="%{getValidActions()}" var="p">
										<td>
											<s:submit type="submit" cssClass="buttonsubmit" value="%{p}"
												id="%{p}" name="%{p}" method="create"
												onclick="return validateForm('%{p}')" />
										</td>
									</s:iterator>
								</s:else>
							</s:if>
							<s:elseif test="%{mode=='modify' || mode=='notmodify'}">
								<td>
									<s:submit cssClass="buttonsubmit" id="save" name="savesubmit"
										value="Save" method="edit"
										onclick="return validateForm('save');" />
								</td>
								<s:iterator value="%{getValidActions()}" var="p">
									<td>
										<s:submit type="submit" cssClass="buttonsubmit" value="%{p}"
											id="%{p}" name="%{p}" method="edit"
											onclick="return validateForm('%{p}')" />
									</td>
								</s:iterator>
							</s:elseif>

							<s:elseif test="%{mode=='edit'}">
								<td>
									<s:submit cssClass="buttonsubmit" id="savesubmit"
										name="savesubmit" value="Save" method="edit"
										onclick="return validateForm('save');" />
								</td>
							</s:elseif>
							<s:elseif test="%{mode=='lock'}">
								<td>
									<s:submit cssClass="buttonsubmit" id="lock" name="lock" value="Lock" method="edit" />
							   </td>
							</s:elseif>
							<s:elseif test="%{mode=='unlock'}">
								<td>
									<s:submit cssClass="buttonsubmit" id="unlock" name="unlock" value="Unlock" method="edit" />
							   	</td>
							</s:elseif>
							<s:elseif test="%{mode!='view'}">
								<td>
									<s:submit cssClass="buttonsubmit" id="savesubmit"
										name="savesubmit" value="Save" method="create"
										onclick="return validateForm('save');" />
								</td>
							</s:elseif>
							<td>
								<input type="button" name="close" id="close" class="button"
									value="Close" onclick="window.close();" />
							</td>
						</tr>
					</table>
					<br>
					<div align="center">
						<font color="red"><s:text name="warning.lbl" />
						</font>
					</div>
				</div>

			</s:push>
</s:form>
</body>

</html>
