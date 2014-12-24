
function initRequest() {
	if (window.XMLHttpRequest) {
		var req=new XMLHttpRequest();
		if (req.overrideMimeType) 
			req.overrideMimeType("text/html;charset=utf-8");
		return req;
	} else if (window.ActiveXObject) {
		isIE = true;
		return new ActiveXObject("Microsoft.XMLHTTP");
	}
}


/* This function checks whether entered field is unique or not.
 * It returns a boolean value if it is unique, 
 * it returns true else false 
 * */

function uniqueIdentifierBoolean(url,tablename,columnname,fieldobj,uppercase,lowercase)
{
	var fieldvalue = document.getElementById(fieldobj).value;
	var isUnique;
		
	if(url != "" && tablename != "" && columnname != "" && fieldvalue != "" && uppercase != "" && lowercase != "")
	{
	fieldvalue = trimFieldValue(fieldvalue);
	var link = ""+url+"?tablename=" + tablename+"&columnname=" + columnname+ "&fieldvalue=" + fieldvalue+ "&uppercase=" + uppercase+ "&lowercase=" + lowercase+ " ";
	var request = initRequest();
	request.open("GET", link, false);
	request.send(null);
	
		if (request.readyState == 4) 
		{
			if (request.status == 200) 
			{
				var response=request.responseText.split("^");
				
				if(response[0]=="false")
				{
					isUnique=false;
	 			}
	 			else
	 			isUnique=true;
			}
		}
	}
	
	return isUnique;
}

function trimFieldValue(value) {
   if(value!=undefined)
   {

	   while (value.charAt(value.length-1) == " ")
	   {
		value = value.substring(0,value.length-1);
		
	   }
	   while(value.substring(0,1) ==" ")
	   {
		value = value.substring(1,value.length);
		
	   }	   
   }
   
   return value ;
}

