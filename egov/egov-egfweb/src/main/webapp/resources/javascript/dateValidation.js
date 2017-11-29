/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

function validateDateBeforeSubmit(dateSelObj,fdateObj,tdateObj)
{

	var rType;
	var rTo;
	var rFrom;
	var rTF;
	//bootbox.alert("JSSSSSSSSSSSSS");
	//bootbox.alert(tdateObj.value+fdateObj.value+dateSelObj.value);
	/*if(dateSelObj.value == "-1")
	{
			bootbox.alert("Please select the date");
			dateSelObj.focus();
			return false;
	}*/

	if((fdateObj.value == "" || tdateObj.value == "") && dateSelObj.value == "3")
	{
		bootbox.alert("Please fill the date parameters correctly");
		fdateObj.focus();
		return false;
	}
	if(tdateObj.value == "" && dateSelObj.value == "1")
	{
		bootbox.alert("To date is necessary");
		tdateObj.focus();
		return false;
	}

	if(fdateObj.value == ""  && dateSelObj.value == "2")
	{
		bootbox.alert("From date is necessary");
		fdateObj.focus();
		return false;
	}
	
	/****** Validation of entered date *******/
	
	if(dateSelObj.value == "1" && tdateObj.value != "")
	{
		rType=validateDate(tdateObj.value);
		if(rType==false)
		{
			bootbox.alert('Please enter the valid To-date');
			tdateObj.value="";
			tdateObj.focus();
			return false;
		}		
	}	
			
	if(dateSelObj.value == "2" && fdateObj.value != "")
	{
		rType=validateDate(fdateObj.value);
		if(rType==false)
		{
			bootbox.alert('Please enter the valid From-date');
			fdateObj.value="";
			fdateObj.focus();
			return false;
		}				
	}	
					
	if(dateSelObj.value == "3" && fdateObj.value != "" && tdateObj.value != "")
	{
		rFrom=validateDate(fdateObj.value);
		rTo=validateDate(tdateObj.value);
		
		//bootbox.alert('I m here');
		if(rFrom==false || rTo==false)
		{
			bootbox.alert('Please Enter Valid Date');
			if(rFrom==false)
			{
				fdateObj.value="";
				fdateObj.focus();
			}
				
			if(rTo==false)
			{
				tdateObj.value="";
				tdateObj.focus();
			}
			return false;
		}
		
		else
		{
			//bootbox.alert('I m inside F & T date');
			rTF=checkFdateTdate(fdateObj.value,tdateObj.value);
			if(rTF==false)
			{
				bootbox.alert('From date should be less than or equal to To Date');
				//fdateObj.value="";
				fdateObj.focus();
				return false;
			}			
			
		}
		
	}	
	
	return true;

}

/*
* Added By Rajalakshmi D.N. on 06/02/2007
* Checks whether the fromDate is necessary based on dateSelection criteria
* @param dateSelection obj
*/

function myFromDate(dateSelObj)
{

	if(dateSelObj.value == "-1")
	{
		bootbox.alert("Please select the date");
		dateSelObj.focus();
		return false;
	}
	if(dateSelObj.value == "0")
	{
		
		bootbox.alert("From Date is not necessary when Date Selection is All Dates");
		dateSelObj.focus();
		return false;
	}
	
	else if(dateSelObj.value == "4")
	{
			
			bootbox.alert("From Date is not necessary when Date Selection is Current Date");
			dateSelObj.focus();
			return false;
	}
	else if(dateSelObj.value == "1")
	{
			bootbox.alert("From Date is not necessary when Date Selection is Before");
			dateSelObj.focus();
			return false;
		
	}
	return true;
}

/*
* Added By Rajalakshmi D.N. on 06/02/2007
* Checks whether the toDate is necessary based on dateSelection criteria
* @param dateSelection obj
*/
function myToDate(dateSelObj)
{
	if(dateSelObj.value == "-1")
	{
		bootbox.alert("Please select the date");
		dateSelObj.focus();
		return false;
	}

	if(dateSelObj.value == "0")
	{		
		bootbox.alert("To Date is not necessary when Date Selection is  All Dates");
		dateSelObj.focus();
		return false;
	}
	
	else if(dateSelObj.value == "4")
	{			
		bootbox.alert("To Date is not necessary when Date Selection is  Current Date");
		dateSelObj.focus();
		return false;
	}
	else if(dateSelObj.value == "2")
	{
					
		bootbox.alert("To Date is not necessary when Date Selection is After");
		dateSelObj.focus();
		return false;
	}
	return true;
}


