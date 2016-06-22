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
$('#Save').click(function() {
	var totalRows = $("#tblmilestone tbody tr").length;
	var flag = false;
	if(flag)
		bootbox.alert('Completion Date should be greater than Schedule Start Date');
	else {
		if($('#mbHeader').valid()) {
			var mbHeaderId = $('#id').val();
			$('.loader-class').modal('show', {backdrop: 'static'});
			$.ajax({
				type: "POST",
				url: "/egworks/measurementbook/create/" + mbHeaderId,
				cache: true,
				dataType: "json",
				"data": getFormData(jQuery('form')),
				success: function (message) {
					$('#measurementBookDiv').remove();
					$('#successMessage').html(message);
					$('#successPage').show();
				},
				error: function (error) {
					console.log(error.responseText.slice(0,-2));
					var json = $.parseJSON(error.responseText.slice(0,-2));
					
					$.each(json, function(key, value){
						$('#errorMessage').append(value + '</br>');
					});
					$('#errorMessage').show();
				}
			});
			$('.loader-class').modal('hide');
		}
		else
			return false;
	}
	
	return false;
});

$('#searchAndAdd').click(function() {
	var workOrderEstimateId = $('#workOrderEstimateId').val();
	var workOrderNumber = $('#workOrderNumber').val();
	
	window.open("/egworks/measurementbook/searchactivityform?woeId=" + workOrderEstimateId + "&workOrderNo=" + workOrderNumber, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
});