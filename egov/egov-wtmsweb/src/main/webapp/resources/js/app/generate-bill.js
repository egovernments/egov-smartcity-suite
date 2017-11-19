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
	$('#report-footer').hide();
	jQuery('#searchid').click(function(e) {
		if(validategeneratebill())
		loadingReport();
	});
	
	
	
jQuery('#mergeid').click(function(e) {
	if(validategeneratebill())
	merge();
});


jQuery('#zipid').click(function(e) {
	if(validategeneratebill())
	zipanddownload();
});


});



function validategeneratebill()
{
	var isFilled=false;
	$('input[type=text], select').each(function(){
	    if($(this).val())
	    {
	    	console.log('value is ->'+$(this).val());
	    	isFilled=true;
	    }
	});
	
	if(!isFilled)
	{
		
		bootbox.alert('Enter any one search criteria');
		return false;
	}
	
	return true;

}


function merge()
{
	
	var params={
			
			'zone': $("#zone").val(),
			'revenueWard':$("#revenueWard").val(),
			'propertyType' :$("#propertyType").val(),
			'applicationType': $("#applicationType").val(),
			'connectionType': $("#connectionType").val(),
			'consumerCode': $("#consumerCode").val(),
			'houseNumber': $("#houseNumber").val(),
			'assessmentNumber': $("#assessmentNumber").val(),
			'start':0,
			'length':4000
		};
	
	window.open("/wtms/report/generateBill/search/mergeAndDownload"+obj_to_query(params), "_self");

}

function obj_to_query(obj) {
    var parts = [];
    for (var key in obj) {
        if (obj.hasOwnProperty(key)) {
            parts.push(encodeURIComponent(key) + '=' + encodeURIComponent(obj[key]));
        }
    }
    return "?" + parts.join('&');
}


function zipanddownload()
{
	
var params={
			'zone': $("#zone").val(),
			'revenueWard':$("#revenueWard").val(),
			'propertyType' :$("#propertyType").val(),
			'applicationType': $("#applicationType").val(),
			'connectionType': $("#connectionType").val(),
			'consumerCode': $("#consumerCode").val(),
			'houseNumber': $("#houseNumber").val(),
			'assessmentNumber': $("#assessmentNumber").val(),
			'start':0,
			'length':4000
		};
window.open("/wtms/report/generateBill/search/zipAndDownload"+obj_to_query(params), "_self");
	
}

