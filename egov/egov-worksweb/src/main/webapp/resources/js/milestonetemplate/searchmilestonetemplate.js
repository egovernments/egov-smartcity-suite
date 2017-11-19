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
$typeOfWork=window.opener.$('#typeOfWork').val();
$subTypeOfWork=window.opener.$('#subTypeOfWork').val();

$(document).ready(function() {
	jQuery("#typeOfWork option").each(function() {
		if (jQuery(this).val() == $typeOfWork) {
			jQuery(this).attr("selected", "selected");
		}
	});
	//TODO Need To remove trigger 
	$('#typeOfWork').trigger('change');
});

jQuery('#btnsearch').click(function(e) {
	$('#selectMilestoneTemplate').hide();
	if($('#milestoneTemplate').valid())
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

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/egworks/milestone/ajaxsearchmilestonetemplate",
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
					if (data.code != null)
						$('td:eq(0)',row).html('<input type="radio" data='+ data.milestoneId +' name="selectCheckbox" value="'+ data.milestoneId +'"/>');
					$('td:eq(1)', row).html(index + 1);
					$('#selectMilestoneTemplate').show();
					if (data.code != null)
						$('td:eq(2)', row).html(
								'<a href="javascript:void(0);" onclick="openMilestoneTemplate(\''
										+ data.milestoneId + '\')">'
										+ data.code + '</a>');
					return row;
				},
				aaSorting : [],
				columns : [ {
					"data" : "",
					"sClass" : "text-center","width": "2%"
				}, {
					"data" : "",
					"sClass" : "text-center","width": "2%"
				}, {
					"data" : "code",
					"sClass" : "text-left","autoWidth": "false"
				}, {
					"data" : "typeOfWork",
					"sClass" : "text-left" ,"width": "6%",
				}, {
					"data" : "subTypeOfWork",
					"sClass" : "text-left","autoWidth": "false"
				}, {
					"data" : "description",
					"sClass" : "text-left","autoWidth": "false"
				} ]
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
				console.log(value);
				$('#subTypeOfWork').empty();
				$('#subTypeOfWork').append($("<option value=''>Select from below</option>"));
				$.each(value, function(index, val) {
					var selected="";
					if($subTypeOfWork)
					{
						if($subTypeOfWork==val.id)
						{
							selected="selected";
						}
					}
				     $('#subTypeOfWork').append($('<option '+ selected +'>').text(val.description).attr('value', val.id));
				});
			});
		}
	});

jQuery('#milestoneDetails').click(function(e) {
	var milestoneId = $('input[name=selectCheckbox]:checked').val();
	var rowcounthidden = window.opener.$("#tblmilestone tbody tr:hidden[id='milestoneRow']").length;
	if(rowcounthidden == 1)
		window.opener.$('#tblmilestone tbody tr').show();
	if(milestoneId == null)
		bootbox.alert("Please select Atleast One Milestone Template");
	else{
		$.ajax({
			url: "/egworks/milestone/setmilestonetemplateactivities/"+milestoneId,     
			type: "GET",
			dataType: "json",
			success: function (milestoneTemplateActivities) {
				$.each(milestoneTemplateActivities, function(index,milestoneTemplateActivity){
					if(index!=0)
						window.opener.addMilestoneDetails();
					var stageOrderNo = window.opener.document.getElementsByName('activities['+index+'].stageOrderNo');
					$(stageOrderNo).val(milestoneTemplateActivity.stageOrderNo);
					var description = window.opener.document.getElementsByName('activities['+index+'].description');
					$(description).val(milestoneTemplateActivity.description);
					var percentage = window.opener.document.getElementsByName('activities['+index+'].percentage');
					$(percentage).val(milestoneTemplateActivity.percentage);
					window.opener.calculatePercentageTotal();
				});
				window.close();
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
		
	}
});

