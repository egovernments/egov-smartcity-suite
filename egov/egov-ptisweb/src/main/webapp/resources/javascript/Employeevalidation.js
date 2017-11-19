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

/**
* Checks for Valid Challan Num
*Spaces and Special Characters are not allowed
**/

function validateBankChallan(obj)
{
	var iChars = "!@#$%^&*()+=[]\\\';,.{}|\":<>?";
	for (var i = 0; i < obj.value.length; i++)
	{
		if (iChars.indexOf(obj.value.charAt(i)) != -1)// || (obj.value.charAt(i))=="") 
		{
			alert ("Please Enter valid BankChallan/Receipt number");
			obj.value="";
			obj.focus();
			return false;
		}
	}
	obj.value=trimAll(obj.value); 
}

/**
* Checks for Reg Num
**/

/*function checkAlphaNumeric(obj)
 {
 
 var isNotAlphaNumric="false";
 var str=obj.value;
 var len=str.length;
 var i=0,j=0;
 var character;
 var finalStr; 
 var validchars="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
 
 //bootbox.alert("LengthfromSASvalidation="+len);
 //if(trimAll(obj.value)!="" && obj.value!=null)
 if(obj.value!=null || obj.value!="")
 {
 	for(i=0;i<len && isNotAlphaNumric=="false";i++)
 	{
 		//bootbox.alert("Str()="+str.charAt(i));
 		if(str.charAt(0)=="" || str.charAt(0)==null)
 		{
 			//bootbox.alert("Hii");
 			str=trimAll(obj.value);
 		}
 		character=str.charAt(i);
 		
 		//if(isNaN(character))
 		if(validchars.indexOf(str.charAt(i))!=-1)
 		{
 			//isnumber="false";
 			//finalStr=character;
 			j++;
 			//bootbox.alert("finalStr="+finalStr);
 		}
 		else
 		{
 			isNotAlphaNumric="true";
 		}
 	}
 	
 	if(isNotAlphaNumric=="true")
 	{
 		bootbox.alert("Please enter a valid character!!");
 		//obj.value=trimAll(str.substr(0,j));
 		obj.value=str.substr(0,j);
 		//bootbox.alert("SubString="+obj.value);
 		obj.focus();
 		return false;
 	}
 	//obj.value=trimAll(obj.value); 		
 	//obj.value=obj.value; 		
 }
 return;
} */

/**
* Checks for a Valid voucherNumber
**/
function validVocNumber(obj)
//  check for valid numeric strings
{
   var strString=obj.value;
   var strValidChars = "0123456789-/\ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
   var strChar;
   var blnResult = true;
   var j=-1;
   
   if (strString.length == 0) return false;

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
     	bootbox.alert("Please enter a valid voucher number!!");
     	obj.value=strString.substr(0,j);
     	return false;
     }
}

/**
* Checks whether the value of the string entered evaluates to zero
* If so gives the alert msg saying "Zero values are not allowed"
*/
function checkZero(obj)
{
	var val;
	var Objvalue=obj.value;
	//bootbox.alert("inside checkZero val="+obj.value+obj.readOnly);
	if(obj.readOnly==false && obj.value!=null && obj.value!="")	
	{
		//bootbox.alert("Objvalue.isNaN()="+isNaN(Objvalue));
		if(!isNaN(Objvalue))
		{
			val=eval(obj.value);			
			//bootbox.alert("val="+val);
			if(val==0)
			{
				bootbox.alert("Series of Zeroes is Not a Valid voucher Number!!");
				obj.value="";
				obj.focus();
				return false;
			}
		}
	}
}
	
/**
* Accepts the string and changes it to the uppercase
*/

function changeCase(obj)
{
	var str2=obj.value;
	var str1;
	
	var len=str2.length;
	//bootbox.alert("Length="+len);   
	//bootbox.alert("I m called");
	
        if(str2!="" && str2!=null && len>0)
	{
		str1=str2.toUpperCase();
		obj.value=str1;
	}	
	
	return str1;
}


function checkNumber(obj)
{

    if (obj.value!="" && obj.value!=null)
    {
	    if(isNaN(obj.value))
	    {
	    bootbox.alert('Please Enter Only Numeric Value');
	    obj.value="";
	    obj.focus();
	    return false;
    	    }
    }

    return true;
}

/**
* Checks for a number Phone and PINcode
**/
function validNumber(obj)
//  check for valid numeric strings
{
   var strString=obj.value;
   var strValidChars = "0123456789";
   var strChar;
   var blnResult = true;
   var j=-1;
   
   if (strString.length == 0) return false;

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
}

