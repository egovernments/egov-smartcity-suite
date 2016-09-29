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
jQuery(document).ready(function() {
	$('#defaultersReport-header').hide();
	$('#reportgeneration-header').hide();
	$('#report-footer').hide();
	
	
	jQuery('#defaultersReportSearch').click(function(e) {
		var fromAmount = $("#fromAmount").val();
		var toAmount = $("#toAmount").val(); 
		var ward = $("#ward").val();
		var topDefaulters = $("#topDefaulters").val();
		console.log(parseInt(fromAmount)+'-'+parseInt(toAmount)+'-'+topDefaulters);
		if ((fromAmount == undefined || parseInt(fromAmount) == 0) &&
				(toAmount == undefined || parseInt(toAmount) == 0) && (topDefaulters == '')) {
			bootbox.alert('Enter either From amount or  To Amount or Top Defaulters , One is mandatory');
			return false;
		}
		 
		if (parseInt(toAmount)!=0 && fromAmount > parseInt(toAmount)) {
			bootbox.alert('From Amount should not be greater than to Amount');
			return false;
		}
		loadingReport();
	});
	
});

function loadingReport()
{
		if($('form').valid()){
			var fromAmount = $("#fromAmount").val();
			var toAmount = $("#toAmount").val(); 
			var ward = $("#ward").val();
			var topDefaulters = $("#topDefaulters").val();
			var today = getdate();

			oTable= $('#defaultersReport-table');
			$('#defaultersReport-header').show();
			$('#reportgeneration-header').show();
	        $("#resultDateLabel").html(fromAmount+" - "+toAmount);	
			var oDataTable=oTable.dataTable({
				"sPaginationType": "bootstrap",
				"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
				"aLengthMenu": [[10, 25, 50, 100,-1], [10, 25, 50, 100,"All"]],
				"autoWidth": false,
				"bDestroy": true,
				"processing": true,
		        "serverSide": true,
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ 
					               {
						             "sExtends": "pdf",
						             "mColumns": [ 1, 2, 3, 4,5,6,7,8,9,10],
	                                 "sPdfMessage": "Defaulters Report as on "+today+"",
	                                 "sTitle": "Water Tax Defaulters Report",
	                                 "sPdfOrientation": "landscape"
					                },
					                {
							             "sExtends": "xls",
							             "mColumns": [ 1,2,3,4,5,6,7,8,9,10],
		                                 "sPdfMessage": "Defaulters Report",
		                                 "sTitle": "Water Tax Defaulters Report"
						             },
						             {
							             "sExtends": "print",
							             "mColumns": [ 1,2,3,4,5,6,7,8,9,10],
		                                 "sPdfMessage": "Defaulters Report",
		                                 "sTitle": "Water Tax Defaulters Report"
						             }],
					
				},
				ajax : {
					url : "/wtms/report/defaultersWTReport/search/result",
					data : {
						'ward': ward,
						'topDefaulters':topDefaulters,
						'fromAmount' :fromAmount,
						'toAmount': toAmount
					}
				},
				"columns" : [{"sTitle" : "S.no", "render": function ( data, type, full, meta ) {
						      return oTable.fnPagingInfo().iStart+meta.row+1;
						    }},
							  { "data" : "hscNo" , "title": "H.S.C NO"},  
							  { "data" : "ownerName", "title": "Owner Name"},
							  { "data" : "wardName", "title": "Revenue Ward"},
							  { "data" : "houseNo", "title": "Door No"},
							  { "data" : "locality", "title": "Locality"},
							  { 
								  "data" : "mobileNumber", "title": "Mobile Number",
								  "render" : function(data, type, row) {
									  return (!data || parseInt(data)==0? "NA" : data);
								  }
							  },
							  { "data" : "duePeriodFrom", "title": "Due Period From"},
							  { "data" : "arrearsDue", "title": "Arears Amount","className": "text-right"},
							  { "data" : "currentDue", "title": "Current Amount","className": "text-right"},
							  { "data" : "totalDue", "title": "Total","className": "text-right"}
							],
							 /* "aaSorting": [[3, 'asc'] , [8,'desc']] ,*/
							  "footerCallback" : function(row, data, start, end, display) {
									var api = this.api(), data;
									if (data.length == 0) {
										jQuery('#report-footer').hide();
									} else {
										jQuery('#report-footer').show(); 
									}
									if (data.length > 0) {
										updateTotalFooter(8, api);
										updateTotalFooter(9, api);
										updateTotalFooter(10, api);
									}
								},
					            "fnInitComplete": function() {
					            	if(oDataTable){ oDataTable.fnSort( [ [7,'desc'] , [3,'asc'] ] ); }
					            },
					            
								"aoColumnDefs" : [ {
									"aTargets" : [8,9,10],
									"mRender" : function(data, type, full) {
										return formatNumberInr(data);    
									}
								} ]		
					});
			
			
			e.stopPropagation();
		}
		
		function updateSerialNo()
		{
			$( "#defaultersReport-table tbody tr" ).each(function(index) {
				if($(this).find('td').length>1)
				{
					oDataTable.fnUpdate(''+(index+1), index, 0);
				}
			});
			
		}
		
	
}

function formatNumberInr(x) {
	if (x) {
		x = x.toString();
		var afterPoint = '';
		if (x.indexOf('.') > 0)
			afterPoint = x.substring(x.indexOf('.'), x.length);
		x = Math.floor(x);
		x = x.toString();
		var lastThree = x.substring(x.length - 3);
		var otherNumbers = x.substring(0, x.length - 3);
		if (otherNumbers != '')
			lastThree = ',' + lastThree;
		var res = otherNumbers.replace(/\B(?=(\d{2})+(?!\d))/g, ",")
				+ lastThree + afterPoint;
		return res;
	}
	return x;
}
function getdate()
{
	var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth()+1; //January is 0!

    var yyyy = today.getFullYear();
    if(dd<10){
        dd='0'+dd
    } 
    if(mm<10){
        mm='0'+mm
    } 
    var today = dd+'/'+mm+'/'+yyyy;
    return today;
}
function updateTotalFooter(colidx, api) {
	// Remove the formatting to get integer data for summation
	var intVal = function(i) {
		return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
				: typeof i === 'number' ? i : 0;
	};

	// Total over all pages
	total = api.column(colidx).data().reduce(function(a, b) {
		return intVal(a) + intVal(b);
	});

	// Total over this page
	pageTotal = api.column(colidx, {
		page : 'current'
	}).data().reduce(function(a, b) {
		return intVal(a) + intVal(b);
	}, 0);

	// Update footer
	jQuery(api.column(colidx).footer()).html(
			formatNumberInr(pageTotal) + ' (' + formatNumberInr(total)
					+ ')');
}