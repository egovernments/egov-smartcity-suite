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
			url: "/adtax/hoarding/subcategories-by-category",    
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
			"sPaginationType": "bootstrap",
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
			"ajax": "/adtax/hoarding/search-list?"+$("#hoardingsearchform").serialize(),
			"columns" : [
			              { "data" : "id","visible": false, "searchable": false },
						  { "data" : "advertisementNumber", "title":"Advertisement No."},
						  { "data" : "applicationNumber", "title": "Application No."},
						  { "data" : "applicationFromDate", "title": "Application Date"},
						  { "data" : "agencyName", "title": "Agency"},
						  { "data" : "pendingDemandAmount", "title": "Amount"},
						  { "data" : "penaltyAmount", "title": "Penalty Amount"},
						  { "data" : "", "title": "Actions","target":-1,"defaultContent": '<button type="button" class="btn btn-xs btn-secondary collect-hoardingWiseFee"><span class="glyphicon glyphicon-edit"></span>&nbsp;Collect</button>&nbsp;'}

						  ],
						  "aaSorting": [[4, 'asc']] 
				});
		} else {
			
			prevdatatable = oTable.dataTable({
				"sPaginationType": "bootstrap",
				"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6'l><'col-md-1 col-xs-2 text-right'p>>",
				"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
				"autoWidth": false,
				"bDestroy": true,
				"ajax": "/adtax/hoarding/search-list?"+$("#hoardingsearchform").serialize(),
				"columns" : [
				              { "data": "hordingIdsSearchedByAgency","visible": false, "searchable": false },
							  { "data" : "agencyName", "title": "Agency"},
							  { "data" : "totalHoardingInAgency", "title": "No.of hoarding"},
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
			"ajax": {url:"/adtax/hoarding/search-for-update?"+$("#hoardingsearchform").serialize(),
				type:"POST"
			},
			"sPaginationType": "bootstrap",
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
			"sPaginationType": "bootstrap",
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"bDestroy": true,
			"autoWidth": false,
			"columns" : [
			  { "data" : "id","visible" : false, "searchable": false},
		      { "data" : "advertisementNumber", "title":"Advertisement No."},
			  { "data" : "applicationNumber", "title": "Application No."},
			  { "data" : "agencyName", "title": "Agency"},
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
		window.open("updateLegacy/"+hoardingId, ''+hoardingId+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')
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
		var pendingAmount = oTable.fnGetData($(this).parent().parent(),3); 

		window.open("collectTaxByAgency/"+agencyName+"/"+hoardingIds+"/"+pendingAmount ,''+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')
	
	});
	
});

