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
/** Added by Rajalakshmi.D.N *********/

/**
* By Raj check for valid numeric character in the entered IP 
**/

function validNumber(obj)
{
   var strString=obj.value;
   var strValidChars = "0123456789.";
   var strChar;
   var blnResult = true;
   var j=-1;
   
 if(obj.readOnly==false)
 {
   if (strString.length == 0) 
   	return false;

   //  test strString consists of valid characters listed above
   for (i = 0; i < strString.length && blnResult == true; i++)
   {
      	strChar = strString.charAt(i);
      	//bootbox.alert("Char="+strChar);
      	j=j+1;
      	
      	if (strValidChars.indexOf(strChar) == -1)
        {
         	blnResult = false;         
        }
   }
   
   if(blnResult==false)
   {
     	bootbox.alert("Please enter a valid number!!");
     	obj.value=strString.substr(0,j);
     	return false;
   }
     
     //return;
   return blnResult;
 } 
}


/**
* By Raj Validates the IP address
**/

function validateIP(obj)
{
	var ipStr;
	var str;
	var startI=0, endI=0;
	var partStr;
	var strlenGL="false";
	var invalid="false";
	var dot=0


   	var strString=obj.value;
   	var strValidChars = "0123456789.";
   	var strChar;
   	var blnResult = true;
   	var j=-1;
	
	//var validChar=validNumber(obj);
	
	if(obj.value!="" && obj.value!=null)
	{   
   	       if (strString.length == 0) return false;

   	       //  test strString consists of valid characters listed above
   		for (i = 0; i < strString.length && blnResult == true; i++)
   		{
   	   		strChar = strString.charAt(i);
   	  		j=j+1;
      	   		if (strValidChars.indexOf(strChar) == -1)
           		{
             			blnResult = false;         
           		}
        	}
  		
		ipStr=obj.value;
		if(blnResult== true)
		{
			if(obj.value.length<7 || obj.value.length>15)
			{
				invalid="true";
			}
		
			for(var i=0;i<ipStr.length && strlenGL=="false" && invalid=="false";i++)
			{	
				if(ipStr.charAt(i)==".")
				{
					dot++;
					endI=eval(i)-eval(startI);
					//bootbox.alert("StartI="+startI+" endI="+endI);
					str=ipStr.substr(startI,endI);
					partStr=ipStr.substr(startI,eval(endI));
					startI=eval(i)+eval(1);
					//bootbox.alert("str="+str+" partStr="+partStr);
					
					if(str.length>3 || str.length<=0 || str>255)
					{
						strlenGL="true";
					}
				}
			}
			if(dot>3)
			{
				invalid="true"
			}
		
			if(invalid=="true" || strlenGL=="true")
			{
				bootbox.alert("Invalid IP Address  "+obj.value);
				obj.value="";
				obj.focus();
				//return false;
			}
	     	}

	}
	
	return;
} 
 
 
 function checkInteger(obj,amount)
 {
    
    var objt = obj;
    var amt = amount;
    if(amt != null)
    {
        if(amt < 0  )
        {
            bootbox.alert("Please enter Integer for Cheque/DD/Card No.");
            objt.value="";
            objt.focus();
        }
        if(isNaN(amt))
        {
            bootbox.alert("Please enter a Integer for Cheque/DD/Card No.");
            objt.value="";
            objt.focus();
        }
        var amtstr = new String(amt);
        if(amtstr.indexOf(".") > -1)
        {
            bootbox.alert("Please enter a Integer for Cheque/DD/Card No.");
            objt.value="";
            objt.focus();
        }
        
    }
 }


/* checking for only two places after decimal
     in price fields
   param1 --object
   param2 -- value
     */

function checkdecimalval(obj,amount)
{
    var objt = obj;
    var amt = amount;
    if(amt != null && amt != "")
    {
        if(amt < 0 )
        {
            bootbox.alert("Please enter positive value for the amount paid");
            objt.focus();
            return false;

        }
        if(isNaN(amt))
        {
            bootbox.alert("Please enter a numeric value for the amount paid");
            objt.focus();
            return false;

        }
        
           objt.value= roundoff(amount);
    }
}
function checkdecimalvalwithneg(obj,amount)
{
    var objt = obj;
    var amt = amount;
    if(amt != null && amt != "")
    {
        if(isNaN(amt))
        {
            bootbox.alert("Please enter a numeric value for the amount paid");
            objt.focus();
            return false;

        }
        
        objt.value= roundoff(amount);
    }
}

/** This method will validate whether cheque date is with in six months of entered 
**or not
**/

function validateDateFor6MonthsCurrDate(strValue)
{
 	entereddate = strValue;
 
 	enteredyear = entereddate.substr(6,9);
 	enteredmonth = entereddate.substr(3,2);
 	entereddate = entereddate.substr(0,2);
 
 	entereddate1 = new Date(enteredyear,enteredmonth-1,entereddate);
 	
 	//today = new Date();
 	//set upper limit to be 5 months later
 	//today.setMonth(today.getMonth()+7);
 
 	//thisdate = today.getDate();
 	//bootbox.alert("thisdate"+thisdate);
 	//thismonth = today.getMonth()+1;
 	//bootbox.alert("thismonth"+thismonth);
 	 //thisyear = today.getYear();
 	 //bootbox.alert("thisyear"+thisyear);
 	
 	//today1 = new Date(thisyear,thismonth-1,thisdate);
 	//bootbox.alert("today1" + today1);
 	
 	todayNew = new Date();
 	//set lower limit to be 5 months before current date
 	todayNew.setMonth(todayNew.getMonth()-6);

	thisdate1 = todayNew.getDate();
	//bootbox.alert("thisdate1"+thisdate1);
	thismonth1 = todayNew.getMonth()+1;
	//bootbox.alert("thismonth1"+thismonth1);
	thisyear1 = todayNew.getYear();
	 //bootbox.alert("thisyear1"+thisyear1);
 	
	
	today2 = new Date(thisyear1,thismonth1-1,thisdate1);
 	//bootbox.alert('today2'+today2);
 	//bootbox.alert("entereddate1"+entereddate1);
 
 	if(entereddate1 < today2)
 	{
 		//bootbox.alert("Please enter valid Date");
 		return false;
 	}
 	else
 	{
 		return true;
	}
}
// to get back rounded to 2 decimal places 

function roundoff(amount)
{
    var amtstr = new String(amount);
        
    if(amtstr.indexOf(".") > -1)
    {
        var floatlen = amtstr.length - amtstr.indexOf(".");

      if(floatlen > 2)
      {
        amtstr = amtstr.substr(0 , amtstr.indexOf(".")+ 3)
      }
      if(floatlen == 2)
      {
        amtstr += "0";
      }
      if(floatlen == 1)
      {
        amtstr += "00";
      }
    }
    else
    {
            amtstr += ".00";
    }
    
    
   return amtstr;
}



/*  to check if value entered is a number
    param 1-- object
    param 2-- value
    */
function checkfornumber(obj,val)
{
    var objt = obj;
    var value = val;
    if ((value != null) || (value !="" ) )
    {
       if(isNaN(value))
       {
        bootbox.alert("Please enter a numeric value.");
        objt.value="";
        objt.focus();
        return false;
       }

    }
    

}

    
function trim(obj,value)
{
    value = value;
    
   while (value.charAt(value.length-1) == " ")
   {
    value = value.substring(0,value.length-1);
   } 
   while(value.substring(0,1) ==" ")
   {
    value = value.substring(1,value.length);
   }
   obj.value = value;

   return value ;
}

var openwin = "false";
function checkward()
{
    
 if(document.FindForm2.wardid2.value == 0)
 {
   bootbox.alert("Please select the Ward first");
 }
 else
 {
   openwin ="true";
 }
}
function checkward1()
{
    
 if(document.FindForm.wardid.value == 0)
 {
   bootbox.alert("Please select the Ward first");
 }
 else
 {
   openwin ="true";
 }
}   
    

function checkforward()
{
  if(document.dateForm.wardid.value == 0)
 {
   bootbox.alert("Please select the Ward first");
   return false;
 }
 else
 {
   return true;
 }
}   
    



function setlink()
{
  if(openwin =="true")
  {
    link ="/streetID.jsp?wardid="+document.FindForm2.wardid2.value+"&"+"formName="+document.FindForm2.formName.value;
    win1 =window.open(link,"Streetidwindow",'scrollbars=yes,resizable=yes,height=600,width=775,status=yes');
    win1.moveTo(10, 10);
    win1.focus();
  }

}

function fnSetAccountlink()
{
  if(openwin =="true")
  {
    link ="/administration/accountNumbers.jsp?branchid="+document.BankAccountForm.selBranchName.value+"&"+"formName="+document.BankAccountForm.formName.value;
    win1 =window.open(link,"AccountNumbersWindow",'scrollbars=yes,resizable=yes,height=600,width=775,status=yes');
    win1.moveTo(10, 10);
    win1.focus();
  }
}

function fnSetDeactivatedAccountlink()
{
  if(openwin =="true")
  {
	link ="/administration/deactivatedAccountNumbers.jsp?branchid="+document.BankAccountForm.selBranchName.value+"&"+"formName="+document.BankAccountForm.formName.value;
	win1 =window.open(link,"AccountNumbersWindow",'scrollbars=yes,resizable=yes,height=600,width=775,status=yes');
	win1.moveTo(10, 10);
	win1.focus();
  }
}


function fnCheckAccount()
{
    
 if(document.BankAccountForm.selBranchName.value == 0)
 {
   bootbox.alert("Please select the Branch Name first");
 }
 else
 {
   openwin ="true";
 }
}


function setlink1()
{
  if(openwin =="true")
  {
    link ="/streetID.jsp?wardid="+document.FindForm.wardid.value+"&"+"formName="+document.FindForm.formName.value;
    win1 =window.open(link,"Streetidwindow",'scrollbars=yes,resizable=yes,height=600,width=775,status=yes');
    win1.moveTo(10, 10);
    win1.focus();
  }

}

