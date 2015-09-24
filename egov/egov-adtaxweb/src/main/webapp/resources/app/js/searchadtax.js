$(document).ready(function(){
	
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
		oTable.dataTable({
			"sPaginationType": "bootstrap",
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
			"ajax": "/adtax/hoarding/search-list?"+$("#hoardingsearchform").serialize(),
			"columns" : [
			  { "mData" : "hoardingNumber",
				"sTitle" : "Hoarding No"
			  },
			  { "mData" : "agency",
				"sTitle" : "Agency"
			  },
			  { "mData" : "hoardingId",
				"visible": false
			  }]
				});
		}
		e.stopPropagation();
	});
});

