/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
/**
* Checks for valid characters in Employee code
**/

function checkAlphaNumericForCode(obj)
{

	if(obj.value!="")
	{
		var num=obj.value;//[^a-zA-Z0-9_]*$
		var objRegExp  = /^[a-zA-Z0-9_-]+$/;
		if(!objRegExp.test(num))
		{
			alert('Please fill the proper code number. Only numbers, alphabets, -, _ are allowed');
			obj.value="";
			obj.focus();
		}
	}
}

//to delete a specific row
function deleteSpecificRow(obj,tableId)
{
	var delRow = obj.parentNode.parentNode;
	var tblT = delRow.parentNode.parentNode;
	var rIndex = delRow.rowIndex;
	var tbl=document.getElementById(tableId);
	var rowt=tbl.rows.length;
	if(rowt>2)//row1 is header and row2 on wards values
	{
	  tblT.deleteRow(rIndex);;
	  return true;
	}
	else
	alert("This Row can not be deleted");

}

//This Method return a Max Date from the Array
function getMax(maxDate)
{  

	var maxTodate = maxDate;			
    var dates = new Array();    
    var dateStrings = maxTodate;    
    if(!maxTodate.isEmpty())
	{
			//i-Index date-Value
		    $.each(dateStrings , function(i, date){ 
		        var strDate = date;
		        var objDate = getValidDateObject(strDate);
		
		        if (!objDate){
		                
		                throw new Error('Invalid Date: ' + strDate);
		        }
		
		        dates[i] =  objDate.getTime();                  
		    });

		//sort the array by default in ascending order
	    dates.sort();
	    //reverse the sorted array
	    dates.reverse();

		//get the max date
	    var d2 = new Date(dates[0]);
	    //getShortDateString method used to convert date format to string format in dd/MM/yyyy format
	    //dd/MM/yyyy format Standard format used in egov
	    var maxDateString = getShortDateString(d2);
		return maxDateString;
	}
   
}
//This Method receives the passed date from String format
//and convert that to date format
function getValidDateObject(strDate){

   
    month=strDate.substr(3,2);
	day=strDate.substr(0,2);
	year=strDate.substr(6,4);
   //formatting to mm/dd/yyyy format as end user are passing date in dd/MM/yyyy format
	var strMondayYear = month+'/'+day+'/'+year
	//replacing - to /
	strMondayYear = strMondayYear.replace(/[-]/g,'/')
	var objDate = new Date(strMondayYear);
    if (objDate == 'Invalid Date') {
        alert('bad input: ' + strDate);                 
        return false;
    }
    return objDate;     
}
 //getShortDateString method used to convert date format to string format in dd/MM/yyyy format
 //dd/MM/yyyy format Standard format used in egov
function getShortDateString(objDate){
        var y = objDate.getFullYear();
        var m = objDate.getMonth() + 1;
        var d = objDate.getDate();

        if (m < 10) m = "0" + m; 
        if (d < 10) d = "0" + d;

        var strDate = d + '/' + m + '/' + y;

        return strDate;
}
Array.prototype.isEmpty = function() 
{  
	return this.length == 0;
}

//This Method return the max Date for a enumerable object type
//'.dataList' name should be mensioned in the text field as class=dataList
function getMaxGeneric(){  

    var dates = new Array();    
    var dateStrings = $('.dataList');           

    $.each(dateStrings , function(i, date){ 
        var strDate = $(date).val();
        var objDate = getValidDateObject(strDate);

        if (!objDate){
                
                throw new Error('Invalid Date: ' + strDate);
        }

        dates[i] =  objDate.getTime();                  
    });

    dates.sort();
    dates.reverse();

    var d2 = new Date(dates[0]);
    //maxDateString is the max date
    var maxDateString = getShortDateString(d2);
    return maxDateString;
	
       
}

if (!Array.prototype.indexOf)Array.prototype.indexOf = function(item, i) {
  i || (i = 0);
  var length = this.length;
  if (i < 0) i = length + i;
  for (; i < length; i++)
    if (this[i] === item) return i;
  return -1;
}; 


