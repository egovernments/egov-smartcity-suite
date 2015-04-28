/*
#   eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
# */
/*
 *  This method is to validate special characters
 */
jQuery.noConflict();
var maskingTimeForDownloads = 30000;//30 seconds
var displayTagExportPDF = 'd-5394226-e=5';
var displayTagExportExcel = 'd-5394226-e=2';
	function checkSpecialCharacters(val,errordivname,labelname,obj)
	{
		var iChars = "!@$%^*+=[']\\';{}|\":<>?#&().";
		for (var i = 0; i < val.length; i++)
		{
			if (iChars.indexOf(val.charAt(i)) != -1)
			{
				return showErrorwithColor("Marked fields should be Numeric ",errordivname,obj);
				return false;
				
			}
		}
		return true;
	}

 /*
  *  This is to validate pincode 
  */

	function validatePincode(obj)
	{
		if(obj.value.length!=6)
			return false;
		if(obj.value.charAt(0) == 0)
			return false;
		var count=0;
		for(var i=0;i<obj.value.length;i++)
		{
			if(obj.value.charAt(i)==0)
				count++;
		}
		if(count==6)
			return false;
		else
			return true;
	}
	
	function checkForOnlyAlplabets(obj)
	{

		re = /^[A-Za-z ]+$/;
		if(re.test(obj.value))
		{
			return true;
		}
		else
		{
			return false;
		}

	}
	
	/*
	 * 
	 */
	
	 function trimAll(sString)
	 {
		 while (sString.substring(0,1) == ' ')
		 {
			 	sString = sString.substring(1, sString.length);
		 }
		 while (sString.substring(sString.length-1, sString.length) == ' ')
		 {
			 sString = sString.substring(0,sString.length-1);
		 }
		 return sString;
	 }
	 
	function checkSpecialCharactersInCode(obj)
	{
		var iChars = "!@$%^*+=[']\\';{}|\":<>?#&() ";
		for (var i = 0; i < obj.value.length; i++)
		{
			if (iChars.indexOf(obj.value.charAt(i)) != -1)
			{
				return false;
			}
		}
		return true;
	}
	
	/*
	 *   This method is to validate the Number like( ShopNo)
	 */
	
	
	function checkSpecialCharactersInNumber(obj)
	{
		var iChars = "!@$%^*+=[']\\';{}|\":<>?#&(),- ";
		for (var i = 0; i < obj.value.length; i++)
		{
			if (iChars.indexOf(obj.value.charAt(i)) != -1)
			{
				return false;
			}
		}
		return true;
	}
	
	/*
	 * This method is to validate the Number like( tenderAuctionNo) which allows & : - / characters
	 */
	
	function checkSplCharIncludingFewSplchar(obj)
	{
		var iChars = "!@$%^*+=[']\\';{}|\"<>?#(),";
		for (var i = 0; i < obj.value.length; i++)
		{
			if (iChars.indexOf(obj.value.charAt(i)) != -1)
			{
				return false;
			}
		}
		return true;
	}
	
	
	
	/**
	 * @return
	 * This method returns today's date
	 */
	
	
	function getTodayDate()
	{
		var date;
  	    var d = new Date();
		var curr_date = d.getDate();
		var curr_month = d.getMonth();
			curr_month++;
		var curr_year = d.getFullYear();
  	    date=curr_date+"/"+curr_month+"/"+curr_year;
  	    return date;
	}
	
	function checkPhoneNumberContent(obj)
	{

	var validChars="0123456789";
	var objVal=obj.value;
	var len= 0;
	var invalid=false;
	if(objVal!=null && objVal!="")
	{
		if(obj.value.length!=10){
			invalid=true; }
		else if(obj.value.charAt(0) == 0){
			invalid=true;
		}else if(objVal!=null && objVal!="")
		{
			len= objVal.length;
			for(var i=0;i<len && invalid==false;i++)
			{
				chars=objVal.charAt(i);
				
				if(validChars.indexOf(chars)==-1)
				{			
					invalid=true;
				}
			}
			if(invalid==false && obj.value==0)
			{
				invalid=true;
			}
		}
		if(invalid==true)
		{
			alert("Please enter the valid contact number.");
			obj.value="";
			obj.focus();
		}
	}
	return;
	}
	
	/**
	 * 
	 * @param valObj
	 * @param errordivname where to display validation message
	 * @labelname name of the lable for which you are validating
	 * @return
	 * 
	 * This method is to check upto two decimal places
	 */
	
	function checkUptoTwoDecimalPlace(value,errordivname,labelname,obj)
	{
		
		
		if(value!=""){
			if (isNaN(value)) {						
				return showErrorwithColor("Marked fields should be Numeric",errordivname,obj);
				
			}
		
			else if (value < 0) {
				value = "";
				return showErrorwithColor("Marked fields cannot be Negative",errordivname,obj);
			}
			
			else if(trimAll(value)!="" && String(value).indexOf(".") !=-1 && (String(value).indexOf(".") < String(value).length - 3)) {
			 	value="";
			 	return showErrorwithColor("Marked fields can only be 2 decimal places at most.",errordivname,obj);
			}
		}
	}
	
	function showError(msg,errordivname)
	{
		if(dom.get(errordivname)!=null)
		 {
			 dom.get(errordivname).style.display = '';
		 	 document.getElementById(errordivname).innerHTML = msg;
		 }
		 else
			 alert(msg);
		
		return false;
	}
	
	function showErrorwithColor(msg,errordivname,obj){
		if(dom.get(errordivname)!=null)
		 {
			 dom.get(errordivname).style.display = '';
		 	 document.getElementById(errordivname).innerHTML = msg;
		 }
		 else{
			 alert(msg);
		 }
		jQuery(obj).css("border", "1px solid red");		
		return false;
		
	}
	
	/**
	 * 
	 * @param obj
	 * @param errordivname
	 * @param msg
	 * @return
	 * This method will allow numbers like maximum digti before demal point like 123.98
	 */
	
	function checkOnlyThreeDigitBeforeDecimal(obj,errordivname,msg)
	{
		if(obj.value!=""){
			
			if(String(obj.value).indexOf(".") ==-1 && String(obj.value).length>3){
				obj.value = "";
				showError(msg,errordivname);
				return false;
			}
			
			else if(String(obj.value).indexOf(".") !=-1){
			    var rate=obj.value.split(".");
			    if(rate[0].length>3){
			    	obj.value = "";
					showError(msg,errordivname);
					return false;
				}
			}
		}
		return true;
	}
	
	function showWaiting() 
  	{
		document.getElementById('loading').style.display ='block';
	}
	
	function checkNumbers(value,errordivname,labelname,obj){
	
		if (isNaN(value)) {						
			return showErrorwithColor(labelname+" should be Numeric",errordivname,obj);
			
		}else{
			dom.get(errordivname).style.display = '';
		}
		
	}
	
	function autoTab(input,len, e) {
		var isNN = (navigator.appName.indexOf("Netscape")!=-1);
	  var keyCode = (isNN) ? e.which : e.keyCode; 
	  var filter = (isNN) ? [0,8,9] : [0,8,9,16,17,18,37,38,39,40,46];
	  if(input.value.length >= len && !containsElement(filter,keyCode)) {
	    input.value = input.value.slice(0, len);
	    input.form[(getIndex(input)+1) % input.form.length].focus();
	  }

	  function containsElement(arr, ele) {
	    var found = false, index = 0;
	    while(!found && index < arr.length)
	    if(arr[index] == ele)
	    found = true;
	    else
	    index++;
	    return found;
	  }

	  function getIndex(input) {
	    var index = -1, i = 0, found = false;
	    while (i < input.form.length && index == -1)
	    if (input.form[i] == input)index = i;
	    else i++;
	    return index;
	  }
	  return true;
	}
	
	 function refreshInbox()
	  {

	  if(opener.top.document.getElementById('inboxframe')!=null)
	  {
	  	if(opener.top.document.getElementById('inboxframe').contentWindow.name!=null && opener.top.document.getElementById('inboxframe').contentWindow.name=="inboxframe")
	  		{ 
	  		 opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
	  		}
	  	}
	 }
	 
	 /**
	 * Checks whether the From date is greater than  To date
	 **/
	 function checkFdateTdate(fromDate,toDate)
	 {
	 	//ENTERED DATE FORMAT MM/DD/YYYY	

	 	//alert('From Year'+fromDate.substr(6,4)+'To'+toDate.substr(6,4));
	 	//alert('From Month'+fromDate.substr(0,2)+'To'+toDate.substr(0,2));
	 	//alert('From Date'+fromDate.substr(3,2)+'To'+toDate.substr(3,2));
	 	
	 	if(fromDate.substr(6,4) > toDate.substr(6,4))
	 	{
	 		return false;
	 	}
	 	
	 	else if(fromDate.substr(6,4) == toDate.substr(6,4))
	 	{
	 		if(fromDate.substr(3,2) > toDate.substr(3,2))
	 		{
	 			return false;
	 		}
	 	   
	 		else if(fromDate.substr(3,2) == toDate.substr(3,2))
	 		{
	 			if(fromDate.substr(0,2) > toDate.substr(0,2))
	 			{
	 				return false;
	 			}	
	 	
	 			else 
	 			{
	 				return true;
	 			}
	 		}
	 		else
	 		{
	 			return true;
	 		}
	 	}
	 	else
	 	{
	 		return true;
	 	}
	 }
	 
	 /**
	 * Checks whether the value of the string entered evaluates to zero
	 * If so gives the alert msg saying "Zero values are not allowed"
	 */
	 function checkZero(obj,msg)
	 {
	 	var val;
	 	var Objvalue=obj.value;
	 	//alert("inside checkZero val="+obj.value+obj.readOnly);
	 	if(obj.readOnly==false && obj.value!=null && obj.value!="")	
	 	{
	 		//alert("Objvalue.isNaN()="+isNaN(Objvalue));
	 		if(!isNaN(Objvalue))
	 		{
	 			val=eval(obj.value);			
	 			//alert("val="+val);
	 			if(val==0)
	 			{
	 				alert("Series of Zeroes is Not a Valid"+msg);
	 				obj.value="";
	 				obj.focus();
	 				return false;
	 			}
	 		}
	 	}
	 }
	 
	 function removeLoadingMask() {
		   jQuery("#loadingMask").dialog("close");
		}
	function loadingMask() {
		   jQuery("#loadingMask").dialog({
		       modal: true,
		       width: 250,
		       height: 90,
		       position: [(window.width / 2),100],
		       closeOnEscape: false,
		       resizable: false,
		       open: function(event, ui) {
		           jQuery(".ui-dialog-titlebar-close").hide();
		           jQuery(".ui-dialog-titlebar").hide();  
		       }
		   });
		}
	jQuery(document).ready(function() {
	       
	       jQuery( "form" ).submit(function( event ) {
	    	   loadingMask();
	       });
	       loadingMask();
	       
	               });
	jQuery(window).load(function () {
		removeLoadingMask();
		});
	
	jQuery(document).click(function() {
		if(this.activeElement.href) {
			var name = this.activeElement.href;
			if(name.indexOf(displayTagExportExcel)!=-1 || name.indexOf(displayTagExportPDF)!=-1){
				doLoadingMask();
				setTimeout(function() {
					undoLoadingMask();
				}, maskingTimeForDownloads)
			}
		}
		if(this.activeElement.href){
			var href = this.activeElement.href;
			if(href.indexOf("&page=")!=-1 ){
				doLoadingMask();
			}
		}
		if(this.activeElement.defaultValue){
			var name = this.activeElement.defaultValue;
			name = name.toUpperCase();
			if(name.indexOf("EXCEL")!=-1 || name.indexOf("PDF")!=-1 ){
				setTimeout(function() {
					undoLoadingMask();
				}, maskingTimeForDownloads)
			}
		}
		
	});
	 
	 
	
	
