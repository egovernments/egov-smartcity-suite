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
var revenueWardName;

jQuery(document).ready(function() {
	drillDowntableContainer = jQuery("#tbldcbdrilldown");
	jQuery('#report-backbutton').hide();
	setDefaultCourtCaseValue();
	jQuery('#btnsearch').click(function(e) {
		jQuery('#mode').val('ward');
		jQuery('#revenueWardName').val('');
		callAjaxByBoundary();
	});

	jQuery('#backButton').click(function(e) {
		var temp = jQuery('#selectedModeBndry').val();
		var valArray = temp.split('-');
		if (jQuery('#mode').val() == 'property') {
			if (valArray.length > 0) {
				var propVal = valArray[1].split('~');
				if (propVal.length > 0) {
					jQuery('#mode').val(propVal[0]);
					jQuery('#drillDownType').val(propVal[1]);
				}
				jQuery('#selectedModeBndry').val(valArray[0]);
			}
		} else if (jQuery('#mode').val() == 'block') {
			if (valArray.length > 0) {
				var blockVal = valArray[0].split('~');
				if (blockVal.length > 0) {
					jQuery('#mode').val(blockVal[0]);
					jQuery('#drillDownType').val(blockVal[1]);
				}
				jQuery('#selectedModeBndry').val('');
				jQuery('#revenueWardName').val('');
			}
		}
		callAjaxByBoundary();
	});

});

function setHiddenValueByLink(obj, param) {
	jQuery('input[name=' + jQuery(obj).data('hiddenele') + ']').val(
			jQuery(obj).data('eleval'));
	if (param.value == 'property') {
		window.open(
				"../view/viewDCBProperty-displayPropInfo.action?propertyId="
						+ jQuery('#drillDownType').val(), '',
				'scrollbars=yes,width=1000,height=700,status=yes');
	} else {
		if (param.value == 'ward') {
			jQuery('#mode').val("block");
			jQuery('#revenueWardName').val(jQuery(obj).data('eleval'));
		} else if (param.value == 'block') {
			jQuery('#mode').val("property");
		}
		callAjaxByBoundary();
	}
}

function setDefaultCourtCaseValue() {
	checkCourtCase(document.getElementById("courtCase"));
}

function checkCourtCase(obj) {
	if (obj.checked == true)
		document.getElementById("courtCase").value = 'true';
	else
		document.getElementById("courtCase").value = 'false';
}

