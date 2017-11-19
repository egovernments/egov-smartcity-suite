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
	var department = $('#department').val();
	var workOrderNumber = $('#workOrderNumber').val();
	var workIdentificationNumber = $('#workIdentificationNumber').val();
	var billNumber = $('#billNumber').val();
	if(department == '' && workOrderNumber == '' && workIdentificationNumber == '' && billNumber == '')
		bootbox.alert('Atleast one search criteria is mandatory');
	else
		callAjaxSearch();
});

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/egworks/contractorbill/cancel/ajax-search",
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
					$('td:eq(0)',row).html('<input type="radio" data="'+ data.voucherNumber +'" name="selectCheckbox" value="'+ data.id +'"/>');
					if (data.billNumber != null)
						$('td:eq(1)', row).html(
								'<a href="javascript:void(0);" onclick="openLineEstimate(\''
										+ data.id + '\')">'
										+ data.billNumber + '</a>');
					return row;
				},
				aaSorting : [],
				columns : [ {
					"data" : "",
					"sClass" : "text-center","width": "2%"
				}, {
					"data" : "",
					"sClass" : "text-center","autoWidth": "false"
				}, {
					"data" : "billDate",
					"sClass" : "text-center" ,"width": "6%",
					render: function (data, type, full) {
						if(full!=null &&  full.billDate != undefined) {
							var regDateSplit = full.billDate.split(" ")[0].split("-");		
							return regDateSplit[2] + "/" + regDateSplit[1] + "/" + regDateSplit[0];
						}
						else return "";
			    	}
				}, {
					"data" : "estimateNumber",
					"sClass" : "text-center","autoWidth": "false"
				}, {
					"data" : "workIdentificationNumber",
					"sClass" : "text-center","autoWidth": "false"
				}, {
					"data" : "workOrderNumber",
					"sClass" : "text-center","autoWidth": "false"
				}, {
					"data" : "contractorName",
					"sClass" : "text-center","autoWidth": "false",
					 "render":function(data, type, full, meta){
					       return full.contractorCode + " - " + full.contractorName;
					    } 
				} ]
			});
}

function openLineEstimate(contractorBillId) {
	window.open("/egworks/contractorbill/view/" + contractorBillId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

$(document).ready(function() {
	var workIdNumber = new Bloodhound({
	    datumTokenizer: function (datum) {
	        return Bloodhound.tokenizers.whitespace(datum.value);
	    },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
	    remote: {
	        url: '/egworks/contractorbill/ajaxworkidentificationnumbers-contractorbilltocancel?code=%QUERY',
	        filter: function (data) {
	            return $.map(data, function (ct) {
	                return {
	                    name: ct
	                };
	            });
	        }
	    }
	});

	workIdNumber.initialize();
	var workIdNumber_typeahead = $('#workIdentificationNumber').typeahead({
		hint : true,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'name',
		source : workIdNumber.ttAdapter()
	});
	
	var billNumber = new Bloodhound({
	    datumTokenizer: function (datum) {
	        return Bloodhound.tokenizers.whitespace(datum.value);
	    },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
	    remote: {
	        url: '/egworks/contractorbill/ajaxbillnumbers-contractorbilltocancel?billNumber=%QUERY',
	        filter: function (data) {
	            return $.map(data, function (ct) {
	                return {
	                    name: ct
	                };
	            });
	        }
	    }
	});

	billNumber.initialize();
	var billNumber_typeahead = $('#billNumber').typeahead({
		hint : true,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'name',
		source : billNumber.ttAdapter()
	});
	
	$('#btncancel').click(function() {
		var contractorBillId = $('input[name=selectCheckbox]:checked').val();
		if(contractorBillId == null) {
			bootbox.alert("Please select a Contractor Bill to Cancel");
		} else {
			if($('#cancellationReason').val() == 'Others')
				$('#cancellationRemarks').attr('required', 'required');
			else
				$('#cancellationRemarks').removeAttr('required');
			
			if($("#cancelForm").valid()) {
				$('#cancelForm #id').val(contractorBillId);
				var voucherNumber = $('input[name=selectCheckbox]:checked').attr('data');
				if(voucherNumber != "") {
					bootbox.alert($('#voucherCreatedMessage').val() + " " + voucherNumber);
				} else {
					bootbox.confirm($('#confirm').val(), function(result) {
						if(!result) {
							bootbox.hideAll();
							return false;
						} else {
							$("#cancelForm").attr('action','/egworks/contractorbill/cancel');
							$("#cancelForm").submit();
						}
					});
				}
			}
		}
		return false;
	});
});