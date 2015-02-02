jQuery(document).ready(function($)
{
	var mySource = [{ id: 1, name: 'Terry Terry Terry Terry Terry Terry'}, { id: 2, name: 'Mark'}, { id: 3, name: 'Jacob'}];
	
	$('#com_type, #clocation').typeahead({
		source: mySource
	});
	
	 $(":input").inputmask();
	
	$("#f-name").on("input", function(){
		var regexp = /[^a-zA-Z]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
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