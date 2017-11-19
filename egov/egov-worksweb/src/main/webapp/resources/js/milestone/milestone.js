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

$detailsRowCount = $('#detailsSize').val();
var templateCode = new Bloodhound({
    datumTokenizer: function (datum) {
        return Bloodhound.tokenizers.whitespace(datum.value);
    },
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
        url: '/egworks/milestone/ajaxmilestonetemplatecode-milestone?code=%QUERY', 
        filter: function (data) {
            return $.map(data, function (ct) {
                return {
                    code: ct.code,
                    id:ct.id
                };
            });
        }
    }
});

templateCode.initialize();
var templateCode_typeahead = $('#templateCode').typeahead({
	hint : true,
	highlight : true,
	minLength : 3
}, {
	displayKey : 'code',
	source : templateCode.ttAdapter()
}).on('typeahead:selected typeahead:autocompleted', function(event, data){            
	$("#milestoneTemplateId").val(data.id);   
});

function initializeDatePicker()
{
	$('.datepicker').datepicker().off('changeDate');
	$(".scheduleEndDate").datepicker({
		format: "dd/mm/yyyy",
		autoclose:true
	}).on('changeDate', function (ev) {
		$(this).datepicker('hide');
	});
	$('.scheduleStartDate').datepicker({
		format: "dd/mm/yyyy",
		autoclose:true
	}).on('changeDate', function (ev) {
		$(this).datepicker('hide');
	});
	$('.datepicker').datepicker('update');
	try { $(".datepicker").inputmask(); }catch(e){}	
}

function validateScheduleStartDate()
{
	var isValidationSuccess=true;
	var startDateCollection=[];
	$('.scheduleStartDate').each(function(i){
		var textbox=$(this);
		var currentDate=0;
		if($(this).val())
		{
			currentDate=$(this).data('datepicker').date;	
		}
		if(i===0)
		{
			if(currentDate<workOrderDate)
			{
				isValidationSuccess=false;
				var message = document.getElementById('errorScheduleLOADate').value;
				bootbox.alert(message, function(){ 
					setTimeout(function(){ textbox.focus(); }, 400);
				});
				
				return false;	
			}
			startDateCollection.push(currentDate);
		}
		else {
			var isExit=false;
			$.each(startDateCollection, function(index, dateObj){
				if(currentDate instanceof Date){
					if(dateObj.getTime() > currentDate.getTime())
					{
						isValidationSuccess=false;
						var message = document.getElementById('errorScheduleDates').value;
						bootbox.alert(message, function(){ 
							setTimeout(function(){ textbox.focus(); }, 400);
						});
						isExit=true;
						return false;
					}
					startDateCollection.push(currentDate);
				}
			});
			if(isExit)
			{
				return false;
			}
		}
	});
	return isValidationSuccess;
}

function validateScheduleEndDate()
{
	var isSuccess=true;
	$('.scheduleEndDate').each(function(i){
		var scheduleStartDate=$('.scheduleStartDate[data-idx="'+ i +'"]').data('datepicker').date;
		var scheduleEndDate=$(this).data('datepicker').date;
		if(scheduleStartDate>scheduleEndDate)
		{
			isSuccess=false;
			var message = document.getElementById('errorScheduleEndDates').value;
			bootbox.alert(message);
		}
	});
	return isSuccess;
}

var workOrderDate=new Date($('#workOrderDate').val());
initializeDatePicker();

function validatePercentage() {
	$( "input[name$='percentage']" ).on("keyup", function(){
	    var valid = /^[1-9](\d{0,9})(\.\d{0,2})?$/.test(this.value),
	        val = this.value;
	    
	    if(!valid){
	        console.log("Invalid input!");
	        this.value = val.substring(0, val.length - 1);
	    }
	});
}
function daydiff(first, second) { 
	var diffDays=Math.round((second-first)/(1000*60*60*24));
	return (diffDays<0?(diffDays)+'d':'+'+diffDays+'d');
}