/**
* Returns the Index of the parent column of the floor
**/

/*function returnIndexOfFloor(obj)
{
    var td;
    var idx;
    var tbl=document.getElementById('floor_table');
    var col=tbl.rows[1].cells.length-1;
       //bootbox.alert("OBJ="+obj.value);
 	if(col>1)
	{		
		if(obj.parentNode!=null)
		{
			//bootbox.alert("beforecalBuildingTax(obj)->obj.parentNode!=null");
			td=obj.parentNode;
			idx=td.cellIndex;
		}
		else
		{
		 	idx=0;
		}
	}
	else
	{
		//bootbox.alert("beforecalBuildingTax(obj)->obj.parentNode==null");
		idx=0;
	}
	//bootbox.alert("Index="+idx);
	return idx;  
}*/


/**
* Checks for numbers in a string
* If number is entered...Returns the String till the last etered number 
**/
function checkNotNumber(obj)
 {
 
 var isnumber="false";
 var str=obj.value;
 var len=str.length;
 var i=0,j=0;
 var character;
 var finalStr; 
 var invalidchars="0123456789";
 
 //bootbox.alert("LengthfromSASvalidation="+len);
 //if(trimAll(obj.value)!="" && obj.value!=null)
 if(obj.value!=null || obj.value!="")
 {
 	for(i=0;i<len && isnumber=="false";i++)
 	{
 		//bootbox.alert("Str()="+str.charAt(i));
 		if(str.charAt(0)=="" || str.charAt(0)==null)
 		{
 			//bootbox.alert("Hii");
 			str=trimAll(obj.value);
 		}
 		character=str.charAt(i);
 		
 		//if(isNaN(character))
 		if(invalidchars.indexOf(str.charAt(i))==-1)
 		{
 			//isnumber="false";
 			//finalStr=character;
 			j++;
 			//bootbox.alert("finalStr="+finalStr);
 		}
 		else
 		{
 			isnumber="true";
 		}
 	}
 	
 	if(isnumber=="true")
 	{
 		bootbox.alert("Please enter a valid character!!");
 		//obj.value=trimAll(str.substr(0,j));
 		obj.value=str.substr(0,j);
 		//bootbox.alert("SubString="+obj.value);
 		obj.focus();
 		return false;
 	}
 	//obj.value=trimAll(obj.value); 		
 	//obj.value=obj.value; 		
 }
 return;
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
					if(collDate.substr(0,2)<=31)
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
* Checks whether the first date is greater than the second date 
* RETURN TYPE:: true -- if less than second date......Else returns false
**/
function checkFdateTdate(fromDate,toDate)
{
	//ENTERED DATE FORMAT MM/DD/YYYY
	
	//bootbox.alert('I hv entered the function');
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

/************view SAS******************/
function ifPartSr(strString)
   //  check for valid strings
   {
   var strValidChars = "0123456789-/\ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
   var strChar;
   var blnResult = true;

   if (strString.length == 0) return false;

   //  test strString consists of valid characters listed above
   for (i = 0; i < strString.length && blnResult == true; i++)
      {
      strChar = strString.charAt(i);
      if (strValidChars.indexOf(strChar) == -1)
         {
         blnResult = false;
         }
      }
   return blnResult;
   }

function IsPropertyNumber(strString)
   //  check for valid numeric strings
   {
   var strValidChars = "0123456789-/ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
   var strChar;
   var blnResult = true;

   if (strString.length == 0) return false;

   //  test strString consists of valid characters listed above
   for (i = 0; i < strString.length && blnResult == true; i++)
      {
      strChar = strString.charAt(i);
      if (strValidChars.indexOf(strChar) == -1)
         {
         blnResult = false;
         }
      }
   return blnResult;
   }

function IsFloorNumber(strString)
   //  check for valid numeric strings
   {
   var strValidChars = "0123456789";
   var strChar;
   var blnResult = true;

   if (strString.length == 0) return false;

   //  test strString consists of valid characters listed above
   for (i = 0; i < strString.length && blnResult == true; i++)
      {
      strChar = strString.charAt(i);
      if (strValidChars.indexOf(strChar) == -1)
         {
         blnResult = false;
         }
      }
   return blnResult;
   }

function validateAlpha( strValue )
{
	var objRegExp  = /^([a-zA-Z]+)$/i;
	return objRegExp.test(strValue)
}

function validateNotEmpty(obj)
{
   var strTemp = obj.value;
   strTemp = trimAll(strTemp);
   //bootbox.alert('length :'+strTemp.length);
   if(strTemp.length <= 0)
   {
      bootbox.alert('Please Fill in the value');
      obj.focus();
      return false;
   }
   return true;
 }

function IsNumeric(sText)
{
   var ValidChars = "0123456789.";
   var IsNumber=true;
   var Char;


   for (i = 0; i < sText.length && IsNumber == true; i++)
      {
      Char = sText.charAt(i);
      if (ValidChars.indexOf(Char) == -1)
         {
         IsNumber = false;
         }
      }
   return IsNumber;
}

   function IsValidYear(Year)
{
	//bootbox.alert("IsValidYear");
	var d = new Date();
	var curr_date = d.getFullYear();
	//bootbox.alert("Year" + Year);


	if( (Year<1900) || (Year>curr_date))
	{
		//bootbox.alert("IsValidYear 1");
		return false;
	}
	else
	{
		//bootbox.alert("IsValidYear 2");
		return true;
	}
}

function removeAllOptions(selectbox)
{
	//bootbox.alert("selectbox: " + selectbox);
	var i;
	for(i=selectbox.options.length-1;i>=0;i--)
	{
		selectbox.remove(i);
	}

}

function IsNum(str)
 {
 
 var isnumber="true";
 var len=str.length;
 var i=0;
 var character;
  
 //bootbox.alert("Length="+len);
 for(i=0;i<len;i++)
 {
 	character=str.charAt(i);
 	if(isNaN(character))
 	{
 		isnumber="false";	
 	}
 	else
 	{
 		isnumber="true";
 	}
 } 
 return isnumber;
}


function checkAlphabet(obj)
{

   var objRegExp  = /^([a-zA-Z\ ]+)$/i;

   if(obj.value!="")
   {
	   if(!objRegExp.test(obj.value))
	   {
		   bootbox.alert('Please Enter Only Alphabets');
		   obj.value="";
		   obj.focus();
		   return false;
	   }
   }
   return true;
}



function checkNumberMandatory(obj)
{

    if ( obj.value =="" )
    {
    bootbox.alert('should not blank');
    obj.focus();
    return false;
    }

    else  if(isNaN(obj.value))
    {
    bootbox.alert('Enter only numeric value.');
    obj.value="";
    obj.focus();
    return false;
    }

    return true;
}

/**
* E-Mail Validation
**/
function validateEmail( obj)
{
       var objRegExp  = /^[a-z0-9]([a-z0-9_\-\.]*)@([a-z0-9_\-\.]*)(\.[a-z]{2,3}(\.[a-z]{2}){0,2})$/i;
        if(obj.value!="")
          {
          if(!objRegExp.test(obj.value))
          {
          bootbox.alert('Pleasr Enter Valid Email Address');
          obj.value="";
          obj.focus();
          return false;
          }
          }


   return true;

}

/**
* Valid year b/n 1900 to current year
**/


function validatingYear(obj)
{
	Year=obj.value;

	//bootbox.alert("Year"+Year);
	var d = new Date();
	var curr_date = d.getFullYear();
	//bootbox.alert("curr_date" + curr_date);

	/*if(Year=="")
	{
		bootbox.alert("Pls enter the Year of Construction!!");
		obj.focus();
	}*/

	if(Year!="" && ((Year<1900) || (Year>curr_date)))
	{
		bootbox.alert("Not A ValidYear!! Pls Enter the year between 1900 And "+ curr_date);
		//obj.value="";
		obj.focus(); //+documnet.viewPropertyForm.yrOfConstr.focus);
		return false;
	}
	else
		return true;
}

/********************** VIEW and CREATE SAS methods **************************/

/**
* Deletes the owner row
**/

function deleteOwner(obj)
{
	//bootbox.alert(">>>>>Inside deleteOwner SAS");
	var delRow = obj.parentNode.parentNode;
	//var tbl = delRow.parentNode.parentNode;
	var rIndex = delRow.rowIndex;
	
    //if(rIndex == 0)
    
    var tbl=document.getElementById('nameTable');
    var rowo=tbl.rows.length;
   //bootbox.alert("No. of Owners="+rowo);
    
    if(rowo<=11)
    {
    	document.getElementById('addOwnerBtn').disabled=false;
    }

    if(rowo<=2)
	{
		bootbox.alert("This Owner can not be deleted");
		return false;
	}
	else
	{
		tbl.deleteRow(rIndex);
		return true;
	}
}


/**
* Deletes the Tenant row
**/

function deleteTenant(obj)
{
	//bootbox.alert(">>>>>Inside deleteTenant");
	var delRow = obj.parentNode.parentNode;
	var tblT = delRow.parentNode.parentNode;
	var rIndex = delRow.rowIndex;
	//if(rIndex == 0)
	var tbl=document.getElementById('nameTenantTable');
	var rowt=tbl.rows.length;
	//bootbox.alert("rowt="+rowt);
	if(rowt<=11)
	{
		document.getElementById('addTenantBtn').disabled=false;
	}
	
	if(rowt==1)
	{

		tblT.deleteRow(rIndex);
		//bootbox.alert("No tenants");

		//bootbox.alert("2222tenantExists="+document.sasForm.tenantExists.value);
		return true;
	}
	else
	{
		tblT.deleteRow(rIndex);
		return true;
	}

}

/**
* Adds an Owner row
**/

function addOwner()
{


    var tbl = document.getElementById('nameTable');
    var rowO=tbl.rows.length;
    //bootbox.alert("rowO="+rowO);
    if(rowO<11)
    {
    	if(document.getElementById('nameRow') != null)
    	{
    			var tbody=tbl.tBodies[0];
			var lastRow = tbl.rows.length;
			/*if(document.viewPropertyForm.firstName[lastRow-1].value="" || document.viewPropertyForm.firstName[lastRow].value==null)
			{
			bootbox.alert("Please Enter Owner FirstName ");
			document.viewPropertyForm.firstName[lastRow-1].focus();
			return false;
			}*/

			var rowObj = document.getElementById('nameRow').cloneNode(true);			
			tbody.appendChild(rowObj);			
			resetRowValues(lastRow-1);
	}
	else
	{
	bootbox.alert("Im in else");
		//var tbl = document.getElementById('nameTable');
		var lastRow = tbl.rows.length;
		var txt1 = 'firstName';
		var txt2 = 'middleName';
		var txt3 = 'lastName';
		//var txt4 = 'fatherName';
		//var btnName = 'deleteOwnr';
		//var idName = 'idOwner';
		createTextNodes(tbl,lastRow,txt1, txt2, txt3);
	}
    }
    //else
    	//document.getElementById('addOwnerBtn').disabled=true;
    	
}


/**
* Adds a Tenant row
**/

function addTenant()
{
  //bootbox.alert("Inside addTenant");
      var tbl = document.getElementById('nameTenantTable');
      var rowT=tbl.rows.length;
      if(rowT<11)
      {
      	if(document.getElementById('tenantNameRow') != null)
      	{
  			var tbody=tbl.tBodies[0];
  			var lastRow = tbl.rows.length;
  			var rowObj = document.getElementById('tenantNameRow').cloneNode(true);
  			tbody.appendChild(rowObj);
  			resetTenentRowValues(lastRow);
  	}
  	else
  	{
  		//var tbl = document.getElementById('nameTable');
  		var lastRow = tbl.rows.length;
  		var txt1 = 'occfirstName';
		var txt2 = 'occmiddleName';
		var txt3 = 'occlastName';
		var txt4 = 'occfatherName';
		var btnName = 'deleteTent';

		createTextNodes(tbl,lastRow,txt1, txt2, txt3, txt4, btnName);
	}
     }
     
     else
     	document.getElementById('addTenantBtn').disabled=true;
}

/**
* Creates a node
**/

function createTextNodes(tbl,lastRow,txt1, txt2, txt3, txt4, btnName)
{
	var tbody=tbl.tBodies[0];
	var row = tbl.tBodies[0].insertRow(0);

	var cell0 = row.insertCell(0);
	var txtInp = document.createElement('input');
	txtInp.setAttribute('type', 'text');
	txtInp.className="fieldcell";
	txtInp.setAttribute('name', txt1);
	txtInp.setAttribute('size', 10);
	txtInp.setAttribute('value', "");
	cell0.appendChild(txtInp);

	var cell1 = row.insertCell(1);
	var txtInp1 = document.createElement('input');
	txtInp1.setAttribute('type', 'text');
	txtInp1.className="fieldcell";
	txtInp1.setAttribute('name', txt2);
	txtInp1.setAttribute('size', 10);
	txtInp1.setAttribute('value', "");
	cell1.appendChild(txtInp1);

	var cell2 = row.insertCell(2);
	var txtInp2 = document.createElement('input');
	txtInp2.setAttribute('type', 'text');
	txtInp2.className="fieldcell";
	txtInp2.setAttribute('name', txt3);
	txtInp2.setAttribute('size', 10);
	txtInp2.setAttribute('value', "");
	cell2.appendChild(txtInp2);

	var cell3 = row.insertCell(3);
	var txtInp3 = document.createElement('input');
	txtInp3.setAttribute('type', 'text');
	txtInp3.className="fieldcell";
	txtInp3.setAttribute('name', txt4);
	txtInp3.setAttribute('size', 10);
	txtInp3.setAttribute('value', "");
	cell3.appendChild(txtInp3);

	var cell4 = row.insertCell(4);
	var btn = document.createElement('input');
	btn.setAttribute('type', 'button');
	btn.className="button2";
	btn.setAttribute('name', btnName);
	btn.setAttribute('size', 10);
	btn.setAttribute('value', "Delete");
	//btn.onclick = function () {deleteOwner(this)};
	if(btnName == 'deleteOwnr')
		btn.onclick = function () {deleteOwner(this)};
	else
		btn.onclick = function () {deleteTenant(this)};
	cell4.appendChild(btn);

	var cell5 = row.insertCell(5);
	var txtInp5 = document.createElement('input');
	txtInp5.setAttribute('type', 'hidden');
	txtInp5.setAttribute('class','fieldcell');

	txtInp5.setAttribute('value', "");
	cell5.appendChild(txtInp5);

	tbody.appendChild(row);
	row.setAttribute('class','fieldcell');
}

/**
* Reorders the columns
**/

function reorderColumns(tbl, col)
{

	//bootbox.alert("Inside reorderColumns");
	//bootbox.alert("col="+col);
	for(j=col; j<tbl.rows[1].cells.length; j++)
	{
		//bootbox.alert("j="+j);
		for (var i=1; i<tbl.rows.length; i++)
		{
			//bootbox.alert("data="+tbl.rows[i].cells[col].childNodes[0].value);
			if(tbl.rows[i].cells[j].childNodes[0].value == "Delete")
			{
				//bootbox.alert("id="+tbl.rows[i].cells[j].childNodes[0].id);
				tbl.rows[i].cells[j].childNodes[0].id=j;
			}
		}
	}
}

//take only taxperc by removing usgName(ex:0.35-residential will returns 0.35)

function getTaxByRemoveUsg(taxWithUsg)
{
	var i=taxWithUsg.indexOf("-");
	var strTax=taxWithUsg.substr(0,i);	
	return strTax;
}

function resetRowValues(lastRow)
{
	document.propWithTaxCollectionForm.firstName[lastRow].value="";
	document.propWithTaxCollectionForm.middleName[lastRow].value="";
	document.propWithTaxCollectionForm.lastName[lastRow].value="";
	//document.sasForm.fatherName[lastRow].value="";
	//document.sasForm.idOwner[lastRow].value="";

	
}

function addPropOwner()
{
    var tbl = document.getElementById('nameTable');
    var rowO=tbl.rows.length;   
    if(rowO<11)
    {
    	if(document.getElementById('nameRow') != null)
    	{
    			var tbody=tbl.tBodies[0];
			var lastRow = tbl.rows.length;
			var rowObj = document.getElementById('nameRow').cloneNode(true);			
			tbody.appendChild(rowObj);			
			resetPropRowValues(lastRow-1);
	}
	else
	{
	bootbox.alert("Im in else");		
		var lastRow = tbl.rows.length;
		var txt1 = 'firstName';
		var txt2 = 'middleName';
		var txt3 = 'lastName';		
		createModTextNodes(tbl,lastRow,txt1, txt2, txt3);
	}
    }    
}

function resetPropRowValues(lastRow)
{
	document.modifyPropertyForm.firstName[lastRow].value="";
	document.modifyPropertyForm.middleName[lastRow].value="";
	document.modifyPropertyForm.lastName[lastRow].value="";	
}

function deleteSpecificOwner(obj)
{
	var delRow = obj.parentNode.parentNode;
	var tblT = delRow.parentNode.parentNode;
	var rIndex = delRow.rowIndex;
	var tbl=document.getElementById('nameTable');
	var rowt=tbl.rows.length;
	if(rowt>2)
	{
	  tblT.deleteRow(rIndex);;
	  return true;
	}
	else
	bootbox.alert("This Owner can not be deleted");

}
