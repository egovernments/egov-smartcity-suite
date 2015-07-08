/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
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
var tableContainer;
jQuery(document).ready(function ($) {
	$(":input").inputmask();
	
	    tableContainer = $("#aplicationSearchResults"); 
	    
	   	
	    $('#searchapprvedapplication').click(function () {
	    	$.post("/wtms/search/waterSearch/", $('#waterSearchRequestForm').serialize())
			.done(function (searchResult) {
				console.log(JSON.stringify(searchResult));
				
				tableContainer.dataTable({
					destroy:true,
					"sPaginationType": "bootstrap",
					"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
					//"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-6 col-md-3 col-left'i><'col-xs-6 col-md-3 text-right col-left'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-xs-12 col-md-3 col-right'p>>",
					"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
					"autoWidth": false,
					searchable:true,
					data: searchResult,
					columns:  [
					{title: 'Consumer No.', data: 'resource.clauses.consumercode'},
					{title: 'Application Name', data: 'resource.searchable.consumername'},
					{title: 'Address', data: 'resource.searchable.locality'},
					{title: 'Usage Type',	data: 'resource.clauses.usage'},
					{title: 'Total Due', data: 'resource.clauses.totaldue'},
					{title: 'Actions',  data : null, "target":-1,"defaultContent": 
						'<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">Additional connection</option><option value="2">Closing connection</option><option value="6">Disconnection</option><option value="1">Change of use</option><option value="3">Reconnection</option><option value="4">Holding connection</option><option value="5">Regularization connection</option></select>'}
					
					
					]
				});
			})
		});
		
	   
	    $("#aplicationSearchResults").on('change','tbody tr td .dropchange',function() {
	       var applicationNumber = tableContainer.fnGetData($(this).parent().parent(),0);
	       if( this.value == 0){
				 var url = '/wtms/application/addconnection/'+applicationNumber; 
					$('#waterSearchRequestForm').attr('method', 'get');
					$('#waterSearchRequestForm').attr('action', url);
					window.location=url;
				//window.location.href="applyforadditionalconnection.html"
			}else if( this.value == 2){
				window.location.href="closingwatertap.html"
			}else if( this.value == 6){
				window.location.href="disconnectionotice.html"
			}else if( this.value == 1){
				window.location.href="changeofuse.html"
			}else if( this.value == 3){
				window.location.href="reconnection.html"
			}
		}); 
	    
	    
	    tableContainer = $("#csearch").dataTable({
			"sPaginationType": "bootstrap",
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			//"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-6 col-md-3 col-left'i><'col-xs-6 col-md-3 text-right col-left'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-xs-12 col-md-3 col-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"oTableTools": {
				"sSwfPath": "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
				"aButtons": ["copy", "csv", "xls", "pdf", "print"]
			}
		});
	   
	   
	   
	   

	    $('#searchwatertax').keyup(function(){
			tableContainer.fnFilter(this.value);
		});
		
});
