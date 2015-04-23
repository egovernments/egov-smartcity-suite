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

function fnFormatDetails(table_id, html) {
    var sOut = "<table id=\"exampleTable_" + table_id + "\">";
    sOut += html;
    sOut += "</table>";
    return sOut;
}

//////////////////////////////////////////////////////////// EXTERNAL DATA - Array of Objects 


// DETAILS ROW A 
var detailsRowAPlayer1 = { pic: "Aslam", name: "Jaedong", team: "evil geniuses", server: "NA" };
var detailsRowAPlayer2 = { pic: "Dinesh", name: "Scarlett", team: "acer", server: "Europe" };
var detailsRowAPlayer3 = { pic: "Sreekanth", name: "Stephano", team: "evil geniuses", server: "Europe" };
var detailsRowAPlayer4 = { pic: "Sreekanth", name: "Stephano", team: "evil geniuses", server: "Europe" };
var detailsRowAPlayer5 = { pic: "Sreekanth", name: "Stephano", team: "evil geniuses", server: "Europe" };
var detailsRowAPlayer6 = { pic: "Sreekanth", name: "Stephano", team: "evil geniuses", server: "Europe" };
var detailsRowAPlayer7 = { pic: "Sreekanth", name: "Stephano", team: "evil geniuses", server: "Europe" };
var detailsRowAPlayer8 = { pic: "Sreekanth", name: "Stephano", team: "evil geniuses", server: "Europe" };
var detailsRowAPlayer9 = { pic: "Sreekanth", name: "Stephano", team: "evil geniuses", server: "Europe" };
var detailsRowAPlayer10 = { pic: "Sreekanth", name: "Stephano", team: "evil geniuses", server: "Europe" };
var detailsRowAPlayer11 = { pic: "Sreekanth", name: "Stephano", team: "evil geniuses", server: "Europe" };

var detailsRowA = [ detailsRowAPlayer1, detailsRowAPlayer2, detailsRowAPlayer3, detailsRowAPlayer4, detailsRowAPlayer5, detailsRowAPlayer6, detailsRowAPlayer7, detailsRowAPlayer8, detailsRowAPlayer9, detailsRowAPlayer10, detailsRowAPlayer11 ];

// DETAILS ROW B 
var detailsRowBPlayer1 = { pic: "Azhar", name: "Grubby", team: "independent", server: "Europe" };

var detailsRowB = [ detailsRowBPlayer1 ];

// DETAILS ROW C 
var detailsRowCPlayer1 = { pic: "Venkat", name: "Bomber", team: "independent", server: "NA" };

var detailsRowC = [ detailsRowCPlayer1 ];

var rowA = { race: "Zerg", year: "2014", total: "3", details: detailsRowA};
var rowB = { race: "Protoss", year: "2014", total: "1", details: detailsRowB};
var rowC = { race: "Terran", year: "2014", total: "1", details: detailsRowC};

var newRowData = [rowA, rowB, rowC] ;

////////////////////////////////////////////////////////////

var iTableCounter = 1;
var oTable;
var oInnerTable;
var detailsTableHtml;

//Run On HTML Build
$(document).ready(function () {
	
	// you would probably be using templates here  <i class="fa fa-angle-down"></i>
	detailsTableHtml = $("#detailsTable").html();
	
	//Insert a 'details' column to the table
	var nCloneTh = document.createElement('th');
	var nCloneTd = document.createElement('td');
	nCloneTd.innerHTML = '<i class="fa fa-chevron-circle-up" class="tooltip-secondary" data-toggle="tooltip" title="History"></i> ';
	nCloneTd.className = "center";
	
	//Initialse DataTables, with no sorting on the 'details' column
	var oTable = $('#exampleTable').dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false,
		"aaData": newRowData,
		"aoColumns": [
		{ "mDataProp": "race" },
		{ "mDataProp": "year" },
		{ "mDataProp": "total" },
		{
			"mDataProp": "History",
			"sClass": "control center",
			"sDefaultContent": '<i class="fa fa-chevron-circle-up" class="tooltip-secondary" data-toggle="tooltip" title="History"></i>'
		}
		],
		"aaSorting": [[1, 'asc']]
	});
	
	$('#inboxsearch').keyup(function(){
		oTable.fnFilter(this.value);
	});
	
	/* Add event listener for opening and closing details
        * Note that the indicator for showing which row is open is not controlled by DataTables,
        * rather it is done here
	*/
	$('#exampleTable tbody').on('click', 'td i', function () {
		var nTr = $(this).parents('tr')[0];
		var nTds = this;
		
		if (oTable.fnIsOpen(nTr)) {
			/* This row is already open - close it */
			$(this).replaceWith('<i class="fa fa-chevron-circle-up" class="tooltip-secondary" data-toggle="tooltip" title="History"></i>');
			oTable.fnClose(nTr);
		}
		else {
			/* Open this row */
			var rowIndex = oTable.fnGetPosition( $(nTds).closest('tr')[0] ); 
			var detailsRowData = newRowData[rowIndex].details;
			$(this).replaceWith('<i class="fa fa-chevron-circle-down" class="tooltip-secondary" data-toggle="tooltip" title="History"></i>');
			oTable.fnOpen(nTr, fnFormatDetails(iTableCounter, detailsTableHtml), 'details');
			$("#exampleTable_" + iTableCounter).addClass('table table-bordered');
			oInnerTable = $("#exampleTable_" + iTableCounter).dataTable({
				"sPaginationType": "bootstrap",
				"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
				"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
				"autoWidth": false,
				"aaData": detailsRowData,
				"bSort" : true, // disables sorting
				"bFilter": false, // disables filtering
				"aoColumns": [
				{ "mDataProp": "pic" },
				{ "mDataProp": "name" },
				{ "mDataProp": "team" },
				{ "mDataProp": "server" }
	            ],
				"bPaginate": false,
				"oLanguage": {
					"sInfo": ""
				}
			});
			iTableCounter = iTableCounter + 1;
		}
	});
	
});