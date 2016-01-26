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
		if(prevdatatable)
		{
			prevdatatable.fnClearTable();
			$('#adtax_search thead tr').remove();
		}
			prevdatatable = oTable.dataTable({
			"sPaginationType": "bootstrap",
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
			"ajax": "/adtax/hoarding/search-adtax-result?"+$("#hoardingsearchform").serialize(),
			"columns" : [
						  { "data" : "advertisementNumber", "title":"Advertisement No."},
						  { "data" : "applicationNumber", "title": "Application No."},
						  { "data" : "applicationFromDate", "title": "Application Date"},
						  { "data" : "agencyName", "title": "Agency"},
						  { "data" : "pendingDemandAmount", "title": "Amount"},
						  { "data" : "penaltyAmount", "title": "Penalty Amount"},
						  { "data" : "permissionNumber", "visible": false},
						  { "data" : "permitStatus", "visible": false},
						  {"title" : "Actions","sortable":false,
				        	   render : function(data, type, row) {
				        			   if (undefined != row.permissionNumber && (row.permitStatus=="ADTAXPERMITGENERATED" || row.permitStatus=="ADTAXAMTPAYMENTPAID")) {
				        					   return ('<select class="dropchange" id="adtaxdropdown" ><option>Select from Below</option><option value="2">View</option><option value="0">Generate Permit Order</option><option value="1">Generate Demand Notice</option></select>');   
				        				   }else if(row.permitStatus=="APPROVED"){
				        					   return ('<select class="dropchange" id="adtaxdropdown" ><option>Select from Below</option><option value="2">View</option><option value="1">Generate Demand Notice</option></select>'); 
				        				   }else
				        					   return ('<select class="dropchange" id="adtaxdropdown" ><option>Select from Below</option><option value="2">View</option></select>')
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
			var applicationNumber = oTable.fnGetData($(this).parent().parent(), 1);
			var advertisementNumber = oTable.fnGetData($(this).parent().parent(), 0);
						if (this.value == 0) {
							var url = '/adtax/advertisement/permitOrder/'+ applicationNumber;
							$('#adtaxsearchform').attr('method', 'get');
							$('#adtaxsearchform').attr('action', url);
							window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
						} else if (this.value == 1) {
							var url = '/adtax/advertisement/demandNotice/'+ applicationNumber;
							$('#adtaxsearchform').attr('method', 'get');
							$('#adtaxsearchform').attr('action', url);
							window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
						} else if (this.value == 2) {
							var url = '/adtax/hoarding/viewAdvertisement/'+ applicationNumber;
							$('#adtaxsearchform').attr('method', 'get');
							$('#adtaxsearchform').attr('action', url);
							window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
						}
						
						}); 
});

