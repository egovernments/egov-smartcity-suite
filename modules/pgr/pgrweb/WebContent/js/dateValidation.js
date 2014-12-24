
//If fromDate is greater than toDate retuns false else true 
//format dd/mm/yyyy
function checkGreater(fromDate,toDate)
	{
		//alert("checkGreater fromDate :"+fromDate+": to date :"+toDate);
		if(fromDate!= ""  && toDate!= "")
		{
			//var fromDate=value;
			var fromDateArr=fromDate.split("/");
			var toDateArr=toDate.split("/");

			var fromDateObj=new Date(fromDateArr[2],fromDateArr[1]-1,fromDateArr[0]);
			var toDateObj=  new Date(toDateArr[2],toDateArr[1]-1,toDateArr[0]);
			if(fromDateObj>toDateObj)
				return false;
			else 
				return true;
		}
		else
			return false;

	}
//checks whether passed value is grater than currentDate 
//format dd/mm/yyyy
	function validateCurrDate(value)
	{
		//alert("checkGreater Date :"+value);
		if(value!="")
		{
			var fromDate=value;
			var fromDateArr=fromDate.split("/");
			var fromDateObj=new Date(fromDateArr[2],fromDateArr[1]-1,fromDateArr[0]);
			var currDateObj=new Date();
			if(fromDateObj>currDateObj)
				return false;
			else 
				return true;
		}
		else
			return false;
	}









/**
*@Params FromDate and ToDate
**/
function enable_date(obj,o1,o2)

{

	//var value = document.AggCollDateForm.dateSelection.value ;
	var value = obj.value ;

	if(value == 0)
	{
		  //o1.disabled = true;
		  //o2.disabled = true;
		  o1.readOnly = true;
		  o2.readOnly = true;
		  
	}
	else if(value == 1)
	{
		//o1.disabled = true;
		//o2.disabled = false;		
		o1.readOnly = true;
		o2.readOnly = false;
	}
	else if(value == 2)
	{
		//o2.disabled = true;
		//o1.disabled = false;	
		o2.readOnly = true;
		o1.readOnly = false;
		
	}
	else if(value == 4)
	{
		  //o1.disabled = true;
		  //o2.disabled = true;
		  o1.readOnly = true;
		  o2.readOnly = true;
	}
    	else if(value == 3)
	{
	    //o1.disabled = false;
	    //o2.disabled = false;
	    o1.readOnly = false;
	    o2.readOnly = false;
	}
}

	
/**
* Checks whether the From date is greater than  To date
**/
function checkFdateTdate(fromDate,toDate)
{
	//ENTERED DATE FORMAT DD/MM/YYYY	

	//alert('From Year'+fromDate.substr(6,4)+'To'+toDate.substr(6,4));
	//alert('From Month'+fromDate.substr(0,2)+'To'+toDate.substr(0,2));
	//alert('From Date'+fromDate.substr(3,2)+'To'+toDate.substr(3,2));
	
	if(fromDate.substr(6,4) > toDate.substr(6,4))
	{

		 alert("From Year returning false" );
		return false;
	}
	
	if(fromDate.substr(3,2) > toDate.substr(3,2))
	{
		 alert("From Month returning false" );
			return false;
	}
	   
	if(fromDate.substr(0,2) > toDate.substr(0,2))
	{
		alert("From day returning false" );
	 return false;
	}	
	else 
	{
	return true;
	}
	
}
  

/**
* Checks whether the date entered is greater than the current date 
**/
function validateDate(strValue)
{
    /*if(!validateEnteredDate(strValue))
        return false;*/
    var today = new Date();
    var day = today.getDate();
    var month = today.getMonth();
    var year = today.getYear()+1900;
    
    //SYS DATE FORMAT DD/MM/YYYY
    //ENTERED DATE FORMAT DD/MM/YYYY
    
  //alert('ENTERED YEAR='+strValue.substr(6,4)+'YEAR='+year);
    //alert('ENTERED MONTH='+strValue.substr(0,2)+'MONTH='+month);
    //alert('ENTERED DATE='+strValue.substr(3,2)+'DAY='+day);

    if(strValue!="")
    {
        //Check for year
        //If entered year is less than the current year return true
      if ( strValue.substr(6,4) > year)
        {
            //alert('pls enter the valid date Year');
            return false;
        }
		if(strValue.substr(3,2)> (month+1))
		{
			//alert('pls enter the valid date month');
			return false;
		}
		if (strValue.substr(3,2) == (month+1) && strValue.substr(0,2)>day)
            {
            	//alert('pls enter the valid daten :date');
            	return false;
            }
            
            /*if(document.AggCollDateForm.fdate.value.substr(3,2)==(month+1) && document.AggCollDateForm.fdate.value.substr(0,2)==day)
            {
            	return false;
            }*/
            
            else
            {   
                return true;
            }
        }
        
    }
    


