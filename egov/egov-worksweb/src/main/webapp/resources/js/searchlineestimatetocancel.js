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
jQuery('#btnsearch').click(function(e) {
		callAjaxSearch();
});

$(document).ready(function(){
	$('#departments').trigger('change');
    var lineEstimateNumber = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '/egworks/lineestimate/lineEstimateNumbers?name=%QUERY',
            filter: function (data) {
                return $.map(data, function (ct) {
                    return {
                        name: ct
                    };
                });
            }
        }
    });
   
    lineEstimateNumber.initialize();
	var lineEstimateNumber_typeahead = $('#lineEstimateNumber').typeahead({
		hint : true,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'name',
		source : lineEstimateNumber.ttAdapter()
	});
});

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/egworks/lineestimate/cancel/ajax-search",
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},
				"bDestroy" : true,
				'bAutoWidth': false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : []
				},
				"fnRowCallback" : function(row, data, index) {
					$('td:eq(0)',row).html('<input type="radio" data="'+ data.id +'" name="selectCheckbox" value="'+ data.id +'"/>');
					$('td:eq(2)', row).html(
							'<a href="javascript:void(0);" onclick="openLineEstimate(\''
									+ data.id + '\')">'
									+ data.lineEstimateNumber + '</a>');
					if(data.estimateNumber != null) {
						var estimateNumber = data.estimateNumber;
						estimateNumber = estimateNumber.replace(/,\s*$/, "");
						$('td:eq(3)',row).html(estimateNumber);
					}
					if(data.workIdentificationNumber != null) {
						var workIdentificationNumber = data.workIdentificationNumber;
						workIdentificationNumber = workIdentificationNumber.replace(/,\s*$/, "");
						$('td:eq(4)',row).html(workIdentificationNumber);
					}
					$('td:eq(5)',row).html(parseFloat(Math.round(data.amount * 100) / 100).toFixed(2));
					return row;
				},
				aaSorting : [],
				columns : [ {
					"data" : "",
					"sClass" : "text-center","width": "2%"
				}, {
					"data" : "department",
					"sClass" : "text-center","autoWidth": "false"
				}, {
					"data" : "lineEstimateNumber",
					"sClass" : "text-center" ,"width": "6%"
				}, {
					"data" : "estimateNumber",
					"sClass" : "text-right","autoWidth": "false"
				}, {
					"data" : "workIdentificationNumber",
					"sClass" : "text-center","autoWidth": "false"
				}, {
					"data" : "amount",
					"sClass" : "text-center","autoWidth": "false"
				}, {
					"data" : "createdBy",
					"sClass" : "text-center","autoWidth": "false"
				}, ]
			});
}

function openLineEstimate(lineEstimateId) {
	window.open("/egworks/lineestimate/view/" + lineEstimateId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}
	
	$('#btncancel').click(function() {
		var lineEstimateId = $('input[name=selectCheckbox]:checked').val();
		if(lineEstimateId == null) {
			var message = $('#selectLineEstimate').val();
			bootbox.alert(message);
		} else {
			if($('#cancellationReason').val() == 'Others')
				$('#cancellationRemarks').attr('required', 'required');
			else
				$('#cancellationRemarks').removeAttr('required');
			
			if($("#cancelForm").valid()) {
				$('#cancelForm #id').val(lineEstimateId);
					$.ajax({
						url: "/egworks/lineestimate/ajax-checkifloascreated?lineEstimateId="+lineEstimateId,     
						type: "GET",
						dataType: "json",
						success: function (message) {
							if(message == "") {
								bootbox.confirm($('#confirm').val(), function(result) {
									if(!result) {
										bootbox.hideAll();
										return false;
									} else {
										$("#cancelForm").attr('action','/egworks/lineestimate/cancel');
										$("#cancelForm").submit();
									}
								});
							} else {
								message = message.replace(/,\s*$/, ".");
								bootbox.alert(message);
							}
						}, 
						error: function (response) {
							console.log("failed");
						}
					});
				}
			}
		return false;
	});

$('#departments').change(function(){
	 if ($('#departments').val() === '') {
		   $('#createdBy').empty();
		   $('#createdBy').append($('<option>').text('Select from below').attr('value', ''));
			return;
			} else {
			$.ajax({
				type: "GET",
				url: "/egworks/lineestimate/ajaxsearchcreatedby",
				cache: true,
				dataType: "json",
				data:{'department' : $('#departments').val()}
			}).done(function(value) {
				console.log(value);
				$('#createdBy').empty();
				$('#createdBy').append($("<option value=''>Select from below</option>"));
				var responseObj = JSON.parse(value);
				$.each(responseObj, function(index, value) {
					$('#createdBy').append($('<option>').text(value.name).attr('value', value.id));
				});
			});
		}
	});
