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

var $subTypeOfWorkId = 0;
var hint='<a href="#" class="hintanchor" title="@fulldescription@"><i class="fa fa-question-circle" aria-hidden="true"></i></a>';
$(document).ready(function(){
	$subTypeOfWorkId = $('#subTypeOfWorkValue').val();
	$('#typeOfWork').trigger('change');

	if($("#nonSorRow td:eq(2) select").val() == "")
		$("#nonSorRow").hide();
	else
		$("#nonSorRow").show();
	
	if($("#sorRow td:eq(0) input").val() == "")
		$("#sorRow").hide();
	else
		$("#sorRow").show();
	
	if($($(".sorhiddenid")[0]).val() != ""){
		$("#scheduleCategory").removeAttr('required');
	}
	
	$('#addRowBtn').click(function() {
		if($("#nonSorRow").is(":visible")) 
			addNonSorRow();
		$("#nonSorRow").show();
	});
	
	var sorSearch = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: '/egworks/masters/ajax-sorbyschedulecategories?code=',
			replace: function (url, query) {
				var scheduleCategories = $('#scheduleCategory').val();
				if(scheduleCategories == null)
					bootbox.alert($('#msgschedulecategory').val());
				return url + query + '&scheduleCategories=' + scheduleCategories;
			},
			filter: function (data) {
				return $.map(data, function (ct) {
					return {
						id: ct.id,
						code: ct.code,
						description: ct.description,
						uom: ct.uom.uom,
						uomid: ct.uom.id,
						summary: ct.summary,
						categoryCode: ct.scheduleCategory.code,
						scheduleCategoryId:ct.scheduleCategory.id,
						displayResult: ct.code+' : '+ct.summary+' : '+ct.scheduleCategory.code 
					};
				});
			}
		}
	});
	sorSearch.initialize();
	$('#sorSearch').typeahead({
		hint : true,
		highlight : true,
		minLength : 2
	}, {
		displayKey : 'displayResult',
		source : sorSearch.ttAdapter()
	}).on('typeahead:selected', function (event, data) {
		var flag = false;
		$('.sorhiddenid').each(function() {
			if($(this).val() == data.id) {
				flag = true;
			}
		});
		if(flag) {
			bootbox.alert($('#erroradded').val(), function() {
				$('#sorSearch').val('');
			});
		}
		else {
			var key = $("#tblSor tbody tr").length;
			if($("#sorRow").is(":visible")) {
				addSorRow();
				$.each(data, function() {
					setSorFieldValues(data,key);
				});
			}else{
				$("#sorRow").show();
				$.each(data, function() {
					var firstRowIdx = 0;
					setSorFieldValues(data,firstRowIdx);
				});
			}
		}
		$('#sorSearch').typeahead('val','');
	});
	
	generateSorSno();
	
	$('#submitBtn').click(function() {
		if ($('#estimateTemplateForm').valid()) {
			var isSuccess = true;
			if($($(".sorhiddenid")[0]).val() == ''){
				bootbox.alert($("#msgschedulecategory").val());
				isSuccess = false;
			}
			
			if($("#nonSorRow").is(":not(':hidden')")){
				var unitRateElements = $(".unitrate")
				for (var i=0;i<unitRateElements.length;i++){
					if(parseFloat($(unitRateElements[i]).val()) == 0){
						bootbox.alert($("#nonSorValueError").val());
						isSuccess = false;
						break;
					}
				}
			}
			
			if(isSuccess)
				return true;
		}
		return false;
	});
	
	$('#btnsearch').click(function(e) {
		callAjaxSearch();
	});
	
	$('.textfieldsvalidate').on('input',function(){
		var regexp_textfields = /[^0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]/g;
		if($(this).val().match(regexp_textfields)){
			$(this).val( $(this).val().replace(regexp_textfields,'') );
		}
	});
	
});
$('#typeOfWork').change(function(){
	 if ($('#typeOfWork').val() === '') {
		   $('#subTypeOfWork').empty();
		   $('#subTypeOfWork').append($('<option>').text('Select from below').attr('value', ''));
			return;
			} else {
			$.ajax({
				type: "GET",
				url: "/egworks/lineestimate/getsubtypeofwork",
				cache: true,
				dataType: "json",
				data:{'id' : $('#typeOfWork').val()}
			}).done(function(value) {
				$('#subTypeOfWork').empty();
				$('#subTypeOfWork').append($("<option value=''>Select from below</option>"));
				$.each(value, function(index, val) {
					var selected="";
					if($subTypeOfWorkId)
					{
						if($subTypeOfWorkId==val.id)
						{
							selected="selected";
						}
					}
				     $('#subTypeOfWork').append($('<option '+ selected +'>').text(val.name).attr('value', val.id));
				});
			});
		}
	});

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/egworks/abstractestimate/ajaxestimatetemplates-search",
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
					$('td:eq(0)',row).html(index+1);
					$('td:eq(7)', row).html(
							'<a href="javascript:void(0);" onclick="modifyEstimateTemplate(\''
									+ data.estimateTemplateId + '\')">Modify</a>');
					return row;
				},
				aaSorting : [],
				columns : [{
						"data" : ""} ,{
						"data" : "code"},{
						"data" : "description"},{
						"data" : "name"},{
						"data" : "typeOfWork"},{
						"data" : "subTypeOfWork"},{
						"data" : "status"},{
						"data" : ""
						}]
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

