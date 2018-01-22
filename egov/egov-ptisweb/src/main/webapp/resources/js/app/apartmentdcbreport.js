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


jQuery(document).ready(function() { 
	drillDowntableContainer = jQuery("#tbldcbdrilldown");
	jQuery('#report-backbutton').hide();
	jQuery('#btnsearch').click(function(e) {
		$("#dcbError").hide();
		if(jQuery('#wardId').val()==""){
			$("#dcbError").html('Please select ward');
			$("#dcbError").show();
			return false;
		}
		if(jQuery('#apartment').val()==""){
			$("#dcbError").html('Please select apartment');
			$("#dcbError").show();
			return false;
		}
		jQuery('#mode').val('apartment');
		callAjaxByBoundary();
	});
	
	jQuery('#backButton').click(function(e) {
		var temp=jQuery('#selectedModeBndry').val();
		var valArray=temp.split('-');
		if(jQuery('#mode').val()=='property'){
			if(valArray.length>0){
				var propVal=valArray[0].split('~');
				if(propVal.length>0){
					jQuery('#mode').val(propVal[0]);
					jQuery('#apartment').val(propVal[1]);
				}
				jQuery('#selectedModeBndry').val(valArray[0]);
			}
		} else{
			if(valArray.length>0){
				var blockVal=valArray[0].split('~');
				if(blockVal.length>0){
					jQuery('#mode').val(blockVal[0]);
					jQuery('#boundaryId').val(blockVal[1]);
				}
			jQuery('#selectedModeBndry').val('');
		}
		}
		callAjaxByBoundary(); 
	});

});

function setHiddenValueByLink(obj, param) {
	jQuery('input[name=' + jQuery(obj).data('hiddenele') + ']')
	.val(jQuery(obj).data('eleval'));   
	if(param.value=='property'){
		window.open("../view/viewDCBProperty-displayPropInfo.action?propertyId="+jQuery('#apartmentId').val(), '', 'scrollbars=yes,width=1000,height=700,status=yes');
	} else{
	  if(param.value=='apartment'){
			jQuery('#mode').val("property");
		} 
		callAjaxByBoundary(); 
	}
}

function callAjaxByBoundary() {
	var temp="";
	var modeVal = jQuery('#mode').val(); 
	var apartment_Id = "";
	var ward_Id = jQuery('#wardId').val();
	if(modeVal=='apartment'){	
		apartment_Id = jQuery('#apartment').val();
		temp=modeVal+"~"+apartment_Id;
		jQuery('#selectedModeBndry').val(temp);
		jQuery('#report-backbutton').hide();
	}
	else{
		apartment_Id = jQuery('#apartmentId').val();
		temp=jQuery('#selectedModeBndry').val()+"-"+modeVal+"~"+apartment_Id;
		jQuery('#selectedModeBndry').val(temp);
		jQuery('#report-backbutton').show();
	}
	jQuery('.report-section').removeClass('display-hide');
	jQuery('#report-footer').show();
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/ptis/report/apartmentdcbreport/result",      
					data : {
						mode : modeVal,
						boundaryId : ward_Id, 
						apartmentId : apartment_Id,
					}
				},
				"bDestroy" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ 
					               {
						             "sExtends": "pdf",
						             "sTitle": jQuery('#pdfTitle').val(),
	                                 "sPdfMessage": "DCB Report",
					                },
					                {
							             "sExtends": "xls",
		                                 "sPdfMessage": "DCB Report",
		                                 "sTitle": jQuery('#pdfTitle').val(),
		                                 "fnClick": function ( nButton, oConfig, oFlash ) {
	                            	    	 reCalculateTotalFooterWhenExport('tbldcbdrilldown');
	                            		     this.fnSetText(oFlash, this.fnGetTableData(oConfig));
	                            		 }
						             },{
							             "sExtends": "print",
		                                 "sPdfMessage": "DCB Report",
		                                 "sTitle": jQuery('#pdfTitle').html(),
						               }],
				},"fnRowCallback" : function(row, data, index) {
				},
				columns : [{
							"data" : function(row, type, set, meta){
								if(modeVal!='property'){
									return { name:row.apartmentName, id:row.apartmentId };
								}
								else {
									return { name:row.assessmentNo, id:row.assessmentNo };
								}
							},
							"render" : function(data, type, row) {
								return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,mode);" data-hiddenele="apartmentId" data-eleval="'
										+ data.id + '">' + data.name + '</a>';
							},
							"sTitle" : "Name"
						}, {
							"data" : "houseNo",
							"sTitle" : "Door No"
						}, {
							"data" : "ownerName",
							"sTitle" : "Owner Name"
						}, {
							"data" : "assessmentCount",
							"sTitle" : "Assessment Count"
						}, {
							"data" : "dmndArrearPT",
							"sTitle" : "Arrear Property Tax"
						}, {
							"data" : "dmndArrearPFT",
							"sTitle" : "Penalty On Arrear"
						}, {
							"data" : "dmndArrearTotal",
							"sTitle" : "Arrear Total"
						}, {
							"data" : "dmndCurrentPT",
							"sTitle" : "Current Property Tax"
						}, {
							"data" : "dmndCurrentPFT",
							"sTitle" : "Penalty On Current"
						}, {
							"data" : "dmndCurrentTotal",
							"sTitle" : "Current Total"
						}, {
							"data" : "totalDemand",
							"sTitle" : "Total Demand"
						}, {
							"data" : "clctnArrearPT",
							"sTitle" : "Arrear Property Tax"
						}, {
							"data" : "clctnArrearPFT",
							"sTitle" : "Penalty On Arrear"
						}, {
							"data" : "clctnArrearTotal",
							"sTitle" : "Arrear Total"
						}, {
							"data" : "clctnCurrentPT",
							"sTitle" : "Current Property Tax"
						}, {
							"data" : "clctnCurrentPFT",
							"sTitle" : "Penalty On Current"
						}, {
							"data" : "clctnCurrentTotal",
							"sTitle" : "Current Total"
						}, {
							"data" : "totalCollection",
							"sTitle" : "Total Collection"
						}, {
							"data" : "balArrearPT",
							"sTitle" : "Arrear Property Tax"
						}, {
							"data" : "balArrearPFT",
							"sTitle" : "Penalty On Arrear"
						}, {
							"data" : "balCurrentPT",
							"sTitle" : "Current Property Tax"
						}, {
							"data" : "balCurrentPFT",
							"sTitle" : "Penalty On Current"
						}, {
							"data" : "totalPTBalance",
							"sTitle" : "Total PropertyTax Balance"
						}],
				"footerCallback" : function(row, data, start, end, display) {
					var api = this.api(), data;
					if (data.length == 0) {
						jQuery('#report-footer').hide();
					} else {
						jQuery('#report-footer').show();
					}
					if (data.length > 0) {
						for(var i=3;i<=22;i++)
						{
						  updateTotalFooter(i, api);	
						}
					}
				}, 
				"aoColumnDefs" : [ {
					"aTargets" : [3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22], 
					"mRender" : function(data, type, full) {
						return formatNumberInr(data);    
					}
				} ]
			});
	
			if(jQuery("#mode").val() === "property")
			{
				reportdatatable.fnSetColumnVis( 1, true );
				reportdatatable.fnSetColumnVis( 2, true );
				reportdatatable.fnSetColumnVis( 3, false );
			}
			else
			{
				reportdatatable.fnSetColumnVis( 1, false );
				reportdatatable.fnSetColumnVis( 2, false );
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
			formatNumberInr(pageTotal) + ' (' + formatNumberInr(total)
					+ ')');
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