function callAjaxByBoundary() {
	var temp = "";
	var modeVal = jQuery('#mode').val();
	var boundary_Id = "";
	var ward_id = "";
	var block_id = "";
	var courtCase = false;
	courtCase = jQuery('#courtCase').val();
	var year = jQuery('#year').val();
	var propertyType = jQuery('#propertyType').val();

	if (modeVal == 'ward') {
		boundary_Id = jQuery('#wardId').val();
		temp = modeVal + "~" + boundary_Id;
		jQuery('#selectedModeBndry').val(temp);
		jQuery('#report-backbutton').hide();
	} else {
		boundary_Id = jQuery('#drillDownType').val();
		temp = jQuery('#selectedModeBndry').val() + "-" + modeVal + "~"
				+ boundary_Id;
		jQuery('#selectedModeBndry').val(temp);
		jQuery('#report-backbutton').show();
	}

	if (modeVal == 'property') {
		ward_id = jQuery('#revenueWardName').val();
		block_id = boundary_Id;
	} else {
		ward_id = boundary_Id;
	}

	jQuery('.report-section').removeClass('display-hide');
	jQuery('#report-footer').show();
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/ptis/report/yearwisedcbreport/result",
					"contentType" : "application/json",
					"type" : "POST",
					"data" : function(d) {
						return JSON.stringify({
							"yearIndex" : year,
							"type" : modeVal,
							"block" : block_id,
							"revenueWard" : ward_id,
							"propertyUsage" : propertyType,
							"isCourtCase" : courtCase
						});
					},
					"dataSrc" : "yearWiseDCBResponse"
				},
				"bDestroy" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [
							{
								"sExtends" : "pdf",
								"sTitle" : jQuery('#pdfTitle').val(),
								"sPdfMessage" : "DCB Report",
							},
							{
								"sExtends" : "xls",
								"sPdfMessage" : "DCB Report",
								"sTitle" : jQuery('#pdfTitle').val(),
								"fnClick" : function(nButton, oConfig, oFlash) {
									reCalculateTotalFooterWhenExport('tbldcbdrilldown');
									this.fnSetText(oFlash, this
											.fnGetTableData(oConfig));
								}
							}, {
								"sExtends" : "print",
								"sPdfMessage" : "DCB Report",
								"sTitle" : jQuery('#pdfTitle').html(),
							} ],
				},
				"fnRowCallback" : function(row, data, index) {
				},
				columns : [
						{
							"data" : function(row, type, set, meta) {
								if (modeVal != 'property') {
									return {
										name : row.boundaryName,
										id : row.boundaryId
									};
								} else {
									return {
										name : row.assessmentNo,
										id : row.assessmentNo
									};
								}
							},
							"render" : function(data, type, row) {
								return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,mode);" data-hiddenele="drillDownType" data-eleval="'
										+ row.drillDownType
										+ '">'
										+ row.drillDownType + '</a>';
							},
							"sTitle" : "Name"
						}, {
							"data" : "doorNo",
							"sTitle" : "Door No"
						}, {
							"data" : "ownersName",
							"sTitle" : "Owner Name"
						}, {
							"data" : "count",
							"sTitle" : "Assessment Count"
						}, {
							"data" : "arrearDemand",
							"sTitle" : "Arrear Property Tax"
						}, {
							"data" : "arrearPenDemand",
							"sTitle" : "Penalty On Arrear"
						}, {
							"data" : "arrearTotalDemand",
							"sTitle" : "Arrear Total"
						}, {
							"data" : "currentDemand",
							"sTitle" : "Current Property Tax"
						}, {
							"data" : "currentPenDemand",
							"sTitle" : "Penalty On Current"
						}, {
							"data" : "currentTotalDemand",
							"sTitle" : "Current Total"
						}, {
							"data" : "totalDemand",
							"sTitle" : "Total Demand"
						}, {
							"data" : "arrearCollection",
							"sTitle" : "Arrear Property Tax"
						}, {
							"data" : "arrearPenCollection",
							"sTitle" : "Penalty On Arrear"
						}, {
							"data" : "arrearTotalCollection",
							"sTitle" : "Arrear Total"
						}, {
							"data" : "currentCollection",
							"sTitle" : "Current Property Tax"
						}, {
							"data" : "currentPenCollection",
							"sTitle" : "Penalty On Current"
						}, {
							"data" : "currentTotalCollection",
							"sTitle" : "Current Total"
						}, {
							"data" : "totalCollection",
							"sTitle" : "Total Collection"
						}, {
							"data" : "waivedoffAmount",
							"sTitle" : "Waived off Amount"
						}, {
							"data" : "arrearBalance",
							"sTitle" : "Arrear Property Tax"
						}, {
							"data" : "arrearPenBalance",
							"sTitle" : "Penalty On Arrear"
						}, {
							"data" : "currentBalance",
							"sTitle" : "Current Property Tax"
						}, {
							"data" : "currentPenBalance",
							"sTitle" : "Penalty On Current"
						}, {
							"data" : "totalBalance",
							"sTitle" : "Total PropertyTax Balance"
						} ],
				"footerCallback" : function(row, data, start, end, display) {
					var api = this.api(), data;
					if (data.length == 0) {
						jQuery('#report-footer').hide();
					} else {
						jQuery('#report-footer').show();
					}
					if (data.length > 0) {
						for (var i = 3; i <= 23; i++) {
							updateTotalFooter(i, api);
						}
					}
				},
				"aoColumnDefs" : [ {
					"aTargets" : [ 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
							16, 17, 18, 19, 20, 21, 22 ],
					"mRender" : function(data, type, full) {
						return formatNumberInr(data);
					}
				} ]
			});

	if (jQuery("#mode").val() === "property") {
		reportdatatable.fnSetColumnVis(1, true);
		reportdatatable.fnSetColumnVis(2, true);
		reportdatatable.fnSetColumnVis(3, false);
	} else {
		reportdatatable.fnSetColumnVis(1, false);
		reportdatatable.fnSetColumnVis(2, false);
	}

}

function updateTotalFooter(colidx, api) {
	// Remove the formatting to get integer data for summation
	var intVal = function(i) {
		return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
				: typeof i === 'number' ? i : 0;
	};

	// Total over all pages
	var total = api.column(colidx).data().reduce(function(a, b) {
		return intVal(a) + intVal(b);
	});

	// Total over this page
	var pageTotal = api.column(colidx, {
		page : 'current'
	}).data().reduce(function(a, b) {
		return intVal(a) + intVal(b);
	}, 0);

	// Update footer
	jQuery(api.column(colidx).footer()).html(
			formatNumberInr(pageTotal) + ' (' + formatNumberInr(total) + ')');
}

//inr formatting number
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