function setlinkCitizen()
{
  if(openwin =="true")
  {
    link ="/propertyTaxCitizen/streetID.jsp?wardid="+document.FindForm.wardid.value+"&"+"formName="+document.FindForm.formName.value;
    win1 =window.open(link,"Streetidwindow",'scrollbars=yes,resizable=yes,height=600,width=775,status=yes');
    win1.moveTo(10, 10);
    win1.focus();
  }
    
}
function FindFormsubmit1()
{
    document.FindForm1.Submit1.focus();
    if(document.FindForm1.pidno1.value != "")
    {
        document.FindForm1.Submit1.disabled=true;
        return true;
    }
    else
    {
        bootbox.alert("Please fill in all the values for this method");
        return false;
    }
    
}
function FindFormsubmit2()
{
    
    document.FindForm2.Submit2.focus();
    
    if(document.FindForm2.streetid.value != "" && !(validatePhoneNo(document.FindForm2.streetid.value)))
    {
        bootbox.alert("Please enter Numeric StreetID");
        document.FindForm2.streetid.value = "";
        document.FindForm2.streetid.focus();
        return false;
    }
    
    if((document.FindForm2.wardid2.value != 0) && (document.FindForm2.streetid.value != ""))
    {
        document.FindForm2.Submit2.disabled=true;
        return true;
    }
    else if((document.FindForm2.wardid2.value != 0) && ((document.FindForm2.newmunicipalno.value != "")||(document.FindForm2.oldmunicipalno.value != "")))
    {
        document.FindForm2.Submit2.disabled=true;
        return true;
    }
    else if((document.FindForm2.rangeid2.value != 0) && ((document.FindForm2.newmunicipalno.value != "")||(document.FindForm2.oldmunicipalno.value != "")))
    {
        document.FindForm2.Submit2.disabled=true;
        return true;
    }
    else
    {
        bootbox.alert("Please fill in all the values for this method");
        return false;
    }
    
}
function FindFormsubmit3()
{
    document.FindForm3.Submit3.focus();
    
    if((document.FindForm3.wardid3.value != 0) && (document.FindForm3.rangeid3.value != "") &&(document.FindForm3.phoneno.value != "") || (document.FindForm3.firstname.value != "") || (document.FindForm3.propertyAddress.value != ""))
    {
        document.FindForm3.Submit3.disabled=false;
        return true;
    }
    else if((document.FindForm3.phoneno.value != "") || (document.FindForm3.firstname.value != "") || (document.FindForm3.propertyAddress.value != ""))
    {
        document.FindForm3.Submit3.disabled=false;
        return true;
    }
    else
    {
        bootbox.alert("Please fill in all the values for this method");
        return false;
    }   
    
}



//Global Variable
var doCalc = true;
function calculator()
{
    if(doCalc == true)
    {
        doCalc = false;
        if(document.payment.totalcollection.value != "" && document.payment.totalcollection.value != 0.00)
        {
            //The RHS values are hidden whose values are set in PTC-Payment.jsp these are now stored in respective LHS vars.


            var arrtot = eval(document.payment.arrearstot.value); 
            var arrpntydmd = eval(document.payment.arrearspenaltydmd.value);
            var curpntydmd = eval(document.payment.currentpenaltydmd.value);
            var dmdpttot = eval(document.payment.demandtot.value);
            var totaldemandamt = eval(document.payment.totaldemandamt.value);
            var collection = eval(document.payment.totalcollection.value);   //Not hidden the amt which is entered by the user

            var tempTotalCurrent = eval(document.payment.tempTotalCurrent.value);

            var m_totalbalanceamt = eval(document.payment.totalbalanceamt.value);

            var   m_arrearscollectionpayment = eval(document.payment.arrearscollectionpayment.value);
            var   m_arrearsbalancepayment = eval(document.payment.arrearsbalancepayment.value);
            var   m_arrearspenaltypaycollection = eval(document.payment.arrearspenaltypaycollection.value);
            var   m_arrearspenaltypaybalance = eval(document.payment.arrearspenaltypaybalance.value);
            var   m_currentpenaltypaycollection = eval(document.payment.currentpenaltypaycollection.value);
            var   m_currentpenaltypaybalance = eval(document.payment.currentpenaltypaybalance.value);
            var   m_currentcollection = eval(document.payment.totalcollection.value);


            var   m_currentbalance = eval(document.payment.currentbalance.value);
            var   m_totalamtpaid= 0.00;
                //bootbox.alert('arrpntydmd'+arrtot);

            //When a collection is made the collected amt is distributed as Arrears,Penalty(arr),Penalty(curr)and finally PT
            //listed asper priority i.e whatever is the collnAmt first its taken for Arrears and then for penaltyArrs and so on..
            //Deduct Arrearsamt from Collecton made if it is Gt Arrearstotal payable,ie if Arrears Total > collection + Amt Already paid for Arrears


            if((collection + m_arrearscollectionpayment > arrtot )  && (collection > 0) )
            {
                if(arrtot > 0.0)
                {
                    document.payment.arrearscollection.value= roundoff(arrtot);                 
                    m_totalamtpaid +=  arrtot;
                    collection = collection - arrtot + m_arrearscollectionpayment;
                }               
                
                document.payment.arrearsbalance.value ="0.00";
            }
            // Else just subtract the collection amt from arrTotal So collection amt is entirely taken for arrears.
            else
            {
                document.payment.arrearscollection.value= roundoff(m_arrearscollectionpayment + collection);
                m_totalamtpaid +=  m_arrearscollectionpayment + collection;
                document.payment.arrearsbalance.value = roundoff(arrtot-collection - m_arrearscollectionpayment);
                collection = 0;
            }

            //Now deduct the Arrears Penalty from the Collection
            if((collection + m_arrearspenaltypaycollection > arrpntydmd) && (collection>0))
            {
                document.payment.arrearspenaltycollection.value = roundoff(arrpntydmd);
                m_totalamtpaid +=arrpntydmd;
                collection = collection-arrpntydmd + m_arrearspenaltypaycollection;
                document.payment.arrearspenaltybalance.value ="0.00";
            }
            else
            {
                document.payment.arrearspenaltycollection.value= roundoff(m_arrearspenaltypaycollection +collection);
                m_totalamtpaid +=m_arrearspenaltypaycollection +collection;
                document.payment.arrearspenaltybalance.value= roundoff(arrpntydmd-collection -m_arrearspenaltypaycollection);
                collection = 0;
            }

            //Now deduct the Current Penalty from the Collection

            if((collection +m_currentpenaltypaycollection > curpntydmd) && (collection>0))
            {
                document.payment.currentpenaltycollection.value =roundoff(curpntydmd);
                m_totalamtpaid += curpntydmd;
                collection = collection-curpntydmd +m_currentpenaltypaycollection;
                document.payment.currentpenaltybalance.value ="0.00";
            }
            else
            {
                document.payment.currentpenaltycollection.value=roundoff(m_currentpenaltypaycollection + collection);
                m_totalamtpaid += m_currentpenaltypaycollection + collection;
                document.payment.currentpenaltybalance.value=roundoff(curpntydmd-collection - m_currentpenaltypaycollection);
                collection = 0;

            }
            //Finally deduct the Current PT/currentpaymentcollection
        //  If the amount collected is greator than the demand total
        // The current payment balance will become zero and the collection value shud be negative
        if((collection + tempTotalCurrent > dmdpttot) && (collection>0))
        {
            //bootbox.alert('>>document.payment.currentpaymentcollection.value 1:' + dmdpttot);
            document.payment.currentpaymentcollection.value =roundoff(dmdpttot);
            m_totalamtpaid +=dmdpttot;
            //collection = collection-dmdpttot + m_currentcollection;
            collection = collection-(dmdpttot - tempTotalCurrent);
            //bootbox.alert('collection-(dmdpttot - tempTotalCurrent) : '+collection-(dmdpttot - tempTotalCurrent));
            document.payment.currentpaymentbalance.value ="0.00";
        }
        else //     If the amount collected is less than the demand total all the reamining money goes to demand and collection becomes 0
        {
            //bootbox.alert('>>document.payment.currentpaymentcollection.value 2:' + roundoff(collection + tempTotalCurrent));
            document.payment.currentpaymentcollection.value= roundoff(collection + tempTotalCurrent);
            m_totalamtpaid += collection;
            //document.payment.currentpaymentbalance.value=roundoff(dmdpttot- collection - m_currentcollection);
            //bootbox.alert('roundoff(dmdpttot- collection - tempTotalCurrent) : '+roundoff(dmdpttot- collection - tempTotalCurrent));
            document.payment.currentpaymentbalance.value=roundoff(dmdpttot- collection - tempTotalCurrent);
            collection = 0;
        }

        if (m_totalbalanceamt < 0)
        {
            //bootbox.alert('>>1 totaldemandamt'+totaldemandamt);
            //bootbox.alert('>>1 m_totalbalanceamt'+m_totalbalanceamt);         
            //bootbox.alert('>>1 m_totalamtpaid'+m_totalamtpaid);
            //bootbox.alert('>>1 collection'+collection);           
            //bootbox.alert('>>1 tempTotalCurrent'+tempTotalCurrent);           
            
            document.payment.totalbalance.value =  roundoff(m_totalbalanceamt - collection);        
        }
        else
        {
            //bootbox.alert('>>2 totaldemandamt'+totaldemandamt);
            //bootbox.alert('>>2 m_totalamtpaid'+m_totalamtpaid);
            //bootbox.alert('>>2 collection'+collection);           
            //bootbox.alert('>>2 tempTotalCurrent'+tempTotalCurrent);           
            if(collection==0)
            {           
                document.payment.totalbalance.value =  roundoff(totaldemandamt - m_totalamtpaid - collection - tempTotalCurrent);       
            }
            else
            {   
                document.payment.totalbalance.value =  roundoff(totaldemandamt - m_totalamtpaid - collection);      
            }
        }
      }     
    }
}

function trimalltext(allelemobj)
{
    
    //bootbox.alert("in trimall"+allelemobj.length);
    for(var i=0; i<allelemobj.length; i++)
    {
        if(allelemobj[i].type == "text")
        {
            trim(allelemobj[i], allelemobj[i].value);
        }
        //if(i==10)
        //{
        //  break;
        //}
    }   
}


