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
			url: '../agency/active-agencies?name=%QUERY',
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
	
   agency.initialize(); // Instantiate the Typeahead UI
	 
	/*$('.typeahead').typeahead({
		  hint: true,
		  highlight: true,
		  minLength: 1
		}, {
		displayKey: 'name',
		source: agency.ttAdapter()
	}).on('typeahead:selected typeahead:autocompleted typeahead:matched', function(event, data){
		$("#agencyId").val(data.value);    
	   }).on('change',function(event,data){
   		if($('#agencyTypeAhead').val() == ''){
   			$("#agencyId").val(''); //reset hidden element value
   	    }
   });*/
	
	
	/*$('#subcategories').change(function(){
		$("#subCategoryId").val($('#subcategories').val());    
	});*/
	
   var agency_typeahead=$('#agencyTypeAhead').typeahead({
	   hint:true,
	   highlight:true,
	   minLength:1
   },
   {
	   displayKey : 'name',
	   source: agency.ttAdapter()
   });
   typeaheadWithEventsHandling(agency_typeahead,'#agencyId');
   
	$('#zoneList').change(function(){
		$.ajax({
			type: "GET",
			url: "/egi/boundary/ajaxBoundary-blockByLocality.action",
			cache: true,
			dataType: "json",
			data:{
				locality : $('#zoneList').val()
		  	   },
			success: function (response) {
				console.log("success"+response);
				$('#wardlist').empty();
				$('#wardlist').append($('<option>').text('Select from below').attr('value', ""));
				$.each(response.results.boundaries, function (j, boundary) {
					if (boundary.wardId) {
							$('#wardlist').append($('<option>').text(boundary.wardName).attr('value', boundary.wardId))
					}
				});
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	});
	
	$('#wardlist').change(function(){
		$("#wardId").val($('#wardlist').val());    
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
				console.log("success"+response);
				//$("#category").val($('#categories').val());    
				$('#subcategories').empty();
				$('#subcategories').append($("<option value=''>Select from below</option>"));
				$.each(response, function(index, value) {
					$('#subcategories').append($('<option>').text(value.description).attr('value', value.id));
				});
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	});
	
	var prevdatatable;
	
	$('#search').click(function(e){
		oTable= $('#adtax_search');
		var radioValue = $("input[name='searchType']:checked").val();
		var radioBtnVal = radioValue.replace(/^"?(.+?)"?$/,'$1'); 

	    console.log('radio button value is -> '+radioBtnVal);
		
		if(prevdatatable)
		{
			prevdatatable.fnClearTable();
			$('#adtax_search thead tr').remove();
		}
		
		if(radioBtnVal=='Advertisement'){
		//oTable.fnClearTable();
			prevdatatable = oTable.dataTable({
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
			"ajax": "/adtax/hoarding/hoarding-search-list?"+$("#hoardingsearchform").serialize(),
			"columns" : [
			              { "data" : "id","visible": false, "searchable": false },
						  { "data" : "advertisementNumber", "title":"Advertisement No."},
						  { "data" : "applicationNumber", "title": "Application No."},
						  { "data" : "applicationFromDate", "title": "Application Date"},
						  { "data" : "agencyName", "title": "Agency"},
						  { "data" : "ownerDetail", "title": "Owner Detail"},
						  { "data" : "pendingDemandAmount", "title": "Amount"},
						  { "data" : "penaltyAmount", "title": "Penalty Amount"},
						  { "data" : "", "title": "Actions","target":-1,"defaultContent": '<button type="button" class="btn btn-xs btn-secondary collect-hoardingWiseFee"><span class="glyphicon glyphicon-edit"></span>&nbsp;Collect</button>&nbsp;'}

						  ],
						  "aaSorting": [[4, 'asc']] 
				});
		} else {
			
			prevdatatable = oTable.dataTable({
				"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6'l><'col-md-1 col-xs-2 text-right'p>>",
				"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
				"autoWidth": false,
				"bDestroy": true,
				"ajax": "/adtax/hoarding/hoarding-search-list?"+$("#hoardingsearchform").serialize(),
				"columns" : [
				              { "data": "hordingIdsSearchedByAgency","visible": false, "searchable": false },
							  { "data" : "agencyName", "title": "Agency"},
					
							  { "data" : "totalHoardingInAgency", "title": "No.of hoarding"},
							  { "data" : "ownerDetail","visible": false},
							  { "data" : "pendingDemandAmount", "title": "Total Amount"},
							  { "data" : "penaltyAmount", "title": "Penalty Amount"},
							  { "data" : "","title": "Actions", "target":-1,"defaultContent": '<button type="button" class="btn btn-xs btn-secondary collect-agencyWiseFee"><span class="glyphicon glyphicon-edit"></span>&nbsp;Collect</button>&nbsp;'}

							  ]
					});
		}
		e.stopPropagation();
	});

	$("#reset").click(function(e){
		$('#agencyId').val("");    
	});
	
	var datatbl = $('#search-update-result-table');
	$('#search-update').click(function(e){
		datatbl.dataTable({
			"ajax": {url:"/adtax/hoarding/findhoarding-for-update?"+$("#hoardingsearchform").serialize(),
				type:"POST"
			},
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"bDestroy": true,
			"autoWidth": false,
			"columns" : [
			  {"data" : "id", "visible": false, "searchable" : false},
		      { "data" : "advertisementNumber", "title":"Advertisement No."},
			  { "data" : "applicationNumber", "title": "Application No."},
			  { "data" : "applicationFromDate", "title": "Application Date"},
			  { "data" : "agencyName", "title": "Agency"},
			  { "data" : "ownerDetail", "title": "Owner Details"},
			  { "data" : "status", "title": "Hoarding Status"},
			  { "data" : "id", "visible": false},
			  { "data" : "", "target":-1,"defaultContent": '<span class="add-padding"><i class="fa fa-edit history-size" class="tooltip-secondary" data-toggle="tooltip" title="Edit"></i></span><span class="add-padding"><i class="fa fa-eye history-size" class="tooltip-secondary" data-toggle="tooltip" title="View"></i></span>'},
			  ]
		});
		e.stopPropagation();
	});
	var datadcbtbl = $('#search-dcbresult-table');
	$('#search-dcb').click(function(e){
		datadcbtbl.dataTable({
			"ajax": {url:"/adtax/reports/search-for-dcbreport?"+$("#hoardingsearchform").serialize(),
				type:"POST"
			},
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"bDestroy": true,
			"autoWidth": false,
			"columns" : [
			  { "data" : "id","visible" : false, "searchable": false},
		      { "data" : "advertisementNumber", "title":"Advertisement No."},
			  { "data" : "applicationNumber", "title": "Application No."},
			  { "data" : "agencyName", "title": "Agency"},
			  { "data" : "ownerDetail", "title": "Owner Details"},
			  { "data" : "status", "title": "Hoarding Status"},
			  { "data" : "","title": "Actions", "target":-1,"defaultContent": '<button type="button" class="btn btn-xs btn-secondary fa-demandCollection"><span class="glyphicon glyphicon-edit"></span>&nbsp;View DCB Report</button>&nbsp;'}			 
			  ]
		});
		e.stopPropagation();
	});
	
	$("#search-dcbresult-table").on('click','tbody tr td .fa-demandCollection',function(e) {
		var hoardingId = datadcbtbl.fnGetData($(this).parent().parent(),0);
		window.open("getHoardingDcb/"+hoardingId, ''+hoardingId+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')
	});
	
	$("#search-update-result-table").on('click','tbody tr td i.fa-edit',function(e) {
		var hoardingId = datatbl.fnGetData($(this).parent().parent().parent(),0);
		window.open("legacyUpdation/"+hoardingId, ''+hoardingId+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')
	});
	
	$("#search-update-result-table").on('click','tbody tr td i.fa-eye',function(e) {
		var hoardingId = datatbl.fnGetData($(this).parent().parent().parent(),0);
		var permitId = datatbl.fnGetData($(this).parent().parent().parent(),6);
		window.open("view/"+permitId, ''+permitId+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')
	});
	
	$("#adtax_search").on('click','tbody tr td .collect-hoardingWiseFee',function(event) {
		var permitId = oTable.fnGetData($(this).parent().parent(),0);
		window.open("generatebill/hoarding/"+permitId, ''+permitId+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')

	});
	
	$("#adtax_search").on('click','tbody tr td .collect-agencyWiseFee',function(event) {
		var hoardingIds = oTable.fnGetData($(this).parent().parent(),0);
		var agencyName = oTable.fnGetData($(this).parent().parent(),1);
		var pendingAmount = oTable.fnGetData($(this).parent().parent(),4); 
		var ownerDetail = oTable.fnGetData($(this).parent().parent(),3); 

		openPopupPage("collectTaxByAgency",agencyName,hoardingIds,pendingAmount,ownerDetail);
		//window.open("collectTaxByAgency/"+agencyName+"/"+hoardingIds+"/"+pendingAmount ,''+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')
	
	});
	
	function openPopupPage(relativeUrl,agencyName,hoardingIds,pendingAmount,ownerDetail)
	{
	 var param = { 'agencyName' : agencyName, 'hoardingIds': hoardingIds ,'total': pendingAmount,'ownerDetail':ownerDetail };
	 OpenWindowWithPost(relativeUrl, "width=1000, height=600, left=100, top=100, resizable=yes, scrollbars=yes", "collectTaxByAgency", param);
	}
	 
	 
	function OpenWindowWithPost(url, windowoption, name, params)
	{
	 var form = document.createElement("form");
	 form.setAttribute("action", url);
	 form.setAttribute("target", name);
	 form.setAttribute("method", "post");
	 for (var i in params)
	 {
	   if (params.hasOwnProperty(i))
	   {
	     var input = document.createElement('input');
	     input.type = 'hidden';
	     input.name = i;
	     input.value = params[i];
	     form.appendChild(input);
	   }
	 }
	 document.body.appendChild(form);
	 window.open("collectTaxByAgency", name, windowoption);
	 form.submit();
	}

		
});

