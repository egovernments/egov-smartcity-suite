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
	
	var isSubmitForm=false;
	$("#deactivation").click(function(e){
		if(isSubmitForm)
		{
			return true;
		}
		if(!validateDate()){
			document.getElementById("deactiveDate").value="";
			document.getElementById("deactiveDate").focus(); 
			return false;
		}
		var pendingTax= $('#ptax').html();
		var id=document.getElementById("id").value;

		if($('#statusdeactivateform').valid()){
			if(parseInt(pendingTax)>0)
			{
				bootbox.confirm("You Have a Pending Tax Of Rupees "+pendingTax+". Do You Want To Continue Deactivation?", function(result){
					if(result)
						{
							var action = '/adtax/deactivate/deactive/' + id;
							$('#statusdeactivateform').attr('method','post');
							$('#statusdeactivateform').attr('action',action);
							isSubmitForm=true;
							$("#deactivation").trigger("click");
						}
				});
				return false;
			}else
				{
					var action = '/adtax/deactivate/deactive/' + id;
					$('#statusdeactivateform').attr('method','post');
					$('#statusdeactivateform').attr('action',action);
					return true;
				}
		}else{
			return false;
			
		}
	});
	
});	

var prevdatatable;
$('#searchrecord').click(function(e){

		oTable= $('#adtax_searchrecord');
		if(prevdatatable)
		{
			prevdatatable.fnClearTable();
			$('#adtax_searchrecord thead tr').remove();
		}
		
			prevdatatable = oTable.dataTable({
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
			"ajax": "/adtax/deactivate/activerecord-list-search?"+$("#activehoardingsearchform").serialize(),
			"columns" : [
						  { "data" : "advertisementNumber", "title":"Advertisement No."},
						  { "data" : "applicationNumber", "title": "Application No."},
						  { "data" : "applicationFromDate", "title": "Application Date"},
						  { "data" : "agencyName", "title": "Agency"},
						  { "data" : "ownerDetail", "title": "Owner Detail"},
						  { "data" : "pendingDemandAmount", "title": "Amount"},
						  { "data" : "additionalTaxAmount", "title": "Additional Tax (Service Tax and Cesses)"},
						  { "data" : "penaltyAmount", "title": "Penalty Amount"},
						  { "data" : "status", "title": "Hoarding Status"},
						  
						  { 
							  "data" : "id",
							  "render" : function(data, type, row, meta) {
									return '<button class="btn btn-primary" onclick="window.open(\'/adtax/deactivate/result/'+ data +'\', \'\', \'width=800, height=600 , scrollbars=yes\');"> Deactivate </button>';
							   },
							   "title": "Actions"
						  }
							  
						  ],
						  "aaSorting": [[4, 'asc']] 
					});
		e.stopPropagation();
		e.preventDefault();

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
	
	function validateDate(){
		var application_date = document.getElementById("applicationDate").value;
		var deactivation_date = document.getElementById("deactiveDate").value;
		var a = compareDate(application_date, deactivation_date);
		if(a==-1){
			bootbox.alert(" The deactivation date should come after the application date : "+ document.getElementById("applicationDate").value);
			return false;
		}
		else{
			return true;
		}
	}
	
