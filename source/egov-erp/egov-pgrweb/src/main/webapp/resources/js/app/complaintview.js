$(document).ready(function()
{
	$('#toggleactions').click(function(){
		if($(this).html()== "More..")
		{
			$(this).html('Less..');
			$('.show-action-more').show();
		}else
		{
			$(this).html('More..');
			$('.show-action-more').hide();
		}
		
	});
	
	$("#btn_submit").click(function(){
		if($('#inc_messge').val() == '')
		{
			$('#inc_messge').addClass('error');
		}else
		{
			document.forms[0].submit();
			//alert("Submitted Successfully");
			$('#inc_messge').removeClass('error');
		}
		
	});
	
	$('.slide-history-menu').click(function(){
		$('.history-slide').slideToggle();
		if($('#toggle-his-icon').hasClass('entypo-down-open'))
		{
			$('#toggle-his-icon').removeClass('entypo-down-open').addClass('entypo-up-open');
			$('#see-more-link').hide();
			}else{
			$('#toggle-his-icon').removeClass('entypo-up-open').addClass('entypo-down-open');
			$('#see-more-link').show();
		}
	});
	
	$('#ct-sel-jurisd').change(function(){
		console.log("came jursidiction"+$('#ct-sel-jurisd').val());
		$.ajax({
			url: "/pgr/ajax-getWards/"+$('#ct-sel-jurisd').val(),
			type: "GET",
			data: {
				zone : $('#ct-sel-jurisd').val()
			},
			dataType: "json",
			success: function (response) {
				console.log("success"+response);
				$('#location').empty();
				
				$('#location').append($("<option value=''>Select</option>"));
				$.each(response, function(index, value) {
					
				     $('#location').append($('<option>').text(value.bndryNameLocal).attr('value', value.id));
				});
				
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	});
	
	$('#approvalDepartment').change(function(){
		//alert("came approvalDepartment");
		$.ajax({
			url: "/pgr/ajax-approvalDesignations",     
			type: "GET",
			data: {
				approvalDepartment : $('#approvalDepartment').val()   
			},
			dataType: "json",
			success: function (response) {
				console.log("success"+response);
				$('#approvalDesignation').empty();
				//alert(document.getElementById('approvalDesignation'));
				$('#approvalDesignation').append($("<option value=''>Select</option>"));
				$.each(response, function(index, value) {
					$('#approvalDesignation').append($('<option>').text(value.designationName).attr('value', value.designationId));
				});
				
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	});
	
	$('#approvalDesignation').change(function(){
		$.ajax({
			url: "/pgr/ajax-approvalPositions",     
			type: "GET",
			data: {
				approvalDesignation : $('#approvalDesignation').val(),
				approvalDepartment : $('#approvalDepartment').val()    
			},
			dataType: "json",
			success: function (response) {
				console.log("success"+response);
				$('#approvalPosition').empty();
				$('#approvalPosition').append($("<option value=''>Select</option>"));
				$.each(response, function(index, value) {
					$('#approvalPosition').append($('<option>').text(value.username).attr('value', value.id));
				});
				
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	});
	
});
