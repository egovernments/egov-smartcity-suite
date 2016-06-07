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

jQuery.noConflict();
jQuery(document).ready(function(){
    
	jQuery('.add-attachment').click(function(){
       console.log('came');
       jQuery(this).parent().before('<div class="col-sm-3 add-margin"> <input type="file" class="form-control" required> </div>');
    });
    
	jQuery('.motorcheck').click(function(){
    	jQuery('.motorpart').toggle();
    });
    
});



function resetOnPropertyNumChange(){
	var propertyNo = jQuery("#propertyNo").val();
   	if(propertyNo!="" && propertyNo!=null){
		document.getElementById("address").disabled="true";
    	document.getElementById("boundary").disabled="true"; 
	} else {
        document.getElementById("address").disabled=false;
    	document.getElementById("boundary").disabled=false;  
    }
	document.getElementById("boundary").value='-1';
	document.getElementById("wardName").value="";
	document.getElementById("address").value="";
}

function detailchange(){
	document.getElementById("detailChanged").value = 'true';
}
	
function checkLength(obj,val){
	if(obj.value.length>val) {
		bootbox.alert('Max '+val+' digits allowed')
		obj.value = obj.value.substring(0,val);
	}
}	

function checkMinLength(obj,val){ 
	if(obj.value.length<val) {  
		bootbox.alert('Cannot be less than '+val+' digits')
		obj.value = "";
	}   
}  

function formatCurrency(obj) {
		if(obj.value=="") {
		return;
	} else {
		obj.value=(parseFloat(obj.value)).toFixed(2);
		}
}