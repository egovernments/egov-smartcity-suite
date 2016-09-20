/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2016>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
        	//console.log("Name : "+o[this.name]);
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            //console.log("value : "+this.value);
            o[this.name].push(this.value);
        } else {
            o[this.name] = this.value;
        }
    });
    console.log(o);
    return o;
};    

jQuery('#btnsearch').click(function(e) {

	callAjaxSearch();
});

$('form').keypress(function(e) {
	if (e.which == 13) {
		e.preventDefault();
		alert($('#councilPreamblesearchform').serialize());
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

/*function getFormData($form) {
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
	
	//console.log("Result :"+indexed_array)
	return indexed_array;
}*/

function callAjaxSearch() {
	
	// To get current date
	var currentDate = new Date()
	var day = currentDate.getDate()
	var month = currentDate.getMonth() + 1
	var year = currentDate.getFullYear()
	var currentDate = day + "-" + month + "-" + year;
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/council/councilreports/preamblewardwise/search-result",
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},

				"sPaginationType" : "bootstrap",
				"bDestroy" : true,
				"autoWidth" : false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [
							{
								"sExtends" : "xls",
								"mColumns": [0,1,2,3,4,5,6,7],
								"sTitle" : "Council Preamble wardwise Report"
							},
							{
								"sExtends" : "pdf",
								"mColumns": [0,1,2,3,4,5,6,7],
								"sPdfMessage" : "Report generated on "
										+ currentDate + "",
								"sTitle" : "Council Preamble wardwise Report"
							}, {
								"sExtends" : "print",
								"sTitle" : "Council Preamble wardwise Report"
							} ]
				},
				"aaSorting" : [],
				"columns" : [ {
					"data" : "", 
					render: function (data, type, row, meta) {
				        return meta.row + meta.settings._iDisplayStart + 1;
				    },
					"sClass" : "text-left"
				}, {
					"data" : "ward","width": "30%",
					"sClass" : "text-left"
				}, {
					"data" : "department", 
					"sClass" : "text-left"
				}, {
					"data" : "preambleType",
					"sClass" : "text-left"
				}, {
					"data" : "gistOfPreamble", "width": "40%",
					"sClass" : "text-left"
				}, {
					"data" : "createdDate",
					"sClass" : "text-left"
				}, {
					"data" : "preambleUsedInAgenda",
					"sClass" : "text-center"
				}, {
					"data" : "meetingDate",
					"sClass" : "text-left"
				}, {
					"data" : "meetingType",
					"sClass" : "text-left"
				}, {
					"data" : "status",
					"sClass" : "text-left"
				}, {
					"data" : "id",
					"visible" : false
				} ],columnDefs:[
			     	              {
			     	                   "render": function ( data, type, row ) {
			     	                	   
			     	                       return type === 'display' && data.length > 500 ? data.substr( 0, 500 )+' <span class="details" data-text="'+escape(data)+'"><button class="btn-xs" style="font-size:10px;">More <i class="fa fa-angle-double-right" aria-hidden="true"></i></button</span>' : data;
			     	                   },
			     	                   "targets": [1,4]
				     	           }
				     	          ] 
			});
}

$("#resultTable").on('click','tbody tr td span.details',function(e) {
	$(this).parent().html(unescape($(this).data('text')));
	e.stopPropagation();
	e.preventDefault();
});