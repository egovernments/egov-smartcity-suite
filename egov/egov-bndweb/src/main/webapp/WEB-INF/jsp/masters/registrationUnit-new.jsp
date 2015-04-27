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
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="sx" uri="/WEB-INF/struts-dojo-tags.tld" %>

<html>
	<head>
		<title>
			<s:if test="%{mode!='view'}">
				<s:text name="create.registrationunit"/>
			</s:if>
			<s:else>
		    	<s:text name="view.registrationunit"></s:text> 
		    </s:else>
	   </title>
		
		<sx:head/>
		
		
	<!--	<jsp:include page='/WEB-INF/jsp/masters/master.jsp' /> -->
				
		<SCRIPT type="text/javascript" type="text/javascript">

		 function checkIsmainRegUnit(ismainObj)
		 {
			 
			   if(ismainObj!=null){
                   if(ismainObj.value=='1'){
                           document.getElementById('ismainregunit').value=true;
                   }
                   else if(ismainObj.value=='0'){
                           document.getElementById('ismainregunit').value=false;
                   }
               }
	         
		 }
	
 /*   function validateForm() 
	{
		var isError = false;
		if (populate("regUnitDesc").value == "") {
			alert('<s:text name="registration.desc.required"/>');
			return false;
		}
		if (populate("regUnitConst").value == "") {
			
			alert('<s:text name="lcn.number.required"/>');
			return false;
		}
		if (!validateAddress("regUnitAddress"))
			return false;
			
		if(!validateForm_registrationUnit()){
	  	   isError = true;
		}
		if(isError)
	  	    return false;
		else
			return true;
	}
*/
	
	
 
	function populateTalukName() {
	 	populate("regUnitAddress.district").value = populate('districtName').options[populate('districtName').selectedIndex].text;
		populateregUnitAddressTaluk({districtId : populate('districtName').value});
		return true;
	}
	
	function populateCityName()
	{
	   populate("regUnitAddress.taluk").value = populate('regUnitAddressTaluk').options[populate('regUnitAddressTaluk').selectedIndex].text;
	   populateregUnitAddressCityTownVillage({talukId : populate('regUnitAddressTaluk').value});
	   return true;
	}
	
	function populateCityNameLbl()
	{
	     populate("regUnitAddress.cityTownVillage").value = populate('regUnitAddressCityTownVillage').options[populate('regUnitAddressCityTownVillage').selectedIndex].text;
	}
	
	
	 function enableSate()
	 {
	 	var isError = false;
			
		if(!validateForm_registrationUnit()){
	  	   isError = true;
		}
		if(isError)
	  	    return false;
		else{
			populate('regUnitAddress.state').disabled=false; 
			return true;
		}

	 }
	  
	   
    
	  
	  function init(){
		
		populate('regUnitAddress.state').disabled=true;
		  var mode= document.getElementById('mode').value;
		  
		  if(mode=='view')
		  {
		  for(var i=0;i<document.forms[0].length;i++)
		  {
		  if( document.forms[0].elements[i].name!='close')
		  document.forms[0].elements[i].disabled =true;
		  }
		  }
		 
}

function validateAddressPincode(obj)
  {
	  
        if(trimAll(obj.value)!=""){
        	if(isNaN(obj.value))
			{				
				dom.get("registrationUnit_error").style.display = '';
		      	document.getElementById("registrationUnit_error").innerHTML = '<s:text name="invalid.pincode" />';
  		      	obj.value="";
		  		return false;
			}
  			else if(!validatePincode(obj)){
  			
  			  dom.get("registrationUnit_error").style.display = '';
		      document.getElementById("registrationUnit_error").innerHTML = '<s:text name="invalid.pincode" />';
  		      obj.value="";
		  		return false;
  			}
  		}
  		return true;
  }
  
   function populate(objName)
  {
     return document.getElementById(objName);
  }
  function validateSpecialCharacter(obj,fieldName)
  {
	
	     if(!checkSpecialCharacters(obj))
	     {
	       dom.get("registrationUnit_error").style.display = '';
		   document.getElementById("registrationUnit_error").innerHTML = '<s:text name="invalid.data" />'+fieldName;
	       obj.value="";
	        return false;
	       
	     }
	    
		return true;
	    
	  
	  }  
  function isRegUnitDescUnique()
  {
	  var regUnitDesc = trimAll(document.getElementById('regUnitDesc').value);
	   if(regUnitDesc!="")
	   {
	  
	    dom.get("registrationUnit_error").style.display='none';
	    populateregUnitDescCheck({regUnitDesc:regUnitDesc});
	   } 
  } 
  
  function isRegUnitConstUnique()
  {
	  var regUnitConst = trimAll(document.getElementById('regUnitConst').value);
	   if(regUnitConst!="")
	   {
	  
	    dom.get("registrationUnit_error").style.display='none';
	    populateregUnitConstCheck({regUnitConst:regUnitConst});
	   } 
  } 

	
	  function checkIsmaxlength(obj){
		dom.get("registrationUnit_error").style.display='none';
		var mlength=obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : "" ;
		if (obj.getAttribute && obj.value.length>mlength){
			obj.value=obj.value.substring(0,mlength);
			dom.get("registrationUnit_error").style.display = '';
			document.getElementById("registrationUnit_error").innerHTML = 'Number of characters entered exceed maxlegth for Street Address';
			obj.value="";
			return false;
		}		
	}
	  