/**
*Change date format from MM-dd-yyyy to dd-MM-yyyy
*/

function changeDateFormat(obj)
{
	var dateStr="";
	if(obj.value != null && obj.value != "")
	{
		//alert('Year'+obj.value.substr(6,4));
		//alert('Month'+obj.value.substr(0,2));
		//alert('Date'+obj.value.substr(3,2));
		dateStr=obj.value.substr(3,2)+"-"+obj.value.substr(0,2)+"-"+obj.value.substr(6,4);
		//alert("dateStr="+dateStr);		
		if(!validateDate(dateStr))
		{
			alert("Date should be less than or equal to Current date");
			obj.value="";
			//obj.focus();
			return false;
		}
		else
			return true;
		
	}
	return true;
}


/**
*Change date format from MM-dd-yyyy to dd-MM-yyyy
*/

function changeDateFormatValue(objValue)
{
	//alert('objValue='+objValue);
	var dateStr="";
	if(objValue != null && objValue != "")
	{
		//alert('Year'+objValue.substr(6,4));
		//alert('Month'+objValue.substr(0,2));
		//alert('Date'+objValue.substr(3,2));
		dateStr=objValue.substr(3,2)+"-"+objValue.substr(0,2)+"-"+objValue.substr(6,4);		
	}
	return dateStr;
}

/*
* Check for Valid Date after changing date format
*/
function validDate(obj)
{
	//alert("Inside validDate");
	if(obj.value != null && obj.value != "")
	{
		if(!changeDateFormat(obj))
			return false;
		else
			return true;
	}
	else
		return true;
}

/**
* Check for the valid characters in the date fields 
**/
function checkDate(obj)
{

var validChars="0123456789/";
var dt=obj.value;
var len= 0;
var invalid=false;

if(obj.readOnly==false)
{
if(dt!="" && dt!=null)
{
	len= dt.length;
	for(var i=0;i<len && invalid==false;i++)
	{
		chars=dt.charAt(i);
		
		if(validChars.indexOf(chars)==-1)
		{			
			invalid=true;
		}
	}
	
	if(invalid==true)
	{
		alert("Please enter the valid characters");
		obj.value="";
		obj.focus();
	}
}
}
return;
}

/**
* Check whether the values entered in the date fields are in dd/MM/yyyy format 
* and whether the dd, MM and yyyy values are valid
**/
function validateDateFormat(obj)
{
 var dtStr=obj.value;
 var year;
 var day;
 var month;
 var leap=0;
 var valid=true;
 var oth_valid=true;
 var feb=false;
 var validDate=true;
 var Ret=true;
  if(obj.readOnly==false)
  {
    if(dtStr!="" && dtStr!=null)
    {
    	year=dtStr.substr(6,4);
    	month=dtStr.substr(3,2);
    	day=dtStr.substr(0,2);
    	
    	//alert("day="+day+" month="+month);
    	if(year=="0000" || year<1900 || month=="00" || day=="00" || dtStr.length<10)
    	{
    		validDate=false;
    	}
    		
    	if(validDate==true)
    	{
    		//if(year>1900 && year<=
    		leap=year%4;
 		
 		//alert("Hi Feb LEAP="+leap);
 		if(leap==0 && month=="02")
    		{
    			if(day>29)
    			{
    				valid=false;
    				feb=true;
    			}
    			//alert("Leap Year");    			
    			/*if(month=="2") // || month==02)
    			{
    				valid=false;
    				feb=true;
    			}
    			else if(month=="02")
    			{
    				if(day>29)
    				{
    					valid=false;
    					feb=true;
    				}
    			}*/
    			
    		}
    	
    		else if(month=="02" && day>28)
    		{    
    			valid=false;
    			feb=true;
    			
    			/*if(month=="2")
    			{
    				valid=false;
    				feb=true;
    			}
    			else if(month=="02")
    			{
    				if(day>28)
    				{
    					valid=false;
    					feb=true;
    				}
    			}*/
    		}
    		
    		if(feb==false)
    		{    			
    			if(month=="03" || month=="01" || month=="05" || month=="07" || month=="08" || month=="10" || month=="12")
    			{
    				if(day>31)
    				{    					
    					oth_valid=false;
    				}
    			}
    		
    			else if(month=="04" || month==06 || month=="09" || month=="11") 
    			{
    				if(day>30)
    				{    					
    					oth_valid=false;
    				}
    			}
    		
    			else
    			{
    				oth_valid=false;
    			}
    		
    		}
    	}
    }	
   
    if(valid==false || oth_valid==false || validDate==false)
    {
    	alert("Please enter the valid date in the dd/MM/yyyy Format only");
    	obj.value="";
    	obj.focus();
    	Ret=false;
    }
    return Ret;
   }
}


