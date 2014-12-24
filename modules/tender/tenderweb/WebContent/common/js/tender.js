
	function showWaiting() {
		document.getElementById('loading').style.display = 'block';
	}
	
	/**
	 * This method is to check mandatory field
	 * @param fieldId-id of the field to validate
	 * @param errordiv-div name to display error message
	 * @return
	 */
	
	function checkStringMandatoryField(fieldId,label, errordiv) {
		if (dom.get(errordiv) != null) {
			dom.get(errordiv).style.display = 'none';
	}
	
	if (trimAll(document.getElementById(fieldId).value) == ""
			|| document.getElementById(fieldId).value == "-1") {
		return showError(label + " is Required ", errordiv);
		}
	else
		return true;
	
	}
	
	/**
	 * This method is to show error msg 
	 * @param msg
	 * @param errordivname
	 * @return
	 */
	
	function showError(msg, errordivname) {
		if (dom.get(errordivname) != null) {
			dom.get(errordivname).style.display = '';
			document.getElementById(errordivname).innerHTML = msg;
		} else
			alert(msg);
		return false;
	}
	
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
	 /*
		 *   This method will be useful to validate the Number
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
		
		function checkUptoTwoDecimalPlace(obj,errordivname,labelname)
		{
			if (dom.get(errordivname) != null) 
				dom.get(errordivname).style.display = 'none';
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
		
		function checkOnlyFourDigitBeforeDecimal(obj,errordivname,msg)
		{
			if(obj.value!=""){
		
				if(String(obj.value).indexOf(".") ==-1 && String(obj.value).length>4){
					obj.value = "";
					showError(msg,errordivname);
					return false;
				}
				
				else if(String(obj.value).indexOf(".") !=-1){
				    var rate=obj.value.split(".");
				    if(rate[0].length>4){
				    	obj.value = "";
						showError(msg,errordivname);
						return false;
					}
				}
			}
			return true;
		}
		
		function checkUptoFourDecimalPlace(obj,errordivname,labelname)
		{
			if (dom.get(errordivname) != null) 
				dom.get(errordivname).style.display = 'none';
			if(obj.value!=""){
	  			if (isNaN(obj.value)) { 
	  				obj.value = "";
					showError(labelname+" should be Numeric",errordivname);
					return false;
				}
			
				else if (obj.value < 0) { 
					obj.value = "";
					showError(labelname+" can not be Negative",errordivname);
					return false;
				}
				
				else if(trimAll(obj.value)!="" && String(obj.value).indexOf(".") !=-1 && (String(obj.value).indexOf(".") < String(obj.value).length - 5)) {
				 	obj.value="";
					showError(labelname+" can only be 4 decimal places at most.",errordivname);
					return false;
				}
			}
			return true;
		}
		
		function checkPhoneNumberContent(obj,errordiv)
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
				showError("Please Enter Valid Contact Number",errordiv);
				obj.value="";
				obj.focus();
			}
		}
		return;
		}
		
		
		function checkSpecialCharactersInName(obj,errordiv,labelname)
		{
			var iChars = "!@$%^*+=[']\\';{}|\":<>?#&()";
			for (var i = 0; i < obj.value.length; i++)
			{
				if (iChars.indexOf(obj.value.charAt(i)) != -1)
				{
					obj.value="";
					showError(labelname+" should not contain any special Characters",errordiv);
					return false;
				}
			}
			return true;
		}
		
		/**
		 * This method is to refresh the inbox...
		 * @return
		 */
		
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
		
		