/**
* Added by Rajalakshmi D.N.
* @Params FromDate and ToDate
**/
function enable_date(obj,o1,o2)

{

	//var value = document.AggCollDateForm.dateSelection.value ;
	var value = obj.value ;

	if(value == 0)
	{
		  //o1.disabled = true;
		  //o2.disabled = true;
		  o1.value="";
		  o2.value="";
		  o1.readOnly = true;
		  o2.readOnly = true;
		  
	}
	else if(value == 1)
	{
		//o1.disabled = true;
		//o2.disabled = false;	
		o1.value="";
		o2.value="";
		o1.readOnly = true;
		o2.readOnly = false;
	}
	else if(value == 2)
	{
		//o2.disabled = true;
		//o1.disabled = false;	
		o1.value="";
		o2.value="";
		o2.readOnly = true;
		o1.readOnly = false;
		
	}
	else if(value == 4)
	{
		  //o1.disabled = true;
		  //o2.disabled = true;
		  o1.value="";
		  o2.value="";
		  o1.readOnly = true;
		  o2.readOnly = true;
	}
    	else if(value == 3)
	{
	    //o1.disabled = false;
	    //o2.disabled = false;
		o1.value="";
		o2.value="";
	    o1.readOnly = false;
	    o2.readOnly = false;
	}
}

