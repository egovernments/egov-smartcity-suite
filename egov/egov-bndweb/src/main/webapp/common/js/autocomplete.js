
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






