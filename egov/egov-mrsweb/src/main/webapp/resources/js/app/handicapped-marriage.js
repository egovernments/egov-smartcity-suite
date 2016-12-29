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

var tableContainer;
jQuery(document).ready(function($) {
	$('.table-header').hide();
	tableContainer = $("#handicappedSearchResults");
	document.onkeydown=function(evt){
		var keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
		if(keyCode == 13){
			submitButton();	
		}
	}
});
	  
$("#btn_handicapped_search").click(function(event){
	submitButton();
});

function submitButton(){
		tableContainer = $("#handicappedSearchResults");
		var applicantType=$('#applicantType').val();
		//alert(applicantType);
		$('#searchResultDiv').show();
		$.post("/mrs/report/handicapped-report-search",jQuery.param({applicantType:applicantType}))
		.done(function(searchResult) {
		console.log(JSON.stringify(searchResult));
		tableContainer.dataTable({ 
		destroy : true,
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 hidden col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu" : [[10,25,50,-1 ],[10,25,50,"All" ] ],
		"autoWidth" : false,
		data : searchResult,
		columns : [{title : 'Application No',class : 'row-detail', data : 'applicationNumber',
		        	   "render": function ( data, type, row, meta ) {
		        		   return '<a target="_blank" href="">'+data+'</a>';} },
		           {title : 'Registration Number',data : "registrationNumber"},
		           {title : 'Registration Date',data : "registrationDate"},
		          /* {title : 'Registration Unit',data :  "doorNumber"},*/
		           {title : 'Zone',data : "zone"},
		           {title : 'Application Status',data : "status"},
		           {title : 'Marriage Date',data : "marriageDate"},
		           {title : 'Husband Name',data : "husbandName"},
		           {title : 'Wife Name',data : "wifeName"}],
		           "aaSorting": [[2, 'asc']]
		});
		
		$('.table-header').show();
		if(tableContainer.fnGetData().length > 1000) {
			$('#searchResultDiv').hide();
			$('#search-exceed-msg').show();
		} else {
			$('#search-exceed-msg').hide();
			$('#searchResultDiv').show();
		}
		});
}