//tab switch handle
var currenttabidx=0;
var lasttabidx=1;

document.body.addEventListener('keydown', function (e) {
    if((e.which === 37 || e.which === 39))
    {
      console.log(currenttabidx);
      if(currenttabidx === lasttabidx)
      {
    	  $('a[data-tabidx="0"]').tab('show');
      }
      else
      {
    	  $('a[data-tabidx="'+ (currenttabidx+1) +'"]').tab('show');
      }
    }
});

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
		$('[data-toggle="popover"]').popover('hide');
		$(this).popover();
		setTimeout(function(s) {
			return function() {
				if ($(s).next('div.popover:visible').length)
					$(s).popover('hide');
			}
		}(clickedbtn), 10000);
	});

	//popup initialize
	$('[data-toggle="popover"]').popover();

	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		  currenttabidx=$(this).data('tabidx');
	});
	
});