function setSorFieldValues(data,keyIdx){
	$($(".sorCategoryCode")[keyIdx]).html(data.categoryCode);
	$($(".sorCode")[keyIdx]).html(data.code);
	$($(".sorSummary")[keyIdx]).html(data.summary);
	$($(".sorDescription")[keyIdx]).html(hint.replace("@fulldescription@",data.description));
	$($(".sorUom")[keyIdx]).html(data.uom);
	$($(".sorhiddenid")[keyIdx]).val(data.id);
	$($(".sorschedulecategoryhiddenid")[keyIdx]).val(data.scheduleCategoryId);
	$($(".sorschedulecategoryhiddencode")[keyIdx]).val(data.code);
	$($(".soruomhiddenid")[keyIdx]).val(data.uomid);
}

function addNonSorRow() {
	var rowcount = $("#tblNonSor tbody tr").length;
	if (rowcount < 30) {
		if ($('#nonSorRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = 0;
			nextIdx = $("#tblNonSor tbody tr").length;
			
			// validate status variable for exiting function
			var isValid = 1;// for default have success value 0
			
			// validate existing rows in table
			$("#tblNonSor tbody tr").find('input, select, textarea').each(
					function() {
						if (($(this).data('optional') === 0)
								&& (!$(this).val())) {
							$(this).focus();
							bootbox.alert($(this).data('errormsg'));
							isValid = 0;// set validation failure
							return false;
						}
			});

			if (isValid === 0) {
				return false;
			}

			// Generate all textboxes Id and name with new index
			$("#nonSorRow").clone().find("input, errors, select,textarea").each(
					function() {

						if ($(this).data('server')) {
							$(this).removeAttr('data-server');
						}
						
							$(this).attr(
									{
										'name' : function(_, name) {
											return name.replace(/\d+/, nextIdx);
										},
										'data-idx' : function(_,dataIdx)
										{
											return nextIdx;
										}
									});

							// if element is static attribute hold values for
							// next row, otherwise it will be reset
							if (!$(this).data('static')) {
								$(this).val('');
								// set default selection for dropdown
								if ($(this).is("select")) {
									$(this).prop('selectedIndex', 0);
								}
							}
							
							$(this).removeAttr('disabled');
							$(this).prop('checked', false);

					}).end().appendTo("#tblNonSor tbody");
			
			generateSno();
		}
	} else {
		  bootbox.alert('limit reached!');
	}
}

function getRow(obj) {
	if(!obj)return null;
	tag = obj.nodeName.toUpperCase();
	while(tag != 'BODY'){
		if (tag == 'TR') return obj;
		obj=obj.parentNode ;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}

function generateSorSno()
{
	var idx=1;
	$(".spansorno").each(function(){
		$(this).text(idx);
		idx++;
	});
}

function deleteSor(obj) {
	var rIndex = getRow(obj).rowIndex;
	var tbl = document.getElementById("tblSor");
	var rowcount=$("#tblSor tbody tr").length;
	if(rowcount == 1){
		$($(".sorhiddenid")[rowcount-1]).val('');
		$($(".sorschedulecategoryhiddenid")[rowcount-1]).val('');
		$($(".sorschedulecategoryhiddencode")[rowcount-1]).val('');
		$($(".soruomhiddenid")[rowcount-1]).val('');
		$("#sorRow").hide();
	}else{
		tbl.deleteRow(rIndex);
		var idx= 0;
		var sno = 1;
		//regenerate index existing inputs in table row
		jQuery("#tblSor tbody tr").each(function() {
		
				jQuery(this).find("input, select, textarea, errors, span, input:hidden").each(function() {
					var classval = jQuery(this).attr('class');
					if(classval == 'spansno') {
						jQuery(this).text(sno);
						sno++;
					} else {
					jQuery(this).attr({
					      'name': function(_, name) {
					    	  if(!(jQuery(this).attr('name')===undefined))
					    		  return name.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'id': function(_, id) {
					    	  if(!(jQuery(this).attr('id')===undefined))
					    		  return id.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'class' : function(_, name) {
								if(!(jQuery(this).attr('class')===undefined))
									return name.replace(/\[.\]/g, '['+ idx +']'); 
							},
						  'data-idx' : function(_,dataIdx)
						  {
							  return idx;
						  }
					   });
					}
			    });
				
				idx++;
		});
	}
	//starting index for table fields
	generateSorSno();
	return true;
}

function generateSlno()
{
	var idx=1;
	$(".spansorno").each(function(){
		$(this).text(idx);
		idx++;
	});
}

function deleteNonSor(obj) {
    var rIndex = getRow(obj).rowIndex;
    var tbl = document.getElementById("tblNonSor");
    var rowcount=$("#tblNonSor tbody tr").length;
	if(rowcount == 1){
		$("#nonSorRow input").eq(0).val('');
		$("#nonSorRow input").eq(1).val("0.0");
		$("#nonSorRow select").val('');
		$("#nonSorRow textarea").val('');
		$("#nonSorRow").hide();
	}else{
		tbl.deleteRow(rIndex);
		var idx= 0;
		var sno = 1;
		//regenerate index existing inputs in table row
		jQuery("#tblNonSor tbody tr").each(function() {
		
				jQuery(this).find("input, select, textarea, errors, span, input:hidden").each(function() {
					var classval = jQuery(this).attr('class');
					if(classval == 'spansno') {
						jQuery(this).text(sno);
						sno++;
					} else {
					jQuery(this).attr({
					      'name': function(_, name) {
					    	  if(!(jQuery(this).attr('name')===undefined))
					    		  return name.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'id': function(_, id) {
					    	  if(!(jQuery(this).attr('id')===undefined))
					    		  return id.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'class' : function(_, name) {
								if(!(jQuery(this).attr('class')===undefined))
									return name.replace(/\[.\]/g, '['+ idx +']'); 
							},
						  'data-idx' : function(_,dataIdx)
						  {
							  return idx;
						  }
					   });
					}
			    });
				
				idx++;
		});
	}
	
	generateSno();
	return true;
}

function addSorRow(){
	var rowcount = $("#tblSor tbody tr").length;
	if (rowcount < 30) {
		if ($('#sorRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = 0;
			nextIdx = $("#tblSor tbody tr").length;
			
			// validate status variable for exiting function
			var isValid = 1;// for default have success value 0

			if (isValid === 0) {
				return false;
			}
			
			// Generate all textboxes Id and name with new index
			$("#sorRow").clone().find("input, errors, textarea").each(
					function() {

						if ($(this).data('server')) {
							$(this).removeAttr('data-server');
						}
						
							$(this).attr(
									{
										'name' : function(_, name) {
											return name.replace(/\d+/, nextIdx);
										},
										'data-idx' : function(_,dataIdx)
										{
											return nextIdx;
										}
									});

							// if element is static attribute hold values for
							// next row, otherwise it will be reset
							if (!$(this).data('static')) {
								$(this).val('');
								// set default selection for dropdown
								if ($(this).is("select")) {
									$(this).prop('selectedIndex', 0);
								}
							}
							
							$(this).removeAttr('disabled');
							$(this).prop('checked', false);

					}).end().appendTo("#tblSor tbody");
			
			generateSlno();
		}
	} else {
		  bootbox.alert('limit reached!');
	}
}

function generateSno()
{
	var idx=1;
	$(".spansno").each(function(){
		$(this).text(idx);
		idx++;
	});
}

function createNewEstimateTemplate(){
	window.location = "estimatetemplate-newform";
}

function modifyEstimateTemplate(estimateTemplateId) {
	window.location = '/egworks/masters/estimatetemplate-edit/'+estimateTemplateId;
}