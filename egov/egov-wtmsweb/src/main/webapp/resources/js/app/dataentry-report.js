/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
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
 *
 */
jQuery(document).ready(function() {

	$('#reportgeneration-header').hide();
	$('#report-footer').hide();
	
	jQuery('#dataEntryReportSearch').click(function(e) {
		
		loadingReport();
	});
	
});

function loadingReport()
{
		if($('form').valid()){
			var ward = $("#ward").val();
			var today = getdate();

			oTable= $('#dataEntryReport-table');
			$('#reportgeneration-header').show();
			var oDataTable=oTable.dataTable({
				"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
				"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
				"autoWidth": false,
				"bDestroy": true,
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ 
					               {
						             "sExtends": "pdf",
						             "mColumns": [ 1, 2, 3, 4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24],
	                                 "sPdfMessage": "Data Entry Connection Report as on "+today+"",
	                                 "sTitle": "Data Entry Connection Report",
	                                 "sPdfOrientation": "landscape"
					                },
					                {
							             "sExtends": "xls",
							             "mColumns": [ 1, 2, 3, 4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25],
		                                 "sPdfMessage": "Data Entry Connection Report",
		                                 "sTitle": "Data Entry Connection Report"
						             },
						             {
							             "sExtends": "print",
							             "mColumns": [ 1, 2, 3, 4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25],
		                                 "sPdfMessage": "Data Entry Connection Report",
		                                 "sTitle": "Data Entry Connection Report"
						             }],
					
				},
				ajax : {
					url : "/wtms/report/dataEntryConnectionReport/search/result/",
					data : {
						'ward': ward
						}
				},
				"columns" : [{"sTitle" : "S.no", /*"data" : "hscNo", "mRender": function ( o,val,data, meta ) { return ''+(meta.row+1); }*/},
			             
		       
							  { "data" : "hscNo" , "title": "H.S.C NO" , "render": function ( data, type, full, meta ) {
									return '<a href="/wtms/viewDcb/consumerCodeWis/'+data+'" target="_blank">'+data+'</a>';
							    } },  
							  { "data" : "assessmentNo", "title": "PT Assessment Number"},
							  { "data" : "ownerName", "title": "Name of the Applicant"},
							  { "data" : "wardName", "title": "Zone / Ward / Block"},
							  { "data" : "locality", "title": "Locality"},
							  { "data" : "address", "title": "address"},
							  { 
								  "data" : "mobileNumber", "title": "Mobile Number","className": "text-center",
								  "render" : function(data, type, row) {
									  return (!data || parseInt(data)==0? "NA" : data);
								  }
							  },
							  { "data" : "email", "title": "Email"},
							  { "data" : "waterSource", "title": "WaterSource Type"},
							  { "data" : "propertyType", "title": "Property Type"},
							  { "data" : "applicationType", "title": "Application Type"},
							  { "data" : "connectionType", "title": "Connection Type"},
							  { "data" : "usageType", "title": "Usage Type"},
							  { "data" : "category", "title": "Category"},
							  { "data" : "pipeSizeInInch", "title": "H.S.C Pipe Size(Inches)","className": "text-center"},
							  { "data" : "aadharNumber", "title": "Aaadhar No"},
							  { "data" : "noOfPersons", "title": "No.Of Persons", "className": "text-right"},
							  { "data" : "noOfFloors", "title": "No of floors", "className": "text-right"},
							  { "data" : "sumpCapacity", "title": "Sump Capacity (Litres)", "className": "text-right"},
							  { "data" : "donationCharges", "title": "Donation Charges", "className": "text-right"},
							  { "data" : "connectionDate", "title": "Connection Date"},
							  { "data" : "monthlyFee", "title": "Monthly Fee", "className": "text-right"},
							  { "data" : "waterTaxDue", "title": "Water Charge Due", "className": "text-right"},
							  { "data" : "propertyTaxDue", "title": "Property Tax Due", "className": "text-right"}
							],
							  "footerCallback" : function(row, data, start, end, display) {
									var api = this.api(), data;
									if (data.length == 0) {
										jQuery('#report-footer').hide();
									} else {
										jQuery('#report-footer').show(); 
									}
								},
					            "fnDrawCallback": function ( oSettings ) {
					                if ( oSettings.bSorted || oSettings.bFiltered )
					                {
					                    for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ )
					                    {
					                        $('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html( i+1 );
					                    }
					                }
					            },
					            
								"aoColumnDefs" : []		
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