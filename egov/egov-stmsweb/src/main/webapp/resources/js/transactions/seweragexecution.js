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
  
  
$(document)
		.ready(
				function($) {
					$('#updateBtn').prop("disabled", true);
					$('.result').hide();

					var tableContainer = $("#aplicationSearchResults");
					document.onkeydown = function(evt) {
						var keyCode = evt ? (evt.which ? evt.which
								: evt.keyCode) : event.keyCode;
						if (keyCode == 13) {
							submitButton();
						}
					}

					$(".search").click(function(event) {
						$('.result').show();
						$('#searchSewerageapplication').show();
						var shscNumber = $('#shscNumber').val();
						var wardName = $('#app-mobno').val();
						var applicationtype = jQuery('#appType').val();
						var applicationNumber = $('#applicationNumber').val();
						submit();
						event.preventDefault();
					});
				});

function submit() {
	$("#aplicationSearchResults")
			.dataTable({
				destroy : true,
				responsive : true,
				processing : true,
				filter : true,
				"searching" : false,
				"bDestroy": true,
				"aLengthMenu" : [ [ 10, 25, 50, 100, -1 ],
						[ 10, 25, 50, 100, "All" ] ],
				ajax : {
					url : "/stms/transactions/ajaxsearch",
					type : 'POST',
					dataType : "json",
					beforeSend : function() {
						$('.loader-class').modal('show', {
							backdrop : 'static'
						});
					},
					"data" : getFormData(jQuery('form')),
					complete : function() {
						$('.loader-class').modal('hide');
					},
					 "dataSrc" : function(res) {
								if (res.data.length > 0)
									$('#updateBtn')
											.prop("disabled", false);
								else
									$('#updateBtn').prop("disabled", true);

								return res.data;
							}
				},

				"aoColumnDefs" : [ {
					bSortable : false,
					aTargets : [ 0 ],
					'checkboxes' : {
						'selectRow' : true
					}
				} ],
				'select' : {
					'style' : 'multi'
				},

				columns : [
						{
							data : "id",
							'render' : function(data, type,
									full, meta) {
								return '<input type="checkbox"  class="check_box" name="id" value="'
										+ $('<div/>').text(data).html()
										+ '">';
							}
						},
						{
							data : "applicationnumber"
						},
						{
							class : 'row-detail',
							data : 'shscnumber',
							"render" : function(data, type,
									row, meta) {
								return '<a onclick="openPopup(\'/stms/existing/sewerage/view/'
										+ row.applicationnumber +'/'
										+ row.shscnumber
										+ '\')" href="javascript:void(0);">'+ data + '</a>';
							}
						},
						{
							data : "ownername"
						},
						{
							data : "applicationtype"
						},
						{
							data : "applicationstatus"
						},
						{
							data : "revenueward"
						},
						{
							data : "applicationDate",
						},
						{
							"render" : function(data, type,
									row, meta) {
								$(".datepicker")
										.datepicker(
												{
													format : 'dd/mm/yyyy',
													autoclose : true
												});
								return ' <input name="executionDate" class="form-control datepicker executionDate" id="executionDate" />';
							}
						}, ]
				
			});


	
$('.allCheckBoxClass').on('click',function() {
		$(
			'#aplicationSearchResults tbody input[type="checkbox"]')
			.prop('checked',this.checked);
	});

}

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});
	return indexed_array;
}



$('#updateBtn').click(function() {
	var idx = 0;
	var jsonObj = [];
	var myObj = {};
	$('.check_box:checked').each(function() {
				var $tr = jQuery(this).closest('tr');
				myObj = {"id" : ""+ $tr.find('.check_box').val(),
				"executionDate" : ""+ $tr.find('.executionDate').val(),
					}
				jsonObj.push(myObj);
				idx = idx + 1;

		});

			var obj = {"sewerageExecutionResult" : jsonObj}
			var o = JSON.stringify(obj);
$.ajax({
		url : "/stms/transactions/connexecutionupdate",
		type : "POST",
		beforeSend : function() {
			$('.loader-class').modal('show', {
				backdrop : 'static'
			});
		},
		data : o,
		complete : function() {
			$('.loader-class').modal('hide');
		},
		cache : false,
		contentType : "application/json; charset=utf-8",
		success : function(
				response) {
			if (response == "Success") {
				bootbox.alert("Sewerage connections executed successfully.....");

				 setTimeout(function(){
			           location.reload(); 
			      }, 5000);

				}
			
			if (response == "EmptyList") {
				bootbox.alert("Please select atleast one application to execute connection");
				return false;
			}
			else if (response == "DateValidationFailed") {
				bootbox.alert("Please check  one of selected application connection execution date is entered wrongly, connection execution date must be greater than the application date.");
				return false;
			}
			else if (response == "UpdateExecutionFailed") {
				bootbox.alert("Please enter connection execution date of selected applications.");
				return false;
			}
		},
		error : function(response) {
			bootbox.alert("Failed");
		}
	});
});


function openPopup(url) {
	window.open(url, 'window',
			'scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
}
