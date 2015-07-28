$(document).ready(function(){
	
   //file chooser filter validation
   $("input:file").change(
			function(e) {
				var fileName = $(this).val();
				if (fileName) {
					var fileext = fileName.split(".");
					var acceptedext = $(this).data('accept');
					if (acceptedext.split(',').indexOf(
							fileext[fileext.length - 1]) < 0) {
						alert($(this).data('errormsg'));
						$(this).val('');
					}
					else if($(this).attr('id')== 'logo')
					{
						var reader = new FileReader();

			            reader.onload = function (e) {
			                $('#imglogo').attr('src', e.target.result);
			            }

			            reader.readAsDataURL(this.files[0]);
			        }
				}
				else {
					if($(this).attr('id')== 'logo')
					{
						$('#imglogo').attr('src', '');
					}
				}
			
	});

    //help button popup view timeout event handling in button click event
	$('.btnpopover').click(function(e) {
		var clickedbtn = $(this);
		setTimeout(function(s) {
			return function() {
				if ($(s).next('div.popover:visible').length)
					$(s).popover('hide');
			}
		}(clickedbtn), 10000);
	});

	//popup initialize
	$('[data-toggle="popover"]').popover();
	
});