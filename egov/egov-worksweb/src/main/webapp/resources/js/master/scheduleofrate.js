/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
function validateSORFormAndSubmit(){
	
    if (document.getElementById("scheduleCategory").value == '' || document.getElementById("scheduleCategory").value == '-1') {
    	var message = document.getElementById('selectCategory').value;
        showMessage('sor_error', message);
        window.scrollTo(0, 0);
    	return false;
    }
    
    if (document.getElementById("code").value == '') {
    	var message = document.getElementById('selectCodeForSor').value;
        showMessage('sor_error', message);
        window.scrollTo(0, 0);
        return false;
    }

    if (document.getElementById("description").value == '') {
    	var message = document.getElementById('selectDescription').value;
        showMessage('sor_error', message);
        window.scrollTo(0, 0);
        return false;
    }
    
    if (document.getElementById("uom").value == '' || document.getElementById("uom").value == '-1') {
    	var message = document.getElementById('selectUOM').value;
        showMessage('sor_error', message);
        window.scrollTo(0, 0);
    	return false;
    }
    
    var sorActivity=document.getElementsByClassName("selectmultilinewk");
    if(sorActivity.length == 0){
    	var message = document.getElementById('selectAtleastOneSOR').value;
        showMessage('sor_error', message);
        window.scrollTo(0, 0);
    	   return false;
    }
    var rate = document.getElementsByClassName("rateforsor");
    for(var i = 0; i < rate.length; i++)
    {
       if(rate.item(i).value == '') {
       	var message = document.getElementById('selectSORRate').value;
        showMessage('sor_error', message);
        window.scrollTo(0, 0);
    	   return false;
       }
    }
    
    var startDate = document.getElementsByClassName("startdate");
    var endDate = document.getElementsByClassName("enddate");
    for(var i = 0; i < startDate.length; i++) {
       if(startDate.item(i).value == '') {
       	var message = document.getElementById('selectSORStartDate').value;
        showMessage('sor_error', message);
        window.scrollTo(0, 0);
    	   return false;
       }
    }
    
    for(var i = 0; i < startDate.length; i++) {
		if(endDate.item(i).value)
		{
		  if(new Date((startDate.item(i).value).split('/').reverse().join('-')).getTime() >  new Date((endDate.item(i).value).split('/').reverse().join('-')).getTime()) {
	    	var message = document.getElementById('errorDateValidate').value;
	    	showMessage('sor_error', message);
	    	window.scrollTo(0, 0);
			return false;
		   }
	    }
   }
    
    for(var i = 0; i < startDate.length-1; i++) {
		if(endDate.length != 1 && endDate.item(i).value == ''){
			var message = document.getElementById('selectSOREndDateValidate').value;
	    	showMessage('sor_error', message);
	    	window.scrollTo(0, 0);
			return false;
		}else {
		  if(new Date((startDate.item(i+1).value).split('/').reverse().join('-')).getTime() <  new Date((endDate.item(i).value).split('/').reverse().join('-')).getTime()) {
	    	var message = document.getElementById('selectSORDateValidate').value;
	    	showMessage('sor_error', message);
	    	window.scrollTo(0, 0);
			return false;
		   }
	    }
   }
    
    var marketRateActivity = document.getElementsByClassName("marketrate");
    //marketrate length validtion
    if(marketRateActivity.length != 0){
    	//marketRate rate validation
        var rate = document.getElementsByClassName("marketrate");
        for(var i = 0; i < rate.length; i++)
        {
           if(rate.item(i).value == '') {
           	var message = document.getElementById('selectMarketRate').value;
            showMessage('sor_error', message);
            window.scrollTo(0, 0);
        	   return false;
           }
        }
        //market rate date validation
        var startDate = document.getElementsByClassName("marketRateStartDate");
        var endDate = document.getElementsByClassName("marketRateEndDate");
        for(var i = 0; i < startDate.length; i++) {
           if(startDate.item(i).value == '') {
           	var message = document.getElementById('selectMarketRateDate').value;
            showMessage('sor_error', message);
            window.scrollTo(0, 0);
        	   return false;
           }
        }
        
        for(var i = 0; i < startDate.length; i++) {
    		if(endDate.item(i).value)
    		{
    		  if(new Date((startDate.item(i).value).split('/').reverse().join('-')).getTime() >  new Date((endDate.item(i).value).split('/').reverse().join('-')).getTime()) {
    	    	var message = document.getElementById('errorMarketDateValidate').value;
    	    	showMessage('sor_error', message);
    	    	window.scrollTo(0, 0);
    			return false;
    		   }
    	    }
       }
        
       for(var i = 0; i < startDate.length-1; i++) {
    	   if(endDate.length != 1 && endDate.item(i).value == ''){
   			var message = document.getElementById('selectMarketRateEndDateValidate').value;
   	    	showMessage('sor_error', message);
   	    	window.scrollTo(0, 0);
   			return false;
   		}else {
    		  if(new Date((startDate.item(i+1).value).split('/').reverse().join('-')).getTime() <  new Date((endDate.item(i).value).split('/').reverse().join('-')).getTime()) {
    	    	var message = document.getElementById('selectMarketRateDateValidate').value;
    	    	showMessage('sor_error', message);
    	    	window.scrollTo(0, 0);
    			return false;
    		   }
    	    }
       }
    }
    var elements = document.getElementsByClassName('rateforsor');
    for (var i = 0; i < elements.length; i++) {
    	if(document.getElementById('idyui-rec'+i).value != "")
    		elements[i].disabled = false;
    }
    var startdate = document.getElementsByClassName('startdate');
    for (var i = 0; i < startdate.length; i++) {
    	if(document.getElementById('idyui-rec'+i).value != "")
    		startdate[i].disabled = false;
    }
    return true;
    
}

$(document).ready(function() {
	var mode = document.getElementById('displData').value;
	if (mode == 'disable') {
		var elements = document.getElementsByClassName('rateforsor');
		for (var i = 0; i < elements.length; i++) {
			if (document.getElementById('idyui-rec' + i).value != "")
				elements[i].disabled = true;
		}
		var startdate = document.getElementsByClassName('startdate');
		for (var i = 0; i < startdate.length; i++) {
			if (document.getElementById('idyui-rec' + i).value != "")
				startdate[i].disabled = true;
		}
	}
});
