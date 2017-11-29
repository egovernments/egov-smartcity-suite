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

function  validateStatus(obj)
{
	console.log($(obj).find(":selected").text());
if($(obj).find(":selected").text()=='Capitalized')
	$('#grossValue').attr("required","required");
else
	$('#grossValue').removeAttr("required");
}

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		if(indexed_array[n['name']])
		{
			var arry=[];
			if(Array.isArray(indexed_array[n['name']]))
			{
				arry=indexed_array[n['name']];
				console.log(arry);
				arry.push(n['value']);
			}
			else
			{
				arry.push(indexed_array[n['name']]);
				arry.push(n['value']);
				indexed_array[n['name']];
			}
			indexed_array[n['name']]=arry;
		}
		else{
		   indexed_array[n['name']] = n['value'];
		}
	});

	return indexed_array;
}

function loadCustomFields() {
	// var fd=jQuery('#mrform').serialize();
	var mode = $('#screenmode').val();
//	console.log('Has Children ->'+($('#customFieldsDiv').children()));
	console.log($('#customFieldsDiv').children().length);
	if(mode === 'edit')
	{	
		if($('#customFieldsDiv').children().length !== 0)
		{
			bootbox.confirm(customFieldsTitle +' data will be lost', function(result) {
				if(result === true)
				{
					assetCategoryValue = $('#assetCategory').val();
//					console.log($('#assetCategory option:selected').html());
					callAjaxCustomFields();
					customFieldsTitle = $('#assetCategory option:selected').html();
				}
				else if(result === false)
				{
					// assetCategoryValue is a global variable to store the asset category value saved;
					$('#assetCategory').val(assetCategoryValue);
				}	
			}); 
		}
		else
		{
			assetCategoryValue = $('#assetCategory').val();
			customFieldsTitle = $('#assetCategory option:selected').html();
			callAjaxCustomFields();
		}
	}

	if(mode === 'new')
	{
		callAjaxCustomFields();
	}
}

function callAjaxCustomFields(){
	$('#customFieldsDiv').empty();
	$.ajax({
		url : "/egassets/assetcategory/properties/"
			+ $('#assetCategory').val(),
			type : "GET",
			// dataType: "text",
			success : function(response) {
//				console.log("success"+response );
				// undoLoadingMask();
				jQuery('#customFieldsDiv').html(response);
				// bootbox.alert("Passed to Reconcile Details");
				patternvalidation();
				makeAutoCompleteFields();

			},
			error : function(response) {
				console.log("failed");
			}
	});
}

function callAjaxSearch() {
	
	
	console.log(JSON.stringify(getFormData(jQuery('form'))));
	
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
	.dataTable({
		ajax : {
			traditional: true,
			url : "/egassets/asset/ajaxsearch/" + $('#mode').val(),
			type : "POST",
			data :getFormData(jQuery('form'))
		},
		"fnRowCallback" : function(row, data, index) {
			$(row).on(
					'click',
					function() {
						console.log(data);
						var rowId = $('#rowId').val();
						console.log(rowId);
						if(rowId !== "")
						{
							var resultData = new Array();
							var row_id = window.opener.$('rowid').value;
							console.log(row_id);
							var parentWindow=window.dialogArguments;
							console.log(parentWindow);
							if(parentWindow == undefined){
								parentWindow = window.opener;
								if(window.opener != null && !window.opener.closed)
								{
									resultData = row_id + '`~`' + data.id + '`~`' + data.code + '`~`' + data.name + '`~`' + data.assetCategory;
									console.log(resultData);
									window.opener.update(resultData);
								}
							}
						}
						else
						{
							window.open('/egassets/asset/'
									+ $('#mode').val() + '/' + data.id, '',
							'width=850, height=600, scrollbars=yes');
						}
					});
		},
		"bDestroy" : true,
		"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
		"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
		"oTableTools" : {
			"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
			"aButtons" : [ "xls", "pdf", "print" ]
		},
		aaSorting : [],
		columns : [ {
			"data" : "code",
			"sClass" : "text-left"
		}, {
			"data" : "name",
			"sClass" : "text-left"
		}, {
			"data" : "assetCategory",
			"sClass" : "text-left"
		}, {
			"data" : "department",
			"sClass" : "text-left"
		}, {
			"data" : "status",
			"sClass" : "text-left"
		}, {
			"data" : "assetDetails",
			"sClass" : "text-left"
		} ]
	});
}

