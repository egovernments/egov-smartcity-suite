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

var emptyRow = '<tr><td colspan="5" id="emptyRow" >No preamble item available</td></tr>';
var tbody = $('#agendaTable').children('tbody');
var table = tbody.find('tr').length == 0 ? $('#agendaTable tbody').append(emptyRow) : $('#agendaTable');

var row = '<tr>'+
/* '<td><span class="sno">{{sno}}</span></td>'+*/
 '<td><input type="text" class="form-control" data-unique name="councilAgendaDetailsForUpdate[{{idx}}].preamble.preambleNumber" {{readonly}} value="{{pnoTextBoxValue}}"/></td>'+
 '<td><input type="text" class="form-control" name="councilAgendaDetailsForUpdate[{{idx}}].preamble.department.name" {{readonly}} value="{{deptTextBoxValue}}"/></td>'+
 '<td><textarea class="form-control" name="councilAgendaDetailsForUpdate[{{idx}}].preamble.gistOfPreamble" rows="3" {{readonly}} maxlength="5000" >{{gistTextBoxValue}}</textarea></td>'+
 '<td><input type="text" class="form-control" name="councilAgendaDetailsForUpdate[{{idx}}].preamble.sanctionAmount" {{readonly}} value="{{amountTextBoxValue}}"/></td>'+
//  '<td><input type="hidden" class="form-control" name="agendaDetails[{{idx}}].preamble.department.id" {{readonly}} value="{{departmentId}}"/>'+
 '<td><input type="hidden" class="form-control" name="councilAgendaDetailsForUpdate[{{idx}}].preamble.id" {{readonly}} value="{{preamableId}}"/>'+
/* '<td><input type="hidden" class="form-control" name="agendaDetails[{{idx}}].markedForRemoval" {{readonly}} value="{{removevalue}}"/>'+*/
 '<button type="button" class="btn btn-xs btn-secondary delete"><span class="glyphicon glyphicon-trash"></span>&nbsp;Delete</button></td>'+
'</tr>';



/*jQuery('#add-agenda').click(function(){
   
	var idx=$(tbody).find('tr').length;
	alert("len : "+idx);
	var sno=idx+1;
   //Add row
	var row={
	       'sno': sno,
	       'idx': idx
	   };
	addRowFromObject(row);
   
});*/

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
	var data=JSON.parse(unescape($(btn).data('row')));
		
	$('#emptyRow').closest('tr').remove();

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
	
	/*$('.agenda-section').show();*/
	var idx=$(tbody).find('tr').length;
	var sno=idx+1;
	
	
	//console.log("Row : ", data);
	
	//Add row
	var row={
		  /* 'sno':sno,*/
		   'idx':idx,
	       'pnoTextBoxValue': data.preambleNumber,
	       'deptTextBoxValue': data.department,
	       'gistTextBoxValue': 
				 data.gistOfPreamble.replace(/\\u[\dA-F]{4}/gi, 
				          function (match) {
				               return String.fromCharCode(parseInt(match.replace(/\\u/g, ''), 16));
				          })
			,
	       'amountTextBoxValue': data.sanctionAmount,
	      // 'departmentId' :data.id,
	       'preamableId':data.id,
	       'readonly':'readonly="readonly"'
	   };
	addRowFromObject(row);
}


jQuery('#btnsearch').click(function(e) {
	callAjaxSearch();
	$('.agenda-section').show();
});

$('form').keypress(function (e) {
    if (e.which == 13) {
    	e.preventDefault();
    	callAjaxSearch();
    }
}); 

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
 
function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");		
	jQuery('.report-section').removeClass('display-hide');
		reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/council/agenda/ajaxsearch",      
					type: "POST",
					traditional: true,
					beforeSend : function() {
						$('.loader-class').modal('show', {
							backdrop : 'static'
						});
					},
					"data" : getFormData(jQuery('form')),
					complete : function() {
						$('.loader-class').modal('hide');
					}
				},
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
							'sTitle' : "Gist of Preamble", "width": "58%",
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
										+ escape(JSON.stringify(full))
										+ '\' onclick="addReadOnlyRow(this)"><span class="glyphicon glyphicon-edit"></span>&nbsp;Add</button>';
							}
						}, {
							"data" : "id",
							"visible" : false,
							"searchable" : false
						}
					],columnDefs:[
									{
										  "render" : function(data, type, row) {
											  var str = data.replace(/\\u[\dA-F]{4}/gi, 
											          function (match) {
									             return String.fromCharCode(parseInt(match.replace(/\\u/g, ''), 16));
									             
									        });
											  return type === 'display' && '<div><span>'+(str.length > 500 ? str.substr( 0, 500 )+'</span> <button class="details" data-text="'+escape(str)+'" class="btn-xs" style="font-size:10px;">More <i class="fa fa-angle-double-right" aria-hidden="true"></i></button></div>' : str+"</p>");;
										  },
									      "targets": [2]
									  }
				     	          ]			
			});
			}


$(document).ready(function() {
	
	$("#resultTable").on('click','tbody tr td button.details',function(e) {
		if($(this).parent().find('span').text().length==500){
			$(this).parent().find('span').text(unescape($(this).data('text')));	
			$(this).html('<i class="fa fa-angle-double-left" aria-hidden="true"></i> Less');
		}
		else
		{
			$(this).parent().find('span').text(unescape($(this).data('text')).substr(0,500));	
			$(this).html('More <i class="fa fa-angle-double-right" aria-hidden="true"></i>');
		}
		e.stopPropagation();
		e.preventDefault();
	});
	
	
	$("#agendaTable tbody").on('click','tr td .delete',function(event) {
		$(this).closest('tr').remove();
		regenerateIndexes();
		var idx  = $("#agendaTable > tbody").children().length;
		if(idx == 0){
			$('#agendaTable tbody').append(emptyRow);
		}
	});
	
	$( "#btnsave" ).click(function(e){
		if ($('#committeeType').val()=="") {
			bootbox.alert("Please select committe type");
			e.preventDefault();
		} 
		if($('#emptyRow').length){
			bootbox.alert("Atleast one preamble item should be added into agenda");
			e.preventDefault();
		}else{
			// form submit happen
		}
		

	});
	
	$( "#buttonSubmit" ).click(function(e){
		
		if ($('#committeeType').val()=="") {
			bootbox.alert("Please select committe type");
			e.preventDefault();
			} 
		if($('#emptyRow').length){
			bootbox.alert("Atleast one preamble item should be added into agenda");
			e.preventDefault();
		}else{
			// form submit happen
		}

	});
	
});

//To Select all wards
$('#selectall').click( function() {
    $('select#wards > option').prop('selected', 'selected');
});