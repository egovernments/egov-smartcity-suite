/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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

String.prototype.compose = (function (){
	   var re = /\{{(.+?)\}}/g;
	   return function (o){
	       return this.replace(re, function (_, k){
	           return typeof o[k] != 'undefined' ? o[k] : '';
	       });
	   }
}());

var tbody = $('#agendaTable').children('tbody');
var table = tbody.length ? tbody : $('#agendaTable');
var row = '<tr>'+
/* '<td><span class="sno">{{sno}}</span></td>'+*/
 '<td><input type="text" class="form-control" data-unique name="councilAgendaDetailsForUpdate[{{idx}}].preamble.preambleNumber" {{readonly}} value="{{pnoTextBoxValue}}"/></td>'+
 '<td><input type="text" class="form-control" name="councilAgendaDetailsForUpdate[{{idx}}].preamble.department.name" {{readonly}} value="{{deptTextBoxValue}}"/></td>'+
 '<td><input type="text" class="form-control" name="councilAgendaDetailsForUpdate[{{idx}}].preamble.gistOfPreamble" {{readonly}} value="{{gistTextBoxValue}}"/></td>'+
 '<td><input type="text" class="form-control" name="councilAgendaDetailsForUpdate[{{idx}}].preamble.sanctionAmount" {{readonly}} value="{{amountTextBoxValue}}"/></td>'+
//  '<td><input type="hidden" class="form-control" name="agendaDetails[{{idx}}].preamble.department.id" {{readonly}} value="{{departmentId}}"/>'+
 '<td><input type="hidden" class="form-control" name="councilAgendaDetailsForUpdate[{{idx}}].preamble.id" {{readonly}} value="{{preamableId}}"/>'+
/* '<td><input type="hidden" class="form-control" name="agendaDetails[{{idx}}].markedForRemoval" {{readonly}} value="{{removevalue}}"/>'+*/
 '<button type="button" class="btn btn-xs btn-secondary delete"><span class="glyphicon glyphicon-trash"></span>&nbsp;Delete</button></td>'+
'</tr>';


jQuery('#add-agenda').click(function(){
   
	$('.agenda-section').show();
	var idx=$(tbody).find('tr').length;
	var sno=idx+1;
   //Add row
	var row={
	       'sno': sno,
	       'idx': idx
	   };
	addRowFromObject(row);
   
});

function addRowFromObject(rowJsonObj)
{
	console.log("Row composed", row.compose(rowJsonObj));
	table.append(row.compose(rowJsonObj));
}

function regenerateIndexes()
{
	var idx=0;
	jQuery(tbody).find("tr").each(function() {
		
		console.log('TR ITERATION!');
		
		jQuery(this).find("input").each(function() {
			
			console.log('BEFORE INPUT ITERATION!', jQuery(this)[0].outerHTML);
			
			   jQuery(this).attr({
			      'name': function(_, name) {
			    	  return name.replace(/\[.\]/g, '['+ idx +']'); 
			      }
			   });
			   
			   console.log('AFTER INPUT ITERATION!', jQuery(this)[0].outerHTML);
			   
	    });
		idx++;
	});
	
	$('span.sno').each(function(i){
		$(this).html((i+1));
	});
	
}


function addReadOnlyRow(btn)
{
	
	var data=$(btn).data('row');
	
	var isDuplicate=false;
	
	$(tbody).find("input[data-unique][readonly]").each(function() {
		
		if($(this).val() === data.preambleNumber)
		{
			bootbox.alert("Preamble already added in agenda.");
			isDuplicate=true;
			return;
		}
		
	});
	
	if(isDuplicate)
	{
		return;
	}
	
	$('.agenda-section').show();
	var idx=$(tbody).find('tr').length;
	var sno=idx+1;
	
	
	console.log("Row", data);
	
	//Add row
	var row={
		  /* 'sno':sno,*/
		   'idx':idx,
	       'pnoTextBoxValue': data.preambleNumber,
	       'deptTextBoxValue': data.department,
	       'gistTextBoxValue': data.gistOfPreamble,
	       'amountTextBoxValue': data.sanctionAmount,
	      // 'departmentId' :data.id,
	       'preamableId':data.id,
	       'readonly':'readonly="readonly"'
	   };
	addRowFromObject(row);
}


jQuery('#btnsearchPreamble').click(function(e) {
	callAjaxSearch();
});

$('form').keypress(function (e) {
    if (e.which == 13) {
    	e.preventDefault();
    	callAjaxSearch();
    }
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
					url : "/council/agenda/ajaxsearch",      
					type: "POST",
					"data":  getFormData(jQuery('form'))
				},
				"sPaginationType" : "bootstrap",
				"bDestroy" : true,
				"autoWidth": false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				
				aaSorting: [],				

								columns : [
						{
							"data" : "preambleNumber",
							'sTitle' : "Preamble Number"
						},
						{
							"data" : "department",
							'sTitle' : "Department"
						},
						{
							"data" : "gistOfPreamble",
							'sTitle' : "Gist of Preamble"
						},
						{
							"data" : "sanctionAmount",
							"sClass" : "text-right",
							'sTitle' : "Amount"
						},
						{
							"data" : "status",
							"sClass" : "text-right",
							'sTitle' : "Status"
						},
						{
							"data" : null,
							"target" : -1,
							"sortable" : false,
							'sTitle' : "Action",
							"render" : function(data, type, full, meta) {
								return '<button type="button" class="btn btn-xs btn-secondary add"  data-row=\''
										+ JSON.stringify(full)
										+ '\' onclick="addReadOnlyRow(this)"><span class="glyphicon glyphicon-edit"></span>&nbsp;Add</button>';
							}
						}, {
							"data" : "id",
							"visible" : false,
							"searchable" : false
						}
					]				
			});
			}


$(document).ready(function() {
	
	/*jQuery( ".dateval" ).datepicker({ 
   	 format: 'dd/mm/yyyy',
   	 autoclose:true,
        onRender: function(date) {
     	    return date.valueOf() < now.valueOf() ? 'disabled' : '';
     	  }
	  }).on('changeDate', function(ev) {
		  var electiondate = jQuery('#electionDate').val();
		  var oathdate = jQuery('#oathDate').val();
		  if(electiondate && oathdate){
			  DateValidation1(electiondate , oathdate);
		  }
		 
	  }).data('datepicker');*/
/*	
	function DateValidation1(start , end){
	    if (start != "" && end != "") {
			var stsplit = start.split("/");
			var ensplit = end.split("/");
			
			start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
			end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];
			
			return ValidRange(start, end);
		}else{
			return true;
		}
	}

	function ValidRange(start, end) {
		var retvalue = false;
	    var startDate = Date.parse(start);
	    var endDate = Date.parse(end);
		
	    // Check the date range, 86400000 is the number of milliseconds in one day
	    var difference = (endDate - startDate) / (86400000 * 7);
	    if (difference < 0) {
			bootbox.alert("Oath date should be greater than Election date");
			$('#oathDate').val('').datepicker("refresh");
			
			} else {
			retvalue = true;
		}
	    return retvalue;
	}*/

	$("#agendaTable tbody").on('click','tr td .delete',function(event) {
		if($(tbody).find('tr').length==1)
		{	bootbox.alert("You cannot delete this row.");
		}
		else {
		$(this).closest('tr').remove();
		regenerateIndexes();
		}
	});
	
});





