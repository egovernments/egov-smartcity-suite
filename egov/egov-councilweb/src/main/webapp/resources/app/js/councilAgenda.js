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

var emptyRow = '<tr><td colspan="6" id="emptyRow" class="text-center">No preamble item available</td></tr>';
var tbody = $('#agendaTable').children('tbody');
var table = tbody.find('tr').length == 0 ? $('#agendaTable tbody').append(emptyRow) : $('#agendaTable');

var row = '<tr>'+
'<td class="text-center"> <i class="fa fa-arrows serialno" aria-hidden="true"></i> &nbsp;&nbsp;<span class="sno serialno">{{sno}}</span> <input type="hidden" class="form-control" data-unique data-sno name="councilAgendaDetailsForUpdate[{{idx}}].order" value="{{order}}"/></td>'+
 '<td><input type="hidden" class="form-control" data-unique name="councilAgendaDetailsForUpdate[{{idx}}].preamble.preambleNumber" value="{{pnoTextBoxValue}}" /><span>{{pnoTextBoxValue}}</span></td>'+
 '<td><span>{{deptTextBoxValue}}</span></td>'+
 '<td><span class="more">{{gistTextBoxValue}}</span></td>'+
 '<td class="text-right"><span>{{amountTextBoxValue}}</span></td>'+
 '<td><input type="hidden" class="form-control" name="councilAgendaDetailsForUpdate[{{idx}}].preamble.id" value="{{preamableId}}"/>'+
 '<a class="btn btn-xs btn-secondary" href="/council/councilpreamble/view/{{preamableId}}"  target="popup"' +
 'onclick="window.open(\'/council/councilpreamble/view/{{preamableId}}\',\'popup\',\'width=600,height=600,resizable=no\'); return false;">'+
 '<i class="fa fa-eye" aria-hidden="true"></i>&nbsp;View'+
	'</a>&nbsp;<button type="button" class="btn btn-xs btn-secondary delete"><span class="glyphicon glyphicon-trash"></span>&nbsp;Delete</button></td>'+
'</tr>';


function addRowFromObject(rowJsonObj)
{
	table.append(row.compose(rowJsonObj));
	generateOrderNo();
	showMoreOrLess();
}

function regenerateIndexes()
{
	var idx=0;
	$(tbody).find("tr").each(function() {
		
		$(this).find("input").each(function() {
			
			console.log('BEFORE INPUT ITERATION!', $(this)[0].outerHTML);
			
			   $(this).attr({
			      'name': function(_, name) {
			    	  console.log('name', name);
			    	  return name.replace(/\[.\]/g, '['+ idx +']'); 
			      }
			   });
			   console.log('AFTER INPUT ITERATION!', $(this)[0].outerHTML);
	    });
		idx++;
	});
	generateOrderNo();
}


var moretext = "More ";
var lesstext = " Less";

$(document).on('click',".morelink", function(){
    if($(this).hasClass("less")) {
        $(this).removeClass("less");
        $(this).html(moretext+'<i class="fa fa-angle-double-right" aria-hidden="true"></i>');
    } else {
        $(this).addClass("less");
        $(this).html('<i class="fa fa-angle-double-left" aria-hidden="true"></i>'+lesstext);
    }
    $(this).parent().prev().toggle();
    $(this).prev().toggle();
    return false;
});

function showMoreOrLess(){
	var showChar = 500; // How many characters are shown by default
    var ellipsestext = "...";
    $('.more:not([data-applied])').each(function() {
	        var content = $(this).html();
	        if(content.length > showChar) {
	            var c = content.substr(0, showChar);
	            var h = content.substr(showChar, content.length - showChar);
	            var html = c + '<span class="moreellipses">' + ellipsestext+ '&nbsp;</span><span class="morecontent"><span>' + h + '</span>&nbsp;&nbsp;<button class="btn-xs morelink" style="font-size:10px;">' + moretext + '<i class="fa fa-angle-double-right" aria-hidden="true"></i></button></span>';
	            $(this).html(html);
	        }
	        $(this).attr('data-applied', 'true');
   });
	 
}

function generateOrderNo(){
	$('span.sno').each(function(i){
		  $(this).html((i+1));    
	});
	
	$('input[data-sno]').each(function(i){
		$(this).val(i+1);;
	});
}


function addReadOnlyRow(btn)
{
	var data=JSON.parse(unescape($(btn).data('row')));
		
	$('#emptyRow').closest('tr').remove();

	var isDuplicate=false;
	
	$(tbody).find("input[data-unique]").each(function() {
		
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
	
	//Add row
	var row={
		   'idx':idx,
		   'order':idx+1,
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
							 "data":null,
							 "sClass" : "text-center",
							 'sTitle' : "S.No.", "width": "4%",
				        	   render: function (data, type, row, meta) {
				        	        return meta.row + meta.settings._iDisplayStart + 1;
			                },   
				        },
						{
							"data" : "preambleNumber",
							'sTitle' : "Preamble Number", "width": "9%"
						},
						{
							"data" : "department",
							'sTitle' : "Department", "width": "9%"
						},
						{
							"data" : "gistOfPreamble",
							'sTitle' : "Gist of Preamble", "width": "50%"
						},
						{
							"data" : "sanctionAmount",
							"sClass" : "text-right",
							'sTitle' : "Amount"
						},
						{
							"data" : "status",
							"sClass" : "text-left",
							'sTitle' : "Status"
						},
						{
							"data" : null, "width" : "11%",
							"target" : -1,
							"sortable" : false,
							'sTitle' : "Action",
							"render" : function(data, type, full, meta) {
								return '<button type="button" class="btn btn-xs btn-secondary view"><i class="fa fa-eye" aria-hidden="true"></i>&nbsp;View</button>&nbsp;<button type="button" class="btn btn-xs btn-secondary add"  data-row=\''
										+ escape(JSON.stringify(full))
										+ '\' onclick="addReadOnlyRow(this)"><i class="fa fa-plus" aria-hidden="true"></i>&nbsp;Add</button>';
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
									      "targets": [3]
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
	
	$("#resultTable").on(
			'click',
			'tbody tr td  .view',
			function(event) {
				var id = reportdatatable.fnGetData($(this).parent().parent(), 7);
				window.open('/council/councilpreamble/view/'
						+ id, '', 'width=800, height=600,scrollbars=yes');

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
	
	
	// changing order of preambles while creating and upating agenda.
    $('.sorted_table').sortable({
    	containerSelector: 'table',
		  itemPath: '> tbody',
		  itemSelector: 'tr',
		  placeholder: '<tr class="placeholder"/>',
		  onDrop: function ($item, container, _super) {
		    container.el.removeClass("active");
		    _super($item, container);
		    regenerateIndexes();
		  }
	});
		   
});
