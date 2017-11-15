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

$(document).ready(function(){

	loadDateFields();
	$('#judgmentType').change(function(){
		loadDateFields();
	});
	
	
function loadDateFields(){
	if ($('#judgmentType :selected').text().localeCompare("Enquiry") == 0 ) { 
		$("#enquirydetails").show();
		}
	else{
		$("#enquirydetails").hide();
	}
		
	if($('#judgmentType :selected').text().localeCompare("Ex-Parte Order") == 0) {  
		$("#exparteorder1").show();
    	$("#exparteorder2").show();
    	$("#exparteorder3").show();
	}else{
		$("#exparteorder1").hide();
    	$("#exparteorder2").hide();
    	$("#exparteorder3").hide();
	}
	
}


});

$("#costAwarded").on("keyup", function(){  // validate 10 digits and two decimal points
    var valid = /^\d{0,10}?$/.test(this.value),
        val = this.value;
    
    if(!valid){
        console.log("Invalid input!");
        this.value = val.substring(0, val.length - 1);
    }
});

$("#compensationAwarded").on("keyup", function(){  // validate 10 digits and two decimal points
    var valid = /^\d{0,10}?$/.test(this.value),
        val = this.value;
    
    if(!valid){
        console.log("Invalid input!");
        this.value = val.substring(0, val.length - 1);
    }
});

$('#buttonSubmit').click(function() {
	
	if($("#judgmentform").valid())
	{
		document.forms[0].submit();
		return true;
		}
	return false;
	
});



$('#btnclose').click(function(){
	bootbox.confirm({
	    message: 'Information entered in this screen will be lost if you close this page ? Please confirm if you want to close. ',
	    buttons: {
	        'cancel': {
	            label: 'No',
	            className: 'btn-default pull-right'
	        },
	        'confirm': {
	            label: 'Yes',
	            className: 'btn-danger pull-right'
	        }
	    },
	    callback: function(result) {
	        if (result) {
	             window.close();
	        }
	    }
	});
	
});
