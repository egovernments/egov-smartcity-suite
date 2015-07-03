/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
$(document).ready(function(){
	$('#propertyIdentifier').blur(function(){
		loadPropertyDetails();
	});
	
	loadPropertyDetails();
	
	function loadPropertyDetails() {
		propertyID=$('#propertyIdentifier').val()
		if(propertyID != '') {
			$.ajax({
				url: "/ptis/rest/property/"+propertyID,      
				type: "GET",
				dataType: "json",
				success: function (response) { 
					console.log("success"+response);
					
					if(response.errorDetails.errorCode != null && response.errorDetails.errorCode != '') {
						$('#propertyIdentifier').val('');
						$('#applicantname').val('');
						$('#mobileNumber').val('');
						$('#propertyaddress').val('');
						$('#zonewardblock').val('');
						$('#propertytaxdue').val('0.00');
						alert(response.errorDetails.errorMessage);
					}
					else {	
						$('#propertyIdentifierError').html('');
						applicantName = '';
						for(i=0; i<response.ownerNames.length; i++) {
							if(applicantName == '')
								applicantName = response.ownerNames[i].ownerName;
							else 							
								applicantName = applicantName+ ', '+response.ownerNames[i].ownerName;
						}
						$("#applicantname").val(applicantName);
						if(response.ownerNames[0].mobileNumber != '')
							$("#mobileNumber").val(response.ownerNames[0].mobileNumber);
						$("#propertyaddress").val(response.propertyAddress);
						boundaryData = '';
						if(response.boundaryDetails.zoneName != null && response.boundaryDetails.zoneName != '')
							boundaryData = response.boundaryDetails.zoneName;
						if(response.boundaryDetails.wardName != null && response.boundaryDetails.wardName != '') {
							if(boundaryData == '')
								boundaryData = response.boundaryDetails.wardName;
							else
								boundaryData = boundaryData + " / " + response.boundaryDetails.wardName;
						}
						if(response.boundaryDetails.blockName != null && response.boundaryDetails.blockName != '') {
							if(boundaryData == '')
								boundaryData = response.boundaryDetails.blockName;
							else
								boundaryData = boundaryData + " / " +response.boundaryDetails.blockName; 
						}
						$("#zonewardblock").val(boundaryData);
						$("#propertytaxdue").val(response.propertyDetails.taxDue);
					}					
				}, 
				error: function (response) {
					console.log("failed");
				}
			});
		}		
	}
	
	var fileformatsexclude = ['exe'];
	
	$('#file0id, #file1id, #file2id, #file3id, #file4id, #file5id, #file6id, #file7id, #file8id, #file9id').on('change.bs.fileinput',function(e) {
		/*validation for file upload*/
		myfile= $( this ).val();
		var ext = myfile.split('.').pop();
		if($.inArray(ext, fileformatsexclude) > -1) {
			alert(ext+" file format is not allowed");
			$( this ).val('');
			return;    
		}		
	});	
	
});
