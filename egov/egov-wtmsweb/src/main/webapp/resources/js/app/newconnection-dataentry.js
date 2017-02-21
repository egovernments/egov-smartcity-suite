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
	
		if($('#connectionType').val()==="METERED")
		$('#waterSupplyDiv').removeClass('display-hide');
	

	$("#connectionType").change(function(){
		
		
		if($('#connectionType').val() === "METERED")
		{
			$('#spanmonthlymandatory').hide();
			$('#waterSupplyDiv').removeClass('display-hide');
			$('#waterSupplyType').attr('required','required');
		}
		else
		{
		   $('#spanmonthlymandatory').show();
		   $('#waterSupplyDiv').addClass('display-hide');
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
		/*	$('#searchResultDiv').show();*/

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
			event.preventDefault();
			
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
				} else {
				return true;
			}
			
	        return true;
			
			
	}