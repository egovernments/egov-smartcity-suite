jQuery(document).ready(function($)
{
	/*complaint through*/
	$('input:radio[name="compthr"]').click(function(e) {
		if($('#pform').is(':checked'))
		{
			$('#recenter, #regnoblock').show();
		}else
		{
			$('#recenter, #regnoblock').hide();
		}
	});
	
	$('#doc').bind('input propertychange', function() {
		var remchar = parseInt(400 - ($('#doc').val().length));
		$('#rcc').html('Remaining Characters : '+remchar);
		
	});
	
	
});