</script>
</head>


<body onload="init();">
     <div class="errorstyle" id="registrationUnit_error" style="display: none;">
	</div>
	<div class="errorstyle" style="display:none" id="regUnitDescCheck">
			<s:text name="registration.desc.alreadyExist"/>
	</div>
	<div class="errorstyle" style="display:none" id="regUnitConstCheck">
			<s:text name="registration.const.alreadyExist"/>
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
		<s:form action="registrationUnit" theme="css_xhtml" name="registrationUnit" validate="true">
		<div class="formheading"/></div>
		<s:token/>
		 <s:push value="model">
			<s:hidden id="mode" name="mode" value="%{mode}"></s:hidden>
			<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td class="bluebox" width="35%">
						&nbsp;
					</td>
					<td class="bluebox" width="15%">
						<s:text name="registration.desc" />
						<span class="mandatory">*</span>
					</td>
					<td class="bluebox">
						<s:textfield id="regUnitDesc" name="regUnitDesc"
							value="%{regUnitDesc}" onblur="validateSpecialCharacter(this,'%{getText('registration.desc')}'); isRegUnitDescUnique();"/>
						 <egov:uniquecheck id="regUnitDescCheck" fieldtoreset="regUnitDesc" fields="['Value']"
										url='common/ajaxCommon!uniqueRegUnitDescCheck.action' />		
					</td>
				</tr>

				<tr>
					<td class="greybox" width="35%">&nbsp;</td>
					<td class="greybox" width="15%"><s:text name="lcn.number" /><span class="mandatory">*</span></td>
					<td class="greybox">
						<s:textfield id="regUnitConst" name="regUnitConst"
							value="%{regUnitConst}"  onblur="validateSpecialCharacter(this,'%{getText('lcn.number')}');isRegUnitConstUnique();"  />
							<egov:uniquecheck id="regUnitConstCheck" fieldtoreset="regUnitConst" fields="['Value']"
										url='common/ajaxCommon!uniqueRegUnitConstCheck.action' />	
					</td>
				</tr>
      
      
               <tr>
					<td class="bluebox" width="35%">&nbsp;</td>
					<td class="bluebox" width="15%"><s:text name="isMainRegUnit" /></td>
					<td class="bluebox">
						<s:radio list="#{'1':'Yes ','0':'No'}" value="%{isMainRegunitValue}"
							name="isMainRegunitValue" id="isMainRegunitValue" onclick="checkIsmainRegUnit(this);" />
						<s:hidden id="ismainregunit" name="ismainregunit" value="%{ismainregunit}"></s:hidden>
					</td>
				</tr>

				<tr>
					<td class="bluebox" width="35%">&nbsp;</td>
					<s:hidden id="regUnitAddress.addressID" name="regUnitAddress.addressID"	value="%{regUnitAddress.addressID}" />
					<td class="bluebox" width="15%"><s:text name="streetaddress.one" /><span class="mandatory">*</span></td>
					<td class="bluebox">
						<s:textarea id="regUnitAddress.streetAddress1"
							name="regUnitAddress.streetAddress1"
							value="%{regUnitAddress.streetAddress1}" rows="1" cols="45" maxlength="512" onblur="validateSpecialCharacter(this,'%{getText('streetaddress.one')}');return checkIsmaxlength(this)" />
					</td>
				</tr>


				<tr>
					<td class="greybox" width="35%">&nbsp;</td>
					<td class="greybox" width="15%"><s:text name="streetaddress.two" /></td>
					<td class="greybox">
						<s:textarea id=" regUnitAddress.streetAddress2"
							name="regUnitAddress.streetAddress2"
							value="%{regUnitAddress.streetAddress2}" rows="1" cols="45" maxlength="512" onblur="validateSpecialCharacter(this,'%{getText('streetaddress.two')}');return checkIsmaxlength(this)"/>
					</td>
				</tr>

				<tr>
					<td class="bluebox" width="35%">&nbsp;</td>
					<td class="bluebox" width="15%"><s:text name="state" /><span class="mandatory">*</span>
					</td>

					<td class="bluebox">
							<s:select name="regUnitAddress.state"
							id="regUnitAddress.state" list="dropdownData.stateList"
							value="%{regUnitAddress.state}" listKey="id" listValue="name"
							headerKey="" headerValue="---choose---" disabled="true"/>
					</td>
				</tr>

				<tr>
					<td class="greybox" width="35%">&nbsp;</td>
					<td class="greybox" width="15%"><s:text name="district" />
						<span class="mandatory">*</span>
					</td>
					<td class="greybox">
						<s:select name="districtName"
							id="districtName" list="dropdownData.districtList"
							value="%{districtName}" listKey="id" listValue="name"
							headerKey="" headerValue="---choose---"
							onchange="populateTalukName();" />
						<s:hidden id="regUnitAddress.district" name="regUnitAddress.district" value="%{regUnitAddress.district}"/>
						<egov:ajaxdropdown id="regUnitAddressTaluk"
							fields="['Text','Value']" dropdownId="regUnitAddressTaluk"
							url="common/ajaxCommon!getTalukNameByDistrict.action" />
					</td>
				</tr>

				<tr>
					<td class="bluebox" width="35%">
						&nbsp;
					</td>
					<td class="bluebox" width="15%">
						<s:text name="taluk" />
						<span class="mandatory">*</span>
					</td>
					<td class="bluebox">
						<s:select id="regUnitAddressTaluk" name="talukName"
							value="%{talukName}" list="dropdownData.talukList"
							listKey="id" listValue="name" headerKey=""
							headerValue="---choose---"  onchange="populateCityName();"/>
						<egov:ajaxdropdown id="regUnitAddressCityTownVillage"
							fields="['Text','Value']" dropdownId="regUnitAddressCityTownVillage"
							url="common/ajaxCommon!getCityNameByTaluk.action" />	
							<s:hidden id="regUnitAddress.taluk" name="regUnitAddress.taluk" value="%{regUnitAddress.taluk}"/>
					</td>
				</tr>

				<tr>
					<td class="greybox" width="35%">
						&nbsp;
					</td>
					<td class="greybox" width="15%">
						<s:text name="city" />
						<span class="mandatory">*</span>
					</td>
					<td class="greybox">
					   <s:select id="regUnitAddressCityTownVillage"
							     name="cityName"
							     value="%{cityName}" onchange="populateCityNameLbl();"
							     list="dropdownData.cityList" listKey="id" listValue="name" headerKey="" headerValue="---choose---" />
					<s:hidden id="regUnitAddress.cityTownVillage" name="regUnitAddress.cityTownVillage" value="%{regUnitAddress.cityTownVillage}"/> 
					</td>
				</tr>

				<tr>
					<td class="bluebox" width="35%">
						&nbsp;
					</td>
					<td class="bluebox" width="15%">
						<s:text name="pincode"></s:text>
						<span class="mandatory">*</span>
					</td>
					<td class="bluebox">
						<s:textfield id="regUnitAddress.pinCode"
							name="regUnitAddress.pinCode" value="%{regUnitAddress.pinCode}"
							maxlength="6" onblur="validateAddressPincode(this);  " />
					</td>
				</tr>

				
			</table>

			<div class="buttonbottom" align="center">


				<table>
					<tr>
					<s:if test="%{mode!='view'}">
						<td>
							<s:submit cssClass="buttonsubmit" id="create" name="create"
								value="Create"  method="create"  onclick="return enableSate();" />
								
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
			</s:push>
		</s:form>

	</body>

</html>
