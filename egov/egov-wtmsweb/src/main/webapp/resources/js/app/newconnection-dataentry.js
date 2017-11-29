/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
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
 *
 */
$(document).ready(function(){
	

	$("#connectionType").change(function(){
		if($('#legacy').val()){
			if($('#connectionType').val() === "METERED")
			{	
				$('#monthlyFee').attr('disabled',true);
				$('#monthlyFee').val('');
				$('#metereddetails').show();
				$('#spanmonthlymandatory').hide();
				$('.showfields').show();
				$('#waterSupplyType').attr('required','required');
				$('#buildingName').attr('required','required');
			}
			else
			{
			    $('#metereddetails').hide();
			    $('#spanmonthlymandatory').show();
			    $('.showfields').hide();
			    $('#monthlyFee').removeAttr('disabled');
			    $('#waterSupplyType').removeAttr('required');
			    $('#buildingName').removeAttr('required');
			    $("#waterSupplyType").val('');
				$("#buildingName").val('');
			}
		}
	});
	
		
		$(".btn-primary").click(function(event){
			
			var previous = parseInt($('#previousReading').val());
			var current = parseInt($('#currentcurrentReading').val());
			if( (previous.length > 0) && (current.length  > 0) ){
				if (previous > current){
					bootbox.alert("Previous reading should not be greater than the current reading");
					return false;
				}
			}

			if($('#executionDate').val() != '' && $('#existreadingDate').val() != ''){
				var start = $('#executionDate').val();
				var end = $('#existreadingDate').val();
				var stsplit = start.split("/");
					var ensplit = end.split("/");
					
					start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
					end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];
					
					if(!validRange(start,end))
					{
					return false;
					}else{
						if (previous > current){
							bootbox.alert("Previous reading should not be greater than the current reading");
							return false;
						}
					}
			}
			
			document.forms[0].submit;
			return true;
			
		});
});	


function validRange(start, end) {
	        var startDate = Date.parse(start);
	        var endDate = Date.parse(end);
			
	        // Check the date range, 86400000 is the number of milliseconds in one day
	        var difference = (endDate - startDate) / (86400000 * 7);
	        if (difference < 0) {
	        	bootbox.alert("Last Reading date should not be less than the connection date.");
				$('#end_date').val('');
				return false;
				} 
	        else {
				return true;
			}
	}