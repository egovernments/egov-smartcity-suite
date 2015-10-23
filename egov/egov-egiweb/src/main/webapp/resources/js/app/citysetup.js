//tab switch handle
var currenttabidx=0;
var lasttabidx=$('a[data-tabidx]').length-1;

document.body.addEventListener('keydown', function (e) {
	if(document.activeElement.tagName == 'INPUT' || document.activeElement.tagName == 'TEXTAREA' || document.activeElement.tagName == 'SELECT'){
		
	}else if(document.activeElement.tagName == 'BODY' || document.activeElement.tagName == 'A'){
		if((e.which === 37 || e.which === 39))
	    {
	      if(currenttabidx === lasttabidx)
	      {
	    	  if(e.which === 39)
	    	  $('a[data-tabidx="0"]').tab('show');
	    	  else if(e.which === 37)
	    	  $('a[data-tabidx="'+ (currenttabidx-1) +'"]').tab('show');
	      }
	      else
	      {
	    	  if(e.which === 37)
	    	  {
	    		 
	    		  currenttabidx = (currenttabidx === 0 ? lasttabidx : (currenttabidx-1));    		  
	    		  $('a[data-tabidx="'+ currenttabidx +'"]').tab('show');
	    	  }
	    	  else{
	    	    $('a[data-tabidx="'+ (currenttabidx+1) +'"]').tab('show');
	    	  }
	      }
	    }
	}
});

$(document).ready(function(){
	
	$('#code').attr('disabled',true);
	
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
	
	//popup initialize
	$('[data-toggle="popover"]').popover({ html : true });

	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		  currenttabidx=$(this).data('tabidx');
	});
	
});

