$(document).ready(function() {
	$('#btnsubmit').on('click', function(event) {
		var form = $('#taxExemptionForm').serialize();
		$.ajax({
			url : '/ptis/exemption/save',
			type : 'POST',
			data : form,
			success : function() {

			},
			error : function() {

			}
		});
	});
});