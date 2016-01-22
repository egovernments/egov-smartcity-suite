/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
# accountability and the service delivery of the government  organizations.
#   
#  Copyright (C) <2015>  eGovernments Foundation
#   
#  The updated version of eGov suite of products as by eGovernments Foundation 
#  is available at http://www.egovernments.org
#   
#  This program is free software: you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  any later version.
#   
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#   
#  You should have received a copy of the GNU General Public License
#  along with this program. If not, see http://www.gnu.org/licenses/ or 
#  http://www.gnu.org/licenses/gpl.html .
#   
#  In addition to the terms of the GPL license to be adhered to in using this
#  program, the following additional terms are to be complied with:
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
   function validateSchedulerReport(){
		  if (!validateForm_eBSchedulerReportForm()) {
	       	undoLoadingMask();
	   		return false;
	           }
		   
	   	return true;
	    }
          
    var path="../..";
	var oAutoCompEntityForJV;
	function autocompleteConsumerNumbers(obj)
	{
	  	   oACDS = new YAHOO.widget.DS_XHR(path+"/EGF/common/eBCommon!ajaxLoadEBConsumerNumbers.action", [ "~^"]);
		   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
		   oACDS.scriptQueryParam = "startsWith";
		   oAutoCompEntityForJV = new YAHOO.widget.AutoComplete(obj.name,'codescontainer',oACDS);
		   oAutoCompEntityForJV.doBeforeSendQuery = function(sQuery){
			   loadWaitingImage(); 
			   return sQuery+"&consumerNumber="+document.getElementById("code").value;
		   } 
		   oAutoCompEntityForJV.queryDelay = 0.5;
		   oAutoCompEntityForJV.minQueryLength = 3;
		   oAutoCompEntityForJV.prehighlightClassName = "yui-ac-prehighlight";
		   oAutoCompEntityForJV.useShadow = true;
		   oAutoCompEntityForJV.forceSelection = true;
		   oAutoCompEntityForJV.maxResultsDisplayed = 20;
		   oAutoCompEntityForJV.useIFrame = true;
		   oAutoCompEntityForJV.doBeforeExpandContainer = function(oTextbox, oContainer, sQDetauery, aResults) {
			   clearWaitingImage();
		           var pos = YAHOO.util.Dom.getXY(oTextbox);
		           pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
		           oContainer.style.width=300;
		           YAHOO.util.Dom.setXY(oContainer,pos);
		           return true;
		   }
	}
	function splitConsumerNumber(obj) 	{	
		var entity=obj.value;
		if(entity.trim()!="")
		{
			var entity_array=entity.split("`~`");
			if(entity_array.length==2)
			{
				document.getElementById("code").value=entity_array[0].split("`-`")[0];
			}
		}

	}
	function splitConsumerNumberForSch(obj) 	{	
		var entity=obj.value;
		//bootbox.alert(entity);
		if(entity.trim()!="")
		{
			var entity_array=entity.split("`~`");
			if(entity_array.length==2)
			{
				document.getElementById("code").value=entity_array[0];
				//bootbox.alert(document.getElementById("code").value);
			}
		}

	}

	function autocompleteAccountNumbers(obj)
	
	{
	  	   oACDS = new YAHOO.widget.DS_XHR(path+"/EGF/common/eBCommon!ajaxLoadEBAccountNumbers.action", [ "~^"]);
		   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
		   oACDS.scriptQueryParam = "startsWith";
		   oAutoCompEntityForJV = new YAHOO.widget.AutoComplete(obj.name,'codescontainer',oACDS);
		   oAutoCompEntityForJV.doBeforeSendQuery = function(sQuery){
			   loadWaitingImage(); 
			   return sQuery+"&accountNumber="+document.getElementById("name").value;
		   } 
		   oAutoCompEntityForJV.queryDelay = 0.5;
		   oAutoCompEntityForJV.minQueryLength = 3;
		   oAutoCompEntityForJV.prehighlightClassName = "yui-ac-prehighlight";
		   oAutoCompEntityForJV.useShadow = true;
		   oAutoCompEntityForJV.forceSelection = true;
		   oAutoCompEntityForJV.maxResultsDisplayed = 20;
		   oAutoCompEntityForJV.useIFrame = true;
		   oAutoCompEntityForJV.doBeforeExpandContainer = function(oTextbox, oContainer, sQDetauery, aResults) {
			   clearWaitingImage();
		           var pos = YAHOO.util.Dom.getXY(oTextbox);
		           pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
		           oContainer.style.width=300;
		           YAHOO.util.Dom.setXY(oContainer,pos);
		           return true;
		   }
	}
	function splitAccountNumber(obj) 
	{	
		var entity=obj.value;
		if(entity.trim()!="")
		{
			var entity_array=entity.split("`~`");
			if(entity_array.length==2)
			{
				document.getElementById("name").value=entity_array[0].split("`-`")[0];
			}
		}

	}
	
	function splitAccountNumberForSch(obj) 
	{	
		var entity=obj.value;
		if(entity.trim()!="")
		{
			var entity_array=entity.split("`~`");
			if(entity_array.length==2)
			{
				document.getElementById("name").value=entity_array[0];
			}
		}

	}

	function validateRTGSReport()
	{
		var fromDate = document.getElementById('fromDate').value;
		var toDate = document.getElementById('toDate').value;
		var month = document.getElementById('month').value;
		var year = document.getElementById('year').value;
		if(fromDate == "" && toDate == ""){
			if(month == "" || year == ""){
				bootbox.alert("Enter the month and year or date range");
			 return false;
			}
		}
		if(month == "" && year == ""){
			if(fromDate == "" || toDate == ""){
				bootbox.alert("Enter the month and year or date range");
			 return false;
			}
		}
		
		if(month!=""){
			if(year == ""){
				bootbox.alert("Please select Year");
			  return false;
			}
		}
		
		return true;
		
	}
		
	
