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
<%@ taglib prefix="s" uri="/struts-tags" %>  

       <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
        <tr>
	 			<td class="greybox" width="19%">&nbsp;</td>
				<td class="greybox" width="15%">&nbsp;</td>
	   			<td class="greybox" width="20%">&nbsp;</td>
	   			<td class="greybox" width="15%">&nbsp;</td>
				<td class="greybox" width="15%" >&nbsp;</td>
				<td class="greybox" width="18%">&nbsp;</td>
	    </tr>
        <tr>
	 			<td class="bluebox" width="19%">&nbsp;</td>
				<td class="bluebox" width="15%"><s:text name="childadopted.lbl"/></td>
	   			<td class="bluebox" width="20%" colspan="2">
	   				<s:checkbox id="isChildAdopted" name="isChildAdopted" value="%{isChildAdopted}" onclick="populateAdoptionDetails();"/>
	   				&nbsp;&nbsp;&nbsp;<font color="blue"><s:text name="fornodatalbl"/></font>
	   			</td>
				<td class="bluebox" width="15%" >&nbsp;</td>
				<td class="bluebox" width="18%">&nbsp;</td>
	     </tr>
        </table>
        <div id="adoptionDetails" style="display:none">
     	<h1 class="subhead" ><s:text name="adoptiondetails.lbl"/></h1>
     	<s:hidden id="adoptionDetail" name="adoptionDetail" value="%{adoptionDetail.id}"/>
     	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" >
     	<tr>
	 			<td class="bluebox" width="19%">&nbsp;</td>
				<td class="bluebox" width="15%">&nbsp;</td>
	   			<td class="bluebox" width="20%">&nbsp;</td>
	   			<td class="bluebox" width="15%">&nbsp;</td>
				<td class="bluebox" width="15%" >&nbsp;</td>
				<td class="bluebox" width="18%">&nbsp;</td>
	     </tr>
          <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><b><s:text name="adoptee.motherdetails.lbl"/>:</b></td>
	   			<td class="greybox" colspan="4">&nbsp;</td>
		 </tr>
	     <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="adoptee.mothername.lbl"/></td>
	   			<td class="bluebox">
	   				<s:text name="firstName.lbl"/><span class="mandatory">*</span><br/>
	   				<s:textfield name="adoptionDetail.adopteeMother.firstName" id="adoptionDetail.adopteeMother.firstName" value="%{adoptionDetail.adopteeMother.firstName}"/><br/>
	   			</td>
	   			<td class="bluebox">
	   				<s:text name="middleName.lbl"/><br>
	   				<s:textfield name="adoptionDetail.adopteeMother.middleName" id="adoptionDetail.adopteeMother.middleName" value="%{adoptionDetail.adopteeMother.middleName}" />
	   			</td>
				<td class="bluebox">
					<s:text name="lastName.lbl"/><br>
					<s:textfield name="adoptionDetail.adopteeMother.lastName" id="adoptionDetail.adopteeMother.lastName" value="%{adoptionDetail.adopteeMother.lastName}"/>
					<s:hidden name="adoptionDetail.adopteeMother.citizenID" id="adoptionDetail.adopteeMother.citizenID" value="%{adoptionDetail.adopteeMother.citizenID}"/>
				</td>
				<td class="bluebox">&nbsp;</td>
	     </tr>
	     <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="mobileno.lbl"/><br/>
	   				<s:textfield name="adoptionDetail.adopteeMother.mobilePhone" id="adoptionDetail.adopteeMother.mobilePhone" 
	   				    value="%{adoptionDetail.adopteeMother.mobilePhone}" onblur="checkPhoneNumberContent(this);"/>
	   			</td>
	   			<td class="bluebox">
	   				<s:text name="email.lbl"/><br>
	   				<s:textfield name="adoptionDetail.adopteeMother.emailAddress" id="adoptionDetail.adopteeMother.emailAddress" 
	   					value="%{adoptionDetail.adopteeMother.emailAddress}" onblur="validateEmail(this);"/>
	   			</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	      <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><b><s:text name="adoptee.fatherdetails.lbl"/>:</b></td>
	   			<td class="greybox" colspan="4">&nbsp;</td>
		 </tr>
	     <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="adoptee.fathername.lbl"/></td>
	   			<td class="bluebox">
	   				<s:text name="firstName.lbl"/><span class="mandatory">*</span><br/>
	   				<s:textfield name="adoptionDetail.adopteeFather.firstName" id="adoptionDetail.adopteeFather.firstName" value="%{adoptionDetail.adopteeFather.firstName}"/><br/>
	   				<s:hidden name="adoptionDetail.adopteeFather.citizenID" id="adoptionDetail.adopteeFather.citizenID" value="%{adoptionDetail.adopteeFather.citizenID}"/>
	   			</td>
	   			<td class="bluebox">
	   				<s:text name="middleName.lbl"/><br>
	   				<s:textfield name="adoptionDetail.adopteeFather.middleName" id="adoptionDetail.adopteeFather.middleName" value="%{adoptionDetail.adopteeFather.middleName}" />
	   			</td>
				<td class="bluebox">
					<s:text name="lastName.lbl"/><br>
					<s:textfield name="adoptionDetail.adopteeFather.lastName" id="adoptionDetail.adopteeFather.lastName" value="%{adoptionDetail.adopteeFather.lastName}"/>
				</td>
				<td class="bluebox">&nbsp;</td>
	     </tr>
	     <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox">
	   				<s:text name="mobileno.lbl"/><br/>
	   				<s:textfield name="adoptionDetail.adopteeFather.mobilePhone" id="adoptionDetail.adopteeFather.mobilePhone" 
	   				value="%{adoptionDetail.adopteeFather.mobilePhone}" onblur="checkPhoneNumberContent(this);"/>
	   			</td>
	   			<td class="bluebox">
	   				<s:text name="email.lbl"/><br>
	   				<s:textfield name="adoptionDetail.adopteeFather.emailAddress" id="adoptionDetail.adopteeFather.emailAddress" 
	   						value="%{adoptionDetail.adopteeFather.emailAddress}" onblur="validateEmail(this);"/>
	   			</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	      <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="adoption.deednumber.lbl"/><span class="mandatory">*</span></td>
	   			<td class="greybox">
	   				<s:textfield name="adoptionDetail.adoptionNumber" id="adoptionDetail.adoptionNumber" value="%{adoptionDetail.adoptionNumber}" />
	   			</td>
	   			<td class="greybox"><s:text name="adoption.affidavitnumber.lbl"/><span class="mandatory">*</span></td>
				<td class="greybox">
					<s:textfield name="adoptionDetail.affidavitNumber" id="adoptionDetail.affidavitNumber" value="%{adoptionDetail.affidavitNumber}"/>
				</td>
				<td class="greybox">&nbsp;</td>
	      </tr>
	      <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="dateof.addoption.lbl"/><span class="mandatory">*</span></td>
	   			<td class="bluebox">
	   				<s:date name="adoptionDetail.adoptionDate" format="dd/MM/yyyy" var="adoptionDateTemp" />
	   				<s:textfield name="adoptionDetail.adoptionDate" id="adoptionDate" value="%{adoptionDateTemp}" onkeyup="DateFormat(this,this.value,event,false,'3')" />
	   				<s:if test="%{mode!='view' && mode!='notmodify'  && mode!='modify' && mode!='edit'}">
						<a href="javascript:show_calendar('forms[0].adoptionDate');" >
							<img src="${pageContext.request.contextPath}/common/image/calendaricon.gif"	border="0" align="absmiddle"/>
						</a>
					</s:if>
	   			</td>
	   			<td class="bluebox"><s:text name="adoptionInstitute.lbl"/></td>
				<td class="bluebox">
					<s:select name="adoptionDetail.adoptionInstituteId" id="adoptionDetail.adoptionInstitute" list="dropdownData.adoptionInstitueList" 
					 listKey="id" listValue="institutionName" value="%{adoptionDetail.adoptionInstituteId}" headerKey="-1" headerValue="----Choose----"/>
				</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	      <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="courtorderno.lbl"/></td>
	   			<td class="greybox">
	   				<s:textfield name="adoptionDetail.courtOrderNumber" id="adoptionDetail.courtOrderNumber" value="%{adoptionDetail.courtOrderNumber}" />
	   			</td>
	   			<td class="greybox"><s:text name="newParentAddress.lbl"/><span class="mandatory">*</span></td>
				<td class="greybox">
				    <s:hidden id="adoptionDetail.adopteeAddress.addressID" name="adoptionDetail.adopteeAddress.addressID" value="%{adoptionDetail.adopteeAddress.addressID}"/>
					<s:textarea name="adoptionDetail.adopteeAddress.streetAddress1" id="adoptionDetail.adopteeAddress.streetAddress1" value="%{adoptionDetail.adopteeAddress.streetAddress1}" rows="3" cols="22"/>
				</td>
				<td class="greybox">&nbsp;</td>
	      </tr>
	       <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="remarks.lbl"/></td>
	   			<td class="bluebox" colspan="2">
	   				<s:textarea name="adoptionDetail.remarks" id="adoptionDetail.remarks" value="%{adoptionDetail.remarks}" rows="3" cols="30"/>
	   			</td>
	   			<td class="bluebox"></td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
     </table>
     </div>
     
     
  