/**
* Checks whether the From date is greater than  To date
**/
function checkFdateTdate(fromDate,toDate)
{
	//ENTERED DATE FORMAT MM/DD/YYYY	

	//bootbox.alert('From Year'+fromDate.substr(6,4)+'To'+toDate.substr(6,4));
	//bootbox.alert('From Month'+fromDate.substr(0,2)+'To'+toDate.substr(0,2));
	//bootbox.alert('From Date'+fromDate.substr(3,2)+'To'+toDate.substr(3,2));
	
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
  
/*function validateDate(strValue)
{
    var today = new Date();
    var day = today.getDate();
    var month = today.getMonth();
    var year = today.getYear();
    
    //SYS DATE FORMAT DD/MM/YYYY
    //ENTERED DATE FORMAT DD/MM/YYYY
    
   //bootbox.alert('ENTERED YEAR='+strValue.substr(6,4)+'YEAR='+year);
   //bootbox.alert('ENTERED MONTH='+strValue.substr(0,2)+'MONTH='+month);
   //bootbox.alert('ENTERED DATE='+strValue.substr(3,2)+'DAY='+day);
        	//bootbox.alert();
    if(strValue!="")
    {
        //Check for year
        //If entered year is less than the current year return true
        if (strValue.substr(6,4) < year )
        {
             return true;
        }
        //If entered year is greater than the current year return false
        else if ( strValue.substr(6,4) > year)
        {
    
            return false;
        }
 
 	//If entered year is equal the current year return false
        else if( strValue.substr(6,4) == year )
        {
            if(strValue.substr(3,2)< (month+1))
            {            	
                 return true;
            }
            if(strValue.substr(3,2)> (month+1))
            {
                 return false;
            }
            if (strValue.substr(3,2) == (month+1) && strValue.substr(0,2)>day)
            {
               	return false;
            }
            
            if(document.AggCollModeForm.fdate.value.substr(3,2)==(month+1) && document.AggCollModeForm.fdate.value.substr(0,2)==day)
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
}*/


//Validates that the Date entered belongs to the givenfinancial year 
function validateFinancialYear(financialYear, collDateObj)
{
    //bootbox.alert('Inside validateFinancialYear'+financialYear);
    var collMonth = eval(collDateObj.substr(3,2));
    var collYear = eval(collDateObj.substr(8,2));
    var finYear = eval(financialYear);
    //bootbox.alert('collYear'+collYear);
   // bootbox.alert('collMonth'+collMonth);
  //  bootbox.alert('finYear'+finYear);
    
    //if(collMonth <= 3 && finYear == (collYear -1))
    
    if(collMonth > 3 && collYear == (finYear -1))
        return true;
    else if(collMonth <= 3 && finYear == collYear)
        return true
    else 
        return false;
    
}

function validateCollDateFinYear(collDateObj)
{	
    var today = new Date();
    var collYear=eval(collDateObj.substr(6,4));
    finYear=today.getYear();
    var FinMonth=today.getMonth();
    //bootbox.alert("FinMonth"+FinMonth+"finYear"+finYear);
    collMonth=eval(collDateObj.substr(3,2));
    if(collMonth>3 && collYear == finYear )
    return true;
    else if(collMonth<=3 && FinMonth<=2 && collYear == finYear)
    return true;
    else
    return false;
   
   
       
}       

/**
* Checks whether the date entered is within the current financial Year 
**/
/*function validateCollDateFinYear(strValue)
{
//bootbox.alert("Entered validateCollectionDate() fun");
    var today = new Date();
    var sys_day = today.getDate();
    var sys_month = today.getMonth();
    var sys_year = today.getYear();
    

//bootbox.alert("Entered validateCollectionDate() fun    "+sys_month);
sys_month=eval(sys_month)+eval(1);

    var coll_year=strValue.substr(6,4);
    var coll_month=strValue.substr(3,2);
    var coll_day=strValue.substr(0,2);
    
   if(sys_month>=04)
   {
   	sys_yearNext=eval(sys_year)+eval(1);
   	if(coll_month>=4)
   	{
   		if(coll_year<sys_year)
   		{
   			return false;
   		}
   	}
   }
   
   else if(sys_year<4)
   {
   	if(coll_month<4)
   	{
   		
   
   
        
    //bootbox.alert("SYS MONTH="+sys_month+" Coll_MONTH="+coll_month);
    if(coll_month>=4)
    {
    	//if(coll_year<sys_year)
    	if(coll_year<(eval(sys_year)-eval(1)))
    	{
    		return false;
    	}
    }
    else if(coll_month<=3)
    {
    	//if((coll_year!=sys_year) || (coll_year!=(eval(sys_year)-eval(1))))
    	//bootbox.alert("SYS YEAR="+sys_year+" Coll_YEAR="+coll_year);
    	if(coll_year!=(eval(sys_year)))
    	{
    		return false;
    	}
    }
    
    else
    {
    	return true;
    }
    
}

*/

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
    var year = today.getFullYear();
    
    //SYS DATE FORMAT DD/MM/YYYY
    //ENTERED DATE FORMAT DD/MM/YYYY
    bootbox.alert("HIIIIIIIIIII");
    
   /* bootbox.alert('ENTERED YEAR='+strValue.substr(6,4)+'YEAR='+year);
    bootbox.alert('ENTERED MONTH='+strValue.substr(0,2)+'MONTH='+month);
    bootbox.alert('ENTERED DATE='+strValue.substr(3,2)+'DAY='+day);*/

    if(strValue!="")
    {
        //Check for year
        //If entered year is less than the current year return true
        if (strValue.substr(6,4) < year )
        {
        	//bootbox.alert('ENTERED YEAR='+strValue.substr(6,4));
        	//bootbox.alert('YEAR='+year);
            return true;
        }
        //If entered year is greater than the current year return false
        else if ( strValue.substr(6,4) > year)
        {
            //bootbox.alert('pls enter the valid date');
            return false;
        }
        //If entered year is equal the current year return false
        else if( strValue.substr(6,4) == year )
        {
            if(strValue.substr(3,2)< (month+1))
            {
                 return true;
            }
            if(strValue.substr(3,2)> (month+1))
            {
            	//bootbox.alert('pls enter the valid date');
                return false;
            }
            if (strValue.substr(3,2) == (month+1) && strValue.substr(0,2)>day)
            {
            	//bootbox.alert('pls enter the valid date');
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
* Checks whether the date entered is greater than the current date 
* (Used when validation.xml is used for validation)
**/
function validateDateJS(obj)
{

    
    /*if(!validateEnteredDate(strValue))
        return false;*/

    var today = new Date();
    var day = today.getDate();
    var month = today.getMonth();
    var year = today.getYear();

     //SYS DATE FORMAT DD/MM/YYYY
    //ENTERED DATE FORMAT DD/MM/YYYY
    //bootbox.alert("HIIIIIIIIIII");
    
   /* bootbox.alert('ENTERED YEAR='+strValue.substr(6,4)+'YEAR='+year);
    bootbox.alert('ENTERED MONTH='+strValue.substr(0,2)+'MONTH='+month);
    bootbox.alert('ENTERED DATE='+strValue.substr(3,2)+'DAY='+day);*/


    var strValue=obj.value;
    

    if(strValue!="")
    {
        //Check for year
        //If entered year is less than the current year return true
        if (strValue.substr(6,4) < year )
        {
        	//bootbox.alert('ENTERED YEAR='+strValue.substr(6,4));
        	//bootbox.alert('YEAR='+year);
            return true;
        }
        //If entered year is greater than the current year return false
        else if ( strValue.substr(6,4) > year)
        {
            bootbox.alert('pls enter the valid date');
            obj.focus();
            return false;
        }
        //If entered year is equal the current year return false
        else if( strValue.substr(6,4) == year )
        {
            if(strValue.substr(3,2)< (month+1))
            {
                  return true;
            }
            if(strValue.substr(3,2)> (month+1))
            {
            	bootbox.alert('pls enter the valid date');
            	obj.focus();
                return false;
            }
           
           if (strValue.substr(3,2) == (month+1) && strValue.substr(0,2)>day)
            {
            	bootbox.alert('pls enter the valid date');
            	obj.focus();
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
		bootbox.alert("Please enter the valid characters");
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
    	if(dtStr.indexOf("/")=="2" && dtStr.lastIndexOf("/")=="5")    	
    	validDate=true;    	
    	else    	
    	validDate=false;
    	checkDate(obj);
    	
    	//bootbox.alert("day="+day+" month="+month);
    	if(year=="0000" || year<1900 || month=="00" || day=="00" || dtStr.length!=10)
    	{
    		validDate=false;
    	}
    		
    	if(validDate==true)
    	{
    		//if(year>1900 && year<=
    		leap=year%4;
 		
 		//bootbox.alert("Hi Feb LEAP="+leap);
 		 if(month=="02")
		 {
		  		//bootbox.alert("dffffffffffffg");
		 		feb=true;
		 }

 		
 		if(leap==0 && month=="02")
    		{
    			//bootbox.alert("111111111111111111111");
    			if(day>29)
    			{
    				valid=false;
    				feb=true;
    			}
    			//bootbox.alert("Leap Year");    			
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
    			//bootbox.alert("222222222222222222");
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
    			//bootbox.alert("33333333333333333333333");
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
   
   //bootbox.alert("valid="+valid+" oth_valid="+oth_valid+" validDate="+validDate);
    if(valid==false || oth_valid==false || validDate==false)
    {
    	bootbox.alert("Please enter the valid date in the dd/MM/yyyy Format only");
    	obj.value="";
    	obj.focus();
    	Ret=false;
    }
    return Ret;
   }
}

/**
* Checks whether the collection date is within the selected payments'financial year
* RETURN TYPE:: true -- if within the financial year......Else returns false
**/

function validateCollectionDate(paymentYear,collectionDate)
{
	var pmntYear=paymentYear;
	var collDate=collectionDate;		
	
	var prevYear=pmntYear.substr(0,4);
	var nextYear=eval(prevYear)+eval(0001);
	
	/*bootbox.alert("Payment year="+pmntYear+"CollectionYear="+collectionDate);	
	bootbox.alert("Substring Year="+prevYear+"Substring Collection="+collDate.substr(6,4));	
	bootbox.alert("Financial Year="+prevYear+"-"+nextYear);*/
	
	if(collDate.substr(6,4) > nextYear || collDate.substr(6,4) <prevYear)
	{
		//bootbox.alert("The collection date should be between the payment year!!");
		//obj.value="";
		return false;
	}
	
	else if(collDate.substr(6,4)<=nextYear && collDate.substr(6,4)>=prevYear)
	{
		if(collDate.substr(6,4)==nextYear)
		{
			//bootbox.alert("ENTERED NEXT YEAR");
			if(collDate.substr(3,2)>3)
			{
				return false;
			}
			else
			{
				if(collDate.substr(3,2)<3)				
				{
					return true;
				}
				
				if(collDate.substr(3,2)==3)				
				{
					// R if(collDate.substr(0,2)<=31)
					if(collDate.substr(0,2)<=day)
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
		}

		if(collDate.substr(6,4)==prevYear)
		{
			//bootbox.alert("ENTERED PREVIOUS YEAR");
			if(collDate.substr(3,2)<4)
			{
				return false;
			}
			else
			{
				if(collDate.substr(3,2)>4)
				{
					return true;
				}
				if(collDate.substr(3,2)==4)
				{
					if(collDate.substr(0,2)>=1)
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
		}
		
	}
			
	else 
	{
		return true;
	}	 
	
}

/**
* Checks whether the collection date is greater than the 01 April of the first year of the 
* financial year selected and <=current date 
* RETURN TYPE:: true -- if within the financial year......Else returns false
**/

function validateCollectionDateCurrenDate(paymentYear,collectionDate)
{
	var pmntYear=paymentYear;
	var collDate=collectionDate;		
	
	var prevYear=pmntYear.substr(0,4);
	
	//bootbox.alert("Ins Year="+paymentYear);
	if(isNaN(prevYear) && pmntYear!="Choose")
	{
		//bootbox.alert("Prior");
		prevYear="2000";
		//return false;
	}
	
	var nextYear=eval(prevYear)+eval(0001);
	
	/******Newly added for the collection date<=todays date and >01April of the financial year selected ***/
	var today = new Date();
   	var day = today.getDate();
   	var month = today.getMonth();
   	var year = today.getYear();
	month=eval(month)+eval(1);
	nextYear=year;
	/***********************************************/
	/*bootbox.alert("Payment year="+pmntYear+"CollectionYear="+collectionDate);	
	bootbox.alert("Substring Year="+prevYear+"Substring Collection="+collDate.substr(6,4));	
	bootbox.alert("Financial Year="+prevYear+"-"+nextYear);*/
	
	if(collDate.substr(6,4) > nextYear || collDate.substr(6,4) <prevYear)
	{
		//bootbox.alert("The collection date should be between the payment year!!");
		//obj.value="";
		return false;
	}
	
	else if(collDate.substr(6,4)<=nextYear && collDate.substr(6,4)>=prevYear)
	{
		if(collDate.substr(6,4)==nextYear)
		{
			//bootbox.alert("ENTERED NEXT YEAR");
			//R if(collDate.substr(3,2)>3)
			if(collDate.substr(3,2)>month)
			{
				return false;
			}
			else
			{
				//R if(collDate.substr(3,2)<3)
				if(collDate.substr(3,2)<month)
				{
					return true;
				}
				//R if(collDate.substr(3,2)==3)
				if(collDate.substr(3,2)==month)
				{
					// R if(collDate.substr(0,2)<=31)
					if(collDate.substr(0,2)<=day)
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
		}

		if(collDate.substr(6,4)==prevYear)
		{
			//bootbox.alert("ENTERED PREVIOUS YEAR");
			if(collDate.substr(3,2)<4)
			{
				return false;
			}
			else
			{
				if(collDate.substr(3,2)>4)
				{
					return true;
				}
				if(collDate.substr(3,2)==4)
				{
					if(collDate.substr(0,2)>=1)
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
		}
		
	}
			
	else 
	{
		return true;
	}
		 
	
}

// ===================================================================
// Author: Matt Kruse <matt@mattkruse.com>
// WWW: http://www.mattkruse.com/
//
// NOTICE: You may use this code for any purpose, commercial or
// private, without any further permission from the author. You may
// remove this notice from your final code if you wish, however it is
// appreciated by the author if at least my web site address is kept.
//
// You may *NOT* re-distribute this code in any way except through its
// use. That means, you can include it in your product, or your web
// site, or any other form where the code is actually being used. You
// may not put the plain javascript up on your site for download or
// include it in your javascript libraries for download. 
// If you wish to share this code with others, please just point them
// to the URL instead.
// Please DO NOT link directly to my .js files from your site. Copy
// the files to your server and use them there. Thank you.
// ===================================================================

// HISTORY
// ------------------------------------------------------------------
// May 17, 2003: Fixed bug in parseDate() for dates <1970
// March 11, 2003: Added parseDate() function
// March 11, 2003: Added "NNN" formatting option. Doesn't match up
//                 perfectly with SimpleDateFormat formats, but 
//                 backwards-compatability was required.

// ------------------------------------------------------------------
// These functions use the same 'format' strings as the 
// java.text.SimpleDateFormat class, with minor exceptions.
// The format string consists of the following abbreviations:
// 
// Field        | Full Form          | Short Form
// -------------+--------------------+-----------------------
// Year         | yyyy (4 digits)    | yy (2 digits), y (2 or 4 digits)
// Month        | MMM (name or abbr.)| MM (2 digits), M (1 or 2 digits)
//              | NNN (abbr.)        |
// Day of Month | dd (2 digits)      | d (1 or 2 digits)
// Day of Week  | EE (name)          | E (abbr)
// Hour (1-12)  | hh (2 digits)      | h (1 or 2 digits)
// Hour (0-23)  | HH (2 digits)      | H (1 or 2 digits)
// Hour (0-11)  | KK (2 digits)      | K (1 or 2 digits)
// Hour (1-24)  | kk (2 digits)      | k (1 or 2 digits)
// Minute       | mm (2 digits)      | m (1 or 2 digits)
// Second       | ss (2 digits)      | s (1 or 2 digits)
// AM/PM        | a                  |
//
// NOTE THE DIFFERENCE BETWEEN MM and mm! Month=MM, not mm!
// Examples:
//  "MMM d, y" matches: January 01, 2000
//                      Dec 1, 1900
//                      Nov 20, 00
//  "M/d/yy"   matches: 01/20/00
//                      9/2/00
//  "MMM dd, yyyy hh:mm:ssa" matches: "January 01, 2000 12:30:45AM"
// ------------------------------------------------------------------

var MONTH_NAMES=new Array('January','February','March','April','May','June','July','August','September','October','November','December','Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec');
var DAY_NAMES=new Array('Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sun','Mon','Tue','Wed','Thu','Fri','Sat');
function LZ(x) {return(x<0||x>9?"":"0")+x}

// ------------------------------------------------------------------
// isDate ( date_string, format_string )
// Returns true if date string matches format of format string and
// is a valid date. Else returns false.
// It is recommended that you trim whitespace around the value before
// passing it to this function, as whitespace is NOT ignored!
// ------------------------------------------------------------------
function isDate(val,format) {
	var date=getDateFromFormat(val,format);
	if (date==0) { return false; }
	return true;
	}

// -------------------------------------------------------------------
// compareDates(date1,date1format,date2,date2format)
//   Compare two date strings to see which is greater.
//   Returns:
//   1 if date1 is greater than date2
//   0 if date2 is greater than date1 of if they are the same
//  -1 if either of the dates is in an invalid format
// -------------------------------------------------------------------
function compareDates(date1,dateformat1,date2,dateformat2) {
	var d1=getDateFromFormat(date1,dateformat1);
	var d2=getDateFromFormat(date2,dateformat2);
	if (d1==0 || d2==0) {
		return -1;
		}
	else if (d1 > d2) {
		return 1;
		}
	return 0;
	}

// ------------------------------------------------------------------
// formatDate (date_object, format)
// Returns a date in the output format specified.
// The format string uses the same abbreviations as in getDateFromFormat()
// ------------------------------------------------------------------
function formatDate(date,format) {
	format=format+"";
	var result="";
	var i_format=0;
	var c="";
	var token="";
	var y=date.getYear()+"";
	var M=date.getMonth()+1;
	var d=date.getDate();
	var E=date.getDay();
	var H=date.getHours();
	var m=date.getMinutes();
	var s=date.getSeconds();
	var yyyy,yy,MMM,MM,dd,hh,h,mm,ss,ampm,HH,H,KK,K,kk,k;
	// Convert real date parts into formatted versions
	var value=new Object();
	if (y.length < 4) {y=""+(y-0+1900);}
	value["y"]=""+y;
	value["yyyy"]=y;
	value["yy"]=y.substring(2,4);
	value["M"]=M;
	value["MM"]=LZ(M);
	value["MMM"]=MONTH_NAMES[M-1];
	value["NNN"]=MONTH_NAMES[M+11];
	value["d"]=d;
	value["dd"]=LZ(d);
	value["E"]=DAY_NAMES[E+7];
	value["EE"]=DAY_NAMES[E];
	value["H"]=H;
	value["HH"]=LZ(H);
	if (H==0){value["h"]=12;}
	else if (H>12){value["h"]=H-12;}
	else {value["h"]=H;}
	value["hh"]=LZ(value["h"]);
	if (H>11){value["K"]=H-12;} else {value["K"]=H;}
	value["k"]=H+1;
	value["KK"]=LZ(value["K"]);
	value["kk"]=LZ(value["k"]);
	if (H > 11) { value["a"]="PM"; }
	else { value["a"]="AM"; }
	value["m"]=m;
	value["mm"]=LZ(m);
	value["s"]=s;
	value["ss"]=LZ(s);
	while (i_format < format.length) {
		c=format.charAt(i_format);
		token="";
		while ((format.charAt(i_format)==c) && (i_format < format.length)) {
			token += format.charAt(i_format++);
			}
		if (value[token] != null) { result=result + value[token]; }
		else { result=result + token; }
		}
	return result;
	}
	
// ------------------------------------------------------------------
// Utility functions for parsing in getDateFromFormat()
// ------------------------------------------------------------------
function _isInteger(val) {
	var digits="1234567890";
	for (var i=0; i < val.length; i++) {
		if (digits.indexOf(val.charAt(i))==-1) { return false; }
		}
	return true;
	}
function _getInt(str,i,minlength,maxlength) {
	for (var x=maxlength; x>=minlength; x--) {
		var token=str.substring(i,i+x);
		if (token.length < minlength) { return null; }
		if (_isInteger(token)) { return token; }
		}
	return null;
	}
	
// ------------------------------------------------------------------
// getDateFromFormat( date_string , format_string )
//
// This function takes a date string and a format string. It matches
// If the date string matches the format string, it returns the 
// getTime() of the date. If it does not match, it returns 0.
// ------------------------------------------------------------------
function getDateFromFormat(val,format) {
	val=val+"";
	format=format+"";
	var i_val=0;
	var i_format=0;
	var c="";
	var token="";
	var token2="";
	var x,y;
	var now=new Date();
	var year=now.getYear();
	var month=now.getMonth()+1;
	var date=1;
	var hh=now.getHours();
	var mm=now.getMinutes();
	var ss=now.getSeconds();
	var ampm="";
	
	while (i_format < format.length) {
		// Get next token from format string
		c=format.charAt(i_format);
		token="";
		while ((format.charAt(i_format)==c) && (i_format < format.length)) {
			token += format.charAt(i_format++);
			}
		// Extract contents of value based on format token
		if (token=="yyyy" || token=="yy" || token=="y") {
			if (token=="yyyy") { x=4;y=4; }
			if (token=="yy")   { x=2;y=2; }
			if (token=="y")    { x=2;y=4; }
			year=_getInt(val,i_val,x,y);
			if (year==null) { return 0; }
			i_val += year.length;
			if (year.length==2) {
				if (year > 70) { year=1900+(year-0); }
				else { year=2000+(year-0); }
				}
			}
		else if (token=="MMM"||token=="NNN"){
			month=0;
			for (var i=0; i<MONTH_NAMES.length; i++) {
				var month_name=MONTH_NAMES[i];
				if (val.substring(i_val,i_val+month_name.length).toLowerCase()==month_name.toLowerCase()) {
					if (token=="MMM"||(token=="NNN"&&i>11)) {
						month=i+1;
						if (month>12) { month -= 12; }
						i_val += month_name.length;
						break;
						}
					}
				}
			if ((month < 1)||(month>12)){return 0;}
			}
		else if (token=="EE"||token=="E"){
			for (var i=0; i<DAY_NAMES.length; i++) {
				var day_name=DAY_NAMES[i];
				if (val.substring(i_val,i_val+day_name.length).toLowerCase()==day_name.toLowerCase()) {
					i_val += day_name.length;
					break;
					}
				}
			}
		else if (token=="MM"||token=="M") {
			month=_getInt(val,i_val,token.length,2);
			if(month==null||(month<1)||(month>12)){return 0;}
			i_val+=month.length;}
		else if (token=="dd"||token=="d") {
			date=_getInt(val,i_val,token.length,2);
			if(date==null||(date<1)||(date>31)){return 0;}
			i_val+=date.length;}
		else if (token=="hh"||token=="h") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<1)||(hh>12)){return 0;}
			i_val+=hh.length;}
		else if (token=="HH"||token=="H") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<0)||(hh>23)){return 0;}
			i_val+=hh.length;}
		else if (token=="KK"||token=="K") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<0)||(hh>11)){return 0;}
			i_val+=hh.length;}
		else if (token=="kk"||token=="k") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<1)||(hh>24)){return 0;}
			i_val+=hh.length;hh--;}
		else if (token=="mm"||token=="m") {
			mm=_getInt(val,i_val,token.length,2);
			if(mm==null||(mm<0)||(mm>59)){return 0;}
			i_val+=mm.length;}
		else if (token=="ss"||token=="s") {
			ss=_getInt(val,i_val,token.length,2);
			if(ss==null||(ss<0)||(ss>59)){return 0;}
			i_val+=ss.length;}
		else if (token=="a") {
			if (val.substring(i_val,i_val+2).toLowerCase()=="am") {ampm="AM";}
			else if (val.substring(i_val,i_val+2).toLowerCase()=="pm") {ampm="PM";}
			else {return 0;}
			i_val+=2;}
		else {
			if (val.substring(i_val,i_val+token.length)!=token) {return 0;}
			else {i_val+=token.length;}
			}
		}
	// If there are any trailing characters left in the value, it doesn't match
	if (i_val != val.length) { return 0; }
	// Is date valid for month?
	if (month==2) {
		// Check for leap year
		if ( ( (year%4==0)&&(year%100 != 0) ) || (year%400==0) ) { // leap year
			if (date > 29){ return 0; }
			}
		else { if (date > 28) { return 0; } }
		}
	if ((month==4)||(month==6)||(month==9)||(month==11)) {
		if (date > 30) { return 0; }
		}
	// Correct hours value
	if (hh<12 && ampm=="PM") { hh=hh-0+12; }
	else if (hh>11 && ampm=="AM") { hh-=12; }
	var newdate=new Date(year,month-1,date,hh,mm,ss);
	return newdate.getTime();
	}

// ------------------------------------------------------------------
// parseDate( date_string [, prefer_euro_format] )
//
// This function takes a date string and tries to match it to a
// number of possible date formats to get the value. It will try to
// match against the following international formats, in this order:
// y-M-d   MMM d, y   MMM d,y   y-MMM-d   d-MMM-y  MMM d
// M/d/y   M-d-y      M.d.y     MMM-d     M/d      M-d
// d/M/y   d-M-y      d.M.y     d-MMM     d/M      d-M
// A second argument may be passed to instruct the method to search
// for formats like d/M/y (european format) before M/d/y (American).
// Returns a Date object or null if no patterns match.
// ------------------------------------------------------------------
function parseDate(val) {
	var preferEuro=(arguments.length==2)?arguments[1]:false;
	generalFormats=new Array('y-M-d','MMM d, y','MMM d,y','y-MMM-d','d-MMM-y','MMM d');
	monthFirst=new Array('M/d/y','M-d-y','M.d.y','MMM-d','M/d','M-d');
	dateFirst =new Array('d/M/y','d-M-y','d.M.y','d-MMM','d/M','d-M');
	var checkList=new Array('generalFormats',preferEuro?'dateFirst':'monthFirst',preferEuro?'monthFirst':'dateFirst');
	var d=null;
	for (var i=0; i<checkList.length; i++) {
		var l=window[checkList[i]];
		for (var j=0; j<l.length; j++) {
			d=getDateFromFormat(val,l[j]);
			if (d!=0) { return new Date(d); }
			}
		}
	return null;
	}

function validateFromAndToDate(fromDate,toDate) {
	  
   	var fromDate = fromDate.split('/');
   	var toDate = toDate.split('/');
     
	var fromDateNew = new Date();
	fromDateNew.setFullYear(fromDate[2],fromDate[1]-1,fromDate[0]);
	fromDateNew.setDate(fromDate[0]);
	fromDateNew.setMonth(fromDate[1]-1);

	var toDateNew = new Date();
	toDateNew.setFullYear(toDate[2],toDate[1]-1,toDate[0]);
	toDateNew.setDate(toDate[0]);
	toDateNew.setMonth(toDate[1]-1);

	var currDate = new Date();
	
	if (fromDateNew > currDate) {
		bootbox.alert("From Date should be less than or equal to Current Date");
		return false;
	} else if (toDateNew > currDate) {
		bootbox.alert("To Date should be less than or equal to  Current Date");
		return false;
	} else if (fromDateNew > toDateNew) {
 		bootbox.alert("To Date should be greater than or equal to from Date");
 		return false;
	} else {
		return true;
	}
}