function submitpaymentCheck()
{
    if(document.payment.assesementYear.value == 0)
    {
        bootbox.alert('Please choose the Assessment year');
        document.payment.assesementYear.focus();
        return false;
    }   
    //Check the Field COllection Date Format
    if(document.payment.collectionLocation[1].checked==true &&
    trim(document.payment.fldcollectionDate,document.payment.fldcollectionDate.value) != "" )
    {
        if(!validateDate(document.payment.fldcollectionDate.value) ) 
        {
            bootbox.alert('Invalid Collection Date');
            document.payment.fldcollectionDate.focus();
            return false;
        }           
        var assesementYear = document.payment.assesementYear.value;
        var fldCollectionDate = document.payment.fldcollectionDate.value; 
        if (!validateFinancialYear(assesementYear,fldCollectionDate))
        {
            bootbox.alert('Not A Collection Valid Date');
            document.payment.fldcollectionDate.focus();
            return false;
        }
    }
    //Check the Bank COllection Date Format 
    if(document.payment.collectionLocation[2].checked==true && trim(document.payment.bankcollectionDate,document.payment.bankcollectionDate.value) != "" )
    {
        if(!validateDate(document.payment.bankcollectionDate.value) ) 
        {
            bootbox.alert('Invalid BankCollection Date');
            document.payment.bankcollectionDate.focus();
            return false;
        }           
        var assesementYear =document.payment.assesementYear.value;
        //bootbox.alert(">>>>assesementYear :"+assesementYear)
        var bankcollectionDate = document.payment.bankcollectionDate.value; 
        if (!validateFinancialYear(assesementYear,bankcollectionDate))
        {
            bootbox.alert('Not A Valid BankCollection Date');
            document.payment.bankcollectionDate.focus();
            return false;
        }
    }
    
    
  
  if(document.payment.collectionLocation[0].checked == false && (document.payment.receiptNumber.value == "" && document.payment.challanNumber.value == ""))
  {
        bootbox.alert("Please fill in the Receipt No./Challan No.");
        //document.payment.receiptNumber.focus();
        return false;   
  }
  if(document.payment.collectionLocation[0].checked == false && (document.payment.fldcollectionDate.value == "" && document.payment.bankcollectionDate.value == ""))
  {
    bootbox.alert("Please fill in the Collection Date");
    document.payment.fldcollectionDate.focus();
      return false;     
  }
  if(document.payment.arrearscollection.value=="" || document.payment.arrearsbalance.value=="" || document.payment.arrearspenaltycollection.value=="" || document.payment.arrearspenaltybalance.value=="" || document.payment.currentpenaltycollection.value=="" || document.payment.currentpenaltybalance.value=="" || document.payment.currentpaymentbalance.value=="" )
  {
      bootbox.alert("Please fill in the payment details");
      return false;
  }
  if(document.payment.totalcollection.value == "")
  {
    bootbox.alert("Please fill in the amount to be paid");
    document.payment.totalcollection.focus();
    return false;
  }
  if(document.payment.arrearsduration[0].checked == false && document.payment.arrearsduration[1].checked == false )
  {
    if(document.payment.duration[0].checked == false && document.payment.duration[1].checked == false && document.payment.duration[2].checked == false && document.payment.duration[3].checked == false)
    {
      bootbox.alert("Please select the type of payment");
      return false;
     } 
  }
  if(document.payment.modeOfPayment[0].checked == false && document.payment.modeOfPayment[1].checked == false && document.payment.modeOfPayment[2].checked == false && document.payment.modeOfPayment[3].checked == false && document.payment.modeOfPayment[4].checked == false )
  {
    bootbox.alert("Please select the payment type(Cash,Cheque,DD,Credit/Debit Card)");
    return false;
  }
  if(document.payment.modeOfPayment[1].checked == true)
  {
    if(document.payment.chequeno.value=="")
    {
        bootbox.alert("Please fill in the Cheque Number");
        document.payment.chequeno.focus();
        return false;
    }
    if(document.payment.chequedate.value=="")
    {
        bootbox.alert("Please fill in the date of cheque");
        document.payment.chequedate.focus();
        return false;
    }
  }
 if(document.payment.modeOfPayment[2].checked == true)
  {
    if(document.payment.ddno.value=="")
    {
        bootbox.alert("Please fill in the DD Number");
        document.payment.ddno.focus();
        return false;
    }
    if(document.payment.dddate.value=="")
    {
        bootbox.alert("Please fill in the date of DD");
        document.payment.dddate.focus();
        return false;
    }
  }
  if(document.payment.modeOfPayment[2].checked == true || document.payment.modeOfPayment[1].checked == true)
  {
    if(document.payment.banksname.value=="")
    {
        bootbox.alert("Please fill in the Name of bank where Cheque or DD was drawn");
        document.payment.banksname.focus();
        return false;
    }
    
  }
   if(document.payment.modeOfPayment[3].checked == true || document.payment.modeOfPayment[4].checked == true)
  {
  		/*if(document.payment.cardNo.value=="")
	    {
	        bootbox.alert("Please fill in the Card Number");
	        document.payment.cardNo.focus();
	        return false;
	    }
	    else
	    {
	    	if(!validateCardNo(document.payment.cardNo))
	    		return false;
	    }*/
	    if(document.payment.authorizationCode.value=="")
	    {
	        bootbox.alert("Please fill in the Authorization Code");
	        document.payment.authorizationCode.focus();
	        return false;
    	}
    	else if(document.payment.authorizationCode.value.length <5)
		{
			bootbox.alert('Authorization Code Should be of 5 or more Character/Digits');
			 document.payment.authorizationCode.focus();
				return false;
	    }
    	if(document.payment.cardType.value==0)
		{
			bootbox.alert("Please Select Card Type");
			document.payment.cardType.focus();
			return false;
    	}
    	
  }
    
  if(document.payment.arryear.value!="" && !validateArrearyear(document.payment.arryear.value))
  {
       
      bootbox.alert("Please Enter a Valid Arrears Paid for the Year");
      document.payment.arryear.value="";
      document.payment.arryear.focus();
      return false;
  }
  
  if(document.payment.totalbalance.value < 0)
 {
    
    bootbox.alert('Balance should not be Negative');
    document.payment.totalbalance.value = "";
    document.payment.totalbalance.focus();
    return false;
 }

  //return updateFinalVars();                       
  var ret = updateFinalVars();              
  
  if(ret == false)
    return false;
  
    var arrbal      =    eval(document.payment.arrearsbalance.value);
    var arrpntybal  =    eval(document.payment.arrearspenaltybalance.value);
    var curPnltyBal =    eval(document.payment.currentpenaltybalance.value);
    var curPTBal    =    eval(document.payment.currentpaymentbalance.value);
    var totBal      =    eval(document.payment.totalbalance.value);
    var sum         =    roundoff(arrbal + arrpntybal + curPnltyBal + curPTBal)
    
    
   // if(totBal > 0)
   // {
        if(totBal != sum) 
        {
            bootbox.alert("Sum of individual Balances is not equal to TotalBalance");
            document.payment.totalbalance.focus();
            return false;

        }
   // }
    
  document.payment.submit.disabled = true;
}
/**********************************************************************************************************************************/
//This method is for pastyearpayment 
function submitpaymentCheckForPastYr()
{
    //bootbox.alert('here in past payment');
    if(document.payment.assesementYear.value == 0)
    {
        bootbox.alert('Please choose the Assessment year');
        document.payment.assesementYear.focus();
        return false;
    }   
    //Check the Field COllection Date Format
    if(document.payment.collectionLocation[1].checked==true &&
    trim(document.payment.fldcollectionDate,document.payment.fldcollectionDate.value) != "" )
    {
        if(!validateDate(document.payment.fldcollectionDate.value) ) 
        {
            bootbox.alert('Invalid Collection Date');
            document.payment.fldcollectionDate.focus();
            return false;
        }           
        var assesementYear = document.payment.assesementYear.value;
        var fldCollectionDate = document.payment.fldcollectionDate.value; 
        if (!validateFinancialYear(assesementYear,fldCollectionDate))
        {
            bootbox.alert('Not A Collection Valid Date');
            document.payment.fldcollectionDate.focus();
            return false;
        }
    }
    //Check the Bank COllection Date Format 
    if(document.payment.collectionLocation[2].checked==true && trim(document.payment.bankcollectionDate,document.payment.bankcollectionDate.value) != "" )
    {
        if(!validateDate(document.payment.bankcollectionDate.value) ) 
        {
            bootbox.alert('Invalid BankCollection Date');
            document.payment.bankcollectionDate.focus();
            return false;
        }           
        var assesementYear = document.payment.assesementYear.value;
        var bankcollectionDate = document.payment.bankcollectionDate.value; 
        if (!validateFinancialYear(assesementYear,bankcollectionDate))
        {
            bootbox.alert('Not A Valid BankCollection Date');
            document.payment.bankcollectionDate.focus();
            return false;
        }
    }
    
    
  
  if(document.payment.collectionLocation[0].checked == false && (document.payment.receiptNumber.value == "" && document.payment.challanNumber.value == ""))
  {
        bootbox.alert("Please fill in the Receipt No./Challan No.");
        //document.payment.receiptNumber.focus();
        return false;   
  }
  if(document.payment.collectionLocation[0].checked == false && (document.payment.fldcollectionDate.value == "" && document.payment.bankcollectionDate.value == ""))
  {
    bootbox.alert("Please fill in the Collection Date");
    document.payment.fldcollectionDate.focus();
      return false;     
  }
  if(document.payment.arrearscollection.value=="" || document.payment.arrearsbalance.value=="" || document.payment.arrearspenaltycollection.value=="" || document.payment.arrearspenaltybalance.value=="" || document.payment.currentpenaltycollection.value=="" || document.payment.currentpenaltybalance.value=="" || document.payment.currentpaymentbalance.value=="" )
  {
      bootbox.alert("Please fill in the payment details");
      return false;
  }
  if(document.payment.totalcollection.value == "")
  {
    bootbox.alert("Please fill in the amount to be paid");
    document.payment.totalcollection.focus();
    return false;
  }
  if(document.payment.arrearsduration[0].checked == false && document.payment.arrearsduration[1].checked == false )
  {
    if(document.payment.duration[0].checked == false && document.payment.duration[1].checked == false && document.payment.duration[2].checked == false && document.payment.duration[3].checked == false)
    {
      bootbox.alert("Please select the type of payment");
      return false;
     } 
  }
  if(document.payment.modeOfPayment[0].checked == false && document.payment.modeOfPayment[1].checked == false && document.payment.modeOfPayment[2].checked == false )
  {
    bootbox.alert("Please select the payment type(Cash,Cheque or DD)");
    return false;
  }
  if(document.payment.modeOfPayment[1].checked == true)
  {
    if(document.payment.chequeno.value=="")
    {
        bootbox.alert("Please fill in the Cheque Number");
        document.payment.chequeno.focus();
        return false;
    }
    if(document.payment.chequedate.value=="")
    {
        bootbox.alert("Please fill in the date of cheque");
        document.payment.chequedate.focus();
        return false;
    }
  }
 if(document.payment.modeOfPayment[2].checked == true)
  {
    if(document.payment.ddno.value=="")
    {
        bootbox.alert("Please fill in the DD Number");
        document.payment.ddno.focus();
        return false;
    }
    if(document.payment.dddate.value=="")
    {
        bootbox.alert("Please fill in the date of DD");
        document.payment.dddate.focus();
        return false;
    }
  }
  if(document.payment.modeOfPayment[2].checked == true || document.payment.modeOfPayment[1].checked == true)
  {
    if(document.payment.banksname.value=="")
    {
        bootbox.alert("Please fill in the Name of bank where Cheque or DD was drawn");
        document.payment.banksname.focus();
        return false;
    }
    
  }
  
  
    
  if(document.payment.arryear.value!="" && !validateArrearyear(document.payment.arryear.value))
  {
       
      bootbox.alert("Please Enter a Valid Arrears Paid for the Year");
      document.payment.arryear.value="";
      document.payment.arryear.focus();
      return false;
  }
  
  if(document.payment.totalbalance.value < 0)
 {
    
    bootbox.alert('Balance should not be Negative');
    document.payment.totalbalance.value = "";
    document.payment.totalbalance.focus();
    return false;
 }

  //return updateFinalVars();                       
  var ret = updateFinalVars();              
  
  if(ret == false)
    return false;
  
    var arrbal      =    eval(document.payment.arrearsbalance.value);
    var arrpntybal  =    eval(document.payment.arrearspenaltybalance.value);
    var curPnltyBal =    eval(document.payment.currentpenaltybalance.value);
    var curPTBal    =    eval(document.payment.currentpaymentbalance.value);
    var totBal      =    eval(document.payment.totalbalance.value);
    var sum         =    roundoff(arrbal + arrpntybal + curPnltyBal + curPTBal)
    
    
   // if(totBal > 0)
   // {
        if(totBal != sum) 
        {
            bootbox.alert("Sum of individual Balances is not equal to TotalBalance");
            document.payment.totalbalance.focus();
            return false;

        }
   // }
    
  document.payment.submit.disabled = true;
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////

/*function validateFinancialYear(financialYear, dateObj)
{
    var month = dateObj.substr(3,2);
    var year = dateObj.substr(9,1);
    var nextYear = null;
    bootbox.alert("financialYear");
    bootbox.alert(parseInt(financialYear)+1);
    if ( month>3)
    {
        nextYear = eval(parseInt(financialYear) + 1);
        
    }
    if(financialYear <10)
    {
        financialYear = "0"+financialYear;
    }
    bootbox.alert("nextYear");
    bootbox.alert(nextYear);
    bootbox.alert("nextYear1");
    if((year == financialYear) || (year == nextYear))
    {
        return true;
    }
    else
    {
        return false;
    }
    bootbox.alert("4");
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


//Temporarily validation of Financial year is been relaxed from 1st April to 1st Jan
function validateFinancialYearTemp(financialYear, collDateObj)
{
    //bootbox.alert('Inside validateFinancialYear'+collYear);
    var collMonth = eval(collDateObj.substr(3,2));
    var collYear = eval(collDateObj.substr(8,2));
    var finYear = eval(financialYear);
    //bootbox.alert('collYear'+collYear);
    //bootbox.alert('collMonth'+collMonth);
    //bootbox.alert('finYear'+finYear);
    
    //if(collMonth <= 3 && finYear == (collYear -1))
    
    if(collYear == (finYear -1))
        return true;
    else if(collMonth <= 3 && finYear == collYear)
        return true
    else 
        return false;
    
}



function updateFinalVars()
{
    
    
    var tempArrearsCollection        = eval(document.payment.arrearscollection.value);
    var tempArrearsPenaltyCollection = eval(document.payment.arrearspenaltycollection.value);
    var tempCurrentPenaltycollection = eval(document.payment.currentpenaltycollection.value);
    var tempCurrentPaymentCollection = eval(document.payment.currentpaymentcollection.value);   
    var balance = document.payment.totalbalance.value;  
    
    var totalAmtinHeads;
    if(balance >= 0)
    {
        totalAmtinHeads = tempArrearsCollection + tempArrearsPenaltyCollection+ tempCurrentPenaltycollection + tempCurrentPaymentCollection;
    }
    else
    {
        var tempTotBal = eval(document.payment.tempTotalBalance.value);
        if( tempTotBal >= 0 )       
            totalAmtinHeads = tempArrearsCollection + tempArrearsPenaltyCollection+ tempCurrentPenaltycollection + tempCurrentPaymentCollection - balance;      
        else
            totalAmtinHeads = tempArrearsCollection + tempArrearsPenaltyCollection+ tempCurrentPenaltycollection + tempCurrentPaymentCollection - balance + tempTotBal;
            
            
    }   
    

    //Round off the number as there are inconsistencies in the 4th. to 5th decimal places
    totalAmtinHeads = Math.round(totalAmtinHeads);  
        
    if(totalAmtinHeads != eval(document.payment.totalcollection.value))
      {
          //disabled temporarily.because the data is in such a bad state it is very
          //hard to debug and alter the script code and test all the 101 cases again.
          bootbox.alert("The Total amount collected does not match the collection break up.");
          return false;         
          
         //return true;
      }
    
    //document.payment.arrearscollection.value = document.payment.arrearscollection.value - document.payment.tempTotalArrears.value;
    //document.payment.arrearspenaltycollection.value = document.payment.arrearspenaltycollection.value - document.payment.tempPenaltyArrears.value;
    //document.payment.currentpenaltycollection.value = document.payment.currentpenaltycollection.value - document.payment.tempPenaltyCurrent.value;
    //document.payment.currentpaymentcollection.value = document.payment.currentpaymentcollection.value - document.payment.tempTotalCurrent.value;  
    
    
}


function enabletext()
{
   // alerrt("here hello"+document.payment.modeOfPayment[4]);
    if(document.payment.modeOfPayment[0].checked == true)
    {
        document.payment.ddno.value="";
        document.payment.dddate.value="";
        document.payment.chequedate.value="";
        document.payment.chequeno.value="";
        document.payment.banksname.value="";
        document.payment.ddno.disabled = true;
        document.payment.dddate.disabled = true;
        document.payment.chequedate.disabled = true;
        document.payment.chequeno.disabled = true;
        document.payment.banksname.disabled = true;
        document.payment.cardNo.disabled = true;
		document.payment.authorizationCode.disabled = true;
		document.payment.cardType.disabled = true;
		document.payment.cardNo.value = "";
		document.payment.authorizationCode.value = "";
		document.payment.cardType.value=0;
    }
    if(document.payment.modeOfPayment[1].checked == true)
    {
        document.payment.ddno.value="";
        document.payment.dddate.value="";
        document.payment.chequedate.disabled = false;
        document.payment.chequeno.disabled = false;
        document.payment.ddno.disabled = true;
        document.payment.dddate.disabled = true;
        document.payment.banksname.disabled = false;
        document.payment.cardNo.disabled = true;
		document.payment.authorizationCode.disabled = true;
		document.payment.cardType.disabled = true;
		document.payment.cardNo.value = "";
		document.payment.authorizationCode.value = "";
		document.payment.cardType.value=0;
    }
    if(document.payment.modeOfPayment[2].checked == true)
    {
        document.payment.chequedate.value="";
        document.payment.chequeno.value="";
        document.payment.chequedate.disabled = true;
        document.payment.chequeno.disabled = true;
        document.payment.ddno.disabled = false;
        document.payment.dddate.disabled = false;
        document.payment.banksname.disabled = false;
        document.payment.cardNo.disabled = true;
		document.payment.authorizationCode.disabled = true;
		document.payment.cardType.disabled = true;
		document.payment.cardNo.value = "";
		document.payment.authorizationCode.value = "";
		document.payment.cardType.value=0;
    }
   if(document.payment.modeOfPayment[3].checked == true || document.payment.modeOfPayment[4].checked == true)
	{
		
		/*if(document.payment.totalcollection.value < 100)
		{	
			bootbox.alert('Credit/Debit Card is not Accepted for Payment of Less than 100 Rs.');
			
			//document.payment.modeOfPayment[3].checked = false;
		}*/
		//else
		//{
			document.payment.ddno.value="";
			document.payment.dddate.value="";
			document.payment.chequedate.value="";
			document.payment.chequeno.value="";
			document.payment.chequedate.disabled = true;
			document.payment.chequeno.disabled = true;
			document.payment.ddno.disabled = true;
			document.payment.dddate.disabled = true;
			document.payment.cardNo.disabled = true;
			document.payment.authorizationCode.disabled = false;
			document.payment.cardType.disabled = false;
		//}
		
    }
}
//////////////////////////////////////////////////////////////////////////////////////
///////For Past Payment
  function enabletextForPastYrPayment()
  {
     // bootbox.alert('Hi ');
      if(document.payment.modeOfPayment[0].checked == true)
      {
         //bootbox.alert('cash checked');
         document.payment.ddno.value="";
          document.payment.dddate.value="";
          document.payment.chequedate.value="";
          document.payment.chequeno.value="";
          document.payment.banksname.value="";
          document.payment.ddno.disabled = true;
          document.payment.dddate.disabled = true;
          document.payment.chequedate.disabled = true;
          document.payment.chequeno.disabled = true;
          document.payment.banksname.disabled = true;
      }
      if(document.payment.modeOfPayment[1].checked == true)
      {
        // bootbox.alert('chq checked');
         document.payment.ddno.value="";
          document.payment.dddate.value="";
          document.payment.chequedate.disabled = false;
          document.payment.chequeno.disabled = false;
          document.payment.ddno.disabled = true;
          document.payment.dddate.disabled = true;
          document.payment.banksname.disabled = false;
      }
      if(document.payment.modeOfPayment[2].checked == true)
      {
        // bootbox.alert('DD checked');
         document.payment.chequedate.value="";
          document.payment.chequeno.value="";
          document.payment.chequedate.disabled = true;
          document.payment.chequeno.disabled = true;
          document.payment.ddno.disabled = false;
          document.payment.dddate.disabled = false;
          document.payment.banksname.disabled = false;
      }
}