function addMilestone() {
	var rowcount = $("#tblmilestone tbody tr").length;
	var rowcounthidden = $("#tblmilestone tbody tr:hidden[id='milestoneRow']").length;
	if (rowcount < 30) {
		if(rowcounthidden == 1){
			$('#tblmilestone tbody tr').show();
			return true;
		}
			
		if (document.getElementById('milestoneRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = 0;
			if($detailsRowCount == 0)
				nextIdx = $("#tblmilestone tbody tr").length;
			else
				nextIdx = $detailsRowCount++;
			var isValid = 1;// for default have success value 0
			// validate existing rows in table
			$("#tblmilestone tbody tr").find('input, select, textarea').each(
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
			$("#milestoneRow").clone().find("input, errors, textarea").each(
					function() {

						if ($(this).data('server')) {
							$(this).removeAttr('data-server');
						}
						
							$(this).attr({
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
							

			}).end().appendTo("#tblmilestone tbody");
			initializeDatePicker();
		}
	} else {
		  bootbox.alert('limit reached!');
	}
}

function calculatePercentageTotal(){
	var percentage=0;
	$( "input[name$='percentage']" ).each(function(){
		percentage = percentage + parseFloat(($(this).val()?$(this).val():"0"));
		if(parseFloat($(this).val()) == 0)
			$(this).val("");
	});
	var totalPercentage=$('#totalPercentage').html(percentage);
}

function deleteMilestone(obj) {
    var rIndex = getRow(obj).rowIndex;
    
    var id = $(getRow(obj)).children('td:first').children('input:first').val();
    //To get all the deleted rows id
    var aIndex = rIndex - 1;

	var tbl=document.getElementById('tblmilestone');	
	var rowcount=$("#tblmilestone tbody tr").length;
	if(rowcount<=1) {
		$('#tblmilestone tbody tr').hide();
		$('.stageOrderNo').val('');
		$('.description').val('');
		$('.percentage').val('');
		$('.scheduleStartDate').val('');
		$('.scheduleEndDate').val('');
		calculatePercentageTotal();
		return true;
	}
		tbl.deleteRow(rIndex);
		//starting index for table fields
		var idx=parseInt($detailsRowCount);
		//regenerate index existing inputs in table row
		$("#tblmilestone tbody tr").each(function() {
			hiddenElem=$(this).find("input:hidden");
			if(!$(hiddenElem).val())
			{
				$(this).find("input, errors, textarea").each(function() {
					   $(this).attr({
					      'name': function(_, name) {
					    	  return name.replace(/\[.\]/g, '['+ idx +']'); 
					      },
						  'data-idx' : function(_,dataIdx)
						  {
							  return idx;
						  }
					   });
			    });
				idx++;
			}
		});
		
		calculatePercentageTotal();
		return true;
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

$('#searchMilestoneTemplate').click(function() {
	window.open("/egworks/milestone/searchmilestonetemplate",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
});

function replaceStatus() {
	var status = document.getElementById('status').innerHTML;
	if(status == 1)
		status = status.replace("1", "ACTIVE");
	status = status.replace("0", "INACTIVE");
    document.getElementById("status").innerHTML = status;
}

function addMilestoneDetails() {
	var rowcount = $("#tblmilestone tbody tr").length;
	if (rowcount < 30) {
		if (document.getElementById('milestoneRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = 0;
			if($detailsRowCount == 0)
				nextIdx = $("#tblmilestone tbody tr").length;
			else
				nextIdx = $detailsRowCount++;
			var isValid = 1;// for default have success value 0
			if (isValid === 0) {
				return false;
			}
			// Generate all textboxes Id and name with new index
			$("#milestoneRow").clone().find("input, errors, textarea").each(
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

					}).end().appendTo("#tblmilestone tbody");
			        initializeDatePicker();
		}
	} else {
		  bootbox.alert('limit reached!');
	}
}

jQuery('#submitMilestoneDetails').click(function(e) {
	var rowcounthidden = $("#tblmilestone tbody tr:hidden[id='milestoneRow']").length;
	if(rowcounthidden == 1)
		$('#tblmilestone tbody tr').show();
	var templateCode = $('#templateCode').val();
	var rowcount = $("#tblmilestone tbody tr").length;
	var milestoneId = $('#milestoneTemplateId').val();
	if(templateCode == ''){
		var message = document.getElementById('errorTemplateCode').value;
		bootbox.alert(message);
	}
	else{
		$.ajax({
			url: "/egworks/milestone/setmilestonetemplateactivities/"+milestoneId,
			type: "GET",
			dataType: "json",
			success: function (milestoneTemplateActivities) {
				$.each(milestoneTemplateActivities, function(index,milestoneTemplateActivity){
					if(index!=0)
						addMilestoneDetails();
					var stageOrderNo = document.getElementsByName('activities['+index+'].stageOrderNo');
					$(stageOrderNo).val(milestoneTemplateActivity.stageOrderNo);
					var description = document.getElementsByName('activities['+index+'].description');
					$(description).val(milestoneTemplateActivity.description);
					var percentage = document.getElementsByName('activities['+index+'].percentage');
					$(percentage).val(milestoneTemplateActivity.percentage);
					calculatePercentageTotal();
				});
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	}
});

function openLetterOfAcceptance() {
	var workOrderId = $('#workOrderId').val();
	window.open("/egworks/letterofacceptance/view/" + workOrderId, '','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

$('#save').click(function() {
	if($('#milestone').valid()){
	var rowcounthidden = $("#tblmilestone tbody tr:hidden[id='milestoneRow']").length;
	if (rowcounthidden == 1) {
		var message = document.getElementById('errorMilestoneDeatail').value;
		bootbox.alert(message);
		return false;
	}
	var totalPercentage = parseFloat($('#totalPercentage').html());
	if (totalPercentage != 100) {
		var message = document.getElementById('errorTotalPercentage').value;
		bootbox.alert(message);
		return false;
	}
	if (validateScheduleStartDate()) {
		return validateScheduleEndDate();
	} else {
		return false;
	}
	return true;
	}
	return false;
});

function replacePercentageValue() {
	var percentage = $('#percentage').val();
	if (percentage == 0.0)
		$('#percentage').val("");
}