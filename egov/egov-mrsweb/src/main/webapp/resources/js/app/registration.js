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

$('body').on('click', 'img.attach-photo', function () {
    
	var img = $(this);
	
    var inputPhoto = document.createElement('INPUT');
	inputPhoto.setAttribute('type', 'file');

	inputPhoto.onchange = function() {

		var image = $(inputPhoto).prop('files')[0];
		var fileReader = new FileReader();

		fileReader.onload = function(e) {
           var imgData = e.target.result; 
           $(img).prop('src', imgData);
           $($(img).siblings('input')).val(imgData);
		}
       
		fileReader.readAsDataURL(image);
		$($(img).siblings('span')).text(image.name);
	}
	
	$(inputPhoto).trigger('click');
});

$(document).ready( function () {
	$('.nav-tabs-top a[data-toggle="tab"]').on('click', function(){
	    $('.nav-tabs-bottom li.active').removeClass('active')
	    var tabRef = '#'+ $(this).prop('href').split('#')[1];	   
	    $('.nav-tabs-bottom a[href="'+tabRef+'"]').parent().addClass('active');
	})

	$('.nav-tabs-bottom a[data-toggle="tab"]').on('click', function(){
	    $('.nav-tabs-top li.active').removeClass('active')
	    var tabRef = '#'+ $(this).prop('href').split('#')[1];	    
	    $('.nav-tabs-top a[href="'+tabRef+'"]').parent().addClass('active');
	})
	
	// Showing the respective tab when mandatory data is not filled in
	$('div.tab-content input').bind('invalid', function(e) {
	    if (!e.target.validity.valid) {
	    	var elem = $(e.target).parents( "div[id$='-info']" )[0];
	    	
	    	$('.nav-tabs-top li.active').removeClass('active')
	    	$('.nav-tabs-bottom li.active').removeClass('active')    	
	    	
	    	if (elem != undefined || elem != null) {
	    		$('.nav-tabs-top a[href="#'+elem.id+'"]').parent().addClass('active');
	        	$('.nav-tabs-bottom a[href="#'+elem.id+'"]').parent().addClass('active');
	        	$('div[id$="-info"].active').removeClass('in active');
	        	$('div#' + elem.id).addClass('in active');	
	    	}
	    } 
	});

	$("input[id$='religionPractice1'").prop("checked", true);
	
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
            console.log('reg_table.fnSettings()' + reg_table.fnSettings());
            var data = reg_table.fnSettings().aoData;
            var selectedRowData = data[reg_table.$('tr.selected')[0]._DT_RowIndex];
            registrationId = selectedRowData._aData[0];
            var isFeeCollectionPending = selectedRowData._aData[9];
            console.log('isFeeCollected= ' + isFeeCollectionPending);
            console.log('click tr > registrationId = ' + registrationId);
            $('#btn_viewdetails').removeClass('disabled');
            
            if (isFeeCollectionPending) {
            	$('#btn_collectfee').removeClass('disabled');
            } else {
            	$('#btn_collectfee').addClass('disabled');
            }
        }
    });
	
	$('#btn_viewdetails').click( function () {
		console.log('registrationId = ' + registrationId);
		window.open('/mrs/registration/' + registrationId + '?mode=view');
	})
	
	$('#btn_collectfee').click( function () {
		window.open('/mrs/collection/bill/' + registrationId);
	})
})

var reg_table = null;
$('#btnregistrationsearch').click( function () {

	reg_table = $('#registration_table').dataTable({		 		        	        
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
	
	$.ajax({
		type : "POST",
		contentType: "application/json",
		accept: "application/json",
		url : "http://localhost:9080/mrs/registration/search",
		data : '{ "registrationNo": "'+$('#registrationNo').val()+'", "dateOfMarriage": "'+$('#dateOfMarriage').val()+'", "husbandName": "'+$('#husbandName').val()+'", "wifeName": "'+$('#wifeName').val()+'", "registrationDate": "'+$('#registrationDate').val()+'" }',
		dataType : "json",
		success : function (response, textStatus, xhr) {
			var searchResults = response.data;
			console.log('searchResults = ' + searchResults);
			$.each(searchResults, function (index, result) {
				var certificateIssued = result.certificateIssued ? 'Yes' : 'No';
				reg_table.fnAddData([result.registrationId, result.registrationNo, result.registrationDate, result.dateOfMarriage, result.husbandName, result.wifeName, certificateIssued, result.status, result.feePaid, result.feeCollectionPending]);
            });
			$('#table_container').show();
			$('#btn_searchresults').removeClass('hidden');
			$('#btn_searchresults').addClass('show');
			$('#registration_table_length').remove();				
			$('#registration_table_filter').addClass('text-right');
		},
		error : function (xhr, textStatus, errorThrown) {
			console.log ( 'errorThrown=' + errorThrown );
		}
	});
});