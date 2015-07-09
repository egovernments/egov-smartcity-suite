/*-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
# accountability and the service delivery of the government  organizations.
# 
# Copyright (C) <2015>  eGovernments Foundation
# 
# The updated version of eGov suite of products as by eGovernments Foundation
# is available at http://www.egovernments.org
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program. If not, see http://www.gnu.org/licenses/ or
# http://www.gnu.org/licenses/gpl.html .
# 
# In addition to the terms of the GPL license to be adhered to in using this
# program, the following additional terms are to be complied with:
# 
# 1) All versions of this program, verbatim or modified must carry this
#    Legal Notice.
# 
# 2) Any misrepresentation of the origin of the material is prohibited. It
#    is required that all modified versions of this material be marked in
#    reasonable ways as different from the original version.
# 
# 3) This license does not grant any rights to any user of the program
#    with regards to rights under trademark law for use of the trade names
#    or trademarks of eGovernments Foundation.
# 
# In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
/* This Function doesnt work for YUI 2.7 -Yui 2.7 is not supporting for item in multiple rows

var yuiflag = new Array();

/* Dont refer -for test */

function showAutocompleteTemp(obj,event,divName,url)
{
	var currRow=getRow(obj);
	var coaCodeObj = getControlInBranch(currRow,obj.id);
	var objName=obj.name;
	var acCodeObj=document.getElementById('coaCode');
	container=document.getElementById('codescontainer');
	objPosition=findPos(obj);
	container.style.position="absolute";
	container.style.left=objPosition[0];
	container.style.top=objPosition[1]+20;
	container.style.width=400;
	

	if(yuiflag[currRow.rowIndex] == undefined)
	{

		if(event.keyCode != 40 )
		{
		if(event.keyCode != 38 )
		{

		var iDS =  new YAHOO.widget.DS_XHR(url,["Results","value"]);
		iDS.responseType = YAHOO.widget.DS_XHR.TYPE_JSON;
		iDS.scriptQueryParam= "itemNo";
		var iAC = new YAHOO.widget.AutoComplete(coaCodeObj, divName, iDS);

		}
	}
	yuiflag[currRow.rowIndex] = 1;


	}

}

/* Dont refer -for test */
function showAutocompleteWORK(obj,event,divName,url)
{


	var currRow=getRow(obj);
	var coaCodeObj = getControlInBranch(currRow,obj.id);
	var objName=obj.name;
	container=document.getElementById(divName);
	objPosition=findPos(obj);
	container.style.position="absolute";
	container.style.left=objPosition[0];
	container.style.top=objPosition[1]+20;
	container.style.width=400;

	var iDS =  new YAHOO.widget.DS_XHR(url,["Results","value"]);

	// Set the responseType
	iDS.responseType = YAHOO.widget.DS_XHR.TYPE_JSON;

	iDS.scriptQueryParam= "itemNo";
	// Define the schema of the JSON results
	//   iDS.queryMatchContains = true;
	//iDS.scriptQueryAppend = "output=json&results=100"; // Needed for YWS

	var iAC = new YAHOO.widget.AutoComplete(coaCodeObj, divName, iDS);


}


/*Refer this*/

function showAutocomplete(obj,event,divName,url)
{

	var currRow=getRow(obj);
	var coaCodeObj = getControlInBranch(currRow,obj.id);
	var objName=obj.name;

	var container=document.getElementById(divName);
	objPosition=findPos(obj);
	container.style.position="absolute";
	container.style.left=objPosition[0];
	container.style.top=objPosition[1]+20;
	container.style.width=400;
	var currRowIndex=1;

	if(yuiflag[currRow.rowIndex] == undefined)
	{
	if(event.keyCode != 40)
	{
	if(event.keyCode != 38)
	{

		var iDS =  new YAHOO.widget.DS_XHR(url,["Results","value"]);
		// Set the responseType
		iDS.responseType = YAHOO.widget.DS_XHR.TYPE_JSON;
		iDS.scriptQueryParam= obj.id;
		// Define the schema of the JSON results
		//   iDS.queryMatchContains = true;
		//iDS.scriptQueryAppend = "output=json&results=100"; // Needed for YWS
		var iAC = new YAHOO.widget.AutoComplete(coaCodeObj, divName, iDS);
		iAC.forceSelection = true;


			var itemHandler = function(sType, aArgs) {   
			var myAC = aArgs[0]; // reference back to the AC instance   
			var elLI = aArgs[1]; // reference to the selected LI element   
			var oData = aArgs[2]; // object literal of selected item's result data 


			itemDetailsAjax(oData[0],obj);

			// update hidden form field with the selected item's ID   
			//myHiddenField.value = oData.id;   
			}; 

		//On selection of item from list this handler ll be called
		iAC.itemSelectEvent.subscribe(itemHandler);  

	}
	}

	yuiflag[currRow.rowIndex] = 1;

	}


}






