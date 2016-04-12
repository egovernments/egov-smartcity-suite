
function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

function callAjaxSearch() {
	var fd=jQuery('#mrform').serialize();
	  jQuery.ajax({
			url: "/EGF/brs/manualReconciliation-ajaxSearch.action",
			type: "POST",
			data: fd,
			//dataType: "text",
			success: function (response) {
				console.log("success"+response );
				 $('#resultDiv').html(response);
				
			}, 
			error: function (response) {
				console.log("failed");
				
				bootbox.alert("Failed to search Details");
			}
		});
}