///////////////////////////////////////////////////////////////////////////////////////////
function fillcollbal(obj)
{
    if(document.payment.totalcollection.value != "")
    {
        var collection = eval(document.payment.totalcollection.value);
        if(document.payment.duration[1] == obj)
        {
            document.payment.currentpaymentcollection.value =roundoff(collection);
            document.payment.currentpaymentbalance.value =roundoff(collection);
            document.payment.totalbalance.value =roundoff(collection);
        }
        if(document.payment.duration[3] == obj)
        {
            document.payment.currentpaymentcollection.value =roundoff(collection);
            document.payment.currentpaymentbalance.value ="0.00";
            document.payment.totalbalance.value ="0.00";
        }
    }   
}

function printpage()
{
 window.print();
 window.setInterval("window.close()",60);
}

function btnhiddenprintpage(obj)
{
	obj.style.visibility = "hidden";
	window.print();
	obj.style.visibility = "visible";
 	
}  

function btnbgcolorprintpage(obj,tablename,color)
{
	tablename.style.background = 'white';
	obj.style.visibility = "hidden";
	window.print();
	tablename.style.background = color;
	obj.style.visibility = "visible";
 	
}  

function newprintpage()
{
 window.print();
 
}

function openprintwindow(link)
{
  win1 =window.open(link,"receiptwindow",'scrollbars=yes,resizable=yes,height=200,width=400,status=yes');
}   
function chequecancelreceipt()
{
    if (document.dateForm.receiptno.value == "")
    {
        bootbox.alert("please enter a Receipt Number");
        return false;
    }
    else
    {
        return true;
    }
}

function chequecancelreceiptconfirm()
{
    if (document.ReceiptCancellationConfirm.reason.value == "")
    {
        bootbox.alert("please enter a Reason for cancellation of receipt.");
        return false;
    }
    else
    {
        return true;
    }
}
//Validates if the current date is greater than the date given


function validateAlpha( strValue )
{
    var objRegExp  = /^([a-zA-Z\ ]+)$/i;
    return objRegExp.test(strValue)
}

function validateNotEmpty( strValue ) 
{
   var strTemp = strValue;
   strTemp = trimAll(strTemp);
   if(strTemp.length > 0)
   {
      return true;
   }  
   return false;
 }
 
 function trimAll( strValue ) 
 {
     var objRegExp = /^(\s*)$/;
 
     //check for all spaces
     if(objRegExp.test(strValue)) 
     {
        strValue = strValue.replace(objRegExp, '');
        if( strValue.length == 0)
           return strValue;
     }
     
     //check for leading & trailing spaces
     objRegExp = /^(\s*)([\W\w]*)(\b\s*$)/;
     if(objRegExp.test(strValue)) 
     {
        //remove leading and trailing whitespace characters
        strValue = strValue.replace(objRegExp, '$2');
     }
     return strValue;
 }
 
 function validateZipCode( strValue ) 
 {
    var objRegExp  = /(^\d{6}$)/;
    return objRegExp.test(strValue);
 } 
 
 function validateEmail( strValue) 
 {
   var objRegExp  = /^[a-z0-9]([a-z0-9_\-\.]*)@([a-z0-9_\-\.]*)(\.[a-z]{2,3}(\.[a-z]{2}){0,2})$/i;
   //check for valid email
   return objRegExp.test(strValue);
 }
 
 function validatePhoneNo( strValue )
 {
   var objRegExp  = /^([0-9]+)$/;
   return objRegExp.test(strValue); 
 }
 function validateAmount( strValue )
  {
    var objRegExp  = /^([0-9]+)$/;
    return objRegExp.test(strValue); 
  }
 
  function validateNumeric( strValue )
   {
     var objRegExp  = /^([0-9]+)$/;
     return objRegExp.test(strValue); 
   }
 
 function validateEnteredDate(strValue)
 {
    var objRegExp  = /^([0-9]{2})-([0-9]{2})-([0-9]{4})$/i;
       //check for valid email
   	return objRegExp.test(strValue);
 }
 
 
 //As per BMP request check is been relaxed for 7 months instead of +/- 5months
