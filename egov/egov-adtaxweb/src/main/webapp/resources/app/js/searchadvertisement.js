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
		if(prevdatatable)
		{
			prevdatatable.fnClearTable();
			$('#adtax_search thead tr').remove();
		}
			prevdatatable = oTable.dataTable({
			"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
			"oTableTools" : {
				"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
				"aButtons" : [ 
		               {
			             "sExtends": "pdf",
	                     "sPdfMessage": "",
	                     "sTitle": "Advertisement Tax Search Result",
	                     "sPdfOrientation": "landscape",
	                     "mColumns": [0, 1, 2, 3, 4, 5, 6, 7]	 
		                },
		                {
				             "sExtends": "xls",
	                         "sTitle": "Advertisement Tax Search Result",
	                         "mColumns": [0, 1, 2, 3, 4, 5, 6, 7]
			             },
			             {
				             "sExtends": "print",
	                         "sTitle": "Advertisement Tax Search Result",
	                         "mColumns": [0, 1, 2, 3, 4, 5, 6, 7]
			             }]
				
			},
			"ajax": "/adtax/hoarding/getsearch-adtax-result?"+$("#adtaxsearchform").serialize(),
			"columns" : [
			              { "data" : "agencyName", "title": "Agency"},
			              { "data" : "ownerDetail", "title": "Owner Detail"},
						  { "data" : "advertisementNumber", "title":"Advertisement No."},
						  { "data" : "applicationNumber", "title": "Application No."},
						  { "data" : "applicationFromDate", "title": "Application Date"},
						  { "data" : "pendingDemandAmount", "title": "Amount"},
						  { "data" : "additionalTaxAmount", "title": "Additional Tax (Service Tax and Cesses)"},
						  { "data" : "penaltyAmount", "title": "Penalty Amount"},
						  { "data" : "totalAmount", "title": "Total Amount"},
						  { "data" : "permissionNumber", "visible": false},
						  { "data" : "permitStatus", "visible": false},
						  { "data" : "id", "visible": false},
						  { "data" : "isLegacy", "visible":false},
						  {"title" : "Actions","sortable":false,
				        	   render : function(data, type, row) {
				        		   
				        		   	 if(row.permitStatus=="APPROVED"){
				        		   		 if(row.totalAmount==0){
				        		   			 return ('<select class="dropchange" id="adtaxdropdown" ><option>Select from Below</option><option value="0">Generate Permit Order</option><option value="2">View</option></select>');
				        		   		 }
				        		   		 else{
			        					   return ('<select class="dropchange" id="adtaxdropdown" ><option>Select from Below</option><option value="2">View</option><option value="1">Generate Demand Notice</option></select>');
				        		   		 }
				        		   	  } 
				        		   	 else if(row.permitStatus=="ADTAXAMTPAYMENTPAID" || row.permitStatus=="ADTAXPERMITGENERATED"){
				        		   		  if(row.isLegacy==true && row.totalAmount==0){
				        		   			return ('<select class="dropchange" id="adtaxdropdown" ><option>Select from Below</option><option value="0">Generate Permit Order</option><option value="2">View</option></select>'); 
				        		   		  }
				        		   		  else if(row.isLegacy==true && row.totalAmount!=0){
				        		   			 return ('<select class="dropchange" id="adtaxdropdown" ><option>Select from Below</option><option value="0">Generate Permit Order</option><option value="1">Generate Demand Notice</option><option value="2">View</option></select>');
				        		   		  }
				        		   		  else
				        		   			  {
					        		   			return ('<select class="dropchange" id="adtaxdropdown" ><option>Select from Below</option><option value="0">Generate Permit Order</option><option value="2">View</option></select>'); 
				        		   			  }
			        				  } 
				        		   	 else
			        					   return ('<select class="dropchange" id="adtaxdropdown" ><option>Select from Below</option><option value="2">View</option></select>');
				        		   		
				        			   }}],
						  "aaSorting": [[4, 'asc']] 
				});
		e.stopPropagation();
	});

	$("#reset").click(function(e){
		$('#agencyId').val("");    
	});

	$("#adtax_search").on('change','tbody tr td .dropchange',
			function() {
			//var applicationNumber = oTable.fnGetData($(this).parent().parent(), 1);
			var adtaxid= oTable.fnGetData($(this).parent().parent(), 11);
			//var advertisementNumber = oTable.fnGetData($(this).parent().parent(), 0);
						if (this.value == 0) {
							var url = '/adtax/advertisement/permitOrder/'+ adtaxid;
							$('#adtaxsearchform').attr('method', 'get');
							$('#adtaxsearchform').attr('action', url);
							window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
						} else if (this.value == 1) {
							var url = '/adtax/advertisement/demandNotice/'+ adtaxid;
							$('#adtaxsearchform').attr('method', 'get');
							$('#adtaxsearchform').attr('action', url);
							window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
						} else if (this.value == 2) {
							var url = '/adtax/hoarding/view/'+ adtaxid;
							$('#adtaxsearchform').attr('method', 'get');
							$('#adtaxsearchform').attr('action', url);
							window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
						}
						
						}); 
	
	$('#renewalsearch').click(function(e){
		oTable= $('#renew_search');
		if(prevdatatable)
		{
			prevdatatable.fnClearTable();
			$('#adtax_search thead tr').remove();
		}
			prevdatatable = oTable.dataTable({
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
			"ajax": "/adtax/hoarding/renewl-search-result?"+$("#renewalsearchform").serialize(),
			"columns" : [
						  { "data" : "advertisementNumber", "title":"Advertisement No."},
						  { "data" : "categoryName", "title": "Category"},
						  { "data" : "subCategoryName", "title": "Sub Category"},
						  { "data" : "agencyName", "title": "Agency"},
						  { "data" : "ownerDetail", "title": "Owner"},
						  { "data" : "financialYear", "title": "Financial Year"},
						  { "data" : "id", "visible": false},
						  {"title" : "Actions","sortable":false,
				        	   render : function(data, type, row) {
return ('<select class="dropchange" id="renewdropdown" ><option>Select from Below</option><option value="0">Adtax Renewal</option></select>');   
				        			   }}],
						  "aaSorting": [[1, 'asc']] 
				});
		e.stopPropagation();
	});

	
	$("#renew_search").on('change','tbody tr td .dropchange',
			function() {
			var adtaxid= oTable.fnGetData($(this).parent().parent(), 6);
						if (this.value == 0) {
							var url = '/adtax/advertisement/renewal/'+ adtaxid;
							$('#renewalsearchform').attr('method', 'get');
							$('#renewalsearchform').attr('action', url);
							window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
						}
						}); 
	
});

