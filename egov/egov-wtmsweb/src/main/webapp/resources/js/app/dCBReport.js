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

/*
 * Note : Property "selectedModeBndry" used to traverse forward and backward. 
 * 1.ondrilldown at each level(ie zone/ward/block/property) this property value gets updated with the concatenated values of mode and boundary.
 * 2. Value format : mode~boundaryId. 
 * 3. Ex: zone~1 (At 1st level), zone~1-ward~6-block~8-property~10 (At last level). 
 * 4. property~10 means show all properties under block with id 10 / block~8 means show all blocks under ward id 8 and so on.  		
 */

var reportdatatable;

$(document).ready(
		function(e) {
			drillDowntableContainer = $("#tbldcbdrilldown");
			$('#report-backbutton').hide();
			$('form').submit(function(e) {
				if ($('#reportType').val() == 'zoneWise') {
					$('#mode').val("zone");
				} else if ($('#reportType').val() == 'wardWise') {
					$('#mode').val("ward");
				} else if ($('#reportType').val() == 'blockWise') {
					$('#mode').val("block");
				}else if ($('#reportType').val() == 'localityWise') {
					$('#mode').val("locality");
				}

				callAjaxByBoundary(e);
			});

			$('#backButton').click(
					function(e) {
						var temp = $('#selectedModeBndry').val();
						var valArray = temp.split('-');
						if ($('#reportType').val() == 'zoneWise') {
							if ($('#mode').val() == 'property') {
								if (valArray.length > 0) {
									var propVal = valArray[2].split('~');
									if (propVal.length > 0) {
										$('#mode').val(propVal[0]);
										$('#boundaryId').val(propVal[1]);
									}
									$('#selectedModeBndry').val(
											valArray[0] + "-" + valArray[1]);
								}
							} else if ($('#mode').val() == 'block') {
								if (valArray.length > 0) {
									var blockVal = valArray[1].split('~');
									if (blockVal.length > 0) {
										$('#mode').val(blockVal[0]);
										$('#boundaryId').val(blockVal[1]);
									}
									$('#selectedModeBndry').val(valArray[0]);
								}
							} else if ($('#mode').val() == 'ward') {
								if (valArray.length > 0) {
									var wardVal = valArray[0].split('~');
									if (wardVal.length > 0) {
										$('#mode').val(wardVal[0]);
										$('#boundaryId').val(wardVal[1]);
									}
									$('#selectedModeBndry').val('');
								}
							}
						} else if ($('#reportType').val() == 'wardWise') {
							if ($('#mode').val() == 'property') {
								if (valArray.length > 0) {
									var propVal = valArray[1].split('~');
									if (propVal.length > 0) {
										$('#mode').val(propVal[0]);
										$('#boundaryId').val(propVal[1]);
									}
									$('#selectedModeBndry').val(
											valArray[0] + "-" + valArray[1]);
								}
							} else if ($('#mode').val() == 'block') {
								if (valArray.length > 0) {
									var blockVal = valArray[0].split('~');
									if (blockVal.length > 0) {
										$('#mode').val(blockVal[0]);
										$('#boundaryId').val(blockVal[1]);
									}
									$('#selectedModeBndry').val(valArray[0]);
								}
							}
						} else if ($('#reportType').val() == 'blockWise') {
							if ($('#mode').val() == 'property') {
								if (valArray.length > 0) {
									var propVal = valArray[0].split('~');
									if (propVal.length > 0) {
										$('#mode').val(propVal[0]);
										$('#boundaryId').val(propVal[1]);
									}
									$('#selectedModeBndry').val(
											valArray[0] + "-" + valArray[1]);
								}
							}
						}else if ($('#reportType').val() == 'localityWise') {
							if ($('#mode').val() == 'property') {
								if (valArray.length > 0) {
									var propVal = valArray[0].split('~');
									if (propVal.length > 0) {
										$('#mode').val(propVal[0]);
										$('#boundaryId').val(propVal[1]);
									}
									$('#selectedModeBndry').val(
											valArray[0] + "-" + valArray[1]);
								}
							}
						}
						callAjaxByBoundary(e);
					});

		});

function setHiddenValueByLink(obj, param, event, boundaryId) {
	$('input[name=' + $(obj).data('hiddenele') + ']')
			.val($(obj).data('eleval'));
	if (param.value == 'property') {
		window.open("/wtms/viewDcb/consumerCodeWis/"
				+ boundaryId, '',
				'scrollbars=yes,width=1000,height=700,status=yes');
	} else {
		$('#boundaryId').val(boundaryId);
		if (param.value == 'zone') {
			$('#mode').val("ward");
		} else if (param.value == 'ward') {
			$('#mode').val("block");
		} else if (param.value == 'block') {
			$('#mode').val("property");
		} else if (param.value == 'locality') {
			$('#mode').val("property");
		}
		callAjaxByBoundary(event);
	}
}