function validateDateForNext6Months(strValue)
{
 	entereddate = strValue;
 
 	enteredyear = entereddate.substr(6,9);
 	enteredmonth = entereddate.substr(3,2);
 	entereddate = entereddate.substr(0,2);
 
 	entereddate1 = new Date(enteredyear,enteredmonth-1,entereddate);
 	
 	today = new Date();
 	//set upper limit to be 5 months later
 	today.setMonth(today.getMonth()+7);
 
 	thisdate = today.getDate();
 	thismonth = today.getMonth()+1;
 	thisyear = today.getYear();
 	
 	today1 = new Date(thisyear,thismonth-1,thisdate);
 	
 	
 	todayNew = new Date();
 	//set lower limit to be 5 months before current date
 	todayNew.setMonth(todayNew.getMonth()-7);

	thisdate1 = todayNew.getDate();
	thismonth1 = todayNew.getMonth()+1;
	thisyear1 = todayNew.getYear();
	
	today2 = new Date(thisyear1,thismonth1-1,thisdate1);
 	//bootbox.alert('today2'+today2);
 	
 
 	if(entereddate1 > today1 || entereddate1 < today2)
 	{
 		//bootbox.alert("Please enter valid Date");
 		return false;
 	}
 	else
 	{
 		return true;
	}
}
/** This method is as per clients request to change the effective date check
**  to 1 year instead of 7 months
**/
 function validateDateFor1Year(strValue)
 {
  	entereddate = strValue;
  
  	enteredyear = entereddate.substr(6,9);
  	enteredmonth = entereddate.substr(3,2);
  	entereddate = entereddate.substr(0,2);
  
  	entereddate1 = new Date(enteredyear,enteredmonth-1,entereddate);
  	
  	today = new Date();
  	//set upper limit to be 12 months later
  	today.setMonth(today.getMonth()+12);
  
  	thisdate = today.getDate();
  	thismonth = today.getMonth()+1;
  	thisyear = today.getYear();
  	
  	today1 = new Date(thisyear,thismonth-1,thisdate);
  	
  	
  	todayNew = new Date();
  	//set lower limit to be 12 months before current date
  	todayNew.setMonth(todayNew.getMonth()-12);
 
 	thisdate1 = todayNew.getDate();
 	thismonth1 = todayNew.getMonth()+1;
 	thisyear1 = todayNew.getYear();
 	
 	today2 = new Date(thisyear1,thismonth1-1,thisdate1);
  	//bootbox.alert('today2'+today2);
  
  
  	if(entereddate1 > today1 || entereddate1 < today2)
  	{
  		//bootbox.alert("Please enter valid Date");
  		return false;
  	}
  	else
  	{
  		return true;
 	}
 }

function validateArrearyear(strValue)
 {
    var objRegExp  = /^([0-9]{4})-([0-9]{4})$/i;
       //check for valid arrear year
   return objRegExp.test(strValue);
 }
function validateAlphaNumeric(strValue)
{
    //bootbox.alert("dfgdf");
    var objRegExp  = /^([a-zA-Z0-9\ ]+)$/i;
    return objRegExp.test(strValue)
}


function validatePropertyID ( strValue )
{
    var objRegExp  = /^([0-9]{1,3})-([0-9]{1,4})-([a-z0-9_\-\\.\,\/()]+)$/i;
    return objRegExp.test(strValue)
}
function chkBoxValidate()
{
    if(document.editPaymentPage.edit.length >0)
    {
        for(var i=0;i<document.editPaymentPage.edit.length;i++)
        {
                
            if(document.editPaymentPage.edit[i].checked)
            {
                document.editPaymentPage.deleteIt[i].disabled = true;
                document.editPaymentPage.receiptNo[i].disabled = false;
                document.editPaymentPage.dateOfPayment[i].disabled = false;
                document.editPaymentPage.ptCurr[i].disabled = false;
                document.editPaymentPage.cessCurr[i].disabled = false;
                document.editPaymentPage.totalCurrent[i].disabled = false;
                document.editPaymentPage.totalCurrent[i].readOnly = true;
                document.editPaymentPage.ptArr[i].disabled = false;
                document.editPaymentPage.cessArr[i].disabled = false;
                document.editPaymentPage.totalArrears[i].disabled = false;
                document.editPaymentPage.totalArrears[i].readOnly = true;
                document.editPaymentPage.penalty[i].disabled = false;
                document.editPaymentPage.totalAmount[i].disabled = false;
                document.editPaymentPage.totalAmount[i].readOnly = true; 
                document.editPaymentPage.refNum[i].disabled = false;
            }
            else if(!document.editPaymentPage.edit[i].checked)
            {
                //bootbox.alert('Here111'+i);//+document.editPaymentPage.delete[i].value);
                document.editPaymentPage.deleteIt[i].disabled = false;
                document.editPaymentPage.receiptNo[i].disabled = true;
                document.editPaymentPage.dateOfPayment[i].disabled = true;
                document.editPaymentPage.ptCurr[i].disabled = true;
                document.editPaymentPage.cessCurr[i].disabled = true;
                document.editPaymentPage.totalCurrent[i].disabled = true;
                document.editPaymentPage.ptArr[i].disabled = true;
                document.editPaymentPage.cessArr[i].disabled = true;
                document.editPaymentPage.totalArrears[i].disabled = true;
                document.editPaymentPage.penalty[i].disabled = true;
                document.editPaymentPage.totalAmount[i].disabled = true;
                document.editPaymentPage.refNum[i].disabled = true;
            }
        }
    }
    else
    {
        if(document.editPaymentPage.edit.checked)
        {
            document.editPaymentPage.deleteIt.disabled = true;
            
            document.editPaymentPage.receiptNo.disabled = false;
            document.editPaymentPage.dateOfPayment.disabled = false;
            document.editPaymentPage.ptCurr.disabled = false;
            document.editPaymentPage.cessCurr.disabled = false;
            document.editPaymentPage.totalCurrent.disabled = false;
            document.editPaymentPage.totalCurrent.readOnly = true;
            document.editPaymentPage.ptArr.disabled = false;
            document.editPaymentPage.cessArr.disabled = false;
            document.editPaymentPage.totalArrears.disabled = false;
            document.editPaymentPage.totalArrears.readOnly = true;
            document.editPaymentPage.penalty.disabled = false;
            document.editPaymentPage.totalAmount.disabled = false;
            document.editPaymentPage.totalAmount.readOnly = true;
            document.editPaymentPage.refNum.disabled = false
        }
        else if(!document.editPaymentPage.edit.checked)
        {
            document.editPaymentPage.deleteIt.disabled = false;

            document.editPaymentPage.receiptNo.disabled = true;
            document.editPaymentPage.dateOfPayment.disabled = true;
            document.editPaymentPage.ptCurr.disabled = true;
            document.editPaymentPage.cessCurr.disabled = true;
            document.editPaymentPage.totalCurrent.disabled = true;
            document.editPaymentPage.ptArr.disabled = true;
            document.editPaymentPage.cessArr.disabled = true;
            document.editPaymentPage.totalArrears.disabled = true;
            document.editPaymentPage.penalty.disabled = true;
            document.editPaymentPage.totalAmount.disabled = true;
            document.editPaymentPage.refNum.disabled = true
        }

    
    }

}
function chkBoxValidate1()
{
    
    if(document.editPaymentPage.deleteIt.length >0)
    {
        for (var i=0;i<document.editPaymentPage.deleteIt.length;i++)
        {
            if(document.editPaymentPage.deleteIt[i].checked)
            {
                //bootbox.alert('Here111'+i);//+document.editPaymentPage.delete[i].value);
                document.editPaymentPage.edit[i].disabled = true;
            }
            else if(!document.editPaymentPage.deleteIt[i].checked)
	    {
		 document.editPaymentPage.edit[i].disabled = false;
            }

        }
    }
    else
    {
        if(document.editPaymentPage.deleteIt.checked)
        {
            document.editPaymentPage.edit.disabled = true;
        }
        else if(!document.editPaymentPage.deleteIt.checked)
	{
	 	document.editPaymentPage.edit.disabled = false;
	}
    
    }

}

