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
	$(".show-ManualLcNumber").hide(); 
	$("#bipartisanDetails[0].governmentDepartment").prop("disabled", true);
	$("#seniordov1").hide(); 
    $("#seniordov2").hide(); 
    $("#seniordov3").hide(); 
   
	
});
function enableGovtDept()
{
	var govtcheck=$('#activeid').val() ;
	if(govtcheck==true)
		{
		$("#bipartisanDetails[0].governmentDepartment").prop("disabled", false);

		}
	
}

function checkLCType()
{
	 if($('#lcNumberType').val() == "MANUAL")
{
		 $(".show-ManualLcNumber").show(); 

}
else
{
	$(".show-ManualLcNumber").hide(); 
}
document.getElementById("lcNumber").value="";	
document.getElementById("wpYear").value="--select---";
 
}
	
function addPetRow()
{     
			var tableObj=document.getElementById('petitionDetails');
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			var rowObj = tableObj.rows[1].cloneNode(true);
			
			nextIdx=(lastRow-1);
			jQuery(rowObj).find("input, select").each(
					function() {

					jQuery(this).attr({
								'id' : function(_, id) {
									return id.replace('[0]', '['
											+ nextIdx + ']');
								},
								'name' : function(_, name) {
									return name.replace('[0]', '['
											+ nextIdx + ']');
								}/*,
								'data-idx' : function(_,dataIdx)
								{
									return nextIdx;
								}*/
					});  
		   });

		   tbody.appendChild(rowObj);}

function addResRow()
{     
	var index=document.getElementById('respodantDetails').rows.length-1;
	    	var tableObj=document.getElementById('respodantDetails');
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			var rowObj = tableObj.rows[1].cloneNode(true);
			
			nextIdx=(lastRow-1);
			jQuery(rowObj).find("input, select").each(
					function() {

					jQuery(this).attr({
								'id' : function(_, id) {
									return id.replace('[0]', '['
											+ nextIdx + ']');
								},
								'name' : function(_, name) {
									return name.replace('[0]', '['
											+ nextIdx + ']');
								}
					});  
		   });

		   tbody.appendChild(rowObj);
		
 }

$(document).on('click',"#pet_delete_row",function (){
	var table = document.getElementById('petitionDetails');
    var rowCount = table.rows.length;
    var counts = rowCount - 1;
    var k = 2;
    var m;
    if(counts==1)
	{
		bootbox.alert("This Floor cannot be deleted");
		return false;
	}else{
    for(m=2;m<=counts;m++){ 
    	$(this).closest('tr').remove();		
    	var prevIndex = m-1;
    	var currentIndex = k-1; 
    	var itemDesc = '#bipartisanDetails'+prevIndex+'name';
    	var quantity = '#bipartisanDetails'+prevIndex+'address';
    	var unitOfMeasurement = '#bipartisanDetails'+prevIndex+'contactNumber';
    	var unitRate = '#bipartisanDetails'+prevIndex+'governmentDepartment';
    	
    		$(itemDesc).attr("id", 'bipartisanDetails'+currentIndex+'name'); 
        	$(quantity).attr("id", 'bipartisanDetails'+currentIndex+'address'); 
        	$(unitOfMeasurement).attr("id", 'bipartisanDetails'+currentIndex+'contactNumber'); 
        	$(unitRate).attr("id", 'bipartisanDetails'+currentIndex+'governmentDepartment'); 
        	k++;
    	
    }	
	}
});

$(document).on('click',"#res_delete_row",function (){
	var table = document.getElementById('respodantDetails');
    var rowCount = table.rows.length;
    var counts = rowCount - 1;
    var j = 2;
    var i;
    if(counts==1)
	{
		bootbox.alert("This Floor cannot be deleted");
		return false;
	}else{
    for(i=2;i<=counts;i++){ 
    	$(this).closest('tr').remove();		
    	var prevIndex = i-1;
    	var currentIndex = j-1; 
    	var itemDesc = '#bipartisanDetailsBeanList'+prevIndex+'name';
    	var quantity = '#bipartisanDetailsBeanList'+prevIndex+'address';
    	var unitOfMeasurement = '#bipartisanDetailsBeanList'+prevIndex+'contactNumber';
    	var unitRate = '#bipartisanDetailsBeanList'+prevIndex+'governmentDepartment';
    	
    		$(itemDesc).attr("id", 'bipartisanDetailsBeanList'+currentIndex+'name'); 
        	$(quantity).attr("id", 'bipartisanDetailsBeanList'+currentIndex+'address'); 
        	$(unitOfMeasurement).attr("id", 'bipartisanDetailsBeanList'+currentIndex+'contactNumber'); 
        	$(unitRate).attr("id", 'bipartisanDetailsBeanList'+currentIndex+'governmentDepartment'); 
        	j++;
    	
    }	
	}
});
