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
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script>

	 var PERMANENTCITIZENADDRESS= "permanentCitizenAddress"; 
	 var NOTAVAILABLE          =  '<s:property value="@org.egov.bnd.utils.BndConstants@NOTAVAILABLE"/>';
	 var EVENTADDRESS 		   = "eventAddress";
	 var PARENTADDRESS 		   = "parentAddress";
	 var ADOPTEEMOTHER         = "adoptionDetail.adopteeMother";
	 var ADOPTEEFATHER         = "adoptionDetail.adopteeFather";
	 
   /**
    *   This method is to validate adoption details
    */  
    
   function checkAdoptionDetails()
   {
      var isError = false;
      var error = "Required";
      var form = document.getElementById("adoption");
      clearErrorMessages(form);
      clearErrorLabels(form);
      if(populate("isChildAdopted").checked==true)
      {
         if(!validateForm_adoption() ){
	  	    isError = true;
		 }
		 if(trimAll(populate("adoptionDate").value)=="" )
		 {
	     	addError(populate("adoptionDate"),error);
	     	isError = true;
		 } 
	   }
      if(isError)
      	return false;
      else
      	return true;
   }
   
   /**
    *  This method is called on load of the page..
    */
    
   function bodyonlaod()
   {
       var mode = document.getElementById("mode").value;
       populate("adoptionDetails").style.display = 'block';
       if(mode=="view")
       {
       
           for (var i =0; i < document.forms[0].length;i++) {
			if (document.forms[0].elements[i].name != 'close')
				document.forms[0].elements[i].disabled = true;
		}
       }
   	   else{
   		for (var i =0; i < document.forms[0].length;i++) {
			if (document.forms[0].elements[i].name != 'close' 
					&& document.forms[0].elements[i].id != 'save')
				document.forms[0].elements[i].disabled = true;
		}
		
		populate("isChildAdopted").disabled = true;
		populate("isChildAdopted").checked = true;
		populate("adoptionDetails").style.display = 'block';
		
		makeEnableCitizen(ADOPTEEMOTHER,"true");
		makeEnableCitizen(ADOPTEEFATHER,"true");
		populate("adoptionDetail.adoptionNumber").disabled = false;
		populate("adoptionDetail.affidavitNumber").disabled = false;
		populate("adoptionDate").disabled = false;
		populate("adoptionDetail.adoptionInstitute").disabled = false;
		populate("adoptionDetail.adopteeAddress.streetAddress1").disabled = false;
		populate("adoptionDetail.courtOrderNumber").disabled = false;
		populate("adoptionDetail.remarks").disabled = false;
			
		
		var permanentAddressFlag = document.getElementsByName("permanentAddressFlag");
		 
	   if(permanentAddressFlag[0].checked)
	     populateDefaultAddress(PERMANENTCITIZENADDRESS);
	 
	   var placeType = getPlaceTypeValue();
   	   if(placeType == '<s:property value="@org.egov.bnd.utils.BndConstants@NOTSTATED"/>')
	       populateDefaultAddress(EVENTADDRESS);
	       
	   var parentAddressFlag = document.getElementsByName("parentAddressFlag");
	   if(parentAddressFlag[0].checked)
	     populateDefaultAddress(PARENTADDRESS);
	   }
   }
   
   
   
  function populateDefaultAddress(address)
  {
     populate(address+".streetAddress1").value = NOTAVAILABLE;
	 populate(address+".streetAddress2").value = NOTAVAILABLE;
     populate(address+".taluk").value          = NOTAVAILABLE;
     populate(address+".cityTownVillage").value= NOTAVAILABLE;
     populate(address+".pinCode").value        = "";
     populate(address+".district").value       = NOTAVAILABLE;
     populate(address+".state").value          = populate("defaultState").value;
	  		
  }
  
  function populate(objName)
  {
     return document.getElementById(objName);
  }
  
  /**
     *   This Api is to return Selected Place type value
     */
     
   function getPlaceTypeValue()
   {
    	var placeType = document.getElementsByName("placeTypeTemp");
		var count;
		var placeTypeValue ="";
		for(count=0;count<placeType.length;count++)
		{
	    	if(placeType[count].checked )
	    	{
	       	 placeTypeValue = placeType[count].value;
	        	break;
	    	}
		}
	return placeTypeValue;
   }
   
   function makeEnableCitizen(citizen,emailflag)
   {
	  populate(citizen+".firstName").disabled = false;
	  populate(citizen+".middleName").disabled = false;
	  populate(citizen+".lastName").disabled = false;
	  if(emailflag=="true")
	  {
		 populate(citizen+".mobilePhone").disabled = false;
		 populate(citizen+".emailAddress").disabled = false;
	  }
	}
	
	function enablingFields()
	{ 
   		for (var i =0; i < document.forms[0].length;i++) {
	    	if (document.forms[0].elements[i].name != 'save')
				document.forms[0].elements[i].disabled = false;
			else
			   document.forms[0].elements[i].disabled = true;
		}
    }
   
</script>
