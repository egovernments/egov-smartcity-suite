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


	//var PERMANENTCITIZENADDRESS="permanentCitizenAddress";
    var PERMANENTCITIZENADDRESSHISTORY ="birthHistory.permanentCitizenAddress";
    var EVENTADDRESSHISTORY = "birthHistory.eventAddress";
    var CITIZENHISTORY  ="birthHistory.citizen";
    var MOTHERHISTORY = "birthHistory.mother";
    var FATHERHISTORY ="birthHistory.father";
    var PARENTADDRESSHISTORY="birthHistory.parentAddress";
    var INFORMANTHISTORY = "informantCitizenBirthHistory";
    var INFORMANTADDRESSHISTORY = "birthHistory.informantAddress";

    var PERMANENTCITIZENADDRESS="permanentCitizenAddress";
	var CITIZEN="citizen";
	var EVENTADDRESS = "eventAddress";
	var MOTHER ="mother";
	var FATHER="father";
	var PARENTADDRESS="parentAddress";
	var INFORMANT = "informantCitizen";
	var INFORMANTADDRESS = "informantAddress";
	var NOTAVAILABLE = '<s:property value="@org.egov.bnd.utils.BndConstants@NOTAVAILABLE"/>';
	var HOSPITALUSERFLAG = '<s:property value="%{birthHospitalUserFlag}"/>';
	var HOSPITALREGISTRARFLAG = '<s:property value="%{birthHospitalRegistrarFlag}"/>';
	var BIRTHADOPTIONFLAG = '<s:property value="%{birthAdoptionFlag}"/>';
	var BIRTHDELAYEDFLAG ='<s:property value="%{birthDelayedFlag}"/>';
	var changedString = "";
	var BIRTHSTATISTICSINFOFLAG ='<s:property value="%{birthStatisticsInfoFlag}"/>';
	var GRACEPERIOD = '<s:property value="%{gracePeriod}"/>';
	var NUMBERGENKEY = '<s:property value="%{numberGenKey}"/>';
	
