/*-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
jQuery('#btnsearch').click(function(e) {

	callAjaxSearch();
});

function validateToValue()
{
	var todays=parseFloat($('#toRange').val());
	var fromdays=parseFloat($('#fromRange').val());
	
	if(todays && fromdays >= todays)
	{
		bootbox.alert('To(days) value should be greater than From(days) value ->'+ fromdays );
		return false;
	}
	return true;
}

$('.daysvalidate').blur(function(){
	validateToValue();
});


$('#buttonSubmit').click(function(e){
	if(validateToValue()){
		
	}else{
		e.preventDefault();
	}
})

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/tl/penaltyRates/ajaxsearch/" + $('#mode').val()+"?licenseAppType="+$('#licenseAppType').val(),
					type : "POST"
				},
				"fnRowCallback" : function(row, data, index) {

				},
				"sPaginationType" : "bootstrap",
				"bDestroy" : true,
				"autoWidth": false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ "xls", "pdf", "print" ]
				},
				aaSorting : [],
				columns : [ {
					"data" : "licenseAppType",
					"sClass" : "text-center"
				}, {
					"data" : "fromRange",
					"sClass" : "text-center"
				}, {
					"data" : "toRange",
					"sClass" : "text-center"
				},
				{
					"data" : "rate",
					"sClass" : "text-center"
				},
				{ "data" : null, "target":-1,
					
	                 sortable: false,
	                 "render": function ( data, type, full, meta ) {
	                     var mode = $('#mode').val();
	                	 if(mode == 'edit')
	                    	 return '<button type="button" class="btn btn-xs btn-secondary edit"><span class="glyphicon glyphicon-edit"></span>&nbsp;Edit</button>';
	                     else
	                    	return '<button type="button" class="btn btn-xs btn-secondary view"><span class="glyphicon glyphicon-tasks"></span>&nbsp;View</button>';
	                 }
				}
				,
				{ "data": "id", "visible":false }
				]
			});
}

$("#resultTable").on('click','tbody tr td .edit',function(event) {
	var id = reportdatatable.fnGetData($(this).parent().parent(),5);
	var url = '/tl/penaltyRates/edit/'+id ;
	window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
});

$("#resultTable").on('click','tbody tr td .view',function(event) {
	var id = reportdatatable.fnGetData($(this).parent().parent(),5);
	var url = '/tl/penaltyRates/view/'+id ;
	window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
});

