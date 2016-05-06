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
})

var reg_table = $('#registration_table').dataTable({
	"sPaginationType": "bootstrap",
	"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
	"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
	columnDefs: [{
           "targets": [ 0 ],
           "visible": false,
           "searchable": false
       }, {
           "targets": [ 9 ],
           "visible": false,
           "searchable": false
       }
	]
});

$('#btnregistrationsearch').click( function () {
	$.ajax({
		type : "POST",
		contentType: "application/json",
		accept: "application/json",
		url : "http://localhost:9080/mrs/registration/search",
		data : '{ "registrationNo": "'+$('#registrationNo').val()+'", "dateOfMarriage": "'+$('#dateOfMarriage').val()+'", "husbandName": "'+$('#husbandName').val()+'", "wifeName": "'+$('#wifeName').val()+'", "registrationDate": "'+$('#registrationDate').val()+'" }',
		dataType : "json",
		success : function (response, textStatus, xhr) {
			var searchResults = response.data;
			reg_table.fnClearTable();
			$.each(searchResults, function (index, result) {
				var certificateIssued = result.certificateIssued ? 'Yes' : 'No';
				var action = '<select class="form-control" id="select-actions'+index+'" style="width:125px;" onchange="performSelectedAction(this);"><option value="default">select</option><option value="view">View</option><option value="correction">Data Entry</option>';
				console.log('feeCollectionPending=' + result.feeCollectionPending);
				if (result.feeCollectionPending) {
					action += '<option value="collectfee">Collect Fee</option>';
				}
				
				if (result.status === 'Approved' && result.certificateIssued) {
					action += '<option value="reissue">Re-Issue</option>';
				}
				
				action += '</select>';
				console.log('registrationDate=' + result.registrationDate);
				reg_table.fnAddData([result.registrationId, 
				                     result.registrationNo, 
				                     result.registrationDate, 
				                     result.dateOfMarriage, 
				                     result.husbandName, 
				                     result.wifeName, 
				                     certificateIssued, 
				                     result.status, 
				                     result.feePaid, 
				                     result.feeCollectionPending, 
				                     action
				]);
            });
			$('#table_container').show();
			$('#registration_table_length').remove();				
			$('#registration_table_filter').addClass('text-right');
		},
		error : function (xhr, textStatus, errorThrown) {
			console.log ( 'errorThrown=' + errorThrown );
		}
	});
});

function performSelectedAction(dropdown) {
	var optionSelected = $(dropdown).val();	
	var data = reg_table.fnSettings().aoData;
    var selectedRowData = data[reg_table.$('tr.selected')[0]._DT_RowIndex];
    registrationId = selectedRowData._aData[0];    
    var url = '';    
    if (optionSelected === 'view') {
		url = '/mrs/registration/' + registrationId + '?mode=view';
	} else if (optionSelected === 'collectfee') {
		url = '/mrs/collection/bill/' + registrationId;
	} else if (optionSelected === 'correction') {
		url = '/mrs/registration/update/' + registrationId;
	} else if (optionSelected === 'reissue') {
		url = '/mrs/reissue/create/' + registrationId;
	}
    
    if (optionSelected != 'select') {
    	window.open(url, '_blank');
    }
}