/**
 *  This method is called on load of the page
 */
   
   function loadDesignationFromMatrix()
    {
       var currentState=document.getElementById('currentState').value;
       var amountRule=document.getElementById('amountRule').value;
       var additionalRule=document.getElementById('additionalRule').value;
       var pendingActions=document.getElementById('pendingActions').value;   
      // var dept=document.getElementById('dept').options[document.getElementById('dept').selectedIndex].text;
       loadDesignationByDeptAndType('StillBirthRegistration',null,currentState,amountRule,additionalRule,pendingActions); 
    }
	
	function populateApprover()
	{
		getUsersByDesignationAndDept();
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
	
	
	function validateDelayedRegistration()
    {
		var eventDate = populate("dateOfEvent").value;
		var registrationDate = populate("registrationDate").value;
		var isMainUnit = populate("ismainregunit").value;
		
		if (eventDate!="" && registrationDate!="" && (BIRTHDELAYEDFLAG==0 || isMainUnit == "false") )
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


	/**
	 *  This method is called to retain place type value on load of the page
	 */
	 
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
		     enableInformant();
		}
		else if(placeType =='<s:property value="@org.egov.bnd.utils.BndConstants@HOSPTIAL"/>')
		{
		   	makeReadOnlyAddress(EVENTADDRESS);
		   	if(populate("mode").value!="notmodify"){
				populate("hospitalType").disabled = false;
		    	populate("establishment").disabled = false;
		    }
		    disableInformant();
		}
		else
		{
			makeEnableAddress(EVENTADDRESS);
			populate("hospitalType").disabled = true;
		    populate("establishment").disabled = true;
		    populate("hospitalType").value = "-1";
		    populate("establishment").value = "-1";
		    enableInformant();
		}
    }
    
    
    
	function bodyOnLoad()
	{
	     
	    var isMainUnit = populate("ismainregunit").value;
	     if(populate("registrationMode").value=='online'&&isMainUnit=='false')
	     populate("registrationDate").disabled=true;
	     else
	     populate("registrationDate").disabled=false;
	     
		  if(populate("mode").value=="edit" || populate("mode").value=="modify")
	      {
	          populate("registrationNo").readOnly = true;
	          populate("dateOfEvent").disabled = true;
	      }
	      
	     // For Inbox it decides whether form is editable or not
	     if(populate("mode").value == "notmodify")
	     {
	   		for (var i =0; i < document.forms[0].length;i++) {
				if (document.forms[0].elements[i].name != 'close' 
					   && document.forms[0].elements[i].id != 'Approve'
				       && document.forms[0].elements[i].id != 'save'
				       && document.forms[0].elements[i].id != 'Reject' 
				       && document.forms[0].elements[i].id != 'approverComments')
					document.forms[0].elements[i].disabled = true;
					
			}
	     }
	     
	     
	      reatinPlaceType();
		 if(HOSPITALUSERFLAG ==1 || HOSPITALREGISTRARFLAG==1)
		 {
		    populate("hospitalType").disabled=true;
		    populate("establishment").disabled=true;
		    disablePlaceType();
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
    
    
    /**
     *  This method is to validate child date of birth
     */
     
    function validateChildDob()
    {
       if(populate("dateOfEvent").value!="" && compareDate(getTodayDate(),populate("dateOfEvent").value)>0){
       		warn('<s:text name="dateOfEvent.today.validate"/>');
       		populate("dateOfEvent").value="";
       		return false;  
       }
       if(populate("registrationDate").value!="" && populate("dateOfEvent").value!=""
          			&& compareDate(populate("registrationDate").value,populate("dateOfEvent").value)>0){
       		warn('<s:text name="dateOfEvent.registration.validate"/>');
       		populate("dateOfEvent").value="";
       		return false;  
       }
       
       //For Hospital user or hospital registrar date of event should be in current year...
       // or date of event should be with in the grace period..... 
       
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
    
    
    function isBirthRegNumUnique()
    {
       getBirthRegNumberRangeJSON();
    }
    
    
    /**
     *   This Api is to do ajax call for getting birth registration number range.
     *   Internally It checks for uniqueness of registration number
     */
    
  function getBirthRegNumberRangeJSON()
  {
      if(populate("mode").value=='offline' &&  HOSPITALUSERFLAG !=1 ){
         var regNumber = trimAll(populate("registrationNo").value);
         var birthDate = "";
         
         if(NUMBERGENKEY == '<s:property value="@org.egov.bnd.utils.BndConstants@REGISTRATIONDATE"/>')
          	birthDate = populate("registrationDate").value;
          else
            birthDate = populate("dateOfEvent").value;
            
         var id = populate('id').value;
         if(isNum(regNumber) && birthDate!=""){
        	var regUnit = populate("registrationUnit").value;
        	makeJSONCall(["minValue","maxValue","uniqueRegFlag"], 
		    	'${pageContext.request.contextPath}/common/ajaxCommon!populateStillBirthNumRange.action',
		    	 {regUnit:regUnit,birthDate:birthDate,regNo:regNumber,birthRegid : id},myBirthNumRangeSuccessHandler,myBirthNumRangeFailureHandler);
  	     }
  	     else
  	      return true;
  	   }
  	   else
  	     return true;
  }
  
  myBirthNumRangeSuccessHandler = function(req,res){
	    var resultdatas=res.results;
	    var minValue=resultdatas[0].minValue;
	    var maxValue=resultdatas[0].maxValue;
	    var uniqueFlag=resultdatas[0].uniqueRegFlag;
	    dom.get("birthRegistration_error").style.display='none';
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
  
  myBirthNumRangeFailureHandler = function(){
	    alert("Unable to get birth registration number range..");
	    return false;
  }  

/**
 *  This method is to validate the form
 */

	function validateForm(obj)
	{
		dom.get("birthRegistration_error").style.display = 'none';
		var isError = false;
		
	 	if(!validateForm_stillBirthRegistration()){
	  	  isError = true;
		}
		
	 	if(!validateChildPlaceOfBirth())
	     isError = true;
	     
	     if(BIRTHSTATISTICSINFOFLAG==1){
		 	if(!validateStatisticsInfo())
		  		isError= true;
		}
		
		if(!validateChildDob())
		{ 
		   isError = true;
		   return false;
		}
		
		if(isError)
	  	  return false;
	  	 
	  	 var confirmFlag= true;
	  	 //modify mode..
	  	 if(populate("mode").value=="edit" || populate("mode").value=="modify"){
	  	     compareWithOldValue();
	  	     if(changedString!=""){
	  	          alert(changedString);
				  confirmFlag = confirm("Do you want to Continue ?");
				  populate("remarksHistory").value=changedString;
			}
	  	 }
	  
	  if(confirmFlag){	 
	    	if(populate("workFlowType")!=null)
	        	populate("workFlowType").value = obj;  
	        	
	        if(!approverInfo())
	    		return false;
	        
	  		populate("registrationUnit").disabled=false;
	    	
	   }
	   else
	       return false;  
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


	/*Town/Village: or District: is selected in PART -2 (Statistical Information) then state is mandatory*/
	function validateStatisticsInfo()
	{
		var error = "Required";
		var isError = false;
		if(populate("motherResidenceAddress.cityTownVillage").value!="" || populate("motherResidenceAddress.district").value!="" ){
			if(populate("motherResidenceAddress.state").value=="-1"){
			 addError(populate("motherResidenceAddress.state"),error);
			 isError = true;
			}
		}
		if(isError)
	      return false;
	    else
	      return true;
	}


	function populateCitizenName()
	{
		var childNameFlag = document.getElementsByName("nameOfchildFlag");
	            
		if(childNameFlag[1].checked)
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
	
	
	function validateChildPlaceOfBirth()
	{
	   
	    var isError = false;
	    var error = "Required";
		var placeType = getPlaceTypeValue();
		if(placeType == "")
	    {
		   warn('<s:text name="PlaceOfBirth.required"/>');	       
	       return false;
	    }
	    if(placeType =='<s:property value="@org.egov.bnd.utils.BndConstants@HOSPTIAL"/>')
	    {
	        if(populate("hospitalType").value=="-1")
	         {
	            addError(populate("hospitalType"), error);
	            isError = true;
	         }
	         
	         if(populate("establishment").value=="-1")
	         {
               addError(populate("establishment"), error);
              isError = true;
	         }  
	    }
	    if(isError)
	        return false;
	    return true;
	    
	}
	
	function disableInformant()
	{
	     var informantFlag = document.getElementsByName("informantFlag");
	     for(count=0;count<informantFlag.length;count++)
	     {
		   informantFlag[count].disabled=true;
		  // if(HOSPITALUSERFLAG!=1)
		   //	informantFlag[count].checked=false;
		 }
	     makeReadOnlyCitizen(INFORMANT,"false");
	     makeReadOnlyAddress(INFORMANTADDRESS);
	}
	
	function enableInformant()
	{
	   makeEnableAddress(INFORMANTADDRESS);
	   makeEnableCitizen(INFORMANT,"false");
	}
		
	function populateInformantHospitalOn()
	{
	    makeEmptyCitizen(INFORMANT,"false");
	    makeEmptyAddress(INFORMANTADDRESS);
	    disableInformant();
	}
	
	function populateInformantHospitalOff()
	{
	     var informantFlag = document.getElementsByName("informantFlag");
	     for(count=0;count<informantFlag.length;count++)
	     {
		   informantFlag[count].disabled=false;
		   informantFlag[count].checked=false;
		 }
	     makeEmptyCitizen(INFORMANT,"false");
	     makeEnableCitizen(INFORMANT,"false");
	     makeEmptyAddress(INFORMANTADDRESS);
	     makeEnableAddress(INFORMANTADDRESS);
	}
	
	/**
	 *  This API is to populate the informat details.
	 */
	 
	function populateInformant()
    {
        var citizen = "";
        form = document.getElementById("stillBirthRegistration");
		clearErrorMessages(form);
        clearErrorLabels(form);
		makeReadOnlyCitizen(INFORMANT);
		if(getSelectedInformantType()=='<s:property value="@org.egov.bnd.utils.BndConstants@FATHER"/>'){
		  	citizen = FATHER;
		  	if(!validateCitizen(citizen,'<s:text name="father.lbl"/>')){
	           //  obj.checked = false;	  	
	           makeEmptyCitizen(INFORMANT,"false");
	      		 return false;
	      	}
	      	populateInformantDetails(citizen);
		}
		else if (getSelectedInformantType()=='<s:property value="@org.egov.bnd.utils.BndConstants@MOTHER"/>'){
			citizen = MOTHER;
			if(!validateCitizen(citizen,'<s:text name="mother.lbl"/>')){
			  // obj.checked = false;
			  makeEmptyCitizen(INFORMANT,"false");
		       return false;
		    }
		    populateInformantDetails(citizen);
		}
		else
		{
		    if(HOSPITALUSERFLAG == 0 && HOSPITALREGISTRARFLAG == 0)
		    {
		       var placeType = getPlaceTypeValue();
		       if(placeType !='<s:property value="@org.egov.bnd.utils.BndConstants@HOSPTIAL"/>'){
		       		makeEmptyCitizen(INFORMANT,"false");
		       	    makeEnableCitizen(INFORMANT,"false");
		       }
		    }
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
			makeReadOnlyCitizen(INFORMANT);
		}
		else
		{
		   makeEmptyCitizen(INFORMANT);
		   makeEnableCitizen(INFORMANT);
		}
		return true;
	}
	
	
	function getSelectedInformantType()
	{
		var informantType = document.getElementsByName("informantFlag");
		var selectedInformantType = -1;
	
		for (_i=0; _i< informantType.length; _i++)
		{
			if (informantType[_i].checked)
			{
				selectedInformantType = informantType[_i].value;
				break;
			}
		}
	
		return selectedInformantType;

	}
	
	
	/**
	 *    This API is to validate the citizen
	 */
	 
	function validateCitizen(citizen,citizenlbl)
	{
		
	    if(trimAll(populate(citizen+".firstName").value)=="")
	    {
	        addError(populate(citizen+".firstName"),error);
	        return false;
	    }
	    return true;
	}

/**
 *  This API is to populate child permanent Address
 */
 
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
    
    
	function clearHospitalDetails()
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
		     populateInformantHospitalOff();
		}
		else if(placeType =='<s:property value="@org.egov.bnd.utils.BndConstants@HOSPTIAL"/>')
		{
		    makeEmptyAddress(EVENTADDRESS);
			makeReadOnlyAddress(EVENTADDRESS);
			populate("hospitalType").disabled = false;
		    populate("establishment").disabled = true;
		    populateInformantHospitalOn();
		}
		else
		{
		    makeEmptyAddress(EVENTADDRESS);
			makeEnableAddress(EVENTADDRESS);
			populate("hospitalType").disabled = true;
		    populate("establishment").disabled = true;
		    populate("hospitalType").value = "-1";
		    populate("establishment").value = "-1";
		    populateInformantHospitalOff();
		}
			
	}
 

  function populate(objName)
  {
     return document.getElementById(objName);
  }
  
  /**
   *   This API is to get Default state value
   */
   
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
		  populate(address+".state").value ="";
		  //alert("Address--->"+(address+".state")+"AAAA---->"+populate(address+".state").value);
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
	     // populate(address+".addressID").value="";
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
	    
	/* function validateAddress(address,addresslbl)
	  {
	       if(populate(address+".streetAddress1").value=="")
	       {
	          warn(addresslbl+" "+'<s:text name="streetAddress1.required"/>');
			  return false;
	       }
	       if(populate(address+".cityTownVillage").value=="")
	       {
	          warn(addresslbl+" "+'<s:text name="cityTownVillage.required"/>');
			  return false;
	       }
	       if(populate(address+".district").value=="")
	       {
			  warn(addresslbl+" "+'<s:text name="district.required"/>');
			  return false;
	       }
	       if(populate(address+".state").value=="-1")
	       {
			  warn(addresslbl+" "+'<s:text name="state.required"/>');
			  return false;
	       }
	       return true;
	  }*/
	  
	  function warn(msg)
	  {
	      dom.get("birthRegistration_error").style.display = 'block';
		  populate("birthRegistration_error").innerHTML = msg;
	  }
	  
  
   
   /**
    *   This is to disable all the adoption details
    */
    
  
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
  
   
  function validatePhoneno(obj)
  {
      if(trimAll(obj.value)!=""){
  			if(!validatePhoneNo(obj.value)){
  		    	warn('<s:text name="invalid.phoneno"/>');
  		    	obj.value="";
		  		return false;
  			}
  		}
  		return true;
  }
  
  
  
  function populateHospitalName()
  {
  	  enableHospitalName();	
      populateestablishment({hospitalType:populate('hospitalType').value,regUnit:populate('registrationUnit').value});
  }


  /* only on select of hospitalType enable establishment*/
  function enableHospitalName()
  {
  		if(populate('hospitalType').value  == "-1"){
  		  populate("establishment").value="-1";
  		  populate("establishment").disabled = true;	
  		}else{
  		  populate("establishment").disabled = false;	
  		}
  }
  
  function resetHospitalTypeAndName()
  {
       populate('hospitalType').value  = "-1";
       populate('establishment').value = "-1";
  }
  
  
  function populateEventAddress(obj)
  {
	   var date= null;
 	    var eventyear=null;
   if(populate('dateOfEvent').value==""){  
      warn("Please Enter Date of Death");
  	populate('establishment').value="-1";
    return false;
    }
    else{
     date= populate('dateOfEvent').value;
 	  eventyear=date.substr(6,4);
     if(obj.value!=-1){
        makeJSONCall(["addressID","streetAddress1","streetAddress2","taluk","cityTownVillage","pinCode","district","state","hospitalType"], 
		    	'${pageContext.request.contextPath}/common/ajaxCommon!populateAddressByEstablishmentJson.action',
		    	{establishmentId:obj.value,eventyear:eventyear},myAddressSuccessHandler,myAddressFailureHandler) ;
  	}
  	else
  	    makeEmptyAddress(EVENTADDRESS);
    }
  	 if(populate("establishment").value==-1){
  	    populate("informantCitizen.firstName").value="";
  	    makeEmptyAddress(INFORMANTADDRESS);
  	    }
  	 else
  	    populate("informantCitizen.firstName").value=populate('establishment').options[populate('establishment').selectedIndex].text;
  }
  
  myAddressSuccessHandler = function(req,res){
	    var resultdatas=res.results;
	   // populate(EVENTADDRESS+".addressID").value=resultdatas[0].addressID;
	    var hosptype=resultdatas[8].hospitalType;
	    if(hosptype=='false'){
  		populate(EVENTADDRESS+".streetAddress1").value=resultdatas[1].streetAddress1;
  		populate(EVENTADDRESS+".streetAddress2").value=resultdatas[2].streetAddress2;
  		populate(EVENTADDRESS+".taluk").value=resultdatas[3].taluk;
  		populate(EVENTADDRESS+".cityTownVillage").value=resultdatas[4].cityTownVillage;
  		populate(EVENTADDRESS+".pinCode").value=resultdatas[5].pinCode;
  		populate(EVENTADDRESS+".district").value=resultdatas[6].district;
  		populate(EVENTADDRESS+".state").value=resultdatas[7].state;
  		
  		//populate(INFORMANTADDRESS+".addressID").value=resultdatas[0].addressID;
  		populate(INFORMANTADDRESS+".streetAddress1").value=resultdatas[1].streetAddress1;
  		populate(INFORMANTADDRESS+".streetAddress2").value=resultdatas[2].streetAddress2;
  		populate(INFORMANTADDRESS+".taluk").value=resultdatas[3].taluk;
  		populate(INFORMANTADDRESS+".cityTownVillage").value=resultdatas[4].cityTownVillage;
  		populate(INFORMANTADDRESS+".pinCode").value=resultdatas[5].pinCode;
  		populate(INFORMANTADDRESS+".district").value=resultdatas[6].district;
  		populate(INFORMANTADDRESS+".state").value=resultdatas[7].state;
  		populate("informantFlagOther").checked=true;
	    }else{
	    	populate('establishment').value="-1";
	  		makeEmptyAddress(EVENTADDRESS);
	  		makeEmptyAddress(INFORMANTADDRESS);
	  		alert('For the selected hospital certificate cannot be registered here it has to be registered in the hospital where the stillbirth occurred');
		    }
	    return true;
	}
	
	myAddressFailureHandler = function(){
	   alert("Unable to get Address for the selected Hospital....")
	}
  
  function populatedeathcause(elem){
		var parentDeathCause = elem.value;  		
		  populatecauseOfDeath({parentDeathCauseId:parentDeathCause});	
  }
  
   /**
   *     For modify mode , To show the values modified by the user
   */
  
  function compareWithOldValue()
  {
       if(populate("registrationDate").value!=populate("birthHistory.registrationDate").value)
           changedString = '<s:text name="registration.date"/>'+" is changed from "+populate("birthHistory.registrationDate").value+ " to "+populate("registrationDate").value+"'\n";
           
       if(populate("dateOfEvent").value!=populate("birthHistory.dateOfEvent").value)
           changedString = changedString+'<s:text name="dateOfbirth"/>'+" is changed from "+populate("birthHistory.dateOfEvent").value+ " to "+populate("dateOfEvent").value+"'\n";
           
       if(populate("citizen.sex").value!=populate("birthHistory.citizen.sex").value)
           changedString = changedString+'<s:text name="sex.lbl"/>'+" is changed from "+populate("birthHistory.citizen.sex").value+ " to "+populate("citizen.sex").value+"'\n";
           
       //compareWithOldCitizen(CITIZEN,CITIZENHISTORY,CITIZEN);
       compareWithOldCitizen(MOTHER,MOTHERHISTORY,"Mother");
       compareWithOldCitizen(FATHER,FATHERHISTORY,"Father");
       //compareWithOldAddress(PERMANENTCITIZENADDRESS,PERMANENTCITIZENADDRESSHISTORY,'<s:text name="permanentAddress"/>');
      
        if(getPlaceTypeValue()!= populate("placeTypeTempBirthHistory").value)
           changedString = changedString+'<s:text name="place.of.birth.lbl"/>'+" is changed from "+populate("placeTypeTempBirthHistory").value+ " to "+getPlaceTypeValue()+"'\n";
        
        //need to check  
        if(populate("hospitalType").options[populate("hospitalType").selectedIndex].text!=populate("birthHistory.hospitalType").value && populate("hospitalType").value!=-1){
           changedString = changedString+'<s:text name="hospital.type.lbl"/>'+" is changed from "+populate("birthHistory.hospitalType").value+ " to "+populate("hospitalType").options[populate("hospitalType").selectedIndex].text+"'\n";
        }
        if(populate("establishment").value!=populate("birthHistory.establishment").value && populate("establishment").value!=-1){
           changedString = changedString+'<s:text name="hospital.name.lbl"/>'+" is changed from "+getDropDownValue(populate("birthHistory.establishment").value,"establishment")+ " to "+populate("establishment").options[populate("establishment").selectedIndex].text+"'\n";
        }
        
        compareWithOldAddress(EVENTADDRESS,EVENTADDRESSHISTORY,'<s:text name="address"/>');
       
       // compareWithOldAddress(PARENTADDRESS,PARENTADDRESSHISTORY,'<s:text name="address.of.parent.at.birth"/>');
        
         if(getSelectedInformantType()!=populate("informantFlagBirthHistory").value)
           changedString = changedString+'<s:text name="informat.details.lbl"/>'+" is changed from "+populate("informantFlagBirthHistory").value+ " to "+getSelectedInformantType()+"'\n";
        compareWithOldCitizen(INFORMANT,INFORMANTHISTORY,'<s:text name="informat.name.lbl"/>');
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
       		 changedString = changedString+addresslbl+" state is changed from "+getDropDownValue(populate(oldAddress+".state").value,EVENTADDRESS+".state")+ " to "+populate(newAddress+".state").options[populate(newAddress+".state").selectedIndex].text+"'\n";
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
       //if(populate("birthHistory.permanentCitizenAddress.addressID").value=="")
         // populateDefaultAddress(PERMANENTCITIZENADDRESSHISTORY);
       if(populate("birthHistory.eventAddress.addressID").value=="")
          populateDefaultAddress(EVENTADDRESSHISTORY);
       //if(populate("birthHistory.parentAddress.addressID").value=="")   
         // populateDefaultAddress(PARENTADDRESSHISTORY);
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
  

</script>
