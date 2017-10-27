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
$bankBranchId=0;
$(document).ready(function(){
	$("#buttonid").click(function() {
		document.forms[0].submit();
	});
	$('#bankId').trigger('blur');
	$bankBranchId=$('#bankBranchId').val();
	callbankdetails();
	$('#paymentMode').change(function(){
	callbankdetails();
	
	});
});
jQuery('#btnsearch').click(function(e) {
		callAjaxSearch();
	});
	
	function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}
 
function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");		
	jQuery('.report-section').removeClass('display-hide');
		reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/lcms/advocatemaster/ajaxsearch/"+$('#mode').val(),      
					type: "POST",
					"data":  getFormData(jQuery('form'))
				},
				"bDestroy" : true,
				'bAutoWidth': false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ "xls", "pdf", "print" ]
				},
				aaSorting: [],				
				columns : [ { 
"data" : "name", "sClass" : "text-center"} ,{ 
"data" : "mobileNumber", "sClass" : "text-center"} ,{ 
"data" : "email", "sClass" : "text-center"},{ 
	"data" : "id","visible": false, "searchable": false }]				
			});
			}
$("#resultTable").on('click','tbody tr',function(event) {
	window.open('/lcms/advocatemaster/'+ $('#mode').val() +'/'+drillDowntableContainer.fnGetData(this,3),'','width=800, height=600');
});


function callbankdetails(){
	if ($('#paymentMode :selected').text().localeCompare("RTGS") == 0 ) {  
		$("#b1details").show();
    	$("#b2details").show();
    	$("#b3details").show();
	}
	else if($('#paymentMode :selected').text().localeCompare("RTGS") != -1)  {
		$("#b1details").hide();
    	$("#b2details").hide();
    	$("#b3details").hide();
	}
	else if ($('#paymentMode :selected').text().localeCompare("Cash") != -1) {  
		$("#b1details").hide();
    	$("#b2details").hide();
    	$("#b3details").hide();
	}
	else if($('#paymentMode :selected').text().localeCompare("Cheque") != -1)  {
		$("#b1details").hide();
    	$("#b2details").hide();
    	$("#b3details").hide();
	}
}


$('#bankId').blur(function(){
	 if ($('#bankId').val() === '') {
		   $('#bankBranch').empty();
		   $('#bankBranch').append($('<option>').text('Select from below').attr('value', ''));
			return;
			} else {
				$.ajax({
					url: "/lcms/ajax-getAllBankBranchsByBank",    
					
				type: "GET",
				data: {
					'bankId' : $('#bankId').val()
				},
				dataType: "json",
				success: function (response) {
				    console.log("success"+response);
					$('#bankBranch').empty();
					$('#bankBranch').append($("<option value=''>Select from below</option>"));
					$.each(response, function(index, value) {
						var selected="";
						if($bankBranchId)
						{
							if($bankBranchId==value.id)
							{
								selected="selected";
							}
						}
//					$('#bankBranch').append($('<option>').text(value.branchname).attr('value', value.id))
					$('#bankBranch').append($('<option '+ selected +'>').text(value.branchname).attr('value', value.id));
					});
				}, 
				error: function (response) {
					console.log("failed");
				}
			});
			}
	});
/*$('#monthlyRenumeration').keyup(function(e) { // validate two decimal points
	var regex = /^\d+(\.\d{0,2})?$/g;
	if (!regex.test(this.value)) {
		$(this).val($(this).getNum());
	}
});
jQuery.fn.getNum = function() {
	var val = $.trim($(this).val());
	if (val.indexOf(',') > -1) {
		val = val.replace(',', '.');
	}
	var num = parseFloat(val);
	var num = num.toFixed(2);
	if (isNaN(num)) {
		num = '';
	}
	return num;
}*/

$("#monthlyRenumeration").on("keyup", function(){  // validate 7 digits and two decimal points
    var valid = /^\d{0,7}(\.\d{0,2})?$/.test(this.value),
        val = this.value;
    
    if(!valid){
        console.log("Invalid input!");
        this.value = val.substring(0, val.length - 1);
    }
});



function checkPanNumber() {
	var text =document.getElementById('panNumber').value;
	var panNumber = document.getElementById('panNumber').value.length;
    if( text != text.toUpperCase() || panNumber<10 && panNumber!='' )
        {
    	bootbox.alert("Enter valid Pan Number.")
        window.scroll(0,0);
        return false;
        }
    return true;
} 
$('#panNumber').keyup(function() {
    this.value = this.value.toUpperCase();
});
