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
<html>
<head>
<title> 
<s:text name="create.adoption.institute" />
</title>
<sx:head/>
<script type="text/javascript">
 function validateSpecialCharacter(obj,fieldname)
  {
	
	     if(!checkSpecialCharacters(obj))
	     {
	    
	       dom.get("adoptioninstitute_error").style.display = '';
		   document.getElementById("adoptioninstitute_error").innerHTML = '<s:text name="invalid.data" />'+fieldname;
	       obj.value="";
	        return false;
	       
	     }
	    
		return true;
	 
	 } 
 function init()
 {
		  if(document.getElementById('mode').value=='view')
		  {
		  for(var i=0;i<document.forms[0].length;i++)
		  {
		  if( document.forms[0].elements[i].name!='close')
		  document.forms[0].elements[i].disabled =true;
		  }
		  }
 }
 function checkAdoptionInstCodeForSpecialCharacters(obj,fieldname)
	{
		var iChars = "!@$%^*+=[']\\';{}|\"<>?#&()";
		for (var i = 0; i < obj.value.length; i++)
		{
			if (iChars.indexOf(obj.value.charAt(i)) != -1)
			{
			  dom.get("adoptioninstitute_error").style.display = '';
		      document.getElementById("adoptioninstitute_error").innerHTML = '<s:text name="invalid.data" />'+fieldname;
	          obj.value="";
				return false;
			}
		}
		return true;
	}
	  
	 function isAdoptionInstituteCodeUnique()
	 {
	
	   var institutionCode = trimAll(document.getElementById('institutionCode').value);
	   if(institutionCode!="")
	   {
	    dom.get("adoptioninstitute_error").style.display='none';
	    populateadoptionInstitutecodeCheck({institutionCode:institutionCode});
	   } 
	 } 
	 
	 function isAdoptionInstituteNameUnique()
	 {
	
	   var institutionName = trimAll(document.getElementById('institutionName').value);
	   if(institutionName!="")
	   {
	    dom.get("adoptioninstitute_error").style.display='none';
	    populateadoptionInstitutenameCheck({institutionName:institutionName});
	   } 
	 } 
	  </script> 
</head>

<body onload="init();">
 <div class="errorstyle" id="adoptioninstitute_error" style="display: none;">
	</div>
	<div class="errorstyle" style="display:none" id="adoptionInstitutecodeCheck">
			<s:text name="adoption.institutionCode.alreadyExist"/>
	</div>
	<div class="errorstyle" style="display:none" id="adoptionInstitutenameCheck">
			<s:text name="adoption.institutionName.alreadyExist"/>
	</div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />	
			<s:fielderror />
		</div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
			<s:actionmessage />
		</div>
	</s:if>
<s:form action="adoptionInstitute" theme="css_xhtml" name="adoptionInstitute" validate="true">
<s:token/>
			 <s:hidden id="mode" name="mode" value="%{mode}"></s:hidden>
			<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<tr>
				 <td class="bluebox" width="35%" >
						&nbsp;
					</td>
					
					<td class="bluebox" width="15%" >
						&nbsp;
					</td>
					<td class="bluebox" width="50%" >
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="greybox" width="35%">
						&nbsp;
					</td>
					<td class="greybox" width="15%">
						<s:text name="adoption.institute.code" />
						<span class="mandatory">*</span>
					</td>
					<td class="greybox">
						<s:textfield id="institutionCode" name="institutionCode"
							value="%{institutionCode}" maxlength="31" onblur="checkAdoptionInstCodeForSpecialCharacters(this,'%{getText('adoption.institute.code')}'); isAdoptionInstituteCodeUnique();" />
						<egov:uniquecheck id="adoptionInstitutecodeCheck" fieldtoreset="institutionCode" fields="['Value']"
										url='common/ajaxCommon!uniqueAdoptionInstitutecodeCheck.action' />	
					</td>
				</tr>
				
				<tr>
					<td class="bluebox" width="35%">
						&nbsp;
					</td>
					<td class="bluebox" width="15%">
						<s:text name="adoption.institute.name" />
						<span class="mandatory">*</span>
					</td>
					<td class="bluebox">
						<s:textfield id="institutionName" name="institutionName"
							value="%{institutionName}" maxlength="250" onblur="validateSpecialCharacter(this,'%{getText('adoption.institute.name')}'); isAdoptionInstituteNameUnique();" />
							<egov:uniquecheck id="adoptionInstitutenameCheck" fieldtoreset="institutionName" fields="['Value']"
										url='common/ajaxCommon!uniqueAdoptionInstitutenameCheck.action' />	
					</td>
				</tr>
			</table>
			
			<div class="buttonbottom" align="center">


				<table>
					<tr>
                    <s:if test="%{mode!='view'}">
						<td>
							<s:submit cssClass="buttonsubmit" id="create" name="create"
								value="Create"  method="create"   />
						</td>

					</s:if>
					<td> <input type="button" class="button" id="close" name="close" value="Close" onclick="window.close();" ></td>
					</tr>
				</table>
			</div>




			<br>
			<div align="center">
				<font color="red"><s:text name="warning.lbl" /> </font>
			</div>
			</div>
</s:form>				
</body onload="init();">
</html>