var masterdata = new Bloodhound({
	datumTokenizer : function(datum) {
		return Bloodhound.tokenizers.whitespace(datum.value);
	},
	queryTokenizer : Bloodhound.tokenizers.whitespace,
	remote : {
		url : '/egassets/assetcategory/masterdata',
		replace : function(url, uriEncodedQuery) {
			return url + '?id=' + $("input[id='typeaheadSourceType']").val()
			+ '&value=' + uriEncodedQuery;
		},
		filter : function(data) {
			data = JSON.parse(data);
			console.log(data);
			return $.map(data, function(ct) {
				return {
					id : ct.id,
					name : ct.name
				};
			});
		}
	}
});

masterdata.initialize();

function initializeTypeAheadTextBox($textbox) {
	var masterdata_typeahead = $textbox.typeahead({
		hint : false,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'name',
		source : masterdata.ttAdapter()
	});

	typeaheadWithEventsHandlingDynamic(masterdata_typeahead, $('input[name="'
			+ $textbox.data('hidden-elem') + '"]'), $('input[name="'
					+ $textbox.data('hidden-elem2') + '"]'));
}

$(document).on('focusin', '.autocomplete', function(e) {

	var dataSourceType = $(this).data('source-type');
	if (dataSourceType) {
		$('input[id="typeaheadSourceType"]').val(dataSourceType);
	}

});

function makeAutoCompleteFields() {

	$('.autocomplete').each(function(idx) {
		initializeTypeAheadTextBox($(this));
	});

	$(".datepicker").datepicker({
		format: "dd/mm/yyyy",
		autoclose: true 
	}); 
	
	/*$('.datetimepicker').datetimepicker({
		useStrict:true,
		format: "DD/MM/YYYY hh:mm a"
	});
	*/
	try { 
		    $(".datepicker").inputmask(); 
		
	}catch(e){}

}

function typeaheadWithEventsHandlingDynamic(typeaheadobj, hiddeneleid,
		hiddenelename) {
	typeaheadobj.on('typeahead:selected', function(event, data) {
		// setting hidden value
		$(hiddeneleid).attr('value', data.id);
		$(hiddenelename).attr('value', data.name);

	}).on('keydown', this, function(event) {
		var e = event;

		var position = $(this).getCursorPosition();
		var deleted = '';
		var val = $(this).val();
		if (e.which == 8) {
			if (position[0] == position[1]) {
				if (position[0] == 0)
					deleted = '';
				else
					deleted = val.substr(position[0] - 1, 1);
			} else {
				deleted = val.substring(position[0], position[1]);
			}
		} else if (e.which == 46) {
			var val = $(this).val();
			if (position[0] == position[1]) {

				if (position[0] === val.length)
					deleted = '';
				else
					deleted = val.substr(position[0], 1);
			} else {
				deleted = val.substring(position[0], position[1]);
			}
		}

		if (deleted) {
			$(hiddeneleid).attr('value', '');
			$(hiddenelename).attr('value', '');
		}

	}).on(
			'keypress',
			this,
			function(event) {
				// getting charcode by independent browser
				var evt = (evt) ? evt : event;
				var charCode = (evt.which) ? evt.which
						: ((evt.charCode) ? evt.charCode
								: ((evt.keyCode) ? evt.keyCode : 0));
				// only characters keys condition
				if ((charCode >= 32 && charCode <= 127)) {
					// clearing input hidden value on keyup
					$(hiddeneleid).attr('value', '');
					$(hiddenelename).attr('value', '');
				}
			}).on('focusout', this, function(event) {
				//focus out clear textbox, when no values selected from suggestion list
				if (!$(hiddeneleid).val()) {
					$(this).typeahead('val', '');
				}
			});
}


