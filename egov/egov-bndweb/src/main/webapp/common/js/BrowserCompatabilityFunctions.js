/*
 * @This Files is used to support ajax or any functions in Mozilla and IE
 * @Author :Neeraja C
 * 
 */

/* This function loads the data for YUI autocomplete  */

function loadYUIDataIntoJSArray(link)
{

	if(link != "")
	{
		var yahooArrayObject;
		var req = initiateRequest();
		req.open("GET", link, false);
		req.send(null);
		if (req.status == 200)
		{		
		var values = req.responseText.split("^");
		var result  = values[0];	
		var resultArray = result.split("+");
		yahooArrayObject = new YAHOO.widget.DS_JSArray(resultArray);	
		
		}
		
		return yahooArrayObject;	
	}
}

var yuiflag = new Array();
function callAutoComplete(currRowIndex,fieldObj,divElementName,yahooArrayObject,maxResultsDisplayed,event)
{

	//40 --> Down arrow, 38 --> Up arrow(
	
	if(yuiflag[currRowIndex] == undefined)
	{
		if(event.keyCode != 40)
		{
		if(event.keyCode != 38)
		{		
			var oAutoComp = new YAHOO.widget.AutoComplete(fieldObj,divElementName, yahooArrayObject);
		
			oAutoComp.queryDelay = 0;
			oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
			oAutoComp.useShadow = true;
			oAutoComp.maxResultsDisplayed = maxResultsDisplayed;
			oAutoComp.useIFrame = true;
		}
		}

		yuiflag[currRowIndex] = 1;
	}


}