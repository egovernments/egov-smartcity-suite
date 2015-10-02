
$(document).ready(function()
{
$('#licenseCategory').change(function(){
		console.log("came onchange of "+$('#licenseCategory').val());
		$.ajax({
			url: "/tl/domain/commonAjax-ajaxPopulateSubCategory.action?categoryId="+$('#licenseCategory').val()+"",
			type: "GET",
			dataType: "json",
			success: function (response) {
				console.log("success"+JSON.stringify(response) );
				$('#subCategory').empty();
				
				$('#subCategory').append($("<option value=''>Select</option>"));
				$.each(response.Result, function(index, value) {
					
				     $('#subCategory').append("<option value="+value.Value+">"+value.Text+"</option>");
				});
				console.log("completed"+response);
				
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	});

$( "#search" ).click(function( event ) {
	var valid = $('#feematrix-new').validate().form()
	if(!valid)
		{
		alert("Please fill mandatory fields");
		return false;
		}
	var r = confirm("This will clear all data from search result and fetch the data for selected combination.Press OK to Continue. ");
	if (r != true) {
		return false;
	} 
	  var param="uniqueNo=";
	  param=param+$('#natureOfBusiness').val()+"-";
	  param=param+$('#licenseAppType').val()+"-";
	  param=param+$('#licenseCategory').val()+"-";
	  param=param+$('#subCategory').val()+"-";
	  param=param+$('#feeType').val()+"-";
	  param=param+$('#unitOfMeasurement').val();
	//  alert(param);
	   $.ajax({
			url: "/tl/feematrix/search?"+param,
			type: "GET",
			//dataType: "json",
			success: function (response) {
				console.log("success"+response );
				 $('#resultdiv').html(response);
				 datepicker();
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	 
});
});

$( "#add-row" ).click(function( event ) {
	
	  alert( "add-row event called." );
	  $(this).closest("tr").remove(); // remove row
	    return false;
});

$('body').on('click', '#add-row', function() {
	var rowCount = $('#result tr').length;
	var content= $('#resultrow0').html();
	resultContent=   content.replace(/0/g,rowCount-1);
	$(resultContent).find("input").val("");
	$('#result > tbody:last').append("<tr>"+resultContent+"</tr>");
	$('#result tr:last').find("input").val("");
	datepicker();
});

$('body').on('click', '#save', function() {
    // do something
	var fd=$('#feematrix-new').serialize();
	 alert( "save event called." );
	  $.ajax({
			url: "/tl/feematrix/create",
			type: "POST",
			data: fd,
			//dataType: "text",
			success: function (response) {
				console.log("success"+response );
				 $('#resultdiv').html(response);
				 datepicker();
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
});

//datepicker();

function datepicker(){
	$(".datepicker").datepicker({
		format: "dd/mm/yyyy",
		autoclose: true 
	}); 
	patternvalidation(); 
	$(".is_valid_number").on("input", function(){
        var regexp = /[^0-9]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	$(".is_valid_alphabetWithsplchar").on("input", function(){
		var regexp = /[^A-Z_-]*$/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	$(".is_valid_alphabet").on("input", function(){
		var regexp = /[^a-zA-Z ]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	
	$(".is_valid_alphaNumWithsplchar").on("input", function(){
		var regexp =  /[^a-zA-Z0-9_@./#&+-]*$/;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	
	$(".is_valid_alphanumeric").on("input", function(){
		var regexp = /[^a-zA-Z _0-9]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	$(".is_valid_letters_space_hyphen_underscore").on("input", function(){
        var regexp = /[^a-zA-Z _0-9_-]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	try { $('.twitter-typeahead').css('display','block'); } catch(e){}
	
	try { $(":input").inputmask(); }catch(e){}
}





