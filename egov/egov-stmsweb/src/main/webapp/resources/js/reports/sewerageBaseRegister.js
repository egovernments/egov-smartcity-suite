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


var tableContainer;
jQuery(document).ready(function($) {
	$('#baseregisterheader').hide();
	$("#report-footer").hide();
	tableContainer = $("#aplicationSearchResults");
	 document.onkeydown=function(evt){
		 var keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
	if(keyCode == 13){
		submitButton();	
	}
	 }
	 
	 $('.slide-history-menu').click(function(){
			$(this).parent().find('.history-slide').slideToggle();
			if($(this).parent().find('#toggle-his-icon').hasClass('fa fa-angle-down'))
			{
				$(this).parent().find('#toggle-his-icon').removeClass('fa fa-angle-down').addClass('fa fa-angle-up');
				//$('#see-more-link').hide();
				}else{
				$(this).parent().find('#toggle-his-icon').removeClass('fa fa-angle-up').addClass('fa fa-angle-down');
				//$('#see-more-link').show();
			}
		}); 
});

var recordTotal = [];
function getSumOfRecords() {

    $.ajax({
        url: "/stms/reports/seweragebaseregister/grand-total?" + $("#sewerageBaseRegisterForm").serialize(),
        type: 'GET',
        data : 
        	$("#sewerageBaseRegisterForm").serialize(),
        success: function (data) {
            recordTotal = [];
            for (var i = 0; i < data.length; i++) {
                recordTotal.push(data[i]);
            }
        }
    })
}

$(".btnSearch").click(function(event) {
	var wardName = $('#ward').val();
	if (wardName == '') {
		bootbox.alert("Please select ward name");
		return false;
	} else {
		submitButton();
		return true;
	}
});


var prevdatatable;
function submitButton() {
	var oTable = $("#aplicationSearchResults");
	oTable.show();
	$("#report-footer").show();
	$('#baseregisterheader').show();
	
	if(prevdatatable){
		prevdatatable.fnClearTable();
		$('#aplicationSearchResults thead tr').remove();
	}
	
	var wardsList = $('#ward').val();
	var temp=null;
	if(wardsList!=null && wardsList!="undefined"){
		if(wardsList.length>0){
			for(var i=0;i<wardsList.length;i++){
				if(temp===null){
					temp=wardsList[i];
				}
				else{
					temp=temp+"~"+wardsList[i];
				}
			}
		}
	}
	var wards = [];
    $('#ward option').each(function(i) {
            if (this.selected == true) {
            	wards.push(this.value);
            }
    });
	prevdatatable = oTable.dataTable({
        dom: "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-2 col-xs-6 text-right'B><'col-md-5 col-xs-6 text-right'p>>",
		processing: true,
        serverSide: true,
        sort: true,
        filter: true,
        "searching": false,
        "order": [[0, 'asc']],
        "autoWidth": false,
        "bDestroy": true,
		 buttons: [
		            {
		                text: 'PDF',
		                action: function (e, dt, node, config) {
		                    window.open("seweragebaseregister/download?" + $("#sewerageBaseRegisterForm").serialize() + "&printFormat=PDF", '', 'scrollbars=yes,width=1300,height=700,status=yes');
		                }
		            },
		            {
		                text: 'XLS',
		                action: function (e, dt, node, config) {
		                    window.open("seweragebaseregister/download?" + $("#sewerageBaseRegisterForm").serialize() + "&printFormat=XLS", '_self');
		                }
		            }],
		            ajax : {
					url : "/stms/reports/baseregisterresult/",
					type : 'POST',
					beforeSend : function() {
						$('.loader-class').modal('show', {
							backdrop : 'static'
						});
					},
					data : function(args) {
						return {
							"args" : JSON.stringify(args),

							"mode" : temp
						};
					},
					complete : function() {
						$('.loader-class').modal('hide');
					}
				},	
		columns : [ {
					"sTitle" : "S.H.S.C Number",
					"name" : "shscNumber",
					"data" : "shscNumber"
				}, {
					"sTitle" : "assesmentno",
					"data" : "assementNo",
					"name" : "assementNo"
				}, {
					"sTitle" : 'ownerName',
					"data" : 'ownerName',
					"name" : "ownerName"
				}, {
					"sTitle" : "ward",
					"data" : "revenueWard",
					"name" : "revenueWard"
				}, {
					"sTitle" : "doorNo",
					"data" : "doorNo",
					"name" : "doorNo"
				}, {
					"sTitle" : "Property type",
					"data" : "propertyType",
					"name" : "propertyType"
				}, {
					"sTitle" : "Residential closets",
					"className": "text-center",
					"data" : "residentialClosets",
					"render" : function(data, type, row, meta) {
						if (row.residentialClosets) {
							return row.residentialClosets;
						} else {
							return 'N/A'
						}
					},
					"name" : "residentialClosets"
				}, {
					"sTitle" : "Non-Residential closets",
					"className" : "text-center",
					"data" : "nonResidentialClosets",
					"render" : function(data, type, row, meta) {
						if (row.nonResidentialClosets) {
							return row.nonResidentialClosets;
						} else {
							return 'N/A'
						}
					},
					"name" : "nonResidentialClosets"
				}, {
					"title" : "Period",
					"data" : "period",
					"name" : "period"
				}, {
					"sTitle" : "Arrears Demand",
					"className": "text-right",
					"data" : "arrears",
					"name" : "arrears"
				}, {
					"sTitle" : "Current Tax Demand",
					"className": "text-right",
					"data" : "currentDemand",
					"name" : "currentDemand"
				}, {
					"sTitle" : "Total Demand",
					"className": "text-right",
					"data" : "totalDemand",
					"name" : "totalDemand"
				}, {
					"sTitle" : "Arrears Collected",
					"className": "text-right",
					"data" : "arrearsCollected",
					"name" : "arrearsCollected"
				}, {
					"sTitle": "Current Tax Collected",
					"className": "text-right",
					"data" : "currentTaxCollected",
					"name" : "currentTaxCollected"
				}, {
					"sTitle" : "Total Tax Collected",
					"className": "text-right",
					"data" : "totalTaxCollected",
					"name" : "totalTaxCollected"
				}, {
					"sTitle" : "Advance amount Collected",
					"className": "text-right",
					"data" : "advanceAmount",
					"name" : "advanceAmount"
				}, ],
		
		"footerCallback" : function(row, data, start, end, display) {
			var api = this.api(), data;
			if (data.length == 0) {
				$('#report-footer').hide();
			} else {
				$('#report-footer').show();
			}
			if (data.length > 0) {
				updateTotalFooter(9, api);
				updateTotalFooter(10, api);
				updateTotalFooter(11, api);
				updateTotalFooter(12, api);
				updateTotalFooter(13, api);
				updateTotalFooter(14, api);
				updateTotalFooter(15, api);

			}
		},
		"aoColumnDefs" : [ {
			"aTargets" : [9, 10, 11, 12,13,14,15 ],
			"mRender" : function(data, type, full) {
				return formatNumberInr(data);
			}
		} ]
				});
	
	var table = $('#aplicationSearchResults').DataTable();
    var info = table.page.info();
    if (info.start == 0)
        getSumOfRecords();
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
	$(api.column(colidx).footer()).html(
			formatNumberInr(pageTotal) + ' (' + formatNumberInr(total)
					+ ')');
}


// inr formatting number
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
