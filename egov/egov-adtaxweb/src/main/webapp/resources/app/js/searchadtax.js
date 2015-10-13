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
	
   agency.initialize(); // Instantiate the Typeahead UI
	
	$('.typeahead').typeahead({
		  hint: true,
		  highlight: true,
		  minLength: 1
		}, {
		displayKey: 'name',
		source: agency.ttAdapter()
	}).on('typeahead:selected typeahead:autocompleted typeahead:matched', function(event, data){
		$("#agencyId").val(data.value);    
   });
   
	$('#subcategories').change(function(){
		$("#subCategoryId").val($('#subcategories').val());    
	});
	
	$('#zoneList').change(function(){
		$.ajax({
			url: "/egi/wards-by-zone",     
			type: "GET",
			data: {
				zoneId: $('#zoneList').val()  
			},
			dataType: "json",
			success: function (response) {
				console.log("success"+response);
				$("#zoneId").val($('#zoneList').val());    
				$('#wardlist').empty();
				$('#wardlist').append($("<option value=''>Select from below</option>"));
				$.each(response, function(index, value) {
					$('#wardlist').append($('<option>').text(value.name).attr('value', value.id))
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
				$("#category").val($('#categories').val());    
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
	
	$('#search').click(function(e){
		oTable= $('#adtax_search');
		 var radioValue = $("input[name='searchType']:checked").val();
		 var radioBtnVal = radioValue.replace(/^"?(.+?)"?$/,'$1'); 

		if(radioBtnVal=='hoarding'){
	//		oTable.dataTable().clear();
		oTable.dataTable({
			"sPaginationType": "bootstrap",
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
			"ajax": "/adtax/hoarding/search-list?"+$("#hoardingsearchform").serialize(),
			"columns" : [
						  { "data" : "hoardingNumber", "title":"Hoarding No."},
						  { "data" : "applicationNumber", "title": "Application No."},
						  { "data" : "applicationFromDate", "title": "Application Date"},
						  { "data" : "agencyName", "title": "Agency"},
						  { "data" : "pendingDemandAmount", "title": "Amount"},
						  { "data" : "penaltyAmount", "title": "Penalty Amount"},
						  { "data" : null, "target":-1,"defaultContent": '<button type="button" class="btn btn-xs btn-secondary collect-hoardingWiseFee"><span class="glyphicon glyphicon-edit"></span>&nbsp;Collect</button>&nbsp;'}

						  ],
						  "aaSorting": [[2, 'desc']] 
				});
		} else {
			//oTable.dataTable().clear();
			oTable.dataTable({
				"sPaginationType": "bootstrap",
				"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
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
							  { "data" : "status", "title": "Hoarding Status"}	,
							  { "data" : null, "target":-1,"defaultContent": '<button type="button" class="btn btn-xs btn-secondary collect-agencyWiseFee"><span class="glyphicon glyphicon-edit"></span>&nbsp;Collect</button>&nbsp;'}

							  ],
							  "aaSorting": [[2, 'desc']]
					});
		}
		e.stopPropagation();
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
		      { "data" : "hoardingNumber", "title":"Hoarding No."},
			  { "data" : "applicationNumber", "title": "Application No."},
			  { "data" : "applicationFromDate", "title": "Application Date"},
			  { "data" : "agencyName", "title": "Agency"},
			  { "data" : "status", "title": "Hoarding Status"},
			  { "data" : null, "target":-1,"defaultContent": '<span class="add-padding"><i class="fa fa-edit history-size" class="tooltip-secondary" data-toggle="tooltip" title="Edit"></i></span><span class="add-padding"><i class="fa fa-eye history-size" class="tooltip-secondary" data-toggle="tooltip" title="View"></i></span>'},
			  ],
			  "aaSorting": [[2, 'desc']]
		});
		e.stopPropagation();
	});
	$("#search-update-result-table").on('click','tbody tr td i.fa-edit',function(e) {
		var hoardingNo = datatbl.fnGetData($(this).parent().parent().parent(),0);
		window.open("update/"+hoardingNo, ''+hoardingNo+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')
	});
	
	
	$("#search-update-result-table").on('click','tbody tr td i.fa-eye',function(e) {
		var hoardingNo = datatbl.fnGetData($(this).parent().parent().parent(),0);
		window.open("view/"+hoardingNo, ''+hoardingNo+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')
	});
	
	$("#adtax_search").on('click','tbody tr td .collect-hoardingWiseFee',function(event) {
		var hoardingNo = oTable.fnGetData($(this).parent().parent(),0);
		window.open("generatebill/hoarding/"+hoardingNo, ''+hoardingNo+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')

	});
	
	$("#adtax_search").on('click','tbody tr td .collect-agencyWiseFee',function(event) {
		var hoardingIds = oTable.fnGetData($(this).parent().parent(),0);
		var agencyName = oTable.fnGetData($(this).parent().parent(),1);
		var pendingAmount = oTable.fnGetData($(this).parent().parent(),3); 

		window.open("collectTaxByAgency/"+agencyName+"/"+hoardingIds+"/"+pendingAmount ,''+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')
	
	  
	});
	
});

