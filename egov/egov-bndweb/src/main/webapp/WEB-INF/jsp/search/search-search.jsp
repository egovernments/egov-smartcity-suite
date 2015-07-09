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
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>


<html>
<title>Search</title>
<head>
</head>
<script language="javascript" type="text/javascript">

	var BIRTH='<s:property value="@org.egov.bnd.utils.BndConstants@SEARCHBIRTH"/>';
	var DEATH='<s:property value="@org.egov.bnd.utils.BndConstants@SEARCHDEATH"/>';
	var STILLBIRTH='<s:property value="@org.egov.bnd.utils.BndConstants@SEARCHSTILLBIRTH"/>';
 	var FLAG;
 	
	function changeRegType(){		
		var type=document.getElementsByName('regType');
		for(var i=0 ; i < type.length; i++){		
			if(type[i].checked){
				document.getElementById('hiddenRegType').value=type[i].value;
			}
		}
	}

	function init(){
		
		if(document.getElementById('hospitalId').value=="")
			document.getElementById('hospitalId').disabled=true;
			
		var string=capturesearchcriteria();	
		
		if(document.getElementById("searchMode").value=='result'){
			if((string!="")){
		 		dom.get("search_error").style.display='';	
		 		document.getElementById("search_error").innerHTML=string;
			}		
		}
	}
	
	function getPlaceOfEventValue(){
		var placeOfEventValue="";
		var type=document.getElementsByName('placeOfEventType');		
		for(var i=0 ; i < type.length; i++){
			if( type[i].checked ){				
				placeOfEventValue=type[i].value;
				break;
			}
		}	
		return placeOfEventValue;
	}

	function enableHospitalList(){
		var placeOfEventValue=getPlaceOfEventValue();
		if(placeOfEventValue=='<s:property value="@org.egov.bnd.utils.BndConstants@HOSPTIAL"/>' ){
			document.getElementById('hospitalId').disabled=false;
		}else{
			document.getElementById('hospitalId').value="";
			document.getElementById('hospitalId').disabled=true;
		}
	}


	function resetValues(){
		dom.get("searchRecords_error").style.display = '';
		document.getElementById('hiddenRegType').value='<s:property value="@org.egov.bnd.utils.BndConstants@SEARCHBIRTH"/>';
		document.getElementById('regNo').value="";
		document.getElementById('regYear').value="";
		document.getElementById('fromDate').value="";
		document.getElementById('toDate').value="";
		document.getElementById('firstName').value="";
		document.getElementById('midName').value="";
		document.getElementById('lastName').value="";
		document.getElementById('sextype').value=-1;
		document.getElementById('fatherName').value="";
		document.getElementById('motherName').value="";
		document.getElementById('hospitalId').disabled=false;
		document.getElementById('hospitalId').value="";
		document.getElementById('hospitalId').disabled=true;
		document.getElementById('registrationUnitId').value="";
		document.getElementById('pincode').value="";
		document.getElementById("tableData").style.display='none';
		resetRadioButtons();
	}

	function resetRadioButtons(){
		
		var eventType=document.getElementsByName('placeOfEventType');		
		for(var i=0 ; i < eventType.length; i++){
			eventType[i].checked=false;
		}	
		var type=document.getElementsByName('regType');
		for(var i=0 ; i < type.length; i++){
			if(type[i].value=='<s:property value="@org.egov.bnd.utils.BndConstants@SEARCHBIRTH"/>'){
				type[i].checked=true;
			}
		}		
	}
	
	function validateYear(obj){
		var year = obj.value;
		if(trimAll(year)!=""){
			
			if(isNaN(year))
			{				
				dom.get("searchRecords_error").style.display = '';
		      	document.getElementById("searchRecords_error").innerHTML = '<s:text name="invalid.number" />';		     
		    	document.getElementById("regYear").value = ""; 
		    	return false;
			}
			else if(year.length!=4){				
				dom.get("searchRecords_error").style.display = '';
		      	document.getElementById("searchRecords_error").innerHTML = '<s:text name="invalid.year.fourDigit" />';  		     
				document.getElementById("regYear").value = ""; 
				return false;			
			}
		}
		return true;
	}
	
	function validatePincodeValue(obj)
  	{	  
  		var pincodetemp=obj.value;
        if(trimAll(pincodetemp)!=""){       
        	if(isNaN(pincodetemp))
			{				
				dom.get("searchRecords_error").style.display = '';
		      	document.getElementById("searchRecords_error").innerHTML = '<s:text name="invalid.pincode" />';
  		      	obj.value="";
		  		return false;
			}
			else if(pincodetemp.length!=6){						
				dom.get("searchRecords_error").style.display = '';
		      	document.getElementById("searchRecords_error").innerHTML = '<s:text name="invalid.pincode" />';
  		      	obj.value="";
		  		return false;			
			}
	  		
  		}
  		return true;
  	}
  	
	
	function validateForm(){
		var curr_date = new Date();
	    var curr_year = curr_date.getFullYear(); 
		var selectedYear=document.getElementById('regYear').value;
		
		if(selectedYear!="" && selectedYear!=null){
			if((selectedYear<1900) || (selectedYear>curr_year))
			{		
				alert('Not A Valid Year!! \nPlease Enter the year between 1900 And '+ curr_year);
				document.getElementById('regYear').value="";
				document.getElementById('regYear').focus();
				return false;
			}
			
			if(document.getElementById('regNo').value=="" || document.getElementById('regNo').value==null){
				 
				dom.get("searchRecords_error").style.display = '';
				document.getElementById("searchRecords_error").innerHTML = '<s:text name="registration.number.mandatory" />';			  					 		 							
				document.getElementById('regNo').focus();
				return false;
			}
		}
		var placeOfEventValue=getPlaceOfEventValue();
		if(placeOfEventValue=='<s:property value="@org.egov.bnd.utils.BndConstants@HOSPTIAL"/>' && document.getElementById('hospitalId').value==""){					
			dom.get("searchRecords_error").style.display = '';
			document.getElementById("searchRecords_error").innerHTML = '<s:text name="hospitalname.mandatory" />';			  					 		 					
			return false;		
		}
		
		var obj=document.getElementById("pincode");
		if(!validatePincodeValue(obj)){
			return false;
		}
		
		if(!dateValidate()){
			return false;
		}
		
		if(trimAll(document.getElementById('regNo').value)=="" &&
			trimAll(document.getElementById('regYear').value)=="" && 
			trimAll(document.getElementById('fromDate').value)==""  && 
		 	trimAll(document.getElementById('toDate').value)=="" && 
		 	trimAll(document.getElementById('firstName').value)=="" && 
		 	trimAll(document.getElementById('midName').value)=="" && 
		 	trimAll(document.getElementById('lastName').value)=="" && 
		 	document.getElementById('sextype').value==-1 && 
		 	trimAll(document.getElementById('fatherName').value)=="" && 
		 	trimAll(document.getElementById('motherName').value)=="" && 
		 	trimAll(document.getElementById('pincode').value)=="" &&
		 	document.getElementById('registrationUnitId').value=="" &&
		 	placeOfEventValue==""){
		 		dom.get("searchRecords_error").style.display = '';
				document.getElementById("searchRecords_error").innerHTML = '<s:text name="select.atleast.one.value" />';			  					 		 		
				return false;
		}	
		
	
		return true;
	}

	function dateValidate(){
		var todaysDate=getTodayDate();
	
    	var fromdate=document.getElementById('fromDate').value;
    	var todate=document.getElementById('toDate').value;
    	
    	if(fromdate!=null && fromdate!="" && fromdate!=undefined && todate!=null && todate!="" && todate!=undefined ){
			if(compareDate(fromdate,todaysDate) == -1)
			{						  	 	
				 dom.get("searchRecords_error").style.display = '';
				 document.getElementById("searchRecords_error").innerHTML = '<s:text name="fromDate.todaysDate.validate" />';			  					 
				 document.getElementById('fromDate').value=""; 
				 document.getElementById('fromDate').focus();
				 return false;
			}
			if(compareDate(todate,todaysDate) == -1)
			{		
				 
				 dom.get("searchRecords_error").style.display = '';
				 document.getElementById("searchRecords_error").innerHTML = '<s:text name="toDate.todaysDate.validate" />';			  
									 
				  document.getElementById('toDate').value="";  
				  document.getElementById('toDate').focus();
				  return false;
			}
			if(compareDate(fromdate,todate) == -1)
			{		
					
				 dom.get("searchRecords_error").style.display = '';
				 document.getElementById("searchRecords_error").innerHTML = '<s:text name="fromDate.toDate.validate" />';			  
				 document.getElementById('fromDate').value="";
				 document.getElementById('toDate').value="";    
				 document.getElementById('fromDate').focus();
				 return false;
			}
			
       }
       if(fromdate==null || fromdate=="" || fromdate==undefined){
    		if(todate!=null && todate!="" && todate!=undefined){
    		  		
    			dom.get("searchRecords_error").style.display = '';
				document.getElementById("searchRecords_error").innerHTML = '<s:text name="select.fromDate" />';
			 	document.getElementById('fromDate').focus();
				return false;
    		}
    	}
    	if(todate==null || todate=="" || todate==undefined){
    		if(fromdate!=null && fromdate!="" && fromdate!=undefined){
    		
    		dom.get("searchRecords_error").style.display = '';
			document.getElementById("searchRecords_error").innerHTML = '<s:text name="select.todate" />';
			 document.getElementById('toDate').focus();
			return false;
			}
    	}
       return true;
    }
		
	function callOnChange(obj){				
		var currRow=getRow(obj); 
		var regtype=document.getElementById('hiddenRegType').value;
		//alert(obj.value);
		
		if(regtype==DEATH){
			regId = getControlInBranch(currRow,'deathId').value;
		}
		else{
			regId = getControlInBranch(currRow,'birthId').value;
		 }
		var url = "${pageContext.request.contextPath}/common/ajaxCommon!ajaxLoadActions.action?regId="+regId+'&registrationType='+regtype+'&arguments='+obj.value;
		var req = initiateRequest();
  	 	req.open("GET", url, false);
		req.send(null);
		if (req.readyState == 4)
  		{
  		    if (req.status == 200)
  		    {
  			    var responseString =req.responseText;  			     			
  			    FLAG=responseString; 	
  			    		    	
  			    if(obj.value=='<s:property value="@org.egov.bnd.utils.BndConstants@VIEW"/>'){
	  			    if(FLAG=='performView'){
	  			       var submitForm = createNewForm("idTemp",regId);
		              	if(regtype==BIRTH){
		              	     submitForm.action= "${pageContext.request.contextPath}/registration/birthRegistration!view.action";
	      	        		 submitForm.submit();
	  			    	}
	  			    	else if(regtype==DEATH){
	  			    	    submitForm.action= "${pageContext.request.contextPath}/registration/deathRegistration!view.action";
	      	        		submitForm.submit();
	  			    	}
	  			    	else if(regtype==STILLBIRTH){
	  			    	     submitForm.action= "${pageContext.request.contextPath}/registration/stillBirthRegistration!view.action";
	      	        		 submitForm.submit();
	  			        }
	  			
	  			    }	  			    	
  			    	else{
  			    		alert("No permission to Perform this action");
  			    	}
  			    }
  			    else if(obj.value=='<s:property value="@org.egov.bnd.utils.BndConstants@UPDATE"/>'){
  			    	if(FLAG=='performUpdate'){
  			    	    var submitForm = createNewForm("idTemp",regId);
  			    		if(regtype==BIRTH){
  			    		    submitForm.action= "${pageContext.request.contextPath}/registration/birthRegistration!beforeEdit.action";
	      	        		submitForm.submit();
	  			    	}
	  			    	else if(regtype==DEATH){
	  			    	    submitForm.action= "${pageContext.request.contextPath}/registration/deathRegistration!beforeEdit.action";
	      	        		submitForm.submit();
	  			    	}
	  			    	else if(regtype==STILLBIRTH){
	  			    		submitForm.action= "${pageContext.request.contextPath}/registration/stillBirthRegistration!beforeEdit.action";
	      	        		submitForm.submit();
	  			    	}
	  		
  			    	}
  			    	else if(FLAG=='otherRegUnit'){
  			    		 alert("This registration Number belongs to Some other Registration Unit or Hospital.There is no permission to update this record.");
  			    	}
  			    	else{
  			    		alert("No permission to Perform this action");
  			    	}
  			    }
  			    else if(obj.value=='<s:property value="@org.egov.bnd.utils.BndConstants@ADOPTION"/>' ){
  			
  			         if(FLAG=='performAdoption'){
  			            var submitForm = createNewForm("idTemp",regId);
  			       		if(regtype==BIRTH){
	  			    		submitForm.action= "${pageContext.request.contextPath}/registration/adoption!newform.action";
	      	        		submitForm.submit();
	  			    	}  			    	 			    		
  			    	}
  			    	else if(FLAG=='childAdopted'){
  			    		 alert("Child already adopted. There is no permission to update this record.");
  			    	}  			    	
  			    	else{
  			       		alert("No permission to Perform this action");
  			    	}
 			    }
 			    else if(obj.value=='<s:property value="@org.egov.bnd.utils.BndConstants@SIDELETTER"/>' ){
  			    	if(FLAG=='performSideLetter'){
  			    	    var submitForm = createNewForm("birthId",regId);
  			    	    submitForm.action= "${pageContext.request.contextPath}/registration/sideLetter!newform.action";
	      	        	submitForm.submit();
  			    	   			    		
  			    	}
  			    	else{
  			    		alert("No permission to Perform this action");
  			    	}
 			    }
 			    else if(obj.value=='<s:property value="@org.egov.bnd.utils.BndConstants@CERTIFICATEGENERATIONFORBIRTH"/>' ){
  			    	if(FLAG=='performCertificate'){
  			    	    var submitForm = createNewForm("reportId",regId);
  			    	    submitForm.action= "${pageContext.request.contextPath}/bill/feeCollection!setUp.action?regType="+regtype;
	      	        	submitForm.submit();
  			    	}
  			    	else if(FLAG=='otherRegUnit'){
  			    		 alert("This registration Number belongs to Some other Registration Unit or Hospital.\n You have no permission to  generate certificate for this record.")
  			    	}
  			    	else if(FLAG=='authHosp1year'){
  			    	 	alert("This registration Number belongs to Authorised Hospital.\n You have no permission to  generate certificate for this record within 1 year")  			   
  			    	}
  			    	else{
  			    		alert("No permission to Perform this action");
  			    	}
 			    }
 			    else if(obj.value=='<s:property value="@org.egov.bnd.utils.BndConstants@CERTIFICATEGENERATION"/>' ){
  			    	if(FLAG=='performCertificate'){ 			    		
	  			    	var submitForm = createNewForm("reportId",regId);
  			    	    submitForm.action= "${pageContext.request.contextPath}/bill/feeCollection!setUp.action?regType="+regtype;
	      	        	submitForm.submit();
  			    	}
  			    	else if(FLAG=='otherRegUnit'){
  			    		 alert("This registration Number belongs to Some other Registration Unit or Hospital.\n You have no permission to  generate certificate for this record.")
  			    	}
  			    	else if(FLAG=='authHosp1year'){
  			    	 	alert("This registration Number belongs to Authorised Hospital.\n You have no permission to  generate certificate for this record within 1 year")  			   
  			    	}
  			    	else{
  			    		alert("No permission to Perform this action");
  			    	}
 			    }
 			     else if(obj.value=='<s:property value="@org.egov.bnd.utils.BndConstants@LOCKRECORD"/>' ){
 			     var mode=document.getElementById('mode').value;
 			     	mode="lock";
 			     	
 			     	if(FLAG=='performLock'){ 			    		
	  			    	if(regtype==BIRTH){
	  			    	    var submitForm = createNewForm("idTemp",regId);
  			    	        submitForm.action= "${pageContext.request.contextPath}/registration/birthRegistration!beforeEdit.action?mode="+mode;
	      	        	    submitForm.submit();
	  			    	}
	  			    	else if(regtype==DEATH){
	  			    	    var submitForm = createNewForm("idTemp",regId);
  			    	        submitForm.action= "${pageContext.request.contextPath}/registration/deathRegistration!beforeEdit.action?mode="+mode;
	  			    	 	submitForm.submit();
	  			    	}
	  			    	else if(regtype==STILLBIRTH){
	  			    	    var submitForm = createNewForm("idTemp",regId);
  			    	        submitForm.action= "${pageContext.request.contextPath}/registration/stillBirthRegistration!beforeEdit.action?mode="+mode;
	  			    	 	submitForm.submit();
	  			    	}	  			
	  			    }
	  			    else if(FLAG=='alreadyLocked') 	
						alert("This Record is already Locked");
					else{
						alert("No permission to Perform this action");
					}	
					  			    	
 			     }
 			     else if(obj.value=='<s:property value="@org.egov.bnd.utils.BndConstants@UNLOCKRECORD"/>' ){
 			     var mode=document.getElementById('mode').value;
 			     	mode="unlock";
 			     	
 			     	if(FLAG=='performUnlock'){ 	
 			     	    var submitForm = createNewForm("idTemp",regId);		    		
	  			    	if(regtype==BIRTH){
	  			    	    submitForm.action= "${pageContext.request.contextPath}/registration/birthRegistration!beforeEdit.action?mode="+mode;
	  			    	 	submitForm.submit();
	  			    	}
	  			    	else if(regtype==DEATH){
	  			    	 	submitForm.action="${pageContext.request.contextPath}/registration/deathRegistration!beforeEdit.action?mode="+mode;
	  			    	 	submitForm.submit();
	  			    	}
	  			    	else if(regtype==STILLBIRTH){
	  			    		submitForm.action="${pageContext.request.contextPath}/registration/stillBirthRegistration!beforeEdit.action?mode="+mode;
	  			    		submitForm.submit();
	  			    	}
	  			    }
	  			    else if(FLAG=='notLocked') 	
						alert("This Record is not Locked. You can UNLOCK only Locked Records");
					else{
						alert("No permission to Perform this action");
					}
  				}
  				
  				else if(obj.value=='<s:property value="@org.egov.bnd.utils.BndConstants@NAMEINCLUSION"/>' ){
 			    var mode=document.getElementById('mode').value;
 			     	mode="nameinclusion";
 			     	
 			     	if(FLAG=='performNameInclusion'){ 
 			     	    var submitForm = createNewForm("idTemp",regId);
	  			    	if(regtype==BIRTH){
	  			    	    submitForm.action= "${pageContext.request.contextPath}/registration/birthRegistration!beforeEdit.action?mode="+mode;
	      	                submitForm.submit();
	  			    	}	  			
	  			    }
	  			    else if(FLAG=='namePresent'){
	  			    	alert("Child Name is Already Exists");
	  			    }
					else{
						alert("No permission to Perform this action");
					}	
					  			    	
 			     }
  				
  				
  				
  			}		
		}
	}
	
	/**
	 *   This method is to create a new form element.
	 */
	 
	function createNewForm(name,id)
	{
	    //Create an input type dynamically.
    	var element = document.createElement("input");
	    //Assign different attributes to the element.
    	element.setAttribute("type", "hidden");
    	element.setAttribute("value", id);
    	element.setAttribute("name", name);
    	submitForm = document.createElement("FORM");
	    submitForm.appendChild(element);
		document.body.appendChild(submitForm);
 	 	submitForm.method = "POST"; 
 	 	return submitForm;
	}
	
	function openBirthRecord(id)
	{
		var regtype=document.getElementById('hiddenRegType').value;
     	var submitForm = createNewForm("idTemp",id);
		if(regtype==BIRTH){
               submitForm.action= "${pageContext.request.contextPath}/registration/birthRegistration!view.action";
	    	   submitForm.submit();
		}
		else if(regtype==STILLBIRTH){
		     submitForm.action= "${pageContext.request.contextPath}/registration/stillBirthRegistration!view.action";
	    	 submitForm.submit();
	    }		  			
	}
	
	function openDeathRecord(id)
	{
	    var submitForm = createNewForm("idTemp",id);
	     submitForm.action= "${pageContext.request.contextPath}/registration/deathRegistration!view.action";
	     submitForm.submit();
	}
	
	function openRecordToUnlock(id)
	{
		var mode=document.getElementById('mode').value;
		var regtype=document.getElementById('hiddenRegType').value;
		var submitForm = createNewForm("idTemp",id);
		if(regtype==BIRTH){
		   submitForm.action= "${pageContext.request.contextPath}/registration/birthRegistration!beforeEdit.action?mode="+mode;
	       submitForm.submit();
		}
		else if(regtype==STILLBIRTH){
		    submitForm.action= "${pageContext.request.contextPath}/registration/stillBirthRegistration!beforeEdit.action?mode="+mode;
	        submitForm.submit();
		}		
		else if(regtype==DEATH){
		    submitForm.action= "${pageContext.request.contextPath}/registration/deathRegistration!beforeEdit.action?mode="+mode;
	        submitForm.submit();
		} 			    	
	}
	

  	function capturesearchcriteria(){
  		var String="";   
  		if((document.getElementById("hiddenRegType").value!=null)&&(document.getElementById("hiddenRegType").value!=""))
  		String=document.getElementById("hiddenRegType").value+" : ";
     	
     	if((document.getElementById("regNo").value!=null)&&(document.getElementById("regNo").value!="")){
			String=String+"Registration Number:"+document.getElementById("regNo").value+" , ";			
			
			if((document.getElementById("regYear").value!=null)&&(document.getElementById("regYear").value!=""))
			String=String+"Registration Year:"+document.getElementById("regYear").value+" , ";
		}
		else{
			if((document.getElementById("fromDate").value!=null)&&(document.getElementById("fromDate").value!=""))
			String=String+"<s:text name='from.date.lbl'/>"+document.getElementById("fromDate").value+" , ";
			
			if((document.getElementById("toDate").value!=null)&&(document.getElementById("toDate").value!=""))
			String=String+"<s:text name='to.date.lbl'/> "+document.getElementById("toDate").value+" , ";
			
			if((document.getElementById("firstName").value!=null)&&(document.getElementById("firstName").value!=""))
			String=String+"<s:text name='first.name.lbl'/>"+document.getElementById("firstName").value+" , ";
			
			if((document.getElementById("midName").value!=null)&&(document.getElementById("midName").value!=""))
			String=String+"<s:text name='middle.name.lbl'/>"+document.getElementById("midName").value+" , ";
			
			if((document.getElementById("lastName").value!=null)&&(document.getElementById("lastName").value!=""))
			String=String+"<s:text name='last.name.lbl'/>"+document.getElementById("lastName").value+" , ";
			
			if((document.getElementById("sextype").value!=null)&&(document.getElementById("sextype").value!=-1))
			String=String+"<s:text name='sex.lbl'/>"+document.getElementById("sextype").options[document.getElementById("sextype").selectedIndex].text+" , ";
			
			if((document.getElementById("fatherName").value!=null)&&(document.getElementById("fatherName").value!=""))
			String=String+"<s:text name='father.name.lbl'/>"+document.getElementById("fatherName").value+" , ";
			
			if((document.getElementById("motherName").value!=null)&&(document.getElementById("motherName").value!=""))
			String=String+"<s:text name='mother.name.lbl'/>"+document.getElementById("motherName").value+" , ";      	  
	      	
	      	var placeOfEventValue=getPlaceOfEventValue();
	      	if(placeOfEventValue!=null && placeOfEventValue!="")
	      	String=String+"<s:text name='place.of.event.lbl'/>"+placeOfEventValue+" , ";
	      	
	      	if((document.getElementById("hospitalId").value!=null)&&(document.getElementById("hospitalId").value!=""))
			String=String+"<s:text name='hospital.name.lbl'/>"+document.getElementById("hospitalId").value+" , ";      	 
	      	
	      	if((document.getElementById("registrationUnitId").value!=null)&&(document.getElementById("registrationUnitId").value!=""))
			String=String+"<s:text name='registrationUnit.name.lbl'/>"+document.getElementById("registrationUnitId").options[document.getElementById("registrationUnitId").selectedIndex].text+" , ";      	 
	      	
	      	if((document.getElementById("pincode").value!=null)&&(document.getElementById("pincode").value!=""))
			String=String+"<s:text name='pincode.lbl'/>"+document.getElementById("pincode").value;     
		}
		return String; 	 
      
  	}
  	
  
