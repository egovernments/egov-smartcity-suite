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
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix="sx" uri="/WEB-INF/struts-dojo-tags.tld"%>

<html>
 <head>
	 <title>
			<s:if test="%{mode!='view'}">
				<s:text name="create.hospital"/>
			</s:if>
			<s:else>
		    	<s:text name="view.hospital"></s:text> 
		    </s:else>
	   </title>
 
  
    <sx:head/>
  	
		<SCRIPT type="text/javascript" >
	
	 
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

/*	function validateForm() 
	{
		
		if (populate("name").value == "") {
			alert('<s:text name="hospital.name.required"/>');
			return false;
		}
		if (populate("type").value == "-1") {
			alert('<s:text name="hospital.type.required"/>');
			return false;
		}
		if (populate("regUnit").value == "-1") {
			alert('<s:text name="registration.unit.required"/>');
			return false;
		}
		
		
       if(!checkisAuth())
       {
         alert('<s:text name="isauthorized.issuecertificate.required"/>');
         return false;
         }
		
		if (!validateAddress("address"))
			return false;

		return true;
	}

		
*/	
/*	function checkisAuth()
	{
	 alert("inside checkisAuth()");
	 alert(document.getElementById("isAuth").value);
	  var flag=false;
	  var isauth =document.getElementsByName("isAuthValue");
		 for(var i=0; i < isauth.length; i++ )
		 {
		    if((isauth[i].checked))
		    {
		     flag=true;
		    }
		     
		 }
		return flag;
	}
*/	
	function populateTalukName() {
		populate("address.district").value = populate('districtName').options[populate('districtName').selectedIndex].text;
		populateaddressTaluk({districtId : populate('districtName').value});
		return true;
	}
	
	function populateAddressCityName()
	{
	   populate("address.taluk").value = populate('addressTaluk').options[populate('addressTaluk').selectedIndex].text;
	   populatecityName({talukId : populate('addressTaluk').value});
	   return true;
	}
	
	function populateCityNameLbl()
	{
	     populate("address.cityTownVillage").value = populate('cityName').options[populate('cityName').selectedIndex].text;
	}
	
	 function validate()
	 {
	 
	
		if(document.getElementById("isAuth").value =="" ||document.getElementById("isAuth").value == null)
		{
	 	   dom.get("hospital_error").style.display = '';
	  	  document.getElementById("hospital_error").innerHTML = '<s:text name="isauthorized.issuecertificate.required" />';
        	return false;
		}
	 
	  
	  var isError = false;
			
		if(!validateForm_establishment()){
	  	   isError = true;
		}
		if(isError)
	  	    return false;
		else{
			 populate('address.state').disabled=false; 
			return true;
		}
	  
	    
	      
	 }
	 
	 
	 function validateSpecialCharacter(obj,fieldName)
	  {
	
	     if(!checkSpecialCharacters(obj))
	     {
	       dom.get("hospital_error").style.display = '';
		   document.getElementById("hospital_error").innerHTML = '<s:text name="invalid.data" />'+fieldName;
	       obj.value="";
	        return false;
	       
	     }
	    
		return true;
	 
	  }
	  
