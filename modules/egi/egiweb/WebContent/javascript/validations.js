
function validateIPAddress(obj) {
	 var ary = obj.value.split(".");
	 var isValidIP = true;
	 
	 for (var i in ary) { 
		 if (!ary[i].match(/^\d{1,3}$/) || (Number(ary[i]) > 255)) {
			 isValidIP = false;
			break;
		 } 
	 }
	 isValidIP = (ary.length != 4) ? false : isValidIP;
	 if (!isValidIP) {    // the value is NOT a valid IP address
		 obj.style.background = "red";
		 obj.value = "Not a valid IP address";
		 obj.select();		
	 } else { // the value IS a valid IP address
		 obj.style.background = "";  
	 }
	 return isValidIP;
}

function validateIP (obj) {
 	errorString = "";
	theName = "IPaddress";
	IPvalue =obj.value;
	var ipPattern = /^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/;
	var ipArray = IPvalue.match(ipPattern);

	if (IPvalue == "0.0.0.0")
	errorString = errorString + theName + ': '+IPvalue+' is a special IP address and cannot be used here.';
	else if (IPvalue == "255.255.255.255")
	errorString = errorString + theName + ': '+IPvalue+' is a special IP address and cannot be used here.';
	if (ipArray == null)
	errorString = errorString + theName + ': '+IPvalue+' is not a valid IP address.';
	else {
	for (i = 1; i < 5; i++) {
	thisSegment = ipArray[i];
	if (thisSegment > 255) {
	errorString = errorString + theName + ': '+IPvalue+' is not a valid IP address.';
	break;
	}
	if ((i == 0) && (thisSegment > 255)) {
	errorString = errorString + theName + ': '+IPvalue+' is a special IP address and cannot be used here.';
	break;
	      }
	      if(ipArray[i].charAt(0)=="0")
		{
			errorString = errorString + theName + ': '+IPvalue+' is not a valid IP address.';
			alert (errorString);
			obj.focus();
			return false;
		}
	   }
	}

	if (errorString == "")
	{
		return true
	}
	else
	{
		alert (errorString);
		obj.focus();
		return false;

	}

}
function validateAlpha( strValue )
{
	var objRegExp  = /^([a-zA-Z]+)$/i;
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
 function validatePositiveInteger(strValue)
 {
  	var objRegExp = /^[1-9]([0-9]*)$/;
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
   var objRegExp  = /^([0-9]*)-([0-9]+)$/;
   return objRegExp.test(strValue); 
 }

 
 function checkInteger(obj,amount)
 {
 	
 	var objt = obj;
 	var amt = amount;
 	if(amt != null)
 	{
 		if(amt < 0  )
 		{
 			alert("Please enter Integer for Cheque/DD No.");
 			objt.value="";
 			objt.focus();
 		}
 		if(isNaN(amt))
 		{
 			alert("Please enter a Integer for Cheque/DD No.");
 			objt.value="";
 			objt.focus();
 		}
 		var amtstr = new String(amt);
 		if(amtstr.indexOf(".") > -1)
 		{
 			alert("Please enter a Integer for Cheque/DD No.");
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
			alert("Please enter positive value for the amount paid");
			objt.focus();
			return false;

		}
		if(isNaN(amt))
		{
			alert("Please enter a numeric value for the amount paid");
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
			alert("Please enter a numeric value for the amount paid");
			objt.focus();
			return false;

		}
		
		objt.value= roundoff(amount);
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
		alert("Please enter a numeric value.");
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
   alert("Please select the Ward first");
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
   alert("Please select the Ward first");
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
   alert("Please select the Ward first");
   return false;
 }
 else
 {
   return true;
 }
}	
	
	
function generateOption(fName, finalLen,userid,username)
{
	document.forms[fName].fwdRedressalOfficerID.options[finalLen] = new Option(username,userid,true,true);
}

function setlink()
{
  if(openwin =="true")
  {
	link ="/propertytax/streetID.jsp?wardid="+document.FindForm2.wardid2.value+"&"+"formName="+document.FindForm2.formName.value;
	win1 =window.open(link,"Streetidwindow",'scrollbars=yes,resizable=yes,height=600,width=775,status=yes');
	win1.moveTo(10, 10);
	win1.focus();
  }

}

function getZoneValue()
{
	
  	//alert('hi');	
  	//alert('true');
	link ="../maps/getZoneValue.html";
	win1 =window.open(link,"Zoneidwindow",'scrollbars=yes,resizable=yes,height=450,width=525,status=yes');
	win1.moveTo(10, 10);
	win1.focus();


}

function getZoneValueForRep()
{
	
  	//alert('hi');	
  	//alert('true');
	link ="../maps/getZoneValueForRep.html";
	win1 =window.open(link,"Zoneidwindow",'scrollbars=yes,resizable=yes,height=450,width=625,status=yes');
	win1.moveTo(10, 10);
	win1.focus();


}

function getWardValue()
{
	
  	//alert('hi');	
  	//alert('true');
  	var i = document.GrievanceForm.zoneID.value;
  	
  	if(i==0)
  	{  	
  		alert('Please choose the zone first.');
  	}
  	else
  	{	
		link ="../maps/getWardValueForZone"+i+".html";
		win1 =window.open(link,"Wardidwindow",'scrollbars=yes,resizable=yes,height=500,width=720,status=yes');
		win1.moveTo(10, 10);
		win1.focus();
	}
}


function setlink1()
{
  if(openwin =="true")
  {
	link ="/propertytax/streetID.jsp?wardid="+document.FindForm.wardid.value+"&"+"formName="+document.FindForm.formName.value;
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
		document.FindForm1.Submit1.disabled=false;
		return true;
	}
	else
	{
		alert("Please fill in all the values for this method");
		return false;
	}
	
}
function FindFormsubmit2()
{
	document.FindForm2.Submit2.focus();
	if((document.FindForm2.wardid2.value != 0) && (document.FindForm2.streetid.value != ""))
	{
		document.FindForm2.Submit2.disabled=false;
		return true;
	}
	else if((document.FindForm2.wardid2.value != 0) && ((document.FindForm2.newmunicipalno.value != "")||(document.FindForm2.oldmunicipalno.value != "")))
	{
		document.FindForm2.Submit2.disabled=false;
		return true;
	}
	else if((document.FindForm2.rangeid2.value != 0) && ((document.FindForm2.newmunicipalno.value != "")||(document.FindForm2.oldmunicipalno.value != "")))
	{
		document.FindForm2.Submit2.disabled=false;
		return true;
	}
	else
	{
		alert("Please fill in all the values for this method");
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
		alert("Please fill in all the values for this method");
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
				//alert('arrpntydmd'+arrtot);

			//When a collection is made the collected amt is distributed as Arrears,Penalty(arr),Penalty(curr)and finally PT
			//listed asper priority i.e whatever is the collnAmt first its taken for Arrears and then for penaltyArrs and so on..
			//Deduct Arrearsamt from Collecton made if it is Gt Arrearstotal payable,ie if Arrears Total > collection + Amt Already paid for Arrears


			if((collection + m_arrearscollectionpayment > arrtot )  && (collection > 0) )
			{
				document.payment.arrearscollection.value= roundoff(arrtot);
				m_totalamtpaid +=  arrtot;
				collection = collection - arrtot + m_arrearscollectionpayment;
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
		//	If the amount collected is greator than the demand total
		// The current payment balance will become zero and the collection value shud be negative
		if((collection + tempTotalCurrent > dmdpttot) && (collection>0))
		{
			//alert('>>document.payment.currentpaymentcollection.value 1:' + dmdpttot);
			document.payment.currentpaymentcollection.value =roundoff(dmdpttot);
			m_totalamtpaid +=dmdpttot;
			//collection = collection-dmdpttot + m_currentcollection;
			collection = collection-(dmdpttot - tempTotalCurrent);
			//alert('collection-(dmdpttot - tempTotalCurrent) : '+collection-(dmdpttot - tempTotalCurrent));
			document.payment.currentpaymentbalance.value ="0.00";
		}
		else //		If the amount collected is less than the demand total all the reamining money goes to demand and collection becomes 0
		{
			//alert('>>document.payment.currentpaymentcollection.value 2:' + roundoff(collection + tempTotalCurrent));
			document.payment.currentpaymentcollection.value= roundoff(collection + tempTotalCurrent);
			m_totalamtpaid += collection;
			//document.payment.currentpaymentbalance.value=roundoff(dmdpttot- collection - m_currentcollection);
			//alert('roundoff(dmdpttot- collection - tempTotalCurrent) : '+roundoff(dmdpttot- collection - tempTotalCurrent));
			document.payment.currentpaymentbalance.value=roundoff(dmdpttot- collection - tempTotalCurrent);
			collection = 0;
		}

		if (m_totalbalanceamt < 0)
		{
			//alert('>>1 totaldemandamt'+totaldemandamt);
			//alert('>>1 m_totalbalanceamt'+m_totalbalanceamt);			
			//alert('>>1 m_totalamtpaid'+m_totalamtpaid);
			//alert('>>1 collection'+collection);			
			//alert('>>1 tempTotalCurrent'+tempTotalCurrent);			
			
			document.payment.totalbalance.value =  roundoff(m_totalbalanceamt - collection); 		
		}
		else
		{
			//alert('>>2 totaldemandamt'+totaldemandamt);
			//alert('>>2 m_totalamtpaid'+m_totalamtpaid);
			//alert('>>2 collection'+collection);			
			//alert('>>2 tempTotalCurrent'+tempTotalCurrent);			
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
	
	//alert("in trimall"+allelemobj.length);
	for(var i=0; i<allelemobj.length; i++)
	{
		if(allelemobj[i].type == "text")
		{
			trim(allelemobj[i], allelemobj[i].value);
		}
		//if(i==10)
		//{
		//	break;
		//}
	}   
}


function submitpaymentCheck()
{
	


  if(document.payment.collectionLocation[0].checked == false && (document.payment.receiptNumber.value == "" && document.payment.challanNumber.value == ""))
  {
    	alert("Please fill in the Receipt No./Challan No.");
        return false;  	
  }
  if(document.payment.arrearscollection.value=="" || document.payment.arrearsbalance.value=="" || document.payment.arrearspenaltycollection.value=="" || document.payment.arrearspenaltybalance.value=="" || document.payment.currentpenaltycollection.value=="" || document.payment.currentpenaltybalance.value=="" || document.payment.currentpaymentbalance.value=="" )
  {
      alert("Please fill in the payment details");
      return false;
  }
  if(document.payment.totalcollection.value == "")
  {
    alert("Please fill in the amount to be paid");
    return false;
  }
  if(document.payment.arrearsduration[0].checked == false && document.payment.arrearsduration[1].checked == false )
  {
  	if(document.payment.duration[0].checked == false && document.payment.duration[1].checked == false && document.payment.duration[2].checked == false && document.payment.duration[3].checked == false)
  	{
      alert("Please select the type of payment");
      return false;
     } 
  }
  if(document.payment.modeOfPayment[0].checked == false && document.payment.modeOfPayment[1].checked == false && document.payment.modeOfPayment[2].checked == false )
  {
    alert("Please select the payment type(Cash,Cheque or DD)");
    return false;
  }
  if(document.payment.modeOfPayment[1].checked == true)
  {
  	if(document.payment.chequeno.value=="")
  	{
  		alert("Please fill in the Cheque Number");
  		document.payment.chequeno.focus();
    	return false;
  	}
  	if(document.payment.chequedate.value=="")
	{
		alert("Please fill in the date of cheque");
		document.payment.chequeno.focus();
		return false;
  	}
  }
 if(document.payment.modeOfPayment[2].checked == true)
  {
  	if(document.payment.ddno.value=="")
  	{
  		alert("Please fill in the DD Number");
  		document.payment.ddno.focus();
    	return false;
  	}
  	if(document.payment.dddate.value=="")
	{
		alert("Please fill in the date of DD");
		document.payment.dddate.focus();
		return false;
  	}
  }
  if(document.payment.modeOfPayment[2].checked == true || document.payment.modeOfPayment[1].checked == true)
  {
	if(document.payment.banksname.value=="")
	{
		alert("Please fill in the Name of bank where Cheque or DD was drawn");
		document.payment.banksname.focus();
		return false;
	}
	
  }
  //return updateFinalVars();  						
  var ret = updateFinalVars();  			
  
  if(ret == false)
  	return false;
  
  document.payment.submit.disabled = true;
}

function updateFinalVars()
{
	
	
	var tempArrearsCollection = eval(document.payment.arrearscollection.value - document.payment.tempTotalArrears.value);
	var tempArrearsPenaltyCollection = eval(document.payment.arrearspenaltycollection.value - document.payment.tempPenaltyArrears.value);
	var tempCurrentPenaltycollection = document.payment.currentpenaltycollection.value - document.payment.tempPenaltyCurrent.value;
	var tempCurrentPaymentCollection = document.payment.currentpaymentcollection.value - document.payment.tempTotalCurrent.value;	
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
	var totcoll = Math.round(eval(document.payment.totalcollection.value));
	
	//alert("totalAmtinHeads :"+totalAmtinHeads);
	//alert("eval(document.payment.totalcollection.value) :"+eval(document.payment.totalcollection.value));
	if(totalAmtinHeads != totcoll)
	  {
	      alert("The Total amount collected does not match the collection break up.");
	      return false;
	  }
	
	document.payment.arrearscollection.value = document.payment.arrearscollection.value - document.payment.tempTotalArrears.value;
	document.payment.arrearspenaltycollection.value = document.payment.arrearspenaltycollection.value - document.payment.tempPenaltyArrears.value;
	document.payment.currentpenaltycollection.value = document.payment.currentpenaltycollection.value - document.payment.tempPenaltyCurrent.value;
	document.payment.currentpaymentcollection.value = document.payment.currentpaymentcollection.value - document.payment.tempTotalCurrent.value;	
	
	
}

function paymentload()
{
	document.payment.ddno.disabled = true;
	document.payment.dddate.disabled = true;
	document.payment.chequedate.disabled = true;
	document.payment.chequeno.disabled = true;
	document.payment.banksname.disabled = true;
	document.payment.modeOfPayment[0].checked = false;
	document.payment.modeOfPayment[1].checked = false;
	document.payment.modeOfPayment[2].checked = false;
	document.payment.duration[0].checked = false;
	document.payment.duration[1].checked = false;
	document.payment.duration[2].checked = false;
	document.payment.duration[3].checked = false;
	document.payment.arrearsduration[0].checked = false;
	document.payment.arrearsduration[1].checked = false;
	
	document.payment.ddno.value="";
	document.payment.dddate.value="";
	document.payment.chequedate.value="";
	document.payment.chequeno.value="";
	document.payment.banksname.value="";
	
	if(document.payment.callcalculator.value == "true")
	 	calculator();
	
}
function enabletext()
{
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
	}
}
  
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

function openprintwindow(link)
{
  win1 =window.open(link,"receiptwindow",'scrollbars=yes,resizable=yes,height=200,width=400,status=yes');
}	
function chequecancelreceipt()
{
	if (document.dateForm.receiptno.value == "")
	{
	 	alert("please enter a Receipt Number");
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
	 	alert("please enter a Reason for cancellation of receipt.");
	 	return false;
	}
	else
	{
		return true;
	}
}



