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
			var span = $(img).siblings('span');
			//$(span).css({'color' : ''});
			$(span).removeClass('error-msg');
			$(span).text(image.name);
		}
		
		$(inputPhoto).trigger('click');
	});


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
	    	
	    	console.log('target' + e.target.id);
	    	var imgAttach = e.target.id;
	    	
	    	/*if (imgAttach.search('.photo') > 0) {
	    		var span = $(e.target).siblings('span'); 
		    	//$(span).css({'color' : 'red'});
		    	$(span).addClass('error-msg');
		    	$(span).text("Photo is required");
	    	}	 */   		    	
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
            /*console.log('reg_table.fnSettings()' + reg_table.fnSettings());
            var data = reg_table.fnSettings().aoData;
            var selectedRowData = data[reg_table.$('tr.selected')[0]._DT_RowIndex];
            registrationId = selectedRowData._aData[0];
            var isFeeCollectionPending = selectedRowData._aData[9];*/
            //console.log('isFeeCollected= ' + isFeeCollectionPending);
            //console.log('click tr > registrationId = ' + registrationId);
            /*$('#btn_viewdetails').removeClass('disabled');
            
            if (isFeeCollectionPending) {
            	$('#btn_collectfee').removeClass('disabled');
            } else {
            	$('#btn_collectfee').addClass('disabled');
            }*/
        }
    });
	
	/*$('#btn_viewdetails').click( function () {
		console.log('registrationId = ' + registrationId);
		window.open('/mrs/registration/' + registrationId + '?mode=view');
	})
	
	$('#btn_collectfee').click( function () {
		window.open('/mrs/collection/bill/' + registrationId);
	})*/
	
	$('#select-marriagefees').change( function () {
		$('#txt-feepaid').val($(this).val());
		$('#txt_feecriteria').val($('#select-marriagefees option:selected').text());
	})
	
	$('input[id$="email"]').blur(function() {
		var pattern = new RegExp("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
		var email = $(this).val();
		if (!pattern.test(email) && $(this).val().length > 0) {
			var span = $(this).siblings('span'); 
	    	$(span).addClass('error-msg');
	    	$(span).text('Please enter valid email..!');
			$(this).show();
			$(this).val("");
		} else {
			var span = $(this).siblings('span'); 
			$(span).removeClass('error-msg');
	    	$(span).text('');
		}
	});
	
	$('.month-field').blur( function () {
		var month = parseInt( $(this).val() );
		if (month != null && month != undefined && (month < 0 || month > 12)) {
			bootbox.alert("Invalid month(s)..!!");
			$(this).val('');
		}
	})
	
})

var reg_table = null;
$('#btnregistrationsearch').click( function () {

	reg_table = $('#registration_table').dataTable({
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
				var action = '<select class="form-control" id="select-actions'+index+'" style="width:125px;" onchange="performSelectedAction(this);"><option value="default">select</option><option value="view">View</option><option value="correction">Data Entry</option>';
				if (result.feeCollectionPending) {
					action += '<option value="collectfee">Collect Fee</option>';
				}
				action += '</select>';
				console.log('registrationDate=' + result.registrationDate);
				reg_table.fnAddData([result.registrationId, result.registrationNo, result.registrationDate, result.dateOfMarriage, result.husbandName, result.wifeName, certificateIssued, result.status, result.feePaid, result.feeCollectionPending, action]);
            });
			$('#table_container').show();
			/*$('#btn_searchresults').removeClass('hidden');
			$('#btn_searchresults').addClass('show');*/
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
	}
    window.open(url);
}

function validateChecklists() {
	var noOfCheckboxes1 = $('input[id^="ageProofH"]:checked').length;
	var noOfCheckboxes2 = $('input[id^="ageProofW"]:checked').length;

	if (noOfCheckboxes1 == undefined || noOfCheckboxes1 == null || noOfCheckboxes1 == 0) {
		bootbox.alert("Any one Age Proof for Husband is mandatory");
		$('#ageProofHLC').focus();
		return false;
	}

	if (noOfCheckboxes2 == undefined || noOfCheckboxes2 == null || noOfCheckboxes2 == 0) {
		bootbox.alert("Any one Age Proof for Wife is mandatory");
		$('#ageProofWLC').focus();
		return false;
	}

	var noOfResProofCheckboxes1 = $('input[id^="resProofH"]:checked').length;
	var noOfResProofCheckboxes2 = $('input[id^="resProofW"]:checked').length;

	if (noOfResProofCheckboxes1 == undefined || noOfResProofCheckboxes1 == null || noOfResProofCheckboxes1 == 0) {
		bootbox.alert("Any one Residence Proof for Husband is mandatory");
		return false;
	}

	if (noOfResProofCheckboxes2 == undefined || noOfResProofCheckboxes2 == null || noOfResProofCheckboxes2 == 0) {
		bootbox.alert("Any one Residence Proof for Wife is mandatory");
		return false;
	}
	
	return true;
}