</script>


<body onload="changeRegType();init();">

	<div class="errorstyle" id="searchRecords_error" style="display: none;"></div>
		<div class="errorstyle" style="display: none" >			
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

	<s:form action="search" theme="simple" name="searchForm">
	
	 <s:hidden name="hiddenRegType" id="hiddenRegType"/>
	 <s:hidden name="isCitizen" id="isCitizen"/>
	 <s:hidden name="searchMode" id="searchMode"/>
	  <s:hidden name="mode" id="mode" value="%{mode}" />
	  <s:hidden name="idTemp" id="idTemp"/>
	  
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td class="greybox" width="40%">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
			</tr>
			<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">
					<s:radio list="registrationTypeList" value="%{regType}" name="regType" id="regType" onclick="changeRegType();" />
			
				</td>
				<td class="bluebox">&nbsp;</td>
			</tr>
			<tr>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
			</tr>
		</table>
		
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="registration.number.lbl"/></td>
				<td class="bluebox" ><s:textfield id="regNo" name="regNo" value="%{regNo}" /></td>
				<td class="bluebox" width="8%"><s:text name="year.lbl"/> </td>
				<td class="bluebox" ><s:textfield id="regYear" name="regYear" value="%{regYear}" maxlength="4"  onblur="return validateYear(this);"/></td>
				<td class="bluebox">&nbsp;</td>
			</tr>
			
			<tr>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>              
				<td class="greybox" align="center">
					<h1 class="subhead" align="center">OR</h1>
				</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
			</tr>
			
			<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="from.date.lbl"/> </td>
				<td class="bluebox">
					<s:date format="dd/MM/yyyy" name="fromDate" var="TempDate"/>
					<s:textfield name="fromDate" id="fromDate"  maxlength="20" value="%{TempDate}"  onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
					<a href="javascript:show_calendar('forms[0].fromDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true; "><img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" border="0"/></a>
					<br/>
				</td>  
				<td class="bluebox"><s:text name="to.date.lbl"/> </td>
				<td class="bluebox">
				
					<s:date name="toDate" format="dd/MM/yyyy" var="dateTemp"/>
					<s:textfield name="toDate" id="toDate" maxlength="20" value="%{dateTemp}" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
					<a href="javascript:show_calendar('forms[0].toDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true; "><img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" border="0"/></a>
					</td>			
				<td class="bluebox">&nbsp;</td>
			</tr>
		
			<tr>
				<td class="greybox" >&nbsp;</td>
				<td class="greybox" ><s:text name="first.name.lbl"/></td>
				<td class="greybox" ><s:textfield id="firstName" name="firstName" value="%{firstName}"/></td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox" >&nbsp;</td>
				<td class="greybox">&nbsp;</td>
			</tr>
			
			<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="middle.name.lbl"/></td>
				<td class="bluebox" ><s:textfield id="midName" name="midName" value="%{midName}"/></td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
			</tr>
			
			<tr>
				<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="8%"><s:text name="last.name.lbl"/></td>
				<td class="greybox" ><s:textfield id="lastName" name="lastName" value="%{lastName}"/></td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
			</tr>
		
		
			<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="sex.lbl"/></td>
				<td class="bluebox"><s:select id="sextype" name="sextype" list="dropdownData.sexTypeList" listKey="code" listValue="code" headerKey="-1" headerValue="-----choose----" value="%{sextype}"/></td>
				<td class="bluebox" ><s:text name="registrationUnit.name.lbl"/></td>
				<td class="bluebox" >
				<s:select name="registrationUnitId" id="registrationUnitId" list="dropdownData.registrationList" value="%{registrationUnitId}" listKey="id" listValue="regUnitDesc" headerKey="" headerValue="---choose---"/>
				</td>
				<td class="bluebox">&nbsp;</td>
			</tr>
			
			<tr>
				<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="8%"><s:text name="father.name.lbl"/></td>
				<td class="greybox" ><s:textfield id="fatherName" name="fatherName" value="%{fatherName}"/></td>
				<td class="greybox" ><s:text name="mother.name.lbl"/></td>
				<td class="greybox" ><s:textfield id="motherName" name="motherName" value="%{motherName}"/></td>
				<td class="greybox">&nbsp;</td>

			</tr>
			<tr>
				<td class="bluebox" >&nbsp;</td>
				<td class="bluebox" ><s:text name="place.of.event.lbl"/></td>
				<td class="bluebox" >
					<s:radio list="dropdownData.placeTypeList" value="%{placeOfEventType}" name="placeOfEventType" id="placeOfEventType" listKey="desc" listValue="desc" onclick="enableHospitalList();" />
				</td>
				<td class="bluebox" ><s:text name="hospital.name.lbl"/></td>
				<td class="bluebox" >
				<s:select name="hospitalId" id="hospitalId" list="dropdownData.hospitalList" value="%{hospitalId}" listKey="id" listValue="name" headerKey="" headerValue="---choose---"/>
			    </td>
				<td class="bluebox">&nbsp;</td>
			</tr>
			<tr>
				<td class="greybox" >&nbsp;</td>
				<td class="greybox" ><s:text name="pincode.lbl"/></td>
				<td class="greybox" ><s:textfield id="pincode" name="pincode" value="%{pincode}" maxlength="6" onblur="return validatePincodeValue(this);"/></td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
			</tr>
		</table>
		<div class="buttonbottom" align="center">
			<table>
				<tr>
					<td><s:submit cssClass="buttonsubmit" id="submit" name="submit" value="Search" onclick="return validateForm();" method="searchresults"  /></td>
			  		 <td><input type="button" id="reset" name="reset" class="button" value="Reset" onclick="resetValues();" /></td>
			  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
	        </table>
	   </div>
	   
	  
	   
	   <div id="tableData">
	   <div class="infostyle" id="search_error" style="display:none;"></div> 
		 <s:if test="%{searchMode=='result'}">
          		 <div id="displaytbl">	
          		     	 <display:table  name="searchResult" export="false" requestURI="" id="regListid"  class="its" uid="currentRowObject" >
          			 	 <div STYLE="display: table-header-group" align="center">
          			 	  
 						 	<display:column title=" Sl No" style="text-align:center;"  >
 						 	 		<s:property value="%{#attr.currentRowObject_rowNum}"/>
 						 	</display:column>
						
							<s:if test="%{hiddenRegType==death}">								
							
								<s:if test="%{mode==unlock}">		
									<display:column title="Registration No " style="text-align:center;" >	
	 						 		<a href="#" onclick="openRecordToUnlock('${currentRowObject.id}')">
	 						 			 ${currentRowObject.registrationNo}
	 						 		</a>
	 						 		</display:column>	
 						 		</s:if>
 						 		<s:else>
	 						 		<display:column title="Registration No " style="text-align:center;" >	
	 						 		<a href="#" onclick="openDeathRecord('${currentRowObject.id}')">
	 						 			 ${currentRowObject.registrationNo}
	 						 		</a>
	 						 		</display:column>	
 						 		</s:else>				 	
 			
							
								<display:column class="hidden" headerClass="hidden"  media="html">
	 						 		<s:hidden id="deathId" name="deathId" value="%{#attr.currentRowObject.id}" />						
								</display:column>
								
								<display:column title="Date of Death" style="text-align:center;" property="dateOfEvent" format="{0,date,dd-MM-yyyy}">						
								</display:column>
							
								<display:column title="Name of Person " style="text-align:center;" property="citizenName">	 								
								</display:column>
								
								
	 						 	<display:column title="Sex" style="text-align:center;" property="citizen.sex" >
 						 		</display:column>
 						 		
 						 		<display:column title="Place of Death " style="text-align:center;" property="placeOfEventAddress" >	 						 
	 						 	</display:column>
	 						 	
	 						 	
	 						 	<display:column title="Registration Unit" style="text-align:center;" property="registrationUnit.regUnitDesc" >
 						 		</display:column>
 						 		
 						 		
 						 		<s:if test="%{isCitizen==false && mode!=unlock}">
 						 			<display:column media="html" class="blueborderfortd" title="Action" style="width:10%">
									<s:select  theme="simple" id="workflowActions" name="workflowActions"  list="#attr.currentRowObject.dropdownActionList"  headerValue="--Choose--" headerKey="0" onchange="callOnChange(this);" ></s:select>
									</display:column>		
								</s:if>	
								
							</s:if>													
							<s:else>	
								<s:if test="%{mode==unlock}">	
									<display:column title="Registration No " style="text-align:center;" >	
	 						 		<a href="#" onclick="openRecordToUnlock('${currentRowObject.id}')">
	 						 			 ${currentRowObject.registrationNo}
	 						 		</a>
	 						 		</display:column>
								</s:if>
								<s:else>												
									<display:column title="Registration No " style="text-align:center;" >	
	 						 		<a href="#" onclick="openBirthRecord('${currentRowObject.id}')">
	 						 			 ${currentRowObject.registrationNo}
	 						 		</a>
	 						 		</display:column>
 						 		</s:else>
								
								<display:column class="hidden" headerClass="hidden"  media="html">
	 						 		<s:hidden id="birthId" name="birthId" value="%{#attr.currentRowObject.id}" />						
								</display:column>
							
								<display:column title="Date of Birth" style="text-align:center;" property="dateOfEvent" format="{0,date,dd-MM-yyyy}" >						
								</display:column>
							
								<display:column title="Name of Child " style="text-align:center;" property="citizenName">	 								
								</display:column>
									 
								<display:column title="Sex" style="text-align:center;" property="citizen.sex" >
 						 		</display:column>
 						 		
								<display:column title="Father Name " style="text-align:center;" property="fatherFullName" >	 						 	
	 						 	</display:column>
	 						 		
	 						 	<display:column title="Mother Name " style="text-align:center;" property="motherFullName" >	 						   	
	 						 	</display:column>	 
							 						 	
	 						 	<display:column title="Place of birth " style="text-align:center;" property="placeOfEventAddress" >	 						 
	 						 	</display:column>
	 						 	
	 						 	<display:column title="Registration Unit" style="text-align:center;" property="registrationUnit.regUnitDesc" >
 						 		</display:column> 						 		
	 						 	
	 						 	<s:if test="%{isCitizen==false && mode!=unlock}">
	 						 		
		 						 		<s:if test="%{hiddenRegType==stillBirth}">
				 						 	<display:column media="html" class="blueborderfortd" title="Action" style="width:10%">
											<s:select  theme="simple" id="workflowActions" name="workflowActions"  list="#attr.currentRowObject.dropdownActionListStillBirth"  headerValue="--Choose--" headerKey="0" onchange="callOnChange(this);" ></s:select>
											</display:column>
										</s:if>
										<s:else>
											<display:column media="html" class="blueborderfortd" title="Action" style="width:10%">
											<s:select  theme="simple" id="workflowActions" name="workflowActions"  list="#attr.currentRowObject.dropdownActionList"  headerValue="--Choose--" headerKey="0" onchange="callOnChange(this);" ></s:select>
											</display:column>
										</s:else>
									
								</s:if>	
							</s:else>
								  			 						 
						</div>
						</display:table>
					</div>
  	   			</s:if>
  	     </div>
	   
	</s:form>
</body>
</html>
