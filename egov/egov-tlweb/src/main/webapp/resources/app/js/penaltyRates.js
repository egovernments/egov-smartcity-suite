/*-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/

$(document).ready(function()
{
$( "#search" ).click(function( event ) {
	var valid = $('#penaltyform').validate().form();
	if(!valid)
		{
		bootbox.alert("Please fill mandatory fields");
		return false;
		}
	  var param="licenseAppType=";
	  param=param+$('#licenseAppType').val();
	   $.ajax({
			url: "/tl/penaltyRates/search?"+param,
			type: "GET",
			//dataType: "json",
			success: function (response) {
				 $('#resultdiv').html(response);
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	 
});

$( "#add-row" ).click(function( event ) {
	var rowCount = $('#result tr').length;
	if(!checkforNonEmptyPrevRow())      
		return false;
	var prevUOMFromVal=getPrevUOMFromData();
	var content= $('#resultrow0').html();
	resultContent=   content.replace(/0/g,rowCount-1);   
	$(resultContent).find("input").val("");
	$('#result > tbody:last').append("<tr>"+resultContent+"</tr>"); 
	$('#result tr:last').find("input").val("");   
	intiUOMFromData(prevUOMFromVal);  
	patternvalidation(); 
});    

$( "#save" ).click(function( event ) {
	if(!validateDetailsBeforeSubmit()){
		return false;
	} 
	var licenseAppTypeDisabled=$('#licenseAppType').is(':disabled');
		$('#licenseAppType').removeAttr("disabled");
		var pr=$('#penaltyform').serialize();
		  $.ajax({
				url: "/tl/penaltyRates/create",
				type: "POST",
				data: pr,
				success: function (response) {
					console.log("success"+response );
					 $('#resultdiv').html(response);
					 if(licenseAppTypeDisabled)
						 $('#licenseAppType').attr("disabled", true); 
					 bootbox.alert("Details saved Successfully");
				}, 
				error: function (response) {
					console.log("failed");
					if(licenseAppTypeDisabled)
						$('#licenseAppType').attr("disabled", true);
					bootbox.alert("Failed to Save Details");
				}
			});
	});
});
