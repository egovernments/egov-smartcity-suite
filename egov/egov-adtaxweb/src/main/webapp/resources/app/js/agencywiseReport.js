
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

$(document).ready(function(){
        try{
            $.fn.dataTable.moment( 'DD/MM/YYYY' );
        }catch(e){
           
        }
        var agency = new Bloodhound({
            datumTokenizer: function (datum) {
                return Bloodhound.tokenizers.whitespace(datum.value);
            },
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '../agency/agencies?name=%QUERY',
                filter: function (data) {
                    return $.map(data, function (ct) {
                        return {
                            name: ct.name,
                            value: ct.id
                        };
                    });
                }
            }
        });
       
        $('#reset').click(function(){
        	$('#agencyTypeAhead').typeahead('val', '');
        	$('#agencyId').val('');
        });
   
   
    agency.initialize();
    var agency_typeahead=$('#agencyTypeAhead').typeahead({
        hint : true,
        highlight : true,
        minLength : 1
    }, {
        displayKey : 'name',
        source : agency.ttAdapter()
    });
    typeaheadWithEventsHandling(agency_typeahead, '#agencyId');
    });

   
$('#categories').change(function(){
	$.ajax({
		url: "/adtax/hoarding/getsubcategories-by-category",
		type: "GET",
		data: {
			categoryId : $('#categories').val()   
		},
		dataType: "json",
		success: function (response) {
			$('#subcategories').empty();
			$('#subcategories').append($("<option value=''>Select from below</option>"));
			$.each(response, function(index, value) {
				$('#subcategories').append($('<option>').text(value.description).attr('value', value.id));
			});
		}, 
		error: function (response) {
		}
	});
});

$('#agencyReportTable').dataTable({
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"aoColumnDefs": [ { "bSortable": false, "aTargets": [0]}],
		"autoWidth": false,
		"bDestroy": true
});


$('#zoneList').change(function(){
	$.ajax({
		type: "GET",
		url: "/egi/boundary/ajaxBoundary-blockByLocality",
		cache: true,
		dataType: "json",
		data:{
			locality : $('#zoneList').val()
	  	   },
		success: function (response) {
			$('#wardlist').empty();
			$('#wardlist').append($('<option>').text('Select from below').attr('value', ""));
			$.each(response.results.boundaries, function (j, boundary) {
				if (boundary.wardId) {
						$('#wardlist').append($('<option>').text(boundary.wardName).attr('value', boundary.wardId))
				}
			});
		}, 
		error: function (response) {
		}
	});
});

$('#wardlist').change(function(){
	$("#wardId").val($('#wardlist').val());    
});

var prevdatatable;
$('#searchagencywise').click(function(e){
	$('.report-section').removeClass('display-hide');
		oTable= $('#adtax_searchagencywiserecord');
		if(prevdatatable)
		{
			prevdatatable.fnClearTable();
			$('#adtax_searchagencywiserecord thead tr').remove();
		}
			prevdatatable = oTable.dataTable({
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
			ajax : {
				url : "/adtax/reports/getAgencyWiseDcb",
				type : "get",
				beforeSend : function() {
					$('.loader-class').modal('show', {
						backdrop : 'static'
					});
				},
				"data" : getFormData($('form')),
				complete : function() {
					$('.loader-class').modal('hide');
				}
			},
				"columns" : [
							  { 
							  	
								  "data" : function(row, type, set, meta) {
										return {
											name : row.agencyName,
											id : row.agency,
										};
									},
								  "render" : function(data, type, row, meta) 
								  {
									return '<a href="javascript:void(0);" onclick="reportFunction('+data.id+');" >'+data.name+'</a> ';
								   },
								   "title": "Agency"
							  },
							  { "data" : "totalHoardingInAgency", "title": "No.of hoarding"},
							  { "data" : "penaltyAmount", "title": "Penalty Amount"},
							  { "data" : "additionalTaxAmount", "title": "Additional Tax (Service Tax and Cesses)"},							  
							  { "data" : "totalDemand", "title": "TotalDemand"},
							  { "data" : "collectedAmount", "title": "Collected Amount"},
							  { "data" : "pendingAmount", "title": "Pending Amount"},
							  ],
								"footerCallback" : function(row, data, start, end, display) {
									var api = this.api(), data;
									if (data.length == 0) {
										jQuery('#report-footer').hide();
									} else {
										jQuery('#report-footer').show(); 
									}
									if (data.length > 0) {
										updateTotalFooter(1, api);
										updateTotalFooter(2, api);
										updateTotalFooter(3, api);
										updateTotalFooter(4, api);
										updateTotalFooter(5, api);
										updateTotalFooter(6, api);
										}
								},
								"aoColumnDefs" : [ {
									"aTargets" : [1,2,3,4,5,6],
									"mRender" : function(data, type, full) {
										return formatNumberInr(data);    
									}
								} ]	
					});
		e.stopPropagation();
		e.preventDefault();
	});

function reportFunction(id)
{
	var category=document.getElementById("categories").value;
	var subcategory=document.getElementById("subcategories").value;
	var zone = document.getElementById("zoneList").value;
	var ward = document.getElementById("wardlist").value;
	var ownerDetail = document.getElementById("ownerDetail").value;
	window.open("/adtax/reports/report-view?id="+id+"&category="+category+"&subcategory="+subcategory+"&zone="+zone+"&ward="+ward+"&ownerDetail="+ownerDetail,'_blank',"width=800, height=600 , scrollbars=yes");
}

function updateTotalFooter(colidx, api) {
	// Remove the formatting to get integer data for summation
	var intVal = function(i) {
		return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
				: typeof i === 'number' ? i : 0;
	};

	// Total over all pages

	if (api.column(colidx).data().length) {
		var total = api.column(colidx).data().reduce(function(a, b) {
			return intVal(a) + intVal(b);
		})
	} else {
		total = 0
	}
	;

	// Total over this page

	if (api.column(colidx).data().length) {
		var pageTotal = api.column(colidx, {
			page : 'current'
		}).data().reduce(function(a, b) {
			return intVal(a) + intVal(b);
		})
	} else {
		pageTotal = 0
	}
	;

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

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}