function validateAddressPincode(obj)
  {
        if(trimAll(obj.value)!=""){
        	if(isNaN(obj.value))
			{				
				dom.get("hospital_error").style.display = '';
		      	document.getElementById("hospital_error").innerHTML = '<s:text name="invalid.pincode" />';
  		      	obj.value="";
		  		return false;
			}
  			if(!validatePincode(obj)){
  			    dom.get("hospital_error").style.display = '';
		      	document.getElementById("hospital_error").innerHTML = '<s:text name="invalid.pincode" />'; 		    	
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
  
  function setIsAuth(isauthObj)
  {
    if(isauthObj!=null){
                   if(isauthObj.value=='1'){
                           document.getElementById('isAuth').value=true;
                   }
                   else if(isauthObj.value=='0'){
                           document.getElementById('isAuth').value=false;
                   }
               }
  }
  
  function isHospitalNameUnique()
  {

     var name = trimAll(document.getElementById('name').value);
	   if(name!="")
	   {
	    dom.get("hospital_error").style.display='none';
	    populatehospitalNameCheck({name:name});
	   } 
  }
  
   function checkIsmaxlength(obj){
		dom.get("hospital_error").style.display='none';
		var mlength=obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : "" ;
		if (obj.getAttribute && obj.value.length>mlength){
			obj.value=obj.value.substring(0,mlength);
			dom.get("hospital_error").style.display = '';
			document.getElementById("hospital_error").innerHTML = 'Number of characters entered exceed maxlegth for Street Address';
			obj.value="";
			return false;
		}		
	}
  
</SCRIPT>
 </head>
 <body onload="init();">
   <div class="errorstyle" id="hospital_error" style="display: none;">
	</div>
	<div class="errorstyle" style="display:none" id="hospitalNameCheck">
			<s:text name="hospital.name.alreadyExist"/>
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
   <s:form action="establishment" theme="css_xhtml" name="establishment" validate="true">
   <div class="formheading"/></div>
   <s:token/>
    	<s:hidden id="mode" name="mode" value="%{mode}"></s:hidden>
     <table width="100%" cellpadding="0" cellspacing="0" border="0">
        <tr>
           <td class="bluebox" width="35%" > &nbsp;</td>
           <td class="bluebox" width="15%">
               <s:text name="hospital.name"/>
               <span class="mandatory">* </span>
           </td> 
           <td class="bluebox">
            <s:textfield  id="name" name="name" value="%{name}" onblur="validateSpecialCharacter(this,'%{getText('hospital.name')}'); isHospitalNameUnique();"/>  
            <egov:uniquecheck id="hospitalNameCheck" fieldtoreset="name" fields="['Value']"
										url='common/ajaxCommon!uniqueHospitalNameCheck.action' />	
           </td>
        </tr>
        
        <tr>
					<td class="greybox" width="35%">
						&nbsp;
					</td>
					<td class="greybox" width="15%">
						<s:text name="hospital.type" />
						<span class="mandatory">*</span>
					</td>
					<td class="greybox">
						<s:select name="type"
							id="type" list="dropdownData.hospitalTypeList" 
							value="%{type.id}" listKey="id" listValue="desc"
							headerKey="" headerValue="---choose---"  />
		
					</td>
				</tr>
        <tr>
					<td class="bluebox" width="35%">
						&nbsp;
					</td>
					<td class="bluebox" width="15%">
						<s:text name="registration.unit" />
						<span class="mandatory">*</span>
					</td>
					<td class="bluebox">
						<s:select name="regUnit"
							id="regUnit" list="dropdownData.registrationUnitList"
							value="%{regUnit.id}" listKey="id" listValue="regUnitDesc"
							headerKey="" headerValue="---choose---" />
						
					</td>
				</tr>
        
        <tr>
		       <td class="greybox" width="35%">
						&nbsp;
			   </td>
				<td class="greybox" width="15%">
						<s:text name="isauthorized.issuecertificate" />
						<span class="mandatory">*</span>
				</td>
				<td class="greybox">
						<s:radio list="#{'1':'Yes ','0':'No'}" value="%{isAuthValue}"
							name="isAuthValue" id="isAuthValue"  onclick="setIsAuth(this);"/>
					   <s:hidden id="isAuth" name="isAuth" value="%{isAuth}" />		
				</td>
		</tr>
				
			
			
			
			<tr>
					<td class="bluebox" width="35%">
						&nbsp;
					</td>
					<s:hidden id="address.addressID"
						name="address.addressID"
						value="%{address.addressID}" />
					<td class="bluebox" width="15%">
						<s:text name="streetaddress.one" />
						<span class="mandatory">*</span>
					</td>
					<td class="bluebox">
						<s:textarea id=" address.streetAddress1"
							name="address.streetAddress1"
							value="%{address.streetAddress1}" rows="1" cols="45" maxlength="512" onblur="validateSpecialCharacter(this,'%{getText('streetaddress.one')}');return checkIsmaxlength(this)" />
					</td>
				</tr>


				<tr>
					<td class="greybox" width="35%">
						&nbsp;
					</td>
					<td class="greybox" width="15%">
						<s:text name="streetaddress.two" />
					</td>
					<td class="greybox">
						<s:textarea id=" address.streetAddress2"
							name="address.streetAddress2"
							value="%{address.streetAddress2}" rows="1" cols="45" maxlength="512"  onblur="validateSpecialCharacter(this,'%{getText('streetaddress.two')}');return checkIsmaxlength(this)" />
					</td>
				</tr>
			
				<tr>
					<td class="bluebox" width="35%">
						&nbsp;
					</td>
					<td class="bluebox" width="15%">
						<s:text name="state" />
						<span class="mandatory">*</span>
					</td>

					<td class="bluebox">
						<s:select name="address.state"
							id="address.state" list="dropdownData.stateList"
							value="%{address.state}" listKey="id" listValue="name"
							headerKey="" headerValue="---choose---" disabled="true"/>
		
						<!--<s:textfield id="address.state" name="address.state"
							value="%{address.state}" disabled="true"/>
					--></td>
				</tr>
				
				
				<tr>
					<td class="greybox" width="35%">
						&nbsp;
					</td>
					<td class="greybox" width="15%">
						<s:text name="district" />
						<span class="mandatory">*</span>
					</td>
					<td class="greybox">
						<s:select name="districtName"
							id="districtName" list="dropdownData.districtList"
							value="%{districtName}" listKey="id" listValue="name"
							headerKey="" headerValue="---choose---" onchange="populateTalukName();" />
						<s:hidden id="address.district" name="address.district" value="%{address.district}"/>
						<egov:ajaxdropdown id="addressTaluk"
							fields="['Text','Value']" dropdownId="addressTaluk"
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
						<s:select id="addressTaluk" name="talukName"
							value="%{talukName}" list="dropdownData.talukList"
							listKey="id" listValue="name" headerKey=""
							headerValue="---choose---"  onchange="populateAddressCityName();"/>
					   <s:hidden id="address.taluk" name="address.taluk" value="%{address.taluk}"/>
						  <egov:ajaxdropdown id="cityName"
							fields="['Text','Value']" dropdownId="cityName"
							url="common/ajaxCommon!getCityNameByTaluk.action" />		
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
					   <s:select id="cityName" name="cityName" value="%{cityName}" 
							     list="dropdownData.cityList" listKey="id" listValue="name" headerKey="" headerValue="---choose---" 
							     onchange="populateCityNameLbl();"/>
					   <s:hidden id="address.cityTownVillage" name="address.cityTownVillage" value="%{address.cityTownVillage}"/> 
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
						<s:textfield id="address.pinCode"
							name="address.pinCode" value="%{address.pinCode}"
							maxlength="6" onblur="validateAddressPincode(this);" />
					</td>
				</tr>			
     
     </table>
     
     <div class="buttonbottom" align="center">


				<table>
					<tr>
					<s:if test="%{mode!='view'}">
						<td>
							<s:submit cssClass="buttonsubmit" id="create" name="create"
								value="Create"  method="create"  onclick="return validate();" />
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
 </body>
</html>