function callAjaxByBoundary(event) {
	var modeVal = "";
	var boundary_Id = "";
	var connectiontype = "";
	var selectedModeBndry = "";
	var reportType = "";
	var temp = "";
	modeVal = $('#mode').val();
	connectiontype = $('#connectionType').val();
	reportType = $('#reportType').val();
	if ($('#reportType').val() == 'zoneWise' && $('#mode').val() == 'zone') {
		boundary_Id = $('#zones').val();
		temp = modeVal + "~" + boundary_Id;
		$('#selectedModeBndry').val(temp);
		$('#report-backbutton').hide();
	} else if ($('#reportType').val() == 'wardWise'
			&& $('#mode').val() == 'ward') {
		boundary_Id = $('#wards').val();
		temp = modeVal + "~" + boundary_Id;
		$('#selectedModeBndry').val(temp);
		$('#report-backbutton').hide();
	} else if ($('#reportType').val() == 'blockWise'
			&& $('#mode').val() == 'block') {
		boundary_Id = $('#blocks').val();
		temp = modeVal + "~" + boundary_Id;
		$('#selectedModeBndry').val(temp);
		$('#report-backbutton').hide();
	} else if ($('#reportType').val() == 'localityWise'
			&& $('#mode').val() == 'locality') {
		boundary_Id = $('#localitys').val();
		temp = modeVal + "~" + boundary_Id;
		$('#selectedModeBndry').val(temp);
		$('#report-backbutton').hide();
	}else {
		boundary_Id = $('#boundaryId').val();
		temp = $('#selectedModeBndry').val() + "-" + modeVal + "~"
				+ boundary_Id;
		$('#selectedModeBndry').val(temp);
		$('#report-backbutton').show();
	}
	selectedModeBndry = $('#selectedModeBndry').val();
	$('.report-section').removeClass('display-hide');
	$('#report-footer').show();
	event.preventDefault();
	var reportdatatable = drillDowntableContainer
			.dataTable({
				type : 'GET',
				responsive : true,
				destroy : true,
				ajax : {
					url : "/wtms/reports/dCBReportList",
					data : {
						'mode' : modeVal,
						'boundaryId' : boundary_Id,
						'connectionType' : connectiontype,
						'selectedModeBndry' : selectedModeBndry,
						'reportType' : reportType
					},
					
					dataSrc: function ( json ) {  
						jQuery('.loader-class').modal('hide'); 
						return json.data;
					}       
				},
				"autoWidth" : false,
				"bDestroy" : true,
				"bProcessing" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ "xls"
					               , {
							             "sExtends": "pdf",
							             "mColumns": [0, 2, 3, 4,5,6,7,8,9,10],
		                                
		                                 
		                                 "sPdfOrientation": "landscape"
						                },
					               "print" ]
				},
				columns : [
						{
							"data" : function(row, type, set, meta) {
								if (modeVal != 'property') {
									return {
										name : row.boundaryName,
										id : row.id
									};
								} else {
									return {
										name : row.hscno,
										id : row.hscno
									};
								}
							},
							"render" : function(data, type, row) {
								return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,mode,event,'
										+ data.id
										+ ');" data-hiddenele="boundaryId" data-eleval="'
										+ data.id + '">' + data.name + '</a>';
							},
							"sTitle" : updateTitle()
						},{
							"data" : "no_of_users",
							"sTitle" : "No.of Consumer No.",
							"className": "text-right"
						},{
							"data" : "arr_demand",
							"sTitle" : "Arrears",
							"className": "text-right"
						},{
							"data" : "curr_demand",
							"sTitle" : "Current",
							"className": "text-right"
						}, {
							"data" : "total_demand",
							"sTitle" : "Total",
							"className": "text-right"
						}, {
							"data" : "arr_coll",
							"sTitle" : "Arrears",
							"className": "text-right"
						}, {
							"data" : "curr_coll",
							"sTitle" : "Current",
							"className": "text-right"
						}, {
							"data" : "total_coll",
							"sTitle" : "Total",
							"className": "text-right"
						}, {
							"data" : "arr_balance",
							"sTitle" : "Arrears",
							"className": "text-right"
						}, {
							"data" : "curr_balance",
							"sTitle" : "Current",
							"className": "text-right"
						}, {
							"data" : "total_balance",
							"sTitle" : "Total",
							"className": "text-right"
						} ],
				"footerCallback" : function(row, data, start, end, display) {
					var api = this.api(), data;
					if (data.length == 0) {
						$('#report-footer').hide();
					} else {
						$('#report-footer').show();
					}
					if (data.length > 0) {
						updateTotalFooter(2, api);
						updateTotalFooter(3, api);
						updateTotalFooter(4, api);
						updateTotalFooter(5, api);
						updateTotalFooter(6, api);
						updateTotalFooter(7, api);
						updateTotalFooter(8, api);
						updateTotalFooter(9, api);
						updateTotalFooter(10, api);

					}
				},
				"aoColumnDefs" : [ {
					"aTargets" : [ 2, 3, 4, 5, 6, 7, 8, 9, 10 ],
					"mRender" : function(data, type, full) {
						return formatNumberInr(data);
					}
				} ]
			});
	
	    if ($('#mode').val() == 'property') {
	    	reportdatatable.fnSetColumnVis(1, false);
	    }
	    else
    	{
	       reportdatatable.fnSetColumnVis(1, true);
    	}

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
			formatNumberInr(pageTotal) + ' (' + formatNumberInr(total) + ')');
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

function updateTitle() {
	if($("#mode").val()==="zone")
		return "Zone";
	else if($("#mode").val()==="ward")
		return "Ward";
	else if($("#mode").val()==="block")
		return "Block";
	else if($("#mode").val()==="property")
		return "Consumer Number";
}