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
	
	/*$('#revenueinspector').change(function(){
		$("#revenueInspectorId").val($('#revenueinspector').val());    
	});*/
	
	$('#categories').change(function(){
		$.ajax({
			url: "/adtax/hoarding/subcategory-by-category",    
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
});