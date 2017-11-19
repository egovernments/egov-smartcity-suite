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

jQuery(document).ready(function(){
	
	$('#searchDonation').click(function(e){
		var fromDate = $('#fromDate').val();
		var toDate = $('#toDate').val();
		var fromAmount = $('#fromAmount').val();
		var toAmount = $('#toAmount').val();
		var pendingForPayment = $('#pendingForPayment').val();
		if(fromAmount!="" && toAmount!="" && parseInt(fromAmount)>parseInt(toAmount)){
			bootbox.alert("From Amount can not be greater than To Amount");
			return false;
		}
		
		if(compareDate(fromDate, toDate)===-1) {
			bootbox.alert("From Date can not be greater than To Date");
			return false;
		}
		
		oTable= $('#donationChargesDCBReport-table');
		$('.report-section').removeAttr('class','display-hide');
		
		console.log("result..........");
	    		$("#resultTable").dataTable({
				
				ajax : {
					url : "/wtms/reports/view-donation",     
					type: "POST",
					beforeSend : function() {
						$('.loader-class').modal('show', {
							backdrop : 'static'
						});
					},
					"data" : getFormData(jQuery('form')),
					complete : function() {
						$('.loader-class').modal('hide');
					}
				},
				"bDestroy" : true,
				'bAutoWidth': false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : []
				},
				aaSorting: [],
				"columns" : [
					{ "data": 'consumerCode', "className" : "text-center"},
					{ "data": 'propertyIdentifier', "className" : "text-center"},
					{ "data": 'ownerName', "className" : "text-center"},
					{ "data": 'mobileNumber', "className" : "text-center"},
					{ "data": 'propertyAddress', "className" : "text-center"},
					{ "data": 'totalDonationAmount', "className":"text-right"},
					{ "data": 'paidDonationAmount', "className":"text-right"},
					{ "data": 'balanceDonationAmount', "className":"text-right"},
								],
				    "footerCallback": function (row, data, start, end, display) {
						    var api = this.api(), data;
						     if (data.length == 0) {
						         $('#report-footer').hide();
						     } else {
						         $('#report-footer').show();
						     }
						     if (data.length > 0) {
						    	updateTotalFooter(5, api);
						        updateTotalFooter(6, api);
						        updateTotalFooter(7, api);
						        updateTotalFooter(8, api);
						     }
					},
					"aoColumnDefs": [{
					"aTargets": [5, 6, 7, 8],
				    "mRender": function (data, type, full) {
				       return formatNumberInr(data);
					}
					}]
				
	});
	
});
	$('#pendingForPaymentOnly').on('change', function() {
		$('#pendingForPaymentOnly').val($(this).is(':checked')? true : false);
	});

});
function updateTotalFooter(colidx, api) {
	// Remove the formatting to get integer data for summation
	var intVal = function(i) {
		return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
				: typeof i === 'number' ? i : 0;
	};

	// Total over all pages
	total = api.column(colidx).data().reduce(function(a, b) {
		return intVal(a) + intVal(b);
	});

	// Total over this page
	pageTotal = api.column(colidx, {
		page : 'current'
	}).data().reduce(function(a, b) {
		return intVal(a) + intVal(b);
	}, 0);

	// Update footer
	jQuery(api.column(colidx).footer()).html(
			formatNumberInr(pageTotal) + ' (' + formatNumberInr(total)
					+ ')');
}


//inr formatting number
function formatNumberInr(x) {
	if (x) {
		x = x.toString();
		var afterPoint = '';
		if (x.indexOf('.') > 0)
			afterPoint = x.substring(x.indexOf('.'), x.length);
		x = Math.floor(x);
		x = x.toString();
		var lastThree = x.substring(x.length - 3);
		var otherNumbers = x.substring(0, x.length - 3);
		if (otherNumbers != '')
			lastThree = ',' + lastThree;
		var res = otherNumbers.replace(/\B(?=(\d{2})+(?!\d))/g, ",")
				+ lastThree + afterPoint;
		return res;
	}
	return x;
}

function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}