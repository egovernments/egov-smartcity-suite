

/*
 *  This method is to validate special characters
 */

	function checkSpecialCharacters(obj)
	{
		var iChars = "!@$%^*+=[']\\';{}|\":<>?#&()";
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
  *  This is to validate pincode 
  */

	function validatePincode(obj)
	{
		
		if(!isNum(obj.value))
			return false;
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
	
	/** 
	 * This API returns the year from a date.
	 * @param strDate
	 * @return
	 */
	
	function getYearByDate(strDate) 
	{
		var eventYear;
		if (null != strDate && strDate != "")
		{
			var ndxYear = strDate.lastIndexOf('/') + 1;
			var strYear = strDate.substr(ndxYear);
			if (parseInt(strYear))
			{
				 eventYear = parseInt(strYear);
			}
		}
		return eventYear;
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
			alert("Please enter 10 digit valid Contact Number");
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
	
	function checkUptoTwoDecimalPlace(obj,errordivname,labelname)
	{
		if(obj.value!=""){
  			
			if (isNaN(obj.value)) {
  				obj.value = "";
				showError(labelname+" should be Numeric",errordivname);
			}
		
			else if (obj.value < 0) {
				obj.value = "";
				showError(labelname+" can not be Negative",errordivname);
			}
			
			else if(trimAll(obj.value)!="" && String(obj.value).indexOf(".") !=-1 && (String(obj.value).indexOf(".") < String(obj.value).length - 3)) {
			 	obj.value="";
				showError(labelname+" can only be 2 decimal places at most.",errordivname);
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
	
	/**
	 * 
	 * @param obj
	 * @param errordivname
	 * @param msg
	 * @return
	 * This method will allow numbers like maximum digit before decimal point like 123.98
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
	
	
	
	function checkNoOfDigitBeforeAndAfterDecimal(obj,errordivname,msg,before,after)
	{
		
		if(trimAll(obj.value)!=""){
			
			if (isNaN(obj.value)) {
  				obj.value = "";
				showError(msg,errordivname);
			}
		
			else if (obj.value <=0) {
				obj.value = "";
				showError(msg,errordivname);
			}
			
			else if(String(obj.value).indexOf(".") !=-1 && (String(obj.value).indexOf(".") < String(obj.value).length - (after+1))) {
			 	obj.value="";
				showError(msg,errordivname);
			}
			
			if(String(obj.value).indexOf(".") ==-1 && String(obj.value).length>before){
				obj.value = "";
				showError(msg,errordivname);
				return false;
			}
			else if(String(obj.value).indexOf(".") !=-1){
				var rate=obj.value.split(".");
				if(rate[0].length>before){
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
	
	function validateDateWithCurrentDate(deedDate)
	{
	 	var today = new Date();
	 	if (null != deedDate)
	 	{
	 	   if(((new Date(formatDate(deedDate))) - (new Date(today)))>0)
	 	   {
	 	     return false;
	 	   }
	 	   
	 	}
	 	return true; 
	}

	function compareFirstDateGtThanSecondDate(eventDate,deedDate)
	{
	 	if (null != eventDate && null != deedDate )
	 	{
	 	   if(((new Date(formatDate(eventDate))) - (new Date(formatDate(deedDate))))>0)
	 	   {
	 		   return false;
	 	   }
	 	   
	 	 }
	 	return true; 
	}
	
	
	
	