$(document).ready(function() {

	if($('#locationId').val() !== "" && $('#locationId').val() !== undefined){
		$('#revenuewardId').html("<option value=''>Select</option>");
		$('#blockId').html("<option value=''>Select</option>");
		$('#streetId').html("<option value=''>Select</option>");
		populateBoundaries();
	}

	$('#locationId').change(function() {
		$('#selectedblock').val("");
		$('#selectedward').val("");
		populateBoundaries();
	});

	$('#blockId').change(function() {
		$('#revenuewardId').html("<option value=''>Select</option>");
		var index = $("#blockId").prop('selectedIndex') - 1;
		var wards = blocks[index].wards;
		$.each(wards,function(j,ward){
			$('#revenuewardId').append("<option value='"+ward.wardId+"'"+(j === 0 ?"selected=selected":'')+">"+ward.wardName+"</option>");
		});
	});

	makeAutoCompleteFields();

	assetCategoryValue = $('#assetCategory').val();
	customFieldsTitle = $('#customFieldsTitle').text();

});

//global variable required for populated location related boundaries
var blocks=[];
var assetCategoryValue = '';
var customFieldsTitle='';

function populateBoundaries(){
	var locationId = $('#locationId').val();
	var selectedBlockId = $('#selectedblock').val();
	var selectedWardId = ($('#selectedward').val()=== "") ?"" : parseInt($('#selectedward').val());

	if(locationId === '')
	{
		$('#revenuewardId').html("<option value=''>Select</option>");
		$('#blockId').html("<option value=''>Select</option>");
		$('#streetId').html("<option value=''>Select</option>");
	}
	else
	{
		$.ajax({
			url: '/egassets/asset/getBoundariesByLocation/'+ locationId,
			type: "GET",
			dataType: "json", 
			success: function(response) {
//				console.log("SUCCESS");
				$('#revenuewardId').html("<option value=''>Select</option>");
				$('#blockId').html("<option value=''>Select</option>");
				$('#streetId').html("<option value=''>Select</option>");
				response=JSON.parse(response);
//				console.log(response);
				blocks=response.data.results.blocks;
				//To populate all the blocks related to the selected locaion
				$.each(response.data.results.blocks,function(j,block){
					$('#blockId').append("<option value='"+block.blockId+"'"+(j === 0 ?"selected=selected":'')+">"+block.blockName+"</option>");

					//To populate only the first blocks relevant wards
					if(j=== 0){
//						console.log("inside wards");
						$.each(block.wards,function(i,ward){
							$('#revenuewardId').append("<option value='"+ward.wardId+"'"+(i == 0 ?"selected=selected":'')+">"+ward.wardName+"</option>");
						});
					}
				});
				//Populate related streets
				$.each(response.data.results.streets, function (j, street) {
					$('#streetId').append("<option value='"+street.streetId+"'"+(j === 0 ?"selected=selected":'')+">"+street.streetName+"</option>");
				});

				//In edit screen select the block which is already saved
				if(selectedBlockId !== ""){
					$('#blockId').val(selectedBlockId);
					$('#revenuewardId').html("<option value=''>Select</option>");
					var index = $("#blockId").prop('selectedIndex') - 1;
//					console.log(index);
					if(index >= 0){
						var wards = blocks[index].wards;
//						console.log(wards);
						$.each(wards,function(j,ward){
//							console.log(ward.wardId);
//							console.log(selectedWardId);
//							console.log(ward.wardId === selectedWardId);
							$('#revenuewardId').append("<option value='"+ward.wardId+"'"+((ward.wardId === selectedWardId) ?"selected=selected":'')+">"+ward.wardName+"</option>");
						});
					}
				}
				//In edit screen select the ward which is already saved
				if(selectedWardId !== "")
				{
					$('#revenuewardId').val(selectedWardId);
				}

			},
			error: function(response){
				console.log("Failed");
			}
		});
	}	
}