function validateEditPaymentForm()
{
    var temp = 0;
    //bootbox.alert('temp :'+temp);
    if(document.editPaymentPage.edit.length >0)
    {
        
        for(var i=0;i<document.editPaymentPage.edit.length;i++)
        {
            if(document.editPaymentPage.edit[i].checked)
            {
                temp = 1;
                if(document.editPaymentPage.receiptNo[i].disabled==false)
                {
                    var receiptNoObj = document.editPaymentPage.receiptNo[i];
                    //if(!validateNotEmpty(receiptNo)
                    if(trim(receiptNoObj,receiptNoObj.value) == "")
                    {
                        bootbox.alert('Plase Enter ReceiptNo');
                        //document.editPaymentPage.receiptNo[i].value="";
                        document.editPaymentPage.receiptNo[i].focus();
                        return false;

                    }
                    
                }
                if(document.editPaymentPage.dateOfPayment[i].disabled==false)
                {
                    var dateOfPaymentObj = document.editPaymentPage.dateOfPayment[i];
                    if(trim(dateOfPaymentObj,dateOfPaymentObj.value) == "")
                    {
                        bootbox.alert('Plase Enter Date Of Payment');
                        document.editPaymentPage.dateOfPayment[i].focus();
                        return false;
                    
                    }
                    if(!validateEnteredDate(document.editPaymentPage.dateOfPayment[i].value))
                    {
                        bootbox.alert('Plase Enter Valid Date Of Payment');
                        document.editPaymentPage.dateOfPayment[i].value="";
                        document.editPaymentPage.dateOfPayment[i].focus();
                        return false;
                    }
                    if(!validateDate(document.editPaymentPage.dateOfPayment[i].value))
		    {
			bootbox.alert('Date Of Payment should not be higher than Current Date');
			document.editPaymentPage.dateOfPayment[i].value="";
			document.editPaymentPage.dateOfPayment[i].focus();
			return false;
                    }
                }
                if(document.editPaymentPage.ptCurr[i].disabled==false && document.editPaymentPage.ptCurr[i].value!="")
                {
                    if(isNaN(document.editPaymentPage.ptCurr[i].value))
                    {
                        bootbox.alert('Current PT Should be Numeric');
                        document.editPaymentPage.ptCurr[i].value="";
                        document.editPaymentPage.ptCurr[i].focus();
                       return false;
                    }
                                
                }
                if(document.editPaymentPage.cessCurr[i].disabled==false && document.editPaymentPage.cessCurr[i].value!="")
                {
                    if(isNaN(document.editPaymentPage.cessCurr[i].value))
                    {
                        bootbox.alert('Current Cess Should be Numeric');
                        document.editPaymentPage.cessCurr[i].value="";
                        document.editPaymentPage.cessCurr[i].focus();
                        return false;
                    }
                                                
                }
                if(document.editPaymentPage.totalCurrent[i].disabled==false && document.editPaymentPage.totalCurrent[i].value!="")
                {
                    if(isNaN(document.editPaymentPage.totalCurrent[i].value))
                    {
                        bootbox.alert('Current Total Should be Numeric');
                        document.editPaymentPage.totalCurrent[i].value="";
                        document.editPaymentPage.totalCurrent[i].focus();
                        return false;

                    }
                
                }
                if(document.editPaymentPage.ptArr[i].disabled==false && document.editPaymentPage.ptArr[i].value!="")
                {
                    //bootbox.alert('here '+document.editPaymentPage.ptArr[i].value);
                    if(isNaN(document.editPaymentPage.ptArr[i].value))
                    {
                        bootbox.alert('Arrears PT Should be Numeric');
                        document.editPaymentPage.ptArr[i].value="";
                        document.editPaymentPage.ptArr[i].focus();
                        return false;
                    }

                }
                
                if(document.editPaymentPage.cessArr[i].disabled==false && document.editPaymentPage.cessArr[i].value!="")
                {
                    if(isNaN(document.editPaymentPage.cessArr[i].value))
                    {
                        bootbox.alert('Arrears Cess Should be Numeric');
                        document.editPaymentPage.cessArr[i].value="";
                        document.editPaymentPage.cessArr[i].focus();
                        return false;
                    }

                }
                if(document.editPaymentPage.totalArrears[i].disabled==false && document.editPaymentPage.totalArrears[i].value!="")
                {
                    if(isNaN(document.editPaymentPage.totalArrears[i].value))
                    {
                        bootbox.alert('ArrearsTotal Should be Numeric');
                        document.editPaymentPage.totalArrears[i].value="";
                        document.editPaymentPage.totalArrears[i].focus();
                        return false;
                    }

                }
                if(document.editPaymentPage.penalty[i].disabled==false && document.editPaymentPage.penalty[i].value!="")
                {
                    if(isNaN(document.editPaymentPage.penalty[i].value))
                    {
                        bootbox.alert('Penalty Should be Numeric');
                        document.editPaymentPage.penalty[i].value="";
                        document.editPaymentPage.penalty[i].focus();
                        return false;
                    }

                }   
                
                if(document.editPaymentPage.totalAmount[i].disabled==false && document.editPaymentPage.totalAmount[i].value!="")
                {
                    if(isNaN(document.editPaymentPage.totalAmount[i].value))
                    {
                        bootbox.alert('TotalAmount Should be Numeric');
                        document.editPaymentPage.totalAmount[i].value="";
                        document.editPaymentPage.totalAmount[i].focus();
                        return false;
                    }
                
                }   
                if(document.editPaymentPage.refNum[i].disabled==false)
				{
					var refNumObj = document.editPaymentPage.refNum[i];
					if(trim(refNumObj,refNumObj.value) == "")
					{
						bootbox.alert('Plase Enter Reference Number');
						//document.editPaymentPage.refNum[i].value="";
						document.editPaymentPage.refNum[i].focus();
						return false;

					}

                }
                
            }
            
        }
        
        if(document.editPaymentPage.deleteIt.length >0)
        {
            for (var j=0;j<document.editPaymentPage.deleteIt.length;j++)
            {
                if(document.editPaymentPage.deleteIt[j].checked)
                {
                    temp=1;
                }

            }
        }
        
        if(temp==0)
        {
            bootbox.alert('Please choose Edit or Delete before proceding');
            return false;
        }
        return true;
    }
    else
    {
        //bootbox.alert('here in else');
        if(document.editPaymentPage.edit.checked)
        {
            if(document.editPaymentPage.receiptNo.disabled==false)
            {
                var receiptNoObj = document.editPaymentPage.receiptNo;
                //if(!validateNotEmpty(receiptNo)
                if(trim(receiptNoObj,receiptNoObj.value) == "")
                {
                    bootbox.alert('Plase Enter ReceiptNo');
                    //document.editPaymentPage.receiptNo.value="";
                    document.editPaymentPage.receiptNo.focus();
                    return false;
            
                }
            
            }
            if(document.editPaymentPage.dateOfPayment.disabled==false)
            {
                var dateOfPaymentObj = document.editPaymentPage.dateOfPayment;
                if(trim(dateOfPaymentObj,dateOfPaymentObj.value) == "")
                {
                    bootbox.alert('Plase Enter Date Of Payment');
                    document.editPaymentPage.dateOfPayment.focus();
                    return false;
            
                }
                if(!validateEnteredDate(document.editPaymentPage.dateOfPayment.value))
                {
                    bootbox.alert('Plase Enter Valid Date Of Payment');
                    document.editPaymentPage.dateOfPayment.value="";
                    document.editPaymentPage.dateOfPayment.focus();
                    return false;
                }
                if(!validateDate(document.editPaymentPage.dateOfPayment.value))
				{
					bootbox.alert('Date Of Payment should not be higher than Current Date');
					document.editPaymentPage.dateOfPayment.value="";
					document.editPaymentPage.dateOfPayment.focus();
					return false;
				}
            
            }
            if(document.editPaymentPage.ptCurr.disabled==false && document.editPaymentPage.ptCurr.value!="")
            {
                if(isNaN(document.editPaymentPage.ptCurr.value))
                {
                    bootbox.alert('Current PT Should be Numeric');
                    document.editPaymentPage.ptCurr.value="";
                    document.editPaymentPage.ptCurr.focus();
                   return false;
                }
        
            }
            if(document.editPaymentPage.cessCurr.disabled==false && document.editPaymentPage.cessCurr.value!="")
            {
                if(isNaN(document.editPaymentPage.cessCurr.value))
                {
                    bootbox.alert('Current Cess Should be Numeric');
                    document.editPaymentPage.cessCurr.value="";
                    document.editPaymentPage.cessCurr.focus();
                    return false;
                }
        
            }
            if(document.editPaymentPage.totalCurrent.disabled==false && document.editPaymentPage.totalCurrent.value!="")
            {
                if(isNaN(document.editPaymentPage.totalCurrent.value))
                {
                    bootbox.alert('Current Total Should be Numeric');
                    document.editPaymentPage.totalCurrent.value="";
                    document.editPaymentPage.totalCurrent.focus();
                    return false;
        
                }
        
            }
            if(document.editPaymentPage.ptArr.disabled==false && document.editPaymentPage.ptArr.value!="")
            {
                //bootbox.alert('here '+document.editPaymentPage.ptArr.value);
                if(isNaN(document.editPaymentPage.ptArr.value))
                {
                    bootbox.alert('Arrears PT Should be Numeric');
                    document.editPaymentPage.ptArr.value="";
                    document.editPaymentPage.ptArr.focus();
                    return false;
                }
        
            }
        
            if(document.editPaymentPage.cessArr.disabled==false && document.editPaymentPage.cessArr.value!="")
            {
                if(isNaN(document.editPaymentPage.cessArr.value))
                {
                    bootbox.alert('Arrears Cess Should be Numeric');
                    document.editPaymentPage.cessArr.value="";
                    document.editPaymentPage.cessArr.focus();
                    return false;
                }
        
            }
            if(document.editPaymentPage.totalArrears.disabled==false && document.editPaymentPage.totalArrears.value!="")
            {
                if(isNaN(document.editPaymentPage.totalArrears.value))
                {
                    bootbox.alert('ArrearsTotal Should be Numeric');
                    document.editPaymentPage.totalArrears.value="";
                    document.editPaymentPage.totalArrears.focus();
                    return false;
                }
        
            }
            if(document.editPaymentPage.penalty.disabled==false && document.editPaymentPage.penalty.value!="")
            {
                if(isNaN(document.editPaymentPage.penalty.value))
                {
                    bootbox.alert('Penalty Should be Numeric');
                    document.editPaymentPage.penalty.value="";
                    document.editPaymentPage.penalty.focus();
                    return false;
                }
        
            }   
        
            if(document.editPaymentPage.totalAmount.disabled==false && document.editPaymentPage.totalAmount.value!="")
            {
                if(isNaN(document.editPaymentPage.totalAmount.value))
                {
                    bootbox.alert('TotalAmount Should be Numeric');
                    document.editPaymentPage.totalAmount.value="";
                    document.editPaymentPage.totalAmount.focus();
                    return false;
                }
        
            }
            
        }
        if(document.editPaymentPage.refNum.disabled==false)
		{
			var refNumObj = document.editPaymentPage.refNum;
			if(trim(refNumObj,refNumObj.value) == "")
			{
				bootbox.alert('Plase Enter Reference Number');
				document.editPaymentPage.refNum.focus();
				return false;

			}

		}
                
        else if((!document.editPaymentPage.edit.checked) && (!document.editPaymentPage.deleteIt.checked))
        {
            bootbox.alert('Please choose Edit or Delete before proceding');
            return false;
        }
        return true;    
    }
    

}

function calculateCurrTotal()
{
    if(document.editPaymentPage.edit.length >0)
    {
        for(var i=0;i<document.editPaymentPage.edit.length;i++)
        {
                //bootbox.alert('this'+document.editPaymentPage.edit[i].value);
            if(document.editPaymentPage.edit[i].checked)
            {
                var ptCurr = eval(document.editPaymentPage.ptCurr[i].value);
                var cessCurr = eval(document.editPaymentPage.cessCurr[i].value);
                if(ptCurr < 0)
                {
                   bootbox.alert('Please Enter Positive Value');
                   document.editPaymentPage.ptCurr[i].value = "";
                   document.editPaymentPage.ptCurr[i].value.focus();
                }
                else if(cessCurr < 0)
                {
		   bootbox.alert('Please Enter Positive Value');
		   document.editPaymentPage.cessCurr[i].value = "";
		   document.editPaymentPage.cessCurr[i].value.focus();
                }
                else
                {
		  var cess  = roundoff(0.34 * ptCurr);
		  var total = roundoff(ptCurr + eval(cess));
		   document.editPaymentPage.cessCurr[i].value= cess;
		   document.editPaymentPage.cessCurr[i].readOnly = true;
		   document.editPaymentPage.totalCurrent[i].value= total;
		   document.editPaymentPage.totalCurrent[i].readOnly = true;
               	}
            }
        }
    }
    else
    {
        if(document.editPaymentPage.edit.checked)
        {
            var ptCurr = eval(document.editPaymentPage.ptCurr.value);
            var cessCurr = eval(document.editPaymentPage.cessCurr.value);
           if(ptCurr < 0)
	   {
	      bootbox.alert('Please Enter Positive Value');
	      document.editPaymentPage.ptCurr.value = "";
	      document.editPaymentPage.ptCurr.value.focus();
	   }
	   else if(cessCurr < 0)
	   {
	       bootbox.alert('Please Enter Positive Value');
	       document.editPaymentPage.cessCurr.value = "";
	       document.editPaymentPage.cessCurr.value.focus();
           }
           else
           {
            	var cess  = roundoff(0.34 * ptCurr);
            	var total = roundoff(ptCurr + eval(cess));
            	//bootbox.alert('cessCurr'+total);
            	document.editPaymentPage.cessCurr.value= cess;
            	document.editPaymentPage.cessCurr.readOnly = true;
            	document.editPaymentPage.totalCurrent.value= total;
		        document.editPaymentPage.totalCurrent.readOnly = true;
            }
        }
        
    }

}

