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
 
<script>


    var PERMANENTCITIZENADDRESS="permanentCitizenAddress";
	var CITIZEN="citizen";
	var EVENTADDRESS = "eventAddress";
	var MOTHER ="mother";
	var FATHER="father";
	var HUSBAND="husband";
	var PARENTADDRESS="parentAddress";
	var DECEASEDADDRESS="deceasedAddress";
	var INFORMANT = "informant";
	var INFORMANTCITIZEN = "informantCitizen";
	var INFORMANTADDRESS = "informantAddress";
	var DECEASEDRELATION="deceasedrelation";
	var NOTAVAILABLE = '<s:property value="@org.egov.bnd.utils.BndConstants@NOTAVAILABLE"/>';
	var HOSPITALUSERFLAG = '<s:property value="%{deathHospitalUserFlag}"/>';
	var HOSPITALREGISTRARFLAG = '<s:property value="%{deathHospitalRegistrarFlag}"/>';
	var DEATHDELAYEDFLAG ='<s:property value="%{deathDelayedFlag}"/>';
    var changedString = "";
    var PERMANENTCITIZENADDRESS="permanentCitizenAddress";
    var PERMANENTCITIZENADDRESSHISTORY ="deathHistory.permanentCitizenAddress";
    var EVENTADDRESSHISTORY = "deathHistory.eventAddress";
    var CITIZENHISTORY  ="deathHistory.citizen";
    var MOTHERHISTORY = "deathHistory.mother";
    var FATHERHISTORY = "deathHistory.father";
    var PARENTADDRESSHISTORY    =     "deathHistory.parentAddress";
    var INFORMANTCITIZENHISTORY = "informantCitizenDeathHistory";
    var INFORMANTADDRESSHISTORY = "deathHistory.informantAddress";
	var DECEASEDRELATIONHISTORY = "deathHistory.deceasedrelation";
	var DECEASEDADDRESSHISTORY  = "deathHistory.deceasedAddress";
	var RELATIONTYPEHISTORY     = "deathHistory.deceasedAddress.relatedAsConst";
	var GRACEPERIOD = '<s:property value="%{gracePeriod}"/>';
	var NUMBERGENKEY = '<s:property value="%{numberGenKey}"/>';
	
	
	function loadDesignationFromMatrix()
    {
       var currentState=document.getElementById('currentState').value;
       var amountRule=document.getElementById('amountRule').value;
       var additionalRule=document.getElementById('additionalRule').value;
       var pendingActions=document.getElementById('pendingActions').value;
      // var dept=document.getElementById('dept').options[document.getElementById('dept').selectedIndex].text;
       loadDesignationByDeptAndType('DeathRegistration',null,currentState,amountRule,additionalRule,pendingActions); 
    }
	
	function populateApprover()
	{
		getUsersByDesignationAndDept();
	}
	
	
	function clearDeceasedRelation()
	
	{	
	if(populate("deceasedrelationType").value=="-1")
	{
	makeEmptyCitizen(DECEASEDRELATION,false);
	makeReadOnlyCitizen(DECEASEDRELATION,false);
	}else	
	{	
	makeEnableCitizen(DECEASEDRELATION,false);
	}
	
	
	}
	
	function bodyOnLoad()		
	{
		 var isMainUnit = populate("ismainregunit").value;
	     if(populate("registrationMode").value=='online'&&isMainUnit=='false')
	    	 populate("registrationDate").disabled=true;
	     else
	     	populate("registrationDate").disabled=false;
	     
	      enablestaticDetails();
		 var deceasedNameFlag = document.getElementsByName("nameOfdeceasedFlag");
		 if(deceasedNameFlag[0].checked)
		    retainCitizen(CITIZEN,"false");
		 
		
		 var permanentAddressFlag = document.getElementsByName("permanentAddressFlag");
		  
		 if(permanentAddressFlag[0].checked)
		    retainAddress(PERMANENTCITIZENADDRESS);
		 	
		    reatinPlaceType();	
		    
		 if(HOSPITALUSERFLAG ==1 || HOSPITALREGISTRARFLAG == 1)
		 {
		    populate("hospitalType").disabled=true;
		    populate("establishment").disabled=true;
		    disablePlaceType();
		 }
		  
		
		 var deceasedAddressFlag = document.getElementsByName("deceasedAddressFlag");
		
		 if(deceasedAddressFlag[0].checked)
		    retainAddress(DECEASEDADDRESS);
		       
		    checkAge();
		   
		  if(populate("mode").value=="edit" || populate("mode").value=="modify")
	      {
	          populate("registrationNo").readOnly = true;
	          populate("dateOfEvent").disabled = true;
	          populate("registrationDate").disabled = true;
	           populateAddressHistory();
	      }   
		     
	     if(populate("mode").value=="view")
	     {
	   		for (var i =0; i < document.forms[0].length;i++) {
				if (document.forms[0].elements[i].name != 'close')
					document.forms[0].elements[i].disabled = true;
					
			}
	     }
	     if(populate("mode").value=="lock")
	     {
	   		for (var i =0; i < document.forms[0].length;i++) {
				if (document.forms[0].elements[i].name != 'close' && document.forms[0].elements[i].id != 'lock' )
					document.forms[0].elements[i].disabled = true;
					
			}
	     }
	       if(populate("mode").value=="unlock")
	     {
	   		for (var i =0; i < document.forms[0].length;i++) {
				if (document.forms[0].elements[i].name != 'close' && document.forms[0].elements[i].id != 'unlock' )
					document.forms[0].elements[i].disabled = true;
					
			}
	     }
	     
	     
	     
	          // For Inbox it decides whether form is editable or not
	     if(populate("mode").value == "notmodify")
	     {
	   		for (var i =0; i < document.forms[0].length;i++) {
				if (document.forms[0].elements[i].name != 'close' && document.forms[0].elements[i].id != 'Approve' 
				       && document.forms[0].elements[i].id != 'save'
				       && document.forms[0].elements[i].id != 'Reject' && document.forms[0].elements[i].id != 'approverComments')
					document.forms[0].elements[i].disabled = true;
					
			}
	     }
	   
	   clearDeceasedRelation();
	   enablestaticDetails()
	}
	
		function populateAddress(obj)
	{
	   
       var addressFlag = document.getElementsByName(obj.name);
       
       var address="",addressType="";
       
       if(obj.name=="permanentAddressFlag")
       {
         address = PERMANENTCITIZENADDRESS;
         addressType = '<s:property value="@org.egov.bnd.utils.BndConstants@PERMANENTADDRESS"/>';
       }
       else if(obj.name=="parentAddressFlag")
       {
         address = PARENTADDRESS;
         addressType = '<s:property value="@org.egov.bnd.utils.BndConstants@CORRESPONDINGADDRESS"/>';
       }
       
        else if(obj.name=="deceasedAddressFlag")
       {
         address = DECEASEDADDRESS;
         addressType = '<s:property value="@org.egov.bnd.utils.BndConstants@DECEASEDADDRESS"/>';
       }
       
       if(addressFlag[0].value==1 && addressFlag[0].checked )
	   {
			makeEmptyAddress(address);
			makeEnableAddress(address);
	   }
		
       if(addressFlag[1].value==1 && addressFlag[1].checked )
		{
			makeEmptyAddress(address);
			makeEnableAddress(address);
		}
		else
		{
		    populateDefaultAddress(address,addressType);
			makeReadOnlyAddress(address);		
		}
	 }
	 
	 
		function retainCitizen(citizen,flag)
	{
	    populate(citizen+".firstName").value = NOTAVAILABLE;
		populate(citizen+".middleName").value = "";
		populate(citizen+".lastName").value = "";
		makeReadOnlyCitizen(citizen,flag);
	}
	
	
	function retainAddress(address,flag)
	{
	    populateDefaultAddress(address,"");
	    makeReadOnlyAddress(address);
	}
	
	
	
	
		function populateCitizenName()
	{
		var deceasedNameFlag = document.getElementsByName("nameOfdeceasedFlag");
	          
		if(deceasedNameFlag[1].checked)
		{
		
			makeEmptyCitizen(CITIZEN,"false");
			makeEnableCitizen(CITIZEN,"false");
		}
		else
		{
		  
			populate("citizen.firstName").value = NOTAVAILABLE;
			populate("citizen.middleName").value = "";
			populate("citizen.lastName").value = "";
			makeReadOnlyCitizen(CITIZEN,"false");
		}
	}
	
 
		function clearHospitalDetails()
	{
		var placeType = document.getElementsByName("placeTypeTemp");
		var count;
		var notStatedFlag="false",hosptialFlag="false";	
		
		for(count=0;count<placeType.length;count++)
		{
		    if(placeType[count].checked )
		    {
		    	
		        if(placeType[count].value=='<s:property value="@org.egov.bnd.utils.BndConstants@NOTSTATED"/>'){
		        	notStatedFlag ="true";
		        	break;
		        }
		        else if(placeType[count].value=='<s:property value="@org.egov.bnd.utils.BndConstants@HOSPTIAL"/>'){
		        	hosptialFlag ="true";
		        	break;
		        }
		    }
		   
		}
	
		if(notStatedFlag == "true")
		{
			 if(populate(EVENTADDRESS+".streetAddress1").value==populate(INFORMANTADDRESS+".streetAddress1").value&&
		 populate(EVENTADDRESS+".streetAddress2").value==populate(INFORMANTADDRESS+".streetAddress2").value&&
		 populate(EVENTADDRESS+".taluk").value==populate(INFORMANTADDRESS+".taluk").value&&
		 populate(EVENTADDRESS+".cityTownVillage").value==populate(INFORMANTADDRESS+".cityTownVillage").value&&
		 populate(EVENTADDRESS+".pinCode").value==populate(INFORMANTADDRESS+".pinCode").value&&
		 populate(EVENTADDRESS+".district").value==populate(INFORMANTADDRESS+".district").value&&
		 populate(EVENTADDRESS+".state").value==populate(INFORMANTADDRESS+".state").value){
		 makeEmptyAddress(INFORMANTADDRESS);
		 makeEnableAddress(INFORMANTADDRESS);
		 makeEmptyCitizen(INFORMANTCITIZEN,false);
    	 makeEnableCitizen(INFORMANTCITIZEN,"false"); 
		 }
		     populate("hospitalType").disabled = true;
		     populate("establishment").disabled = true;
		     populate("hospitalType").value = "-1";
		     populate("establishment").value = "-1";
		     populateDefaultAddress(EVENTADDRESS,'<s:property value="@org.egov.bnd.utils.BndConstants@EVENTADDRESS"/>');
		     makeReadOnlyAddress(EVENTADDRESS);
		     populate("relationType").disabled = false;
		  
		}
		else if(hosptialFlag =="true")
		{
	        
		    makeEmptyAddress(EVENTADDRESS);
			makeReadOnlyAddress(EVENTADDRESS);			
			makeReadOnlyCitizen(INFORMANTCITIZEN);
			populate("hospitalType").disabled = false;
		    populate("establishment").disabled = false;
		    populate("relationType").value="-1";
		    populate("relationType").disabled = true;
		}
		else
		
		{ 
		 if(populate(EVENTADDRESS+".streetAddress1").value==populate(INFORMANTADDRESS+".streetAddress1").value&&
		 populate(EVENTADDRESS+".streetAddress2").value==populate(INFORMANTADDRESS+".streetAddress2").value&&
		 populate(EVENTADDRESS+".taluk").value==populate(INFORMANTADDRESS+".taluk").value&&
		 populate(EVENTADDRESS+".cityTownVillage").value==populate(INFORMANTADDRESS+".cityTownVillage").value&&
		 populate(EVENTADDRESS+".pinCode").value==populate(INFORMANTADDRESS+".pinCode").value&&
		 populate(EVENTADDRESS+".district").value==populate(INFORMANTADDRESS+".district").value&&
		 populate(EVENTADDRESS+".state").value==populate(INFORMANTADDRESS+".state").value){
		 makeEmptyAddress(INFORMANTADDRESS);
		 makeEnableAddress(INFORMANTADDRESS);
		 makeEmptyCitizen(INFORMANTCITIZEN,false);
    	 makeEnableCitizen(INFORMANTCITIZEN,"false"); 
		 }
 
		    makeEmptyAddress(EVENTADDRESS);
			makeEnableAddress(EVENTADDRESS);   
    	    populate("relationType").disabled = false;
			populate("hospitalType").disabled = true;
		    populate("establishment").disabled = true;
		    populate("hospitalType").value = "-1";
		    populate("establishment").value = "-1";
		  
		}
			
	}
	
	
	  function populate(objName)
  {
     return document.getElementById(objName);
  }
  
  
	  
	  function populateDefaultAddress(address,addressType)
	  {
	 	
      	//populate(address+".addressID").value      = "";
	     populate(address+".streetAddress1").value = NOTAVAILABLE;
	     populate(address+".streetAddress2").value = NOTAVAILABLE;
	     populate(address+".taluk").value          = NOTAVAILABLE;
	     populate(address+".cityTownVillage").value= NOTAVAILABLE;
	     populate(address+".pinCode").value        = "";
	     populate(address+".district").value       = NOTAVAILABLE;
	     populate(address+".state").value          = populate("defaultState").value;
	  		
	  }
  
	  function makeEmptyAddress(address)
	  {
	      //populate(address+".addressID").value="";
	  	  populate(address+".streetAddress1").value="";
		  populate(address+".streetAddress2").value="";
		  populate(address+".taluk").value="";
		  populate(address+".cityTownVillage").value="";
		  populate(address+".pinCode").value="";
		  populate(address+".district").value="";
		  populate(address+".state").value="";
	  }
  
	  function makeReadOnlyAddress(address)
	  {
	     // populate(address+".addressID").value="";
	  	  populate(address+".streetAddress1").readOnly = true;
		  populate(address+".streetAddress2").readOnly = true;
		  populate(address+".taluk").readOnly = true;
		  populate(address+".cityTownVillage").readOnly = true;
		  populate(address+".pinCode").readOnly = true;
		  populate(address+".district").readOnly = true;
		  populate(address+".state").disabled =true;
		  populate(address+".streetAddress1").className = "readonlyareacss";
		  populate(address+".streetAddress2").className = "readonlyareacss";
		  populate(address+".taluk").className = "readonlycss";
		  populate(address+".cityTownVillage").className = "readonlycss";
		  populate(address+".pinCode").className = "readonlycss";
		  populate(address+".district").className = "readonlycss";
		  populate(address+".state").className = "readonlycss";
	  }
  
	  function makeEnableAddress(address)
	  {
	      //populate(address+".addressID").value="";
	  	  populate(address+".streetAddress1").readOnly = false;
		  populate(address+".streetAddress2").readOnly = false;
		  populate(address+".taluk").readOnly = false;
		  populate(address+".cityTownVillage").readOnly = false;
		  populate(address+".pinCode").readOnly = false;
		  populate(address+".district").readOnly = false;
		  populate(address+".state").disabled = false;
		  populate(address+".streetAddress1").className = "";
		  populate(address+".streetAddress2").className = "";
		  populate(address+".taluk").className = "";
		  populate(address+".cityTownVillage").className = "";
		  populate(address+".pinCode").className = "";
		  populate(address+".district").className = "";
		  populate(address+".state").className = "";
	  }
  
	   function makeEmptyCitizen(citizen,emailflag)
	   {
	  	  	
	      populate(citizen+".firstName").value = "";
		  populate(citizen+".middleName").value = "";
		  populate(citizen+".lastName").value = "";
		  if(emailflag=="true")
		  {
		    populate(citizen+".mobilePhone").value = "";
		    populate(citizen+".emailAddress").value = "";
		  }
	   }
  
	  function makeReadOnlyCitizen(citizen,emailflag)
	  {
	      populate(citizen+".firstName").readOnly = true;
		  populate(citizen+".middleName").readOnly = true;
		  populate(citizen+".lastName").readOnly = true;
		  populate(citizen+".firstName").className = "readonlycss";
		  populate(citizen+".middleName").className = "readonlycss";
		  populate(citizen+".lastName").className = "readonlycss";
		  if(emailflag=="true")
		  {
		    populate(citizen+".mobilePhone").readOnly = true;
		    populate(citizen+".emailAddress").readOnly = true;
		    populate(citizen+".mobilePhone").className = "readonlycss";
		  	populate(citizen+".emailAddress").className = "readonlycss";
		  }
	  }
	  
	  function makeEnableCitizen(citizen,emailflag)
	  {
	      populate(citizen+".firstName").readOnly = false;
		  populate(citizen+".middleName").readOnly = false;
		  populate(citizen+".lastName").readOnly = false;
		   populate(citizen+".firstName").className = "";
		  populate(citizen+".middleName").className = "";
		  populate(citizen+".lastName").className = "";
		  if(emailflag=="true")
		  {
		    populate(citizen+".mobilePhone").readOnly = false;
		    populate(citizen+".emailAddress").readOnly = false;
		    populate(citizen+".mobilePhone").className = "";
		  	populate(citizen+".emailAddress").className = "";
		  }
	  }
	  
	  
	  /**
	   *     This method is to validate all the address field
	   */
	    
	  function validateAddress(address,addresslbl)
	  {
	       var error="Required";
	       var isError=false;
	       
	       if(populate(address+".streetAddress1").value=="")
	       {
	         // warn(addresslbl+" "+'<s:text name="streetAddress1.required"/>');
	            addError(populate(address+".streetAddress1"),error);
	          isError = true;
			
	       }
	       if(populate(address+".cityTownVillage").value=="")
	       {
	         // warn(addresslbl+" "+'<s:text name="cityTownVillage.required"/>');
	             addError(populate(address+".streetAddress1"),error);
			     isError = true;
	       }
	       if(populate(address+".district").value=="")
	       {
			  //warn(addresslbl+" "+'<s:text name="district.required"/>');
			     addError(populate(address+".streetAddress1"),error);
			    isError = true;
	       }
	       if(populate(address+".state").value=="")
	       {
			  //warn(addresslbl+" "+'<s:text name="state.required"/>');
			     addError(populate(address+".streetAddress1"),error);
			     isError = true;
	       }
	       if(isError)
	       return false;
	       
	       return true;
	  }
	  
	  	function enableInformant()
	{
	     var informantFlag = document.getElementsByName("informantFlag");
	     for(count=0;count<informantFlag.length;count++)
	     {
		   informantFlag[count].readOnly=false;
		   informantFlag[count].checked=false;
		 }
	     makeEmptyCitizen(INFORMANTCITIZEN,"false");
	     makeEnableCitizen(INFORMANTCITIZEN,"false");
	     makeEmptyAddress(INFORMANTADDRESS);
	     makeEnableAddress(INFORMANTADDRESS);
	}
	
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
    
    function populateEstablishment(elem){
    	
   			var hospitaltype = elem.value;
   			var regUnit=document.getElementById('registrationUnit').value	
		  populateestablishment({hospitalType:hospitaltype,regUnit:regUnit});	
		 }
	
   
   function populatedeathcause(elem){
		var parentDeathCause = elem.value;  		
		  populatecauseOfDeath({parentDeathCauseId:parentDeathCause});	
	
	}
	
	
		function validatePlaceOfDeath()
	{
	   
		var placeType = getPlaceTypeValue();
		var isError = false;
		var error="Required";
		if(placeType == "")
	    {
	       warn('<s:text name="PlaceOfDeath.required"/>');
	       return false;
	    }
	    if(placeType =='<s:property value="@org.egov.bnd.utils.BndConstants@HOSPTIAL"/>')
	    {
	         if(populate("hospitalType").value=="-1")
	         {
	           addError(populate("hospitalType"),error);
	            isError = true;
	         }
	         
	         if(populate("establishment").value=="-1")
	         {
	           addError(populate("hospitalType"),error);
	            isError = true;
	         }
	    }
	   /* else if(placeType != '<s:property value="@org.egov.bnd.utils.BndConstants@NOTSTATED"/>')
	    {
	        return validateAddress(EVENTADDRESS,'<s:text name="deatheventAddress.lbl"/>');
	    }*/
	    
	     if(isError)
	      return false;
	    return true;
	    
	}
	
		 
	function validateCitizen(citizen,citizenlbl)
	{	
		
	    if(trimAll(populate(citizen+".firstName").value)=="")
	    {
	        warn(citizenlbl+" "+'<s:text name="firstName.required"/>');
	        return false;
	    }
	    return true;
	}
	
	
	function validateCitizenAge(citizen)
	{
		if (populate('ageType').value == "") {
			 warn('<s:text name="deceased.age.required"/>');
			 return false;
		}
	
	    if(trimAll(populate('age').value)==""||trimAll(populate('age').value)==0)
	    {
	        var agetypes=document.getElementById("ageType").options[document.getElementById('ageType').selectedIndex].text;
	    	if(agetypes!="Not Stated"){
	        warn('<s:text name="deceased.age.required"/>');
	        return false;
	          }
	          
	    }
	    return true;
	}
	
	
	function validateInformant(){
	
	 var placeType = document.getElementsByName("placeTypeTemp");
		var count; 
		for(count=0;count<placeType.length;count++)
		{
		    if(placeType[count].checked )
		    {   	
		         if(placeType[count].value=='<s:property value="@org.egov.bnd.utils.BndConstants@HOSPTIAL"/>'){
		      		return true;
		        }
		   }
		}
	
	if(populate('relationType').value ==""){
	  warn('<s:text name="deceased.informant.lbl.required"/>');
	  return false;
	  }
	  else 
	  return true;
	}
	
	
	
	function validateForm(obj)
	{
	 	dom.get("deathRegistration_error").style.display = '';
	 	form = document.getElementById("deathRegistration");
        clearErrorMessages(form);
        clearErrorLabels(form);
	 	      
		var isError = false;
		var error = "Required";
	 	if(!validateForm_deathRegistration()){
	  	   isError = true;
		}
	      
	      if(!validateCitizenAge('<s:text name="citizenage.lbl"/>'))
	       isError = true;
	         
	      
	    if(!validatePlaceOfDeath())
	      return false;
	       
	     if(!validateInformant())
	      return false; 
	      
	    if(!validateCitizen(INFORMANTCITIZEN,'<s:text name="informant.lbl"/>'))
	      return false;
	      	    
	   
	      
	    if(!validateUsualAddress())
	     return false;
	     
	    if(!checkInformants())
	      return false;
	      
	     if(!validateRelationtype())
	      return false; 
	      
	      if(!validateAddictions())
	      return false; 
	       
	      if(!validateAgeType())
	      return false;
	      
	       	 var confirmFlag= true;
	  	 //modify mode..
	  		  	 if(populate("mode").value=="edit" || populate("mode").value=="modify"){
	  	     compareWithOldValue();
	  	     if(changedString!=""){
	  	          alert(changedString);
				  confirmFlag = confirm("Do you want to Continue ?");
				  if(!confirmFlag)
				  changedString="";
				  populate("remarksHistory").value=changedString;
			}
	  	 }
	  
	  if(confirmFlag){	 
	  	  if(populate("workFlowType")!=null)
	        populate("workFlowType").value = obj;    		
	  		
	  	  if(!approverInfo())
	    	return false;
	    		
	    populate("registrationUnit").disabled=false;	    	
	   	return true;
	   }  
	      
	  return false;
	}
	
	
	function validateAgeType(){
	
	if(populate("ageType").options[populate("ageType").options.selectedIndex].text == "Year")
	{
		if(populate("age").value < 1 ||populate("age").value>130)
		{
			warn(" Age in year, range should be within 1 to 130");
			  populate("age").focus();
			return false;
		}
	
	}
	else if(populate("ageType").options[populate("ageType").options.selectedIndex].text == "Month")
	{
		if(populate("age").value < 1 || populate("age").value > 11)
		{
			warn(" Age in Month, range should be within  1 to 11");
			 populate("age").focus();
			return false;
		}
	
	}
	else if(populate("ageType").options[populate("ageType").options.selectedIndex].text == "Day")
	{
		if(populate("age").value  < 1 || populate("age").value  > 30)
		{
			warn("Age in days, range should be within  1 to 30");
			 populate("age").focus();
			return false;
		}
	}
	else if(populate("ageType").options[populate("ageType").options.selectedIndex].text == "Hour")
	{
		if(populate("age").value < 1 || populate("age").value > 23)
		{
			warn("Age in hours, range should be within  1 to 23");
			 populate("age").focus();
			return false;
		}
	
	}
	return true;
	
	}
	
	
	function imposeMaxLength(Object, MaxLen)
{
   if(Object.value.length <= MaxLen){
   return true;
   }else{
    warn('Maximum limit for this field is '+MaxLen);
   return false;
}	
}
	
	function approverInfo()
  	{
		<s:if test="%{getNextAction()!='END'}">
			if(document.getElementById('workFlowType').value=='Approve' && document.getElementById('approverPositionId').value=="-1")
			{
				alert("<s:text name='approver.validate'/> ");
				return false;
			}
			if(document.getElementById('workFlowType').value=='Forward' && document.getElementById('approverPositionId').value=="-1")
			{
				alert("<s:text name='approver.validate'/> ");
				return false;
			}
		</s:if>
		return true;
	}
	
     function validateAddictions(){ 
     dom.get("deathRegistration_error").style.display = ''; 
     var x;
     var i=0;
     for(x=0;x<4;x++){
     if(document.getElementById('noOfYears'+'['+x+']').value!=""||document.getElementById('noOfYears'+'['+x+']').value!=0){
     if(!checkfornumber(document.getElementById('noOfYears'+'['+x+']').value)){
     i=i+1; 
   
     }
     }
     }
      if(i>0){
      warn('Enter a valid number for Addictions');
      return false;
      }
      else {
        return true;
	}
	}
	
	function checkfornumber(i){

	if(isNaN(i))
	return false;
	
	else if(i<0)
	return false;
	
	else return true;
	
	}
	
	  function warn(msg)
	  {		 
	      dom.get("deathRegistration_error").style.display = '';
	      populate("deathRegistration_error").innerHTML = msg;
	      return false;		
	  }
	  
	  
	  
	  	function populateInformant()
    {
    	makeEmptyCitizen(INFORMANTCITIZEN,false);
    	makeEnableCitizen(INFORMANTCITIZEN,"false");
        var citizen = "";	
		 if (getSelectedInformantType()=="MOTHER"){
			citizen = MOTHER;
			if(!validateCitizen(citizen,'<s:text name="mother.lbl"/>')){
			  populate("relationType").value="";
		       return false;
		       }
		     else
		        populateInformantDetails('mother');
		       
		}
		
		else if(getSelectedInformantType()=="FATHER"){
			
		  	citizen = FATHER;
		  	validateRelation(citizen);
	      	return true;
		}
		
		else if(getSelectedInformantType()=="HUSBAND"){
			
		  	citizen = HUSBAND;
		  	validateRelation(citizen);
	      		return true;
		}
		
		
		
		return true;
	}
	
	
	function populateInformantDetails(citizen)
	{
		
		if(citizen!="")
		{
		   
			populate("informantCitizen.firstName").value =  populate(citizen+".firstName").value;
			populate("informantCitizen.middleName").value = populate(citizen+".middleName").value;
			populate("informantCitizen.lastName").value =   populate(citizen+".lastName").value;
			makeReadOnlyCitizen(INFORMANTCITIZEN);
		}
		else
		{
		   makeEmptyCitizen(INFORMANTCITIZEN);
		   makeEnableCitizen(INFORMANTCITIZEN);
		}
		return true;
	}
	
	
		function getSelectedInformantType()
		
	{
		var informantType = document.getElementById("relationType").options[document.getElementById('relationType').selectedIndex].text;	
	
		return informantType;

	}
	
	
			 
	function validateRelation(citizen)
	{	
	
		var relation= document.getElementById("deceasedrelationType").options[document.getElementById('deceasedrelationType').selectedIndex].text;		
		
		if(relation=="FATHER" && citizen=="father")
		populateInformantDetails('deceasedrelation');
  
	    else if(relation=="HUSBAND" && citizen=="husband")
	    {
	      populateInformantDetails('deceasedrelation');
	    }
	  
	}
	
	
	function validateUsualAddress(){
	
	var address='deceasedUsualAddress';
	var addresslbl="Residence of the Deceased"
	if(populate("deceasedUsualAddress.cityTownVillage").value!=""||populate("deceasedUsualAddress.district").value!=""||
	populate("deceasedUsualAddress.state").value!=-1){
	
	  if(populate("deceasedUsualAddress.cityTownVillage").value=="")
	       {
	          warn(addresslbl+" "+'<s:text name="cityTownVillage.required"/>');
			  return false;
	       }
	       if(populate("deceasedUsualAddress.district").value=="")
	       {
			  warn(addresslbl+" "+'<s:text name="district.required"/>');
			  return false;
	       }
	       if(populate("deceasedUsualAddress.state").value==-1)
	       {
			  warn(addresslbl+" "+'<s:text name="state.required"/>');
			  return false;
	       }
	    return true;   	
	}
	return true;
	}
	
	
	function checkAge(){
	
	 populate("age").className="";
	  populate("age").readOnly = false;
	 var agetypes=document.getElementById("ageType").options[document.getElementById('ageType').selectedIndex].text;
	 if(agetypes=="Not Stated"){
	  populate("age").value="";
	   populate("age").readOnly = true;
	 populate("age").className="readonlycss";
	 }
	}
	
	function changeRelationnames(){
	
	var deceasedrelationtype= document.getElementById("deceasedrelationType").options[document.getElementById('deceasedrelationType').selectedIndex].text;		
	var relationtype = document.getElementById("relationType").options[document.getElementById('relationType').selectedIndex].text;	

		if(relationtype=='MOTHER'){
		  
		    populate("informantCitizen.firstName").value =  populate(MOTHER+".firstName").value;
			populate("informantCitizen.middleName").value = populate(MOTHER+".middleName").value;
			populate("informantCitizen.lastName").value =   populate(MOTHER+".lastName").value;
			makeReadOnlyCitizen(INFORMANTCITIZEN,"false");
		}
		
		else if(document.getElementById("relationType").value!=""&&document.getElementById("deceasedrelationType").value!="-1"&&relationtype==deceasedrelationtype){
		
		    populate("informantCitizen.firstName").value =  populate(DECEASEDRELATION+".firstName").value;
			populate("informantCitizen.middleName").value = populate(DECEASEDRELATION+".middleName").value;
			populate("informantCitizen.lastName").value =   populate(DECEASEDRELATION+".lastName").value;
			makeReadOnlyCitizen(INFORMANTCITIZEN,"false");
		}
		 
	}
	
	
	
	function checkInformants(){
	
	var deceasedrelationtype= document.getElementById("deceasedrelationType").options[document.getElementById('deceasedrelationType').selectedIndex].text;		
	var relationtype = document.getElementById("relationType").options[document.getElementById('relationType').selectedIndex].text;	
		
		if(relationtype=="MOTHER"){
		  if(!checkInformantDetails(MOTHER))
		  return false;
		
		}
		
		if(deceasedrelationtype=="FATHER" && relationtype=="FATHER"){
		   if(!checkInformantDetails(DECEASEDRELATION))
		   return false;
		}
  
	    if(deceasedrelationtype=="HUSBAND" && relationtype=="HUSBAND"){	    
	      if(!checkInformantDetails(DECEASEDRELATION))
	       return false;
	    }
	
	return true;
	}
	
	function checkInformantDetails(ENTITY){
	
	       if(populate(ENTITY+".firstName").value!= populate(INFORMANTCITIZEN+".firstName").value){ 
	         warn('<s:text name="informant.relative.msg"/>')
	         return false;
	         }
			if(populate(ENTITY+".middleName").value!= populate(INFORMANTCITIZEN+".middleName").value){
			   warn('<s:text name="informant.relative.msg"/>')
			   return false;
			   }
			if(populate(ENTITY+".lastName").value != populate(INFORMANTCITIZEN+".lastName").value){
			   warn('<s:text name="informant.relative.msg"/>')
			   return false;
			   }
	     return true;
	}
	
	  function populateEventAddress(obj)
  {
  	   var date= null;
  	    var eventyear=null;
    if(populate('dateOfEvent').value==""){  
       warn("Please Enter Date of Death");
       	populate('establishment').value="-1";
     return false;
  
  }else{
  	 date= populate('dateOfEvent').value;
  	 eventyear=date.substr(6,4);
  }
     if(obj.value!=-1){
        var url = "${pageContext.request.contextPath}/common/ajaxCommon!populateAddressByEstablishment.action?establishmentId="+obj.value+"&eventyear="+eventyear;
  	 	var req = initiateRequest();
  	 	req.open("GET", url, false);
		req.send(null);
  	 	if(req.readyState == 4)
  		{
  		    if (req.status == 200)
  		    {
  			    	var responseString =req.responseText; 			    
  			    	if(responseString=='HOSPITALREGISTERED'){
  					populate('establishment').value="-1";
  					makeEmptyAddress(EVENTADDRESS);
  					makeEmptyAddress(INFORMANTADDRESS);
  					makeEmptyCitizen(INFORMANTCITIZEN,false);
  					alert('For the selected hospital certificate cannot be registered here it has to be registered in the hospital where the death occurred');
  					}else{
  			        var result = responseString.split("+");
  			    	//populate(EVENTADDRESS+".addressID").value=result[0];
  			    	populate(EVENTADDRESS+".streetAddress1").value=result[1];
  			    	populate(EVENTADDRESS+".streetAddress2").value=result[2];
  			    	populate(EVENTADDRESS+".taluk").value=result[3];
  			    	populate(EVENTADDRESS+".cityTownVillage").value=result[4];
  			    	populate(EVENTADDRESS+".pinCode").value=result[5];
  			    	populate(EVENTADDRESS+".district").value=result[6];
  			    	populate(EVENTADDRESS+".state").value=result[7];
  			    	populate(INFORMANTADDRESS+".streetAddress1").value=result[1];
  		            populate(INFORMANTADDRESS+".streetAddress2").value=result[2];
  					populate(INFORMANTADDRESS+".taluk").value=result[3];
  					populate(INFORMANTADDRESS+".cityTownVillage").value=result[4];
  					populate(INFORMANTADDRESS+".pinCode").value=result[5];
  					populate(INFORMANTADDRESS+".district").value=result[6];
  					populate(INFORMANTADDRESS+".state").value=result[7];
  					populate("informantCitizen.firstName").value=populate('establishment').options[populate('establishment').selectedIndex].text;
  					populate("informantCitizen.middleName").value="";
  					populate("informantCitizen.lastName").value="";
  					makeReadOnlyAddress(INFORMANTADDRESS);
  					populate("relationType").disabled=true;
  		
  		}
  		}
  	}
  	}
  	
  	else
  	    makeEmptyAddress(EVENTADDRESS);
  	    
  }
  
      function reatinPlaceType()
    {
         var placeType = getPlaceTypeValue();
      
    	if(placeType == '<s:property value="@org.egov.bnd.utils.BndConstants@NOTSTATED"/>')
		{
			
		     populate("hospitalType").disabled = true;
		     populate("establishment").disabled = true;
		     populate("hospitalType").value = "-1";
		     populate("establishment").value = "-1";
		     populateDefaultAddress(EVENTADDRESS,'<s:property value="@org.egov.bnd.utils.BndConstants@EVENTADDRESS"/>');
		     makeReadOnlyAddress(EVENTADDRESS);
		     
		}
		else if(placeType =='<s:property value="@org.egov.bnd.utils.BndConstants@HOSPTIAL"/>')
		{
		   	makeReadOnlyAddress(EVENTADDRESS);
			populate("hospitalType").disabled = false;
		    populate("establishment").disabled = false;
		     disableInformant();
		}
		else
		{
			makeEnableAddress(EVENTADDRESS);
			populate("hospitalType").disabled = true;
		    populate("establishment").disabled = true;
		    populate("hospitalType").value = "-1";
		    populate("establishment").value = "-1";
		  
		}
    }
    
    	function disableInformant()
	{
	    populate("relationType").disabled = true;
	     makeReadOnlyCitizen(INFORMANTCITIZEN,"false");
	     makeReadOnlyAddress(INFORMANTADDRESS);
	}
    
    function validateRelationtype(){
    
     var placeType = document.getElementsByName("placeTypeTemp");
		var count; 
		for(count=0;count<placeType.length;count++)
		{
		    if(placeType[count].checked )
		    {   	
		         if(placeType[count].value!='<s:property value="@org.egov.bnd.utils.BndConstants@HOSPTIAL"/>'){
		      		  if(populate("relationType").value=="-1"){
		      		  warn("Relation of the Informant to the Deceased is Mandatory");
		      		  return false;
		      		  }
		        }
		   }
		}
    
    if(populate("deceasedrelationType").value!="-1"){
    
    	 if(!validateCitizen(DECEASEDRELATION,'<s:text name="deceased.lbl"/>'))
	      return false;
      }
    return true;
    
    }
    
    
     function disablePlaceType()
    {
        var placeType = document.getElementsByName("placeTypeTemp");
		var placeTypeValue ="";
		for(count=0;count<placeType.length;count++)
		{
		    placeType[count].disabled=true;
		}
		return placeTypeValue;
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
    
    function isDeathRegNumUnique()
    {
   		getDeathRegNumberRangeJSON();
    }
    
    
       
  /**
   *   This Api is to do ajax call for getting birth registration number range.
   *   Internally It checks for uniqueness of registration number.
   */
    
  function getDeathRegNumberRangeJSON()
  {
      if(populate("mode").value=='offline' &&  HOSPITALUSERFLAG !=1 ){
         var regNumber = trimAll(populate("registrationNo").value);
         var deathDate = "";
        if(NUMBERGENKEY == '<s:property value="@org.egov.bnd.utils.BndConstants@REGISTRATIONDATE"/>')
          	deathDate = populate("registrationDate").value;
          else
            deathDate = populate("dateOfEvent").value;
         var id = populate('id').value;
         if(isNum(regNumber) && deathDate!=""){
        	var regUnit = populate("registrationUnit").value;
        	makeJSONCall(["minValue","maxValue","uniqueRegFlag"], 
		    	'${pageContext.request.contextPath}/common/ajaxCommon!populateDeathRegNumRange.action',
		    	 {regUnit:regUnit,deathDate:deathDate,regNo:regNumber,deathRegid : id},myDeathNumRangeSuccessHandler,myDeathNumRangeFailureHandler) ;
  	     }
  	     else
  	      return true;
  	   }
  	   else
  	     return true;
  }
  
  myDeathNumRangeSuccessHandler = function(req,res){
	    var resultdatas=res.results;
	    var minValue=resultdatas[0].minValue;
	    var maxValue=resultdatas[0].maxValue;
	    var uniqueFlag=resultdatas[0].uniqueRegFlag;
	    dom.get("deathRegistration_error").style.display='none';
	    if(uniqueFlag=="true")
	    {
	       warn('<s:text name="registration.number.exists"/>');
	       populate("registrationNo").value=""; 
	       return false;
	    }
	    else
	    {
	       var regNumber = trimAll(populate("registrationNo").value);
	       if(regNumber>=eval(minValue))
	  	    {
	  			 var msg = '<s:text name="regNumber.minvalue.validate"/>'+" "+minValue;
	  			 warn(msg);
	  			 populate("registrationNo").value="";  
	  			 return false;
	  		}
	    }
	    return true;
    }
  
    myDeathNumRangeFailureHandler = function(){
	    alert("Unable to get death registration number range..");
	    return false;
    }  
    
    
    function validateDelayedRegistration()
    {

       
		var eventDate = populate("dateOfEvent").value;
		var registrationDate = populate("registrationDate").value;
		var isMainUnit = populate("ismainregunit").value;
		
		if (eventDate!="" && registrationDate!="" && (DEATHDELAYEDFLAG==0 || isMainUnit == "false") )
		{
		    var noOfDays = new Date(formatDateBnd(registrationDate)) - new Date(formatDateBnd(eventDate));
		   	if (noOfDays> (365 * 24 * 3600 * 1000))
			{
				warn('<s:text name="delayed.reg.invalid"/>' );
				populate("dateOfEvent").value = "";
				return false;
			}
		}
		
		if(registrationDate!="")
		{
		   var todays = getTodayDate();
		   if(compareDate(todays,registrationDate)>0)
		   {
		       warn('<s:text name="registrationDate.todays.validate"/>');
		       populate("registrationDate").value = "";
		       return false;
		   }
		   
		}
		return true;
 	 }
   
    
 	function formatDateBnd(strDate)
   {
	    var dtParts = strDate.split("/");
	    strDay = dtParts[0];
	    strMonth = dtParts[1];
	    strYear = dtParts[2];
	    strFormattedDate = strMonth + "/" + strDay + "/" + strYear;
		dtEvent = Date.parse(strFormattedDate);
		return strFormattedDate;
   } 
   
   
   function validateDeathdate()
   {
       
       if(populate("dateOfEvent").value!="" && compareDate(getTodayDate(),populate("dateOfEvent").value)>0){
       		warn('<s:text name="dateOfDeath.today.validate"/>');
       		populate("dateOfEvent").value="";
       		return false;  
       }
       if(populate("registrationDate").value!="" && populate("dateOfEvent").value!=""
          			&& compareDate(populate("registrationDate").value,populate("dateOfEvent").value)>0){
       		warn('<s:text name="dateOfDeath.registration.validate"/>');
       		populate("dateOfEvent").value="";
       		return false;  
       }
       
        if((HOSPITALREGISTRARFLAG ==1 || HOSPITALUSERFLAG == 1) && populate("dateOfEvent").value!=""){
            var today = getTodayDate();
            var currYear = getYearByDate(today);
            var eventYear = getYearByDate(populate("dateOfEvent").value);
            if(currYear != eventYear)
            {
               if(GRACEPERIOD == null || GRACEPERIOD == 0)
               {
               	  warn('<s:text name="registration.curryeardata"/>');
               	  populate("dateOfEvent").value="";
                  return false;
               }
               else 
               {
                var noOfDays = new Date(formatDateBnd(today)) - new Date(formatDateBnd(populate("dateOfEvent").value));
               	if ( noOfDays > (GRACEPERIOD * 24 * 3600 * 1000)){
                   warn('<s:text name="dateofevent.curryeardata.graceperiod"/>'+GRACEPERIOD+' '+'<s:text name="days.lbl"/>');
               	   populate("dateOfEvent").value="";
                   return false;
                 }
               }
            }
       }
       return true;
   }
   
     /**
   *     For modify mode , To show the values modified by the user
   */
  
  function compareWithOldValue()
  {
       if(populate("registrationDate").value!=populate("deathHistory.registrationDate").value)
           changedString = '<s:text name="registration.date"/>'+" is changed from "+populate("deathHistory.registrationDate").value+ " to "+populate("registrationDate").value+"'\n";
           
       if(populate("dateOfEvent").value!=populate("deathHistory.dateOfEvent").value)
           changedString = changedString+'<s:text name="dateOfbirth"/>'+" is changed from "+populate("deathHistory.dateOfEvent").value+ " to "+populate("dateOfEvent").value+"'\n";
           
       if(populate("citizen.sex").value!=populate("deathHistory.citizen.sex").value)
           changedString = changedString+'<s:text name="sex.lbl"/>'+" is changed from "+populate("deathHistory.citizen.sex").value+ " to "+populate("citizen.sex").value+"'\n";
       
     
       if(populate("deathHistory.deceasedrelationType.relatedAsConst").value!="")       
        if(getDropDownValue(populate("deceasedrelationType").value,"deceasedrelationType")!=populate("deathHistory.deceasedrelationType.relatedAsConst").value)
           changedString = changedString+'<s:text name="deceased.relation.lbl"/>'+" is changed from "+populate("deathHistory.deceasedrelationType.relatedAsConst").value+ " to "+getDropDownValue(populate("deceasedrelationType").value,"deceasedrelationType")+"'\n";
           
           if(getDropDownValue(populate("ageType").value,"ageType")!=populate("deathHistory.ageType.ageTypedesc").value)
           changedString = changedString+'<s:text name="age.lbl"/>'+" is changed from "+populate("deathHistory.ageType.ageTypedesc").value+ " to "+getDropDownValue(populate("ageType").value,"ageType")+"'\n";
       
         if(populate("age").value!=populate("deathHistory.age").value)
           changedString = changedString+'<s:text name="Age"/>'+" is changed from "+populate("deathHistory.age").value+ " to "+populate("age").value+"'\n";
         
             if(populate("deathHistory.crematorium.crematoriumconst").value!="") 
           if(getDropDownValue(populate("crematorium").value,"crematorium")!=populate("deathHistory.crematorium.crematoriumconst").value)
           changedString = changedString+'<s:text name="shamshan.name.lbl"/>'+" is changed from "+populate("deathHistory.crematorium.crematoriumconst").value+ " to "+getDropDownValue(populate("crematorium").value,"crematorium")+"'\n";
             
               
                     
       compareWithOldCitizen(CITIZEN,CITIZENHISTORY,CITIZEN);
       compareWithOldAddress(PERMANENTCITIZENADDRESS,PERMANENTCITIZENADDRESSHISTORY,'<s:text name="permanentAddress"/>');

        if(getPlaceTypeValue()!= populate("placeTypeTempDeathHistory").value)
           changedString = changedString+'<s:text name="place.of.birth.lbl"/>'+" is changed from "+populate("placeTypeTempDeathHistory").value+ " to "+getPlaceTypeValue()+"'\n";
        
        //need to check  

        if(getDropDownValue(populate("hospitalType").value,"hospitalType")!=populate("deathHistory.hospitalType").value && populate("hospitalType").value!=-1){
           changedString = changedString+'<s:text name="hospital.type.lbl"/>'+" is changed from "+populate("deathHistory.hospitalType").value+ " to "+populate("hospitalType").options[populate("hospitalType").selectedIndex].text+"'\n";
        }

        if(populate("establishment").value!=populate("deathHistory.establishment1").value && populate("establishment").value!=-1){
           changedString = changedString+'<s:text name="hospital.name.lbl"/>'+" is changed from "+getDropDownValue(populate("deathHistory.establishment1").value,"establishment")+ " to "+populate("establishment").options[populate("establishment").selectedIndex].text+"'\n";
        }
        
        compareWithOldAddress(EVENTADDRESS,EVENTADDRESSHISTORY,'<s:text name="address"/>');
        compareWithOldCitizen(MOTHER,MOTHERHISTORY,"Mother");
        compareWithOldCitizen(DECEASEDRELATION,DECEASEDRELATIONHISTORY,"Relative");
        compareWithOldAddress(DECEASEDADDRESS,DECEASEDADDRESSHISTORY,'<s:text name="deceased.address.lbl"/>');
        
         if(getSelectedInformantType()!=populate("deathHistory.relationType.relatedAsConst").value)
           changedString = changedString+'<s:text name="informat.details.lbl"/>'+" is changed from "+populate("deathHistory.relationType.relatedAsConst").value+ " to "+getSelectedInformantType()+"'\n";
        compareWithOldCitizen(INFORMANTCITIZEN,INFORMANTCITIZENHISTORY,'<s:text name="informat.name.lbl"/>');
        compareWithOldAddress(INFORMANTADDRESS,INFORMANTADDRESSHISTORY,"Informant Address");
  }
  
  
  //var changedCitizen
  
  function compareWithOldAddress(newAddress,oldAddress,addresslbl)
  {
  	  
       if(trimAll(populate(newAddress+".streetAddress1").value)!=trimAll(populate(oldAddress+".streetAddress1").value)){
          if(trimAll(populate(oldAddress+".streetAddress1").value)!="")
	   	  	changedString = changedString+addresslbl+" Street Address1 is changed from "+populate(oldAddress+".streetAddress1").value+ " to "+populate(newAddress+".streetAddress1").value+"'\n";
	   	  else
	   	    changedString = changedString+addresslbl+" Street Address1 is changed to "+populate(newAddress+".streetAddress1").value+"'\n";
	   }
	   
       if(trimAll(populate(newAddress+".streetAddress2").value)!=trimAll(populate(oldAddress+".streetAddress2").value)){
         if(trimAll(populate(oldAddress+".streetAddress2").value)!="")
	       changedString = changedString+addresslbl+" Street Address2 is changed from "+populate(oldAddress+".streetAddress2").value+ " to "+populate(newAddress+".streetAddress2").value+"'\n";
	     else
	        changedString = changedString+addresslbl+" Street Address2 is changed to "+populate(newAddress+".streetAddress2").value+"'\n";
	   }
	   
       if(trimAll(populate(newAddress+".taluk").value)!=trimAll(populate(oldAddress+".taluk").value)){
           if(trimAll(populate(oldAddress+".taluk").value)!="")
	       	 changedString = changedString+addresslbl+" taluk is changed from "+populate(oldAddress+".taluk").value+ " to "+populate(newAddress+".taluk").value+"'\n";
	       else
	         changedString = changedString+addresslbl+" taluk is changed to "+populate(newAddress+".taluk").value+"'\n";
	   }
	   
       if(trimAll(populate(newAddress+".cityTownVillage").value)!=trimAll(populate(oldAddress+".cityTownVillage").value)){
         if(trimAll(populate(oldAddress+".cityTownVillage").value)!="")
          changedString = changedString+addresslbl+" cityTownVillage is changed from "+populate(oldAddress+".cityTownVillage").value+ " to "+populate(newAddress+".cityTownVillage").value+"'\n";
         else
           changedString = changedString+addresslbl+" cityTownVillage is changed to "+populate(newAddress+".cityTownVillage").value+"'\n";
       }
       
       if(trimAll(populate(newAddress+".pinCode").value)!=trimAll(populate(oldAddress+".pinCode").value)){
          if(trimAll(populate(oldAddress+".pinCode").value)!="")
         	changedString = changedString+addresslbl+" pinCode is changed from "+populate(oldAddress+".pinCode").value+ " to "+populate(newAddress+".pinCode").value+"'\n";
       }
       
       if(trimAll(populate(newAddress+".district").value)!=trimAll(populate(oldAddress+".district").value)){
		 if(trimAll(populate(oldAddress+".district").value)!="")      
		  	changedString = changedString+addresslbl+" district is changed from "+populate(oldAddress+".district").value+ " to "+populate(newAddress+".district").value+"'\n";
		  else
		    changedString = changedString+addresslbl+" district is changed to "+populate(newAddress+".district").value+"'\n";
	   }
	   
       if(trimAll(populate(newAddress+".state").value)!=trimAll(populate(oldAddress+".state").value)){
       	   if(trimAll(populate(oldAddress+".district").value)!="")  
       		 changedString = changedString+addresslbl+" state is changed from "+getDropDownValue(populate(oldAddress+".state").value,PERMANENTCITIZENADDRESS+".state")+ " to "+populate(newAddress+".state").options[populate(newAddress+".state").selectedIndex].text+"'\n";
       	   else
       	      changedString = changedString+addresslbl+" state is changed to "+populate(newAddress+".state").options[populate(newAddress+".state").selectedIndex].text+"'\n";
       }
	   
  } 
  
  function compareWithOldCitizen(newCitizen,oldCitizen,citizenlbl)
  {
 
       if(trimAll(populate(newCitizen+".firstName").value)!=trimAll(populate(oldCitizen+".firstName").value)){
       	   if(trimAll(populate(oldCitizen+".firstName").value)!="") 
	      	 changedString = changedString+citizenlbl+" First Name is changed from "+populate(oldCitizen+".firstName").value+ " to "+populate(newCitizen+".firstName").value+"'\n";
	       else
	         changedString = changedString+citizenlbl+" First Name is changed to "+populate(newCitizen+".firstName").value+"'\n";
	   }
	   
       if(trimAll(populate(newCitizen+".middleName").value)!=trimAll(populate(oldCitizen+".middleName").value)){
           if(trimAll(populate(oldCitizen+".middleName").value)!="") 
	       	changedString = changedString+citizenlbl+" Middle Name is changed from "+populate(oldCitizen+".middleName").value+ " to "+populate(newCitizen+".middleName").value+"'\n";
	       else
	         changedString = changedString+citizenlbl+" Middle Name is changed to "+populate(newCitizen+".middleName").value+"'\n";
	   }
	   
       if(trimAll(populate(newCitizen+".lastName").value)!=trimAll(populate(oldCitizen+".lastName").value)){
           if(trimAll(populate(oldCitizen+".lastName").value)!="") 
	      	 changedString = changedString+citizenlbl+" Last Name is changed from "+populate(oldCitizen+".lastName").value+ " to "+populate(newCitizen+".lastName").value+"'\n";
	       else
	         changedString = changedString+citizenlbl+" Last Name is changed to "+populate(newCitizen+".lastName").value+"'\n";
	   }
	   
       if(populate(newCitizen+".mobilePhone")!=null && trimAll(populate(newCitizen+".mobilePhone").value)!=trimAll(populate(oldCitizen+".mobilePhone").value)){
          if(trimAll(populate(oldCitizen+".mobilePhone").value)!="")
	       changedString = changedString+citizenlbl+" Mobile Phone is changed from "+populate(oldCitizen+".lastName").value+ " to "+populate(newCitizen+".lastName").value+"'\n";
	      else
	        changedString = changedString+citizenlbl+" Mobile Phone is changed to "+populate(newCitizen+".lastName").value+"'\n";
	   }
	   
       if(populate(newCitizen+".emailAddress")!=null && trimAll(populate(newCitizen+".emailAddress").value)!=trimAll(populate(oldCitizen+".emailAddress").value)){
       	 if(trimAll(populate(oldCitizen+".emailAddress").value)!="")
	       changedString = changedString+citizenlbl+" Email Address is changed from "+populate(oldCitizen+".emailAddress").value+ " to "+populate(newCitizen+".emailAddress").value+"'\n";
	     else
	       changedString = changedString+citizenlbl+" Email Address is changed to "+populate(newCitizen+".emailAddress").value+"'\n";
	   }
  }
  
  function populateAddressHistory()
  {
       if(populate("deathHistory.permanentCitizenAddress.addressID").value=="")
          populateDefaultAddress(PERMANENTCITIZENADDRESSHISTORY);
       if(populate("deathHistory.eventAddress.addressID").value=="")
          populateDefaultAddress(EVENTADDRESSHISTORY);
       if(populate("deathHistory.deceasedAddress.addressID").value=="")   
          populateDefaultAddress(DECEASEDADDRESSHISTORY);
  }
  
  
    function validateAddressPincode(obj)
  {
        if(trimAll(obj.value)!=""){
  			if(!validatePincode(obj)){
  		    	warn('<s:text name="invalid.pincode"/>');
  		    	obj.value="";
		  		return false;
  			}
  		}
  		return true;
  }
  
    
  function getDropDownValue(value,dropDownObj)
  {

       var stateText="";
  	   for (var i = 0; i < populate(dropDownObj).options.length; i++)
  	   
      	  if(populate(dropDownObj).options[ i ].value==value){
	  			stateText=populate(dropDownObj).options[ i ].text;
	  			break;
	  	 }
	  	return stateText;
  }
  
  
  function enablestaticDetails(){
  
  var sex = document.getElementById("citizen.sex").value;	
  var mod=document.getElementById("mode").value;
  if(sex=="FeMale"&&mod!="view")
  document.getElementById("pregnancyRelated").disabled=false;
  else
  document.getElementById("pregnancyRelated").disabled=true;
  }

</script>
