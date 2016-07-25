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
$(document).ready(function() {
	
	initializeDatePicker();
		
});

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

function validateForm(){
	$('.statusdate').prop('required', false);
	var tbl=document.getElementById('tblsetstatus');		
	var flag = false;
	var length = $('.statusdate').length;
	
	if(document.getElementById('statusDate_0').value == ""){
		$('#statusDate_0').prop('required', 'required');
		return false;
	}
	
	for (var j = 0; j < length; j++) {
		var offlineStatusesId = document.getElementById('offlineStatusesId_' + j ).value;
		if(offlineStatusesId != ""){
			var k=j+1;
			var statusDate = document.getElementById('statusDate_' + k ).value;
			if(statusDate == ""){
				$('#statusDate_' + k).prop('required', 'required');
				return false;
			}
		}
	}
	
	for (var i = 0; i < length; i++) {
		var statusDate = document.getElementById('statusDate_' + i ).value;
		if(statusDate == "" && !flag){
			flag = true;
		}
		if(statusDate != "" && flag){
			var message = document.getElementById('errorStatusDateIntermediate').value;
			bootbox.alert(message);
			return false;
		}
	}
	flag = false;
	for (var i = 0; i < length; i++) {
		var statusDate = document.getElementById('statusDate_' + i ).value;
		if(statusDate == "" && !flag){
			flag = true;
			index = i + 1;
		}
		if(flag)
			tbl.deleteRow(index);
	}
	
	if($('#offlineStatuses').valid()){
		$(".statusdate").removeAttr('disabled');
		return true;
	}
	else
		return false;
	
}

function validateStatusDates(obj){
	var rIndex = getRow(obj).rowIndex - 1;
	k = rIndex-1;
	var workOrderDate=new Date($('#workOrderDate').val());
	var statusDate = $(obj).data('datepicker').date;
	if(rIndex == 0 && statusDate<workOrderDate)
	{
		var message = document.getElementById('errorStatusLOADate').value;
		$(obj).datepicker("setDate", new Date());
		$(obj).val('');
		$(obj).datepicker('update');
		bootbox.alert(message);
		return false;	
	}
	var rowcount = $("#tblsetstatus tbody tr").length;
	if(k >= 0){
		
		var statusDate = document.getElementById('statusDate_' + k).value;
		if(statusDate == ""){
			var message = document.getElementById('errorStatusDateNull').value;
			$(obj).datepicker("setDate", new Date());
			$(obj).val('');
			$(obj).datepicker('update');
			bootbox.alert(message);
		}
			
		var currentDate = document.getElementById('statusDate_'+ rIndex).value;
		if(new Date((currentDate.split('/').reverse().join('-'))).getTime() < new Date((statusDate.split('/').reverse().join('-'))).getTime())
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

function openLOA() {
	var workOrderId = $('#workOrderId').val();
	window.open("/egworks/letterofacceptance/view/" + workOrderId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}