function loadingReport()
{
		if($('form').valid()){
			var zone = $("#zone").val();
			var revenueWard = $("#revenueWard").val(); 
			var propertyType = $("#propertyType").val();
			var applicationType = $("#applicationType").val();
			var consumerCode = $("#consumerCode").val();
			var assessmentNumber = $("#assessmentNumber").val();
			var houseNumber = $("#houseNumber").val();
			var today = getdate();
			var connectionType = $("#connectionType").val();
			
			$('#warning-msg').addClass('hide');
			$('#generateBill-table_wrapper').removeClass('hide');
			
			oTable= $('#generateBill-table');
			var oDataTable=oTable.dataTable({
				"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
				"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
				"autoWidth": false,
				"bDestroy": true,
				"processing": true,
		        "serverSide": true,
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ 
					               {
						             "sExtends": "pdf",
						             "mColumns": [ 1, 2, 3, 4,5,6,7,8],
	                                 "sPdfMessage": "Generate Report as on "+today+"",
	                                 "sTitle": "Generate Report",
	                                 "sPdfOrientation": "landscape"
					                },
					                {
							             "sExtends": "xls",
							             "mColumns": [ 1,2,3,4,5,6,7,8],
		                                 "sPdfMessage": "Generate Report",
		                                 "sTitle": "Water Tax Defaulters Report"
						             },
						             {
							             "sExtends": "print",
							             "mColumns": [ 1,2,3,4,5,6,7,8],
		                                 "sPdfMessage": "Generate Report",
		                                 "sTitle": "Water Tax Defaulters Report"
						             }],
					
				},
				ajax : {
					url : "/wtms/report/generateBill/search/result",
					dataSrc:function(d){
						if(d.recordsCount>4000)
						{
							$('#warning-msg').removeClass('hide');
							$('#generateBill-table_wrapper').addClass('hide');
						}
						else
						{
							$('#warning-msg').addClass('hide');					
						}
						return d.data;
					},
					data : {
						'consumerCode': consumerCode,
						'zone': zone,
						'revenueWard':revenueWard,
						'propertyType' :propertyType,
						'applicationType': applicationType,
						'connectionType': connectionType,
						'consumerCode': consumerCode,
						'houseNumber': houseNumber,
						'assessmentNumber': assessmentNumber
					}
				},
				"columns" : [
				              {
				            	  "sTitle" : "S.no",
				            	  "render": function ( data, type, full, meta ) {
				            		  
								      return oTable.fnPagingInfo().iStart+meta.row+1;
								    }
				              },
							  { "data" : "hscNo" , 
								"title": "H.S.C NO",
								"render": function ( data, type, full, meta ) {
								      return '<a href="/wtms/report/generateBill/search/result/'+data+'" target="_blank">'+data+'</a>';
								    }
							  },  
							  { "data" : "ownerName", "title": "Owner Name"},
							  { "data" : "propertyId", "title": "Property Id"},
							  { "data" : "billNo", "title": "Bill No"},
							  { "data" : "billDate",
								  render: function (data, type, full) {
	        						if(data != undefined) {
	        							var regDateSplit = data.split("-");		
	        							return regDateSplit[2].substring(0,2) + "/" + regDateSplit[1] + "/" + regDateSplit[0];
	        						}
	        						else return "";
							     }, "title": "Bill Date"
							  },
							     
							  { "data" : "houseNo", "title": "House No"},
							  { "data" : "locality", "title": "Locality"},
							  { "data" : "connectionType", "title": "Connection Type"}
							
							],
							  "footerCallback" : function(row, data, start, end, display) {
									var api = this.api(), data;
									if (data.length == 0) {
										jQuery('#report-footer').hide();
									} else {
										jQuery('#report-footer').show(); 
									}
									
								},
					            "fnInitComplete": function() {
					            	if(oDataTable){ oDataTable.fnSort( [ [7,'desc'] , [3,'asc'] ] ); }
					            },
					            /*"fnDrawCallback": function ( oSettings ) {
					            	
					            	console.log('oSettings', oSettings);
					            	
					                 //Need to redo the counters if filtered or sorted 
					            	if ( oSettings.bSorted || oSettings.bFiltered )
					                {
					                    for ( var i=oSettings._iDisplayStart, iLen=(oSettings._iDisplayStart+oSettings.aiDisplay.length) ; i<iLen ; i++ )
					                    {
					                        $('td:eq(0)', oSettings.aoData[oSettings.aiDisplay[i]].nTr ).html( i+1 );
					                    }
					                }
					               
					            },*/
					            
								"aoColumnDefs" : [ {
								
									"mRender" : function(data, type, full) {
										return formatNumberInr(data);    
									}
								} ]		
					});
			
			
			e.stopPropagation();
		}
		
		function updateSerialNo()
		{
			$( "#generateBill-table tbody tr" ).each(function(index) {
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


jQuery.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings )
{
	return {
		"iStart":         oSettings._iDisplayStart,
		"iEnd":           oSettings.fnDisplayEnd(),
		"iLength":        oSettings._iDisplayLength,
		"iTotal":         oSettings.fnRecordsTotal(),
		"iFilteredTotal": oSettings.fnRecordsDisplay(),
		"iPage":          oSettings._iDisplayLength === -1 ?
			0 : Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
		"iTotalPages":    oSettings._iDisplayLength === -1 ?
			0 : Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
	};
};