function calculateArrTotal()
{
    if(document.editPaymentPage.edit.length >0)
    {
        for(var i=0;i<document.editPaymentPage.edit.length;i++)
        {
                //bootbox.alert('this'+document.editPaymentPage.edit[i].value);
            if(document.editPaymentPage.edit[i].checked)
            {
                var ptArr = eval(document.editPaymentPage.ptArr[i].value);
                var cessArr = eval(document.editPaymentPage.cessArr[i].value);
                if(ptArr < 0)
		{
		   bootbox.alert('Please Enter Positive Value');
		   document.editPaymentPage.ptArr[i].value = "";
		   document.editPaymentPage.ptArr[i].value.focus();
		}
		else if(cessArr < 0)
		{
		   bootbox.alert('Please Enter Positive Value');
		   document.editPaymentPage.cessArr[i].value = "";
		   document.editPaymentPage.cessArr[i].value.focus();
                }
                else
		{	
			var cess  = roundoff(0.34 * ptArr);
			var total = roundoff(ptArr + eval(cess));
			 document.editPaymentPage.cessArr[i].value= cess;
		     document.editPaymentPage.cessArr[i].readOnly = true;
			document.editPaymentPage.totalArrears[i].value= total;
			document.editPaymentPage.totalArrears[i].readOnly = true;
                }
            }
        }
    }
    else
    {
        if(document.editPaymentPage.edit.checked)
        {
            var ptArr = eval(document.editPaymentPage.ptArr.value);
            var cessArr = eval(document.editPaymentPage.cessArr.value);
            if(ptArr < 0)
	    {
	       bootbox.alert('Please Enter Positive Value');
	       document.editPaymentPage.ptArr.value = "";
	       document.editPaymentPage.ptArr.value.focus();
	    }
	    else if(cessArr < 0)
	    {
	       bootbox.alert('Please Enter Positive Values');
	       document.editPaymentPage.cessArr.value = "";
	       document.editPaymentPage.cessArr.value.focus();
	    }
	    else
	    {
		    var cess  = roundoff(0.34 * ptArr);
		    var total = roundoff(ptArr + eval(cess));
		    //bootbox.alert('cessCurr'+total);
		    document.editPaymentPage.cessArr.value= cess;
		    document.editPaymentPage.cessArr.readOnly = true;
		    
		    document.editPaymentPage.totalArrears.value= total;
		    document.editPaymentPage.totalArrears.readOnly = true;
           }
        }
    
    }


}

function calcTotalAmt()
{
    if(document.editPaymentPage.edit.length >0)
    {
        for(var i=0;i<document.editPaymentPage.edit.length;i++)
        {
                //bootbox.alert('this'+document.editPaymentPage.edit[i].value);
            if(document.editPaymentPage.edit[i].checked)
            {
                var totalCurrent = eval(document.editPaymentPage.totalCurrent[i].value);
                var totalArrears = eval(document.editPaymentPage.totalArrears[i].value);
                var penaltyTotal = eval(document.editPaymentPage.penalty[i].value);
                if(penaltyTotal < 0)
                {
                	bootbox.alert('Please Enter Positive Values');
                	document.editPaymentPage.penalty[i].value ="";
                	document.editPaymentPage.penalty[i].value.focus();
                }
                else
                {
					var total = roundoff(totalCurrent + totalArrears + penaltyTotal);
					document.editPaymentPage.totalAmount[i].value= total;
					document.editPaymentPage.totalAmount[i].readOnly = true;
				}
            }
        }
    }
    else
    {
        if(document.editPaymentPage.edit.checked)
        {
            //bootbox.alert('here calc'+document.editPaymentPage.totalCurrent.value);
            var totalCurrent = eval(document.editPaymentPage.totalCurrent.value);
            var totalArrears = eval(document.editPaymentPage.totalArrears.value);
            var penaltyTotal = eval(document.editPaymentPage.penalty.value);
             	if(penaltyTotal < 0)
				{
					bootbox.alert('Please Enter Positive Value');
					document.editPaymentPage.penalty.value ="";
					document.editPaymentPage.penalty.value.focus();
				}
				else
                {
            
					var total = roundoff(totalCurrent + totalArrears + penaltyTotal);
					document.editPaymentPage.totalAmount.value= total;
					document.editPaymentPage.totalAmount.readOnly = true;
				}
        }
        
    }
    

}

function goChangePastDemand()    
{
	    
	    var pid=document.DCBcorrection.propertyidForLink.value;
	    link ="/payment/changePastDemand.jsp?propertyid="+pid;
	    win1 =window.open(link,"ChangePastDemand",'scrollbars=yes,resizable=yes,height=520,width=958,status=yes');
	    win1.moveTo(10, 10);
	    win1.focus();
	    //window.close();
	
	
}


/* This function validates Date
   checks for validation of values for date,month and year
   doesn't allow string and any format accept dd-mm-yyyy
   
   Takes in two parameters dateobject and format specification
*/
	function ValiDate(obj, format)
	{
		
		 
		if(obj.value!="")
		{
			dateBits = DateComponents(obj.value, format);
			if (dateBits == null) 
			{
			  bootbox.alert('Invalid Date, Please Re-enter');
			  obj.value="";
			  obj.focus();
			  return false;
			}

			day = dateBits[0];
			month = dateBits[1];
			year = dateBits[2];

			if ((month < 1 || month > 12) || (day < 1 || day > 31)) // check month range 
			{ 
				bootbox.alert('Invalid Date, Please Re-enter');
				obj.value="";
			  	obj.focus();
				return false;
			} 
			if ((month==4 || month==6 || month==9 || month==11) && day==31)
			{
				bootbox.alert('Invalid Date, Please Re-enter');
				obj.value="";
			  	obj.focus();
				return false;
			}
			if (month == 2) // check for february 29th 
			{

				var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)); 
				if (day>29 || (day==29 && !isleap)) 
				{
					bootbox.alert('Invalid Date, Please Re-enter');
					obj.value="";
			  		obj.focus();
					return false;
				}
			} 
			
			if (year < 1900 || year > 2100)
			{
				bootbox.alert('Invalid Date, Please Re-enter');
				obj.value="";
				obj.focus();
				return false;
			}
		}
			return true;
			
	}

/* This funcation is called during the above function, is used to split the format string 
   in to an array that lets the validation routine know the numeric value of day, month and year
   
   Takes in two parameters dateobject value and format specification
*/

	function DateComponents(dateStr, format)
	{
		
		var results = new Array();
		var datePat = /^(\d{1,2})(\/|-)(\d{1,2})\2(\d{4})$/;
		var matchArray = dateStr.match(datePat);
		
		//bootbox.alert(' matchArray : ' + matchArray);
		//bootbox.alert(' matchArray : ' + matchArray[1]);
		//bootbox.alert(' matchArray : ' + matchArray[3]);
		//bootbox.alert(' matchArray : ' + matchArray[4]);
		
		if (matchArray == null)
			return null; 
			
		// parse date into variables
		/*This Block is commented as it checks whether the entered date is in dd/mm or mm/dd format
		  Since we specify and use dd-mm this need not be included now when needed can be uncommented*/
		/*if (format.charAt(0)=="d") //format=dd/mm 
		{                  
			results[0] = matchArray[1];
			results[1] = matchArray[3];
		}

		else 
		{ 
			results[1] = matchArray[1];
			results[0] = matchArray[3]; 
		}*/

		results[0] = matchArray[1];
		results[1] = matchArray[3];
		results[2] = matchArray[4];
		
		return results;
	}

/** This function checks whether the given date is 1st April or 1st October.
*** Requirement was effective date should always be either of 1st April or 1st October
***/
function checkEffectiveDate(obj)
 {
    // bootbox.alert('function checkEffectiveDate'+obj);
     if(obj.value!="")
     {
		 if(ValiDate(obj,'dd-mm-yyyy'))
		 {
				var effectiveDate = obj.value;
				var day = effectiveDate.substr(0,2);
				var month = effectiveDate.substr(3,2);
				//bootbox.alert('here effectiveDate'+effectiveDate);
				//bootbox.alert('here day'+day);
				//bootbox.alert('here month'+month);


			   if(day != 01)
			   {
				bootbox.alert("Effective Date should  be 1st April or 1st October");
				obj.focus();
				obj.value="";
				return false;
			   }

			   if((month == 04) || (month == 10))
			   {

			   }
			   else
			   {
				 bootbox.alert("Effective Date should  be 1st April or 1st October");
				 obj.focus();
				 obj.value="";
				 return false;

			   } 
			   return true;
		   }
	  }
}

function validateCardNo(obj)
{
	
	cardNo = obj.value;
	var len = cardNo.length;

	     if(len!=16)
	     {
			bootbox.alert('Card No should be of 16 digits. Please Check');
			obj.focus();
			return false;
	     }
	      return true;
}



/*Function to validate receipt no
The function is tested but not code reviwed and is not used anywhere
The specifications are:

The Field Receipt number can only be of the following format. 
A -> Alphabet
1-> Numeric
a} Aplhabetic character followed by 4-6 numeric chracters.
b) NO aphabetic character. Only 4-6 numeric characters.
*/

function validateReceipt(obj)
{

	receiptNo = obj.value;
	var len = receiptNo.length;

	     if(len>7)
	     {
			bootbox.alert('Invalid Receipt Number,length cannot be more than 7');

	     }

	     else
	     {

	 		var firstChar=receiptNo.substr(0,1); //extracting d first character
			var rest=receiptNo.substr(1);

			//check for alpha here
			if (validateAlpha(firstChar))
			{

				var restLength=rest.length;

				if (validateNumeric(rest))
				{
				  if (restLength<=3)
				  {
						bootbox.alert('Invalid receipt No.');
				  }

					else
					{
						var restNum=receiptNo.substr(2);

						if (!validateNumeric(rest))
						{

							bootbox.alert('Invalid receipt No.');
						}
					 }
				}

				else
				{
					bootbox.alert('Invalid receipt No.');
				}

          }

		  //when first character is numeric
		  else if (validateNumeric(firstChar))
		  {

				   var rest=receiptNo.substr(1);
				   var restlength=rest.length;

				   if (validateNumeric(rest))
				   {

						if (restlength<3)
						{
							bootbox.alert('Invalid receipt No.');
						}

						else
						{
							var restNum=receiptNo.substr(2);

							if (!validateNumeric(rest))
							{

								bootbox.alert('Invalid receipt No.');
							}
						}

				   }

				   else
					{
						bootbox.alert('Invalid receipt No.');
					}
		  }
	   }

}
/* checking for textarea maxlength and restrict to given value     
   param1 --object
   param2 -- value for which to restrict text arealength
     */
