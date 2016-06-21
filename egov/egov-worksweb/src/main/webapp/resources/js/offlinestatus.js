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
$detailsRowCount = $('#statusSize').val();

$(document).ready(function() {
	
	$('.offlineStatuses').trigger('change');
	resetAddedOverheads();
	$('#isOfflineStatusValuesLoading').val('false');
	
});

function addNewStatus() {
	if($('#workOrder').valid()){
	var rowcount = $("#tblsetstatus tbody tr").length;
	if (rowcount < 6) {
		if (document.getElementById('statusRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = $("#tblsetstatus tbody tr").length;
			var isValid = 1;// for default have success value 0

			// validate existing rows in table
			$("#tblsetstatus tbody tr").find('input, select, textarea').each(
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
			$("#statusRow").clone().find("input, select, span[name='spandelete']").each(
					function() {

						if ($(this).data('server')) {
							$(this).removeAttr('data-server');
						}
						
							$(this).attr(
									{
										'id' : function(_, id) {
											return id.replace(/\d+/, nextIdx);
										},
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
							
							$(this).attr('readonly', false);
							$(this).removeAttr('disabled');
							$(this).prop('checked', false);

					}).end().appendTo("#tblsetstatus tbody");
			generateSno();
			$("#spandelete_" + nextIdx).removeAttr('style');
		}
	} else {
		  bootbox.alert('limit reached!');
	}
	}
	
	initializeDatePicker();
	
	return false;
}

function generateSno()
{
	var idx=1;
	$(".spansno").each(function(){
		$(this).text(idx);
		idx++;
	});
}

function resetAddedOverheads(){
	addedOfflineStatus = new Array();
	var resultLength = jQuery('#tblsetstatus tr').length-1;
	var index;
	for (var i = 0; i < resultLength; i++) {
		index = i;
		var offlineStatusValue = document.getElementById('offlineStatuses['
				+ index + '].egwStatus').value;
		if(offlineStatusValue != "")
			addedOfflineStatus.push(offlineStatusValue);
	}
}

function deleteSetStatus(obj) {
    var rIndex = getRow(obj).rowIndex;
    
    var id = $(getRow(obj)).children('td:first').children('input:first').val();
    //To get all the deleted rows id
    var aIndex = rIndex - 1;

	var tbl=document.getElementById('tblsetstatus');	
	var rowcount=$("#tblsetstatus tbody tr").length;
		tbl.deleteRow(rIndex);
		//starting index for table fields
		var idx=parseInt($detailsRowCount);
		//regenerate index existing inputs in table row
		$("#tblsetstatus tbody tr").each(function() {
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

var addedOfflineStatus = new Array();

function cheackOfflineStatus(offlineStatuses) {
	if(offlineStatuses.value==""){
		resetAddedOverheads();
	}else
		if(addedOfflineStatus.indexOf(offlineStatuses.value) == -1) {
			if($('#isOfflineStatusValuesLoading').val() == 'false')
				resetAddedOverheads();
			var objName = offlineStatuses.name;
			var index =objName.substring(objName.indexOf("[")+1,objName.indexOf("]")); 
		}
				else{
		var index =offlineStatuses.name.substring(offlineStatuses.name.indexOf("[")+1,offlineStatuses.name.indexOf("]")); 
		offlineStatuses.value= "";
		bootbox.alert("The Offline Status is already added");
		resetAddedOverheads();
	}
}

function validateForm(){
	$(".offlineStatusValue").removeAttr('disabled');
	$(".statusdate").removeAttr('disabled');
	if($('#workOrder').valid())
		return false;
}

function validateStatusDates(obj){
	var workOrderDate=new Date($('#workOrderDate').val());
	var statusDate = $(obj).data('datepicker').date
	if(statusDate<workOrderDate)
	{
		var message = document.getElementById('errorStatusLOADate').value;
		bootbox.alert(message);
		$(obj).datepicker("setDate", new Date());
		$(obj).datepicker('update');
		$(obj).val('');
		return false;	
	}
	
	var rowcount = $("#tblsetstatus tbody tr").length;
	if(rowcount > 1){
		var rIndex = getRow(obj).rowIndex - 1;
		
		var statusDate = document.getElementById('offlineStatuses['+ (rIndex-1) + '].statusDate').value;
		var currentDate = document.getElementById('offlineStatuses['+ (rIndex) + '].statusDate').value;
		if(currentDate < statusDate )
		{
			var message = document.getElementById('errorStatusDate').value;
			$(obj).datepicker("setDate", new Date());
			$(obj).val('');
			$(obj).datepicker('update');
			bootbox.alert(message);
			return false;	
		}
		return true;
	}
	
}

initializeDatePicker();

function initializeDatePicker(){
	
	$('.statusdate').datepicker().off('changeDate');
	
	jQuery( ".statusdate" ).datepicker({ 
		format: 'dd/mm/yyyy',
		autoclose:true,
		onRender: function(date) {
			return date.valueOf() < now.valueOf() ? 'disabled' : '';
		}
		}).on('changeDate', function(ev) {
		
			var string=jQuery(this).val();
			if(!(string.indexOf("_") > -1)){
			isDatepickerOpened=false; 
			validateStatusDates(this);
			$('.statusdate').datepicker('hide');
			}

		}).data('datepicker');
	
	$('.statusdate').datepicker('update');
	try { $(".statusdate").inputmask(); }catch(e){}	

}