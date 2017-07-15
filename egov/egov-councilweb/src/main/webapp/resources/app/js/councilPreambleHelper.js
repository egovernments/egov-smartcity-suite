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

jQuery('#btnsearch').click(function(e) {

	callAjaxSearch();
});

jQuery('#preamblebtnsearch').click(function(e) {

	callAjaxSearchForAgendaPreamble();
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

$('#buttonClose')
		.click(
				function(e) {
					bootbox
							.confirm({
								message : 'Information entered in this screen will be lost if you close this page. please confirm if you want to close',
								buttons : {
									'cancel' : {
										label : 'No',
										className : 'btn-danger pull-right'
									},
									'confirm' : {
										label : 'Yes',
										className : 'btn-danger pull-right'
									}
								},
								callback : function(result) {
									if (result) {
										self.close();
									} else {
										// document.forms["councilPreambleform"].submit();//submit
										// it

										e.preventDefault();

									}
								}
							});
				});

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/council/councilpreamble/ajaxsearch/"
							+ $('#mode').val(),
					type : "POST",
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
				"autoWidth" : false,
				"bDestroy" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ {
						"sExtends" : "xls",
						"mColumns": [0,1,2,3]
					}, {
						"sExtends" : "pdf",
						"mColumns": [0,1,2,3]
					}, {
						"sExtends" : "print"
					} ]
				},
				"fnRowCallback" : function(row, data, index) {
					var mode = $('#mode').val();
					if (mode == 'edit')
						$('td:eq(5)',row).html(data.implementationStatus);				
					else
						$('td:eq(5)',row).html(data.status);
					return row;
				},
				aaSorting : [],
				columns : [
						{
							 "data":null,
							 "sClass" : "text-center",
				        	   render: function (data, type, row, meta) {
				        	        return meta.row + meta.settings._iDisplayStart + 1;
			                },   
				        },
						{
							"data" : "department",
							"width" : "8%",
							"sClass" : "text-left"
						},
						{
							"data" : "ward",
							"width" : "29%",
							"sClass" : "text-left"
						},
						{
							"data" : "preambleNumber",
							"sClass" : "text-left"
						},
						{
							"data" : "gistOfPreamble",
							"width" : "29%",
							"sClass" : "text-left"
						},
						{
							"data" : "status",
							"width" : "15",
							"sClass" : "text-left"
						},
						{

							"data" : "sanctionAmount",
							"sClass" : "text-right"
						},
						{
							"data" : null,
							"target" : -1,

							sortable : false,
							"render" : function(data, type, full, meta) {
								var mode = $('#mode').val();
								if (mode == 'edit')
									return '<button type="button" class="btn btn-xs btn-secondary changeStatus"><span class="glyphicon glyphicon-change Status"></span>&nbsp;&nbsp;Change Status</button>';
								else
									return '<button type="button" class="btn btn-xs btn-secondary view"><i class="fa fa-eye" aria-hidden="true"></i>&nbsp;&nbsp;View</button>';
							}
						}, {
							"data" : "id",
							"visible" : false
						} ],columnDefs:[
					     	              {
					     	                   "render": function ( data, type, row ) {
					     	                	   
					     	                	  return type === 'display' && '<div><span>'+(data.length > 500 ? data.substr( 0, 500 )+'</span> <button class="details" data-text="'+escape(data)+'" class="btn-xs" style="font-size:10px;">More <i class="fa fa-angle-double-right" aria-hidden="true"></i></button></div>' : data+"</p>");
					     	                   },
					     	                   "targets": [2]
						     	           },
						     	           {
							     	        	  "render" : function(data, type, row) {
							     	        		  var str = data.replace(/\\u[\dA-F]{4}/gi, 
													          function (match) {
											               return String.fromCharCode(parseInt(match.replace(/\\u/g, ''), 16));
											               
											          });
							     	        		  return type === 'display' && '<div><span>'+(str.length > 500 ? str.substr( 0, 500 )+'</span> <button class="details" data-text="'+escape(str)+'" class="btn-xs" style="font-size:10px;">More <i class="fa fa-angle-double-right" aria-hidden="true"></i></button></div>' : str+"</p>");;
							     	        	  },
						     	                   "targets": [4]
							     	           }
						     	          ] 
			});
}

function callAjaxSearchForAgendaPreamble() {
	drillDowntableContainer = jQuery("#preambleResultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/council/councilpreamble/ajaxsearch/"
							+ $('#mode').val(),
					type : "POST",
					traditional: true,
					"data" : getFormData(jQuery('form'))
				},
				"bDestroy" : true,
				"autoWidth" : false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ {
						"sExtends" : "xls"
					}, {
						"sExtends" : "pdf"
					}, {
						"sExtends" : "print"
					} ]
				},
				aaSorting : [],
				columns : [
						{
							 "data":null,
							 "sClass" : "text-center",
				        	   render: function (data, type, row, meta) {
				        	        return meta.row + meta.settings._iDisplayStart + 1;
			                },   
				        },
						{
							"data" : "department",
							"sClass" : "text-left"
						},
						{
							"data" : "preambleNumber",
							"sClass" : "text-left"
						},
						{
							"data" : "gistOfPreamble",
							"sClass" : "text-left"
						},
						{
							"data" : "sanctionAmount",
							"sClass" : "text-right"
						},
						{
							"data" : null,
							"target" : -1,

							sortable : false,
							"render" : function(data, type, full, meta) {
								var mode = $('#mode').val();
								if (mode == 'edit')
									return '<button type="button" class="btn btn-xs btn-secondary edit"><span class="glyphicon glyphicon-edit"></span>&nbsp;Edit</button>';
								else
									return '<button type="button" class="btn btn-xs btn-secondary view"><span class="glyphicon glyphicon-tasks"></span>&nbsp;View</button>';
							}
						}, {
							"data" : "id",
							"visible" : false
						} ],"columnDefs":[
					     	              {
							     	        	  "render" : function(data, type, row) {
							     	        		  var str = data.replace(/\\u[\dA-F]{4}/gi, 
													          function (match) {
											               return String.fromCharCode(parseInt(match.replace(/\\u/g, ''), 16));
											          });
							     	        		  return type === 'display' && '<div><span>'+(str.length > 500 ? str.substr( 0, 500 )+'</span> <button class="details" data-text="'+escape(str)+'" class="btn-xs" style="font-size:10px;">More <i class="fa fa-angle-double-right" aria-hidden="true"></i></button></div>' : str+"</p>");
							     	        	  },
						     	                   "targets": [3]
							     	           }
						     	          ] 
			});
}

$("#resultTable,#preambleResultTable").on('click','tbody tr td button.details',function(e) {
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
			var id = reportdatatable.fnGetData($(this).parent().parent(), 8);
			window.open('/council/councilpreamble/' + $('#mode').val() + '/'
					+ id, '', 'width=800, height=600,scrollbars=yes');

		});

$("#resultTable").on(
		'click',
		'tbody tr td  .changeStatus',
		function(event) {
			var id = reportdatatable.fnGetData($(this).parent().parent(), 8);
			window.open('/council/councilpreamble/updateimplimentaionstatus' + '/'
					+ id, '', 'width=800, height=600,scrollbars=yes');

		});

$("#momdetails").on(
		'click',
		'tbody tr td .view',
		function(event){
    var id = $('#test').val();
    window.open('/council/councilmom/view'+ '/'+id,'','width=800, height=600,scrollbars=yes');
});
