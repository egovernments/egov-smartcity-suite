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

$(document).ready( function () {
	
	$('#table_search').keyup(function(){
    	$('#registration_table').fnFilter(this.value);
    });
	
	var registrationId;
	$('body').on( 'click', 'tr', function () {
		if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
            $('#btn_viewdetails').addClass('disabled');
            $('#btn_collectfee').addClass('disabled');
        } else {
            $('#registration_table > tbody > tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
    });
	
	$('#cb-registrationapproved').click( function () {
		$('#cb-registrationrejected').prop('checked', false);
	})
	
	$('#cb-registrationrejected').click( function () {
		$('#cb-registrationapproved').prop('checked', false);
	})
	
})

var regstatus_table = $('#registrationstatus_table').dataTable({
							"sPaginationType": "bootstrap",
							"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
							"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
						});
$('#btn_registrationstatus_search').click( function () {
	
	var date = $('#txt-fromdate').val().split('/');
	var fromDate = date[2] + '-' + date[1] + '-' + date[0];
	date = $('#txt-todate').val().split('/');
	var toDate = date[2] + '-' + date[1] + '-' + date[0];
	
	$.ajax({
		type : "POST",
		contentType: "application/json",
		accept: "application/json",
		url : "/mrs/report/registrationstatus",
		data : '{ "fromDate": "'+fromDate+'", "toDate": "'+toDate+'", "registrationApproved": "'+$('#cb-registrationapproved').prop("checked")+'", "registrationRejected": "'+$('#cb-registrationrejected').prop("checked")+'" }',
		dataType : "json",
		success : function (response, textStatus, xhr) {
			var searchResults = response.data;
			console.log('searchResults = ' + searchResults);
			regstatus_table.fnClearTable();
			$.each(searchResults, function (index, result) {				
				regstatus_table.fnAddData([
				                           (index + 1), 
				                           result.husbandName, 
				                           result.wifeName, 
				                           result.registrationDate, 
				                           result.dateOfMarriage, 
				                           result.applicationType, 
				                           result.feePaid, 
				                           result.status, 
				                           result.remarks
				 ]);
            });
			$('#regstatustable_container').show();
			$('#registrationstatus_table_length').remove();				
			$('#registrationstatus_table_filter').addClass('text-right');
		},
		error : function (xhr, textStatus, errorThrown) {
			console.log ( 'errorThrown=' + errorThrown );
		}
	});
});