function checkTextAreaLength(obj,maxLen)
{
   if(obj.value.length>maxLen)
   {
     bootbox.alert("You Entered "+obj.value.length+" Characters, So Please Enter Characters with max of "+maxLen+" characters");
     obj.value="";
     obj.focus();
     return false;
   }
}
function formatAmount(amount)
{
	//bootbox.alert("formatAmount");

	var i = parseFloat(amount);

	if(isNaN(i)) 
	{
	//bootbox.alert('isNaN');
	 i = 0.00; 
	}
	 var minus = '';
		if(i < 0) { minus = '-'; }
			i = Math.abs(i);
		i = parseInt((i + .005) * 100);
		i = i / 100;
		s = new String(i);
	if(s.indexOf('.') < 0) { s += '.00'; }
	if(s.indexOf('.') == (s.length - 2)) { s += '0'; }
	s = minus + s;
	return s;
}

function validateUniqueOrderNum(obj)
{
	var link = "../commonyui/egov/uniqueCheckAjax.jsp?tablename=EGPT_PROPERTY_STATUS_VALUES&columnname=REF_NUM&fieldvalue=" + obj.value+ "&uppercase=yes&lowercase=no";
	request = initiateRequest();
	request.onreadystatechange=function()
	{
		if (request.readyState == 4) 
		{
			if (request.status == 200) 
			{
				var response=request.responseText.split("^");
				if(response[0]=="false")
				{
				bootbox.alert("Please Enter Correct Order Num,This Order Number allready exists");
				   obj.value = "";
				   obj.focus();
	  			 return false;				
				}				
			}
		}
	};
	request.open("GET", link, true);
	request.send(null);
}

function waterMarkTextIn(styleId,value)
{
	var txt=document.getElementById(styleId).value;
	if(txt==value)
	{
	document.getElementById(styleId).value='';
	document.getElementById(styleId).style.color='';
	}
}

function waterMarkTextOut(styleId,value)
{
	var txt=document.getElementById(styleId).value;
	if(txt=='')
	{
	document.getElementById(styleId).value=value;
	document.getElementById(styleId).style.color='DarkGray';
	}
}

function waterMarkInitialize(styleId,value)
{
	document.getElementById(styleId).value=value;
	document.getElementById(styleId).style.color='DarkGray';
}

function pincodeInitialize(styleId)
{
	document.getElementById(styleId).value='600';
}
/** This function checks whether the given order number is valid or not for COC Specific 
* this function allows order number the following chars : a-z,A-Z,0-9,(,),-,/
**/
function validateOrderNumber(obj)
{
  orderNum = obj.value
  orderNumRegExp = /^([a-zA-Z0-9()\-\/]+)$/i;  
  if(orderNum!="")
  if(!orderNumRegExp.test(orderNum))
  {
    bootbox.alert("Please Enter Valid Order Number"+"\n"+"The valid Characters for Order number are : alphanumeric and (,),-,/");
    obj.focus();
  }
}

function checkNotSpecialCharForName(obj)
{
	if(obj.value!="")
	{
		var num = obj.value;
		var NameSize = num.length;
		var objRegExp  = "!@#$%^&*()+=-[]\\\';,./{}|\":<>?";		
		
		for (var i = 0; i < NameSize; i++) 
		{
			if (objRegExp.indexOf(num.charAt(i)) != -1) 
			{
		        alert ("Please enter valid Characters only!\n");
		        obj.value="";
				obj.focus();
				return false;
        		}
		}
	}
}

function checkSpecialCharForName(obj) {
	if (obj.value != "") {
		var name = obj.value;
		var objRegExp = /^([a-zA-Z \/\,\.\&\_\-]+)$/i;

		if (!objRegExp.test(name)) {
			bootbox.alert("Please enter valid Characters only!\n");
			obj.value = "";
			obj.focus();
			return false;
		}
	}
}

function validateAddress(obj) {
	if (obj.value != "") {
		var name = obj.value;
		var objRegExp = /^([a-zA-Z0-9 \/\,\.\&\_\-]+)$/i;

		if (!objRegExp.test(name)) {
			bootbox.alert("Please enter valid Characters only!\n");
			obj.value = "";
			obj.focus();
			return false;
		}
	}
}
    function checkZero(obj)
	{
		var val;
		var Objvalue=obj.value;
		if(obj.readOnly==false && obj.value!=null && obj.value!="")	
		{
			if(!isNaN(Objvalue))
			{
				val=eval(obj.value);
				if(val==0)
				{
					bootbox.alert("Series of Zeros is Not a Valid Number!!");
					obj.value="";
					obj.focus();
					return false;
				}
			}
		}
}

    function checkSpecialCharForRateType(obj)
    {
    	if(obj.value!="")
    	{
    		var num = obj.value;
    		var NameSize = num.length;
    		var objRegExp  = "!@#$%^&*+=\\\';{}|\":<>?";		
    		
    		for (var i = 0; i < NameSize; i++) 
    		{
    			if (objRegExp.indexOf(num.charAt(i)) != -1) 
    			{
    		                alert ("Please enter valid Characters only!\n");
    		                obj.value="";
    				obj.focus();
    				return false;
            		}
    		}
    	}
    }

function trimme(obj,value)
    {  
		
        value = value;
        
       while (value.charAt(value.length-1) == " ")
       {
        value = value.substring(0,value.length-1);
       } 
       while(value.substring(0,1) ==" ")
       {
        value = value.substring(1,value.length);
       }
       obj.value = value;
    
       return value;
    } 
    
    
   function isPopEnabled(buttonId,divid,errorMessage)
   {
  	var result = window.open("", "popped", "width=10, height=10, location=no, menubar=no, status=no, toolbar=no, scrollbars=no, resizable=no");
	 if (result == null) 
	 {
	   document.getElementById(divid).innerHTML = errorMessage;
	   bootbox.alert( "Please Enable the PopUps from the browser settings and try again");
	   document.getElementById(buttonId).disabled = true; 
	   result.close();
	 }
	 else
	 {
	   document.getElementById(buttonId).disabled = false;
	   result.close();
	 }
   
   }
function populateInboxHistory(stateId)
{	
	var link = "/egi/workflow/inbox!populateHistory.action?stateId="+stateId;
	var request = initiateRequest();
	request.open("GET", link, true);
	request.onreadystatechange=function()
	{
	if (request.readyState == 4) 
	{
		if (request.status == 200) 
		{
			var response = request.responseText;
			historyTxt = eval('(' + response + ')')
			renderInboxHistory(historyTxt);
		}
	}
	};	
	request.send(null);
}
function renderInboxHistory(historyText){
var historyTable = document.getElementById("HistoryTable");
for(var j=0;j<historyText.length;j++)
{
var tr = document.createElement("TR");
tr.setAttribute("id",j);
  var td1 = document.createElement("TD");
  td1.className = 'blueboxWFH';
  td1.innerHTML = historyText[j].Date;
  tr.appendChild(td1);
    var td2 = document.createElement("TD");
  td2.innerHTML = historyText[j].Sender;
  td2.className = 'blueboxWFH';
    tr.appendChild(td2);
    var td3 = document.createElement("TD");
  td3.innerHTML = historyText[j].Task;
  td3.className = 'blueboxWFH';
    tr.appendChild(td3);
    var td4 = document.createElement("TD");
  td4.innerHTML = historyText[j].Status;
  td4.className = 'blueboxWFH';
    tr.appendChild(td4);
    var td5 = document.createElement("TD");
  td5.innerHTML = historyText[j].Details;
  td5.className = 'blueboxWFH';
    tr.appendChild(td5);
  historyTable.appendChild(tr);  
}
}

function validateDecimalNumbers(sText, fieldname)
{
	 var str = sText.value;
	 if (str=="") return false;
	 var ValidChars = "0123456789.";
	 var IsNumber=true;
	 var Char;
	 var dotCnt = 0;
	 for (i = 0; i <= str.length && IsNumber == true; i++)
	 {
		 Char = str.charAt(i);
		 if (Char==".") dotCnt = dotCnt +1;
		 if (ValidChars.indexOf(Char) == -1 || dotCnt>1)
		 {
			 IsNumber = false;
		 }
	 }
	if(IsNumber==false)
   {
     	bootbox.alert("Please enter a valid number for "+fieldname);
     	str.focus();
     	return false;
   }
	 return IsNumber;
}

//XMLHttpRequest is a request object used in Ajax. We create a Request Object here.	
function initiateRequest() 
{	
	try {
	req = new XMLHttpRequest();
	} catch(err1) {
	    try {
	    req = new ActiveXObject("Msxml2.XMLHTTP");
	    } catch (err2) {
		try {
		req = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (err3) {
		    req = false;
		}
	    }
	}
	return req;
}

function validatePlotNo(obj,msg)
{
  orderNum = obj.value;
  orderNumRegExp = /^([a-zA-Z0-9#\-\/\+]+)$/i;
  if(orderNum!="")
  if(!orderNumRegExp.test(orderNum))
  {
    bootbox.alert("Please Enter Valid "+msg+"\n"+"The valid Characters for "+msg+" are : alphanumeric and #,-,/");
    obj.value="";
    obj.focus();
  }
}

function checkYearPrevCurr(yearObj)
{
     var inputYear = yearObj.value;	
	var date =  new Date();
	var currYear = date.getFullYear();
	var prevYear = 1900;
	if(inputYear!="" && (inputYear > currYear || inputYear < prevYear))
	{
	   bootbox.alert("Please Enter Valid Year Of Construction");
	   yearObj.value="";
	   yearObj.focus();
	   return false;
	}	
}

// Confirmation of close action
function confirmClose(){
	var result = confirm("Do you want to close the window?");
	if(result==true){
		window.close();
		return true;
	}else{
		return false;
	}
}	
//to test the input is valid integer,wont allow decimals and negative numbers
function validateInteger(obj)
{
 	var objRegExp = /^[1-9]([0-9]*)$/;
 	if(obj.value != null && obj.value != "" && !objRegExp.test(obj.value)) {
 		bootbox.alert("Please Enter Numbers without decimal ");
 		obj.focus();
 		return false;
 	} else {
 		return true;
 	}
 	
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

function checkNumberRange(object, value, minValue, maxValue) {
	var intValue = parseInt(value);
	var isInvalidData = false; 
	
	if (value < minValue || value > maxValue) {
		bootbox.alert('Invalid range, must be between ' + minValue + '-' + maxValue);
		object.value='';
		object.focus();
		return false;
	} 
	
	return true;
}

function validateRegDocNumber(obj,msg)
{
  orderNum = obj.value;
  orderNumRegExp = /^([a-zA-Z0-9-\/\+]+)$/i;
  if(orderNum!="")
  if(!orderNumRegExp.test(orderNum))
  {
    bootbox.alert("Please Enter Valid "+msg+"\n"+"The valid Characters for "+msg+" are : alphanumeric and -,/");
    obj.value="";
    obj.focus();
  }
}

function validate10Digit(obj,msg) {
	var number = obj.value;
	if(number != null && number.length < 10) {
		bootbox.alert("Please Enter Valid 10 digit "+msg+"");
	    obj.focus();
	}
}