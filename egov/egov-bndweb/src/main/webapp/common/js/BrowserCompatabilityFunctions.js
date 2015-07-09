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
