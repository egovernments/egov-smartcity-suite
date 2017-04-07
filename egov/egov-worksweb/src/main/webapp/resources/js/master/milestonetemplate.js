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

function calculateTotalMilestoneTemplateActivityPercentage(){
	var totalPercentage=0;
	$( "input[name$='percentage']" ).each(function(){
		totalPercentage = totalPercentage + parseFloat(($(this).val()?$(this).val():"0"));
		if(parseFloat($(this).val()) == 0)
			$(this).val("");
	});
	$('#totalPercentageValue').html(totalPercentage);
}

function addMilestoneTemplateActivity() {
	var rowcount = $("#tblmilestonetemplate tbody tr").length;
	if (rowcount < 30) {
		if ($('#milestoneTemplateRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = 0;
			nextIdx = $("#tblmilestonetemplate tbody tr").length;
			
			// validate status variable for exiting function
			var isValid = 1;// for default have success value 0

			// validate existing rows in table
			$("#tblmilestonetemplate tbody tr").find('input, select, textarea').each(
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
			$("#milestoneTemplateRow").clone().find("input, errors, textarea").each(
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

					}).end().appendTo("#tblmilestonetemplate tbody");
			
			generateSno();
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

$('#submitBtn').click(function() {
	if ($('#milestoneTemplateForm').valid()) {
		var isSuccess=true;
		
		if(parseFloat($("#totalPercentageValue").html()) != 100){
			isSuccess=false;
			bootbox.alert($("#milestoneTemplateActivityTotalValueError").val());
			return isSuccess;
		}
		
		var elements = $(".stageordernumber")
		for (var i=0;i<elements.length;i++){
			if(!$.isNumeric($(elements[i]).val()) || $(elements[i]).val() == "0.0"){
				isSuccess=false;
				bootbox.alert($("#stageOrderNumberInvalidError").val());
				break;
			}
		}
		
		if(isSuccess)
			return true;
	}
	return false;
});

function deleteMilestoneTemplateActivity(obj) {
    var rIndex = getRow(obj).rowIndex;
    
	var tbl=document.getElementById('tblmilestonetemplate');	
	var rowcount=$("#tblmilestonetemplate tbody tr").length;

    if(rowcount<=1) {
		bootbox.alert($("#rowDeleteErrorMessage").val());
		return false;
	} else {
		tbl.deleteRow(rIndex);
		//starting index for table fields
		var idx= 0;
		var sno = 1;
		//regenerate index existing inputs in table row
		jQuery("#tblmilestonetemplate tbody tr").each(function() {
		
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
		generateSno();
		calculateTotalMilestoneTemplateActivityPercentage();
		return true;
	}	
}

function getRow(obj) {
	if(!obj)
		return null;
	tag = obj.nodeName.toUpperCase();
	while(tag != 'BODY'){
		if (tag == 'TR') 
			return obj;
		obj=obj.parentNode ;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}

function validatePercentage() {
	$( ".percentage" ).on("keyup", function(){
	    var valid = /^[0-9](\d{0,9})(\.\d{0,2})?$/.test(this.value),
	        val = this.value;
	    
	    if(!valid){
	        console.log("Invalid input!");
	        this.value = val.substring(0, val.length - 1);
	    }
	});
}

function createNewMilestoneTemplate() {
	window.location = "milestonetemplate-newform";
}
$subTypeOfWorkId = 0;

$(document).ready(function() {
	validatePercentage();
	jQuery("#typeOfWork option").each(function() {
		if (jQuery(this).val() == $("#typeOfWork").val()) {
			jQuery(this).attr("selected", "selected");
		}
	});
	$subTypeOfWorkId = $('#subTypeOfWorkValue').val();
	$('#typeOfWork').trigger('change');
});

$('#btnsearch').click(function(e) {
	$('#selectMilestoneTemplate').hide();
	if ($('#searchRequestMilestoneTemplateForm').valid()) {
		var isSuccess=true;
		
		if(isSuccess)
			callAjaxSearch();
	}
	return false;
});

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
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
					url : "/egworks/masters/milestonetemplate-searchdetails",
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
					$('td:eq(0)', row).html(index + 1);
					$('td:eq(7)', row).html(
							'<a href="javascript:void(0);" onclick="modifyMilestoneTemplate(\''
									+ data.milestoneTemplateId + '\')">Modify</a>');
					return row;
				},
				aaSorting : [],
				columns : [{
					"data" : "",
					"sClass" : "text-center","width": "2%"
				}, {
					"data" : "templateCode",
				},{
					"data" : "templateName",
				}, {
					"data" : "templateDescription",
				},{
					"data" : "typeOfWork",
				}, {
					"data" : "subTypeOfWork",
				} , {
					"data" : "templateStatus",
				},{
					"data" : "",
					"sClass" : "text-center"
				}]
			});
}

function openMilestoneTemplate(milestoneId) {
	window.open("/egworks/milestone/viewmilestonetemplate/" + milestoneId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

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

function modifyMilestoneTemplate(mileStoneTemplateId) {
	window.location = '/egworks/masters/milestonetemplate-edit/'+mileStoneTemplateId;
}