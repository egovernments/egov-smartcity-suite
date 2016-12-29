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
var $bankBranchId = 0;

$(document).ready(function(){
	$bankBranchId = $('#bankbranchname').val();
});

jQuery('#btnsearch').click(function(e) {

	callAjaxSearch();
});

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

$('#bank').change(function () {
	$bankBranchId = "";
	$('#bankbranchname').empty();
	$('#bankbranchname').append($('<option>').text('Select from below').attr('value', ''));
	if($('#bank').val()!="")
		loadBankBranches($('#bank').val());
	else
		loadBankBranches(0);
});

function loadBankBranches(bankId){
		$.ajax({
			method : "GET",
			url : "/EGF/common/getbankbranchesbybankid",
			data : {
				bankId : bankId
			},
			async : true
		}).done(
				function(response) {
					$('#bankbranchname').empty();
					$('#bankbranchname').append($("<option value=''>Select from below</option>"));
					$.each(response, function(index, value) {
						var selected="";
						if($bankBranchId && $bankBranchId==value.id)
						{
								selected="selected";
						}
						$('#bankbranchname').append($('<option '+ selected +'>').text(value.branchname).attr('itemValue', value.id));
					});
				});
}
function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/EGF/bankbranch/ajaxsearch/" + $('#mode').val(),
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},
				"fnRowCallback" : function(row, data, index) {
					$(row).on(
							'click',
							function() {
								window.open('/EGF/bankbranch/' + $('#mode').val()+ '/' + data.id, '','width=800, height=600');
							});
				},
				"bDestroy" : true,
				dom: "<'row'<'col-xs-12 pull-right'f>r>t<'row buttons-margin'<'col-md-3 col-xs-6'i><'col-md-3  col-xs-6'l><'col-md-3 col-xs-6'B><'col-md-3 col-xs-6 text-right'p>>",
				buttons: [
						  {
						    extend: 'print',
						    title: 'Bank Branches',
						    filename: 'Bank Branches'
						},{
						    extend: 'pdf',
						    title: 'Bank Branches',
						    filename: 'Bank Branches'
						},{
						    extend: 'excel',
						    filename: 'Bank Branches'
						}
						],
				aaSorting : [],
				columns : [ {
					"data" : "bank",
					"sClass" : "text-left"
				},{
					"data" : "branchname",
					"sClass" : "text-left"
				}, {
					"data" : "branchcode",
					"sClass" : "text-left"
				}, {
					"data" : "branchMICR",
					"sClass" : "text-left"
				}, {
					"data" : "branchaddress1",
					"sClass" : "text-left"
				}, {
					"data" : "contactperson",
					"sClass" : "text-left"
				}, {
					"data" : "branchphone",
					"sClass" : "text-left"
				},{
					"data" : "narration",
					"sClass" : "text-left"
				}, {
					"data" : "isactive",
					"sClass" : "text-left"
				} ]
			});
}