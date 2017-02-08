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

var $partyTypeId = 0;
$(document).ready(function() {
	$partyTypeId = $('#partyType').val();
	
	var advanceRequisitionNumber = new Bloodhound(
			{
				datumTokenizer : function(datum) {
					return Bloodhound.tokenizers.whitespace(datum.value);
				},
				queryTokenizer : Bloodhound.tokenizers.whitespace,
				remote : {
					url : '/EGF/common/ajaxarfnumbers-searcharf?advanceRequisitionNumber=%QUERY',
					filter : function(data) {
						return $.map(data, function(ct) {
							return {
								name : ct
							};
						});
					}
				}
			});

	advanceRequisitionNumber.initialize();
	
	var advanceRequisitionNumber_typeahead = $('#arfNumber').typeahead({
		hint : true,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'name',
		source : advanceRequisitionNumber.ttAdapter()
	}).on('typeahead:selected', function(event, data) {
		loadPartyType(data.name);
	});
	
	jQuery('#btnsearch').click(function(e) {

		if (!validateFields())
			return false
		callAjaxSearch();
	});
	
	
	$('#btncreateadvancepay').click(
			function(e) {
				var advanceBillId = $('input[name=selectCheckbox]:checked').val();
				if (advanceBillId == null) {
					bootbox.alert($('#errorSelectBill').val());
				} else {
					window.location = "/EGF/advancepayment/newform?billId="
							+ advanceBillId;
				}
			});

});


function loadPartyType(advanceRqnNumber) {
	if (!advanceRqnNumber) {
		$('#partyType').empty();
		$('#partyType').append(
				$('<option>').text('Select from below').attr('value', ''));
		return;
	} else {

		$.ajax({
			method : "GET",
			url : "/EGF/common/getpartytypebyarfnumber",
			data : {
				advanceRqnNumber : advanceRqnNumber
			},
			async : true
		}).done(
				function(response) {
					$('#partyType').empty();
					$('#partyType').append(
							$("<option value=''>Select from below</option>"));
					$.each(response, function(index, value) {
						var selected = "";
						if ($partyTypeId && $partyTypeId == value.id) {
							selected = "selected";
						}
						$('#partyType').append(
								$('<option ' + selected + '>').text(value.code)
										.attr('value', value.id));
					});
				});
	}
}



function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/EGF/advancepayment/ajaxsearch",
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},
				"bDestroy" : true,
				'bAutoWidth' : false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : []
				},
				"fnRowCallback" : function(row, data, index) {
					$('td:eq(0)', row)
							.html(
									'<input type="radio" name="selectCheckbox" id="selectCheckbox'
											+ index + '" value="' + data.billId
											+ '"/>');
					$('td:eq(1)', row).html(index + 1);
					if (data.advanceRequisitionNumber != null) {
						$('td:eq(2)', row).html(
								'<a href="javascript:void(0);" onclick="viewAdvanceBill(\''
										+ data.advanceReqSourcePath + '\')">'
										+ data.advanceRequisitionNumber
										+ '</a>');
						$('#btncreatecr').show();
					}
					if (data.voucherNumber != null)
						$('td:eq(4)', row).html(
								'<a href="javascript:void(0);" onclick="viewVoucher(\''
										+ data.voucherId + '\')">'
										+ data.voucherNumber + '</a>');

				},
				aaSorting : [],
				columns : [ {
					"data" : ""
				}, {
					"data" : ""
				}, {
					"data" : "advanceRequisitionNumber",
					"sClass" : "text-left"
				}, {
					"data" : "partyType",
					"sClass" : "text-left"
				}, {
					"data" : "voucherNumber",
					"sClass" : "text-left"
				}, {
					"data" : "amount",
					"sClass" : "text-right"
				} ]
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
function viewVoucher(voucherId) {
	window.open("/EGF/voucher/preApprovedVoucher-loadvoucherview.action?vhid="
			+ voucherId, '',
			'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function viewAdvanceBill(advanceReqSourcePath) {
	window.open(advanceReqSourcePath, '',
			'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function validateFields() {
	if ($('#fund').val() == '') {
		bootbox.alert("Please Select Fund");
		return false;
	}
	if ($('#arfNumber').val() == ''
			&& ($('#partyType').val() == '' || $('#partyType') == null)) {
		bootbox.alert("Please select ARF Number Or Party Type");
		return false;
	}
	return true;
}