Array.prototype.contains = function (element) 
			{
				for (var i = 0; i < this.length; i++) {
				if (this[i] == element) {
				return true;
				}
				}
				return false;
			}
function checkduplicate(tableObj,obj,value,fieldNameDisplay,index,ifId,typeOfFeild)
{
        
    //value-field value
    //obj - field's obj
    //tableObj - table name
    //fieldNameDisplay - pass the name in which u want in alert
    //index-exact row count
    //ifId - Yes - can be used for dropDown,AutoComplete and No can be used for Text field
    //typeOfFeild - text will reset the value to empty String and otherValue will reset the value to 0 (for DropDown)
        
	if(trimAll(obj.value)!='')
	{
		var indexObj = index;
	  
		var scripts = new Array();
		var tbl= document.getElementById(tableObj);
		var rowLength= tbl.rows.length;
		var row=getRow(obj);
		var firstName = getControlInBranch(row,obj.name);
		var firstNameVal = firstName.value;
		 
		var rowIndex = row.rowIndex-indexObj;
		var rowExactlength = rowLength-indexObj;
		 
		for(i=0;i<rowExactlength;i++)
		{
		   if(i!=(rowIndex))
		     {
		        if(ifId=='Yes')
		        {
		        	scripts.push(document.getElementsByName(obj.name)[i].value);
		        }
		        else
		        {
		            var value = document.getElementsByName(obj.name)[i].value;
		            scripts.push(trimAll(value.toLowerCase().replace(/\s/g,"")));
		        }
		     }
		}
		
		if(scripts.contains(firstNameVal))
		{
		    if(typeOfFeild=='text')
		    {
		      alert('Please enter the unique '+fieldNameDisplay);
		      firstName.value = '';
		    }
		    else
		    {
		        alert('Please select the unique '+fieldNameDisplay);
				firstName.value = "0";
			}

		    firstName.focus();
			return false;
		}
	}
}
function checkForPct(obj){
		var objt = obj;
	    var amt = obj.value;
	    if(amt != null && amt != "")
	    {
	      if(amt < 0 || amt >100)
	        {
	            alert("Please enter value (0-100) for the percentage");
	            obj.value="";
	            objt.focus();
	            return false;
	
	        }
	        if(isNaN(amt))
	        {
	            alert("Please enter a numeric value for the percentage");
	            obj.value="";
	            objt.focus();
	            return false;	
	        }	           
	    }
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


function ismaxlength(obj){
	var mlength=obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : ""
	if (obj.getAttribute && obj.value.length>mlength){
		obj.value=obj.value.substring(0,mlength)
	}
}

//Document Upload Starts
function showDocumentManager()
{
    var v= dom.get("docNumber").value;
    var url;
    if(v==null||v==''||v=='To be assigned')
    {
      url="/egi/docmgmt/basicDocumentManager.action?moduleName=EIS";
    }
    else
    {
      url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+v+"&moduleName=EIS";
    }
    var wdth = 1000;
    var hght = 400;
    window.open(url,'docupload','width='+wdth+',height='+hght);
}

function viewDocumentManager()
{
   var v= dom.get("docNumber").value;
   if(v!='') {
   var url= "/egi/docmgmt/basicDocumentManager!viewDocument.action?docNumber="+v+"&moduleName=EIS";
   var wdth = 1000;
    var hght = 400;
    window.open(url,'docupload','width='+wdth+',height='+hght);
}
   else { 
		alert("No Documents Found");
		return;
	}
}

function viewDocumentManager(docNumber)
{
   if(docNumber!='') {
		var url= "/egi/docmgmt/basicDocumentManager!viewDocument.action?docNumber="+docNumber+"&moduleName=EIS";
	   var wdth = 1000;
	   var hght = 400;
	   window.open(url,'docupload','width='+wdth+',height='+hght);
   }
   else { 
		alert("No Documents Found");
		return;
	}
}

function setupDocNumberBeforeSave()
{
	   var v= dom.get("docNumber").value;
       if(v=='To be assigned')
       {
          dom.get("docNumber").value='';
       }
}
// Document Upload Ends
